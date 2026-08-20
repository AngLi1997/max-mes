---
title: MES Workflow 模块（工作流 / 流程编排适配层）
created: 2026-07-02
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/workflow/
status: active
---

# MES Workflow 模块

## 概述 / 职责

Workflow 模块是 mes 对 **`bmos-orchestrator-starter` 流程编排引擎**的**适配/集成层**：把 mes 的工序/步骤语义（[[mes-process-module]] 的 Process→Procedure→Step→Task）接入通用流程引擎，负责流程部署、流程实例生命周期（启动/完成/重启/终止）、任务指派行为定制、流程节点事件监听、强制操作（激活/跳转/强制完成）、计划进度与待办查询、以及工序换班管理。

- 包路径：`com.bmos.mes.service.workflow/`
- 规模：**Controller 1 · Service 接口 3 · ServiceImpl 4 · Mapper 1 · Java 65**
- 表：**仅 1 张**（`bm_product_change_team`，换班记录）。流程定义/实例/任务/历史等状态**全部存于 orchestrator 引擎内部**，不在 mes 落表。
- 关键依赖：`com.bmos.orchestrator.engine.core.*`（来自 `bmos-orchestrator-starter`，源码未入库，见下方 TODO）

> ⚠️ **本模块自身业务表极少（1 张），代码量却达 65 Java**——这正是 [[PLAYBOOK-backend]] 提醒的"单看 Controller 数会低估"。workflow 是后台支撑型编排模块，复杂度在**与引擎的协同代码**（behavior / listener / executor），而非表。

> 📝 **`bmos-orchestrator-starter` 源码未入库**：当前 workflow 通过 pom 依赖 `bmos-orchestrator-starter`（坐标见 [[mes-overview]] 关键 starter 表），但其源码不在 monorepo 内。本页所有"引擎内部机制"的描述均基于 workflow 侧的**调用证据**（import 的类、实现的接口、注册的 listener），引擎本身如何执行不在本文覆盖范围。**待 starter 源码以 subtree 引入后**，按 [[PLAYBOOK-backend]] 第十节 TODO 补足方法论并新建独立页（详见本文末「TODO · orchestrator 待补」）。

## 子包速览（按职责分 5 块）

| 子包 | 职责 | 与 orchestrator 关系 |
|---|---|---|
| **behavior** | 任务**指派行为**定制（动态算出任务办理人） | 实现 `TaskAssigneeBehavior`（引擎扩展点） |
| **listener** | 流程节点**事件监听**（工序/步骤结束、流程结束、流程终止） | 实现 `InfiniteEventListener`（引擎回调） |
| **service** | 流程**部署 / 查询 / 强制操作**（核心业务接口） | 调 `CreateDeploymentCmd` / `StartProcessInstanceCmd` 等引擎命令 |
| **controller** | REST 入口（`/flow/**`，20 个接口） | — |
| **change** | 工序**换班**（独立子域，唯一落表部分） | 独立于引擎，纯 mes 业务 |

## 数据模型（1 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_product_change_team` | `ProductChangeTeam` | 工序换班记录（`autoResultMap=true`，含 JSON 字段；按 planId + procedureModelId + nodeFunction + changeNumber 维度记录换班成员） |

> 流程定义（部署）、流程实例、任务实例、执行历史等**均由 orchestrator 引擎自管**，mes 不复制这些表。查流程进度/待办/历史时，workflow 通过引擎 API（`RuntimeContext` / `TaskInstance` / `ExecutionInstance` 等）读取，而非自己的 Mapper。

## 关键枚举

### `WorkflowType`（流程节点类型 · 在 `workflow/enums/`）

| 枚举值 | 含义 |
|---|---|
| `PROCEDURE` | 工序 |
| `PROCEDURE_STEP` | 工序步骤 |
| `IS_PAUSE` | 暂停节点 |

> 流程相关的其它状态机枚举（节点类型 `NodeTypeEnum`、步骤节点功能 `ProcedureStepNodeFunctionEnum`、工序条件实例等）在 **`bmos-mes-common` 和 `process` 子域**，详见 [[mes-process-module]]，本页不重复。

