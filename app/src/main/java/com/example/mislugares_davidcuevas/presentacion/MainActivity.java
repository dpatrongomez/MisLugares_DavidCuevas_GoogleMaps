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
 * Clase MainActivity que es la encargada de mostrar la pantalla principal
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
    private int _id = -1;

    /**
     * Método para inicializar el layout, los listener y llenar las demás clases
     * @param savedInstanceState
     */
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
        /**
         * Método para inicializar los listener, en este el del floating button para que cuando lo pulsemos llame al método de crear un nuevo Lugar
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        /**
         * Nos permite identificar que gesto hemos realizado en la pantalla táctil.
         * <p>
         *     Sobre el vamos a sobreescribir el método onSingleTapUp y a devolver true.
         *     Lo que conseguimos con esto es que el Listener sólo reaccione, cuando hagamos un tapup.
         *     Hacemos click en la pantalla y levantamos el dedo.
         * </p>
         */
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        /**
         * Permite que la aplicación intercepte eventos táctiles en progreso en el nivel de la jerarquía de vistas de RecyclerView
         * antes de que esos eventos táctiles sean considerados para el propio comportamiento de desplazamiento de RecyclerView
         * <p>
         *     Puede ser útil para aplicaciones que desean implementar diversas formas de manipulación gestual de vistas de elementos dentro de RecyclerView.
         *     OnItemTouchListeners puede interceptar una interacción táctil que ya está en progreso
         * </p>
         */
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }

            /**
             * Se usa para controlar que hacemos click, Tapup sobre la lista
             * <p>
             *     con el, obtenemos en child el item que queremos capturar a partir de la posición X e Y del motionEvent.
             *     Esta clase se utiliza para informar de eventos de movimiento (mouse, bolígrafo, dedo...).
             *     Los eventos de movimiento pueden contener movimientos absolutos o relativos y otros datos, según el tipo de dispositivo.
             * </p>
             * @param recyclerView
             * @param motionEvent
             * @return verdadero o falso
             */
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                try {

                    /**
                     * Encuentra la primera vista, ítem de la lista justo debajo del punto dado.
                     * Cada Item de la lista se muestra como un ViewHolder, es una vista en sí (elementolista.xml)
                     */
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

    /**
     * Método para crear el menú superior con el menú establecido en el xml
     *
     * @param menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    /**
     * Método para definir las acciones de cada elemento del menú
     *
     * @param item
     * @return true
     */
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
     * Método para lanzar el Activity de Preferencias
     *¡
     */
    public void LanzarPreferencias(){
        Intent i = new Intent(this, PreferenciasActivity.class);
        startActivityForResult(i, RESULTADO_PREFERENCIAS);
    }

    /**
     * Lanza la clase acerca de.
     */
    public void LanzarAcercaDe(){
        Intent acercaDe = new Intent(MainActivity.this, AcercaDeActivity.class);
        startActivity(acercaDe);
    }

    /**
     * Método para abrir un Diálogo para escribir el id del lugar que quieres visualizar
     *
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
     * Lanza la clase que visualiza el mapa.
     */
    public void LanzarMapa(){
        Intent mapa = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(mapa);
    }

    /**
     * Método para inicializar la ReciclerView con su respectiva layout y adaptador
     * Ajustamos el tamaño a fijo
     * recyclerView.setHasFixedSize(true);
     * Ponemos de LayoutManager un Linear
     * recyclerView.setLayoutManager(new LinearLayoutManager(this));
     * Y cargamos el adaptador que vamos a definir.
     * recyclerView.setAdapter(adaptador);
     */
    public void inicializarReciclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);
    }


    /**
     * Método para controlar los permisos necesarios
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override public void onRequestPermissionsResult(int requestCode,
                                                     String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_LOCALIZACION
                && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            usoLocalizacion.permisoConcedido();

    }

    /**
     * Método que activa la localización cuando vuelve a estar en primer plano
     */
    @Override protected void onResume() {
        super.onResume();
        usoLocalizacion.activar();
    }


    /**
     * Método que desactiva la localización cuando se pausa la app
     */
    @Override protected void onPause() {
        super.onPause();
        usoLocalizacion.desactivar();
    }


    /**
     * Método que se llama cuando una activity se completa, desde aquí se controla el evento de modificar la configuración en preferencias
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override protected void onActivityResult(int requestCode, int resultCode,
                                              Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULTADO_PREFERENCIAS) {
            adaptador.setCursor(lugares.extraeCursor());
            adaptador.notifyDataSetChanged();
        }
    }

}