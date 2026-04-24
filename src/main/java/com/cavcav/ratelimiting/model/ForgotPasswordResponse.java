package com.cavcav.ratelimiting.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForgotPasswordResponse {

    private boolean success;
    private String message;
    private PasswordResetData data;
    private ErrorDetails error;
    private Long remainingAttempts;
    private Long globalCapacity;
    private String status;

    // Constructors
    public ForgotPasswordResponse() {}

    public ForgotPasswordResponse(boolean success, String message, String status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

    // Static factory methods
    public static ForgotPasswordResponse success(PasswordResetData data, Long remainingAttempts, Long globalCapacity) {
        ForgotPasswordResponse response = new ForgotPasswordResponse(true, "Password reset link sent to your email", "SUCCESS");
        response.setData(data);
        response.setRemainingAttempts(remainingAttempts);
        response.setGlobalCapacity(globalCapacity);
        return response;
    }

    public static ForgotPasswordResponse rateLimited(String message, ErrorDetails error, Long remainingAttempts, Long globalCapacity) {
        ForgotPasswordResponse response = new ForgotPasswordResponse(false, message, "RATE_LIMITED");
        response.setError(error);
        response.setRemainingAttempts(remainingAttempts);
        response.setGlobalCapacity(globalCapacity);
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PasswordResetData getData() {
        return data;
    }

    public void setData(PasswordResetData data) {
        this.data = data;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public Long getRemainingAttempts() {
        return remainingAttempts;
    }

    public void setRemainingAttempts(Long remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public Long getGlobalCapacity() {
        return globalCapacity;
    }

    public void setGlobalCapacity(Long globalCapacity) {
        this.globalCapacity = globalCapacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}