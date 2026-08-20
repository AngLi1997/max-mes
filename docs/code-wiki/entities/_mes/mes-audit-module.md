---
title: MES Audit 模块（审计 / 审批流引擎适配层）
created: 2026-07-02
updated: 2026-07-02
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/audit/
status: active
---

# MES Audit 模块

## 概述 / 职责

Audit 模块是 mes 对 **`bmos-audit-engine-starter` 审批流引擎**的**适配/集成层**：管理审批流配置（模板/版本/分类/用户/消息/工艺绑定）、对接 7 类业务审批（记录/工艺/生产 BOM/指令单/操作规程/批记录/批签发）、驱动审批流（部署/启动/完成/退回/会签/或签）、提供待办/已办/历史查询与导出。是 [[mes-lotrelease-module]] 等业务模块审批回调的上游引擎。

- 包路径：`com.bmos.mes.service.audit/`
- 规模：**Controller 1 · Service 接口 5 · Mapper 6 · Java 90**
- 表：**6 张**（审批流**配置**侧：模板/版本/分类/用户/消息/工艺绑定；流程实例/任务存于引擎内部）
- 关键依赖：`com.bmos.audit.engine.core.*`（来自 `bmos-audit-engine-starter`，源码未入库，见下方 TODO）

> 📝 **与 [[mes-workflow-module]] 同构**：workflow 是 `bmos-orchestrator-starter`（工序流转）的适配层，audit 是 `bmos-audit-engine-starter`（审批流）的适配层。两者都有 behavior / listener / 命令调用三扩展点，但用途不同——workflow 管"工序怎么走"，audit 管"谁来审批、怎么通过"。

> 📝 **`bmos-audit-engine-starter` 源码未入库**：audit 通过 pom 依赖该 starter（引擎包名 `com.bmos.audit.engine.core.*`），源码不在 monorepo。本页引擎机制描述基于 audit 侧调用证据。**待 starter 源码以 subtree 引入后**，按 [[PLAYBOOK-backend]] 第十节补足方法论并新建独立页（详见本文末 TODO）。

## 子包速览（按职责分 9 块）

| 子包 | 职责 | 与引擎关系 |
|---|---|---|
| **controller** | REST 入口（`/audit/**`，22 接口） | — |
| **service / mapper / model / dto / vo / convert** | 审批流**配置**业务层（模板/版本/分类/用户/消息 CRUD + 流程操作） | 调引擎命令 |
| **condition** ★ | **审计数据条件策略**（7 类业务各一个，决定审批流取哪些业务数据） | 引擎取数钩子 |
| **complete** ★ | **任务完成策略**（会签 CountersignComplete / 或签 OrViseComplete） | 引擎完成判定扩展点 |
| **Behavior** | 任务**指派行为**定制（动态算审批人） | 实现 `TaskAssigneeBehavior`（引擎扩展点） |
| **listener** | 审批流**事件监听**（5 个：节点执行/流程结束/退回/拒绝结束/执行结束） | 实现 `InfiniteEventListener`（引擎回调） |
| **builder** | `AuditCategoryServiceEnum`（业务类别 ↔ Condition 映射）+ 条件数据构建器 | — |
| **validate** | 用户任务载荷校验规则 | — |
| **constant** | 审计消息常量 | — |

> ⚠️ **包名大小写不规范**：`Behavior/` 子包首字母大写（Java 包名按惯例应全小写）。现状如此，重构需改 import 路径。

## 数据模型（6 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_flow_audit` | `FlowAudit` | 审批流模板主表 |
| `bm_flow_audit_version` | `FlowAuditVersion` | 审批流版本（模板多版本管理） |
| `bm_flow_audit_category` | `FlowAuditCategory` | 审批分类（按业务类别 `AuditCategoryServiceEnum` 组织） |
| `bm_flow_audit_user` | `FlowAuditUser` | 审批流用户配置（审批人/候选人） |
| `bm_flow_audit_message` | `FlowAuditMessage` | 审批消息（待办通知/审批意见） |
| `bm_flow_audit_process` | `FlowAuditProcess` | 审批流 ↔ 工艺绑定（按工艺绑定审批流版本） |

