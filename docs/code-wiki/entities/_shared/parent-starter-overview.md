---
title: bmos-parent-starter 后端共享脚手架总览
created: 2026-07-15
updated: 2026-07-15
type: entity
service: shared
tags: [shared-lib, backend, architecture, tech-stack, config, deploy, module]
sources:
  - packages/backend/shared/bmos-parent-starter/pom.xml
  - packages/backend/shared/bmos-parent-starter/bmos-cloud-dependency/
  - packages/backend/shared/bmos-parent-starter/bmos-starter-autoconfigure/
  - packages/backend/shared/bmos-parent-starter/bmos-starter-common/
status: active
---

# bmos-parent-starter 后端共享脚手架总览

> 这是 bmos 全部 5 个后端微服务（platform / mes / lims / wms / gateway）共同依赖的**父级脚手架工程**——Spring Boot AutoConfiguration 启动器集合 + 版本中心 BOM。
> 经 `git subtree add --squash` 从 `http://172.16.0.180/bmos/bmos-parent-starter.git` 的 **dev 分支**（HEAD `af804eec4`）引入，与本仓库其它服务**完全隔离、独立演进**。
> 引入记录见 [[log]] 2026-07-15 条；整合方案见 [[MONOREPO_INTEGRATION_PLAN]] 第 2.2/3.2 节 `shared/starters`。

## 这是什么 / 不是什么

| 维度 | 说明 |
|---|---|
| **定位** | starter / SDK / 平台库代码——**非业务 services 应用层** |
| **消费方** | platform / mes / lims / wms / gateway 五服务通过 Maven 依赖引入，获得自动装配能力 |
| **核心机制** | `@EnableBmosXxx` 注解 + `@Import` 导入 Configuration（**主动声明式** starter，非传统 classpath 自动发现） |
| **与业务 services wiki 的差异** | 无 `@TableName` / 无 Controller / 无 Service 业务方法；抓信号要看 `@AutoConfiguration` / `@ConditionalOnXxx` / `META-INF/spring.factories` / `@EnableXxx` / `@ConfigurationProperties` / 抽象基类与 SPI 接口 |

> 建页方法论依据：[[PLAYBOOK-backend]] 第十节「未覆盖场景 · starter / SDK / 平台库代码」。此前 [[api-conventions]] / [[data-access-pattern]] / [[mes-audit-module]] / [[mes-workflow-module]] 等页曾标注"源码在 monorepo 外"，本页即那些外部依赖的本体。

## 工程坐标

| 属性 | 值 |
|---|---|
| groupId / artifactId | `com.bmos` / `bmos-parent-starter` |
| version（`${revision}`） | **`1.15.2-SNAPSHOT`**（dev 分支） |
| packaging | `pom` |
| Spring Boot | `2.6.15` |
| Spring Cloud | `3.1.8`（2021.x） |
| Spring Cloud Alibaba | `2021.0.4.0`（Nacos） |
| Java | JDK 8 |
| 版本管理 | 根 pom `dependencyManagement` 通过 BOM import 统一管理 30+ 第三方库版本；`flatten-maven-plugin`（`resolveCiFriendliesOnly`）处理 `${revision}` |
| 制品发布 | 内网 Nexus `172.30.1.212:8081`（releases + snapshots） |
| 规模 | **16 个 Maven 子模块 / 278 个 Java 文件**（272 main + 6 test） |

> ⚠️ **版本对齐提示**（依赖收口的输入）：dev 分支 `revision=1.15.2-SNAPSHOT`，与 [[service-overview]] 中 lims（1.15.2）齐平，高于 platform/mes（1.15.0）与 gateway/wms（1.14.0）。这印证 [[MONOREPO_INTEGRATION_PLAN]] 4.1 节"`bmos.version` 分歧暂不统一"的决策。若后续做依赖收口，**此 dev 版本是最新的基准**，但升级需逐服务回归。

## 16 个子模块速览

依赖层次（`bmos-starter-common` 是底座，64 类）：

```
bmos-starter-common (底座)
  ├── mybatis / formula / adaptor / mq / expire / data / logging   (直接依赖 common)
  ├── i18n (common + cloud-dependency)
  ├── web (common + adaptor + i18n)
  ├── cache (web + mybatis)
  ├── unit (web + cache + adaptor + mybatis)
  └── autoconfigure (聚合 adaptor + cache + web + mq + mybatis + i18n + logging + unit + data)
```

