---
title: MES Preparation 模块（制剂 · 液体量取 + 投料 + 产出，移动端作业）
created: 2026-07-06
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/preparation/
status: active
---

# MES Preparation 模块

## 概述 / 职责

Preparation 模块是 mes 的**制剂/前处理域**：液体制剂的计划与算量、液体质取（扫码/确认/称量打印/签名/换人/完成）、投料（绑计划/扫码容器物料/录入）、产出（进度/确认/签名/换人/报废）。是 [[mes-overview]] 头部子域中**唯一覆盖"产出"环节**的制造执行四段式子域。

- 包路径：`com.bmos.mes.service.preparation/`
- 规模：**Controller 4 · Service 接口 5 · Mapper 10 · Java 105 · 表 10**
- 关键依赖：plan（生产计划驱动制剂计划）、storage（mes 端物料批次视图）、product（物料主数据）、mobile（★ input/produce 两个 Controller 挂 `/mobile/` 前缀，对接 mes-app）；详见耦合点
- 独有机制：**液体量取（measure）完整闭环**、**产出（produce）含报废/换人**、**移动端作业入口**

> ⚠️ **与 weigh/ingredient 的同构关系**：preparation 与 [[mes-weigh-module]]、[[mes-ingredient-module]] 都是"计划→称量/量取→投料"的制造执行三段式，但各有侧重：
> - **weigh**：按"称量需求/任务/票"组织，纯称量，无投料无产出
> - **ingredient**：按"配料计划"组织，配料三段式（plan/input/weigh），无产出
> - **preparation**：按"液体制剂计划"组织，**多了一步"产出（produce）"**，且 input/produce 走移动端
>
> 改制造执行逻辑前先确认业务落哪个子域——三者各有独立实现，命名前缀也不同（`bm_weigh_*` / `bm_ingredient_*` / `bm_preparation_*` + `bm_liquid_preparation_*`）。

## 四段式流程（4 子包）

| 阶段 | 子包 | Java | 路由前缀 | 核心 |
|---|---:|---:|---|---|
| **① 计划** | `plan/` | 26 | `/liquid/preparation/plan` | 液体制剂计划：实例/绑定批次/可用批次/算量/完成 |
| **② 量取** | `measure/` | 38 | `/liquid/preparation/measure` | ★ 液体质取全流程（最重）：扫码/确认/加耗材/称量打印/签名/换人/完成/日志 |
| **③ 投料** | `input/` | 16 | `/mobile/preparation/input` | 投料（**移动端**）：绑计划/扫码容器/扫码物料/录入/完成 |
| **④ 产出** | `produce/` | 25 | `/mobile/preparation/produce` | 产出（**移动端**）：进度/确认/生产处理/签名/换人/报废 |

> 命名规律：**plan/measure = 液体制剂（`Liquid*` 类名 + `bm_liquid_preparation_*` 表），input/produce = 通用制剂（`Preparation*` 类名 + `bm_preparation_*` 表）**。

## 数据模型（10 张表）

### 液体制剂量取（measure + plan，6 张 · `bm_liquid_preparation_*`）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_liquid_preparation_plan` | `LiquidPreparationPlan` | ★ 液体制剂计划主表 |
| `bm_liquid_preparation_plan_material_batch` | `LiquidPreparationMaterialBatch` | 计划绑定的物料批次 |
| `bm_liquid_preparation_measure_instance` | `LiquidPreparationMeasureInstance` | 量取实例 |
| `bm_liquid_preparation_measure_batch` | `LiquidPreparationMeasureBatch` | 批次级量取 |
| `bm_liquid_preparation_measure_record` | `LiquidPreparationMeasureRecord` | 量取记录 |
| `bm_liquid_preparation_measure_log` | `LiquidPreparationMeasureLog` | 量取日志 |

### 通用制剂（input + produce，4 张 · `bm_preparation_*`）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_preparation_input_record` | `PreparationInputRecord` | 投料记录 |
| `bm_preparation_input_component_instance` | `PreparationInputComponentInstance` | 投料组件实例 |
| `bm_preparation_produce_progress` | `PreparationProduceProgress` | ★ 产出进度主表 |
| `bm_preparation_produce_record` | `PreparationProduceRecord` | 产出记录 |

