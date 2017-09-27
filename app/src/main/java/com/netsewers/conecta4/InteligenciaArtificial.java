package com.netsewers.conecta4;

import java.util.List;
import java.util.Random;

/**
 * Created by Saul Castillo Forte on 6/03/17.
 *
 * Basada en la clase original Decision, creada por: Jose Francisco Lopez Rodriguez
 */

public class InteligenciaArtificial {

    private short[][] tablero;
    private int[] puntuacionesColumnas;
    private int[] alturaColumnas;
    List<Regla> reglasJuego;

    public InteligenciaArtificial(short[][] tablero) {
        //Inicializa el tablero con la matriz de la Activity Juego
        this.tablero = tablero;

        //inicializamos el array de puntuaciones de las columnas
        puntuacionesColumnas = new int[7];
        for (int i = 0; i < 7; i++){
            puntuacionesColumnas[i] = 0;
        }

        //inicializamos el array que el control del numero de fichas que hay en cada columna.
        alturaColumnas = new int[7];
        for (int i = 0; i < 7; i++){
            alturaColumnas[i] = 0;
        }

        //Inicaliza las reglas
        reglasJuego = new Reglas().getReglas();
    }

    /**
     * Calcula la columna en la que insertar la ficha
     * @return numero de columna
     */
    public int calcularDondeInsertar(){

        for(int i = 0; i < reglasJuego.size(); i++){

            Regla r = reglasJuego.get(i);

            comprobarHorizontal(r);
            comprobarVertical(r);
            comprobarOblicuoA(r);
            comprobarOblicuoD(r);

        }
        int columna = insertarFicha();

        resetarPuntuaciones();

        return columna;
    }

    /**
     * Comprobamos donde se va a cumplir una regla horizontalmente
     *
     * @param r Regla a comprobar si se cumple
     */
    public void comprobarHorizontal(Regla r){

        int[] arrayAux = new int[7];

        for(int i = 0; i < 6; i++){
            for (int j = 0; j < 7; j++){
                arrayAux[j]=tablero[i][j];
            }
            i++;

            int[] aux = cumpleRequisitosHorizontales(arrayAux,r);
            for(int k = 0; k < 7; k++){
                puntuacionesColumnas[k] = puntuacionesColumnas[k] +  aux[k]*r.getPuntuacion();
            }
        }
    }

    /**
     * Comprobamos donde se va a cumplir una regla verticalmente
     * @param r Regla a comprobar si se cumple
     */
    public void comprobarVertical(Regla r){

        int[] arrayAux = new int[6];

        for(int i = 0; i < 7; i++){
            for (int j = 0; j < 6; j++){
                arrayAux[j]= tablero[j][i];
            }
            int[] aux = cumpleRequisitosVerticales(arrayAux,i,r);
            for(int k = 0; k < 7; k++){
                puntuacionesColumnas[k] = puntuacionesColumnas[k] +  aux[k]*r.getPuntuacion();
            }

        }

    }

    /**
     * Comprobamos que se cumple una regla en sentido oblicuo ascendente
     * @param r Regla a comprobar si se cumple
     */
    public void comprobarOblicuoA(Regla r){

        int[] arrayAux= new int[4];

        for(int j = 0; j < 4; j++){
            for(int i = 0; i < 3; i++){
                arrayAux[0]=tablero[i][j];
                arrayAux[1]=tablero[i+1][j+1];
                arrayAux[2]=tablero[i+2][j+2];
                arrayAux[3]=tablero[i+3][j+3];

                int[] filaAux = cumpleRequisitosOblicuosA(arrayAux,j,r);

                for(int k = 0; k < 7; k++){
                    puntuacionesColumnas[k] = puntuacionesColumnas[k] + filaAux[k] * r.getPuntuacion();
                }
            }
        }
    }

    /**
     * Comprobamos que se cumple una regla en sentido oblicuo descendente
     * @param r Regla a comprobar si se cumple
     */
    public void comprobarOblicuoD(Regla r){

        int[] arrayAux= new int[4];

        for(int j = 0; j < 4; j++){
            for(int i = 3; i < 5; i++){
                arrayAux[0]=tablero[i][j];
                arrayAux[1]=tablero[i-1][j+1];
                arrayAux[2]=tablero[i-2][j+2];
                arrayAux[3]=tablero[i-3][j+3];

                int[] filaAux = cumpleRequisitosOblicuosD(arrayAux,j,r);

                for(int k = 0; k < 7; k++){
                    puntuacionesColumnas[k] = puntuacionesColumnas[k] + filaAux[k] * r.getPuntuacion();
                }
            }
        }

    }

