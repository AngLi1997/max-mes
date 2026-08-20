---
title: Platform 认证与 License 模块
created: 2026-06-30
updated: 2026-06-30
type: entity
service: platform
tags: [backend, platform, module, auth, license]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/controller/UserController.java
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/service/impl/UserServiceImpl.java
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/redis/PermissionRedisDao.java
  - packages/backend/services/platform/bmos-platform-facade/src/main/java/com/bmos/platform/facade/auth/
  - packages/backend/services/platform/bmos-platform-common/src/main/java/com/bmos/platform/common/utils/RsaUtils.java
status: active
---

# Platform 认证与 License 模块

## 概述 / 职责

platform 服务的**认证与 license 校验中枢**:登录签发 JWT、维护 Redis 会话、激活码校验。是全平台鉴权源头(gateway 与各业务服务都依赖它写入的 Redis 会话)。

- 所属服务:platform(60100),模块在 `bmos-platform-service/.../system/user/` + `bmos-platform-facade/.../auth/` + `bmos-platform-common/utils/`
- 完整跨服务流程见 [[auth-and-license]](本页是 platform 侧的实体索引)

## Maven 模块

platform 三模块:`bmos-platform-common`(RsaUtils/Activate/枚举)、`bmos-platform-facade`(对外 Feign + auth SDK jar,供 wms/lims/mes 依赖)、`bmos-platform-service`(业务实现)。

## 目录结构（认证/license 关键文件）

| 用途 | 路径 |
|---|---|
| 登录/激活入口 Controller | `service/.../system/user/controller/UserController.java` |
| 登录/密码/license 实现 | `service/.../system/user/service/impl/{UserServiceImpl,ActiveServiceImpl}.java` |
| Redis 会话 DAO | `service/.../system/user/redis/PermissionRedisDao.java` |
| license Feign 入口(被回调) | `service/.../feign/system/user/ActiveValidFeignController.java` |
| RSA 工具(私钥硬编码) | `common/.../common/utils/RsaUtils.java` |
| 激活码模型 | `common/.../common/utils/Activate.java` |
| Redis key 定义(共享) | `facade/.../auth/constant/BmosRedisKeyDefine.java` |
| 对外 license Feign | `facade/.../auth/feign/ActiveValidFeign.java` |
| 各服务 token 拦截器 | `facade/.../auth/interceptor/TokenValidateInterceptor.java` |
| 认证自动装配 | `facade/.../auth/config/BmosAuthAutoConfiguration.java` |
| 启用认证注解 | `facade/.../auth/annotation/EnableBmosAuth.java` |

## 核心 API（UserController，前缀 `/user`）

| 方法 | 路径 | 用途 |
|---|---|---|
| POST | `/user/login` | 登录(DES 比对 + 错误锁定 + JWT + Redis + 多端互踢) |
| POST | `/user/loginNoValidate` | ⚠️ 免密登录(仅 loginName 发 token) |
| DELETE | `/user/logout` | 登出(清 token/ip/login 缓存,需 terminalType header) |
| GET | `/user/status` | 当前登录用户信息 |
| POST | `/user/active` | 保存激活码 |
| POST | `/user/actived` | 查询是否已激活 → `RsaVO{active,date}` |
| GET | `/user/mac` | 取本机 MAC(供生成激活码) |

## Redis 会话（PermissionRedisDao）

| 常量 | key 模板 | TTL |
|---|---|---|
| USER_TOKEN_ID_CACHE | `bmos:user:token:%s` | 2h(每请求续期) |
| USER_LOGIN_CACHE | `bmos:user:login:%s`(Hash:terminalType→token) | 2h |
| USER_TOKEN_IP_CACHE | `bmos:user:ip:%s` | 2h |
| USER_INFO_CACHE | `bmos:user:info:%s` | FOREVER |

> gateway 的 `BmosRedisKeyDefine` 是 facade 的精简副本(仅 token/login),与 platform 共享同一 Redis。

## license 校验（ActiveService + ActiveValidFeign）

- 对外 facade `ActiveValidFeign`(@FeignClient bmos-platform-service)→ `POST /api/app/platform/system/active/valid`,实现 `ActiveValidFeignController` → `ActiveService.valid`
- 用 `RsaUtils` 私钥解激活码 → 校验 MAC + applicationName + 日期;开关 `platform.sys.license.isRequired`
- 激活码存 `bp_active` 表(单行覆盖,全局唯一)

## 与其他服务关系

- **gateway**:依赖 platform 写入的 `bmos:user:token:%s` 反查会话(见 [[gateway-overview]])
- **mes/lims/wms**:依赖 facade auth SDK(`@EnableBmosAuth` + `TokenValidateInterceptor`)做本地 token 校验,并每请求 Feign 回调 `/system/active/valid` 做 license 校验
- 完整链路见 [[auth-and-license]]、[[service-integration]]

## 隐藏地雷 ⚠️

1. **RSA 私钥硬编码**(RsaUtils.java:21,26):激活码 + 密码两把私钥明文写死。
2. **密码 DES 对称加密**(非 BCrypt),`bmos.secret-key` 经 Nacos 下发。
3. **`loginNoValidate` 后门**:仅凭 loginName 发有效 token。
4. **USER_INFO_CACHE 无 TTL**:登出/挤人不清,改密后有状态不一致窗口。
5. **重复枚举**:`UserActiveEnums`(common)与 `ActiveEnum`(service)同义重复,易混。
6. **JWT 密钥不可见**:`JwtUtils`/`SecurityConstant` 在外部 jar,签名密钥/有效期无法在仓库内核验。

## AI 定位提示

- 改 **登录/登出/会话** → `system/user/service/impl/UserServiceImpl.java`(login/logout/createLoginToken)
- 改 **Redis 会话 key/踢人** → `system/user/redis/PermissionRedisDao.java`
- 改 **license/激活校验** → `ActiveServiceImpl` + `ActiveValidFeignController` + `RsaUtils`
- 改 **各服务鉴权拦截** → `facade/.../auth/interceptor/TokenValidateInterceptor.java`(SDK,影响 mes/lims/wms)
- 排查 **401/会话失效** → 见 [[auth-and-license]] 流程

## 相关页面

- [[auth-and-license]] — 完整认证 + license 流程(跨服务视图)
- [[platform-user-module]] — 用户/角色/权限/菜单管理(同 system/user 包)
- [[gateway-overview]] — 网关侧 token 反查
- [[service-integration]] — facade 暴露的 Feign 矩阵
- [[monorepo-architecture]] — 外部 jar 依赖边界
