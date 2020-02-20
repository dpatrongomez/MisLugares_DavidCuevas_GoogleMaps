package com.example.mislugares_davidcuevas.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase LugaresLista que implementa la clase RepositorioLugares
 */
public class LugaresLista implements RepositorioLugares {


    protected List<Lugar> listaLugares;

    /**
     * Constructor para definir un ArrayList de Lugar y llamar el método de Añadir ejemplos
     */
    public LugaresLista() {
        listaLugares = new ArrayList<Lugar>();
        aniadeEjemplos();

    }

    /**
     * Método para devolver el lugar de ese id
     *
     * @param id Id del Lugar
     * @return Lugar
     */
    public Lugar elemento(int id) {
        return listaLugares.get(id);
    }

    /**
     * Método para añadir un Lugar a la lista
     *
     * @param lugar Lugar
     */
    public void aniade(Lugar lugar) {
        listaLugares.add(lugar);
    }

    /**
     * Método para crear un nuevo Lugar
     *
     * @return Id del Lugar agregado
     */
    public int nuevo() {
        Lugar lugar = new Lugar();
        listaLugares.add(lugar);
        return listaLugares.size()-1;
    }

    /**
     * Método para borrar un Lugar de la lista
     *
     * @param id Id del Lugar a borrar
     */
    public void borrar(int id) {
        listaLugares.remove(id);
    }

    /**
     * Método para obtener el tamaño de la lista de lugares
     *
     * @return Tamaño de la lista
     */
    public int tamanyo() {
        return listaLugares.size();
    }

    /**
     * Método para actualizar el Lugar indicado
     *
     * @param id    Id del Lugar que quiere editar
     * @param lugar Objeto Lugar con los nuevos datos
     */
    public void actualiza(int id, Lugar lugar) {
        listaLugares.set(id, lugar);
    }


    /**
     * Método para agregar objetos Lugar al ArrayList
     */
    public void aniadeEjemplos() {
        aniade(new Lugar("Escuela Politécnica Superior de Gandía",
                "C/ Paranimf, 1 46730 Gandia (SPAIN)", -0.166093, 38.995656,
                TipoLugar.EDUCACION, 962849300, "http://www.epsg.upv.es",
                "Uno de los mejores lugares para formarse.", 3));
        aniade(new Lugar("Al de siempre",
                "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
                -0.190642, 38.925857, TipoLugar.BAR, 636472405, "",
                "No te pierdas el arroz en calabaza.", 3));
        aniade(new Lugar("androidcurso.com",
                "ciberespacio", 0.0, 0.0, TipoLugar.EDUCACION,
                962849300, "http://androidcurso.com",
                "Amplia tus conocimientos sobre Android.", 5));
        aniade(new Lugar("Barranco del Infierno",
                "Vía Verde del río Serpis. Villalonga (Valencia)",
                -0.295058, 38.867180, TipoLugar.NATURALEZA, 0,
                "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-"+
                        "verde-del-rio.html","Espectacular ruta para bici o andar", 4));
        aniade(new Lugar("La Vital",
                "Avda. de La Vital, 0 46701 Gandía (Valencia)", -0.1720092,
                38.9705949, TipoLugar.COMPRAS, 962881070,
                "http://www.lavital.es/", "El típico centro comercial", 2));

    }
}