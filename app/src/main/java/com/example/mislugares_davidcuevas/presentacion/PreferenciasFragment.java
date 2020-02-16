package com.example.mislugares_davidcuevas.presentacion;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.mislugares_davidcuevas.R;

/**
 * Fragment de PreferenciasActivity
 * @author David Cuevas Cano
 */
public class PreferenciasFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
