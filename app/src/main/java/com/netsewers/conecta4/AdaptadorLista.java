package com.netsewers.conecta4;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Saul Castillo Forte on 1/03/17.
 */

public class AdaptadorLista extends BaseAdapter {
    private Activity actividad;
    private List<String> listaJug;
    private List<String> listaDur;

    public AdaptadorLista(Activity actividad, List<String> listaJug, List<String> listaDur) {
        super();
        this.actividad = actividad;
        this.listaJug = listaJug;
        this.listaDur = listaDur;
    }
    @Override
    public int getCount() {
        return listaJug.size();
    }

    @Override
    public Object getItem(int i) {
        return listaJug.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = actividad.getLayoutInflater();

        View v = inflater.inflate(R.layout.elemento_lista, null, true);
        TextView tvJugadorG = (TextView)v.findViewById(R.id.tvJugadorG);
        tvJugadorG.setText(listaJug.get(i));
        TextView tvDuracionG = (TextView)v.findViewById(R.id.tvDuracionG);
        tvDuracionG.setText(listaDur.get(i) + " segundos");

        return v;
    }
}
