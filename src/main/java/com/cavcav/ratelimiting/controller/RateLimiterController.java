package com.cavcav.ratelimiting.controller;


import com.cavcav.ratelimiting.model.*;
import com.cavcav.ratelimiting.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


/**
 * REST Controller for Zomato-like rate limiting
 * <p>
 * This controller provides endpoints with dual rate limiting:
 * - Global: 10,000 requests per minute (system capacity)
 * - Per-user: 5 requests per minute per user (prevents spam)
 * <p>
 * Endpoints:
 * - /api/place-order: Order placement with dual rate limiting
 * - /api/forgot-password: Password reset with dual rate limiting
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RateLimiterController {

    @Autowired
    private RateLimiterService rateLimiterService;

    /**
     * 🍕 Place Order endpoint - Core Zomato functionality with dual rate limiting
     * Global: 10,000 orders per minute (system capacity)
     * Per-user: 5 orders per minute per user (prevents spam)
     * <p>
     * This is where the main throttling logic is applied for order placement
     */
    @PostMapping("/place-order")
    public ResponseEntity<OrderResponse> placeOrder(@RequestParam String userId,
                                                    @RequestParam String restaurantId,
                                                    @RequestParam String items,
                                                    @RequestParam(defaultValue = "299.00") String amount) {

        if (rateLimiterService.isAllowedForOrderPlacement(userId)) {
            // Create successful order response using POJOs
            String orderId = "ORD_" + System.currentTimeMillis();
            OrderData orderData = OrderData.create(userId, restaurantId, items, "₹" + amount);

            OrderResponse response = OrderResponse.success(
                    orderId,
                    orderData,
                    rateLimiterService.getRemainingTokensPerUser(userId),
                    rateLimiterService.getRemainingTokensFoodOrder()
            );

            return ResponseEntity.ok(response);
        } else {
            // Check which limit was exceeded and create appropriate error response
            RateLimiterService.RateLimitStatus status = rateLimiterService.getRateLimitStatus(userId);

            ErrorDetails errorDetails;
            String errorMessage;

            if (status.foodOrderRemaining() == 0) {
                errorDetails = ErrorDetails.systemOverloaded();
                errorMessage = "System is at capacity. Too many orders being placed right now. Please try again later.";
            } else {
                errorDetails = ErrorDetails.userOrderLimitExceeded();
                errorMessage = "You're placing orders too quickly. Please wait before placing another order.";
            }

            OrderResponse response = OrderResponse.rateLimited(
                    errorMessage,
                    errorDetails,
                    status.userRemaining(),
                    status.foodOrderRemaining()
            );

            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("X-Retry-After", "60")
                    .body(response);
        }
    }

    /**
     * Forgot Password endpoint with strict rate limiting
     * Rate: 3 requests per minute (shared across all users for security)
     * This is stricter than order placement for sensitive operations
     */
    @GetMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPasswordEndpoint(@RequestParam String email) {

        if (rateLimiterService.isAllowedForForgotPassword()) {
            // Create successful password reset response using POJOs
            PasswordResetData resetData = PasswordResetData.create(email, null);

            ForgotPasswordResponse response = ForgotPasswordResponse.success(
                    resetData,
                    rateLimiterService.getRemainingTokensForgotPassword(),
                    null // No global capacity for forgot password
            );

            return ResponseEntity.ok(response);
        } else {
            // Rate limited - too many forgot password attempts
            ErrorDetails errorDetails = ErrorDetails.passwordResetRateLimited();
            String errorMessage = "Too many password reset attempts. Please try again later.";

            ForgotPasswordResponse response = ForgotPasswordResponse.rateLimited(
                    errorMessage,
                    errorDetails,
                    rateLimiterService.getRemainingTokensForgotPassword(),
                    null // No global capacity for forgot password
            );

            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .header("X-Retry-After", "60")
                    .body(response);
        }
    }
}
