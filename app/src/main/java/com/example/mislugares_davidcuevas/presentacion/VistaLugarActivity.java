package com.example.mislugares_davidcuevas.presentacion;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.casos_uso.CasoUsoAlmacenamiento;
import com.example.mislugares_davidcuevas.casos_uso.CasosUsoLugar;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.Lugar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Clase VistaLugarActivity que extiende de AppCompatActivity
 * @author David Cuevas Cano
 */
public class VistaLugarActivity extends AppCompatActivity {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;

    public final static int RESULTADO_GALERIA = 2;

    final static int RESULTADO_FOTO = 3;
    private static final int MY_READ_REQUEST_CODE = 1;
    private static final int MY_WRITE_REQUEST_CODE = 2;
    private ImageView foto;
    private CasoUsoAlmacenamiento usoAlmacenamiento;
    private Uri uriUltimaFoto;

    final static int RESULTADO_EDITAR = 1;

    public int _id;

    private Activity actividad;



    public final Calendar c = Calendar.getInstance();

    int mes = c.get(Calendar.MONTH);

    int dia = c.get(Calendar.DAY_OF_MONTH);

    int anio = c.get(Calendar.YEAR);

    int hora = c.get(Calendar.HOUR_OF_DAY);

    int minuto = c.get(Calendar.MINUTE);

    int segundos = c.get(Calendar.SECOND);


    ImageButton icono_hora, icono_fecha;

    TextView txtFecha;

