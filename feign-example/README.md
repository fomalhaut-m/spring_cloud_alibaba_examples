# Feign Example

OpenFeign 同步服务调用示例，包含 Provider 和 Consumer 两个模块。

## 模块说明

- `provider/` - 服务提供者 (端口 8082)
- `consumer/` - 服务消费者 (端口 8083)

## 快速开始

### 本地运行

```bash
cd feign-example

# 启动 Provider
cd provider && mvn spring-boot:run &

# 启动 Consumer
cd consumer && mvn spring-boot:run
```

### 带 Nacos 运行

```bash
# Provider
cd provider && mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod" &

# Consumer
cd consumer && mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 测试接口

| 接口 | 说明 |
|------|------|
| GET :8082/provider/echo/{msg} | Provider 直接调用 |
| GET :8083/consumer/feign/{msg} | Consumer 通过 Feign 调用 Provider |