> 表前缀清晰区分：`bm_liquid_preparation_*`（液体制剂量取侧）vs `bm_preparation_*`（通用投料+产出侧）。

## Controller（4 个）

### `LiquidPreparationPlanController`（`/liquid/preparation/plan`）

| 类别 | 接口 |
|---|---|
| 计划 | `GET /instance`（计划实例）/ `GET /boundMaterialBatch` / `GET /availableBoundMaterialBatch` / `POST /boundMaterialBatch`（绑定批次）/ `POST /complete`（完成）/ `POST /calculate`（算量） |

### `LiquidPreparationMeasureController`（`/liquid/preparation/measure`，最重）

| 类别 | 接口 |
|---|---|
| 查询 | `GET /instance` / `GET /plan/list` / `GET /plan/detail` / `GET /queryMeasureBatchDetail` / `GET /result` |
| 量取 | `POST /confirmMeasure`（确认量取）/ `POST /measureAndPrint`（★ 量取并打印）/ `POST /complete`（完成）/ `POST /sign`（签名）/ `PUT /changeMeasurer`（换人） |
| 耗材 | `POST /addConsumeStorageMaterial` |
| 日志 | `GET /log/page` |

### `PreparationInputMobileController`（`/mobile/preparation/input`，移动端）

| 类别 | 接口 |
|---|---|
| 投料 | `GET /instance` / `GET /queryPendingInputPlanList` / `POST /bind`（绑计划）/ `GET /queryInputList` / `POST /operate`（投料操作）/ `POST /complete` |

### `PreparationProduceController`（`/mobile/preparation/produce`，移动端）

| 类别 | 接口 |
|---|---|
| 查询 | `GET /progress`（进度）/ `GET /plan/list` / `GET /queryMaterial` / `GET /queryMaterialBatch` / `GET /queryCheckUserList` / `GET /queryProduce` |
| 产出 | `PUT /confirm`（确认）/ `PUT /handle`（★ 生产处理）/ `PUT /sign`（签名）/ `PUT /changeProducer`（换人）/ `PUT /scrap`（★ 报废） |

## Service 核心方法

### `LiquidPreparationPlanService`（计划与算量）

| 方法 | 功能 |
|---|---|
| `getPreparationPlanInstance(dto)` | 计划实例 |
| `getBoundMaterialBatch(dto)` / `getBoundAndAvailableMaterialBatch(dto)` | 已绑/已绑+可用批次 |
| `BindMaterialBatch(dto)` | 绑定批次（⚠️ 方法名首字母大写，违反 Java 命名规范，历史遗留） |
| `completePreparationPlan(dto)` | 完成计划 |
| `calculatePreparationQuantity(dto)` | ★ 制剂量计算 |
| `getUnmeasuredPreparationPlanList(productPlanId)` | 未量取计划列表 |

### `LiquidPreparationMeasureService`（量取核心，最重）

| 方法 | 功能 |
|---|---|
| `measureAndPrint(dto)` | ★ 量取并打印 |
| `confirmMeasure(dto)` | 确认量取（返回 measureBatchId） |
| `addConsumeStorageMaterial(dto)` | 加耗材 |
| `sign(dto)` / `completeMeasure(dto)` / `changeMeasurer(dto)` | 签名 / 完成 / 换人 |
| `scanLiquidMeasureMaterialPiece(dto)` | 扫码量取物料件 |
| `queryMeasureBatchDetailInfo(measureBatchId)` / `queryMeasureResult(dto)` / `queryLiquidPreparationPlanDetail(dto)` / `getMeasureInstance(dto)` | 详情/结果查询 |

### `LiquidPreparationMeasureLogService`（量取日志）

| 方法 | 功能 |
|---|---|
| `saveLog(dto)` | 保存日志 |
| `queryMeasureLogPage(dto)` | 日志分页 |

### `PreparationInputService`（投料）

| 方法 | 功能 |
|---|---|
| `bindPreparationPlan(dto)` | 绑定制剂计划（返回 ID） |
| `input(dto)` / `complete(dto)` | 投料 / 完成 |
| `scanPreparationInputContainer(dto)` / `scanPreparationInputMaterial(dto)` | 扫码容器 / 扫码物料 |
| `getInputComponentInstance(dto)` / `queryInputListByPlanId(componentInstanceId)` / `queryPendingInputPlanList(productPlanId)` | 组件实例/投料清单/待投计划 |

