package com.netsewers.conecta4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        String ganador = getIntent().getStringExtra("ganador");

        TextView tvGanador = (TextView)findViewById(R.id.tvGanador);
        tvGanador.setText("Ganador: " + ganador);
        TextView tvDuracion = (TextView)findViewById(R.id.tvDuracion);
        int duracion = (int)(getIntent().getLongExtra("duracion", 0) / 1000) ;
        tvDuracion.setText("Duración partida: " + duracion + "s");
    }
}
