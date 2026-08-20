---
title: Gateway 服务总览
created: 2026-06-30
updated: 2026-06-30
type: entity
service: gateway
tags: [backend, gateway, auth, api, architecture]
sources:
  - packages/backend/services/gateway/src/main/java/com/bmos/gateway/
  - packages/backend/services/gateway/src/main/java/com/bmos/gateway/BmosGatewayApplication.java
  - packages/backend/services/gateway/src/main/java/com/bmos/gateway/filter/AuthenticationFilter.java
  - packages/backend/services/gateway/pom.xml
  - packages/backend/services/gateway/src/main/resources/bootstrap.yml
status: active
---

# Gateway 服务总览

## 概述 / 职责

**gateway 是整个 bmos 平台的唯一外部入口**,基于 Spring Cloud Gateway(reactive / WebFlux)。前端所有请求经它路由转发到下游 5 个业务服务,并在转发前完成 **JWT + Redis 双重鉴权**。

- 端口:**60300** ｜ Nacos 注册名:`bmos-gateway-service` ｜ context-path:无(默认 `/`,网关入口)
- 启动类:`com.bmos.gateway.BmosGatewayApplication`
- 规模:**极轻量** —— 全服务仅 **4 个 Java 文件**,0 Controller / 0 Mapper / 0 表(无持久层)
- bmos.version:**1.14.0-SNAPSHOT**(落后于 platform/mes 的 1.15.x,详见 [[monorepo-architecture]])

## 技术栈

- Spring Cloud Gateway **3.1.8**(reactive,WebFlux,非 Servlet)
- spring-cloud-loadbalancer 3.1.8(服务发现负载均衡,`lb://` 路由)
- redisson-spring-boot-starter 3.17.7(token 会话存储)
- Spring Boot 2.6.15 / Java 8

> ⚠️ reactive 特性:启动类显式 `exclude = DataSourceAutoConfiguration.class`;`bmos-starter-common` 与 `bmos-starter-cache` 在 pom 中**排除了** `spring-boot-starter-web` 与 `bmos-starter-web`(因 reactive 不用 Servlet)。改网关代码时不能引入任何 Servlet/MyBatis 依赖。

## 模块结构

**扁平单模块**(与其它服务的 common/feign/service 多模块结构不同):

```
gateway/
└── src/main/java/com/bmos/gateway/
    ├── BmosGatewayApplication.java   # 启动类
    ├── filter/
    │   └── AuthenticationFilter.java # ★ 唯一鉴权过滤器(GlobalFilter)
    ├── properties/
    │   └── BmosAuthProperties.java   # @ConfigurationProperties("bmos.auth")
    └── redis/
        └── BmosRedisKeyDefine.java   # Redis key 常量(USER_TOKEN_ID_CACHE)
```

## 核心机制:鉴权过滤器

`AuthenticationFilter implements GlobalFilter, Ordered`(`getOrder() == 0`)是网关唯一的横切逻辑。处理流程:

1. 放行 `OPTIONS`(跨域预检)
2. `bmos.auth.enable=false` → 全部放行(总开关)
3. 白名单放行:`BmosAuthProperties.excludeUrls` 用 `AntPathMatcher` 通配匹配
4. 取请求头 `RequestConstant.BMOS_TOKEN`;为空 → **401**
5. `JwtUtils.parseToken(token)` 解析 JWT Claims;失败 → **401**
6. 用 Claims 中 `SecurityConstant.LOGIN_TOKEN` 作 Redis key(`bmos:user:token:%s`)反查 userId
7. 校验 Redis 的 userId == Claims 中 `SecurityConstant.USER_ID`;不一致 → **401**(支持踢人:删 Redis 即失效)
8. 向下游传用户信息:把 `loginToken` 做 `URLEncoder.encode(UTF-8)` 后**塞回 header `BMOS_TOKEN`**,再 `chain.filter()`
9. 401 响应:`ResponseInfo.failure(BaseResponseCode.UN_AUTHORIZATION)` 以 JSON 写回(WebFlux `DataBuffer`)

> 判定有效 = **JWT 签名合法 且 Redis 中该 token 仍映射到同一 userId**。token→userId 的会话落在 Redis,网关本身无状态。

## 配置属性与 Redis Key

`BmosAuthProperties`(`@ConfigurationProperties("bmos.auth")`)仅两个字段:

