---
title: MES Requisition 模块（领料 · 预约/收货/发料 + mes 唯一外向调 wms）
created: 2026-07-06
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis, feign, integration]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/requisition/
status: active
---

# MES Requisition 模块

## 概述 / 职责

Requisition 模块是 mes 的**领料/预约/收货域**：从生产计划驱动领料需求、wms 库存查询与库存预约（reserve）、到货收货（receive）、发料出库（sendOut）的全流程。是 [[mes-overview]] 头部子域中**唯一外向调用 wms 的子域**——`WmsFeignClient` 是 mes → wms 的唯一 Feign 入口。

- 包路径：`com.bmos.mes.service.requisition/`
- 规模：**Controller 1（25 接口）· Service 接口 1（22 方法）· Mapper 7 · Java 60 · 表 7**
- 关键依赖：wms（`WmsFeignClient` 查库存 / 提交发料单）、mcp（★ `queryInventoryData` 借道 mcp 子域 DTO/VO）、plan（生产计划驱动）、storage（mes 端物料批次视图）；详见耦合点
- 独有机制：**mes → wms 唯一外向集成**、**库存预约（reserve）/ 收货（receive）/ 发料（sendOut）三段式**

> ⚠️ **职责集中度提醒**：requisition 是 mes 中**唯一持有 wms Feign 客户端**的子域。其它子域若需 wms 数据，应通过 requisition 的 Service 暴露的方法，而非直接 new Feign。这是 mes 的服务边界约定。

## 三段式流程

requisition 的 25 个 Controller 接口按业务环节分三段：

| 阶段 | 关键方法 | 涉及表 |
|---|---|---|
| **① 预约（reserve）** | `reserveStorageMaterial` / `reserveRepositoryMaterial` / `cancelReservedSingle` / `cancelRepositoryMaterial` | `bm_reserve_component_instance` / `bm_reserve_component_material` / `bm_requisition_plan_reserved` |
| **② 收货（receive）** | `receiveRepositoryByBatch` / `receiveRepositoryByMaterial` / `completeReceive` | `bm_requisition_received` / `bm_requisition_received_batch` / `bm_requisition_received_material` |
| **③ 发料（sendOut）** | `sendOut` / `cancelSendOut`（→ 调 wms `submitSendOutOrderByBatch`） | `bm_requisition_plan` |

> 辅助：`getMaterialRequisitionPlanVO`（领料计划）/ `getRequisitionList`（领料清单）/ `getComponentBoundRequisition`（组件绑定领料）/ `calculateQuantity`（量计算）等查询/算量方法。

## 数据模型（7 张表）

### 领料计划（1 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_requisition_plan` | `Requisition` | ★ 领料计划主表（注：Model 类名 `Requisition`，非 `RequisitionPlan`） |

> ⚠️ **命名映射**：表 `bm_requisition_plan` → Model 类 `Requisition`（不是 `RequisitionPlan`）。与 [[mes-plan-module]] 的 `bm_product_plan` → 类 `Plan` 同类历史命名——类名与表名不一致时显著标注，避免 AI 误读。

### 预约（reserve，3 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_requisition_plan_reserved` | `RequisitionMaterialReserved` | 领料计划的已预约物料 |
| `bm_reserve_component_instance` | `ReserveComponentInstance` | 组件实例级预约 |
| `bm_reserve_component_material` | `ReserveComponentMaterial` | 组件物料级预约 |

### 收货（receive，3 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_requisition_received` | `RequisitionReceived` | 收货主表 |
| `bm_requisition_received_batch` | `RequisitionReceivedBatch` | 批次级收货 |
| `bm_requisition_received_material` | `RequisitionReceivedMaterial` | 物料级收货 |

> 表前缀注意：`bm_requisition_*`（领料/收货）与 `bm_reserve_*`（组件预约）是**两个不同前缀**，对应两个业务子概念。

## ★ 对外 Feign 契约：WmsFeignClient

`feign/WmsFeignClient.java` —— `@FeignClient(name = "bmos-wms-service")`，mes 唯一调 wms 的入口。**4 个方法**：

| 方法 | wms 路径 | 用途 |
|---|---|---|
| `queryBatchByMaterial(dto)` | `POST /api/app/wms/inventory/batchList` | 按物料查 wms 库存批次 |
| `queryAvailableQuantityList(dto)` | `POST /api/app/wms/inventory/availableQuantityList` | 查可用库存量 |
| `submitSendOutOrderByBatch(dto)` | `POST /api/app/wms/sendOut/submit` | ★ 提交发料出库单（requisition → wms 的核心写操作） |
| `queryInventoryData(dto)` | `POST /api/app/wms/mcp/inventory` | ⚠️ 查 wms 库存——**DTO/VO 借道 mcp 子域**（见下方隐藏耦合） |

> 调用方向：mes → wms（requisition 是发起方）。wms 反向调 mes 见 [[mes-overview]] 调用关系章节。

## Controller（1 个 · `/requisition`）

`RequisitionController` 共 25 个接口，按业务环节：

| 类别 | 接口（节选） |
|---|---|
| **预约 reserve** | `POST /storage/reserve`（预约）/ `POST /storage/cancel`（取消预约）/ `GET /reservedAvailableMaterial` / `GET /reservedMaterial` |
| **收货 receive** | `POST /receive/repository/batch`（按批收货）/ `POST /receive/repository/material`（按物料收货）/ `POST /receive/repository/complete` / `POST /receive/complete` / `GET /receive/repository/*`（批次/可用量/已预约批次等查询）/ `GET /receive/material/list` / `GET /receive/boundRequisition` |
| **发料 sendOut** | `POST /receive/sendOut`（调 wms 提交发料单） |
| **查询/算量** | `GET /detail` / `GET /list` / `GET /receive/repository/materialBatch` |

