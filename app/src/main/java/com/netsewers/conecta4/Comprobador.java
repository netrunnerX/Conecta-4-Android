package com.netsewers.conecta4;

/**
 * Created by Saul Castillo Forte on 7/03/17.
 */

public class Comprobador {

    private short[][] matriz;

    public Comprobador(short[][] matriz) {
        this.matriz = matriz;
    }

    public int comprobarGanador() {
        int ganador;
        ganador = comprobarGanadorHorizontal();
        if (ganador == 1)
            return 1;
        else if (ganador == 2)
            return 2;

        ganador = comprobarGanadorVertical();
        if (ganador == 1)
            return 1;
        else if (ganador == 2)
            return 2;

        ganador = comprobarGanadorDiagonal();
        if (ganador == 1)
            return 1;
        else if (ganador == 2)
            return 2;

        return 0;
    }

    private int comprobarGanadorHorizontal() {
        int contJug1 = 0;
        int contJug2 = 0;

        //Bucle para seleccionar fila
        for (int i=matriz.length-1; i>=0; i--) {
            //rango indica el tamaño de fila restante, este se decrementa en cada iteracion del segundo bucle
            int rango = matriz[0].length;

            //segundo bucle: recorre una fila de la matriz
            for (int j=0; j<matriz[i].length-1; j++){
                rango--;
                //comprueba el valor de una posicion de la matriz
                switch (matriz[i][j]) {
                    //si el valor corresponde con una ficha del jugador 1
                    case 1:
                        //si la cuenta de fichas del jugador 1 es 0
                        if (contJug1 == 0)
                            //incrementa la cuenta en 1
                            contJug1++;
                        //si la siguiente casilla tiene una ficha del mismo jugador
                        if (1==matriz[i][j+1]) {
                            //incrementa la cuenta en 1
                            contJug1++;
                            //si la cuenta de fichas alineadas del jugador es 4
                            if (contJug1 == 4)
                                //devuelve 1 (gana el jugador 1)
                                return 1;
                        }
                        //si la siguiente casilla no tiene una ficha del mismo jugador
                        else
                            //resetea la cuenta de fichas alineadas para el jugador 1
                            contJug1 = 0;
                        break;
                    //si el valor corresponde con una ficha del jugador 2
                    case 2:
                        //se realizan las mismas operaciones que en el caso 1, para el jugador 2
                        if (contJug2 == 0)
                            contJug2++;
                        if (2==matriz[i][j+1]) {
                            contJug2++;
                            if (contJug2 == 4)
                                return 2;
                        }
                        else
                            contJug2 = 0;
                        break;
                }
                //si el tamaño restante de fila ya no permite que pueda haber cuatro fichas alineadas
                //para un jugador, sale del segundo bucle para dar paso a la siguiente fila
                if (!(rango + contJug1 >= 4 || rango + contJug2 >= 4))
                    break;

            }
        }
        //si no se encontraron 4 fichas alineadas para ningun jugador, devuelve 0
        return 0;
    }

    private int comprobarGanadorVertical() {
        int contJug1 = 0;
        int contJug2 = 0;

        //bucle para seleccionar columna
        for (int i=0; i<matriz[0].length; i++) {
            //obtiene el tamaño de la columna, utilizado para controlar el tamaño restante
            int rango = matriz.length;

            //segundo bucle: recorre las casillas de la columna en sentido ascendente
            for (int j=matriz.length-1; j>0; j--){
                //decrementa en 1 el tamaño restante de columna
                rango--;
                //comprueba el valor de una posicion en la columna
                switch (matriz[j][i]) {
                    //si el valor corresponde con una ficha del jugador 1
                    case 1:
                        //si la cuenta de fichas alineadas del jugador 1 es 0
                        if (contJug1 == 0)
                            //incrementa en 1 la cuenta de fichas del jugador 1
                            contJug1++;
                        //si la siguiente casilla tambien tiene una ficha del jugador 1
                        if (1==matriz[j-1][i]) {
                            //incrementa en 1 la cuenta de fichas del jugador 1
                            contJug1++;
                            //si la cuenta de fichas es igual a 4
                            if (contJug1 == 4)
                                //devuelve 1, gana el jugador 1
                                return 1;
                        }
                        //si la siguiente casilla no tiene una ficha del jugador 1
                        else
                            //resetea la cuenta de fichas alineadas del jugador 1
                            contJug1 = 0;
                        break;
                    //mismo procedimiento para el caso del jugador 2
                    case 2:
                        if (contJug2 == 0)
                            contJug2++;
                        if (2==matriz[j-1][i]) {
                            contJug2++;
                            if (contJug2 == 4)
                                return 2;
                        }
                        else
                            contJug2 = 0;
                        break;
                }
                //si el tamaño restante de columna ya no permite que pueda haber cuatro fichas alineadas
                //para un jugador, sale del segundo bucle para dar paso a la siguiente columna
                if (!(rango + contJug1 >= 4 || rango + contJug2 >= 4))
                    break;

            }
        }
        //si no se encontraron 4 fichas alineadas para ningun jugador, devuelve 0
        return 0;
    }

