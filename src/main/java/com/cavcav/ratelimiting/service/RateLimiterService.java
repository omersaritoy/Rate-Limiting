package com.cavcav.ratelimiting.service;


import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Bucket foodOrderRateLimiter;
    private final Bucket forgotPasswordRateLimiter;
    private final Map<String, Bucket> userBuckets = new ConcurrentHashMap<>();


    public RateLimiterService(@Qualifier("foodOrderRateLimit") Bucket foodOrderRateLimiter, @Qualifier("forgotPasswordRateLimiter") Bucket forgotPasswordRateLimiter) {
        this.foodOrderRateLimiter = foodOrderRateLimiter;
        this.forgotPasswordRateLimiter = forgotPasswordRateLimiter;

    }

    /**
     * Check if request is allowed for food orders (system capacity check)
     * Food order limit: 10,000 orders per minute
     */
    public boolean isAllowedFoodOrder() {
        return foodOrderRateLimiter.tryConsume(1);
    }

    /**
     * Check if request is allowed for a specific user
     * Per-user limit: 5 requests per minute
     * Creates a new bucket for the user if it doesn't exist
     */
    public boolean isAllowedPerUser(String userId) {
        Bucket userBucket = userBuckets.computeIfAbsent(userId,
                k -> Bucket.builder()
                        .addLimit(limit ->
                                limit.capacity(5)
                                        .refillGreedy(5, Duration.ofMinutes(1))
                                        .initialTokens(5)).build());
        return userBucket.tryConsume(1);
    }
    /**
     * 🍕 Dual rate limiting check for ORDER PLACEMENT (main Zomato functionality)
     * Both food order system AND per-user limits must pass for the order to be allowed
     * This is the core method where throttling is applied for orders
     */
    public boolean isAllowedForOrderPlacement(String userId) {
        // Check food order system capacity first (fail fast if system is overloaded)
        if (!foodOrderRateLimiter.tryConsume(1)) {
            return false; // Food order system capacity exceeded - system overloaded
        }

        // Check per-user limit
        return isAllowedPerUser(userId); // User limit exceeded - user placing orders too quickly
        // Both limits passed - order can be placed
    }

    /**
     * 📧 Forgot Password rate limiting check
     * Uses a stricter 3 requests per minute limit for security
     * This is a single shared bucket for all forgot password requests
     */
    public boolean isAllowedForForgotPassword() {
        return forgotPasswordRateLimiter.tryConsume(1);
    }

    /**
     * Get detailed consumption information for food order rate limiter
     */
    public ConsumptionProbe getConsumptionProbeFoodOrder() {
        return foodOrderRateLimiter.tryConsumeAndReturnRemaining(1);
    }

    /**
     * Get detailed consumption information for per-user rate limiter
     */
    public ConsumptionProbe getConsumptionProbePerUser(String userId) {
        Bucket userBucket = userBuckets.computeIfAbsent(userId,
                k -> Bucket.builder()
                        .addLimit(limit ->
                                limit.capacity(5)
                                        .refillGreedy(5, Duration.ofMinutes(1))
                                        .initialTokens(5)).build());
        return userBucket.tryConsumeAndReturnRemaining(1);
    }

    /**
     * Get remaining tokens for food order rate limiter
     */
    public long getRemainingTokensFoodOrder() {
        return foodOrderRateLimiter.getAvailableTokens();
    }

    /**
     * Get remaining tokens for per-user rate limiter
     */
    public long getRemainingTokensPerUser(String userId) {
        Bucket userBucket = userBuckets.get(userId);
        return userBucket != null ? userBucket.getAvailableTokens() : 5; // Return max if user hasn't made requests yet
    }

    /**
     * Get remaining tokens for forgot password rate limiter
     */
    public long getRemainingTokensForgotPassword() {
        return forgotPasswordRateLimiter.getAvailableTokens();
    }


    /**
     * Get comprehensive rate limiting status for a user
     * Useful for monitoring and debugging
     */
    public RateLimitStatus getRateLimitStatus(String userId) {
        return new RateLimitStatus(
                getRemainingTokensFoodOrder(),
                getRemainingTokensPerUser(userId),
                userBuckets.containsKey(userId)
        );
    }

    /**
     * Reset user's rate limit (for testing purposes)
     */
    public void resetUserRateLimit(String userId) {
        userBuckets.remove(userId);
    }

    /**
     * Get all user buckets (for monitoring purposes)
     */
    public Map<String, Bucket> getAllUserBuckets() {
        return Map.copyOf(userBuckets);
    }


    public record RateLimitStatus(long foodOrderRemaining, long userRemaining, boolean userHasBucket) {
    }

}
