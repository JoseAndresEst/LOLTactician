package com.example.loltactician.accesoDatos.modelos;

// Clase para estadísticas de un campeón
public class ChampionStats {
    private int ataque;
    private int defensa;
    private int magia;
    private int dificultad;

    public ChampionStats(int ataque, int defensa, int magia, int dificultad) {
        this.ataque = ataque;
        this.defensa = defensa;
        this.magia = magia;
        this.dificultad = dificultad;
    }

    // Getters
    public int getAtaque() { return ataque; }
    public int getDefensa() { return defensa; }
    public int getMagia() { return magia; }
    public int getDificultad() { return dificultad; }
}