### `PreparationProduceService`（产出）

| 方法 | 功能 |
|---|---|
| `produceHandle(dto)` | ★ 生产处理（返回产出记录标识） |
| `produceConfirm(dto)` | 产出确认 |
| `sign(dto)` | 签名 |
| `changeProducer(dto)` | 换生产人 |
| `scrap(dto)` | ★ 报废 |
| `getPreparationProduceProgress(dto)` / `getProducePlanList(productPlanId)` / `queryMaterial(planId)` / `queryMaterialBatch(dto)` / `queryCheckUserList(dto)` / `queryProduce(progressId)` | 进度/计划/物料/批次/校验人/产出查询 |
| `scanPreparationProduceContainer(code)` / `scanPreparationCargoCode(code)` | 扫码容器 / 扫码库位 |

## 独有机制

### 液体量取闭环

measure 子包是 preparation 最重的部分（38 Java）——量取有自己的"扫码→确认→加耗材→称量打印→签名→换人→完成"完整闭环，与 [[mes-weigh-module]] 的称量闭环高度对称（都有 `measureAndPrint/weighAndPrint`、`sign`、`changeMeasurer/changeWeigher`、`complete`）。

### 产出含报废/换人

produce 是 preparation **独有**的环节（weigh/ingredient 无产出）：
- `produceHandle` 生产处理（主入口）
- `scrap` 报废（weigh/ingredient 无此概念）
- `changeProducer` 换生产人（与称量换人 `changeWeigher` 对称）

### 移动端作业入口

input/produce 两个 Controller 都挂 `/mobile/` 前缀——制剂的投料与产出主要在车间现场通过 mes-app 移动端操作，与 [[mobile-overview]] 的 mes-app 联动。plan/measure 走 Web 端。

## 与其它子域 / 服务的耦合点

- **← plan**：制剂计划由生产计划驱动。详见 [[mes-plan-module]]。
- **↔ storage**：量取耗材/投料扫码绑定 mes 端库位物料批次。详见 [[mes-storage-module]]。
- **← product**：物料主数据。详见 [[mes-product-module]]。
- **↔ mobile（跨端）**：input/produce 走 `/mobile/` 前缀，对接 mes-app。详见 [[mobile-overview]]。
- **↔ weigh / ingredient**：姊妹域，制造执行三段式的不同侧重（见概述 ⚠️）。
- **→ 内部**：无 Feign、无 MQ、无定时任务、无独立枚举类——纯进程内业务。

## AI 定位提示

- **业务落 preparation 还是 weigh/ingredient？** → 液体制剂量取/产出走 preparation；纯称量走 weigh；配料计划维度走 ingredient。三者命名前缀不同（`bm_liquid_preparation_*` / `bm_preparation_*` / `bm_weigh_*` / `bm_ingredient_*`）。
- 液体量取全流程 → `LiquidPreparationMeasureService`（`measureAndPrint` / `confirmMeasure` / `sign` / `complete` / `changeMeasurer`）
- 制剂量计算 → `LiquidPreparationPlanService.calculatePreparationQuantity`
- 投料（移动端） → `PreparationInputService`（`bindPreparationPlan` / `input` / 扫码系列）
- 产出/报废/换人 → `PreparationProduceService`（`produceHandle` / `scrap` / `changeProducer`）
- 移动端联调 → input/produce 的 `/mobile/preparation/*` 路由，对接 mes-app
- ⚠️ `BindMaterialBatch` 方法名首字母大写，违反 Java 规范（历史遗留，调用时注意）

## 相关页面

- [[mes-overview]] — mes 服务总览（preparation 为头部子域，唯一覆盖产出环节）
- [[mes-weigh-module]] / [[mes-ingredient-module]] — 制造执行姊妹域（对照三段式侧重差异）
- [[mes-plan-module]] — 生产计划（制剂计划的来源）
- [[mes-storage-module]] — mes 端物料批次视图
- [[mobile-overview]] — mes-app（input/produce 移动端入口）
- [[database-schema-overview]] — `bm_preparation_*` / `bm_liquid_preparation_*` 10 表归属
