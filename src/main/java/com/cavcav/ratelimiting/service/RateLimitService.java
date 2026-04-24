package com.cavcav.ratelimiting.service;


import com.cavcav.ratelimiting.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RateLimitService {
    private final Map<String, Bucket> bucketCache;
    private final RateLimitConfig rateLimitConfig;

    public RateLimitService(Map<String, Bucket> bucketCache, RateLimitConfig rateLimitConfig) {
        this.bucketCache = bucketCache;
        this.rateLimitConfig = rateLimitConfig;
        System.out.println(rateLimitConfig.perUserRateLimit() == rateLimitConfig.perUserRateLimit());

    }

    public boolean tryConsume(String ipAddress) {
        // IP'ye ait bucket yoksa yeni oluştur
        Bucket bucket = bucketCache.computeIfAbsent(
                ipAddress,
                ip -> rateLimitConfig.perUserRateLimit()
        );

        // 1 token tüketmeye çalış
        return bucket.tryConsume(1);
    }

    // Kalan token sayısını döner (monitoring için)
    public long getAvailableTokens(String ipAddress) {
        Bucket bucket = bucketCache.get(ipAddress);
        return bucket != null ? bucket.getAvailableTokens() : -1;
    }
}
