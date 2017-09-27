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

public class DialogoJugador extends DialogFragment {

    private EditText etJugador1;

    public static DialogFragment newInstance(String titulo) {
        DialogFragment dialog = new DialogoJugador();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        dialog.setArguments(args);
        return dialog;
    }

    public Dialog onCreateDialog(Bundle savedInstance) {
        String titulo = getArguments().getString("titulo");

        final View formulario = getActivity().getLayoutInflater().inflate(R.layout.dialogo_jugador, null);

        etJugador1 = (EditText)formulario.findViewById(R.id.etJugador1);


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
                        //si el EditText del nombre del jugador no esta vacio
                        if (!etJugador1.getText().toString().equals("")) {
                            //Crea un bundle en el que introducir el nombre de jugador
                            Bundle b = new Bundle();
                            //introduce el nombre de jugador en el bundle
                            b.putString("jugador1", etJugador1.getText().toString());
                            //llama al metodo que inicia el juego contra maquina pasandole el bundle
                            //por parametro
                            ((MainActivity)getActivity()).iniciarJuegoContraMaquina(b);
                            //cierra el dialogo
                            alertDialog.dismiss();
                        }
                        //si el campo esta vacio
                        else {
                            //muestra un mensaje tip informando del error
                            etJugador1.setError("Debes introducir un nombre de jugador");
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

}
