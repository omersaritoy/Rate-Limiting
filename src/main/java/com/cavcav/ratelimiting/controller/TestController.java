package com.cavcav.ratelimiting.controller;


import com.cavcav.ratelimiting.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    private final RateLimitService rateLimitService;

    public TestController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @GetMapping("/hello")
    public ResponseEntity<Map<String, Object>> hello(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        long remaining = rateLimitService.getAvailableTokens(ip);

        return ResponseEntity.ok(Map.of(
                "message", "Merhaba!",
                "remainingTokens", remaining
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        long remaining = rateLimitService.getAvailableTokens(ip);

        return ResponseEntity.ok(Map.of(
                "ip", ip,
                "remainingTokens", remaining,
                "status", remaining > 0 ? "OK" : "RATE_LIMITED"
        ));
    }
}
