package com.example.loadbalancer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lb")
public class LBController {

    private final ReactiveDiscoveryClient reactiveDiscoveryClient;
    private final WebClient.Builder webClientBuilder;
    private final ReactorLoadBalancer<ServiceInstance> loadBalancer;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    public LBController(
            ReactiveDiscoveryClient reactiveDiscoveryClient,
            WebClient.Builder webClientBuilder,
            ReactorLoadBalancer<ServiceInstance> loadBalancer) {
        this.reactiveDiscoveryClient = reactiveDiscoveryClient;
        this.webClientBuilder = webClientBuilder;
        this.loadBalancer = loadBalancer;
    }

    @GetMapping("/webclient/{msg}")
    public Mono<Map<String, Object>> webClientInvoke(@PathVariable String msg) {
        return webClientBuilder.build()
                .get()
                .uri("http://service-provider/provider/echo/" + msg)
                .retrieve()
                .bodyToMono(Map.class)
                .map(providerResponse -> Map.<String, Object>of(
                    "type", "@LoadBalanced WebClient",
                    "provider", providerResponse,
                    "consumer", serviceName
                ))
                .onErrorResume(e -> Mono.just(Map.of(
                    "type", "@LoadBalanced WebClient",
                    "success", false,
                    "error", e.getMessage()
                )));
    }

    @GetMapping("/manual/{msg}")
    public Mono<Map<String, Object>> manualLBInvoke(@PathVariable String msg) {
        return loadBalancer.choose()
                .map(response -> {
                    ServiceInstance instance = response.getServer();
                    String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/provider/echo/" + msg;
                    return Map.<String, Object>of(
                        "type", "ReactorLoadBalancer Manual",
                        "selectedInstance", instance.getHost() + ":" + instance.getPort(),
                        "url", url,
                        "consumer", serviceName
                    );
                })
                .flatMap(info -> {
                    String url = (String) info.get("url");
                    return WebClient.create()
                            .get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(Map.class)
                            .map(providerResponse -> {
                                Map<String, Object> result = new HashMap<>(info);
                                result.put("provider", providerResponse);
                                return result;
                            })
                            .onErrorResume(e -> Mono.just(Map.of(
                                "type", "ReactorLoadBalancer Manual",
                                "success", false,
                                "error", e.getMessage()
                            )));
                });
    }

    @GetMapping("/discovery")
    public Mono<Map<String, Object>> getServices() {
        return reactiveDiscoveryClient.getServices()
                .collectList()
                .map(services -> Map.<String, Object>of(
                    "services", services,
                    "count", services.size()
                ));
    }
}
