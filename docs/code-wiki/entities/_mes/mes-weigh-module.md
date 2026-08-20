---
title: MES Weigh 模块（称量 · centre/centre2 双模式 + free + data + simulate）
created: 2026-07-06
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/weigh/
status: active
---

# MES Weigh 模块

## 概述 / 职责

Weigh 模块是 mes 的**称量作业域**：从称量需求生成、编排（手动/自动）、到称量执行（扫码、称重、签名、完成）、耗材记录、自由称量、设备配置，以及称量看板。覆盖原料/辅料在投料前的**精确称重全流程**，是 [[mes-overview]] 头部子域中体量最大的待建项。

- 包路径：`com.bmos.mes.service.weigh/`
- 规模：**Controller 13 · Service 接口 14 · Mapper 20 · Java 178 · 表 20**（前缀 `bm_weigh_*` + 1 张 `mes_scale_config` + 1 张 `bm_free_weigh_history`）
- 关键依赖：plan（生产计划/工艺组件实例驱动需求）、storage（mes 端物料批次视图）、product（物料主数据）；详见耦合点
- 独有机制：**centre / centre2 双称量模式并存**（见下方专门章节）

> ⚠️ **规模数字校正**：[[mes-overview]] 头部子域表标 Weigh Tbl=16，实扫 `@TableName` 为 **20 张**（含 `bm_weigh_execute_consume_record` / `bm_weigh_execute_weigh_record` / `bm_weigh_ticket` / `bm_weigh_ticket_requirement` / `bm_weigh_ticket_requirement_group` 5 张 `value=` 写法表，首版扫描漏计）。本页以实扫为准。

## 子包速览（5 个）

weigh 子域**按"称量模式"而非"业务环节"切包**，理解模式差异是读懂本模块的前提：

| 子包 | Java | Ctrl | Svc | 模式定位 |
|---|---:|---:|---:|---|
| **centre** | 83 | 6 | 6 | ★ **模式一：需求/任务驱动**（老模式）。requirement → task → execute → input 五段式 |
| **centre2** | 73 | 5 | 5 | ★ **模式二：称量单驱动**（新模式，类名一律 `Ticket*`）。ticket → ticketRequirement → execute |
| **free** | 9 | 1 | 1 | 自由称量（无需求/工单，直接称重并打印） |
| **data** | 8 | 1 | 1 | 称量数据查询（按组件实例聚合） |
| **simulate** | 5 | 0 | 1 | 设备/电子秤配置（`mes_scale_config`，含随机重量生成，疑似演示/调试用） |

> 命名提示：`centre`/`centre2` 指称量中心（WeighCentre，物理工位 `bm_weigh_centre*`）。两者**不是版本号**，是**两套并存的业务编排**——centre 走 `requirement/task`，centre2 走 `ticket`。改业务前先确认目标模式。

## 核心模式：centre vs centre2

两代模式各自**端到端闭环**，编排入口、数据表、状态枚举都各自一套：

| 维度 | centre（老） | centre2（新） |
|---|---|---|
| 驱动 | 称量需求 `bm_weigh_requirement` + 任务 `bm_weigh_task` | 称量单 `bm_weigh_ticket` + 单据需求 `bm_weigh_ticket_requirement[_group]` |
| 编排入口 | `IWeighTaskService.programManual/Auto` + `IWeighRequirementService.createRequirement` | `ITicketService.programManual/Auto` + `ITicketService.issue/cancel` |
| 执行 Service | `IWeighExecuteService`（`weighAndPrint` / `finish` / `sign` / `changeBatch`） | `TicketWeighExecuteService`（`executeWeighRequirement` / `saveWeighRequirementRecord` / `signWeigh` / `finishWeighRequirement`） |
| 看板 | （无独立看板） | `IWeighDashboardService`（今日单/趋势/完成率） |
| 状态枚举 | （散落在流程字段，无独立 enum 类） | `DashboardWeighStatusEnum`（已下发/称量中/已完成） |

> ⚠️ **新需求落点建议**：两套模式并存通常意味着 centre2 是 centre 的重构替代但未做迁移。新需求若无明确指定，**默认确认是否走 centre2 的 ticket 模式**；centre 的老接口大概率是存量维护。

## 数据模型（20 张表）

