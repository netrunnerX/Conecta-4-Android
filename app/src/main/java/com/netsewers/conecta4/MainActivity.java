package com.netsewers.conecta4;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void jugarContraJugador(View v) {
        DialogFragment dialogFragment = DialogoJugadores.newInstance("Introducir nombres jugadores");
        dialogFragment.show(getFragmentManager(), "DialogoJugadores");
    }

    public void iniciarJuegoContraJugador(Bundle b) {
        Intent intent = new Intent(getApplicationContext(), Juego.class);
        b.putBoolean("pausada", false);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void jugarContraMaquina(View v) {
        DialogFragment dialogFragment = DialogoJugador.newInstance("Introducir nombre jugador");
        dialogFragment.show(getFragmentManager(), "DialogoJugador");
    }

    public void iniciarJuegoContraMaquina(Bundle b) {
        Intent intent = new Intent(getApplicationContext(), JuegoContraMaquina.class);
        b.putBoolean("pausada", false);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void mostrarPuntuaciones(View v) {
        Intent intent = new Intent(getApplicationContext(), ListaActivity.class);
        startActivity(intent);
    }

    public void reanudarPartida(View v) {
        File file = getApplicationContext().getFileStreamPath("partida.dat");

        if (file.exists()) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("pausada", true);

            int modoPartida = obtenerModoPartida(file);
            if (modoPartida == 1) {
                Intent intent = new Intent(getApplicationContext(), Juego.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            else if (modoPartida == 2) {
                Intent intent = new Intent(getApplicationContext(), JuegoContraMaquina.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No existe una partida pausada", Toast.LENGTH_SHORT).show();
        }
    }

    public int obtenerModoPartida(File file) {
        int modoPartida = 0;
        try {
            FileInputStream fis = openFileInput(file.getName());
            DataInputStream dataIn = new DataInputStream(fis);
            modoPartida = dataIn.readInt();
            dataIn.close();
            fis.close();
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Archivo no encontrado", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error de lectura", Toast.LENGTH_SHORT).show();
        }
        finally {
            return modoPartida;
        }
    }
}
