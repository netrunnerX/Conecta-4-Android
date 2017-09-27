package com.netsewers.conecta4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saul Castillo Forte on 6/03/17.
 */

public class Reglas {

    private List<Regla> reglas = new ArrayList();

    public Reglas() {

        reglas.add(new Regla(3, 2, 1, 10000000));
        reglas.add(new Regla(3, 1, 1, 600000));
        reglas.add(new Regla(2, 2, 2, 6000));
        reglas.add(new Regla(2, 1, 2, 5000));
        reglas.add(new Regla(1, 2, 3, 4000));
        reglas.add(new Regla(1, 1, 3, 3000));
        reglas.add(new Regla(0, 1, 4, 1));

    }

    public List<Regla> getReglas() {
        return reglas;
    }
}
