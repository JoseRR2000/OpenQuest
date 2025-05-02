package com.example.openquest;

import java.time.LocalDateTime;

public class Partida {
    private int id;
    private int jugador;
    private String resultado;
    private int puntuacion;
    private LocalDateTime fecha;

    //Getters
    public int getId() {
        return id;
    }

    public int getJugador() {
        return jugador;
    }

    public String getResultado() {
        return resultado;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
