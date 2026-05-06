# Discovery Example

Nacos 服务注册与发现核心示例。

## 核心功能

- Nacos 服务注册与发现
- ReactiveDiscoveryClient 响应式服务查询
- 服务上下线自动感知

## 快速开始

### 本地无 Nacos 运行

```bash
cd discovery-example
mvn spring-boot:run
```

### 带 Nacos 运行

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 测试接口

| 接口 | 说明 |
|------|------|
| GET /discovery/services | 获取所有服务名 |
| GET /discovery/instances/{serviceName} | 获取服务实例列表 |
| GET /discovery/info | 获取当前服务信息 |
