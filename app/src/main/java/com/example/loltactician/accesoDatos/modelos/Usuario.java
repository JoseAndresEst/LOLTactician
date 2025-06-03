package com.example.loltactician.accesoDatos.modelos;

import java.util.Date;
import java.util.List;

/**
 * Clase que representa un usuario de la aplicación
 */
public class Usuario {
    private String id;
    private String nombreUsuario;
    private String email;
    private Date fechaRegistro;
    private List<String> campeonesFavoritos;

    // Constructor por defecto necesario para Firebase
    public Usuario() {
        this.fechaRegistro = new Date();
    }

    // Constructor con parámetros
    public Usuario(String id, String nombreUsuario, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.fechaRegistro = new Date();
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<String> getCampeonesFavoritos() {
        return campeonesFavoritos;
    }

    public void setCampeonesFavoritos(List<String> campeonesFavoritos) {
        this.campeonesFavoritos = campeonesFavoritos;
    }
}
