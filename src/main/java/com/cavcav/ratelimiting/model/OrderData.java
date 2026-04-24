package com.cavcav.ratelimiting.model;


import java.time.Instant;

/**
 * Order data DTO containing order details
 * Used within OrderResponse for successful order placement
 */
public class OrderData {

    private String userId;
    private String restaurantId;
    private String items;
    private String amount;
    private String estimatedDelivery;
    private String orderStatus;
    private String placedAt;

    // Constructors
    public OrderData() {}

    public OrderData(String userId, String restaurantId, String items, String amount) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
        this.amount = amount;
        this.estimatedDelivery = "30-45 minutes";
        this.orderStatus = "CONFIRMED";
        this.placedAt = Instant.now().toString();
    }

    // Static factory method
    public static OrderData create(String userId, String restaurantId, String items, String amount) {
        return new OrderData(userId, restaurantId, items, amount);
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getEstimatedDelivery() {
        return estimatedDelivery;
    }

    public void setEstimatedDelivery(String estimatedDelivery) {
        this.estimatedDelivery = estimatedDelivery;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPlacedAt() {
        return placedAt;
    }

    public void setPlacedAt(String placedAt) {
        this.placedAt = placedAt;
    }
}
