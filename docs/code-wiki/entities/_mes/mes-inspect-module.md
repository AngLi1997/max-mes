---
title: MES Inspect 模块（检验 · lims 网关双模式 + 请验/回调全流程）
created: 2026-07-06
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis, integration]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/inspect/
status: active
---

# MES Inspect 模块

## 概述 / 职责

Inspect 模块是 mes 的**检验域**：检验配置（按物料绑定请验单/检验方案）、发起请验、重试、lims 回调接收结果、驳回、检验结果查询，以及与业务组件（record）的结果回填。是 [[mes-overview]] 中部子域里**跨服务联动最密集**的——mes / lims / wms 三方在检验环节交汇。

- 包路径：`com.bmos.mes.service.inspect/`
- 规模：**Controller 4 · Service 接口 3 · Mapper 6 · Java 53 · 表 6**（前缀 `bm_inspect*`）
- 关键依赖：lims（★ 通过网关抽象，支持自研/第三方双模式）、platform（开关参数 `INSPECT_LIMS_CONFIG`）、record/execute（检验结果回填到业务组件）、product（物料主数据）；详见耦合点
- 独有机制：★ **LIMS 网关双模式（策略 + 选择器 + 开关）**、**请验/回调闭环**

> ⚠️ **跨服务检验闭环**：inspect 是 mes 侧检验的发起方与回调接收方。`InspectFeignController`（`/feign/inspect/callback`）是 **lims 检验完成后回调 mes** 的入口，对应 mes-feign 模块暴露的 `InspectFeign` 契约（见 [[mes-overview]] "对外 Feign 契约"章节）。lims 回调的 4 个跨服务共享 DTO（`InspectProgramResultDTO` / `InspectRejectDTO` / `InspectResultCallBackDTO` / `InspectResultItemDTO`）全部围绕检验场景。

## 核心机制：LIMS 网关双模式

inspect 子域**不直接 Feign 调 lims**，而是通过 `lims/` 子包的**策略 + 选择器 + 开关**抽象，支持"自研 bmos lims / 第三方 lims"双模式，运行时由平台参数动态切换。

### 结构（`inspect/lims/` 7 个类）

| 角色 | 类 | 职责 |
|---|---|---|
| **策略接口** | `LimsInspectGateway` | 统一抽象：`type()` / `queryConfig(materialId)` / `querySchemes(materialId)` / `initiate(ctx)` / `retry(ctx)` |
| **类型枚举** | `LimsType` | `BMOS`（自研）/ `THIRD_PARTY`（第三方） |
| **实现 1** | `BmosLimsGateway`（`impl/`） | ★ 自研路径——走 bmos-lims 服务（Feign），返回真实检验单号/配置/方案 |
| **实现 2** | `ThirdPartyLimsGateway`（`impl/`） | 第三方路径——`queryConfig`/`querySchemes` 返回空、`initiate`/`retry` 返回 null（由调用方走本地兜底） |
| **选择器** | `LimsGatewaySelector` | Spring 注入所有 `LimsInspectGateway`，按 `LimsType` 注册到 `EnumMap`，`current()` 返回当前生效的实现 |
| **开关** | `InspectLimsSwitch` | 读平台参数 `INSPECT_LIMS_CONFIG`（JSON），解析出 `{enabled, type}`，解析失败按"不对接"兜底 |
| **上下文** | `InitiateInspectContext` / `RetryInspectContext` | 发起/重试的入参封装 |

### 切换逻辑

```
InspectLimsSwitch.current()  ← 读 platform 参数 INSPECT_LIMS_CONFIG
   → {enabled, type}
LimsGatewaySelector.current()  ← 按 type 从 EnumMap 选 gateway
   → BmosLimsGateway (type=BMOS)  或  ThirdPartyLimsGateway (type=THIRD_PARTY)
InspectService.initiateInspect / retryInitiateInspect  → gateway.initiate(ctx) / retry(ctx)
```

