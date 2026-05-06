# Spring Cloud Alibaba 示例项目规则

## 一、版本规范

| 组件 | 版本 |
|------|------|
| JDK | 17+ |
| Spring Boot | 3.5.0 |
| Spring Cloud | 2025.0.0 |
| Spring Cloud Alibaba | 2025.0.0.0 |
| Nacos | 3.1.x |

## 二、项目命名规范

- 所有子项目使用短名称命名
- 命名格式：{功能}-example
- 示例：config-example、discovery-example、feign-example

## 三、Nacos 配置规则

### 强制认证要求
- Nacos 3.0+ 强制要求开启认证
- Token 必须为复杂的 Base64 字符串，不能留空或设为简单的 "admin"

### Token 设置规则
```bash
NACOS_AUTH_TOKEN=U2VjcmV0S2V5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5
```

### 启动脚本配置
- 必须设置 JVM 内存参数：JVM_XMS=256m, JVM_XMX=512m
- 容器重启策略：--restart=unless-stopped
- 端口映射：8080:8080, 8848:8848, 9848-9849:9848-9849

## 四、项目结构规则

- 每个子项目必须有独立的 README.md
- 每个子项目必须有独立的 pom.xml
- 配置文件使用多环境配置：application.yml、application-local.yml、application-prod.yml

## 五、代码规范

- 主类命名：{功能}Application.java
- 包名：com.example.{功能}
- 激活 profile：--spring.profiles.active=local
- JVM 参数：-Xms256m -Xmx512m

## 六、Git 忽略规则

- 忽略 target/ 目录
- 忽略 .idea/ 目录
- 忽略 *.iml 文件
- 忽略 nacos-data/ 目录
- 忽略 logs/ 目录