    /**
     * Comprueba que se cumplen los requisitos de una regla en horizontal
     * @param fila fila a comprobar
     * @param r regla a utilizar
     * @return vector de aciertos
     */
    public int[] cumpleRequisitosHorizontales(int[]fila, Regla r){

        int inicio = 0, fin = 4;
        int nAlineadas1, nAlineadas2, nHuecos;

        int[] vectorAciertos = new int[7];
        for (int i = 0; i < 7; i++) {

            vectorAciertos[i] = 0;
        }

        while(fin<=7){
            nAlineadas1 = 0;
            nAlineadas2 = 0;
            nHuecos = 0;

            for(int i = inicio; i < fin; i++){
                if(fila[i]==1)
                    nAlineadas1++;
                if(fila[i]==2)
                    nAlineadas2++;
                if(fila[i]==0)
                    nHuecos++;
            }
            if(r.getTipo()==1 && r.getnAlineadas()==nAlineadas1 &&r.getnHuecos()==nHuecos){
                for(int j = inicio; j < fin; j++){
                    if(fila[j] == 0){
                        vectorAciertos[j]++;
                    }
                }
            }

            if(r.getTipo()==2 && r.getnAlineadas()==nAlineadas2 &&r.getnHuecos()==nHuecos){
                for(int j = inicio; j < fin; j++){
                    if(fila[j] == 0){
                        vectorAciertos[j]++;
                    }
                }
            }

            inicio++;
            fin++;
        }

        return vectorAciertos;
    }

    /**
     * Comprueba que se cumplen los requisitos de una regla en vertical
     * @param fila fila a comprobar
     * @param columna numero de columna
     * @param r Regla a utilizar
     * @return vector de aciertos
     */
    public int[] cumpleRequisitosVerticales(int[]fila, int columna, Regla r){

        int inicio = 0, fin = 4;
        int nAlineadas1, nAlineadas2, nHuecos;

        int[] vectorAciertos = new int[7];
        for (int i = 0; i < 7; i++) {

            vectorAciertos[i] = 0;
        }

        while(fin<=6){
            nAlineadas1 = 0;
            nAlineadas2 = 0;
            nHuecos = 0;

            for(int i = inicio; i < fin; i++){
                if(fila[i]==1)
                    nAlineadas1++;

                if(fila[i]==2)
                    nAlineadas2++;

                if(fila[i]==0)
                    nHuecos++;

                if(fila[i]==-1)
                    nHuecos++;
                if(fila[i]==-2)
                    nHuecos++;

            }

            if(r.getTipo()==1 && r.getnAlineadas()==nAlineadas1 &&r.getnHuecos()==nHuecos){
                for(int j = inicio; j < fin; j++){
                    if(fila[j] == 0){
                        vectorAciertos[columna]++;
                    }
                }
            }

            if(r.getTipo()==2 && r.getnAlineadas()==nAlineadas2 &&r.getnHuecos()==nHuecos){
                for(int j = inicio; j < fin; j++){
                    if(fila[j] == 0){
                        vectorAciertos[columna]++;
                    }
                }
            }

            inicio++;
            fin++;
        }

        return vectorAciertos;
    }


    /**
     * Comprueba que se cumplen los requisitos de una regla en oblicuo ascendente
     * @param fila fila a comprobar
     * @param columna numero de columna
     * @param r Regla a utiizar
     * @return vector de aciertos
     */
    public int[] cumpleRequisitosOblicuosA(int[] fila ,int columna, Regla r){

        int nAlineadas1, nAlineadas2, nHuecos;

        int[] vectorAciertos = new int[7];
        for (int i = 0; i < 7; i++) {
            vectorAciertos[i] = 0;
        }

        nAlineadas1 = 0;
        nAlineadas2 = 0;
        nHuecos = 0;

        for(int i = 0; i < 4; i++){
            if(fila[i]==1)
                nAlineadas1++;

            if(fila[i]==2)
                nAlineadas2++;

            if(fila[i]==0)
                nHuecos++;
        }

        if(r.getTipo()==1 && r.getnAlineadas()==nAlineadas1 &&r.getnHuecos()==nHuecos){
            for(int j = 0; j < 4; j++){
                if(fila[j] == 0){
                    vectorAciertos[columna+j]++;
                }
            }
        }

        if(r.getTipo()==2 && r.getnAlineadas()==nAlineadas2 &&r.getnHuecos()==nHuecos){
            for(int j = 0; j < 4; j++){
                if(fila[j] == 0){
                    vectorAciertos[columna+j]++;
                }
            }
        }

        return vectorAciertos;
    }

