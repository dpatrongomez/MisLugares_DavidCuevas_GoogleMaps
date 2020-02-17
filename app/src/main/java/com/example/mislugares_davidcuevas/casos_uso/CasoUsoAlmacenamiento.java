package com.example.mislugares_davidcuevas.casos_uso;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


/**
 * Permisos de almacenamiento.
 * @author David Cuevas Cano
 */
public class CasoUsoAlmacenamiento implements ActivityCompat.OnRequestPermissionsResultCallback {


    private static final String TAG = "Mis_Lugares";
    private Activity actividad;
    private int codigoPermiso;


    /**
     * @param actividad
     * @param codigoPermiso
     */
    public CasoUsoAlmacenamiento(Activity actividad, int codigoPermiso) {
        this.actividad = actividad;
        this.codigoPermiso = codigoPermiso;
        solicitarPermiso(Manifest.permission.READ_EXTERNAL_STORAGE,
                "Necesita permisos de almacenamiento para añadir fotografías",1);
        solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Necesita permisos de almacenamiento para añadir fotografías",2);
    }


    /**
     * Solicitar permiso.
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     */
    public  void solicitarPermiso(final String permiso, String justificacion, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad,
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else {
            ActivityCompat.requestPermissions(actividad,
                    new String[]{permiso}, requestCode);
        }
    }

    /**
     * Solicitar permiso fragment.
     *
     * @param permiso
     * @param justificacion
     * @param requestCode
     * @param fragment
     */
    public  void solicitarPermisoFragment(final String permiso, String justificacion, final int requestCode, final Fragment fragment) {
        if (fragment.shouldShowRequestPermissionRationale(permiso)){
            new AlertDialog.Builder(fragment.getActivity())
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            fragment.requestPermissions(
                                    new String[]{permiso}, requestCode);
                        }})
                    .show();
        } else {
            fragment.requestPermissions(new String[]{permiso}, requestCode);
        }
    }


    /**
     * Si devuelve true, la app tiene permisos de lectura
     *
     * @return true o false
     */
    public boolean hayPermisoAlmacenamiento() {
        return (ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }


    /**
     * Si devuelve true, la app tiene permisos de escritura
     *
     * @return true o false
     */
    public boolean hayPermisoAlmacenamientoEscritura() {
        return (ActivityCompat.checkSelfPermission(
                actividad, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }


    public boolean hayPermisoUbicacion(){
        return (ActivityCompat.checkSelfPermission(actividad,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }



    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == codigoPermiso
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this.actividad, "Permiso de lectura concedido",Toast.LENGTH_LONG).show();
    }
}


