package com.cavcav.ratelimiting.model;

/**
 * Error details DTO for rate limiting errors
 * Provides structured error information in API responses
 */

public class ErrorDetails {
    private String code;
    private String type;
    private String details;
    private String suggestion;

    public ErrorDetails(String code, String type, String details, String suggestion) {
        this.code = code;
        this.type = type;
        this.details = details;
        this.suggestion = suggestion;
    }

    // Static factory methods for common error types
    public static ErrorDetails systemOverloaded() {
        return new ErrorDetails(
                "SYSTEM_OVERLOADED",
                "TooManyOrders",
                "System is at capacity. Too many orders being placed right now. Please try again later.",
                "Our kitchen is busy! Try again in a minute."
        );
    }

    public static ErrorDetails userOrderLimitExceeded() {
        return new ErrorDetails(
                "USER_ORDER_LIMIT_EXCEEDED",
                "TooManyOrders",
                "You're placing orders too quickly. Please wait before placing another order.",
                "Take a moment to review your order, then try again."
        );
    }

    public static ErrorDetails userRateLimited() {
        return new ErrorDetails(
                "USER_RATE_LIMITED",
                "TooManyRequests",
                "Too many password reset attempts. Please try again later.",
                "Please wait before trying again."
        );
    }

    public static ErrorDetails passwordResetRateLimited() {
        return new ErrorDetails(
                "RATE_LIMITED",
                "TooManyRequests",
                "You have exceeded the maximum number of password reset attempts. Please wait before trying again.",
                "Please wait before trying again."
        );
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
