package com.example.mislugares_davidcuevas.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.mislugares_davidcuevas.R;

/**
 * Crea un fragment que contiene una ventana con las opciones de preferencias definidas en un recurso XML
 *
 * @see {@link PreferenciasActivity}
 */
public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