## Controller（1 个）

`WorkflowController`（`@RequestMapping("/flow")`，20 个接口），按职责分组：

| 类别 | 接口 |
|---|---|
| **流程模型/部署** | `GET /flow/model`（取流程模型）· `POST`（部署/绑定/批量部署，见 service） |
| **计划管理/历史** | `GET /flow/plan/manage/page` · `GET /flow/plan/history/page` · `GET /flow/plan/progress/page` |
| **节点查询** | `GET /flow/procedures`（含历史）· `GET /flow/steps`（含历史）· `GET /flow/procedure/step/detail` |
| **待办** | `GET /flow/todoPage/fresh`（新鲜待办分页） |
| **进度** | `GET /flow/procedure/progress` · `GET /flow/list/step/progress` · `GET /flow/subRecordList` |
| **实例操作** | `POST /flow/complete/task` · `POST /flow/complete/execution` · `POST /flow/terminate/{processInstanceId}` · `POST /flow/procedure/restart` |
| **强制操作** | `POST /flow/coerce/active`（强制激活节点）· `POST /flow/coerce/procedure/complete`（强制完成工序）· `POST /flow/active/step`（激活步骤） |
| **换班** | `POST /flow/change/team` · `GET /flow/list/change/team` |

## Service 核心方法

### `WorkflowService`（部署 / 查询 / 强制操作 · 引擎命令入口）

按业务类别归组：

**流程部署与启动**
| 方法 | 功能 | 引擎依赖 |
|---|---|---|
| `createDeployment(CreateDeploymentCmd)` | 部署流程模型 | `CreateDeploymentCmd`（引擎命令） |
| `getProcessModel(processModelId)` | 取流程模型 | 引擎 API |
| `bindDeployment(dto)` / `bindBatchDeployment(deploymentId, bindings)` | 绑定部署（计划 ↔ 流程模型） | — |
| `validateDeployment(processModelId)` / `deployBatch(ids)` | 校验/批量部署 | — |
| `startProcessInstance(StartProcessInstanceCmd)` | 启动流程实例 | `StartProcessInstanceCmd`（引擎命令） |

**节点 / 进度查询**
| 方法 | 功能 |
|---|---|
| `getWorkflowProcedures(processInstanceId, versionId)` / `getWorkflowProcedureSteps(dto)` | 取工序/步骤节点 |
| `getWorkflowHistoryProcedures` / `getWorkflowHistoryProcedureSteps` | 历史节点 |
| `getPlanManagePage` / `getPlanHistoryPage` / `getPlanProgressPage` | 计划管理/历史/进度分页 |
| `getTodoPageFresh(dto)` | 待办分页（新鲜） |
| `procedureProgress(processInstanceId)` / `listStepProgress(dto)` | 工序/步骤进度 |
| `getProductionProcedureStepDetailInfo(dto)` | 工序步骤详情 |
| `queryPlanSubRecordList(dto)` | 计划子记录 |
| `findByExecutionIdAndProcessInstanceId(executionId, processInstanceId)` | 按执行实例查任务（`TaskInstance`） |

**强制操作（运维/异常干预）**
| 方法 | 功能 |
|---|---|
| `activeStep(dto)` | 激活步骤 |
| `coerceActiveStep(dto)` | **强制**激活节点 |
| `coerceProcedureComplete(dto)` | **强制**完成工序 |
| `changeTeam(teamDTO)` | 换班（接入 change 子域） |

> 强制操作是排障/卡流程的关键干预手段，生产慎用。

### `WorkflowExecutor`（流程实例生命周期）

| 方法 | 功能 |
|---|---|
| `startWorkflow(StartWorkflowDTO)` | 启动工作流（mes 侧封装的启动入口） |
| `completeTask(CompleteTaskDTO)` | 完成任务 |
| `completeTaskByExecution(dto)` | 按执行实例完成任务 |
| `restart(WorkflowRestartDTO)` | 重启 |
| `terminate(processInstanceId)` | 终止 |

> `WorkflowExecutor` 与 `WorkflowService` 的分工：Executor 负责**实例生命周期**（启/完/重/止），Service 负责**部署/查询/强制**。两者都最终落到 orchestrator 引擎。

