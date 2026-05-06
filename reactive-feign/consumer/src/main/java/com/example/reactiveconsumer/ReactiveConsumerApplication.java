package com.example.reactiveconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ReactiveConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveConsumerApplication.class, args);
    }
}