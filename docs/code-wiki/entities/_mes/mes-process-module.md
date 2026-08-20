---
title: MES Process 模块（工艺/工序/任务编排）
created: 2026-06-29
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/process/
  - packages/backend/services/mes/bmos-mes-common/src/main/java/com/bmos/mes/common/enums/process/
status: active
---

# MES Process 模块

## 概述 / 职责

Process 模块是 mes 的**业务编排中枢**，定义和执行三层结构：

```
Process（工艺主流程）
  └─ Procedure（工序）
      └─ ProcedureStep（工步）
          └─ ProcedureTask（任务）+ ProcedureCondition（条件）
```

提供工艺建模、版本审批、运行时任务条件计算、工艺-批记录关联、工艺看板等能力。是 [[mes-record-module]] 批记录的**配置上游**，与 plan / weigh / ingredient / equipment 等子域通过条件事件耦合。

- 包路径：`com.bmos.mes.service.process/`
- 规模：**6 Controller / ~22 Mapper / 28 张表 / 19 Service**
- 关键依赖：`bmos-orchestrator-starter`（流程审批）

## 数据模型（28 张表）

按业务层次分组：

### Process（工艺主流程，5 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_process` | `Process` | 工艺主表 |
| `bm_process_version` | `ProcessVersion` | 工艺版本（含审批状态） |
| `bm_process_relation` | `ProcessRelation` | 工艺关联关系 |
| `bm_process_relation_material` | `ProcessRelationMaterial` | 关联物料 |
| `bm_process_production_line` | `ProcessProductionLine` | 工艺-产线绑定 |

### Procedure（工序，6 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_procedure` | `Procedure` | 工序主表 |
| `bm_procedure_model` | `ProcedureModel` | 工序模型 |
| `bm_procedure_model_group` | `ProcedureModelGroup` | 模型分组 |
| `bm_procedure_model_material` | `ProcedureModelMaterial` | 模型物料 |
| `bm_procedure_model_room` | `ProcedureModelRoom` | 模型房间 |
| `bm_procedure_confirm` / `bm_process_confirm` | `ProcedureConfirm` / `ProcessConfirm` | 工序/工艺确认记录 |

### ProcedureStep（工步，5 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_procedure_step` | `ProcedureStep` | 工步主表 |
| `bm_procedure_step_config` | `ProcedureStepConfig` | 工步配置 |
| `bm_procedure_step_model` | `ProcedureStepModel` | 工步模型 |
| `bm_procedure_step_role` | `ProcedureStepRole` | 工步角色绑定 |
| `bm_procedure_step_sop` | `ProcedureStepSop` | 工步 SOP 文档 |

### Task & Condition（任务与条件编排，7 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_procedure_task` | `ProcedureTask` | 任务定义 |
| `bm_procedure_task_instance` | `ProcedureTaskInstance` | 任务实例（运行时） |
| `bm_procedure_task_instance_history` | `ProcedureTaskInstanceHistory` | 任务实例历史 |
| `bm_procedure_condition` | `ProcedureCondition` | 条件定义 |
| `bm_procedure_condition_instance` | `ProcedureConditionInstance` | 条件实例（运行时） |
| `bm_procedure_condition_instance_history` | — | 条件实例历史 |
| `bm_procedure_expression` | `ProcedureExpression` | 表达式 |

### 配置/关联（5 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_process_batch_record` | `ProcessBatchRecordRelation` | **工艺→批记录绑定**（与 [[mes-record-module]] 桥接） |
| `bm_process_record_order` | `ProcessRecordOrder` | 工艺执行批次 |
| `bm_process_dashboard_config` | `ProcessDashboardConfig` | 工艺看板配置（`autoResultMap`） |
| `bm_process_dashboard_config_data` | `ProcessDashboardConfigData` | 看板数据 |
| `product_schedule_procedure_config` | `ProductScheduleProcedureConfig` | 产品排程-工序配置（⚠️ 未带 `bm_` 前缀，是整合前遗留） |

## 关键枚举（状态机的关键）

### ProcessStateEnum（工艺状态）

| 枚举 | code | value |
|---|---|---|
| `INACTIVE` | 未激活 | 0 |
| `ACTIVE` | 进行中 | 1 |
| `IS_ACTIVE` | 已激活 | 2 |
| `IS_END` | 已结束 | 3 |
| `COMPLETE` | 已完成 | 4 |

### ActionStateEnum（版本审批/生效状态）

`EDIT`(编辑) → `APPROVAL`(审批) → `CONFIRM`(确认) → `WAIT_VALID`(待生效) → `VALID`(生效) → `INVALID`(失效)；`FRESH_EDIT`(重新编辑) 用于退回。

