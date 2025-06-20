package com.example.openquest;

import com.google.gson.annotations.SerializedName;

public class Pregunta {
    @SerializedName("idPregunta")
    private int idPregunta;
    @SerializedName("pregunta")
    private String pregunta;
    @SerializedName("categoria")
    private String categoria;
    @SerializedName("respuesta1")
    private String respuesta1;
    @SerializedName("respuesta2")
    private String respuesta2;
    @SerializedName("respuesta3")
    private String respuesta3;
    @SerializedName("respuesta4")
    private String respuesta4;
    @SerializedName("respuestaCorrecta")
    private String respuestaCorrecta;

    //Getters y Setters
    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public String getRespuesta4() {
        return respuesta4;
    }

    public void setRespuesta4(String respuesta4) {
        this.respuesta4 = respuesta4;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
}
