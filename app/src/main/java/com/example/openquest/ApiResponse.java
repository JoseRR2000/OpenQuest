package com.example.openquest;

import com.google.gson.annotations.SerializedName; // Importa si los nombres JSON no coinciden directamente

public class ApiResponse {

    @SerializedName("mensaje")
    private String mensaje;
    @SerializedName("error")
    private String error;
    @SerializedName("success") // El nombre en el JSON de PHP
    private boolean success;

    @SerializedName("message") // El nombre en el JSON de PHP
    private String message;

    @SerializedName("id") // El nombre en el JSON de PHP (puede ser null si es un error)
    private Integer id; // Usamos Integer (objeto) en lugar de int (primitivo) para permitir valores nulos

    // Constructor sin argumentos (necesario para Gson)
    public ApiResponse() {

    }

    // Getters para acceder a los datos
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Integer getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getError() {
        return error;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
