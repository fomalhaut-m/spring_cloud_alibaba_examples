# Reactive Feign Example

ReactiveFeign 响应式服务调用示例，基于 WebFlux 全异步编程。

## 模块说明

- `provider/` - 响应式服务提供者 (端口 8084)
- `consumer/` - 响应式服务消费者 (端口 8085)

## 核心功能

- ReactiveFeign 响应式非阻塞调用
- WebFlux 全异步编程模型
- 适配高并发低延迟场景

## 快速开始

### 本地运行

```bash
cd reactive-feign

# 启动 Provider
cd provider && mvn spring-boot:run &

# 启动 Consumer
cd consumer && mvn spring-boot:run
```

## 测试接口

| 接口 | 说明 |
|------|------|
| GET :8084/provider/echo/{msg} | Provider 直接调用 |
| GET :8085/consumer/reactive/{msg} | Consumer 通过 ReactiveFeign 调用 |
