package com.example.config.controller;

import com.example.config.config.AppConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigTestController {

    private final AppConfig appConfig;

    public ConfigTestController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @GetMapping("/get")
    public Map<String, Object> getConfig() {
        return Map.of(
            "name", appConfig.getName(),
            "version", appConfig.getVersion(),
            "desc", appConfig.getDesc(),
            "source", "Nacos/Local"
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "nacosEnabled", "true"
        );
    }
}
