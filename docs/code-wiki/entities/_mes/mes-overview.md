---
title: MES 服务总览
created: 2026-06-29
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/BmosMesApplication.java
  - packages/backend/services/mes/bmos-mes-feign/
  - packages/backend/services/mes/bmos-mes-common/
  - packages/backend/services/mes/bmos-mes-service/pom.xml
status: active
---

# MES 服务总览

## 概述 / 职责

**MES（Manufacturing Execution System，制造执行系统）** 是 bmos 平台的**业务核心服务**，承担从生产计划、领料、配料、称量、工序执行、批记录到检验、追溯的全流程。

- 端口：**60200** ｜ Nacos 注册名：`bmos-mes-service` ｜ context-path：`/api/app/mes`
- 启动类：`com.bmos.mes.service.BmosMesApplication`
- 规模：**101 Controller / 170 Mapper / ~169 张表（前缀 `bm_`，约占全平台 50%）**
- 复杂度：38 个业务子域，是 bmos 5 个服务中**代码量与业务复杂度最高**的服务
- 被依赖：lims、wms 反向调 mes 接口（检验回调 / 反向集成）

## Maven 模块结构

```
mes/
├── bmos-mes-common/    # 常量、枚举、公共 DTO（包结构非常简洁，只一层 common）
├── bmos-mes-feign/     # ★对外契约：3 个 Feign + 配套 DTO/VO
└── bmos-mes-service/   # 业务实现 + Controller + Mapper + 启动类
```

> 与 platform 的 `-facade` 模块对应，mes 用 `-feign` 命名。两者职责相同——对外暴露 Feign 接口。

## 关键 starter 与能力

`bmos-mes-service/pom.xml` 中引入的 bmos 平台 starter（非通用 Spring）：

| starter | 提供能力 |
|---|---|
| `bmos-cloud-dependency` | BOM，统一依赖版本 |
| `bmos-platform-facade` | 调 platform 的 Feign 契约（详见 [[service-integration]]） |
| `bmos-api-feign` | Feign 客户端基础设施 |
| `bmos-audit-engine-starter` | **审计/追溯引擎**（批记录、操作日志依赖此） |
| `bmos-orchestrator-starter` | **流程编排**（工序流转、工作流依赖此） |
| `bmos-scheduler-core` | 调度核心（定时任务、轮询） |

> 这三个 starter（audit / orchestrator / scheduler）是 mes 业务复杂度的支撑组件。理解任何"流程相关"的逻辑前先看这些 starter 的注解和注入点。

## 业务子域全景（39 个）

按 **Java 文件数**分层（包含 Controller / Service / Mapper / Entity / DTO/VO 等全部代码，比单看 Controller 数更能反映模块体量）。**头部子域 = 已建或值得建独立模块页**。

### 头部子域（Java ≥ 50，14 个，核心业务/独立模块）

