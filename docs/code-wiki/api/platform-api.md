---
title: Platform API 规范
created: 2026-06-30
updated: 2026-06-30
type: api
service: platform
tags: [backend, platform, api, auth]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/
  - packages/backend/services/platform/bmos-platform-facade/src/main/java/com/bmos/platform/facade/
status: active
---

# Platform API 规范

## 网关路由

- 前端前缀 `/api/app/platform/**` → gateway → `lb://bmos-platform-service`(60100)
- context-path `/api/app/platform`;路由/白名单在 Nacos(见 [[gateway-overview]])

## 统一响应与异常码

- 响应体 `ResponseInfo<T>`(成功 code=0),见 [[api-conventions]]
- 业务错误码段 **81**(格式 `81_YY_ZZZ`),`PlatformResponseCode`
- 鉴权:gateway 校验 JWT+Redis;license 经 facade auth SDK,见 [[auth-and-license]]

## 核心接口地图（按 Controller 前缀）

**认证 / license**(见 [[platform-auth-module]])

| 前缀 | 核心接口 |
|---|---|
| /user | `POST /login`、`POST /loginNoValidate` ⚠️、`DELETE /logout`、`GET /status`、`POST /active`、`POST /actived`、`GET /mac` |

**用户权限**(见 [[platform-user-module]])

| 前缀 | 核心接口 |
|---|---|
| /user | `/page`、`/save`、`/start`、`/resetPwd`、`/permission`(按钮码)、`/relate-role-save`、`/relate-dept-save` |
| /role | `/menu/save`(功能授权)、`/auth/menu/save`(可授权权限)、`/tree-all` |
| /menu | ★ `/auth/tree`(动态菜单下发)、`/admin/tree`、`/save` |
| /dept | `/tree-all`、`/partition/tree`(数据权限分区树)、`/user/tree` |
| /resource/permission | `/save`、`/list/dept`(资源→部门) |

**基础主数据**:物料 `/material` · 单位 `/unit` · 业务参数 `/parameter` · 编码取号 `/code` · 工厂建模 · 字典 · 标签 · 设备 · 消息(详见 [[platform-overview]] 13 子域)

**对外 Feign**(facade,被 mes/lims/wms 调,见 [[service-integration]])

| Feign | 前缀 | 高频方法 |
|---|---|---|
| UserFeign | /feign/user | `listByUserIds`、`getUserByName`、`listByMenuIdAndResourceId` |
| RoleFeign | /feign/role | `authUserList(authCode)` |
| MenuFeign | /feign/menu | `getAllChildMenuIdList` |
| DeptFeign | /feign/dept | `tree-all`、`user/tree` |
| ActiveValidFeign | /system/active/valid | license 激活校验 |

## 分页

`BasePage`(pageNum/pageSize≤100) → `CommonPage<T>`,PageHelper(见 [[api-conventions]])。

## 相关页面

- [[platform-overview]] / [[platform-auth-module]] / [[platform-user-module]]
- [[api-conventions]] / [[auth-and-license]] / [[service-integration]]
