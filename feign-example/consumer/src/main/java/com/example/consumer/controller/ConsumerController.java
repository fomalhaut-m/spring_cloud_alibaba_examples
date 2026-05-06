package com.example.consumer.controller;

import com.example.consumer.feign.ProviderFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private final ProviderFeignClient providerFeignClient;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    public ConsumerController(ProviderFeignClient providerFeignClient) {
        this.providerFeignClient = providerFeignClient;
    }

    @GetMapping("/feign/{msg}")
    public Map<String, Object> feignEcho(@PathVariable String msg) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> providerResponse = providerFeignClient.echo(msg);
            result.put("success", true);
            result.put("provider", providerResponse);
            result.put("consumer", serviceName);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
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