| # | artifactId | Java | 职责 | 对外 starter |
|---|---|---|---|:---:|
| 1 | **bmos-starter-common** | 64 | 公共底座：`BmosException` 异常体系、`ResponseInfo` 响应体、`BaseDO`/`SysUser`、EasyExcel 读写、树形结构、Redis Key 注册表、JWT/JSON/Date 工具 | ✅ |
| 2 | **bmos-cloud-dependency** | 0 | Spring Cloud Alibaba 基础设施 BOM（Nacos 配置中心 + 注册发现 + bootstrap + actuator），纯依赖声明 | ✅（BOM） |
| 3 | **bmos-starter-autoconfigure** | 1 | **一站式聚合 starter**：`@EnableBmosAutoConfiguration` 一键启用 7 个子 starter，业务服务最常用入口 | ✅ |
| 4 | **bmos-starter-mybatis** | 15 | MyBatis-Plus 增强：`BaseDO` 审计基类、`BaseMapperX`、`LambdaQueryWrapperX`、`DefaultDBFieldHandler` 自动填充、`EscapeUnderlineSelectInterceptor` | ✅ |
| 5 | **bmos-starter-web** | 18 | Web 层：`GlobalExceptionHandler`、Swagger3、API 版本控制（`/v1`/`/v2`）、CORS、`ObjectMapper` 定制 | ✅ |
| 6 | **bmos-starter-cache** | 12 | Redis/Redisson：`RedisService`、`@DistributedLock` 分布式锁、`@ApiStabilization` 接口幂等 | ✅ |
| 7 | **bmos-starter-adaptor** | 18 | Feign 远程调用适配层：`PlatformApiAdaptor`（用户/密码验证）、`FileManagerApiAdaptor`（文件）、`ActiveApiAdaptor`（RSA 激活）、统一错误解码器 | ✅ |
| 8 | **bmos-starter-i18n** | 20 | 国际化：基于 Nacos 的动态多语言加载、FrontApp/FrontWeb 多前端来源、`@RefreshScope` 热更新 | ✅ |
| 9 | **bmos-starter-mq** | 13 | Kafka：`KafkaProducer`/`KafkaConsumer`、状态机事件监听 `InfiniteEventListener`、`@EnableBmosKafka` | ✅ |
| 10 | **bmos-starter-rocketmq** | 19 | RocketMQ：`@Consumer`/`@Topic`/`@MessageQueueScan` 注解驱动、Producer 策略接口、延迟消息 | ✅ |
| 11 | **bmos-starter-logging** | 13 | 操作日志：`@OperationLog` + AOP 切面、`@OperationUserDefined` 自定义日志、`OperationLogService` SPI | ✅ |
| 12 | **bmos-starter-unit** | 15 | 计量单位：`@PrecisionValue`/`@PrecisionUnitId`、`UnitCache`（Feign 远程拉换算比例 + 本地缓存） | ✅ |
| 13 | **bmos-starter-data** | 3 | 数据源：Druid 连接池、MySQL 驱动、Flyway 迁移、`DruidSqlLogFilter` | ✅ |
| 14 | **bmos-starter-formula** | 35 | 数学表达式引擎：Shunting-Yard 解析、Tokenizer、`Operator`/`Function` 抽象、`ExpressionCalculator`、BigDecimal 精度 | ✅ |
| 15 | **bmos-starter-file** | 25 | 文件处理：DOCX 拆分/合并（docx4j + aspose-words）、Excel（easyexcel + poi）、HTML（jsoup） | ❌ 工具库 |
| 16 | **bmos-starter-expire** | 7 | 到期/过期通知：`@ExpireMessageListener`、`ExpireMessageProducer`、`ExpireListener` SPI | ✅ |

> `bmos-starter-common` 里的 `ResponseInfo` / `BmosException` / `GlobalExceptionHandler`（在 web）/ `BaseDO` / `BaseMapperX` / `BasePage` / `CommonPage` 等共性类——正是 [[api-conventions]] 与 [[data-access-pattern]] 两页此前标注"源码在 monorepo 外"的本体。

## 自动装配机制（starter 核心，业务 services 无此层）

### 接入模式：主动声明式（非 classpath 自动发现）

大部分 starter **不依赖** `spring.factories` 也**不依赖** Spring Boot 2.7+ 的 `AutoConfiguration.imports`，而是业务服务在启动类上标 `@EnableBmosAutoConfiguration`（或各子 `@EnableBmosXxx`），由注解上的 `@Import` 导入 Configuration 类触发装配。