    /**
     * Comprueba que se cumplen los requisitos de una regla en oblicuo descendente
     * @param fila fila a comprobar
     * @param columna numero de columna
     * @param r Regla a utiizar
     * @return vector de aciertos
     */
    public int[] cumpleRequisitosOblicuosD(int[]fila,int columna, Regla r){

        int nAlineadas1, nAlineadas2, nHuecos;

        int[] vectorAciertos = new int[7];
        for (int i = 0; i < 7; i++) {
            vectorAciertos[i] = 0;
        }

        nAlineadas1 = 0;
        nAlineadas2 = 0;
        nHuecos = 0;

        for(int i = 0; i < 4; i++){
            if(fila[i]==1)
                nAlineadas1++;

            if(fila[i]==2)
                nAlineadas2++;

            if(fila[i]==0)
                nHuecos++;
        }

        if(r.getTipo()==1 && r.getnAlineadas()==nAlineadas1 &&r.getnHuecos()==nHuecos){
            for(int j = 0; j < 4; j++){
                if(fila[j] == 0){
                    vectorAciertos[columna+j]++;
                }
            }
        }

        if(r.getTipo()==2 && r.getnAlineadas()==nAlineadas2 &&r.getnHuecos()==nHuecos){
            for(int j = 0; j < 4; j++){
                if(fila[j] == 0){
                    vectorAciertos[columna+j]++;
                }
            }
        }

        return vectorAciertos;
    }

    /**
     * Reseteamos las puntiaciones de las columnas para evaluar una nueva regla.
     *
     */
    public void resetarPuntuaciones(){
        for(int i = 0; i < 7; i++)
            puntuacionesColumnas[i] = 0;
    }

    /**
     * Este metodo calcula la columna donde debe insertarse la ficha
     * @return la columna donde se debe insertar
     */
    public int insertarFicha(){
        int[] columnaInsertar = new int[7];

        for (int i = 0; i < 7; i++) {
            columnaInsertar[i] = -1;
        }

        int mayorPuntuacion = -1;
        for (int i = 0; i < 7; i++) {
            if (puntuacionesColumnas[i] > mayorPuntuacion) {
                mayorPuntuacion = puntuacionesColumnas[i];
            }

        }
        int j = -1;
        for (int i = 0; i < 7; i++) {
            if (puntuacionesColumnas[i] == mayorPuntuacion) {
                j++;
                columnaInsertar[j] = i;
            }
        }

        if (j == 0) {

            return columnaInsertar[0];

        } else {

            return columnaInsertar[columnasEmpatadas(columnaInsertar)];

        }

    }

    /**
     * Si hay columnas con la misma puntuacion pues aleatoriamente decide donde debe insertarse la ficha
     *
     * @param columnaInsertar un array que dice las columnas con misma puntuacion
     * @return la columna donde se debe insertar
     */
    public int columnasEmpatadas(int[] columnaInsertar){
        int col=0;
        boolean flag = false;
        while(flag == false){

            Random r = new Random();
            int x = r.nextInt(7);
            if (columnaInsertar[x] != -1 && alturaColumnas[x] < 6) {
                col = x;
                flag = true;
            }
        }

        return col;
    }

    /**
     * Actualiza el numero de fichas insertadas por columna
     */
    public void actualizarAlturas() {
        int cont = 0;
        for(int i = 0; i < 7; i++){
            int alt = 0;
            for(int j = 0; j < 6; j++){
                if(tablero[j][i] != 0)
                    alt++;
            }
            alturaColumnas[cont] = alt;
            cont++;
        }
    }

}
