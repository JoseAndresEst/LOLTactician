package com.example.loltactician.accesoDatos.modelos;

import java.io.Serializable;

public class Seleccion implements Serializable {

    /**
     * Enumeración para el tipo de selección (pick o ban)
     */
    public enum TipoSeleccion {
        PICK,
        BAN
    }

    /**
     * Enumeración para el equipo que realiza la selección
     */
    public enum Equipo {
        EQUIPO_A, // Equipo azul
        EQUIPO_B  // Equipo rojo
    }

    private String id;
    private TipoSeleccion tipo;
    private Equipo equipo;
    private int ordenSeleccion;
    private String idCampeon;

    // Constructor por defecto necesario para Firebase
    public Seleccion() {
    }

    // Constructor con parámetros
    public Seleccion(String id, TipoSeleccion tipo, Equipo equipo, int ordenSeleccion, String idCampeon) {
        this.id = id;
        this.tipo = tipo;
        this.equipo = equipo;
        this.ordenSeleccion = ordenSeleccion;
        this.idCampeon = idCampeon;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipoSeleccion getTipo() {
        return tipo;
    }

    public void setTipo(TipoSeleccion tipo) {
        this.tipo = tipo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getOrdenSeleccion() {
        return ordenSeleccion;
    }

    public void setOrdenSeleccion(int ordenSeleccion) {
        this.ordenSeleccion = ordenSeleccion;
    }

    public String getIdCampeon() {
        return idCampeon;
    }

    public void setIdCampeon(String idCampeon) {
        this.idCampeon = idCampeon;
    }
}
