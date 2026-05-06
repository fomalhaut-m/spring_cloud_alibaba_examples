package com.example.discovery.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
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
        Map<String, Object> result = new HashMap<>();
        List<String> services = discoveryClient.getServices();
        result.put("services", services);
        result.put("count", services.size());
        return result;
    }

    @GetMapping("/instances/{serviceName}")
    public Map<String, Object> getInstances(@PathVariable String serviceName) {
        Map<String, Object> result = new HashMap<>();
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        result.put("serviceName", serviceName);
        result.put("instances", instances);
        result.put("count", instances.size());
        return result;
    }

    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("serviceName", serviceName);
        result.put("description", discoveryClient.description());
        return result;
    }
}