### centre（模式一，10 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_weigh_requirement` | `WeighRequirement` | ★ 称量需求主表（物料/数量/来源计划） |
| `bm_weigh_requirement_quality` | `WeighRequirementQualityDO` | 需求的质量属性快照 |
| `bm_weigh_requirement_record` | `WeighRequirementRecordDO` | 需求的称量记录 |
| `bm_weigh_task` | `WeighTask` | ★ 称量任务（多个需求编排成一个任务） |
| `bm_weigh_execute_weigh_record` | `WeighExecuteWeighRecord` | 称量执行记录（每次称重） |
| `bm_weigh_execute_consume_record` | `WeighExecuteConsumeRecord` | 称量耗材记录 |
| `bm_weigh_input_process` | `WeighInputProcess` | 投料过程 |
| `bm_weigh_input_record` | `WeighInputRecord` | 投料记录 |
| `bm_weigh_storage_material_requirement_record` | `WeighStorageMaterialRequirementDO` | 库位物料-需求关联 |
| `bm_weigh_centre` / `bm_weigh_centre_category` / `bm_weigh_centre_station` | `WeighCentre` / `WeighCentreCategory` / `WeighCentreStation` | 称量中心主数据（中心/分类/工位） |

### centre2（模式二，6 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_weigh_ticket` | `TicketDO` | ★ 称量单主表 |
| `bm_weigh_ticket_requirement` | `TicketRequirementDO` | 单据关联的称量需求 |
| `bm_weigh_ticket_requirement_group` | `TicketRequirementGroupDO` | 需求分组（一组需求一起编排） |
| `bm_weigh_ticket_quality` | `WeighTicketQualityDO` | 单据的质量属性 |
| `bm_weigh_ticket_user` | `WeighTicketUserDO` | 单据的称量人员 |

### free / data / simulate（3 张 + 共用 1 张）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_free_weigh_history` | `FreeWeighHistoryDO` | 自由称量历史 |
| `bm_weigh_data` | `WeighDataDO` | 称量数据（按 componentInstanceId 聚合，跨模式查询层） |
| `mes_scale_config` | `ScaleConfig` | ⚠️ **无 `bm_` 前缀**（命名规范外，整合前遗留）—— 电子秤/设备配置 |

> ⚠️ **表名规范外**：`mes_scale_config` 是 weigh 子域**唯一不带 `bm_` 前缀**的表（与 [[mes-overview]] 提到的 `product_schedule_procedure_config` 同类历史遗留）。改表结构或迁移时注意。

## 关键枚举

### `DashboardWeighStatusEnum`（centre2 看板 · 称量单状态）

位置：`centre2/dashboard/enums/`

| 枚举值 | code | 中文 |
|---|---|---|
| `SEND` | 1 | 已下发 |
| `WEIGHING` | 2 | 称量中 |
| `WEIGHED` | 3 | 已完成 |

> 仅 centre2 的看板使用此枚举。centre（老模式）的需/任务状态散落在各表字段，无独立 enum 类——排查时直接看 `bm_weigh_task.status` 等字段值。

## Controller（13 个）

### centre（6 个，模式一）

| Controller | 职责 |
|---|---|
| `WeighRequirementController` | 需求 CRUD / 分页 / 自动编排候选 |
| `WeighTaskController` | 任务编排（`programManual`/`programAuto`）/ 下发 / 取消 / 确认 / 执行查询 |
| `WeighExecuteController` | 称量执行（`weighAndPrint`/`sign`/`finish`/`changeBatch`/`changeWeigher`） |
| `WeighInputController` | 投料（扫码 / `input` / `finishInput`） |
| `WeighCentreController` | 称量中心主数据 |
| `WeighCentreCategoryController` | 中心分类主数据 |

### centre2（5 个，模式二 · Ticket）

| Controller | 职责 |
|---|---|
| `TicketWeighTicketController` | 称量单 CRUD / 编排（`programManual`/`programAuto`）/ 下发（`issue`）/ 取消 |
| `TicketWeighRequirementController` | 单据需求查询（`queryMaterialList`/`list`） |
| `TicketWeighRequirementGroupController` | 需求分组（创建 / 确认 / 取消 / 公式计算 `calcFormulaQuantity`） |
| `TicketWeighExecuteController` | 单据执行（`executeWeighRequirement`/`saveWeighRequirementRecord`/`saveOddmentWeighRecord`/`signWeigh`/`finishWeighRequirement`） |
| `WeighDashboardController` | 看板（今日单 / 趋势 / 完成率 / 概览） |

