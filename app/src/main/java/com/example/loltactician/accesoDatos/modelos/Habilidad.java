package com.example.loltactician.accesoDatos.modelos;

// Clase para representar una habilidad
public class Habilidad {
    private String tecla;        // P, Q, W, E, R
    private String nombre;
    private String descripcion;
    private String imagenUrl;

    public Habilidad(String tecla, String nombre, String descripcion, String imagenUrl) {
        this.tecla = tecla;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    // Getters y setters
    public String getTecla() { return tecla; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenUrl() { return imagenUrl; }
}
