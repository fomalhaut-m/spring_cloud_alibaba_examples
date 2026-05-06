package com.example.reactiveconsumer.controller;

import com.example.reactiveconsumer.feign.ProviderReactiveFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/consumer")
public class ReactiveConsumerController {

    private final ProviderReactiveFeignClient providerFeignClient;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    public ReactiveConsumerController(ProviderReactiveFeignClient providerFeignClient) {
        this.providerFeignClient = providerFeignClient;
    }

    @GetMapping("/reactive/{msg}")
    public Mono<Map<String, Object>> reactiveEcho(@PathVariable String msg) {
        return providerFeignClient.echo(msg)
            .map(providerResponse -> Map.<String, Object>of(
                "success", true,
                "provider", providerResponse,
                "consumer", serviceName
            ))
            .onErrorResume(e -> Mono.just(Map.of(
                "success", false,
                "error", e.getMessage()
            )));
    }

    @GetMapping("/health")
    public Mono<Map<String, String>> health() {
        return Mono.just(Map.of(
            "status", "UP",
            "service", serviceName
        ));
    }
}