> 流程实例、任务实例、执行历史等**由 audit-engine 引擎自管**，mes 不复制。待办/已办/历史查询经引擎 API（`TaskListResp` / `PageHistoryInstanceResp` / `TaskHistoryResp`）读取。

## 关键枚举：`AuditCategoryServiceEnum`（★ 业务接入核心）

定义在 `builder/AuditCategoryServiceEnum.java`，是**业务模块接入审批的注册表**——每个业务审批类别绑定一个 Condition 策略类：

| 枚举值 | 中文 | code | Condition 策略类 | 对接业务模块 |
|---|---|---|---|---|
| `RECODE` | 记录审批 | `12002000101` | `RecordAuditCondition` | [[mes-record-module]] |
| `PROCESS` | 工艺审批 | `12002000201` | `ProcessAuditConditon` ⚠️ | [[mes-process-module]] |
| `PRODUCT_FORMULA` | 生产 BOM 审核 | `12002000301` | `ProductAuditCondition` | [[mes-product-module]] |
| `PRODUCT_PLAN` | 指令单审核 | `12003000101` | `PlanAuditCondition` | [[mes-plan-module]] |
| `OPERATE_RULE_AUDIT` | 操作规程审批 | `12002000401` | `OperateRuleAuditCondition` | （操作规程） |
| `BATCH_RECORD_ARCHIVE` | 批记录审批 | `12005000101` | `BatchRecordCondition` | [[mes-record-module]]（归档） |
| `BATCH_SIGNATURE` | 批签发审核 | `12004000101` | `LotReleaseCondition` | [[mes-lotrelease-module]] |

> `getService(code)` 按 code 反查 Condition 类——审批流启动时按业务类别取对应 Condition 收集业务数据。**新增一种业务审批 → 加一个枚举值 + Condition 实现类**。

> ⚠️ **错别字**：`ProcessAuditConditon.java`（应为 `Condition`，漏 i），与 mes-overview 的 `facotry` 同类历史遗留。改名涉及 import 路径，需作独立重构任务。

## 扩展点 1：condition — 审计数据条件策略（7 个）

`AbstractAuditDataCondition`（抽象基类）+ 7 个业务实现，依赖 `PlatformApiAdaptor` / `ProcessService` / `PlanService` 等取业务数据：

- `RecordAuditCondition` / `BatchRecordCondition`（记录、批记录）
- `ProcessAuditConditon` ⚠️（工艺）
- `ProductAuditCondition`（生产 BOM）
- `PlanAuditCondition`（指令单）
- `OperateRuleAuditCondition`（操作规程）
- `LotReleaseCondition`（批签发）

> Condition 决定"审批该业务时，引擎要带上哪些业务上下文数据"。改某业务审批的数据收集逻辑 → 改对应 Condition。

## 扩展点 2：complete — 任务完成策略

`TaskComplete`（接口）+ 工厂 `TaskCompleteFactory` + 两个实现：

| 实现 | 含义 | 完成判定 |
|---|---|---|
| `CountersignComplete` | **会签** | 所有人都审批后才算完成 |
| `OrViseComplete` | **或签** | 任一人审批即完成 |

> 引擎到用户任务节点时，按配置选会签/或签策略判定任务是否完成。

## 扩展点 3：Behavior / listener — 引擎回调（同 workflow 模式）

### behavior（任务指派）
- `FlowAuditTaskAssigneeBehavior`：实现引擎 `TaskAssigneeBehavior`，动态算审批人（依赖 `FlowAuditUser` 配置 + `PlatformApiAdaptor` 取用户）。
- `FlowAuditTaskBehaviorFactory`：行为工厂，注入引擎。

### listener（审批流事件，5 个）

| 监听器 | 触发事件 |
|---|---|
| `AuditFlowNodeExecutionListener` | 节点执行 |
| `AuditFlowExecutionEndListener` | 执行结束 |
| `AuditFlowProcessEndListener` | 流程结束 |
| `AuditFlowBackToPrevListener` | **退回上一步** |
| `AuditFlowProcessRejectEndListener` | **拒绝结束** |

> 依赖引擎的 `InfiniteEventListener` / `InfiniteEvent`（同 workflow）。改"审批通过/退回/拒绝时要做什么"→ 改对应 listener。

