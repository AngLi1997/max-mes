---
title: Wms API 规范
created: 2026-06-30
updated: 2026-06-30
type: api
service: wms
tags: [backend, wms, api]
sources:
  - packages/backend/services/wms/bmos-wms-service/src/main/java/com/bmos/wms/service/
  - packages/backend/services/wms/bmos-wms-feign/src/main/java/com/bmos/wms/inspect/feign/
status: active
---

# Wms API 规范

## 网关路由

- `/api/app/wms/**` → gateway → `lb://bmos-wms-service`(60900),context-path `/api/app/wms`

## 统一响应与异常码

- `ResponseInfo<T>`;业务码段 **83**(仅 00/01/09/10,`WmsResponseCode`),**无 83_11**(见 [[api-conventions]])
- 鉴权同上;license 激活 `/user/active`、`/user/actived`、`/user/mac`

## 核心接口地图（14 Controller）

| 前缀 | 用途 |
|---|---|
| /cargo、/cargo/category | 货品主数据 / 分类 |
| /inventory | 库存(出入库 / 盘点 / 移库) |
| /material/position | 货位 |
| /storage/config | 存储区域树(4 级,StorageLevelEnum) |
| /inspect(前端) | 请验单 UI |
| /feign/inspect(LIMS 回调入向) | 实现 `InspectFeign` 供 lims 反调 |
| /sendOut | 发料单(→ mes) |
| /log/cargo、/log/position、/log | 库存变动 / 操作日志 |
| /unit | 平台 unit 代理 |
| /user(active) | license 激活 |
| /resource/permission | 数据权限 |

> `InspectController`(前端 UI)与 `InspectFeignController`(供 LIMS 反调)职责不同。

## 对外 Feign（见 [[service-integration]]）

- **暴露** `InspectFeign`(/feign/inspect/callback 结果回传、/reject)——被 lims 回调
- **调用** mes(`/requisition/receive/sendOut` 发料)、platform(物料/编码/用户/参数)、lims(经 `BmosLimsGateway` 发起请验)

## 检验三方联动

wms 发起请验 → lims 检验 → lims 回调 wms `InspectFeign.callback`(出向 `sourceSystem=WMS`,见 [[wms-overview]])。

## 分页

`BasePage` → `CommonPage<T>`(PageHelper)。

## 相关页面

- [[wms-overview]] / [[api-conventions]] / [[service-integration]]