### `ProductChangeTeamService` + `change/execute`（换班）

| 方法 | 功能 |
|---|---|
| `saveChangeTeam(list, changeTeamNumber, nodeFunction)` | 保存换班记录（写入 `bm_product_change_team`） |
| `queryByPlanIdAndProcedureModelId(dto)` | 查计划的换班记录 |
| `selectListByPlanId(planId, nodeFunction, changeNumber)` | 按计划/节点功能/换班序号查 |
| `ChangeTeamService.changeTeam(ChangeTeamContext)` | 换班执行入口（`change/execute/`，配合 `ChangeTeamFactory` + `ProcessChangeTeamServiceImpl` / `ProcedureChangeTeamServiceImpl` 两个策略实现） |

> `change/execute/` 是**策略模式**：`ChangeTeamService` 接口 + `ProcessChangeTeamServiceImpl`（流程级换班）和 `ProcedureChangeTeamServiceImpl`（工序级换班）两个实现，由 `ChangeTeamFactory` 按上下文分发。

## 扩展点：与 orchestrator 引擎的协同（★ 本模块核心）

> 以下是 workflow 作为引擎**适配层**的三个扩展点。每个都是"实现/注册引擎接口 → 注入 mes 业务逻辑"。引擎接口本身在 `bmos-orchestrator-starter`，源码未入库。

### 1. behavior — 任务指派行为（`TaskAssigneeBehavior`）

- `CustomTaskAssigneeBehavior`：实现引擎的 `TaskAssigneeBehavior`，**动态计算任务办理人**。依赖 `PlanService` / `InstructionTeamService`（计划班组）、`ProcedureStepModelService` / `ProcedureModelService`（工序模型）、`ITaskConditionCalculator` / `ProcedureExpressionService`（条件/表达式计算）。
- `CustomTaskBehaviorFactory`：行为工厂，把上述 behavior 注入引擎。

> 引擎到某个用户任务节点时回调 `CustomTaskAssigneeBehavior` 算出谁该办理——这是 mes 班组/指派规则接入流程引擎的钩子。

### 2. listener — 流程事件监听（`InfiniteEventListener`）

四个监听器，对应工序/流程生命周期的关键事件：

| 监听器 | 触发事件 | 处理 |
|---|---|---|
| `WorkflowProcedureStepEndEventListener` | 工序**步骤**结束 | 步骤收尾（写 process 确认/记录） |
| `WorkflowProcedureEndEventListener` | **工序**结束 → 子流程结束 | 调 `ProcessConfirmService` / `ProcedureConfirmService` 写确认 |
| `WorkflowProcessEndEventListener` | **流程**结束 | 流程级收尾 |
| `WorkflowProcessTerminateEventListener` | 流程**终止** | 终止清理 |

> 监听器依赖引擎的 `InfiniteEvent` / `InfiniteEventListener` / `InfiniteEventType` / `InfiniteProcessEngineListenerHelper`。`@PostConstruct` 注册。改"工序/流程结束时要做什么"→ 改对应 listener。

### 3. executor/service — 命令调用

`WorkflowService` / `WorkflowExecutorImpl` 通过引擎命令对象（`CreateDeploymentCmd` / `StartProcessInstanceCmd` / `CompleteTaskParam`）和上下文（`RuntimeContext`）驱动引擎，并把引擎返回的 `ProcessInstance` / `ExecutionInstance` / `TaskInstance` 转成 mes VO。

## 独有机制

- **流程引擎适配**：本模块的全部"独有机制"本质都是 orchestrator 引擎能力的 mes 化封装（见上节）。mes 侧独有的只有**换班**子域（落 1 张表）。
- **无定时任务、无 MQ**（扫描未发现 `@Scheduled` / `@XxlJob` / MQ topic）。

## 与其它子域 / 服务的耦合点