## Controller（1 个）

`FlowAuditController`（`@RequestMapping("/audit")`，22 接口），按职责分组：

| 类别 | 接口 |
|---|---|
| **审批流配置** | `GET /audit/flow/audit/page`· `POST /audit/save/flow/audit`· `GET /audit/delete/flow/audit`· `GET /audit/detail/flow/audit`· `PUT /audit/changeState`· `GET /audit/get/flow/audit/code`· `GET /audit/list/flow/audit/category` |
| **工艺绑定** | `PUT /audit/flow/audit/bind/process`· `GET /audit/flow/audit/process/list`· `POST /audit/checkout/deployment`（校验部署） |
| **流程操作** | `POST /audit/complete`（完成审批）· `POST /audit/complete/not/approve`（不通过完成）· `POST /audit/back/to/prev`（退回上一步）· `GET /audit/list/make/user`（取审批人） |
| **待办/已办** | （`queryToDoListByCategory` / `queryDoneListByCategory` 走 service，引擎 API） |
| **历史/导出** | `GET /audit/list/flow/audit/history`· `GET /audit/list/audit/history`· `GET /audit/list/task/history`· `GET /audit/export/audit/history`· `GET /audit/export/task/history`· `GET /audit/flow/audit/history/category` |

> 审批流启动（`flowAuditStart`）在 service，对应 `/audit` 下提交入口。

## Service 核心方法

### `FlowAuditService`（核心 · 配置 + 流程操作）

**审批流配置**
| 方法 | 功能 |
|---|---|
| `flowAuditPage(dto)` / `detailFlowAudit(versionId)` | 审批流分页/详情 |
| `saveFlowAudit(SaveAuditDTO)` | 保存审批流（含部署） |
| `checkoutDeployment(dto)` | 校验部署（部署前检查流程模型） |
| `bindFlowAuditProcess(dto)` / `selectBindProcessFlowAudit(code, processId)` / `flowAuditProcessList(code)` | 工艺绑定/查询 |
| `changeFlowAuditState(dto)` | 改版本状态 |

**流程操作**
| 方法 | 功能 |
|---|---|
| `flowAuditStart(FlowStartDTO)` | ★ 启动审批流（返回实例） |
| `flowAuditComplete(CompleteDTO)` | ★ 完成审批（通过） |
| `flowAuditCompleteNotApprove(dto)` | 完成审批（不通过） |
| `flowAuditBackToPrev(dto)` | 退回上一步 |
| `saveAuditBackHistory(businessId, comment, remark, nodeName, modelName)` | 保存退回历史 |

**待办/已办/历史**
| 方法 | 功能 |
|---|---|
| `queryToDoListByCategory(dto)` / `queryDoneListByCategory(dto)` | 按类别的待办/已办（引擎 API） |
| `findByTaskId(taskId)` / `findBatchTaskByProcessInstanceId(pid)` | 按任务/实例查 |
| `findHistoryByCategoryCodeAndAssignee(cmd)` / `listTaskHistory(pid)` | 历史 |
| `listFlowAuditHistory(dto)` / `listAuditHistory(dto)` | 审批历史 |
| `exportAuditHistory(dto, resp)` / `exportTaskHistory(dto, resp)` | 导出（Excel 流） |
| `getAuditCategoryToDoCount(userId)` | 按类别的待办计数（首页红点） |

### 其它 Service

- `FlowAuditVersionService` — 版本管理
- `FlowAuditCategoryService` — 分类管理
- `FlowAuditUserService` — 审批人配置
- `FlowAuditMessageService` — 审批消息（待办通知/意见）

## 独有机制

- **审批流引擎适配**：本模块的全部"独有机制"本质是 audit-engine 的 mes 化封装（condition / complete / behavior / listener 四扩展点）。mes 侧独有的是 **7 类业务审批的 Condition 策略注册表**（`AuditCategoryServiceEnum`）。
- **无定时任务、无 MQ**（扫描未发现 `@Scheduled` / MQ topic）。

## 与其它子域 / 服务的耦合点

