package com.example.mislugares_davidcuevas.mapas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.fragment.app.FragmentActivity;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.casos_uso.CasoUsoLocalizacion;
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
 * FragmentActivity sobre el que vamos a cargar nuestro mapa de la API de Google Maps
 * @author David Cuevas Cano
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap maps;
    private AdaptadorLugaresBD adaptador;
    private CasoUsoLocalizacion usoLocalizacion;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private Context contexto;

    /**
     * Este metodo inicializa el layout, el adaptador y el mapa
     *
     * @param savedInstanceState
     */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        usoLocalizacion = new CasoUsoLocalizacion(this, SOLICITUD_PERMISO_LOCALIZACION);
        contexto = (Aplicacion)getApplication();
    }

    /**
     * Este método se va a llamar cuando el mapa este listo para usarse y Obtengamos un objeto GoogleMap no nulo
     *
     * @param googleMap
     */
    @Override public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);

        maps.setMapType(Integer.parseInt(pref.getString("tipoMap", "1")));

        if (usoLocalizacion.hayPermisoLocalizacion()) {
            maps.setMyLocationEnabled(true);
        }
        maps.getUiSettings().setZoomControlsEnabled(true);
        maps.getUiSettings().setCompassEnabled(true);
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
     * Evento que lanza una actividad {@link VistaLugarActivity} cuando se pulsa algun lugar marcado en el mapa
     *
     * @param marker
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
