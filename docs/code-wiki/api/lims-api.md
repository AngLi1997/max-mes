---
title: Lims API 规范
created: 2026-06-30
updated: 2026-06-30
type: api
service: lims
tags: [backend, lims, api]
sources:
  - packages/backend/services/lims/bmos-lims2-web/src/main/java/com/bmos/lims2/web/
  - packages/backend/services/lims/bmos-lims2-feign/src/main/java/com/bmos/lims2/feign/
status: active
---

# Lims API 规范

## 网关路由

- `/api/app/lims2/**` → gateway → `lb://bmos-lims2-service`(61001)⚠️ **是 lims2 不是 lims**
- context-path `/api/app/lims2`;命名分裂五层不一致(见 [[lims-overview]])

## 统一响应与异常码

- `ResponseInfo<T>`;业务码段 **83**(混 81/82/83,`LimsResponseCode`),**`83_11_xxx` = 分样模块**(见 [[api-conventions]])
- 鉴权同 mes(gateway + `@EnableBmosAuth` + license 回调)
- 文档转换深度依赖 Aspose.Words(`license.xml` 授权,见 [[lims-overview]])

## 核心接口地图（63 Controller，按子域）

| 域 | Ctrl | 核心 Controller 前缀 |
|---|---|---|
| **inspect** 检验(28) | /inspect/order、/inspection-scheme*、/inspect/item\|method\|parameter\|sampling\|query\|trend、/sample*、/sample-audit、/sample-receive、/retention-*、/document/config、/mes/inspect |
| **eln** 电子记录(11) | /record*、/signature、/mobile/signature、/dSignature、/app/eln/*、/app/task、/app/conclusion |
| **stability** 稳定性(7) | /stability-* |
| report(2) | /report* |
| operate(3) / material(4) / task(1) / audit(2) | /operateRule、/material、/task、/flowAudit* |

## 对外 Feign（见 [[service-integration]]）

- **暴露** `MesInspectFeign`(/mes/inspect/document-config、/schemes、/order 建单、/order/retry)——被 mes 调建检验单
- **回调** mes/wms:`MesInspectCallbackClient`、`WmsInspectCallbackClient`(/feign/inspect/callback、/reject),按 `source_system` 路由(WMS→wms,否则→mes)

## 审批流

`/flowAudit*`(audit 子域),按 `AuditCategoryCodeEnum.code` 在 `lm_flow_audit_process` 找绑定流程(见 [[lims-overview]] 审批流引擎)。

## 分页

`BasePage` → `CommonPage<T>`(PageHelper)。

## 相关页面

- [[lims-overview]] / [[api-conventions]] / [[service-integration]]
