package com.example.brigadauniversitaria.common.pojo;

public class Contacto {
    public static final String NOMBRE ="nombre";
    public static final String TELEFONO ="telefono";

    String nombre;
    String telefono;


    public Contacto() {
    }

    public Contacto(String nombre,String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
