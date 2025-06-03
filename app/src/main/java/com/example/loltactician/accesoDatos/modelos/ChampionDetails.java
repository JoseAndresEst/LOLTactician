package com.example.loltactician.accesoDatos.modelos;

import java.util.List;

public  class ChampionDetails {
    private String id;
    private String nombre;
    private String titulo;
    private String historia;
    private List<String> roles;
    private ChampionStats stats;
    private List<Habilidad> habilidades;
    private String imagenUrl;

    public ChampionDetails(String id, String nombre, String titulo, String historia,
                           List<String> roles, ChampionStats stats,
                           List<Habilidad> habilidades, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.titulo = titulo;
        this.historia = historia;
        this.roles = roles;
        this.stats = stats;
        this.habilidades = habilidades;
        this.imagenUrl = imagenUrl;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTitulo() { return titulo; }
    public String getHistoria() { return historia; }
    public List<String> getRoles() { return roles; }
    public ChampionStats getStats() { return stats; }
    public List<Habilidad> getHabilidades() { return habilidades; }
    public String getImagenUrl() { return imagenUrl; }
}

