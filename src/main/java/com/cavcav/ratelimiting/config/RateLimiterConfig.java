package com.cavcav.ratelimiting.config;


import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    /*
            Food Order System Capacity:10000 orders per minute
            Protects overall food ordering system from overload during peak hours
    */

    @Bean("foodOrderRateLimit")
    public Bucket foodOrderRateLimit() {
        return Bucket.builder()
                .addLimit(limit ->
                        limit.capacity(10000)
                                .refillGreedy(10000, Duration.ofMinutes(1))
                                .initialTokens(10000)

                ).build();
    }

    /*
        Per-User Rate Limiter: 5 requests per minute per user
        Prevents individual user spam and ensures fair usage
        Each userId gets their own bucket with this configuration
    */

    @Bean("perUserRateLimiter")
    public Bucket perUserRateLimiter() {
        return Bucket.builder().addLimit(limit ->
                limit.capacity(5)
                        .refillGreedy(5, Duration.ofMinutes(1))
                        .initialTokens(5)).build();
    }

    /*
            Forgot Password Rate Limiter: 3 requests per minute
            Stricter rate limiting for sensitive password reset operations
    */

    @Bean("forgotPasswordRateLimiter")
    public Bucket forgotPasswordRateLimiter() {
        return Bucket.builder().addLimit(limit ->
                limit.capacity(3)
                        .refillGreedy(3, Duration.ofMinutes(1))
                        .initialTokens(3)).build();
    }

}


