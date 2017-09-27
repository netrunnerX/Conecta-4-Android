package com.netsewers.conecta4;

/**
 * Created by Saul Castillo Forte on 6/03/17.
 *
 * Original por: Jose Francisco Lopez Rodriguez
 */

public class Regla {
    private int nAlineadas;//numero de fichas alineadas.
    private int tipo; //si es una ficha del tipo 1 o una ficha de tipo 2.
    private int nHuecos; //numero de huecos existentes.
    private int puntuacion; //puntuacion que se le da a la columna.

    public Regla(int nAlineadas, int tipo, int nHuecos, int puntuacion) {
        this.nAlineadas = nAlineadas;
        this.tipo = tipo;
        this.nHuecos = nHuecos;
        this.puntuacion = puntuacion;
    }

    public int getnAlineadas() {
        return nAlineadas;
    }

    public void setnAlineadas(int nAlineadas) {
        this.nAlineadas = nAlineadas;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getnHuecos() {
        return nHuecos;
    }

    public void setnHuecos(int nHuecos) {
        this.nHuecos = nHuecos;
    }
}