    TextView txtHora;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_lugar);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        adaptador = ((Aplicacion) getApplication()).adaptador;

        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", -1) ;
        _id = extras.getInt("_id",-1);
        if (_id!=-1){
            lugar = lugares.elemento(_id);
        }
        else{
            lugar = adaptador.lugarPosicion(pos);
        }
        lugares = ((Aplicacion) getApplication()).lugares;
        lugares.actualiza(_id,lugar);
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);
        lugar = adaptador.lugarPosicion (pos);
        foto = (ImageView) findViewById(R.id.foto);
        actualizaVistas();
        setSupportActionBar(toolbar);
        inicializarListener();
        usoAlmacenamiento = new CasoUsoAlmacenamiento(this, MY_WRITE_REQUEST_CODE);

        if (extras != null) {
            pos = extras.getInt("pos", 0);
        }
        else  {
            pos = 0;
        }
        _id = adaptador.idPosicion(pos);

        if (lugar.getTelefono() == 0) {
            findViewById(R.id.telefono).setVisibility(View.GONE);
        } else {
            findViewById(R.id.telefono).setVisibility(View.VISIBLE);
            TextView telefono = findViewById(R.id.telefono);
            telefono.setText(Integer.toString(lugar.getTelefono()));
        }



    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_lugar, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.accion_compartir:
                usoLugar.compartir(lugar);
                return true;

            case R.id.accion_llegar:
                usoLugar.verMapa(lugar);
                return true;

            case R.id.accion_editar:
                usoLugar.editar(pos, RESULTADO_EDITAR);
                return true;

            case R.id.accion_borrar:
                int _id = adaptador.idPosicion(pos);
                usoLugar.borrar(_id);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inicializamos las vistas que contiene la clase
     */
    public void actualizaVistas() {
        TextView nombre = findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        ImageView logo_tipo = findViewById(R.id.logo_tipo);
        logo_tipo.setImageResource(lugar.getTipo().getRecurso());
        TextView tipo = findViewById(R.id.tipo);
        tipo.setText(lugar.getTipo().getTexto());
        TextView direccion = findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        TextView telefono = findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));
        TextView url = findViewById(R.id.url);
        url.setText(lugar.getUrl());
        TextView comentario = findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        TextView fecha = findViewById(R.id.fecha);
        fecha.setText(DateFormat.getDateInstance().format(
                new Date(lugar.getFecha())));
        TextView hora = findViewById(R.id.hora);
        hora.setText(DateFormat.getTimeInstance().format(
                new Date(lugar.getFecha())));
        RatingBar valoracion = findViewById(R.id.valoracion);
        valoracion.setRating(lugar.getValoracion());
        valoracion.setOnRatingBarChangeListener(
        new RatingBar.OnRatingBarChangeListener() {
            @Override public void onRatingChanged(RatingBar ratingBar, float valor, boolean fromUser) {
                lugar.setValoracion(valor);
                usoLugar.actualizaPosLugar(pos, lugar);
                pos = adaptador.posicionId(_id);
            }
        });
        usoLugar.visualizarFoto(lugar, foto);
    }


    /**
     * Iniciamos listener para el uso de cámara, galería, ver paginas web, etc..
     */
    public void inicializarListener () {
        LinearLayout lmap = findViewById(R.id.LinearMapa);
        LinearLayout lweb = findViewById(R.id.LinearWeb);
        LinearLayout ltelefono = findViewById(R.id.LinearTelefono);
        ImageView galeria = (ImageView) findViewById(R.id.galeria);
        final ImageView camara = (ImageView) findViewById(R.id.camara);
        final ImageView eliminarFoto = (ImageView) findViewById(R.id.eliminarfoto);
        lmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.verMapa(lugar);
            }
        });

        lweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.verPgWeb(lugar);
            }
        });

        ltelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.llamarTelefono(lugar);
            }
        });


        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.galeria(foto);

            }
        });

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!usoAlmacenamiento.hayPermisoAlmacenamientoEscritura()) {
                uriUltimaFoto = usoLugar.tomarFoto(RESULTADO_FOTO);
                }else {
                    Toast.makeText(getBaseContext(), "No hay permisos de almacenamiento, no se puede tomar la foto", Toast.LENGTH_LONG).show();
                    usoAlmacenamiento = new CasoUsoAlmacenamiento(VistaLugarActivity.this, MY_WRITE_REQUEST_CODE);
                }
            }
        });
        eliminarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usoLugar.ponerFoto(pos, "", foto);
            }
        });



        icono_fecha = findViewById(R.id.icono_fecha);
        icono_hora = findViewById(R.id.icono_hora);



        icono_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lugar lugar = adaptador.lugarPosicion(pos);

                pos = adaptador.posicionId(_id);
                obtenerFecha();
            }
        });

        icono_hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lugar lugar = adaptador.lugarPosicion(pos);

                pos = adaptador.posicionId(_id);
                obtenerHora();
            }
        });

        txtFecha = findViewById(R.id.fecha);
        txtHora = findViewById(R.id.hora);

    }

    /**
     * Resultado de la accion de botones de galeria foto y editar
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_EDITAR) {
            lugar = lugares.elemento(_id);
            pos = adaptador.posicionId(_id);
            actualizaVistas();
        }else if (requestCode == RESULTADO_GALERIA) {
            if (resultCode == Activity.RESULT_OK) {
                usoLugar.ponerFoto(pos, data.getDataString(), foto);
            } else {
                Toast.makeText(this, "Foto no cargada",Toast.LENGTH_LONG).show();
            }
        }else if (requestCode == RESULTADO_FOTO) {
            if (resultCode == Activity.RESULT_OK && uriUltimaFoto!=null) {
                lugar.setFoto(uriUltimaFoto.toString());
                usoLugar.ponerFoto(pos, lugar.getFoto(), foto);
            } else {
                Toast.makeText(this, "Error en captura", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Obtener fecha y sobreescribimos en la base de datos
     */
    public void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {


                Calendar cal = Calendar.getInstance();

                cal.setTimeInMillis(lugar.getFecha());

                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                lugar.setFecha(cal.getTimeInMillis());

                usoLugar.actualizaPosLugar(pos, lugar);
                txtFecha.setText(DateFormat.getDateInstance()
                        .format(new Date(cal.getTimeInMillis())));
                anio = year;
                mes = month;
                dia = dayOfMonth;


            }
        },anio, mes, dia);

        recogerFecha.getDatePicker();
        recogerFecha.show();

    }

    /**
     * Obtener hora y sobreescribimos en la base de datos
     */
    public void obtenerHora(){
        final TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(lugar.getFecha());
                calendario.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendario.set(Calendar.MINUTE, minute);
                lugar.setFecha(calendario.getTimeInMillis());
                usoLugar.actualizaPosLugar(pos, lugar);

                txtHora.setText(DateFormat.getTimeInstance().format(
                        new Date(calendario.getTimeInMillis())));
                hora = hourOfDay;
                minuto = minute;

            }

        }, hora, minuto, true);
        recogerHora.show();

    }

}
