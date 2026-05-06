# Spring Cloud Alibaba 全功能示例合集
> 一套覆盖微服务核心场景、可独立运行、本地零依赖Nacos、面试&学习一站式示例代码
> 完整覆盖2026.05.06全量学习内容，从基础配置到高级调用，每个模块独立可运行，开箱即用

---

## 一、项目总览
本合集为Spring Cloud Alibaba微服务全场景示例代码，**每个功能一个独立子项目**，结构清晰、无冗余、零报错，同时兼容「本地无Nacos开发」和「生产Nacos集群运行」双模式，覆盖微服务开发核心能力：
- Nacos配置中心 + 动态刷新 + 容灾兜底
- Nacos服务注册与发现
- 同步OpenFeign服务调用
- 响应式ReactiveFeign + WebFlux全异步调用
- @LoadBalanced + ReactorLoadBalancer全场景负载均衡
- 多环境隔离 + 本地开发零依赖

---

## 二、统一版本规范（必看，避免版本冲突）
所有子项目统一使用以下稳定兼容版本，杜绝版本不兼容问题：
| 技术组件 | 固定版本 | 说明 |
|----------|----------|------|
| JDK | 17+ | 兼容企业级主流环境 |
| Spring Boot | 3.5.0 | 3.x稳定版，兼容性最强 |
| Spring Cloud | 2025.0.0 | 对应Spring Boot 3.5.x |
| Spring Cloud Alibaba | 2025.0.0.0 | 官方适配对应版本 |
| Nacos | 3.1.x | 服务注册与配置中心 |
| 核心组件 | WebFlux、OpenFeign、ReactiveFeign、ReactorLoadBalancer | 全量覆盖 |

---

## 三、环境准备
1. JDK 17+ 环境配置完成
2. Maven 3.6+ 环境配置完成
3. IDEA/Eclipse 开发工具
4. 可选：Nacos 3.x 服务端（本地开发可不用启动，直接用local环境运行）

---

## 四、项目结构总览
```
spring_cloud_alibaba_examples/
├── README.md （本文档）
├── docker-nacos            【Nacos Docker 快速启动】
├── config-example          【Nacos配置中心核心示例】
├── discovery-example       【Nacos服务注册发现示例】
├── feign-example           【同步OpenFeign服务调用示例】
├── reactive-feign          【响应式ReactiveFeign示例】
└── lb-example              【全场景负载均衡示例】
```

---

## 五、各模块详细说明
### 1. config-example
#### 核心知识点（面试必背）
- Nacos配置中心核心用法
- @RefreshScope 配置动态刷新
- fail-fast=false 容灾兜底机制
- 本地无Nacos可正常运行
- 三层配置优先级：Nacos云端 > 本地yml > 代码默认值

#### 项目结构
```
config-example/
├── pom.xml
├── src/main/java/com/example/config/
│   ├── NacosConfigApplication.java （启动类）
│   ├── config/AppConfig.java （配置绑定类+@RefreshScope）
│   └── controller/ConfigTestController.java
└── src/main/resources/
    ├── bootstrap.yml
    ├── application.yml
    └── application-local.yml （本地无Nacos环境）
```

#### 核心配置
bootstrap.yml（启动前置配置）
```yaml
spring:
  application:
    name: nacos-config-demo
  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
        file-extension: yml
        fail-fast: false # 核心：Nacos不可用不崩溃，用本地兜底
        enabled: ${spring.cloud.nacos.config.enabled:true}
```

application-local.yml（本地无Nacos环境）
```yaml
spring:
  cloud:
    nacos:
      config:
        enabled: false # 关闭Nacos，纯本地运行
      discovery:
        enabled: false
# 本地兜底配置
app:
  name: 本地兜底配置
  version: 1.0.0
  desc: 本地无Nacos也能正常运行
```

#### 核心代码
AppConfig.java（动态刷新配置类）
```java
@Component
@ConfigurationProperties(prefix = "app")
@RefreshScope // 开启配置动态刷新
@Data
public class AppConfig {
    private String name = "代码默认兜底值";
    private String version = "1.0.0";
    private String desc = "默认描述";
}
```

#### 运行步骤
1. 本地无Nacos：直接启动，默认加载local环境，无需启动Nacos
2. 带Nacos运行：启动Nacos后，指定`--spring.profiles.active=prod`启动
3. 动态刷新测试：Nacos后台修改配置并发布，无需重启项目，接口自动返回新值

#### 测试接口
| 接口地址 | 说明 |
|----------|------|
| GET /config/get | 获取当前配置，验证动态刷新 |

---