### free / data（2 个）

| Controller | 职责 |
|---|---|
| `FreeWeighController` | 自由称量（`weighAndPrint` / 历史查询 / 天平列表） |
| `WeighDataController` | 称量数据查询（按组件实例） |

## Service 核心方法

### centre2 / `ITicketService`（模式二编排核心）

| 方法 | 功能 |
|---|---|
| `programAuto()` | ★ 自动编排（按规则把需求组成称量单） |
| `programManual(requirementIds)` | ★ 手动编排（指定需求组单） |
| `issue(id)` / `cancel(id)` | 下发 / 取消单据 |
| `edit(TicketEditDTO)` | 编辑单据 |
| `page(TicketPageQuery)` | 单据分页 |
| `getTicketInfo(ticketId)` / `getWeighRecord(ticketId)` | 单据详情 / 称量记录 |

### centre2 / `TicketWeighExecuteService`（模式二执行核心）

| 方法 | 功能 |
|---|---|
| `executeWeighRequirement(dto)` | 执行称量需求（绑定库位物料） |
| `bindOperator(dto)` | 绑定操作员 |
| `saveWeighRequirementRecord(dto)` | 保存称量记录（返回是否足量 `TicketRequirementEnoughVO`） |
| `saveOddmentWeighRecord(dto)` | 保存余料称量记录 |
| `signWeigh(dto)` | 签名确认 |
| `finishWeighRequirement(dto)` | 完成称量需求 |
| `bindMaterialToRequirement(...)` | 需求绑定库位物料 |
| `pageWeighTicket(dto, history)` / `getWeighTicketDetail(ticketId)` | 单据分页/详情 |
| `getOddmentInfoByTicketId` / `getWeighRecordsByTicketId` | 余料信息 / 称量记录 |

### centre2 / `ITicketRequirementGroupService`（需求分组）

| 方法 | 功能 |
|---|---|
| `createRequirementGroup` / `editRequirementGroup` | 创建/编辑分组 |
| `makeSureRequirementGroup(id)` / `cancelRequirement(id)` | 确认 / 取消 |
| `calcFormulaQuantity(dto)` | ★ 公式计算数量（称量公式） |
| `saveRequirement(dto)` / `validateSaveRequirement(dto)` | 保存/校验 |

### centre2 / `IWeighDashboardService`（看板）

| 方法 | 功能 |
|---|---|
| `getTicketOverview(recentDays)` | 单据概览 |
| `getTodayTicket` / `getProductionCompletion` / `getTicketCompletion` | 今日单 / 生产完成 / 单据完成率 |
| `getTicketTrend` / `getRequirementTrend` | 单据/需求趋势 |

### centre / `IWeighTaskService`（模式一编排核心）

| 方法 | 功能 |
|---|---|
| `programManual(requirementIds)` / `programAuto()` | ★ 手动/自动编排任务 |
| `send(taskId)` / `cancel(taskId)` / `makeSure(taskId)` | 下发 / 取消 / 确认 |
| `edit(WeighTaskEditDTO)` | 编辑任务 |
| `queryPage` / `queryRequirementListByTaskId` / `queryUnPlanedRequirementListByTaskId` | 分页/按任务查需求/未编排需求 |
| `queryExecuteTaskPage` / `queryHistoryTaskPage` | 执行中 / 历史 |

### centre / `IWeighRequirementService`（模式一需求）

| 方法 | 功能 |
|---|---|
| `createRequirement(productPlanId, componentInstances)` | ★ 由生产计划+组件实例生成需求 |
| `listAutoProgramRequirements()` | 自动编排候选 |
| `releaseRequirement(requirements)` | 释放需求 |
| `queryPage` / `queryListByTaskId` / `selectByIds` / `updateBatch` | 查询/批更新 |

### centre / `IWeighExecuteService`（模式一执行）

| 方法 | 功能 |
|---|---|
| `weighAndPrint(dto)` | ★ 称量并打印 |
| `makeSureWeigh(dto)` | 确认称量 |
| `addConsumeStorageMaterial(dto)` | 添加耗材 |
| `changeBatch(dto)` / `changeWeigher(dto, validSignStatus)` | 换批 / 换人称量 |
| `sign(dto)` / `finish(dto)` | 签名 / 完成 |
| `refreshTaskStatus(taskIds)` | 刷新任务状态 |

