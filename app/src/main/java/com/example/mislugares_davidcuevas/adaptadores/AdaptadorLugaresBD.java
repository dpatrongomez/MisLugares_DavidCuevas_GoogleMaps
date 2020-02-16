package com.example.mislugares_davidcuevas.adaptadores;

import android.database.Cursor;

import com.example.mislugares_davidcuevas.datos.LugaresBD;
import com.example.mislugares_davidcuevas.modelo.Lugar;
import com.example.mislugares_davidcuevas.modelo.RepositorioLugares;

/**
 * Adaptador de base de datos
 * @author David Cuevas Cano
 */
public class AdaptadorLugaresBD extends AdaptadorLugares  {


    protected Cursor cursor;

    /**
     * @param lugares the lugares
     * @param cursor  the cursor
     */
    public AdaptadorLugaresBD(RepositorioLugares lugares, Cursor cursor) {
        super(lugares);
        this.cursor = cursor;
    }

    /**
     * @return the cursor
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    /**
     * Lugar posicion lugar.
     *
     * @param posicion the posicion
     * @return the lugar
     */
    public Lugar lugarPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return LugaresBD.extraeLugar(cursor);
    }

    /**
     * @param posicion the posicion
     * @return the int
     */
    public int idPosicion(int posicion) {
        cursor.moveToPosition(posicion);
        return cursor.getInt(0);
    }

    /**
     * @param id the id
     * @return the int
     */
    public int posicionId(int id) {
        int pos = 0;
        while (pos<getItemCount() && idPosicion(pos)!=id) pos++;
        if (pos>= getItemCount()){
            return -1;
        }
        else{
            return pos;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int posicion) {
        Lugar lugar = lugarPosicion(posicion);
        holder.personaliza(lugar);
        holder.itemView.setTag(new Integer(posicion));
    }

    @Override public int getItemCount() {
        return cursor.getCount();
    }



}
