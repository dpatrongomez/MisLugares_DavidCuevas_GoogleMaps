package com.example.mislugares_davidcuevas.presentacion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.casos_uso.CasoUsoAlmacenamiento;
import com.example.mislugares_davidcuevas.casos_uso.CasoUsoLocalizacion;
import com.example.mislugares_davidcuevas.casos_uso.CasosUsoLugar;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.mapas.MapsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Clase Main donde se inicializa el RecycleView
 * @author David Cuevas Cano
 */
public class MainActivity extends AppCompatActivity {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private static final int MY_WRITE_REQUEST_CODE = 2;
    private CasosUsoLugar usoLugar;
    private CasoUsoAlmacenamiento usoAlmacenamiento;
    private RecyclerView recyclerView;

    static final int RESULTADO_PREFERENCIAS = 0;
    private static final int SOLICITUD_PERMISO_LOCALIZACION = 1;
    private CasoUsoLocalizacion usoLocalizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        lugares = ((Aplicacion) getApplication()).lugares;

        usoLugar = new CasosUsoLugar(this, lugares, adaptador);

        //permisos

        usoAlmacenamiento = new CasoUsoAlmacenamiento(MainActivity.this, MY_WRITE_REQUEST_CODE);
        usoAlmacenamiento.solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                "Necesita permisos de almacenamiento para añadir fotografías",MY_WRITE_REQUEST_CODE);
        usoLocalizacion = new CasoUsoLocalizacion(this,
                SOLICITUD_PERMISO_LOCALIZACION);
        setSupportActionBar(toolbar);

        inicializarReciclerView();


        adaptador.setOnItemClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int pos =(Integer)(v.getTag());
                usoLugar.mostrar(pos);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usoLugar.nuevo();
            }
        });


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {
                    View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                    if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {

                        int pos = recyclerView.getChildAdapterPosition(child);
                        usoLugar.mostrar(pos);
                        Toast.makeText(MainActivity.this,"Seleccionado el lugar numero: "+ pos ,Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            LanzarPreferencias();
            return true;
        }
        if (id == R.id.menu_buscar) {
            lanzarVistaLugar();
            return true;
        }

        if (id == R.id.menu_acercaDe){
            LanzarAcercaDe();
            return  true;
        }
        if (id==R.id.menu_mapa) {
            LanzarMapa();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Lanzar preferencias.
     */
    public void LanzarPreferencias(){
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivityForResult(i, RESULTADO_PREFERENCIAS);
    }

    /**
     * Lanzar acerca de.
     */
    public void LanzarAcercaDe(){
        Intent acercaDe = new Intent(MainActivity.this, AcercaDeActivity.class);
        startActivity(acercaDe);
    }

    /**
     * Lanzar vista lugar.
     */
    public void lanzarVistaLugar(){
        final EditText entrada = new EditText(this);
        entrada.setText("0");
        new AlertDialog.Builder(this)
            .setTitle("Selección de lugar")
            .setMessage("indica su id:")
            .setView(entrada)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    int id = Integer.parseInt (entrada.getText().toString());
                    usoLugar.mostrar(id);
                }})
            .setNegativeButton("Cancelar", null)
            .show();
    }

    /**
     * Lanzar mapa.
     */
    public void LanzarMapa(){
        Intent mapa = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(mapa);
    }

    /**
     * Inicializar recyclerView.
     */
    public void inicializarReciclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }

    @Override public void onRequestPermissionsResult(int requestCode,
                                                     String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido();

    }


    @Override protected void onResume() {
        super.onResume();
        usoLocalizacion.activar();
    }

    @Override protected void onPause() {
        super.onPause();
        usoLocalizacion.desactivar();
    }
    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_PREFERENCIAS) {
            adaptador.setCursor(lugares.extraeCursor());
            adaptador.notifyDataSetChanged();
        }
    }

}