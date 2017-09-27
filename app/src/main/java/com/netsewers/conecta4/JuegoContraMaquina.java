package com.netsewers.conecta4;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class JuegoContraMaquina extends AppCompatActivity {

    private short turnoJugador;
    private short[][] matriz;
    private Button[][] matrizBotones;
    private Button botonPausar;
    private TextView tvTurno;
    private long tiempoInicio;
    private long duracionPartida;
    private String nombreJugador1;
    private String nombreJugador2;
    private SpannableString textJugador1;
    private SpannableString textJugador2;
    private SpannableString textTurno;
    private SpannableStringBuilder ssbTurnoJug1;
    private SpannableStringBuilder ssbTurnoJug2;
    private MediaPlayer mediaPlayer;
    private Comprobador comprobador;
    private InteligenciaArtificial misterMachine;
    private final int modoPartida = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        //Inicializa componentes
        inicializarMatriz();
        comprobador = new Comprobador(matriz);
        misterMachine = new InteligenciaArtificial(matriz);
        inicializarMatrizBotones();
        botonPausar = (Button)findViewById(R.id.botonPausar);
        tvTurno = (TextView)findViewById(R.id.tvTurno);

        //obtiene el bundle del intent
        Bundle bundle = getIntent().getExtras();
        //si se ha iniciado la actividad a partir de reanudar partida
        if (bundle.getBoolean("pausada", true)) {
            //carga la partida guardada
            cargarPartida();
        }
        //si es una partida nueva
        else {
            //obtiene los nombres de los jugadores
            nombreJugador1 = bundle.getString("jugador1");
            nombreJugador2 = "M1st3rM4ch1n3";
            //establece el turno al jugador 1
            turnoJugador = 1;
            //inicializa la duracion de partida a 0
            duracionPartida = 0;
        }

        //codigo para establecer los colores de los mensajes de turno
        ssbTurnoJug1 = new SpannableStringBuilder();
        ssbTurnoJug2 = new SpannableStringBuilder();

        textTurno = new SpannableString("Turno: ");
        textTurno.setSpan(new ForegroundColorSpan(Color.WHITE),0,6,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ssbTurnoJug1.append(textTurno);
        ssbTurnoJug2.append(textTurno);

        textJugador1 = new SpannableString(nombreJugador1);
        textJugador1.setSpan(new ForegroundColorSpan(Color.RED),0,textJugador1.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ssbTurnoJug1.append(textJugador1);

        textJugador2 = new SpannableString(nombreJugador2);
        textJugador2.setSpan(new ForegroundColorSpan(Color.GREEN),0,textJugador2.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ssbTurnoJug2.append(textJugador2);

        //configura el texto del Textview con el mensaje de turno correspondiente
        tvTurno.setText((turnoJugador==1?ssbTurnoJug1:ssbTurnoJug2), TextView.BufferType.SPANNABLE);

        //inicia el reproductor de musica
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.valk);
        mediaPlayer.start();
        //obtiene el tiempo en milisegundos en el que se inicio la partida
        tiempoInicio = System.currentTimeMillis();
    }

    /**
     * metodo para cambiar de turno
     */
    public void cambioTurno() {

        //si el turno actual es del jugador 1
        if (turnoJugador == 1) {
            //cambia el turno al jugador 2
            turnoJugador = 2;
            //cambia el texto en el TextView por el mensaje de turno del jugador 2
            tvTurno.setText(ssbTurnoJug2, TextView.BufferType.SPANNABLE);
        }
        //si el turno actual es del jugador 2
        else {
            //cambia el turno al jugador 1
            turnoJugador = 1;
            //cambia el texto en el TextView por el mensaje de turno del jugador 1
            tvTurno.setText(ssbTurnoJug1, TextView.BufferType.SPANNABLE);
        }

    }

    public void pulsarCol1(View v) {
        pulsarColumna(0);
    }

    public void pulsarCol2(View v) {
        pulsarColumna(1);
    }

    public void pulsarCol3(View v) {
        pulsarColumna(2);
    }

    public void pulsarCol4(View v) {
        pulsarColumna(3);
    }

    public void pulsarCol5(View v) {
        pulsarColumna(4);
    }

    public void pulsarCol6(View v) {
        pulsarColumna(5);
    }

    public void pulsarCol7(View v) {
        pulsarColumna(6);
    }

    /**
     * Metodo al que se llama al pulsar en cualquiera de las columnas
     * @param numColumna numero de la columna pulsada
     */
    private void pulsarColumna(int numColumna) {
        boolean isColocada;

        //intenta colocar la ficha en la columna indicada
        isColocada = colocarFicha((short)1,numColumna);
        //si la ficha fue colocada
        if (isColocada) {
            //deshabilita los botones para evitar eventos durante el procesamiento
            habilitarBotones(false);
            //comprueba si hay un ganador
            switch (comprobador.comprobarGanador()) {
                //si no hay ganador
                case 0:
                    //si el panel esta lleno
                    if (comprobador.isPanelLleno()) {
                        //termina la partida por empate
                        gameOver(0);
                        //se usan sentencias return para solucionar un bug en el que
                        //es posible seguir insertando fichas en el instante antes de cargar
                        //la pantalla game over
                        return;
                    }
                    break;
                //si gana el jugador 1
                case 1:
                    gameOver(1);
                    return;
            }
            //cambia de turno
            cambioTurno();
            //la maquina hace su movimiento
            boolean terminaJuego = movimientoMaquina();
            //si la maquina ha ganado
            if (terminaJuego)
                return;
            //cambia de turno
            cambioTurno();

            //habilita los botones
            habilitarBotones(true);
        }
    }

    public boolean movimientoMaquina() {
        //calcula la columna donde insertar la ficha
        int colInsertar = misterMachine.calcularDondeInsertar();
        //inserta la ficha en la columna calculada
        colocarFicha((short)2, colInsertar);
        //actualiza las alturas de las columnas
        misterMachine.actualizarAlturas();

        boolean terminaJuego = false;
        //comprueba si hay un ganador
        switch (comprobador.comprobarGanador()) {
            //si no hay ganador
            case 0:
                //si el panel esta lleno
                if (comprobador.isPanelLleno()) {
                    //termina la partida por empate
                    gameOver(0);
                    //indicamos que la partida termina con un valor booleano
                    //para poder usar return en el metodo pulsarColumna
                    //y evitar el bug de inserción de fichas
                    terminaJuego = true;
                }
                break;
            //si gana la maquina
            case 2:
                gameOver(2);
                terminaJuego = true;
                break;
        }

        return terminaJuego;
    }

    /**
     * Coloca la ficha en el tablero
     * @param fichaJugador numero que indica el jugador que inserta la ficha
     * @param numColumna numero de columna donde insertar la ficha
     * @return true si la ficha fue colocada, false si no se pudo colocar la ficha (columna llena)
     */
    public boolean colocarFicha(short fichaJugador, int numColumna) {
        boolean isColocada = false;
        //bucle para recorrer la columna en busca de una casilla vacia
        for (int i=matriz.length-1; i>=0; i--) {
            //si encuentra una casilla vacia
            if (matriz[i][numColumna] == 0) {
                //actualiza el valor de la casilla en la matriz con el valor de la ficha de jugador
                matriz[i][numColumna] = fichaJugador;
                //si la ficha es del jugador 1
                if (fichaJugador == 1)
                    //actualiza el boton de la casilla con la imagen de una ficha roja
                    matrizBotones[i][numColumna].setBackgroundResource(R.drawable.casilla_rojo);
                //si la ficha es de la maquina
                else
                    //actualiza el boton de la casilla con la imagen de una ficha verde
                    matrizBotones[i][numColumna].setBackgroundResource(R.drawable.casilla_verde);
                isColocada = true;
                break;
            }
        }
        return isColocada;
    }

    /**
     * Inicializa la matriz
     */
    public void inicializarMatriz() {
        matriz = new short[6][7];

        for (int i=0; i<matriz.length; i++) {
            for (int j=0; j<matriz[i].length; j++) {
                matriz[i][j] = 0;
            }
        }

    }

    /**
     * Inicializa la matriz de botones
     */
    public void inicializarMatrizBotones() {
        matrizBotones = new Button[][]
                {{(Button)findViewById(R.id.buttonA1),(Button)findViewById(R.id.buttonA2),
                        (Button)findViewById(R.id.buttonA3),(Button)findViewById(R.id.buttonA4),
                        (Button)findViewById(R.id.buttonA5),(Button)findViewById(R.id.buttonA6),
                        (Button)findViewById(R.id.buttonA7)},
                        {(Button)findViewById(R.id.buttonB1),(Button)findViewById(R.id.buttonB2),
                                (Button)findViewById(R.id.buttonB3),(Button)findViewById(R.id.buttonB4),
                                (Button)findViewById(R.id.buttonB5),(Button)findViewById(R.id.buttonB6),
                                (Button)findViewById(R.id.buttonB7)},
                        {(Button)findViewById(R.id.buttonC1),(Button)findViewById(R.id.buttonC2),
                                (Button)findViewById(R.id.buttonC3),(Button)findViewById(R.id.buttonC4),
                                (Button)findViewById(R.id.buttonC5),(Button)findViewById(R.id.buttonC6),
                                (Button)findViewById(R.id.buttonC7)},
                        {(Button)findViewById(R.id.buttonD1),(Button)findViewById(R.id.buttonD2),
                                (Button)findViewById(R.id.buttonD3),(Button)findViewById(R.id.buttonD4),
                                (Button)findViewById(R.id.buttonD5),(Button)findViewById(R.id.buttonD6),
                                (Button)findViewById(R.id.buttonD7)},
                        {(Button)findViewById(R.id.buttonE1),(Button)findViewById(R.id.buttonE2),
                                (Button)findViewById(R.id.buttonE3),(Button)findViewById(R.id.buttonE4),
                                (Button)findViewById(R.id.buttonE5),(Button)findViewById(R.id.buttonE6),
                                (Button)findViewById(R.id.buttonE7)},
                        {(Button)findViewById(R.id.buttonF1),(Button)findViewById(R.id.buttonF2),
                                (Button)findViewById(R.id.buttonF3),(Button)findViewById(R.id.buttonF4),
                                (Button)findViewById(R.id.buttonF5),(Button)findViewById(R.id.buttonF6),
                                (Button)findViewById(R.id.buttonF7)}};
    }

    /**
     * Metodo utilizado para habilitar o desabilitar los botones del juego
     * @param estado true para habilitar, false para deshabilitar
     */
    public void habilitarBotones(boolean estado) {
        for (int i = 0; i < matrizBotones.length; i++) {
            for (int j = 0; j < matrizBotones[i].length; j++)
                matrizBotones[i][j].setEnabled(estado);
        }
        botonPausar.setEnabled(estado);
    }

    /**
     * metodo que termina la partida
     * @param jugadorGanador numero que identifica al jugador ganador, o empate en caso de que el valor sea 0
     */
    public void gameOver(int jugadorGanador) {
        //para la musica
        mediaPlayer.release();
        //calcula la duracion de la partida
        duracionPartida += System.currentTimeMillis() - tiempoInicio;

        //Hace vibrar el dispositivo durante un segundo
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

        //Intent para cambiar a la actividad de juego terminado
        Intent intent = new Intent(this, GameOverActivity.class);

        switch (jugadorGanador) {
            //si hubo un empate
            case 0:
                //introduce en el intent partida empatada como dato del ganador
                intent.putExtra("ganador", "Partida empatada");
                break;
            //si gano el jugador 1
            case 1:
                //introduce en el intent el nombre del jugador 1 como dato del ganador
                intent.putExtra("ganador", nombreJugador1);
                //almacena el nombre del jugador y la duracion de partida en la base de datos
                //llamando al metodo almacenarVictoria
                almacenarVictoria(nombreJugador1, (int)duracionPartida/1000);
                break;
            //si gano la maquina
            case 2:
                //introduce en el intent el nombre de la maquina como dato del ganador
                intent.putExtra("ganador", nombreJugador2);
                break;
        }
        //introduce en el intent el dato correspondiente a la duracion de la partida
        intent.putExtra("duracion", duracionPartida);
        //inicia la actividad de juego terminado
        startActivity(intent);
        //finaliza esta actividad
        finish();
    }

    /**
     * metodo para almacenar los datos de la victoria en la base de datos
     * @param jugador nombre del jugador
     * @param duracion duracion de la partida
     */
    public void almacenarVictoria(String jugador, int duracion) {
        //crea un objecto AdminSQLiteOpenHelper utilizado para acceder a la base de datos
        //o crearla en el caso de que no exista
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "admin", null, 1);

        //Obtiene la base de datos en modo escritura
        SQLiteDatabase db = admin.getWritableDatabase();
        //crea un registro
        ContentValues registro = new ContentValues();
        //almacena los datos de la victoria en el registro
        registro.put("jugador", jugador);
        registro.put("duracion", Integer.toString(duracion));

        //inserta el registro en la base de datos
        db.insert("puntuaciones", null, registro);

        //cierra la conexion con la base de datos
        db.close();
    }

    /**
     * metodo para pausar la partida
     * @param v
     */
    public void pausarPartida(View v) {
        try {
            //calcula la duracion de la partida
            duracionPartida += System.currentTimeMillis() - tiempoInicio;
            //obtiene un flujo de escritura a un fichero
            FileOutputStream fos = openFileOutput("partida.dat", MODE_PRIVATE);
            DataOutputStream dataOut = new DataOutputStream(fos);
            //escribe los datos de la partida en el fichero
            dataOut.writeInt(modoPartida);
            dataOut.writeLong(duracionPartida);
            dataOut.writeShort(turnoJugador);
            dataOut.writeUTF(nombreJugador1);
            dataOut.writeUTF(nombreJugador2);

            for (int i = 0; i<matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    dataOut.writeShort(matriz[i][j]);
                }
            }
            //descarga el flujo
            dataOut.flush();
            //cierra el flujo
            dataOut.close();
            fos.close();
            //finaliza la actividad
            finish();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error de escritura", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Metodo para cargar una partida pausada
     */
    public void cargarPartida() {
        try {
            //Obtiene los flujos de lectura
            FileInputStream fis = openFileInput("partida.dat");
            DataInputStream dataIn = new DataInputStream(fis);
            dataIn.readInt(); //Para saltar el dato de modoPartida
            //lee los datos de la partida
            tiempoInicio = dataIn.readLong();
            turnoJugador = dataIn.readShort();
            nombreJugador1 = dataIn.readUTF();
            nombreJugador2 = dataIn.readUTF();

            for (int i = 0; i<matriz.length; i++) {
                for (int j = 0; j<matriz[i].length; j++) {
                    matriz[i][j] = dataIn.readShort();
                }
            }

            //cierra el flujo
            dataIn.close();
            fis.close();

            //Se borra el archivo para que no se pueda volver a cargar la partida
            getApplicationContext().deleteFile("partida.dat");
            //actualiza el tablero de la interfaz
            repintarTablero();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Archivo no encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error de lectura", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * metodo para actualizar el tablero de la interfaz
     */
    public void repintarTablero() {
        for (int i = 0; i<matriz.length; i++) {
            for (int j = 0; j<matriz[i].length; j++) {
                if (matriz[i][j] == 1)
                    matrizBotones[i][j].setBackgroundResource(R.drawable.casilla_rojo);
                else if (matriz[i][j] == 2)
                    matrizBotones[i][j].setBackgroundResource(R.drawable.casilla_verde);
            }
        }
    }

    /**
     * Metodo llamado cuando la actividad deja de ser visible
     */
    public void onStop() {
        super.onStop();
        //para la musica
        mediaPlayer.release();
    }
}