> 这是版本流转的核心状态机，所有 `*VersionAuditCallback` 回调都在改这个字段。

### AuditPerorationStateEnum（审计结论）

`ELIGIBLE`(合格) / `NOT_ELIGIBLE`(不合格) / `RESTS`(其他)

### ConditionTypeEnum（条件类型，定义 process 与其它子域的耦合点）

| 枚举 | value | 含义 |
|---|---|---|
| `STEP_NODE_COMPLETE` | step_node_complete | 步骤节点完成 |
| `TASK_NODE_COMPLETE` | task_node_complete | 任务节点完成 |
| `EQUIPMENT_USE_STATE` | equipment_use_state | 设备使用状态 |
| `ROOM_STATE` | room_state | 房间状态 |
| `MATERIAL_RESERVE_NUMBER` | material_reserve_number | 物料预定量 |
| `DOSING_SIGNATURE` | dosing_signature | 配料称量签名 |
| `OUTPUT_SIGNATURE` | output_signature | 中间品产出签名 |

### 其它任务相关

- `ExpressionTypeEnum` — `EXECUTE_CONDITION`(执行条件) / `COMPLETE_CONDITION`(完成条件)
- `NodeTypeEnum` — `PROCEDURE`(工序节点) / `STEP_OR_TASK`(工步节点)
- `MaterialCheckEnum` — 物料比较（`>` `==` `<` `>=` `<=`，含 compareTo 结果集）
- `ProcedureStepNodeFunctionEnum` / `StepTaskTypeEnum`

## Controller（6 个）

| Controller | 角色 |
|---|---|
| `ProcessController` | 工艺主入口 |
| `ProcedureController` | 工序入口 |
| `ProcedureStepController` | 工步入口 |
| `ProcessConfirmController` / `ProcedureConfirmController` | 工艺/工序确认 |
| `ProcedureExpressionController` | 表达式管理 |

## Service 体系（19 个，分三层）

### 工艺层（Process）

- **`ProcessService`** — 主服务，方法分 5 类：
  - 查询：`getPage` / `getList` / `getDetail` / `getVersionList` / `getListTree` / `getProductProcessTree`
  - 编辑：`save` / `modifyProcess` / `saveProcessVersion` / `copyProcessVersion`
  - 状态：`changeProcessVersionState` / `auditVersion` / `updateProcessVersionActionState`
  - **审批回调**（与 orchestrator-starter 配合）：`auditProcessSuccessCallBack` / `auditProcessRejectCallBack` / `auditExecutionSuccessCallBack` / `auditExecutionRejectCallBack`
  - 关联：`getProcessRelation` / `saveProcessRelation` / `getRecursionRelationProcessList` / `saveDashboardConfig`
- `ProcessVersionService` — 版本 CRUD + 状态切换（`updateState` / `validateEditState` / `validateVersionAudit` / `getByProcessInstanceId`）
- `ProcessConfirmService` / `ProcessRelationService` / `ProcessRelationMaterialService` / `ProcessRecordOrderService`
- `ProcessBatchRecordRelationService` — **批记录绑定**（process ↔ record 的桥）
- `ProcessFormulaRelationService` — 公式关联

### 工序/工步层（Procedure / ProcedureStep）

- `ProcedureService` — `saveBatch` / `getHistoricList` / `validateProcedureName` / `saveProductScheduleProcedureConfig`
- `ProcedureModelService` / `ProcedureModelGroupService` / `ProcedureModelMaterialService`
- `ProcedureStepService` / `ProcedureStepConfigService` / `ProcedureStepModelService` / `ProcedureStepRoleRelationService` / `ProcedureStepSopService`
- `ProcedureConfirmService`

### 任务编排层（Task / Condition，运行时核心）

- `ProcedureTaskInstanceService` / `ProcedureTaskInstanceHistoryService` — 任务实例与历史
- `ProcedureConditionService` / `ProcedureConditionInstanceService` / `ProcedureConditionInstanceHistoryService` — 条件、条件实例与历史
- `ProcedureExpressionService` — 表达式

### Repository

- `ProcessRepository` / `ProcessRepositoryImpl` — process 的 Repository 抽象层（mes 内少见，仅 process 使用）

## 复制能力（version 复制）

`service/impl/copy/` 包含一组**版本复制策略**：

- `CopyContext` — 复制上下文（携带 ID 映射、状态等）
- `CopyProcessVersion` — 工艺版本复制入口
- `CopyProcedure` — 工序复制
- `CopyProcedureStep` — 工步复制

> 复制是工艺建模的高频操作，集中在此包；改复制逻辑只动这里。