| 字段 | 类型 | 默认 | 说明 |
|---|---|---|---|
| `enable` | Boolean | `true` | 鉴权总开关,`false` 时全部放行 |
| `excludeUrls` | List<String> | `[]` | 白名单(AntPathMatcher 通配),**值在 Nacos** |

`BmosRedisKeyDefine` 定义两个 key(均 **2h TTL**):

| 常量 | key 模板 | 用途 |
|---|---|---|
| `USER_TOKEN_ID_CACHE` | `bmos:user:token:%s` | loginToken → userId(鉴权反查比对) |
| `USER_LOGIN_CACHE` | `bmos:user:login:%s` | 用户信息缓存(下游使用,filter 不直接读) |

> filter 第 81 行 `redisService.get(loginToken, USER_TOKEN_ID_CACHE)`:以 JWT Claims 的 `LOGIN_TOKEN` 填入 `bmos:user:token:%s` 反查 userId,再与 Claims 的 `USER_ID` 比对。

## 路由与白名单(⚠️ 不在代码仓库)

**重要**:`application.yml` 只有一行 `server.port: 60300`。路由表(routes)、`bmos.auth.excludeUrls`(白名单)、`bmos.auth.enable` 全部托管在 **Nacos 配置中心**(`bmos-gateway-service-prod.yaml`),**代码仓库不持久化**。

排障/写文档时必须连 Nacos 查路由。前端 → 网关 → 下游的 context-path 对应关系见 [[service-overview]]:

| 前端请求前缀 | 目标服务(uri) |
|---|---|
| `/api/app/platform/**` | `lb://bmos-platform-service`(60100) |
| `/api/app/mes/**` | `lb://bmos-mes-service`(60200) |
| `/api/app/lims2/**` | `lb://bmos-lims2-service`(61001)⚠️ 是 lims2 |
| `/api/app/wms/**` | `lb://bmos-wms-service`(60900) |

> CORS、限流、签名校验、license 校验在**代码层不可见**(Filter 仅 1 个);若存在,只能以 Nacos `default-filters` / `CorsWebFilter` bean 形式配置。license 校验不在网关,在下游各业务服务。

## 跨服务依赖

- **无 Feign、无 RestTemplate**:网关只做路由转发,不主动调用任何下游服务。
- bmos starter 依赖:`bmos-cloud-dependency`(BOM)、`bmos-starter-common`、`bmos-starter-cache`。

## 隐藏地雷 ⚠️

1. **历史 TSD 加密(已于 2026-06-30 解密)**:gateway/lims/wms 工作区 `.java` 曾被注入 `%TSD-Header-###%` 头导致 Read/Grep 乱码,需 `git show HEAD:<path>` 读。**现已解密,可直接读取**(见 [[monorepo-architecture]])。
2. **路由与白名单零代码持久化**:本地代码看不到路由表,排障必须连 Nacos。
3. **版本滞后**:gateway 1.14.0-SNAPSHOT,落后于 platform/mes(1.15.x),与较新下游服务可能存在协议兼容隐患。
4. **Nacos 凭据硬编码**:bootstrap.yml 中 username/password 硬编码 `nacos/nacos`(仅 server-addr 走了 `${NACOS_HOST}` 占位)。
5. **下游用户信息透传只走 token header**:仅传 URL-encoded 的 loginToken,未单独透传 userId;`URLEncoder.encode` 异常时**静默用空串**,可能产生难以追踪的 401。

## AI 定位提示

- 排查 **401 / 鉴权不过** → `filter/AuthenticationFilter.java`(看第 4~7 步哪一步拦截)
- 排查 **路由不到 / 404** → 不在代码,**连 Nacos 看 `bmos-gateway-service-prod.yaml`**
- 排查 **白名单不生效** → Nacos 的 `bmos.auth.excludeUrls`(AntPathMatcher 通配)
- 排查 **踢人 / token 失效** → Redis key `bmos:user:token:%s`(loginToken → userId 映射)
- ⚠️ 历史上工作区被 TSD 加密需 `git show HEAD:<path>`,**现已解密可直接读**

## 相关页面

- [[service-overview]] — 5 服务端口/context-path 速查
- [[service-integration]] — 网关本身不参与 Feign 调用矩阵
- [[auth-and-license]] — 网关 token 校验与下游 license 校验的关系(待建)
- [[monorepo-architecture]] — TSD 加密约束、版本不一致
