package com.example.openquest;

import com.google.gson.annotations.SerializedName; // Importa si los nombres JSON no coinciden directamente

public class ApiResponse {

    @SerializedName("mensaje")
    private String mensaje;
    @SerializedName("error")
    private String error;
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private Integer id;

    public ApiResponse() {

    }

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