## 条件事件机制（最具特色的设计）

### 核心抽象

入口：`service/condition/`

- **接口 `ITaskConditionCalculator`** — 条件结果处理器，方法 `refreshConditionResult(ConditionChangeType changeType)`
- **基类 `ConditionChangeType`**（抽象） — **所有条件变化事件的基类**，子类需实现 `getConditionType()` 与 `innerCalculateConditionChange()`
- 实现 `TaskConditionChangeCalculatorImpl` — 计算入口
- 上下文 `ConditionCalculateContext` — 计算共享上下文
- `ConditionChangeCalculator` — 计算接口

### 9 个事件子类（与其它子域的耦合点）

定义在 `service/condition/event/`，全部继承 `ConditionChangeType`：

| 事件类 | 触发来源 | 对应 ConditionTypeEnum |
|---|---|---|
| `StepCompleteCompleteType` | 工步完成 | `STEP_NODE_COMPLETE` |
| `TaskCompleteType` | 任务完成 | `TASK_NODE_COMPLETE` |
| `TaskInitType` | 任务初始化 | — |
| `EquipmentStatusType` | 设备状态变化（持有 `equipmentId` `equipmentState`） | `EQUIPMENT_USE_STATE` |
| `RoomStatusType` | 房间状态变化 | `ROOM_STATE` |
| `MaterialReserveType` | 物料预留变化 | `MATERIAL_RESERVE_NUMBER` |
| `WeighingIngredientSignType` | 配料称量签名 | `DOSING_SIGNATURE` |
| `OutputWeighSignType` | 产出称量签名 | `OUTPUT_SIGNATURE` |
| `ProcedureRestartType` | 工序重启 | — |

> **机制要点**：其它子域（weigh / ingredient / equipment / station）发生业务动作时，构造对应事件实例并通过 `refreshConditionResult` 触发条件重算。新增"触发 process 任务的事件类型" = 新增一个 `ConditionChangeType` 子类。

### ConditionChangeCalculator 接口

定义在 `ConditionChangeType` 实现：`calculateConditionChange(List<ProcedureConditionInstance>, ConditionCalculateContext)`。基类已实现公共过滤+日志，子类实现 `innerCalculateConditionChange`。

## 工艺-批记录关联

`ProcessBatchRecordRelationService` + `bm_process_batch_record` 是 process 与 [[mes-record-module]] 的桥接：
- 一个工艺版本可绑定多张批记录
- 与 record 模块 `ProductMaterialService.bindBatchRecords`、`BatchRecordService.bindExpression` 配合，构成"产品 → 工艺 → 批记录 → 表达式"的完整配置链

## 看板能力

`getDashBoardConfig(processId)` / `saveDashboardConfig(dto)` + `bm_process_dashboard_config[_data]` —— 工艺执行实时看板。

## 关键常量

`ProcessConstant`：
- `REUSE_PROCEDURE_STEP_ID = 0L` — 复用工步的特殊 ID
- `PROCESS_ENABLE_LOCK = "process:enable:lock:%s"` — Redis 分布式锁键（按 processId 加锁）

## AI 定位提示

- **改工艺/工序/工步 CRUD** → 各自 `*Controller` + `*Service`
- **审批回调失败** → `ProcessService.audit*CallBack`，与 `bmos-orchestrator-starter` 配合
- **任务条件不重算 / 重算错** → `ITaskConditionCalculator.refreshConditionResult` + 对应 `ConditionChangeType` 子类
- **新增触发事件类型** → 新写一个 `ConditionChangeType` 子类 + 添加 `ConditionTypeEnum`
- **版本状态卡住** → 查 `ActionStateEnum` 流转 + `ProcessVersionService.validate*`
- **复制工艺出问题** → `service/impl/copy/` 三个 Copy 类
- **process ↔ record 绑定** → `ProcessBatchRecordRelationService`
- **遇到 `product_schedule_procedure_config`**（无 `bm_` 前缀）→ 是整合前遗留命名，不要按"工艺表 = bm_ 前缀"假设处理

## 相关页面

- [[mes-overview]] — mes 服务总览
- [[mes-record-module]] — 工艺通过 `bm_process_batch_record` 绑定批记录
- [[mes-audit-module]] — 工艺审批（`AuditCategoryServiceEnum.PROCESS` + `FlowAuditProcess` 工艺绑定审批流版本）
- [[mes-product-module]] — 产品定义（process 的上游配置依据）
- [[database-schema-overview]] — `bm_process_*` / `bm_procedure_*` 28 表全景
- [[service-integration]] — process 通过 platform 表达式服务（`PlatformExpressionFeignClient`）扩展计算能力
