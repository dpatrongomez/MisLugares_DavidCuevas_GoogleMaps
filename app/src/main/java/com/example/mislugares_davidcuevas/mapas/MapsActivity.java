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
import com.example.mislugares_davidcuevas.datos.LugaresBD;
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
    private CasoUsoLocalizacion usoLocalizacion;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private Context contexto;


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
     *
     * @param googleMap
     * Prepara el mapa y lo muestra con las marcas que existen de los lugares
     */

    @Override public void onMapReady(GoogleMap googleMap) {
        maps = googleMap;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(contexto);

        /*witch (pref.getString("tipoMap", "tiposMapaValores")) {
            case "0":
                maps.setMapType(googleMap.MAP_TYPE_NORMAL);
                break;
            case "1":
                maps.setMapType(googleMap.MAP_TYPE_SATELLITE);
                break;

            case "2":
                maps.setMapType(googleMap.MAP_TYPE_TERRAIN);
                break;
            case "3":
                maps.setMapType(googleMap.MAP_TYPE_HYBRID);
                break;
            default:
                maps.setMapType(googleMap.MAP_TYPE_NORMAL);
                break;
        }*/
        maps.setMapType(Integer.valueOf(pref.getString("tipoMap", "tiposMapaValores")));

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
