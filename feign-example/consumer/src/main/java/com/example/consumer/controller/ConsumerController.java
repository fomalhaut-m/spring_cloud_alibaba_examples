package com.example.consumer.controller;

import com.example.consumer.feign.ProviderFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            Map<String, Object> providerResponse = providerFeignClient.echo(msg);
            return Map.of(
                "success", true,
                "provider", providerResponse,
                "consumer", serviceName
            );
        } catch (Exception e) {
            return Map.of(
                "success", false,
                "error", e.getMessage()
            );
        }
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", serviceName
        );
    }
}