### 2. discovery-example
#### 核心知识点
- Nacos服务注册与发现核心机制
- ReactiveDiscoveryClient 响应式服务查询
- 服务上下线自动感知
- 多实例服务管理

#### 项目结构
```
discovery-example/
├── pom.xml
├── src/main/java/com/example/discovery/
│   ├── DiscoveryApplication.java
│   └── controller/DiscoveryController.java
└── src/main/resources/
    ├── bootstrap.yml
    ├── application.yml
    └── application-local.yml
```

#### 核心代码
DiscoveryController.java
```java
@RestController
@RequestMapping("/discovery")
public class DiscoveryController {

    @Resource
    private ReactiveDiscoveryClient reactiveDiscoveryClient;

    // 获取所有服务列表
    @GetMapping("/services")
    public Flux<String> getServiceList() {
        return reactiveDiscoveryClient.getServices();
    }

    // 获取指定服务的实例列表
    @GetMapping("/instances/{serviceName}")
    public Flux<ServiceInstance> getInstances(@PathVariable String serviceName) {
        return reactiveDiscoveryClient.getInstances(serviceName);
    }
}
```

#### 测试接口
| 接口地址 | 说明 |
|----------|------|
| GET /discovery/services | 获取注册中心所有服务名 |
| GET /discovery/instances/{serviceName} | 获取指定服务的所有实例 |

---

### 3. feign-example
#### 核心知识点
- OpenFeign 同步服务调用
- @LoadBalanced 客户端负载均衡
- 服务间调用自动集成Nacos注册中心
- 超时、重试配置

#### 项目结构
```
feign-example/
├── pom.xml
├── src/main/java/com/example/feign/
│   ├── FeignConsumerApplication.java （消费者启动类）
│   ├── FeignProviderApplication.java （提供者启动类）
│   ├── feign/ProviderFeignClient.java
│   └── controller/
│       ├── ProviderController.java
│       └── ConsumerController.java
└── src/main/resources/
    ├── bootstrap.yml
    ├── application-provider.yml
    └── application-consumer.yml
```

#### 核心代码
ProviderFeignClient.java
```java
@FeignClient(name = "service-provider")
public interface ProviderFeignClient {
    @GetMapping("/provider/echo/{msg}")
    String echo(@PathVariable String msg);
}
```

#### 测试接口
| 接口地址 | 说明 |
|----------|------|
| GET /provider/echo/{msg} | 服务提供者接口 |
| GET /consumer/feign/{msg} | 消费者通过Feign调用提供者 |

---

### 4. reactive-feign
#### 核心知识点
- ReactiveFeign 响应式非阻塞调用
- WebFlux 全异步编程模型
- 底层基于WebClient实现
- 适配高并发低延迟场景

#### 项目结构
```
reactive-feign/
├── pom.xml
├── src/main/java/com/example/reactivefeign/
│   ├── ReactiveProviderApplication.java
│   ├── ReactiveConsumerApplication.java
│   ├── feign/ProviderReactiveFeignClient.java
│   └── controller/
│       ├── ReactiveProviderController.java
│       └── ReactiveConsumerController.java
└── src/main/resources/
    ├── bootstrap.yml
    ├── application-provider.yml
    └── application-consumer.yml
```

#### 核心代码
ProviderReactiveFeignClient.java
```java
@ReactiveFeignClient(name = "service-provider")
public interface ProviderReactiveFeignClient {
    @GetMapping("/provider/echo/{msg}")
    Mono<String> echo(@PathVariable String msg);
}
```

#### 测试接口
| 接口地址 | 说明 |
|----------|------|
| GET /provider/echo/{msg} | 响应式提供者接口 |
| GET /consumer/reactive/{msg} | 消费者通过ReactiveFeign异步调用 |

---

### 5. lb-example
#### 核心知识点
- @LoadBalanced 注解式负载均衡
- ReactorLoadBalancer 手动响应式负载均衡
- WebClient.Builder 注入与使用
- ReactiveDiscoveryClient 服务发现
- 负载均衡策略自定义

#### 项目结构
```
lb-example/
├── pom.xml
├── src/main/java/com/example/loadbalancer/
│   ├── LoadBalancerApplication.java
│   ├── config/WebClientConfig.java
│   └── controller/LBController.java
└── src/main/resources/
    ├── bootstrap.yml
    ├── application.yml
    └── application-local.yml
```

#### 核心配置
WebClientConfig.java
```java
@Configuration
public class WebClientConfig {
    // 开启@LoadBalanced负载均衡
    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

#### 核心代码
LBController.java
```java
@RestController
@RequestMapping("/lb")
public class LBController {

    @Resource
    private ReactiveDiscoveryClient reactiveDiscoveryClient;

