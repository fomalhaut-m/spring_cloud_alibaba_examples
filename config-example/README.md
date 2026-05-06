# Nacos Config Example

Nacos 配置中心核心示例，演示配置管理和动态刷新功能。

## 核心功能

- Nacos 配置中心核心用法
- @RefreshScope 配置动态刷新
- fail-fast 容灾兜底机制
- 本地无 Nacos 可正常运行

## 快速开始

### 本地无 Nacos 运行（默认）

```bash
cd config-example
mvn spring-boot:run
```

访问：http://localhost:8080/config/get

### 带 Nacos 运行

1. 启动 Nacos：
```bash
cd ../docker-nacos/scripts
./start-nacos.sh
```

2. 启动项目：
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

## 测试接口

| 接口 | 说明 |
|------|------|
| GET /config/get | 获取当前配置 |
| GET /actuator/health | 健康检查 |

## 配置说明

- `application-local.yml` - 本地开发配置（关闭 Nacos）
- `application-prod.yml` - 生产配置（连接 Nacos）

## 三层配置优先级

Nacos 云端配置 > 本地 application.yml > 代码默认值
