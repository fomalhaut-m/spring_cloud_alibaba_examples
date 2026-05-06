# Nacos Docker 示例项目

通过 Docker 快速启动 Nacos 服务，适合学习环境使用。

## 快速开始

### 1. 启动 Nacos

```bash
cd scripts
chmod +x *.sh
./start-nacos.sh
```

启动成功后访问：http://localhost:8848/nacos
- 默认账号：`nacos`
- 默认密码：`nacos`

### 2. 查看状态

```bash
./start-nacos.sh status
```

### 3. 查看日志

```bash
./logs-nacos.sh
```

### 4. 停止 Nacos

```bash
./stop-nacos.sh stop
```

## 数据持久化

- 数据目录：`nacos-data/`

删除容器不会丢失数据，重新启动即可恢复。

## 端口说明

| 端口 | 说明 |
|------|------|
| 8080 | Nacos 主端口 |
| 8848 | Nacos 控制台 |
| 9848-9849 | gRPC 端口 |

## 重要说明

**Nacos 3.0+ 强制认证要求：**
- Nacos 3.0.3 及以上版本强制要求开启认证
- `NACOS_AUTH_ENABLE=false` 仅在开发环境中使用
- Token 必须设为复杂的 Base64 字符串，不能留空或设为简单的 "admin"
- 如需开启认证，Token 示例：
  ```bash
  -e NACOS_AUTH_TOKEN=SecretKey012345678901234567890123456789012345678901234567890123456789
  ```

## 常用命令

```bash
# 查看容器
docker ps | grep nacos

# 进入容器
docker exec -it nacos-local bash

# 删除容器（保留数据）
docker rm -f nacos-local

# 拉取镜像
docker pull nacos/nacos-server:v3.0.3
```
