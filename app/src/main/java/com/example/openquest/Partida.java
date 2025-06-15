package com.example.openquest;


import com.google.gson.annotations.SerializedName;

import java.util.Date; // Date para la fecha
import java.util.List;

public class Partida {
    private int id;
    private int id_jugador;
    private String jugador;
    private int puntuacion;
    private Date fecha;

    private String dificultad;
    private int rondasJugadas;
    private List<PreguntasPorPartida> preguntasPartida;

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

    public List<PreguntasPorPartida> getPreguntasPartida() {
        return preguntasPartida;
    }

    public void setId(int id) { this.id = id; }
    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }
    public void setJugador(String jugador) {
        this.jugador = jugador;
    }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public void setRondasJugadas(int rondasJugadas) { this.rondasJugadas = rondasJugadas; }
    public void setPreguntasPartida(List<PreguntasPorPartida> preguntasPartida) {
        this.preguntasPartida = preguntasPartida;
    }
}