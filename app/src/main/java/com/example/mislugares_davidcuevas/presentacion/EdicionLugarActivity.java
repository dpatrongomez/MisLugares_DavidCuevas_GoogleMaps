package com.example.mislugares_davidcuevas.presentacion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.adaptadores.AdaptadorLugares;
import com.example.mislugares_davidcuevas.casos_uso.CasosUsoLugar;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.modelo.RepositorioLugares;
import com.example.mislugares_davidcuevas.modelo.TipoLugar;


public class EdicionLugarActivity extends AppCompatActivity {
    private RepositorioLugares lugares;
    private CasosUsoLugar usoLugar;
    private int pos;
    private Lugar lugar;
    private EditText nombre, direccion, telefono, url, comentario;
    private Spinner tipo;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("pos", 1);
        lugares = ((Aplicacion) getApplication()).lugares;
        usoLugar = new CasosUsoLugar(this, lugares);
        lugar = lugares.elemento(pos);
        actualizaVistas(pos);

        tipo = findViewById(R.id.tipoSpinner);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());

    }

    public void actualizaVistas(int pos) {
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

    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancelar:
                finish();
                return true;
            case R.id.guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());

                lanzarGuardado();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void lanzarGuardado(){
        new AlertDialog.Builder(this)
                .setTitle("Guardar Lugar")
                .setMessage("¿Desea guardar los cambios?")
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        usoLugar.guardar(pos,lugar);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }
}


