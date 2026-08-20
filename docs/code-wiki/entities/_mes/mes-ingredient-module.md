---
title: MES Ingredient 模块（配料 · plan/input/weigh 三段式 + 允差计算）
created: 2026-07-06
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/ingredient/
status: active
---

# MES Ingredient 模块

## 概述 / 职责

Ingredient 模块是 mes 的**配料/投料域**：按配料计划组织物料的批次绑定、理论量计算、投料录入、称量复核（称重+签名+完成）、允差判定，以及称量日志。是 [[mes-weigh-module]] 的**下游姊妹域**——weigh 按"称量需求/任务"组织，ingredient 按"配料计划"组织，两者执行环节高度对称但业务维度不同。

- 包路径：`com.bmos.mes.service.ingredient/`
- 规模：**Controller 3 · Service 接口 4 · Mapper 8 · Java 75 · 表 8**（7 张 `bm_ingredient_*` + 1 张 `bm_weigh_log`）
- 关键依赖：plan（生产计划/工艺组件实例驱动配料计划）、formula（配方 + 允差类型，`DiffUtil` 直接依赖）、storage（mes 端物料批次视图）；详见耦合点
- 独有机制：**允差计算（DiffUtil）**、**配料计划三段式（plan → input → weigh）**

> ⚠️ 与 [[mes-weigh-module]] 的 centre2 类似，ingredient 的执行 Service（`IIngredientWeighService`）也有一整套 `weighAndPrint` / `sign` / `finish` / `makeSureWeigh` / `changeWeigher` / 扫码系列——这是 mes 内**称量能力的第二次实现**（按配料维度）。改称量相关逻辑时注意 weigh 子域与 ingredient 子域**各有独立实现**，确认业务落在哪一边。

## 三段式流程

ingredient 子域按配料的业务环节切三个子包，**端到端闭环**：

| 阶段 | 子包 | Java | 核心 Service | 关键动作 |
|---|---|---:|---|---|
| **① 计划** | `plan/` | 23 | `IngredientService` | 配料计划生成、可用物料批次查询、绑定批次、理论量/配料量计算、完成计划 |
| **② 投料** | `input/` | 14 | `IIngredientInputService` | 投料录入、按计划查投料清单、扫码识别物料、待投计划查询 |
| **③ 称量复核** | `weigh/` | 37 | `IIngredientWeighService` + `WeighLogService` | 称量执行（称重/签名/完成/换人）、扫码（物料/设备/容器/库位）、允差校验、称量日志 |

> 数据流：plan 生成 `bm_ingredient_plan` + 绑定 `bm_ingredient_plan_material_batch` → input 写 `bm_ingredient_input_record` + `bm_ingredient_input_component_instance` → weigh 写 `bm_ingredient_weigh_process` / `bm_ingredient_weigh_batch_process` / `bm_ingredient_weigh_record` + `bm_weigh_log`。

## 数据模型（8 张表）

### plan（2 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_ingredient_plan` | `IngredientPlan` | ★ 配料计划主表 |
| `bm_ingredient_plan_material_batch` | `IngredientMaterialBatch` | 计划绑定的物料批次 |

### input（2 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_ingredient_input_record` | `IngredientInputRecord` | 投料记录 |
| `bm_ingredient_input_component_instance` | `IngredientInputComponentInstance` | 投料的组件实例 |

### weigh（4 张 + 1 张共用日志）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_ingredient_weigh_process` | `IngredientWeighProcess` | 配料称量过程主表 |
| `bm_ingredient_weigh_batch_process` | `IngredientWeighBatchProcess` | 批次级称量过程 |
| `bm_ingredient_weigh_record` | `IngredientWeighRecord` | 称量记录（每次称重） |
| `bm_weigh_log` | `WeighLog` | ⚠️ **前缀异常**：表名是 `bm_weigh_log`（非 `bm_ingredient_weigh_log`）—— ingredient 称量日志共用 weigh 前缀，命名规范外 |

