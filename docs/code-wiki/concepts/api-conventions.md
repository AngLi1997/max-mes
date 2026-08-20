---
title: API 约定（统一响应 / 异常码 / 路由）
created: 2026-06-30
updated: 2026-07-15
type: concept
service: cross
tags: [architecture, api, convention, backend]
sources:
  - packages/backend/services/gateway/src/main/resources/bootstrap.yml
  - packages/backend/services/mes/bmos-mes-common/src/main/java/com/bmos/mes/common/exception/MesResponseCode.java
  - packages/backend/services/wms/bmos-wms-common/src/main/java/com/bmos/wms/common/exception/WmsResponseCode.java
  - packages/backend/services/lims/bmos-lims2-common/src/main/java/com/bmos/lims2/common/i18n/LimsResponseCode.java
  - packages/backend/services/platform/bmos-platform-common/src/main/java/com/bmos/platform/common/exception/PlatformResponseCode.java
status: active
---

# API 约定（统一响应 / 异常码 / 路由）

> ⚠️ **共性代码已引入**：`ResponseInfo`/`BmosException`/`BaseResponseCode`/`GlobalExceptionHandler`/`BasePage`/`CommonPage` 均来自 `bmos-parent-starter` 工程，已于 2026-07-15 经 git subtree 引入 `packages/backend/shared/bmos-parent-starter/`（见 [[parent-starter-overview]]，源码在 `bmos-starter-common` / `bmos-starter-web`）。各服务只维护自己的业务错误码段位。

## 统一响应体 `ResponseInfo<T>`

全 5 服务唯一封装类 `com.bmos.common.response.ResponseInfo<T>`(4 字段):

| 字段 | 类型 | 说明 |
|---|---|---|
| `code` | int | 响应码,**成功 = 0**(非 200) |
| `message` | String | 经 i18n 处理的返回信息 |
| `data` | T | 数据 |
| `args` | Object[] | i18n 参数(hidden) |

工厂:`ResponseInfo.success(data)` / `success()`;`failure(ResponseItem)` / `failure(int, String)`。判定 `isSuccess()`(code==0)/`isError()`。**无 R.ok/R.fail 别名**。

## 异常码体系

格式 `XX_YY_ZZZ` = 服务段(2位)_模块段(2位)_序号,消息支持 `{0}` 占位符(MessageFormat)。

**通用码**(`BaseResponseCode`,共享):`SUCCESS=0`、`UN_ACTIVE=301`、`UN_AUTHORIZATION=401`、`SERVER_EXCEPTION=500`、`ILLEGAL_REQUEST_PARAMETER=507`、`DUPLICATE_KEY_ERROR=508`、`FEIGN_REMOTE_CALL_ERROR=510`。

**各服务段位分配**:

| 服务 | 错误码类 | 主段 | 代表码 |
|---|---|---|---|
| platform | `PlatformResponseCode` | **81** | `81_01_004` 物料编码已存在 |
| mes | `MesResponseCode` | **82** | `82_01_002` 工艺名已存在 |
| wms | `WmsResponseCode` | **83**(仅 00/01/09/10) | `83_09_001` 存储区不存在 |
| lims | `LimsResponseCode` | **83**(混 81/82/83) | `83_11_01` 样品未接收无法分样 |

> **段位澄清**:`83_11_xxx`(分样模块)**只在 lims 出现**,wms 无此段(此前 wiki 笼统记 wms 83_11_xxx 有误,以此页为准)。

## 全局异常处理

共享兜底 `com.bmos.web.exception.GlobalExceptionHandler`(@RestControllerAdvice):

- `BmosException`(最常用业务异常,持 `ResponseItem`)→ 用其 code+args
- 校验类(ValidationException / MethodArgumentNotValid / ConstraintViolation 等)→ 507 + 字段错误 Map + HTTP 422
- `AuthorizationException` / `ActiveException` → HTTP 403;`DuplicateKeyException` → 508;兜底 `Exception` → 500

各服务追加:mes/lims 各有 `MesExceptionHandler`/`MesAuditExceptionHandler` 处理引擎 `InfiniteEngineException`;wms `WmsExceptionHandler` 空壳;platform 无自定义。

> 用法:`throw new BmosException(MesResponseCode.PROCESS_NOT_EXIST);`

## 网关路由约定

| 前端前缀 | 目标服务(uri) | 端口 |
|---|---|---|
| `/api/app/platform/**` | `lb://bmos-platform-service` | 60100 |
| `/api/app/mes/**` | `lb://bmos-mes-service` | 60200 |
| `/api/app/lims2/**` | `lb://bmos-lims2-service` | 61001 ⚠️ lims2 |
| `/api/app/wms/**` | `lb://bmos-wms-service` | 60900 |

> ⚠️ **路由表不在代码仓库**:gateway `application.yml` 仅 `server.port:60300`,路由/白名单/CORS/限流全托管在 **Nacos**(`bmos-gateway-service-${profile}.yaml`)。见 [[gateway-overview]]。

## 分页

**用 PageHelper(MySQL 方言),非 MyBatis-Plus 分页插件**。

- 请求:`com.bmos.mybatis.page.BasePage`(pageNum 默认1 / pageSize 默认20 上限100 / orderBy)
- 响应:`com.bmos.mybatis.page.CommonPage<T>`(pageNum/pageSize/totalPage/total/list),工厂 `CommonPage.convertPage(List)`
- Controller 典型签名:`ResponseInfo<CommonPage<XxxVO>>`

## 相关页面
- [[gateway-overview]] — 网关鉴权与路由(Nacos)
- [[data-access-pattern]] — BaseDO/Mapper/分页底座
- [[service-overview]] — 端口/context-path 速查
- [[auth-and-license]] — 401/403 触发场景
- [[parent-starter-overview]] — 共性代码本体（已引入 shared/）
