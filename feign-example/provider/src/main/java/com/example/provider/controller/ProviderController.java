package com.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @GetMapping("/echo/{msg}")
    public Map<String, Object> echo(@PathVariable String msg) {
        return Map.of(
            "message", msg,
            "from", serviceName,
            "timestamp", System.currentTimeMillis()
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", serviceName
        );
    }
}