## Service 核心方法（RequisitionService · 22 个）

### 领料计划

| 方法 | 功能 |
|---|---|
| `handleNameAndSave(requisition)` | 保存领料计划 |
| `getMaterialRequisitionPlanVO(dto)` | 查领料计划 |
| `getRequisitionList(batchId)` / `getRequisitionMaterialBatchList(requisitionId)` | 领料清单 / 物料批次 |
| `getComponentBoundRequisition(dto)` | 组件绑定的领料 |

### 预约 reserve

| 方法 | 功能 |
|---|---|
| `reserveStorageMaterial(dto)` / `reserveRepositoryMaterial(dto)` | 预约库存物料 |
| `reserveComponentCancelReserve(dto)` | 组件取消预约 |
| `cancelReservedSingle(dto)` / `cancelRepositoryMaterial(dto)` | 取消单条/取消仓库物料预约 |
| `getReserveComponentInstanceInfo(dto)` | 预约组件实例信息 |

### 收货 receive

| 方法 | 功能 |
|---|---|
| `receiveRepositoryByBatch(dto)` / `receiveRepositoryByMaterial(dto)` | 按批/按物料收货 |
| `completeRequisitionPlan(dto)` / `completeReceive(dto)` | 完成领料计划 / 完成收货 |
| `getRepositoryBatchMaterialList(dto)` / `getRepositoryMaterialBatch(dto)` / `getRepositoryReservedBatch(dto)` / `getRepositoryMaterialQuantityList(dto)` | 仓库批次/物料/已预约/量查询 |

### 发料 sendOut

| 方法 | 功能 |
|---|---|
| `sendOut(dto)` | ★ 发料（调 wms `submitSendOutOrderByBatch`） |
| `cancelSendOut(requisitionPlanId)` | 取消发料 |

### 算量

| 方法 | 功能 |
|---|---|
| `calculateQuantity(dto)` | 领料量计算 |
| `getBatchReservedMaterialInfo(dto)` | 批次已预约物料信息 |

## 独有机制

### mes → wms 唯一外向集成

整个 mes 服务对 wms 的 Feign 调用**只通过本子域的 `WmsFeignClient`**（详见 [[service-integration]] 调用矩阵）。这是 mes 的服务边界设计：wms 数据/操作的统一收口在 requisition，避免散落各子域。其它子域需要 wms 库存数据时，调 requisition 的 Service，不直接持 Feign。

### ⚠️ 隐藏耦合：借道 mcp 子域 DTO/VO

`WmsFeignClient.queryInventoryData` 的入参 `WmsStorageInventoryDataQuery` 与出参 `WmsStorageInventoryFeignVO` 都来自 **mcp 子域**（`com.bmos.mes.service.mcp.dto` / `com.bmos.mes.service.mcp.vo`），而非 requisition 自己的 dto/vo 包。

> 这暗示 mcp（[[mes-overview]] 中部子域，22 Java）是 **wms 集成的适配/契约层**——wms 库存数据的 DTO 契约由 mcp 子域定义，requisition 的 Feign 方法只是调用入口。改 wms 库存相关 DTO 结构必须看 mcp 子域的定义。详见 [[mes-overview]] 中部子域的 mcp 行（⏳ 未建独立页）。

## 与其它子域 / 服务的耦合点

- **→ wms（跨服务）**：★ 唯一外向集成。`WmsFeignClient` 查 wms 库存 + 提交发料单。详见 [[service-integration]] 与 [[wms-overview]]。
- **↔ mcp**：`queryInventoryData` 借道 mcp 的 DTO/VO（wms 库存契约在 mcp）。
- **← plan**：领料计划由生产计划驱动。详见 [[mes-plan-module]]。
- **↔ storage**：预约/收货与 mes 端库位物料批次关联（storage 是 mes 端视图，wms 是仓库端视图，requisition 在两者间衔接）。详见 [[mes-storage-module]]。
- **→ 内部**：无 MQ、无定时任务、无独立枚举——纯同步业务。

## AI 定位提示

- **wms 调用问题**（库存查不到 / 发料失败） → `WmsFeignClient`（`queryBatchByMaterial` / `queryAvailableQuantityList` / `submitSendOutOrderByBatch` / `queryInventoryData`）
- **wms 库存 DTO 改动** → ⚠️ `queryInventoryData` 的 DTO/VO 在 **mcp 子域**，不是 requisition
- 库存预约异常 → `reserveStorageMaterial` / `cancelReservedSingle`（reserve 段）
- 收货流程 → `receiveRepositoryByBatch` / `receiveRepositoryByMaterial` / `completeReceive`（receive 段）
- 发料出库 → `sendOut`（→ wms `submitSendOutOrderByBatch`）
- 领料量计算 → `calculateQuantity`
- **新增 wms 调用** → ⚠️ 不要在其它子域新建 Feign，统一加到本子域 `WmsFeignClient`（mes 服务边界约定）

## 相关页面

- [[mes-overview]] — mes 服务总览（requisition 为头部子域；mes → wms 唯一调用方）
- [[mes-storage-module]] — mes 端物料批次视图（requisition 预约/收货的关联）
- [[mes-plan-module]] — 生产计划（领料需求的来源）
- [[service-integration]] — mes → wms 的 Feign 调用矩阵（requisition 是唯一入口）
- [[wms-overview]] — 调用目标服务
- [[database-schema-overview]] — `bm_requisition_*` / `bm_reserve_*` 7 表归属
