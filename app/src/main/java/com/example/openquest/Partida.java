package com.example.openquest;

import com.google.gson.annotations.SerializedName; // Importa esto si usas Gson y los nombres JSON difieren

import java.util.Date; // Date para la fecha

public class Partida {
    private int id;
    private int jugador; // Asumo que esto es el ID del jugador
    private String resultado;
    private int puntuacion; // Nombre del campo para la puntuación
    private Date fecha;     // Tipo Date para la fecha (como lo usas en el adapter)

    // *** NUEVOS CAMPOS AÑADIDOS ***
    private String dificultad; // Para almacenar la dificultad ("Fácil", "Normal", "Difícil")
    private int rondasJugadas; // Para almacenar el número de rondas jugadas

    // Constructor sin argumentos (necesario para Gson/Retrofit)
    public Partida() {

    }

    // Constructor con todos los campos (útil para crear objetos manualmente)
    public Partida(int id, int jugador, String resultado, int puntuacion, Date fecha, String dificultad, int rondasJugadas) {
        this.id = id;
        this.jugador = jugador;
        this.resultado = resultado;
        this.puntuacion = puntuacion;
        this.fecha = fecha;
        this.dificultad = dificultad;
        this.rondasJugadas = rondasJugadas;
    }

    // Getters para todos los campos (incluyendo los nuevos)

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
        return puntuacion; // Coincide con el nombre del campo
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

    // Opcional: Setters si necesitas modificar los datos después de la creación
    public void setId(int id) { this.id = id; }
    public void setJugador(int jugador) { this.jugador = jugador; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public void setRondasJugadas(int rondasJugadas) { this.rondasJugadas = rondasJugadas; }

    // Si tu API PHP devuelve JSON con nombres de clave diferentes, usa @SerializedName:
    // Por ejemplo, si PHP devuelve "score" en lugar de "puntuacion":
    // @SerializedName("score")
    // private int puntuacion;
}