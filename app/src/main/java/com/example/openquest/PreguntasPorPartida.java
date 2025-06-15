package com.example.openquest;

import com.google.gson.annotations.SerializedName;

public class PreguntasPorPartida {
    @SerializedName("idPregunta")
    private int idPregunta;
    @SerializedName("orden_aparicion")
    private int ordenAparicion;
    @SerializedName("respondida_correctamente")
    private boolean respondidaCorrectamente;

    // Constructor
    public PreguntasPorPartida(int idPregunta, int ordenAparicion, boolean respondidaCorrectamente) {
        this.idPregunta = idPregunta;
        this.ordenAparicion = ordenAparicion;
        this.respondidaCorrectamente = respondidaCorrectamente;
    }
    public int getIdPregunta() { return idPregunta; }
    public int getOrdenAparicion() { return ordenAparicion; }
    public boolean isRespondidaCorrectamente() { return respondidaCorrectamente; }
}
