package com.example.openquest;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("username")
    private String username;

    public LoginResponse(boolean success, String message, int userId) {
        this.success = success;
        this.message = message;
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}