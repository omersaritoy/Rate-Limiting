package com.cavcav.ratelimiting.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response DTO for order placement API
 * Represents the response structure for /api/place-order endpoint
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private boolean success;
    private String message;
    private String orderId;
    private OrderData data;
    private ErrorDetails error;
    private Long remainingOrders;
    private Long systemCapacity;
    private String status;

    // Constructors
    public OrderResponse() {}

    public OrderResponse(boolean success, String message, String status) {
        this.success = success;
        this.message = message;
        this.status = status;
    }

    // Static factory methods for success and error responses
    public static OrderResponse success(String orderId, OrderData data, Long remainingOrders, Long systemCapacity) {
        OrderResponse response = new OrderResponse(true, "Order placed successfully!", "SUCCESS");
        response.setOrderId(orderId);
        response.setData(data);
        response.setRemainingOrders(remainingOrders);
        response.setSystemCapacity(systemCapacity);
        return response;
    }

    public static OrderResponse rateLimited(String message, ErrorDetails error, Long remainingOrders, Long systemCapacity) {
        OrderResponse response = new OrderResponse(false, message, "RATE_LIMITED");
        response.setError(error);
        response.setRemainingOrders(remainingOrders);
        response.setSystemCapacity(systemCapacity);
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public Long getRemainingOrders() {
        return remainingOrders;
    }

    public void setRemainingOrders(Long remainingOrders) {
        this.remainingOrders = remainingOrders;
    }

    public Long getSystemCapacity() {
        return systemCapacity;
    }

    public void setSystemCapacity(Long systemCapacity) {
        this.systemCapacity = systemCapacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
