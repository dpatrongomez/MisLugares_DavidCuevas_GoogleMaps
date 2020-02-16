package com.example.mislugares_davidcuevas.presentacion;

import android.app.Activity;
import android.os.Bundle;

import com.example.mislugares_davidcuevas.R;

/**
 * Clase AcercaDeActivity
 * @author David Cuevas Cano
 * Genera un texto donde explica para que sirve la app
 */
public class AcercaDeActivity extends Activity {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acercade);
    }
}