### 11 个 `@EnableBmosXxx` 启用注解

| 注解 | 模块 | 装配的 Configuration |
|---|---|---|
| **`@EnableBmosAutoConfiguration`** | autoconfigure | **组合注解**，叠加下方 7 个 |
| `@EnableBmosAdaptor` | adaptor | `BmosApiAdaptorAutoConfiguration`、`FeignConfiguration` |
| `@EnableBmosWeb` | web | `BmosWebAutoConfiguration`、`BmosApiVersionConfiguration`、`Swagger3Configuration` |
| `@EnableBmosMybatis` | mybatis | `MybatisAutoConfiguration` |
| `@EnableBmosRedis` | cache | `BmosRedisAutoConfiguration`、`BmosRedissonAutoConfiguration` |
| `@EnableBmosUnit` | unit | `UnitAutoConfiguration` |
| `@EnableBmosDataSource` | data | `DataSourceConfiguration` |
| `@EnableBmosLocale` | i18n | `BmosLocaleAutoConfiguration`、`BmosRefreshScopeListener` |
| `@EnableBmosLogAutoConfiguration` | logging | `LogTranslateUtil` |
| `@EnableBmosExpressionAutoConfiguration` | formula | `ExpressionConfiguration` |
| `@EnableBmosKafka` | mq | `BmosKafkaConfiguration` |

> **聚合关系**：`@EnableBmosAutoConfiguration` 一次叠加 adaptor + web + mybatis + redis + unit + dataSource + locale 共 **7 个**。**不包含** logging / formula / kafka / rocketmq / expire——这些按需单独引入。

### 17 个 `@Configuration` 类 + 6 处 `@ConditionalOnXxx`

`@ConditionalOnXxx` 集中在 3 模块：**adaptor**（3 处，`@ConditionalOnMissingBean` 防覆盖）、**cache**（1 处，`@ConditionalOnClass(RedisTemplate)`）、**expire**（1 处，`@ConditionalOnBean`）、**web**（1 处）。

### `spring.factories`（旧式自动装配，仅 2 处）

工程**未使用** Spring Boot 2.7+ 的 `AutoConfiguration.imports`，仅保留 2 个旧式 `spring.factories`：
- `bmos-starter-expire/.../META-INF/spring.factories` → `ExpireMessageConfiguration` + `ExpireMessageProducer`
- `bmos-starter-rocketmq/.../META-INF/spring.factories` → `RocketMqAutoConfig` + `SpringContextUtil`

### Properties 前缀类（6 个 `@ConfigurationProperties`）

| 类名 | prefix | 模块 |
|---|---|---|
| `LogPropertiesConfig` | `log-properties` | logging |
| `KafkaProperties` | `spring.kafka` | mq |
| `BmosI18nProperties` | `i18n` | i18n |
| `FrontAppI18nProperties` | `front.i18n.app` | i18n |
| `FrontWebI18nProperties` | `front.i18n.web` | i18n |
| `RedissonProperties` | `spring.redis.redisson`（复用第三方 redisson-spring-boot-starter） | cache |

> 排查前缀冲突 / 配置不生效时，先查这张表。

## SPI / 扩展点（业务服务通过实现/继承来扩展）

> 工程无 `META-INF/services/` 文件。扩展点全部是 Java 接口 / 抽象基类。

| 接口/抽象类 | 模块 | 业务如何扩展 |
|---|---|---|
| `OperationLogService<T extends LogModel>` | logging | 实现此接口 `save(T)` 完成操作日志持久化 |
| `OperationLogAspect<E,T>` (abstract) | logging | 继承切面 + `@OperationLog` 自动记日志 |
| `ExpireListener` | expire | 实现 `onExpire(...)` 接收到期通知，配 `@ExpireMessageListener` |
| `InfiniteEventListener` | mq(kafka) | 实现 `notified(InfiniteEvent)` 接收状态机事件（[[mes-workflow-module]] 4 个 listener 即此机制） |
| `MessageQueueProducerStrategy` | rocketmq | 实现 `producer(...)` 自定义 MQ 发送逻辑 |
| `Function` (abstract) / `Operator` (abstract) | formula | 继承实现自定义公式函数/运算符 |
| `BaseDO` (abstract) | mybatis | 所有数据实体继承，获得 id/createTime/updateTime/createBy/updateBy/deleted 审计字段 |
| `BaseMapperX<T>` | mybatis | 所有 Mapper 继承，获得 `selectOne`/`insertBatch`/`saveOrUpdate` 等便捷方法 |
| `ApiAdaptor` | adaptor | Feign 适配器标记接口 |
| `I18nMessageResource` / `FrontBaseMessageResource` | i18n | 自定义国际化消息源 |

