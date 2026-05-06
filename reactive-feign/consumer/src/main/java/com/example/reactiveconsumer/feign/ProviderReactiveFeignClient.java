package com.example.reactiveconsumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

import java.util.Map;

@FeignClient(name = "reactive-service-provider")
public interface ProviderReactiveFeignClient {

    @GetMapping("/provider/echo/{msg}")
    Mono<Map<String, Object>> echo(@PathVariable String msg);

    @GetMapping("/provider/health")
    Mono<Map<String, String>> health();
}