- **← process**：流程定义（Process/Procedure/Step 模型、条件、表达式）来自 [[mes-process-module]]，workflow 负责把这些定义跑起来。
- **← plan**：流程实例绑定生产计划（`Plan` / `PlanService`），计划是流程实例的业务载体。详见 [[mes-plan-module]]。
- **← instruction team**：任务指派用 `InstructionTeamService`（plan/team）算办理人。
- **→ process 确认**：工序结束时 listener 调 `ProcessConfirmService` / `ProcedureConfirmService` 写确认数据。
- **bmos-orchestrator-starter**：核心依赖（源码未入库，见 TODO）。

## AI 定位提示

- 改任务办理人指派规则 → `behavior/CustomTaskAssigneeBehavior`（+ `CustomTaskBehaviorFactory`）
- 改工序/流程结束时的收尾逻辑 → `listener/Workflow*EventListener`（按工序步骤/工序/流程/终止四类选）
- 流程部署/启动/查询/强制干预 → `WorkflowService` / `WorkflowExecutor`
- REST 接口 → `WorkflowController`（`/flow/**`）
- 卡流程的排障 → 强制操作 `coerceActiveStep` / `coerceProcedureComplete`（生产慎用）
- 换班 → `change/`（`ProductChangeTeamService` + `change/execute/` 策略）
- **查流程实例/任务/历史状态**：不在 mes 表，走 orchestrator 引擎 API（`TaskInstance` / `ExecutionInstance`）

## TODO · `bmos-orchestrator-starter` 待补（源码未入库）

> 📝 这是 [[PLAYBOOK-backend]] 第十节"未覆盖场景 TODO"的具体落地条目。当 starter 源码入库后回来补齐。

**现状**：
- `bmos-mes-service/pom.xml` 依赖 `bmos-orchestrator-starter`（引擎包名 `com.bmos.orchestrator.engine.core.*`），但 **starter 源码不在 monorepo**。
- 本页描述的引擎能力（`TaskAssigneeBehavior` / `InfiniteEventListener` / `CreateDeploymentCmd` / `StartProcessInstanceCmd` / `RuntimeContext` / `ProcessInstance` / `ExecutionInstance` / `TaskInstance` 等）均来自该 starter。

**触发补齐的条件**（任一）：
- `packages/backend/shared/` 下出现 orchestrator 源码
- orchestrator 以 git subtree 引入 monorepo

**待补内容**：
1. 新建独立页（建议 `entities/_shared/orchestrator-starter.md` 或按 PLAYBOOK 拆 `PLAYBOOK-starter.md`），覆盖：
   - 引擎核心抽象：`RuntimeContext` / 流程模型元素（`BaseElement`）/ 实例模型（`ProcessInstance`/`ExecutionInstance`/`TaskInstance`）
   - 命令体系：`CreateDeploymentCmd` / `StartProcessInstanceCmd` / `CompleteTaskParam`
   - 扩展点契约：`TaskAssigneeBehavior` / `InfiniteEventListener` / `InfiniteEventType`（这是 mes workflow behavior/listener 实现的接口，需要引擎侧文档明确"何时回调、参数含义、返回约定"）
   - 引擎自管的存储（流程定义/实例/任务/历史的落表方式——解释为何 mes workflow 只有 1 张表）
2. 回链到本页"扩展点"章节，把"基于调用证据的推断"升级为"基于源码的确认"。
3. 在 [[PLAYBOOK-backend]] 第十节 1 下登记"已实战"。

> ⚠️ 在 starter 源码入库前，修改 workflow 的 behavior/listener 时**必须先看引擎接口的方法签名和注释**（从依赖 jar 反编译或 IDE 跳转），不能只凭本页推断——引擎回调契约可能比 workflow 侧看到的更复杂。

## 相关页面

- [[mes-overview]] — mes 服务总览（workflow 为头部子域；`bmos-orchestrator-starter` 在关键 starter 表）
- [[mes-process-module]] — 流程定义（Process→Procedure→Step→Task），workflow 把它跑起来
- [[mes-plan-module]] — 生产计划（流程实例的业务载体）
- [[mes-execute-module]] — 副本版本接 workflow 换班（`queryStepChangeTeamList`）
- [[service-integration]] — 流程引擎作为 mes 的平台 starter 依赖
- [[database-schema-overview]] — `bm_product_change_team` 归属（workflow 唯一表）
