package com.example.mislugares_davidcuevas.presentacion;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugaresBD;
import com.example.mislugares_davidcuevas.casos_uso.CasosUsoLugar;
import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.modelo.TipoLugar;


/**
 * Clase EdicionLugarActivity
 * @author David Cuevas Cano
 */
public class EdicionLugarActivity extends AppCompatActivity {
    private LugaresBD lugares;
    private AdaptadorLugaresBD adaptador;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;
    private EditText nombre, direccion, telefono, url, comentario;
    private Spinner tipo;
    private int _id;



    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adaptador = ((Aplicacion) getApplication()).adaptador;
        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares, adaptador);

        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", -1);
        _id = extras.getInt("_id", -1);
        if (_id!=-1) lugar = lugares.elemento(_id);
        else         lugar = adaptador.lugarPosicion (pos);

        actualizaVistas();
    }

    /**
     * Actualiza vistas.
     */
    public void actualizaVistas() {
        nombre = findViewById(R.id.nombree);
        nombre.setText(lugar.getNombre());

        direccion = findViewById(R.id.direccione);
        direccion.setText(lugar.getDireccion());
        telefono = findViewById(R.id.telefonoe);
        telefono.setText(Integer.toString(lugar.getTelefono()));
        url = findViewById(R.id.urle);
        url.setText(lugar.getUrl());
        comentario = findViewById(R.id.comentarioe);
        comentario.setText(lugar.getComentario());

        tipo = findViewById(R.id.tipoSpinner);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancelar:
                if (_id!=-1) lugares.borrar(_id);
                finish();
                return true;
            case R.id.guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                if (_id==-1) _id = adaptador.idPosicion(pos);
                usoLugar.guardar(_id, lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


