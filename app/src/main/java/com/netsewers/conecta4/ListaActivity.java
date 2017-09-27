package com.netsewers.conecta4;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ListaActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        List<String> jugadores = new ArrayList();
        List<String> duraciones = new ArrayList();

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "admin", null, 1);

        SQLiteDatabase db = admin.getReadableDatabase();

        Cursor fila = db.rawQuery("select jugador, duracion from puntuaciones order by duracion limit 3", null);

        while (fila.moveToNext()) {
            jugadores.add(fila.getString(0));
            duraciones.add(fila.getString(1));
        }

        db.close();

        setListAdapter(new AdaptadorLista(this, jugadores, duraciones));

    }
}