> ⚠️ **改检验对接模式** → 改 platform 参数 `INSPECT_LIMS_CONFIG`（`bmos-platform-facade` 的 `BusinessParameterCodeConstants.INSPECT_LIMS_CONFIG`），**不改代码**。新增第三种 lims 类型 → 实现 `LimsInspectGateway` + 加 `LimsType` 枚举值，Spring 自动注入选择器。

> 这是 inspect 子域最重要的设计——把"对接哪个 lims"从代码硬编码变成运行时可配。详见 [[service-integration]] 的 mes↔lims 检验回调链。

## 数据模型（6 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_inspect` | `Inspect` | ★ 检验单主表（检验单号、物料、状态、来源） |
| `bm_inspect_info` | `InspectInfo` | 检验信息明细 |
| `bm_inspect_result` | `InspectResult` | 检验结果（lims 回调写入） |
| `bm_inspect_config` | `InspectConfig` | 检验配置主表（请验单/方案配置） |
| `bm_inspect_config_data` | `InspectConfigData` | 配置数据项 |
| `bm_inspect_config_material` | `InspectConfigMaterial` | 配置绑定的物料 |

> 配置侧 3 张表（`bm_inspect_config*`）+ 业务侧 3 张表（`bm_inspect` / `_info` / `_result`）。配置按物料组织，业务按检验单组织。

## Controller（4 个）

### 业务 Controller（3 个）

| Controller | 路由 | 职责 |
|---|---|---|
| `InspectController` | `/inspect` | ★ 检验业务：`initiate`（发起请验）/ `retry/initiate`（重试）/ `page` / `info` / `program/result` / `query/materialBatchNo/{...}/materialId/{...}`（按批次查结果）/ `schemes/{formulaMaterialId}` |
| `InspectConfigController` | `/inspect/config` | 检验配置：`save` / `update` / `delete` / `enable` / `disable` / `queryDetail` / `queryList` / `bind/material`（绑物料）/ `query/material/{id}` / `query/{formulaMaterialId}` |
| `InspectResultComponentController` | `/inspect/component` | 检验结果与业务组件：`notRejectPage`（未驳回结果分页）/ `confirm`（确认回填到业务组件） |

### Feign 回调 Controller（1 个）

| Controller | 路由 | 职责 |
|---|---|---|
| `InspectFeignController` | `/feign/inspect` | ★ **lims 回调入口**：`POST /callback`（检验结果回调 `InspectResultCallBackDTO`）/ `POST /reject`（驳回 `InspectRejectDTO`） |

> `InspectFeignController` 是 mes-feign 模块 `InspectFeign` 契约的实现方——lims 检验完成后调这两个接口通知 mes。这是 mes **被调**方向（lims → mes）。

## Service 核心方法

### `InspectService`（检验业务核心）

| 方法 | 功能 |
|---|---|
| `initiateInspect(dto)` | ★ 发起请验（经 gateway 双模式派发） |
| `retryInitiateInspect(dto)` | 重试发起（同上） |
| `inspectCallback(dto)` | ★ 接收 lims 回调结果（`InspectResultCallBackDTO`，被 `InspectFeignController.callback` 调） |
| `rejectInspect(dtoList)` | 驳回（`InspectRejectDTO`） |
| `queryPage(dto)` / `queryDetail(id)` | 分页/详情 |
| `queryInspectResult(id)` / `queryInspectResultByMaterialBatchNoAndMaterialId(...)` | 结果查询（按单/按批次） |
| `querySchemes(formulaMaterialId)` | 查检验方案（经 gateway） |

### `InspectConfigService`（检验配置）

| 方法 | 功能 |
|---|---|
| `save(dto)` / `update(dto)` / `delete(id)` | 配置 CRUD |
| `enable(id)` / `disable(id)` | 启用/禁用配置 |
| `bindMaterial(dto)` / `queryBindMaterial(id)` | 绑定物料 / 查已绑物料 |
| `queryDetail(id)` / `queryList(dto)` / `queryConfigByFormulaMaterialId(id)` | 详情/分页/按配方物料查 |

### `InspectComponentService`（结果与业务组件）

