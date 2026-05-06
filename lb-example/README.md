# LoadBalancer Example

全场景负载均衡示例，演示 @LoadBalanced 和 ReactorLoadBalancer 的使用。

## 核心功能

- @LoadBalanced 注解式负载均衡
- ReactorLoadBalancer 手动响应式负载均衡
- WebClient.Builder 注入与使用
- ReactiveDiscoveryClient 服务发现

## 快速开始

### 本地运行

```bash
cd lb-example
mvn spring-boot:run
```

### 带 Nacos 运行

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 测试接口

| 接口 | 说明 |
|------|------|
| GET /lb/webclient/{msg} | @LoadBalanced WebClient 调用 |
| GET /lb/manual/{msg} | ReactorLoadBalancer 手动负载均衡 |
| GET /lb/discovery | 服务发现查询 |
| GET /lb/instances/{serviceName} | 获取服务实例详情 |