### centre / `IWeighInputService`（投料）

| 方法 | 功能 |
|---|---|
| `input(dto)` / `finishInput(componentInstanceId)` | 投料 / 完成投料 |
| `getInputList(componentInstanceId)` | 投料清单 |
| `scanWeighMaterialCodeWithMaterialWeighComponentId(...)` | 扫码识别物料 |

### free / data / simulate

- **`IFreeWeighService`**：`weighAndPrint(dto)`（自由称量并打印）/ `getBalanceList()`（天平列表）/ `queryHistoryPage`
- **`IWeighDataService`**：`saveData(dto)` / `getWeighList(componentInstanceId)`（按组件实例聚合称量数据，是跨模式的查询层）
- **`ScaleConfigService`**：`getEnabledConfig()` / `generateRandomWeight()` —— ⚠️ 后者生成随机重量，疑似演示/调试用途，生产环境需确认是否启用。

## 独有机制

### 双称量模式并存

见前述"核心模式"章节。两套模式**数据表不共享**（`bm_weigh_requirement` vs `bm_weigh_ticket_requirement`），**编排入口各自独立**。新代码务必确认落点。

### 公式计算数量

centre2 的 `ITicketRequirementGroupService.calcFormulaQuantity` 在分组保存时按公式计算称量数量——与 record/process 的公式体系不同，是 weigh 自有的"按组件公式推算投料量"逻辑。

### 自由称量 + 打印

`free/IFreeWeighService.weighAndPrint` 与 centre 的 `IWeighExecuteService.weighAndPrint` 都带"称完即打印"语义，对接前端打印模板（具体模板在 web/前端）。

## 与其它子域 / 服务的耦合点

- **← plan**：称量需求由生产计划 + 工艺组件实例驱动（`createRequirement(productPlanId, componentInstances)`）。详见 [[mes-plan-module]]。
- **↔ storage**：称量执行绑定 mes 端库位物料（`bindMaterialToRequirement` / `addConsumeStorageMaterial` / `scanWeighMaterialCode...`）。storage 提供 mes 端物料批次视图，与 wms 区分。详见 [[mes-storage-module]]。
- **← product**：物料主数据（物料编码、扩展字段）。详见 [[mes-product-module]]。
- **← ingredient**：weigh 产出的称量记录是配料/投料（ingredient）的输入。两子域前后衔接。
- **→ 内部**：无 Feign、无 MQ、无定时任务——纯进程内业务，与其它服务无直接调用。

## AI 定位提示

- **新需求是 centre 还是 centre2？** → 先看 Controller 路径：`/weigh/task*`（老）vs `/ticket*`（新，称量单）；不确定就找产品/前端确认模式
- 单据/任务编排异常 → centre2 `ITicketService.programAuto/Manual` / centre `IWeighTaskService.programAuto/Manual`
- 称量执行/签名/完成 → centre2 `TicketWeighExecuteService` / centre `IWeighExecuteService`（注意 `weighAndPrint` 两边都有）
- 公式算量不对 → centre2 `ITicketRequirementGroupService.calcFormulaQuantity`
- 看板数据缺失 → `IWeighDashboardService`（仅 centre2）
- 自由称量 → `IFreeWeighService.weighAndPrint`
- 投料/扫码识别 → centre `IWeighInputService.scanWeighMaterialCode...`
- 电子秤/设备配置 → `ScaleConfigService` + `mes_scale_config`（⚠️ 无 `bm_` 前缀）
- **改表结构** → ⚠️ 注意两套模式表分离，且 `mes_scale_config` 命名规范外

## 相关页面

- [[mes-overview]] — mes 服务总览（weigh 为头部子域；本页已校正表数为 20）
- [[mes-plan-module]] — 生产计划（称量需求的来源）
- [[mes-storage-module]] — mes 端物料批次视图（称量绑定的库位物料）
- [[mes-product-module]] — 物料主数据
- [[mes-ingredient-module]] — 配料/投料（weigh 的下游姊妹域，按配料计划组织）
- [[mes-preparation-module]] — 制剂/前处理（制造执行姊妹域，含产出环节）
- [[database-schema-overview]] — `bm_weigh_*` / `bm_free_weigh_*` / `mes_scale_config` 表归属
