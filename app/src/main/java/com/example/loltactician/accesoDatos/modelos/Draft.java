package com.example.loltactician.accesoDatos.modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

public class Draft implements Serializable{
    private String id;
    private String idUsuario;
    private String nombreDraft;
    private Date fechaCreacion;
    private List<Seleccion> selecciones;

    // Constructor por defecto necesario para Firebase
    public Draft() {
        this.selecciones = new ArrayList<>();
        this.fechaCreacion = new Date();
    }

    //Constructor con parámetros
    public Draft(String id, String idUsuario, String nombreDraft) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nombreDraft = nombreDraft;
        this.fechaCreacion = new Date();
        this.selecciones = new ArrayList<>();
    }

    //Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreDraft() {
        return nombreDraft;
    }

    public void setNombreDraft(String nombreDraft) {
        this.nombreDraft = nombreDraft;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<Seleccion> getSelecciones() {
        return selecciones;
    }

    public void setSelecciones(List<Seleccion> selecciones) {
        this.selecciones = selecciones;
    }


    public void addSeleccion(Seleccion seleccion) {
        if (this.selecciones == null) {
            this.selecciones = new ArrayList<>();
        }
        this.selecciones.add(seleccion);
    }

    public List<Seleccion> getPicksEquipoA() {
        List<Seleccion> picks = new ArrayList<>();
        if (selecciones != null) {
            for (Seleccion seleccion : selecciones) {
                if (seleccion.getTipo() == Seleccion.TipoSeleccion.PICK &&
                        seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_A) {
                    picks.add(seleccion);
                }
            }
        }
        return picks;
    }

    public List<Seleccion> getPicksEquipoB() {
        List<Seleccion> picks = new ArrayList<>();
        if (selecciones != null) {
            for (Seleccion seleccion : selecciones) {
                if (seleccion.getTipo() == Seleccion.TipoSeleccion.PICK &&
                        seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_B) {
                    picks.add(seleccion);
                }
            }
        }
        return picks;
    }

    public List<Seleccion> getBansEquipoA() {
        List<Seleccion> bans = new ArrayList<>();
        if (selecciones != null) {
            for (Seleccion seleccion : selecciones) {
                if (seleccion.getTipo() == Seleccion.TipoSeleccion.BAN &&
                        seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_A) {
                    bans.add(seleccion);
                }
            }
        }
        return bans;
    }

    /**
     * Obtiene los bans del equipo B (equipo rojo)
     */
    public List<Seleccion> getBansEquipoB() {
        List<Seleccion> bans = new ArrayList<>();
        if (selecciones != null) {
            for (Seleccion seleccion : selecciones) {
                if (seleccion.getTipo() == Seleccion.TipoSeleccion.BAN &&
                        seleccion.getEquipo() == Seleccion.Equipo.EQUIPO_B) {
                    bans.add(seleccion);
                }
            }
        }
        return bans;
    }
}