    @Resource
    private WebClient.Builder webClientBuilder;

    @Resource
    private ReactorLoadBalancer<ServiceInstance> loadBalancer;

    // 1. @LoadBalanced WebClient调用
    @GetMapping("/webclient/{msg}")
    public Mono<String> webClientInvoke(@PathVariable String msg) {
        return webClientBuilder.build()
                .get()
                .uri("http://service-provider/provider/echo/" + msg)
                .retrieve()
                .bodyToMono(String.class);
    }

    // 2. ReactorLoadBalancer手动负载均衡调用
    @GetMapping("/manual/{msg}")
    public Mono<String> manualLBInvoke(@PathVariable String msg) {
        return loadBalancer.choose()
                .map(response -> response.getServer())
                .flatMap(instance -> {
                    String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/provider/echo/" + msg;
                    return WebClient.create().get().uri(url).retrieve().bodyToMono(String.class);
                });
    }

    // 3. 服务发现查询
    @GetMapping("/discovery")
    public Flux<String> getServices() {
        return reactiveDiscoveryClient.getServices();
    }
}
```

#### 测试接口
| 接口地址 | 说明 |
|----------|------|
| GET /lb/webclient/{msg} | @LoadBalanced WebClient调用 |
| GET /lb/manual/{msg} | ReactorLoadBalancer手动负载均衡调用 |
| GET /lb/discovery | 服务发现查询 |

---

## 六、统一运行指南
### 1. IDEA本地运行（无Nacos）
1. 打开对应子项目
2. 直接启动启动类，默认加载`local`环境
3. 无需启动Nacos，直接访问对应测试接口即可

### 2. IDEA带Nacos运行
1. 本地启动Nacos 2.x服务端（默认端口8848）
2. 启动类添加VM参数：`--spring.profiles.active=prod`
3. 启动项目，服务会自动注册到Nacos，拉取云端配置

### 3. Maven命令行运行
```bash
# 1. 打包
mvn clean package -DskipTests

# 2. 本地无Nacos运行
java -jar target/xxx-example.jar --spring.profiles.active=local

# 3. 生产带Nacos运行
java -jar target/xxx-example.jar --spring.profiles.active=prod

# 4. 指定启动类运行（多启动类项目）
mvn spring-boot:run -Dspring-boot.main-class=com.example.xxx.XxxApplication
```

---

## 七、核心知识点总结（面试必背）
1. **Nacos配置中心**：通过bootstrap.yml前置加载配置，`fail-fast=false`实现容灾兜底，`@RefreshScope`实现配置动态刷新
2. **配置优先级**：Nacos云端配置 > 本地application.yml > 配置类代码默认值
3. **服务调用**：同步场景用OpenFeign，响应式WebFlux场景用ReactiveFeign，底层分别基于HttpClient和WebClient
4. **负载均衡**：`@LoadBalanced`注解实现客户端负载均衡，底层基于ReactorLoadBalancer，自动集成Nacos服务发现
5. **本地开发兼容**：通过`spring.cloud.nacos.config.enabled=false`关闭Nacos，实现本地零依赖开发，无需修改业务代码

---

## 八、常见问题排查（避坑指南）
1. **项目启动失败，报Nacos连接异常**
   - 检查是否启用了local环境，确认`spring.cloud.nacos.config.enabled=false`
   - 检查Nacos服务端地址是否正确，端口是否可访问

2. **配置修改后不刷新**
   - 检查配置类是否加了`@RefreshScope`注解
   - 检查Nacos的DataId是否与`${spring.application.name}.${file-extension}`匹配
   - 检查配置格式是否正确（YAML格式缩进问题）

3. **Feign调用报服务找不到**
   - 检查服务是否正确注册到Nacos
   - 检查@FeignClient的name是否与服务名一致
   - 检查是否引入了spring-cloud-starter-loadbalancer依赖

4. **@LoadBalanced不生效**
   - 检查WebClient.Builder是否加了`@LoadBalanced`注解
   - 检查调用地址是否使用服务名，而非IP+端口
   - 检查是否引入了loadbalancer相关依赖

---

## 九、学习路线推荐（按从易到难顺序）
1. 先学：`spring_cloud_alibaba_nacos_config_example`（配置中心核心，基础中的基础）
2. 再学：`spring_cloud_alibaba_discovery_example`（服务注册发现，微服务核心）
3. 进阶：`spring_cloud_alibaba_feign_example`（同步服务调用，企业最常用）
4. 深入：`spring_cloud_alibaba_reactive_feign_example`（响应式编程，高并发场景）
5. 精通：`spring_cloud_alibaba_loadbalancer_example`（负载均衡底层原理）