| 方法 | 功能 |
|---|---|
| `queryNotRejectInspectResultPage(dto)` | 未驳回检验结果分页 |
| `confirmFillFormData(dto)` | ★ 确认把检验结果回填到业务组件表单数据（接 [[mes-record-module]] / [[mes-execute-module]]） |

## 独有机制

### LIMS 网关双模式

见前述"核心机制"章节。**inspect 子域最核心的设计**——策略 + 选择器 + 开关，把 lims 对接方式从硬编码变成平台参数动态配置。

### 请验/回调闭环

```
mes inspect → gateway.initiate() → lims（自研）/ 本地兜底（第三方）
lims 检验完成 → POST /feign/inspect/callback → InspectService.inspectCallback → 写 bm_inspect_result
lims 驳回    → POST /feign/inspect/reject    → InspectService.rejectInspect
```

回调入参 DTO（`InspectResultCallBackDTO` / `InspectRejectDTO` 等）是 mes-feign 模块的跨服务共享对象，lims 侧也有同名引用。改这些 DTO 结构必须同步 lims。

### 检验结果回填业务组件

`InspectComponentService.confirmFillFormData` 把检验结果回填到 record/execute 的业务组件表单数据——检验作为批记录的一个数据来源。这是 inspect → record/execute 的写入点。

## 与其它子域 / 服务的耦合点

- **↔ lims（跨服务）**：★ 通过网关双模式对接。BMOS 模式调 bmos-lims Feign；回调统一走 `InspectFeignController`。详见 [[service-integration]] 与 [[lims-overview]]。
- **← platform**：开关参数 `INSPECT_LIMS_CONFIG`（`BusinessParameterCodeConstants`）+ 物料主数据。详见 [[platform-overview]]。
- **→ record / execute**：`InspectComponentService.confirmFillFormData` 回填业务组件表单数据。详见 [[mes-record-module]] / [[mes-execute-module]]。
- **← product**：检验配置按物料组织（`bindMaterial`）。详见 [[mes-product-module]]。
- **↔ wms（间接）**：[[mes-overview]] 提到 inspect 与 lims/wms 三方联动——wms 侧的物料批次检验场景通过 lims 回调链间接关联（无直接 Feign）。
- **→ 内部**：无 MQ、无定时任务——同步业务 + Feign 回调。

## AI 定位提示

- **检验对接 lims 失败/切换** → `InspectLimsSwitch.current()`（读 `INSPECT_LIMS_CONFIG` 参数）+ `LimsGatewaySelector.current()`；改对接模式改平台参数，不改代码
- **新增第三种 lims 类型** → 实现 `LimsInspectGateway` + 加 `LimsType` 值，Spring 自动注入
- **发起请验逻辑** → `InspectService.initiateInspect`（经 gateway 派发）
- **lims 回调结果没收到** → `InspectFeignController.callback` → `InspectService.inspectCallback`（检查 `bm_inspect_result` 是否写入）
- **驳回流程** → `InspectFeignController.reject` → `InspectService.rejectInspect`
- **检验配置（请验单/方案）** → `InspectConfigService`（配置按物料绑定）
- **检验结果回填业务组件异常** → `InspectComponentService.confirmFillFormData`
- **改回调 DTO 结构** → ⚠️ 同步检查 lims 侧引用（`InspectResultCallBackDTO` 等是 mes-feign 跨服务共享对象）

## 相关页面

- [[mes-overview]] — mes 服务总览（inspect 在中部子域；mes-feign 的 `InspectFeign` 回调契约）
- [[mes-record-module]] — 批记录组件（检验结果回填目标）
- [[mes-execute-module]] — 执行表单数据（检验结果回填目标）
- [[mes-product-module]] — 物料主数据（检验配置按物料组织）
- [[service-integration]] — mes ↔ lims 检验回调链（含网关双模式）
- [[lims-overview]] — 检验回调发起方
- [[platform-overview]] — 开关参数 `INSPECT_LIMS_CONFIG` 来源
- [[database-schema-overview]] — `bm_inspect*` 6 表归属