| 子域 | Ctrl | Svc | Tbl | Java | 包路径 | 业务定位 |
|---|---:|---:|---:|---:|---|---|
| **process** 工艺/任务编排 | 6 | 24 | 20 | 259 | `service/process/` | 业务编排中枢（Process→Procedure→Step→Task）— 详见 [[mes-process-module]] |
| **plan** 计划/排程/归档 | 13 | 16 | 14 | 258 | `service/plan/` | 业务入口层，7 子包含批记录归档 — 详见 [[mes-plan-module]] |
| **record** 批记录（设计） | 4 | 8 | 2 | 184 | `service/record/` | 批记录文档结构 + 57 个组件策略 — 详见 [[mes-record-module]] |
| **weigh** 称量 | 13 | 14 | 20 | 178 | `service/weigh/` | 称重作业全流程，**centre/centre2 双模式并存**（需求驱动 vs 称量单驱动）。表 `bm_weigh_*`（19）+ `mes_scale_config`（⚠️ 无 `bm_` 前缀）+ `bm_free_weigh_history` — 详见 [[mes-weigh-module]] |
| **storage** mes 端储位/库存 | 8 | 8 | 4 | 107 | `service/storage/` | mes 端的物料/批次视图（与 wms 区分）— 详见 [[mes-storage-module]] |
| **preparation** 制剂/前处理 | 4 | 5 | 10 | 105 | `service/preparation/` | 液体量取 + 投料 + 产出（移动端），唯一覆盖产出环节的制造执行四段式 — 详见 [[mes-preparation-module]] |
| **dataset** 数据集 | 2 | 2 | 4 | 91 | `service/dataset/` | 数据集与采集点模板（+批记录文档渲染）— 详见 [[mes-dataset-module]] |
| **audit** 审计 | 1 | 5 | 6 | 90 | `service/audit/` | 业务审计追溯（独立审计模块，与 `bmos-audit-engine-starter` 协同）— 详见 [[mes-audit-module]] |
| **execute** 执行 | 2 | 5 | 4 | 76 | `service/execute/` | 执行表单数据（`bm_execute_form_data*` 主写方，lims 只读复用）— 详见 [[mes-execute-module]] |
| **ingredient** 配料/投料 | 3 | 4 | 8 | 75 | `service/ingredient/` | 配料三段式（plan/input/weigh）+ 允差计算（DiffUtil），与 weigh 是姊妹域 — 详见 [[mes-ingredient-module]] |
| **workflow** 工作流 | 1 | 3 | 1 | 65 | `service/workflow/` | 工作流引擎封装（与 `bmos-orchestrator-starter` 协同）— 详见 [[mes-workflow-module]] |
| **product** 产品/物料主数据 | 4 | 4 | 3 | 63 | `service/product/` | 物料、分类、扩展字段 — 详见 [[mes-product-module]] |
| **lotrelease** 批放行 | 3 | 3 | 7 | 61 | `service/lotrelease/` | 制药质量门禁 — 详见 [[mes-lotrelease-module]] |
| **requisition** 领料 | 1 | 1 | 7 | 60 | `service/requisition/` | 领料三段式（预约/收货/发料），mes 唯一外向调 wms（`WmsFeignClient` 4 方法，借道 mcp DTO） — 详见 [[mes-requisition-module]] |

### 中部子域（Java 30~50，10 个）

业务量适中，按需扩展独立页或并入 overview：

| 子域 | Ctrl | Svc | Tbl | Java | 备注 |
|---|---:|---:|---:|---:|---|
| inspect 检验 | 4 | 3 | 6 | 53 | 与 lims/wms 三方联动，**LIMS 网关双模式**（自研/第三方，平台参数动态切换） — 详见 [[mes-inspect-module]] |
| platform 平台适配 | 1 | 1 | 0 | 41 | 调 platform 的本地适配层，归 [[service-integration]] |
| tag 标签 | 2 | 1 | 0 | 36 | 业务标签 |
| operate 操作记录 | 3 | 3 | 1 | 34 | 操作日志 |
| formula 公式 | 1 | 1 | 3 | 34 | 公式引擎数据层（与 record 公式协同） |
| output 产出 | 2 | 2 | 4 | 33 | 产出汇总 |
| trace 追溯 | 2 | 3 | 3 | 32 | 追溯查询（与 plan/PlanRetraceService 不同维度） |
| equipment 设备 | 2 | 5 | 4 | 30 | mes 端设备视图（主数据在 platform） |
| facotry 工厂 ⚠️ | 2 | 1 | 0 | 25 | ⚠️ 包名错别字（应为 factory），重构需作为独立任务 |
| mcp | 2 | 1 | 0 | 22 | mcp 集成 |

### 尾部子域（Java < 30，15 个）

支撑性、轻量集成、合规辅助，**不单独建页**，归并到 overview 或对应概念页：

`lotsummary`(22) · `components`(20) · `exception`(17) · `log`(15) · `utils`(15) · `signature`(14) · `operation`(13) · `unit`(12) · `tareweigh`(10) · `query`(8) · `permission`(7) · `active`(6) · `station`(3) · `serial`(1) · `schedules`(1)

