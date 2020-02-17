package com.example.mislugares_davidcuevas.casos_uso;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.presentacion.Aplicacion;
import com.example.mislugares_davidcuevas.presentacion.EdicionLugarActivity;
import com.example.mislugares_davidcuevas.presentacion.VistaLugarActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.example.mislugares_davidcuevas.presentacion.VistaLugarActivity.RESULTADO_GALERIA;

/**
 * Casos de uso en lugares
 * @author David Cuevas Cano
 */
public class CasosUsoLugar {
    private Activity actividad;
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;


    /**
     * @param actividad
     * @param lugares
     * @param adaptador
     */
    public CasosUsoLugar(Activity actividad, LugaresBD lugares, AdaptadorLugaresBD adaptador) {
        this.actividad = actividad;
        this.lugares = lugares;
        this.adaptador = adaptador;
    }


    /**
     * Mostrar.
     *
     * @param pos
     */
    public void mostrar(int pos) {
        Intent i = new Intent(actividad, VistaLugarActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivity(i);
    }


    /**
     * Borrar.
     *
     * @param id
     */
    public void borrar(final int id) {
        new AlertDialog.Builder(actividad)
                .setTitle("Borrar lugar")
                .setMessage("¿Desea eliminar este lugar?")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        lugares.borrar(id);
                        adaptador.setCursor(lugares.extraeCursor());
                        adaptador.notifyDataSetChanged();
                        actividad.finish();

                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }

    /**
     * Editar.
     *
     * @param pos
     * @param codidoSolicitud
     */
    public void editar(int pos, int codidoSolicitud) {
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("pos", pos);
        actividad.startActivityForResult(i, codidoSolicitud);
    }

    /**
     * Guardar.
     *
     * @param id
     * @param nuevoLugar
     */
    public void guardar(int id, Lugar nuevoLugar) {
        lugares.actualiza(id, nuevoLugar);
        adaptador.setCursor(lugares.extraeCursor());
        adaptador.notifyDataSetChanged();
        //Toast.makeText(actividad.getBaseContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    /**
     * Actualiza posicion de lugar.
     *
     * @param pos
     * @param lugar
     */
    public void actualizaPosLugar(int pos, Lugar lugar) {
        int id = adaptador.idPosicion(pos);
        guardar(id, lugar);  //
    }

    /**
     * Compartir.
     *
     * @param lugar
     */
    public void compartir(Lugar lugar) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - " + lugar.getUrl());
        actividad.startActivity(i);
    }


    /**
     * Llamar telefono.
     *
     * @param lugar
     */
    public void llamarTelefono(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + lugar.getTelefono())));
    }


    /**
     * Ver pg web.
     *
     * @param lugar
     */
    public void verPgWeb(Lugar lugar) {
        actividad.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(lugar.getUrl())));
    }


    /**
     * Ver mapa.
     *
     * @param lugar
     */
    public void verMapa(Lugar lugar) {
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        Uri uri = lugar.getPosicion() != GeoPunto.SIN_POSICION
                ? Uri.parse("geo:" + lat + ',' + lon)
                : Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        actividad.startActivity(new Intent("android.intent.action.VIEW", uri));
    }


    /**
     * Poner foto.
     *
     * @param pos
     * @param uri
     * @param imageView
     */
    public void ponerFoto(int pos, String uri, ImageView imageView) {
        Lugar lugar = adaptador.lugarPosicion(pos);
        lugar.setFoto(uri);
        visualizarFoto(lugar, imageView);
        actualizaPosLugar(pos, lugar);
    }


    /**
     * Galeria.
     *
     * @param view
     */
    public void galeria(View view) {
        String action;
        if (android.os.Build.VERSION.SDK_INT >= 19) { // API 19 - Kitkat
            action = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            action = Intent.ACTION_PICK;
        }
        Intent intent = new Intent(action,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        actividad.startActivityForResult(intent, RESULTADO_GALERIA);
    }


    /**
     * Visualizar foto.
     *
     * @param lugar
     * @param imageView
     */
    public void visualizarFoto(Lugar lugar, ImageView imageView) {
        if (lugar.getFoto() != null && !lugar.getFoto().isEmpty()) {
            imageView.setImageBitmap(reduceBitmap(actividad, String.valueOf(Uri.parse(lugar.getFoto())), 1024,   1024));
        } else {
            imageView.setImageBitmap(null);
        }
    }


    /**
     * Tomar foto uri.
     *
     * @param codidoSolicitud
     * @return  uri
     */
    public Uri tomarFoto(int codidoSolicitud) {
        try {
            Uri uriUltimaFoto;
            File file = File.createTempFile(
                    "img_" + (System.currentTimeMillis()/ 1000), ".jpg" ,
                    actividad.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (Build.VERSION.SDK_INT >= 24) {
                uriUltimaFoto = FileProvider.getUriForFile(
                        actividad, "com.example.mislugares_davidcuevas.fileprovider", file);
            } else {
                uriUltimaFoto = Uri.fromFile(file);
            }
            Intent intent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra (MediaStore.EXTRA_OUTPUT, uriUltimaFoto);
            actividad.startActivityForResult(intent, codidoSolicitud);
            return uriUltimaFoto;
        } catch (IOException ex) {
            Toast.makeText(actividad, "Error al crear fichero de imagen",
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }

    /**
     * Reducir MB de la foto
     * @param contexto
     * @param uri
     * @param maxAncho
     * @param maxAlto
     * @return
     */
    private Bitmap reduceBitmap(Context contexto, String uri, int maxAncho, int maxAlto) {
        try {
            InputStream input = null;
            Uri u = Uri.parse(uri);
            if (u.getScheme().equals("http") || u.getScheme().equals("https")) {
                input = new URL(uri).openStream();
            } else {
                input = contexto.getContentResolver().openInputStream(u);
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(input, null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso de imagen no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Toast.makeText(contexto, "Error accediendo a imagen",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Metodo para crear un uevo lugar
     */
    public void nuevo() {
        int id = lugares.nuevo();
        GeoPunto posicion = ((Aplicacion) actividad.getApplication())
                .posicionActual;
        if (!posicion.equals(GeoPunto.SIN_POSICION)) {
            Lugar lugar = lugares.elemento(id);
            lugar.setPosicion(posicion);
            lugares.actualiza(id, lugar);
        }
        Intent i = new Intent(actividad, EdicionLugarActivity.class);
        i.putExtra("_id", id);
        actividad.startActivity(i);
    }

}
