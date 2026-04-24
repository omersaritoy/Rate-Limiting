package com.cavcav.ratelimiting.config;


import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitConfig {


    /*
      Configures a Bucket4j bucket with a capacity of 5 tokens that refills at a rate of 5 tokens per minute.
      Each token represents the capacity to handle one request.
    */

    @Bean
    public Bucket perUserRateLimit() {

        //Define the bandwidth with a limit of 5 tokens, refilled every minute

        return Bucket.builder().addLimit(limit->
                //max request
                limit.capacity(5)
                        //add 1 token every 6 seconds
                        .refillGreedy(1, Duration.ofSeconds(6))
                        //starting 10 request after this it requests will be slow
                        .initialTokens(5)).build();
    }


    //create bucket for every ip
    @Bean
    public Map<String,Bucket> buckets() {
        return new ConcurrentHashMap<>();
    }
}