    private int comprobarGanadorDiagonal() {
        int contJug1 = 0;
        int contJug2 = 0;
        final int longitudColumna = matriz.length;
        final int longitudFila = matriz[0].length;

        /**
         * Fases 1-4
         * recorre diagonales tomando como punto de partida
         * las posiciones de la primera columna de la matriz (fase 1 y 2)
         * y de la ultima columna de la matriz (fase 3 y 4)
         */
        //1 Fase, recorre en sentido ^>
        //bucle principal: en cada iteracion asciende una posicion de la primera columna de la matriz
        for (int i=longitudColumna-1; i>=0; i--){
            //variable k: utilizada para desplazar la seleccion a la derecha
            int k = 0;
            contJug1 = 0;
            contJug2 = 0;
            //segundo bucle: recorre en sentido ascendente desde la posicion i hasta llegar a
            //la penultima fila, en combinacion con la variable k se produce el sentido diagonal
            for (int j=i; j>0; j--) {
                //condicion que evita que al comprobar la siguiente casilla se salga de la matriz
                //por la derecha
                if (k < longitudFila-1) {
                    //comprueba el valor de una posicion de la matriz
                    switch (matriz[j][k]) {
                        //si el valor corresponde con una ficha del jugador 1
                        case 1:
                            //si la cuenta de fichas alineadas del jugador 1 es 0
                            if (contJug1 == 0)
                                //incrementa en 1 la cuenta de fichas del jugador 1
                                contJug1++;
                            //si la siguiente casilla en sentido ^> contiene una ficha del mismo jugador
                            if (1==matriz[j-1][k+1]) {
                                //incrementa en 1 la cuenta de fichas del jugador 1
                                contJug1++;
                                //si la cuenta de fichas es igual a 4
                                if (contJug1 == 4)
                                    //devuelve 1, gana el jugador 1
                                    return 1;
                            }
                            //si la siguiente casilla no tiene una ficha del jugador 1
                            else
                                //resetea la cuenta de fichas alineadas del jugador 1
                                contJug1 = 0;
                            break;
                        //mismo procedimiento para el caso del jugador 2
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[j-1][k+1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    //incrementa en 1 la variable k, con esto y el paso a la siguiente iteracion
                    //se realiza un movimiento en sentido ^>
                    k++;
                }
                //Si llego al limite por la derecha, sale del segundo bucle para dar paso a una nueva
                //iteracion del bucle principal
                else
                    break;
            }

        }

        //2 Fase, recorre en sentido v>
        //El procedimiento es similar a la fase 1, con la diferencia
        //de que se inicia desde la esquina superior izquierda de la matriz
        //y se van realizando comprobaciones en sentido v>
        for (int i=0; i<longitudColumna; i++){
            int k = 0;
            contJug1 = 0;
            contJug2 = 0;
            for (int j=i; j<longitudColumna-1; j++) {
                if (k < longitudFila-1) {
                    switch (matriz[j][k]) {
                        case 1:
                            if (contJug1 == 0)
                                contJug1++;
                            if (1==matriz[j+1][k+1]) {
                                contJug1++;
                                if (contJug1 == 4)
                                    return 1;
                            }
                            else
                                contJug1 = 0;
                            break;
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[j+1][k+1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    k++;
                }
                else
                    break;
            }

        }

        //3 Fase, recorre en sentido <^
        //Inicia desde la esquina inferior derecha de la matriz
        //y se van realizando comprobaciones en sentido <^
        for (int i=longitudColumna-1; i>=0; i--){
            //la variable k en este caso permite realizar un desplazamiento hacia la izquierda
            int k = longitudFila-1;
            contJug1 = 0;
            contJug2 = 0;
            for (int j=i; j>0; j--) {
                if (k > 0) {
                    switch (matriz[j][k]) {
                        case 1:
                            if (contJug1 == 0)
                                contJug1++;
                            if (1==matriz[j-1][k-1]) {
                                contJug1++;
                                if (contJug1 == 4)
                                    return 1;
                            }
                            else
                                contJug1 = 0;
                            break;
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[j-1][k-1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    k--;
                }
                else
                    break;
            }

        }

        //4 Fase, recorre en sentido <v
        //Inicia desde la esquina superior derecha de la matriz
        //y se van realizando comprobaciones en sentido <v
        for (int i=0; i<longitudColumna; i++){
            int k = longitudFila-1;
            contJug1 = 0;
            contJug2 = 0;
            for (int j=i; j<longitudColumna-1; j++) {
                if (k > 0) {
                    switch (matriz[j][k]) {
                        case 1:
                            if (contJug1 == 0)
                                contJug1++;
                            if (1==matriz[j+1][k-1]) {
                                contJug1++;
                                if (contJug1 == 4)
                                    return 1;
                            }
                            else
                                contJug1 = 0;
                            break;
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[j+1][k-1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    k--;
                }
                else
                    break;
            }

        }

        /**
         * Fases 5 y 6
         * Recorre diagonales tomando como punto de partida las posiciones
         * de la ultima fila de la matriz
         * No inicia en la primera y ultima posicion, ya que esas diagonales
         * ya se han recorrido en las fases 1 y 3
         *
         * Si (longFila - longColumna) es menor que 2, se salta las fases
         */
        //Fase 5, recorre en sentido ^>
        //No toma en cuenta diagonales a partir de la posicion
        //longFila - longColumna, puesto que esas diagonales ya se han recorrido
        //en la fase 4

        //En cada iteracion se toma como inicio del recorrido una posicion de la fila inferior
        //de la matriz, a diferencia de las anteriores fases, en las que se toma como inicio una
        //posicion en la columna izquierda (fases 1 y 2) o una posicion en la columna derecha
        //(fases 3 y 4)

        //las variables i y j desplazan hacia la derecha
        for (int i=1; i<longitudFila - longitudColumna; i++) {
            //la variable k desplaza hacia arriba
            int k = longitudColumna-1;
            contJug1 = 0;
            contJug2 = 0;
            for (int j=i; j<longitudFila-1; j++) {
                //Comprueba que no este en el limite superior de la matriz
                if (k > 0) {
                    switch (matriz[k][j]) {
                        case 1:
                            if (contJug1 == 0)
                                contJug1++;
                            if (1==matriz[k-1][j+1]) {
                                contJug1++;
                                if (contJug1 == 4)
                                    return 1;
                            }
                            else
                                contJug1 = 0;
                            break;
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[k-1][j+1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    k--;
                }
                else
                    break;
            }
        }

        //Fase 6, recorre en sentido <^
        //No toma en cuenta diagonales cuya posicion de inicio es menor a
        //longFila - longColumna, puesto que esas diagonales ya se han recorrido
        //en la fase 2

        //las variables i y j desplazan hacia la izquierda
        for (int i=longitudFila-2; i>=longitudFila - longitudColumna; i--) {
            //la variable k desplaza hacia arriba
            int k = longitudColumna-1;
            contJug1 = 0;
            contJug2 = 0;
            for (int j=i; j>0; j--) {
                //comprueba que no este en el limite superior de la matriz
                if (k > 0) {
                    switch (matriz[k][j]) {
                        case 1:
                            if (contJug1 == 0)
                                contJug1++;
                            if (1==matriz[k-1][j-1]) {
                                contJug1++;
                                if (contJug1 == 4)
                                    return 1;
                            }
                            else
                                contJug1 = 0;
                            break;
                        case 2:
                            if (contJug2 == 0)
                                contJug2++;
                            if (2==matriz[k-1][j-1]) {
                                contJug2++;
                                if (contJug2 == 4)
                                    return 2;
                            }
                            else
                                contJug2 = 0;
                            break;
                    }
                    k--;
                }
                else
                    break;
            }
        }
        //si no encontro ningun ganador, devuelve 0
        return 0;
    }

    public boolean isPanelLleno() {
        boolean isLleno = true;

        for (int i = 0; i<matriz.length; i++) {
            for (int j = 0; j<matriz[i].length; j++) {
                if (matriz[i][j] == 0) {
                    isLleno = false;
                    break;
                }
            }
            if (isLleno == false)
                break;
        }
        return isLleno;
    }
}
