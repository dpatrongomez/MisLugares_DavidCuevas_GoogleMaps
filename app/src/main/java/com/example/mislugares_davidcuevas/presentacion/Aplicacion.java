package com.example.mislugares_davidcuevas.presentacion;

import android.app.Application;

import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugares;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.modelo.LugaresLista;
import com.example.mislugares_davidcuevas.modelo.RepositorioLugares;

public class Aplicacion extends Application {

    public RepositorioLugares lugares = new LugaresLista();
    @Override public void onCreate() {
        super.onCreate();
    }
    public RepositorioLugares getLugares() {
        return lugares;
    }
    public AdaptadorLugares adaptador = new AdaptadorLugares(lugares);

    public AdaptadorLugares getAdaptador() {
        return adaptador;
    }
    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);
}


