package com.example.mislugares_davidcuevas.mapas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.presentacion.Aplicacion;
import com.example.mislugares_davidcuevas.presentacion.VistaLugarActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Mapas
 * @author David Cuevas Cano
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap maps;
    private AdaptadorLugaresBD adaptador;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    /**
     *
     * @param googleMap
     * Prepara el mapa y lo muestra con las marcas que existen de los lugares
     */

    @Override public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;
        maps.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            maps.setMyLocationEnabled(true);
            maps.getUiSettings().setZoomControlsEnabled(true);
            maps.getUiSettings().setCompassEnabled(true);
        }
        if (adaptador.getItemCount() > 0) {
            GeoPunto p = adaptador.lugarPosicion(0).getPosicion();
            maps.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(p.getLatitud(), p.getLongitud()), 12));
        }
        for (int n=0; n<adaptador.getItemCount(); n++) {
            Lugar lugar = adaptador.lugarPosicion(n);
            GeoPunto p = lugar.getPosicion();
            if (p != null && p.getLatitud() != 0) {

                Bitmap iGrande = BitmapFactory.decodeResource(
                        getResources(), lugar.getTipo().getRecurso());
                Bitmap icono = Bitmap.createScaledBitmap(iGrande,
                        iGrande.getWidth() / 7, iGrande.getHeight() / 7, false);
                maps.addMarker(new MarkerOptions()
                        .position(new LatLng(p.getLatitud(), p.getLongitud()))
                        .title(lugar.getNombre()).snippet(lugar.getDireccion())
                        .icon(BitmapDescriptorFactory.fromBitmap(icono)));
            }
        }
        maps.setOnInfoWindowClickListener(this);
    }

    /**
     *
     * @param marker
     * Muestra informacion de la marca pulsada
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        for (int id=0; id<adaptador.getItemCount(); id++){
            if (adaptador.lugarPosicion(id).getNombre()
                    .equals(marker.getTitle())){
                Intent intent = new Intent(this, VistaLugarActivity.class);
                intent.putExtra("pos", id);
                startActivity(intent);
                break;
            }
        }
    }
}
