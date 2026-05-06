package com.example.config.controller;

import com.example.config.config.AppConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        Map<String, Object> result = new HashMap<>();
        result.put("name", appConfig.getName());
        result.put("version", appConfig.getVersion());
        result.put("desc", appConfig.getDesc());
        result.put("source", "Nacos/Local");
        return result;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> result = new HashMap<>();
        result.put("status", "UP");
        result.put("nacosEnabled", "true");
        return result;
    }
}