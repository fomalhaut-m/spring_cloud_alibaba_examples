package com.example.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "service-provider")
public interface ProviderFeignClient {

    @GetMapping("/provider/echo/{msg}")
    Map<String, Object> echo(@PathVariable String msg);

    @GetMapping("/provider/health")
    Map<String, String> health();
}