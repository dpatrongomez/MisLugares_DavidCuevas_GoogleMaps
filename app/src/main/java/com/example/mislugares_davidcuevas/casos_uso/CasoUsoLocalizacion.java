package com.example.mislugares_davidcuevas.casos_uso;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugares;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.presentacion.Aplicacion;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Permisos para localizacion.
 * @author David Cuevas Cano
 */
public class CasoUsoLocalizacion implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = "MisLugares";
    private Activity actividad;
    private int codigoPermiso;
    private LocationManager manejadorLoc;
    private Location mejorLoc;
    private GeoPunto posicionActual;
    private AdaptadorLugares adaptador;
    private static final long DOS_MINUTOS = 2 * 60 * 1000;


    /**
     * @param actividad
     * @param codigoPermiso
     */
    public CasoUsoLocalizacion(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        manejadorLoc = (LocationManager) actividad.getSystemService(LOCATION_SERVICE);
        posicionActual = ((Aplicacion) actividad.getApplication())
                .posicionActual;
        adaptador = ((Aplicacion) actividad.getApplication()).adaptador;
        ultimaLocalizazion();
        solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                "Necesita permisos de localizacion",3, actividad);
    }

    /**
     * Si devuelve true es que la app tiene permisos de localizacion
     *
     * @return true o false
     */
    public boolean hayPermisoLocalizacion() {
        return (ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Ultima localizazion.
     */
    @SuppressLint("MissingPermission")
    void ultimaLocalizazion(){
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER));
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                actualizaMejorLocaliz(manejadorLoc.getLastKnownLocation(
                        LocationManager.NETWORK_PROVIDER));
            } else  {
                solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                        "Sin el permiso localización no puedo mostrar la distancia"+
                                " a los lugares.", codigoPermiso, actividad);
            }
        }
    }

    /**
     * Solicitar permiso.
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     * @param actividad
     */
    public static void solicitarPermiso(final String permiso, String
            justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad,
                permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }}).show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    /**
     * Permiso concedido.
     */
    public void permisoConcedido() {
        ultimaLocalizazion();
        activarProveedores();
        adaptador.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    private void activarProveedores() {
        if (hayPermisoLocalizacion()) {
            if (manejadorLoc.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                manejadorLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        20 * 1000, 5, this);
            }
            if (manejadorLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                manejadorLoc.requestLocationUpdates(LocationManager
                        .NETWORK_PROVIDER, 10 * 1000, 10, this);
            }
        } else {
            solicitarPermiso(Manifest.permission.ACCESS_FINE_LOCATION,
                    "Sin el permiso localización no puedo mostrar la distancia"+
                            " a los lugares.", codigoPermiso, actividad);
        }
    }
    @Override public void onLocationChanged(Location location) {
        Log.d(TAG, "Nueva localización: "+location);
        actualizaMejorLocaliz(location);
        adaptador.notifyDataSetChanged();
    }
    @Override public void onProviderDisabled(String proveedor) {
        Log.d(TAG, "Se deshabilita: "+proveedor);
        activarProveedores();
    }
    @Override public void onProviderEnabled(String proveedor) {
        Log.d(TAG, "Se habilita: "+proveedor);
        activarProveedores();
    }
    @Override
    public void onStatusChanged(String proveedor, int estado, Bundle extras) {
        Log.d(TAG, "Cambia estado: "+proveedor);
        activarProveedores();
    }

    private void actualizaMejorLocaliz(Location localiz) {
        if (localiz != null && (mejorLoc == null
                || localiz.getAccuracy() < 2*mejorLoc.getAccuracy()
                || localiz.getTime() - mejorLoc.getTime() > DOS_MINUTOS)) {
            Log.d(TAG, "Nueva mejor localización");
            mejorLoc = localiz;
            posicionActual.setLatitud(
                    localiz.getLatitude());
            posicionActual.setLongitud(
                    localiz.getLongitude());
        }
    }


    public void activar() {
        if (hayPermisoLocalizacion()) activarProveedores();
    }


    public void desactivar() {
        if (hayPermisoLocalizacion()) manejadorLoc.removeUpdates(this);
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == codigoPermiso
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this.actividad, "Permiso de localización concedido",Toast.LENGTH_LONG).show();
    }
}