## 业务注解（业务服务用这些注解触发 starter 行为）

| 注解 | 模块 | 作用 |
|---|---|---|
| `@OperationLog` / `@OperationUserDefined` | logging | 方法级操作日志（可 filterFields 过滤敏感字段） |
| `@DistributedLock` | cache | Redisson 分布式锁（key/expression/duration） |
| `@ApiStabilization` | cache | 接口幂等 / 防重复提交 |
| `@Consumer` / `@Topic` / `@MessageQueueScan` | rocketmq | RocketMQ 消费者/Topic/扫描包声明 |
| `@ExpireMessageListener` | expire | 到期监听器标记 |
| `@PrecisionValue` / `@PrecisionUnitId` | unit | 字段级单位精度截取 |

## 与各业务服务的关系

- **被谁依赖**：全部 5 个后端服务通过 `bmos-starter-autoconfigure`（或按需单引子 starter）获得能力。各服务的 `bmos.version` 即对应本工程的 `${revision}` 快照（见 [[service-overview]] 版本列）。
- **审计/工作流引擎**：[[mes-audit-module]] 依赖 `bmos-audit-engine-starter`、[[mes-workflow-module]] 依赖 `bmos-orchestrator-starter`——**这两个 starter 不在本工程**，本工程只含上面 16 个模块。audit-engine / orchestrator 仍为外部独立依赖，对应 wiki 页的「TODO · 引擎待补」章节未因本次引入而闭合。

## 隐藏地雷 / 踩坑提示

1. ⚠️ **版本漂移点**：`bmos-starter-i18n` 的 pom 对 `bmos-cloud-dependency` 引用了**硬编码版本 `1.15.0-SNAPSHOT`**（非 `${revision}`），与当前 revision `1.15.2-SNAPSHOT` 不一致——升级时需手动同步。
2. ⚠️ **system-scope 本地 jar**：`bmos-starter-file/lib/` 含 3 个本地 jar（`aspose-words-24.3.jar`、`aspose-pdf-22.10-my.jar`、`docx4j-Enterprise-MergeDocx-8.4.0.4-trial.jar`），为 system scope，构建时需 `includeSystemScope`。
3. ⚠️ **非标包名**：`bmos-starter-data` 用 `annotation.`/`config.`/`filter.`（无 `com.bmos` 前缀）；`bmos-starter-autoconfigure` 的 `com.bmos.autoconfigure` 用点而非目录嵌套——与其它模块 `com.bmos.*` 规范不一致。
4. ⚠️ **`Activate` 类（web 模块）**：软件授权 / MAC 地址验证工具类（非 Spring 组件），与 [[auth-and-license]] 的 license 链路相关。

## 后续可深化项

- 各子 starter 独立子页（mybatis / cache / logging / rocketmq / formula 均够建页体量，按需）。
- audit-engine / orchestrator 引擎源码引入后，补平台 starter 方法论（见 [[PLAYBOOK-backend]] 第十节）。
- 上游 dev 更新后同步：`git subtree pull --prefix=packages/backend/shared/bmos-parent-starter <url> dev --squash`。

## 相关页面

- [[data-access-pattern]] —— `BaseDO`/`BaseMapperX`/`DefaultDBFieldHandler` 的统一数据访问模式（源码在本工程 `bmos-starter-mybatis` / `bmos-starter-common`）。
- [[api-conventions]] —— `ResponseInfo`/`BmosException`/`GlobalExceptionHandler`/分页（源码在本工程 `bmos-starter-common` / `bmos-starter-web`）。
- [[service-overview]] —— 各服务 `bmos.version` 与本工程 `${revision}` 的对应关系。
- [[service-integration]] —— Feign 调用矩阵，adaptor 模块是 Feign 适配层。
- [[auth-and-license]] —— license 校验链路，`ActiveApiAdaptor` 与 `Activate` 类在本工程。
- [[PLAYBOOK-backend]] 第十节 —— starter / SDK 建页方法论（本页即该方法论的首次实战）。
