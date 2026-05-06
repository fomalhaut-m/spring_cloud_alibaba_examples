package com.example.reactiveprovider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/provider")
public class ReactiveProviderController {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @GetMapping("/echo/{msg}")
    public Mono<Map<String, Object>> echo(@PathVariable String msg) {
        return Mono.just(Map.of(
            "message", msg,
            "from", serviceName,
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> health() {
        return Mono.just(Map.of(
            "status", "UP",
            "service", serviceName
        ));
    }
}