> ⚠️ **表名前缀异常**：`bm_weigh_log` 是 ingredient 子域唯一不以 `bm_ingredient_` 开头的表。可能是历史共享设计（weigh 子域与 ingredient 共用一张日志表）。改日志结构需同时确认两子域的读取方。

## Controller（3 个，三段各一）

| Controller | 职责 |
|---|---|
| `IngredientController`（plan） | 配料计划：查询 / 可用+已绑批次 / 绑定批次 / 查已绑批次 / 完成 / 理论量计算 / 配料量计算 |
| `IngredientInputController`（input） | 投料：按计划查清单 / `input` / 扫码识别物料 / 待投计划 / 组件实例 |
| `IngredientWeighController`（weigh） | 称量复核：扫码（物料/设备/容器/库位） / 待称计划 / 计划详情 / 确认 / 加耗材 / `weighAndPrint` / 签名 / 换人 / 完成 / 结果 / 校验签名 / 天平列表 |

## Service 核心方法

### `IngredientService`（plan · 计划与算量）

| 方法 | 功能 |
|---|---|
| `getMaterialIngredientPlanVO(dto)` | 查配料计划 |
| `getAvailableAndAddedMaterialBatch(dto)` | 可用 + 已绑定批次 |
| `ingredientBindMaterialBatch(dto)` | 绑定物料批次到计划 |
| `getBoundMaterialBatch(dto)` | 查已绑批次 |
| `completeIngredientPlan(dto)` | 完成配料计划 |
| `calculateTheoreticalQuantity(dto)` | ★ 理论量计算（返回 `IngredientQuantityCalculateVO`） |
| `calculateIngredientQuantity(dto)` | ★ 配料量计算（批量，返回 `IngredientQuantityListCalculateVO`） |

### `IIngredientInputService`（input · 投料）

| 方法 | 功能 |
|---|---|
| `queryInputListByPlanId(ingredientPlanId, componentInstanceId)` | 按计划查投料清单 |
| `input(dto)` | 投料录入 |
| `scanWeighMaterialCodeWithIngredientPlanId(...)` | 扫码识别物料（按配料计划） |
| `queryPendingInputPlanList(dto)` | 待投料计划列表 |
| `getInputComponentInstance(dto)` | 投料组件实例 |

### `IIngredientWeighService`（weigh · 称量复核，最重）

| 方法 | 功能 |
|---|---|
| `weighAndPrint(dto)` | ★ 称量并打印 |
| `makeSureWeigh(dto)` | 确认称量 |
| `addConsumeStorageMaterial(dto)` | 添加耗材 |
| `sign(dto)` / `finish(dto)` / `changeWeigher(dto)` | 签名 / 完成 / 换人 |
| `validateComponentSign(validateSignList)` | 校验组件签名 |
| `queryWeighStorageMaterial(scanQuery)` | 扫码查称量物料 |
| `scanDeviceCode(scanQuery)` / `scanWeighContainerCode(code)` / `scanWeighPositionCode(code)` | 扫码：设备 / 容器 / 库位 |
| `getBalanceListByStationIds(stationIds)` | 按工位查天平列表 |
| `queryPendingIngredientPlanList(productPlanId, batchNo)` | 待称配料计划 |
| `queryIngredientPlanById(...)` / `getIngredientWeighProcess(...)` / `queryWeighDetailByPlanIdAndBatchId(...)` / `queryResult(query)` | 计划详情 / 称量过程 / 批次详情 / 结果 |

### `WeighLogService`（称量日志）

| 方法 | 功能 |
|---|---|
| `saveLog(dto)` / `saveLogs(dtos)` | 保存日志（单条/批量） |
| `queryWeighLogPage(dto)` | 日志分页 |

## 独有机制

### 允差计算（DiffUtil）

位置：`ingredient/DiffUtil.java`（子域根级工具类，非子包）

