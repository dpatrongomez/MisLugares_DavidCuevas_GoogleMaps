package com.example.mislugares_davidcuevas.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mislugares_davidcuevas.R;
import com.example.mislugares_davidcuevas.modelo.GeoPunto;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.modelo.RepositorioLugares;
import com.example.mislugares_davidcuevas.presentacion.Aplicacion;

/**
 * The type Adaptador lugares.
 * @author David Cuevas Cano
 * @version 0.5
 * @see androidx.recyclerview.widget.RecyclerView.Adapter
 */
public class AdaptadorLugares  extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> {

    protected View.OnClickListener onClickListener;

    protected RepositorioLugares lugares;         // Lista de lugares a mostrar


    public AdaptadorLugares(RepositorioLugares lugares) {
        this.lugares = lugares;
    }

    /**
     * View Holder de RecycleView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre,direccion;

        public ImageView foto;

        public RatingBar valoracion;

        public TextView distancia;

        /**
         * @param itemView
         */
        public ViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre);
            direccion = itemView.findViewById(R.id.direccion);
            foto = itemView.findViewById(R.id.foto);
            valoracion= itemView.findViewById(R.id.valoracion);
            distancia=itemView.findViewById(R.id.distancia);
        }

        /**
         * Personaliza.
         *
         * @param lugar the lugar
         */
        public void personaliza(Lugar lugar) {
            nombre.setText(lugar.getNombre());
            direccion.setText(lugar.getDireccion());
            int id = R.drawable.otros;
            switch(lugar.getTipo()) {
                case RESTAURANTE:id = R.drawable.restaurante; break;
                case BAR:    id = R.drawable.bar;     break;
                case COPAS:   id = R.drawable.copas;    break;
                case ESPECTACULO:id = R.drawable.espectaculos; break;
                case HOTEL:   id = R.drawable.hotel;    break;
                case COMPRAS:  id = R.drawable.compras;   break;
                case EDUCACION: id = R.drawable.educacion;  break;
                case DEPORTE:  id = R.drawable.deporte;   break;
                case NATURALEZA: id = R.drawable.naturaleza; break;
                case GASOLINERA: id = R.drawable.gasolinera; break;  }
            foto.setImageResource(id);
            foto.setScaleType(ImageView.ScaleType.FIT_END);
            valoracion.setRating(lugar.getValoracion());

            GeoPunto pos=((Aplicacion) itemView.getContext().getApplicationContext())
                    .posicionActual;
            if (pos.equals(GeoPunto.SIN_POSICION) ||
                    lugar.getPosicion().equals(GeoPunto.SIN_POSICION)) {
                distancia.setText("... Km");
            } else {
                int d=(int) pos.distancia(lugar.getPosicion());
                if (d < 2000) distancia.setText(d + " m");
                else          distancia.setText(d / 1000 + " Km");
            }
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.elementolista, parent, false);
        return new ViewHolder(v);


    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugares.elemento(posicion);
        holder.personaliza(lugar);
    }

    @Override public int getItemCount() {
        return lugares.tamanyo();
    }


    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener =  onClickListener;
    }


}

