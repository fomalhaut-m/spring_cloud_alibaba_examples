package com.example.discovery.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/discovery")
public class DiscoveryController {

    private final DiscoveryClient discoveryClient;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    public DiscoveryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/services")
    public Map<String, Object> getServiceList() {
        return Map.of(
            "services", discoveryClient.getServices(),
            "count", discoveryClient.getServices().size()
        );
    }

    @GetMapping("/instances/{serviceName}")
    public Map<String, Object> getInstances(@PathVariable String serviceName) {
        return Map.of(
            "serviceName", serviceName,
            "instances", discoveryClient.getInstances(serviceName),
            "count", discoveryClient.getInstances(serviceName).size()
        );
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return Map.of(
            "serviceName", serviceName,
            "description", discoveryClient.description()
        );
    }
}
