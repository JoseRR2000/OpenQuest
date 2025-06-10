package com.example.openquest;

import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    // Constructor (opcional)
    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    // Setters (opcionales si solo deserializas)
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