- **← 7 类业务模块**：通过 `AuditCategoryServiceEnum` + Condition 接入——record（记录/批记录归档）、process（工艺）、product（生产 BOM）、plan（指令单）、lotrelease（批签发）、操作规程。
- **→ 业务模块的审批回调**：如 [[mes-lotrelease-module]] 的 `auditCallback` 由本模块审批完成后回调。
- **← platform**：`PlatformApiAdaptor`（用户/部门/权限）、消息推送（审批待办通知）。
- **bmos-audit-engine-starter**：核心依赖（源码未入库，见 TODO）。

## AI 定位提示

- 某业务（批签发/批记录/指令单…）审批不生效 → 先查 `AuditCategoryServiceEnum` 找对应 code + Condition → 看 Condition 取数逻辑 → 再看 `flowAuditStart` 是否正确启动
- 审批人算错 → `FlowAuditTaskAssigneeBehavior`（Behavior/）+ `FlowAuditUser` 配置
- 会签/或签判定错 → `complete/`（`CountersignComplete` / `OrViseComplete` + `TaskCompleteFactory`）
- 审批通过/退回/拒绝的副作用没执行 → `listener/AuditFlow*Listener`
- 待办/已办查不到 → `queryToDoListByCategory`（引擎 API，确认引擎实例是否存在）
- 工艺绑定审批流错 → `bindFlowAuditProcess` / `selectBindProcessFlowAudit`
- 历史导出 → `exportAuditHistory` / `exportTaskHistory`
- **查流程实例/任务/历史**：不在 mes 表，走 audit-engine 引擎 API
- ⚠️ 改 `ProcessAuditConditon` 类名需同步改 `AuditCategoryServiceEnum` 的引用

## TODO · `bmos-audit-engine-starter` 待补（源码未入库）

> 📝 这是 [[PLAYBOOK-backend]] 第十节"未覆盖场景 TODO"的具体落地条目（与 [[mes-workflow-module]] 的 orchestrator TODO 同类）。

**现状**：
- `bmos-mes-service/pom.xml` 依赖 `bmos-audit-engine-starter`（引擎包名 `com.bmos.audit.engine.core.*`），源码不在 monorepo。
- 本页引擎能力（`TaskAssigneeBehavior` / `InfiniteEventListener` / `CreateDeploymentCmd` / `StartProcessInstanceCmd` / `CompleteBatchTaskCmd` / `BackToPrevCmd` / `UpdateTaskCmd` / `RuntimeContext` / `TaskConstant` 等）均来自该 starter。

**触发补齐条件**（任一）：
- `packages/backend/shared/` 下出现 audit-engine 源码
- audit-engine 以 git subtree 引入

**待补内容**：
1. 新建独立页（建议 `entities/_shared/audit-engine-starter.md`），覆盖：引擎核心抽象、命令体系、四扩展点契约（condition 取数/complete 判定/behavior 指派/listener 回调的"何时触发、参数、返回约定"）、引擎自管存储。
2. 回链本页四扩展点，把"基于调用证据的推断"升级为"基于源码的确认"。
3. 在 [[PLAYBOOK-backend]] 第十节登记"已实战"。
4. **可与 orchestrator starter 合并为同一份"平台 starter 方法论"**（两者结构高度同构），视复杂度决定是否拆 `PLAYBOOK-starter.md`。

> ⚠️ starter 入库前修改 audit 的 behavior/listener/condition/complete 时**必须先看引擎接口签名**（从 jar 反编译或 IDE 跳转），不能只凭本页推断。

## 相关页面

- [[mes-overview]] — mes 服务总览（audit 为头部子域；`bmos-audit-engine-starter` 在关键 starter 表）
- [[mes-workflow-module]] — 同构的引擎适配层（orchestrator），可对照阅读
- [[mes-lotrelease-module]] — 批签发审核（audit 的典型业务接入方，`BATCH_SIGNATURE`）
- [[mes-record-module]] — 记录审批 / 批记录归档审批（`RECODE` / `BATCH_RECORD_ARCHIVE`）
- [[mes-plan-module]] — 指令单审核（`PRODUCT_PLAN`）
- [[mes-process-module]] — 工艺审批（`PROCESS`）
- [[service-integration]] — audit-engine 作为 mes 平台 starter 依赖 + platform 审批通知
- [[database-schema-overview]] — `bm_flow_audit*` 表归属
