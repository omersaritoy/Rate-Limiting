package com.cavcav.ratelimiting.model;

import java.time.Instant;

public class PasswordResetData {
    private String email;
    private String userId;
    private String resetToken;
    private String expiresIn;
    private String sentAt;

    // Constructors
    public PasswordResetData() {}

    public PasswordResetData(String email, String userId) {
        this.email = email;
        this.userId = userId;
        this.resetToken = "RESET_" + System.currentTimeMillis();
        this.expiresIn = "15 minutes";
        this.sentAt = Instant.now().toString();
    }

    // Static factory method
    public static PasswordResetData create(String email, String userId) {
        return new PasswordResetData(email, userId);
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }
}
