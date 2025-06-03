package com.example.loltactician.accesoDatos.modelos;

import java.io.Serializable;

public class Campeon implements Serializable{
    private String id;
    private String nombre;
    private String rol;
    private int dificultad;
    private String descripcion;
    private double winrate;
    private String imagenUrl;

    //Constructor por defecto necesario para Firebase
    public Campeon() {
    }

    //Constructor completo
    public Campeon(String id, String nombre, String rol, int dificultad, String descripcion, double winrate, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.dificultad = dificultad;
        this.descripcion = descripcion;
        this.winrate = winrate;
        this.imagenUrl = imagenUrl;
    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getWinrate() {
        return winrate;
    }

    public void setWinrate(double winrate) {
        this.winrate = winrate;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}