其中归并去向：
- `signature` `operate` `log` `trace` — 制药合规相关（audit 已升头部），未来可归 `concepts/audit-and-compliance.md`
- `lotsummary` `tareweigh` — weigh 的延伸，作为 weigh 子页的"相关"章节
- `active` — license 校验，归未来的 `concepts/auth-and-license.md`
- `config` `utils` `schedules` `exception` `serial` — 纯支撑包，不建业务页

### 子页建设清单（按本节修订）

```
✅ 已建头部（14，全部）：product / record / process / plan / storage / workflow / execute / dataset / lotrelease / audit / weigh / ingredient / requisition / preparation
✅ 已建中部（1）：inspect（跨服务联动密集，破例独立成页）
⏳ 待建头部（0）—— 头部子域全部建完 ✅
```

> ⚠️ **包名错别字**：`service/facotry/`（应为 `factory`）—— 现状如此，重构需统一改名，注意涉及 import 路径。

> 📋 **统计口径**：Java 文件数 > 50 且至少 2 Service 或 2 表 → 值得建子页（此口径比单看 Controller 数更准确，已修正首版 overview 中按 Controller 数分档的偏差）。

## 对外 Feign 契约（mes-feign 模块）

仅暴露 3 个 Feign（其它服务调 mes 的入口）：

| Feign | 路径 | 用途 |
|---|---|---|
| `InspectFeign` | `inspect/controller/InspectFeignController`（`/feign/inspect`） | 检验回调（lims 调，含网关双模式） — 详见 [[mes-inspect-module]] |
| `MaterialFeign` | `mes/material/feign/` | 物料数据 |
| `MaterialBatchFeign` | `mes/storage/material/feign/` | 物料批次数据 |

> 配套 4 个跨服务共享 DTO：`InspectProgramResultDTO` `InspectRejectDTO` `InspectResultCallBackDTO` `InspectResultItemDTO`（全部围绕**检验流程**这一跨服务高频场景）。

## 调用关系

- **mes → platform**（10 个客户端）：用户、字典、参数、单号规则、物料、单位、标签、表达式等。详见 [[service-integration]]。
- **mes → wms**（1 个客户端，4 方法）：`service/requisition/feign/WmsFeignClient` —— 领料/库存查询/发料；⚠️ `queryInventoryData` 借道 mcp 子域 DTO/VO（wms 库存契约在 mcp）。详见 [[mes-requisition-module]]。
- **被 lims 调**：lims 检验完成后通过 `MesInspectCallbackClient` 回调 mes 的检验接口。
- **被 wms 调**：wms 反向集成 mes（具体场景待 wms overview 补充）。
- **被 platform 调**：platform 通过 `MesFeignClient` / `MesMessageFeignClient` 反向取数/推消息。

## 数据库表概要

170 个 Mapper / ~169 张表，全部前缀 `bm_`，约占 bmos 平台表总量的一半。按业务域分组速查见 [[database-schema-overview]] 的 mes 章节。

跨服务复用提醒：
- `bm_execute_form_data*` 系列在 **lims 中也被引用**，是 mes→lims 的数据流接口表，**写入归属在 mes**，lims 只读。
- `bm_batch_record_version` 在 lims 中复用，同上原则。

## AI 处理任务时的入口约定

- **找业务代码** → 先用本页"业务子域全景"定位子域 → 进入 `service/<子域>/` 包
- **找检验相关** → mes/lims/wms 三方联动，先看 [[service-integration]] 的检验回调链
- **找批记录/工序流转** → 多半涉及 `bmos-orchestrator-starter` 注解，搜对应注解先找编排定义
- **找审计/追溯** → 涉及 `bmos-audit-engine-starter`，操作日志大多自动生成
- **改包名错别字 `facotry`** → 跨包 import 影响面较大，需作为独立任务处理

## 相关页面

- [[service-overview]] — 5 服务速查
- [[database-schema-overview]] — `bm_*` 表分组（mes 章节）
- [[service-integration]] — Feign 调用矩阵（mes 是横向调用最活跃的服务）
- [[platform-overview]] — mes 的最重依赖底座
- [[lims-overview]] / [[wms-overview]] — mes 的横向集成对象（待建）