- `diff(target, formulaMaterial)` — 按配方物料的允差上下限/类型计算**实际允差范围** `【下限, 标准, 上限】`
- 依赖 formula 子域：`ProductFormulaMaterial`（配方物料）+ `ToleranceTypeEnum`（允差类型，来自 `bmos-mes-common/.../enums/formula/`）+ `MaterialQuantityCalculateUtil`（utils 子域）
- 配方物料字段：`oddmentToleranceLower` / `oddmentToleranceUpper` / `oddmentToleranceType` / `scale` / `scaleLength`

> 这是 ingredient 与 formula 子域的**核心耦合点**。改允差规则必须同步看 formula 的 `ProductFormulaMaterial` 字段定义与 `ToleranceTypeEnum` 取值。详见 [[mes-overview]] 中部子域的 formula 行（⏳ 暂未建独立页）。

### 配料量计算

`IngredientService.calculateTheoreticalQuantity` / `calculateIngredientQuantity` 是 plan 阶段的算量入口——按配方 + 计划推算每物料的理论/配料量，与 weigh 阶段的实际称量做对比（允差判定）。

### 扫码系列（4 类）

`IIngredientWeighService` 集成了 4 类扫码：物料 / 设备 / 容器 / 库位——称量复核现场的全流程扫码闭环，与 [[mes-weigh-module]] centre 的 `IWeighInputService.scanWeighMaterialCode...` 是平行实现。

## 与其它子域 / 服务的耦合点

- **← plan**：配料计划由生产计划 + 工艺组件实例驱动（与 weigh 的 `createRequirement(productPlanId, ...)` 同源）。详见 [[mes-plan-module]]。
- **← formula**：★ **允差计算核心依赖**——`DiffUtil` 直接用 `ProductFormulaMaterial` + `ToleranceTypeEnum`。formula（中部子域，⏳ 未建独立页）。
- **↔ storage**：投料/称量绑定 mes 端库位物料批次（`StorageMaterialVO` / `addConsumeStorageMaterial`）。详见 [[mes-storage-module]]。
- **↔ weigh**：姊妹域，称量能力各自实现（见概述 ⚠️）。`bm_weigh_log` 表可能两子域共用。详见 [[mes-weigh-module]]。
- **→ 内部**：无 Feign、无 MQ、无定时任务——纯进程内业务。

## AI 定位提示

- **称量业务落哪边？** → 按"配料计划"维度走 ingredient；按"称量需求/任务/票"维度走 [[mes-weigh-module]]（centre/centre2）。两边的 `weighAndPrint/sign/finish` 不共享实现。
- 配料量/理论量算不对 → `IngredientService.calculateTheoreticalQuantity` / `calculateIngredientQuantity`
- 允差判定异常 → `DiffUtil.diff` + formula 的 `ProductFormulaMaterial` 字段（`oddmentTolerance*` / `scale*`）+ `ToleranceTypeEnum`
- 投料录入 / 扫码识别物料 → `IIngredientInputService.input` / `scanWeighMaterialCodeWithIngredientPlanId`
- 称量复核扫码（物料/设备/容器/库位） → `IIngredientWeighService.scan*`
- 称量日志查询 → `WeighLogService.queryWeighLogPage`（⚠️ 表名 `bm_weigh_log`，非 `bm_ingredient_*`）
- **改 `bm_weigh_log` 表结构** → ⚠️ 确认 weigh 子域是否也读这张表（前缀异常，疑似共用）

## 相关页面

- [[mes-overview]] — mes 服务总览（ingredient 为头部子域）
- [[mes-weigh-module]] — 称量姊妹域（按需求/任务/票组织，与 ingredient 按配料计划组织对照）
- [[mes-preparation-module]] — 制剂/前处理（制造执行姊妹域，含产出环节）
- [[mes-plan-module]] — 生产计划（配料计划的来源）
- [[mes-storage-module]] — mes 端物料批次视图
- [[database-schema-overview]] — `bm_ingredient_*` / `bm_weigh_log` 表归属
