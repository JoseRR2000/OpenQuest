package com.example.openquest;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    // @SerializedName("success") indica que el campo JSON "success"
    // se mapeará a esta variable Java 'success'.
    @SerializedName("success")
    private boolean success;

    // @SerializedName("message") mapea el campo JSON "message"
    // a esta variable Java 'message'.
    @SerializedName("message")
    private String message;

    // @SerializedName("user_id") mapea el campo JSON "user_id"
    // a esta variable Java 'userId'.
    // ¡Asegúrate de que el nombre del campo JSON ('user_id') sea exactamente el que envía tu PHP!
    @SerializedName("user_id")
    private int userId;

    // Constructor (Retrofit/Gson puede no necesitarlo explícitamente para deserializar,
    // pero es buena práctica para la creación manual de objetos si fuera necesario)
    public LoginResponse(boolean success, String message, int userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    // --- Getters (Métodos para acceder a los valores) ---

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    // --- Setters (Opcionales si solo vas a deserializar, pero útiles si necesitas modificar el objeto) ---

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}