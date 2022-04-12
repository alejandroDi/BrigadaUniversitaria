package com.example.brigadauniversitaria.common.pojo;

import com.google.firebase.database.Exclude;

public class Market {
    public static final String LATITUD ="latitud";
    public static final String LONGITUD ="longitud";
    public static final String CLASIFICACION ="clasificacion";
    public static final String LUGAR ="lugar";
    public static final String DESCRIPCION ="descripcion";


    private double latitud;
    private double longitud;
    private String clasificacion;
    private String lugar;
    private String descripcion;

    public Market() {
        super();
    }

    public Market(double latitud,double longitud,String clasificacion,String lugar,String descripcion) {

        this.latitud = latitud;
        this.longitud = longitud;
        this.clasificacion = clasificacion;
        this.lugar = lugar;
        this.descripcion = descripcion;
    }



    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
