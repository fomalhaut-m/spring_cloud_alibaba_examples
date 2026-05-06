package com.example.config.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@RefreshScope
@Data
public class AppConfig {
    private String name = "代码默认兜底值";
    private String version = "1.0.0";
    private String desc = "默认描述";
}