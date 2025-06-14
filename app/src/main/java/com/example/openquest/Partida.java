package com.example.openquest;


import java.util.Date; // Date para la fecha

public class Partida {
    private int id;
    private int id_jugador;
    private String jugador;
    private String resultado;
    private int puntuacion; // Nombre del campo para la puntuación
    private Date fecha;     // Tipo Date para la fecha)

    private String dificultad; // Para almacenar la dificultad ("Fácil", "Normal", "Difícil")
    private int rondasJugadas; // Para almacenar el número de rondas jugadas

    // Constructor sin argumentos (necesario para Gson/Retrofit)
    public Partida() {

    }

    public int getId() {
        return id;
    }
    public int getId_jugador() {
        return id_jugador;
    }
    public String getJugador() {
        return jugador;
    }
    public String getResultado() {
        return resultado;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getDificultad() { // Nuevo getter
        return dificultad;
    }

    public int getRondasJugadas() { // Nuevo getter
        return rondasJugadas;
    }

    public void setId(int id) { this.id = id; }
    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }
    public void setJugador(String jugador) {
        this.jugador = jugador;
    }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public void setRondasJugadas(int rondasJugadas) { this.rondasJugadas = rondasJugadas; }
}