package com.example.provider.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/provider")
public class ProviderController {

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @GetMapping("/echo/{msg}")
    public Map<String, Object> echo(@PathVariable String msg) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", msg);
        result.put("from", serviceName);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", serviceName);
        return result;
    }
}