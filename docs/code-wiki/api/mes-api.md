---
title: Mes API 规范
created: 2026-06-30
updated: 2026-06-30
type: api
service: mes
tags: [backend, mes, api]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/
  - packages/backend/services/mes/bmos-mes-feign/src/main/java/com/bmos/mes/feign/
status: active
---

# Mes API 规范

## 网关路由

- `/api/app/mes/**` → gateway → `lb://bmos-mes-service`(60200),context-path `/api/app/mes`

## 统一响应与异常码

- `ResponseInfo<T>`(code=0 成功);业务码段 **82**(`MesResponseCode`,见 [[api-conventions]])
- 鉴权:gateway JWT + `@EnableBmosAuth` 本地校验 + 每请求 license 回调 platform(见 [[auth-and-license]])

## 核心接口地图（101 Controller，按业务域）

| 域 | 核心 Controller 前缀 | 说明 |
|---|---|---|
| **weigh** 称量(13) | /materialWeighing、/weighingCenter、/weighingWorkOrder、/weighingHistory | 称量五域 |
| **plan** 计划(13) | /plan、/planArchive、/batchTemplate、/instruction | 生产排程→批记录归档(见 [[mes-plan-module]]) |
| **process** 工艺(6) | /process、/procedure、/procedureStep | 工艺编排(见 [[mes-process-module]]) |
| **record** 批记录 | /record、/batchRecord | 文档载体(见 [[mes-record-module]]) |
| **product** 物料 | /material、/materialCategory | 物料主数据(见 [[mes-product-module]]) |
| **storage** 库存(8) | /storage、/inventory | 暂存间/库存 |
| **execute / dataset / audit** | /execute、/dataset、/audit | 执行/数据集/审计(头部子域) |

> mes 体量大(101 Controller / 38 子域),本页只给域级地图,单域细节见 [[mes-overview]] 及各子模块页。

## 对外 Feign（被 lims/wms 调，见 [[service-integration]]）

| Feign | 用途 |
|---|---|
| InspectFeign | 检验(被 lims/wms 调) |
| MaterialFeign / MaterialBatchFeign | 物料 / 批次 |

## 检验三方联动

mes 发起请验 → lims 检验 → lims 按 `source_system` 回调 mes `/feign/inspect/callback`(见 [[lims-overview]])。

## 分页

`BasePage` → `CommonPage<T>`(PageHelper)。

## 相关页面

- [[mes-overview]] / [[mes-plan-module]] / [[mes-process-module]] / [[mes-record-module]] / [[mes-product-module]]
- [[api-conventions]] / [[service-integration]]
