package com.example.mislugares_davidcuevas.presentacion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad que simplemente muestra {@link PreferenciasFragment}
 */
public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new PreferenciasFragment())
            .commit();
    }

}
