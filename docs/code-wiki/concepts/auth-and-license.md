---
title: 认证与 License 校验流程
created: 2026-06-30
updated: 2026-07-15
type: concept
service: cross
tags: [architecture, auth, license, integration, backend]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/controller/UserController.java
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/service/impl/UserServiceImpl.java
  - packages/backend/services/platform/bmos-platform-facade/src/main/java/com/bmos/platform/facade/auth/interceptor/TokenValidateInterceptor.java
  - packages/backend/services/gateway/src/main/java/com/bmos/gateway/filter/AuthenticationFilter.java
  - packages/backend/services/platform/bmos-platform-common/src/main/java/com/bmos/platform/common/utils/RsaUtils.java
status: active
---

# 认证与 License 校验流程

> 两套独立横切机制，均由 **platform 主导**：**认证**（JWT + Redis 双重会话）与 **License**（每请求激活码校验）。gateway 与各业务服务协同。

## 一、认证:JWT + Redis 双重会话

**核心设计**:返回前端的是 JWT,但真正的会话凭证是 JWT Claims 里的 `loginToken`(UUID) 落在 Redis。判定有效 = **JWT 签名合法 且 Redis 中该 token 仍映射到同一 userId**(支持踢人:删 Redis 即失效)。

### 登录签发(platform)
`UserController.login` → `UserServiceImpl.login`(`system/user/service/impl/`):
1. 查用户 → 校验激活/状态/密码(密码 **DES 对称**解密比对,非 BCrypt)
2. 错误次数累计,超 `platform.user.pwd-rule.tryNum` 锁定(`PASSWORD_LOCK`)
3. 同终端已登录则挤下线(删旧 token/ip 缓存)
4. `token = IdUtil.fastUUID()`(会话凭证)
5. `createLoginToken`:JWT Claims 放 `userId`+`loginToken`,`JwtUtils.createToken` 签发
6. 写 Redis(`PermissionRedisDao`):`token→userId`、`userId→User`、`token→ip`

### 网关校验(gateway)
`AuthenticationFilter`(GlobalFilter,order=0),见 [[gateway-overview]]:OPTIONS 放行 → `bmos.auth.enable` 总开关 → `excludeUrls` 白名单(AntPathMatcher) → 取 header `BMOS_TOKEN` → `JwtUtils.parseToken` → Claims 的 `loginToken` 填 `bmos:user:token:%s` 反查 userId → 与 Claims `userId` 比对 → 通过则 URL 编码 token 回写下游。

### 各业务服务本地校验
各服务 `@EnableBmosAuth` → `BmosAuthAutoConfiguration` 注册 `TokenValidateInterceptor`(order=1):每请求**本地读 Redis**校验 token 并续期 `bmos:user:token:%s` 的 2h TTL。不为本请求再回调 platform。

### Redis 会话 key(`BmosRedisKeyDefine`,platform facade 与 gateway 副本共享同一 Redis)
| key 模板 | 用途 | TTL |
|---|---|---|
| `bmos:user:token:%s` | loginToken→userId(**鉴权反查命脉**) | 2h(每请求续期) |
| `bmos:user:login:%s` | userId→{terminalType→token} Hash(挤人检测) | 2h |
| `bmos:user:ip:%s` | token→ip | 2h |
| `bmos:user:info:%s` | userId→BaseUserDO JSON | FOREVER |

> 踢人 = 同终端再登录删旧 token 缓存;登出需前端传 `terminalType` header。

## 二、License:每请求激活码校验

**核心设计**:各业务服务(mes/lims/wms/platform 自身)每个请求都 Feign 回调 platform 校验激活码——**平台是单点**。

- **激活码模型**:明文 JSON `Activate{date, mac, applicationName}`,`date="ALL"` 表永久。用 **RSA 私钥解密**激活码,校验本机 MAC(NetworkInterface)+ applicationName + 日期。激活码存 `bp_active` 表(单行覆盖,全局唯一)。
- **校验链路**:`TokenValidateInterceptor` 每请求 → `activeApiAdaptor.getActiveCode()` → 构造 `LicenseParamDTO(activeCode, spring.application.name)` → Feign 调 `ActiveValidFeign.activeValid` → platform `ActiveService.valid` 用 `RsaUtils` 解码校验 → `active=false` 抛 `ActiveException`(HTTP 403)。
- **平台侧入口**:`/api/app/platform/system/active/valid`(`ActiveValidFeignController`)。各服务前端入口:`/user/active`(保存)、`/user/actived`(查询)、`/user/mac`(取本机 MAC 供生成码)。
- **开关**:业务参数 `platform.sys.license.isRequired`——false 直接返回永久有效(跳过校验),true 才真正校验。

> 是**激活码模型**(本机激活、平台校验),非功能/流量 license。wms 的 `Lic.java` 是离线生成器残留(见 [[wms-overview]])。

## 关键代码路径
| 环节 | 路径 |
|---|---|
| 登录签发 | `platform-service/.../system/user/{controller/UserController, service/impl/UserServiceImpl}.java` |
| Redis 会话 | `platform-service/.../system/user/redis/PermissionRedisDao.java` + `platform-facade/.../auth/constant/BmosRedisKeyDefine.java` |
| 本地校验 SDK | `platform-facade/.../auth/{interceptor/TokenValidateInterceptor, config/BmosAuthAutoConfiguration, annotation/EnableBmosAuth}.java` |
| 网关校验 | `gateway/.../filter/AuthenticationFilter.java` |
| License 校验 | `platform-service/.../system/user/service/impl/ActiveServiceImpl.java` + `feign/system/user/ActiveValidFeignController.java` |
| RSA 工具(私钥硬编码) | `platform-common/.../common/utils/RsaUtils.java` |

> 边界:JWT 签发(`JwtUtils`/`SecurityConstant`)、Redis 封装(`RedisService`)、`BaseUserDO` 来自 `bmos-parent-starter`（`com.bmos.common` / `com.bmos.cache.redis`），已于 2026-07-15 引入 shared/（见 [[parent-starter-overview]]）。

## 隐藏地雷 ⚠️
1. **RSA 私钥硬编码**(`RsaUtils.java:21,26`):激活码 + 密码两把私钥明文写死,随 jar 分发,泄露即全线失守。
2. **密码 DES 对称加密**(非 BCrypt),密钥 `bmos.secret-key` 经 Nacos 下发,DB 密码可逆。
3. **`/user/loginNoValidate`**:仅凭 loginName 发有效 token,绕过密码/锁定——若网关白名单放行即后门。
4. **`bmos:user:info:%s` 无 TTL**,登出/挤人不清,改密/锁定后有状态不一致窗口。
5. **License 校验是请求级 Feign 回调**:platform 挂 → 所有业务服务请求全抛 `ActiveException`(强耦合单点)。

## 相关页面
- [[gateway-overview]] — 网关 AuthenticationFilter 鉴权细节
- [[platform-auth-module]] — platform 认证模块实体页
- [[platform-user-module]] — 用户/密码/锁定管理
- [[api-conventions]] — 401/403 异常码与统一响应
- [[parent-starter-overview]] — `ActiveApiAdaptor`/`Activate` license 适配层本体（已引入 shared/）
