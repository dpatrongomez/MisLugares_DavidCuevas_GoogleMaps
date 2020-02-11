package com.example.mislugares_davidcuevas.presentacion;

import android.app.Application;

import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugares;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.modelo.LugaresLista;
import com.example.mislugares_davidcuevas.modelo.RepositorioLugares;

public class Aplicacion extends Application {

    public LugaresBD lugares;
    public AdaptadorLugaresBD adaptador;
    public GeoPunto posicionActual = new GeoPunto(0.0, 0.0);

    @Override public void onCreate() {
        super.onCreate();
        lugares = new LugaresBD(this);
        adaptador= new AdaptadorLugaresBD(lugares, lugares.extraeCursor());
    }
}


