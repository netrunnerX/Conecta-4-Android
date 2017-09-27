package com.netsewers.conecta4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Saul Castillo Forte on 22/02/17.
 */

public class DialogoJugadores extends DialogFragment {

    private EditText etJugador1;
    private EditText etJugador2;

    public static DialogFragment newInstance(String titulo) {
        DialogFragment dialog = new DialogoJugadores();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        dialog.setArguments(args);
        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {
        String titulo = getArguments().getString("titulo");

        final View formulario = getActivity().getLayoutInflater().inflate(R.layout.dialogo_jugadores, null);

        etJugador1 = (EditText)formulario.findViewById(R.id.etJugador1);
        etJugador2 = (EditText)formulario.findViewById(R.id.edJugador2);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(titulo);
        dialogBuilder.setView(formulario);

        //Configuramos el boton de aceptar con un escuchador null
        //despues creamos el alertDialog antes de devolverlo y le configuramos un onShowListener
        dialogBuilder.setPositiveButton("Aceptar", null);
        dialogBuilder.setNegativeButton("Cancelar", null);

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            //en el metodo onShow, obtenemos el boton de aceptar y le configuramos un escuchador
            //View.OnclickListener
            //De esta forma podemos validar el texto introducido por el usuario,
            //controlando cuando cerrar el dialogo
            @Override
            public void onShow(DialogInterface dialog) {
                Button botonAceptar = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                botonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //si los EditText de los nombres de los jugadores no estan vacios
                        if (!(etJugador2.getText().toString().equals("")
                        || etJugador1.getText().toString().equals(""))) {
                            //Crea un bundle en el que introducir el nombre de los jugadores
                            Bundle b = new Bundle();
                            //introduce el nombre de jugador 1 en el bundle
                            b.putString("jugador1", etJugador1.getText().toString());
                            //introduce el nombre de jugador 2 en el bundle
                            b.putString("jugador2", etJugador2.getText().toString());
                            //llama al metodo que inicia el juego jugador contra jugador
                            // pasandole el bundle por parametro
                            ((MainActivity)getActivity()).iniciarJuegoContraJugador(b);
                            //cierra el dialogo
                            alertDialog.dismiss();
                        }
                        //si alguno de los campos esta vacio
                        else {
                            //si el campor del jugador 1 esta vacio
                            if (etJugador1.getText().toString().equals(""))
                                //muestra un mensaje tip informando del error
                                etJugador1.setError("Debes introducir un nombre para el jugador 1");
                            //si el campo del jugador 2 esta vacio
                            if (etJugador2.getText().toString().equals(""))
                                //muestra un mensaje tip informando del error
                                etJugador2.setError("Debes introducir un nombre para el jugador 2");
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

}
