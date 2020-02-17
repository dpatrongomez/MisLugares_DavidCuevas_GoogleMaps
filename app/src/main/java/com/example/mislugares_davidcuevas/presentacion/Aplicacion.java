package com.example.mislugares_davidcuevas.presentacion;

import android.app.Application;

import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;

/**
 * The type Aplicacion.
 * @author David Cuevas Cano
 */
public class Aplicacion extends Application {

    /**
     * Base de datos Lugares.
     */
    public LugaresBD lugares;
    /**
     * Adaptador de la base de datos
     */
    public AdaptadorLugaresBD adaptador;
    /**
     * La posicion actual
     */
    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);

    @Override public void onCreate() {
        super.onCreate();
        lugares = new LugaresBD(this);
        adaptador= new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }
}


