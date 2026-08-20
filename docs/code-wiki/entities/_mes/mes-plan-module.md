---
title: MES Plan 模块（生产计划/排程/归档）
created: 2026-06-29
updated: 2026-06-29
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/plan/
  - packages/backend/services/mes/bmos-mes-common/src/main/java/com/bmos/mes/common/enums/plan/
status: active
---

# MES Plan 模块（生产计划/排程/归档）

## 概述 / 职责

Plan 模块是 mes 的**业务入口层**，从生产排程到批记录归档形成业务闭环：

```
生产计划（ProductionPlan）
  ↓ 排程下发
生产指令（Instruction）  ←  班组（ProductPlanTeam）
  ↓ 分解
产品计划/批次（Plan / ProductPlan）
  ↓ 启动 → 工艺执行（衔接 [[mes-process-module]]）
  ↓ 完成
批记录归档（BatchRecordArchive）  ←  批记录模板（BatchTemplate）
```

- 包路径：`com.bmos.mes.service.plan/`
- 规模：**13 Controller / ~25 Mapper / 21 张表 / 17 Service**
- 独有机制：**自研 MQ 抽象**（@Topic/@Consumer）+ **XXL-Job 定时任务**（@XxlJob）
- 整体按 **7 个并列子包**组织：`info` / `production` / `instruction` / `team` / `template` / `document` / `rule`

## 7 子包速览（建议先看本节理解目录）

| 子包 | 业务 | Controller | 关键表 |
|---|---|---:|---|
| **info** 计划信息 | 产品计划（Plan）主流程：审批、执行、回溯 | 3 | `bm_product_plan` `bm_product_plan_relation` `bm_product_plan_code_rule` `bm_product_plan_no_info` |
| **production** 生产计划 | 上层生产计划（一个生产计划拆成多个 Plan） | 1 | `bm_production_plan` `bm_production_plan_item` |
| **instruction** 生产指令 | 生产指令分解、确认、下发 | 1 | `bm_product_instruction` |
| **team** 班组 | 计划/指令的班组绑定 | 2 | `bm_product_plan_team` `bm_product_instruction_team` `bm_team_production_line` |
| **template** 计划模板 | 计划模板（含批次/工序配置） | 1 | `bm_plan_template` `bm_plan_template_batch` |
| **document** 批记录文档归档 | 批记录模板 + 归档（plan 的输出物） | 4 | `bm_batch_template_*`（5 表） + `bm_batch_record_archive*`（3 表） |
| **rule** 编码规则 | 计划编号规则、批次号规则 | 1 | （依赖 platform 编码规则） |

## 数据模型（21 张表）

### info 子包（5 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_product_plan` | `Plan` | **产品计划主表** ⚠️ 注意 model 类名是 `Plan` 不是 `ProductPlan` |
| `bm_product_plan_relation` | `ProductPlanRelation` | 计划与产品关联 |
| `bm_product_plan_no_info` | `ProductPlanNoInfo` | 计划编号信息 |
| `bm_product_plan_code_rule` | — | 编号规则缓存 |
| `bm_plan_template_batch` | `PlanTemplateBatch` | 模板批次（`autoResultMap`） |

### production 子包（2 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_production_plan` | `ProductionPlan` | 生产计划主表（上层） |
| `bm_production_plan_item` | `ProductionPlanItem` | 生产计划明细 |

### instruction 子包（1 表）

`bm_product_instruction` → `Instruction` 生产指令主表

### team 子包（3 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_product_plan_team` | `ProductPlanTeam` | 计划班组（`autoResultMap`） |
| `bm_product_instruction_team` | `InstructionTeam` | 指令班组（`autoResultMap`） |
| `bm_team_production_line` | `TeamProductionLine` | 班组-产线绑定 |

### template 子包（2 表）

| 表 | Model | 说明 |
|---|---|---|
| `bm_plan_template` | `PlanTemplate` | 计划模板 |
| `bm_plan_template_batch` | `PlanTemplateBatch` | 模板批次配置 |

### document 子包（8 表，plan 内最复杂）

| 表 | Model | 说明 |
|---|---|---|
| `bm_batch_template_info` | `BatchTemplateInfo` | 批记录模板主表 |
| `bm_batch_template_version` | `BatchTemplateVersion` | 批记录模板版本 |
| `bm_batch_template_category` | `BatchTemplateCategory` | 模板分类 |
| `bm_batch_template_info_process` | `BatchTemplateInfoProcess` | 模板-工艺绑定 |
| `bm_batch_template_variable` | `BatchTemplateVariable` | 模板变量 |
| `bm_batch_template_operate_log` | `BatchTemplateOperateLog` | 模板操作日志 |
| `bm_batch_record_archive` | `BatchRecordArchive` | **批记录归档主表** |
| `bm_batch_record_archive_generate` | `BatchRecordArchiveGenerate` | 归档生成记录 |
| `bm_batch_record_archive_log` | `BatchRecordArchiveLog` | 归档操作日志 |

> **概念区分**（最易混淆）：
> - `bm_batch_record`（→ [[mes-record-module]]）= **批记录文档结构/模板设计**
> - `bm_batch_template_*`（plan/document）= **plan 内独立的"批记录模板"管理**（含版本、分类、工艺绑定、操作日志）
> - `bm_batch_record_archive*`（plan/document）= **批记录执行后的归档产物**
> 三者层次：record 设计 → plan/template 模板管理 → plan/archive 执行归档。

## 关键枚举（计划全生命周期状态机的核心）

### ProductPlanStatusEnum（产品计划状态）

`EDIT`(编辑) → `AUDIT`(审批中) → `CONFIRM`(确认) → `DISCARD`(作废)

### ProductPlanStartEnum（计划执行启动状态，用于 MQ 消息）

`WAIT`(等待) / 启动中（见下方 STARTING）/ `END`(已完成) / `TERMINATION`(终止)

### ProductionStatusEnum（生产状态，复合枚举，含 ProductPlanStartEnum 映射）

| 枚举 | 含义 | 映射 PlanStart |
|---|---|---|
| `NOT_ISSUED` | 未下发 | WAIT |
| `ISSUED` | 已下发 | WAIT |
| `DURING_PRODUCTION` | 生产中 | STARTING |
| `PRODUCTION_PAUSED` | 生产暂停 | STARTING |
| `PRODUCTION_COMPLETED` | 生产完成 | END |
| `PRODUCTION_TERMINATION` | 生产终止 | TERMINATION |

> 这是 mes 最复杂的状态枚举之一——同时承载"生产业务态"和"计划启动态"两个维度。

### ProductPlanInstructStatusEnum（指令分解/下发状态）

`WAIT_DECOMPOSE`(待分解) → `WAIT_CONFIRM`(待确认) → `WAIT_SEND`(待下发) → `SEND`(已下发)

### ProductPlanInstructStatusEvent（指令状态触发事件）

`WAIT_CONFIRM`(分解) / `CONFIRM`(确认) / `SEND`(下发)

### ProductPlanTypeEnum（计划类型，3 类共存）

`PRODUCT`(生产批次, A) / `EXPERIMENT`(实验批次, B) / `VERIFY`(验证批次, C)

### InstructionStatusEnum（指令状态）

`RESOLVE`(已分解) / `CONFIRM`(已确认)

### PlanArchiveStatusEnum（归档状态）

`WAIT_ARCHIVE`(待归档) → `ARCHIVE_ING`(归档中) → `ARCHIVE_SUCCESS`(已归档) / `ARCHIVE_FAIL`(归档失败)

### BatchRecordArchiveStatusEnum（归档单状态）

`EDIT`(830401,"编辑") → `AUDIT`(830402,"审批中") → `EFFECTIVE`(830403,"生效") / `SCRAP`(830404,"作废")

### BatchRecordArchiveOperateTypeEnum（归档操作类型，code 用于操作日志）

`RE_GENERATE`(830301) / `UPLOAD`(830302) / `DOWNLOAD`(830303) / `AUDIT`(830304) / `AUDIT_COMPLETE`(830305) / `SCRAP`(830306) / `GENERATE`(830307) / `AUTO_GENERATE`(830308)

### TemplateVersionStatusEnum / TemplateVersionOperateTypeEnum

模板版本状态 EDIT/CONFIRM/SCRAP（830201~830203），操作 ADD/UPLOAD/DELETE/CONFIRM/SCRAP/NORMAL/DOWNLOAD（830101~830107）。

### PlanAuditProgressStatusEnum（审批进度，含状态机标记）

`PENDING_SUBMISSION`("待提交", "DISABLE") → `SUBMITTED`("已提交", "ENABLE") → `UNDER_AUDIT`("审核中", "ACTIVATED") → `AUDIT_COMPLETED`("审核完成", "COMPLETE")

> 第 3 个字段是 `ProductTaskStatusEnum`：DISABLE / ENABLE / ACTIVATED / COMPLETE。

### CodeRuleTypeEnum（编码规则类型）

`PRODUCT_PLAN_NO`(生产计划批号规则) / `PRODUCT_PLAN_BATCH_NO`(生产批号规则)

## Controller（13 个，按子包归类）

| 子包 | Controller |
|---|---|
| info | `PlanController` / `PlanRelationController` / `PlanRetraceController` |
| production | `ProductionPlanController` |
| instruction | `InstructionController` |
| team | `InstructionTeamController` / `ProductPlanTeamController` |
| template | `PlanTemplateController` |
| document | `BatchRecordArchiveController` / `BatchTemplateCategoryController` / `BatchTemplateController` / `PlanArchiveController` |
| rule | `CodeRuleController` |

## Service 核心方法（17 Service，挑头部讲）

### PlanService（info，主服务）

方法量最大，约 45 个，分 6 类：

| 类别 | 代表方法 |
|---|---|
| 查询 | `page` / `pageTraceable` / `auditPage` / `detail` / `getById` / `getByIds` |
| 编辑/批量 | `batchSave` / `update` / `discard` / `approve` / `approveBatch` / `updateRelation` |
| **审批回调**（与 orchestrator 配合） | `auditSuccess` / `auditTermination` / `auditPlanLog` |
| **执行回调** | `executeCallBackSuccess` / `executeCallBackTermination` / `selectByExecuteProcessInstanceId` |
| 执行控制 | `pauseExecute` / `recoveryExecute` |
| 查询/启动列表 | `startPlanList` / `batchListByPlanStart` / `getTodoPlanStart` |

### ProductionPlanService（production）

`listPage` / `listPlanDetail` / `planNullify` / **`buildPlan`**（构建计划） / **`buildBatchNo`**（生成批号） / **`issueProductionPlan`**（下发生产计划） / `buildPlanNoAndBatchNo` / `directlyCreatePlan`（直接创建）

### InstructionService（instruction）

`page` / `detail` / `save` / `update` / **`generate`**（生成，含 `autoConfirm` 参数） / **`send`**（下发）

### ProductPlanTeamService（team）

`page` / `detail` / `save` / `update` / `enable` / `disable` / `getListByUserId` / `getTeamListByProductionLineIds` / `getTeamListByProductPlanId`

### PlanTemplateService（template）

`savePlanTemplate` / `editPlanTemplate` / `deletePlanTemplate` / `changePlanTemplateState` / `getPlanTemplateDetail` / `getEnablePlanTemplateList` / `validateProcessVersionMatch`（校验工艺版本是否匹配） / `updateTemplateConfirmStatus`

### BatchTemplateService（document，批记录模板）

`fileUpload` / `saveTemplate` / `saveTemplateVersion` / `uploadTemplateVersion` / `downloadTemplateVersion` / `scrapTemplateVersion` / `confirmTemplateVersion` / `normalTemplateVersion`(设为默认) / **`templateInfoBindProcess`**（绑定工艺） / **`templateInfoBindDataAuth`**（绑定数据权限） / `templateInfoPage` / `templateVersionPage`

### BatchRecordArchiveService（document，批记录归档）

| 方法 | 功能 |
|---|---|
| `generateArchive(dto)` | 手动生成归档 |
| `autoGenerateArchive(dto)` | **自动生成**（被 MQ 消费触发） |
| `reGenerate(dto)` | 重新生成 |
| `generateCallBack(dto)` | 生成回调 |
| `judgeGenerate(generateId)` | 判定生成结果 |
| `verifyTemplateVersion(dto)` | 校验模板版本 |
| `removeVerifyArchive()` | **被 XXL-Job 定时调** — 删除验证产生的临时文件 |
| `auditArchive` / `auditCallBack` / `scrapArchive` / `effectiveArchive` | 归档生命周期 |
| `download` / `downloadPath` | 下载 |
| `archivePage` / `planArchiveRecordPage` / `archiveFlowPage` | 分页查询 |
| `planInfo(dto)` | 计划信息查询 |

### PlanRetraceService（info，**追溯**）

按维度提供回溯能力：
- `planBatchRetracePage` — 批次回溯
- `detailInfo` — 详情
- `executeTracePage` — 执行追溯
- `materialTracePage` — 物料追溯
- `equipmentTracePage` — 设备追溯
- `roomTracePage` — 房间追溯
- `procedureTracePage` — 工序追溯（偏差）
- `getProcedureStepTaskExecuteList(id)` — 工步任务执行清单

> 这是制药 MES 合规的核心能力：任何一个批次都能反查到物料/设备/房间/操作的完整链路。

### CodeRuleService（rule）

`page` / `save` / `update` / `detailCode` / `selectByProcessIdAndType` / `getNextUseNo`（取下一个编号） / `getBatchNextUserNo` / `getCodeRuleListByProcessIdAndType`

## 自研 MQ 抽象（plan 独有亮点）

### 设计

- **基础抽象**：`com.bmos.mes.mq.BaseMqTopic`（在 mes-common 或基础包）
- **注解**：`@Topic("TOPIC_NAME")` 标记接口、`@Consumer` 标记消费方法
- **入口**：`info/mq/topic/PlanStatusChangeTopic.java` 声明 topic，`info/mq/message/PlanStatusChangeMessage.java` 定义消息体

### 实例：计划状态变更

```java
@Topic("PLAN_STATUS_STATUS_TOPIC")
public interface PlanStatusChangeTopic extends BaseMqTopic<PlanStatusChangeMessage> {}

public class PlanStatusChangeMessage implements Serializable {
    private Plan plan;
    private ProductPlanStartEnum currentPlanStatus;
}
```

### 消费端

`document/mq/PlanArchiveConsumer.java`（@Consumer）—— 计划状态变更 → 触发批记录自动归档 `autoGenerateArchive`。

> **机制要点**：Plan 状态变化通过 MQ 广播给归档子系统，避免 Service 直接耦合。新增"计划状态变更的订阅者" = 加一个 `@Consumer` 类。

## XXL-Job 定时任务

入口：`document/job/BatchArchiveJob.java`

| 任务 | 注解 | 用途 |
|---|---|---|
| `updateStorageBatchAvailable()` | `@XxlJob("removeVerifyArchive")` | 定时删除批记录验证产生的临时文件 |

> 依赖：`bmos-scheduler-core`（见 [[mes-overview]] 的 starter 清单）。新增定时任务 = 加 `@XxlJob` 方法。

## 关键常量

`PlanConstant`：负责把 MySQL `DuplicateKeyException` 的**唯一索引名**映射到业务错误码：

| 唯一索引 | 业务错误码 |
|---|---|
| `bm_product_plan.uk_planNo` | `PRODUCT_PLAN_NO_DUPLICATE` |
| `bm_product_plan.uk_processId_batchNo` | `PRODUCT_PLAN_BATCH_NO_DUPLICATE` |

入口：`PlanConstant.findException(DuplicateKeyException)` —— **改唯一索引名时必须同步改本类映射**，否则错误会降级为通用 `DUPLICATE_KEY_ERROR`。

## 与其它子域的耦合点

- **→ process**：Plan 启动后绑定工艺版本执行（详见 [[mes-process-module]] 审批回调）
- **→ record**：批记录模板（document/BatchTemplate）的设计依赖 record 的组件体系（详见 [[mes-record-module]]）
- **→ product**：计划绑定产品（`ProductPlanRelation` 与 [[mes-product-module]] 的产品树）
- **→ platform**：编码规则通过 platform `CodeRuleFeign`（详见 [[service-integration]]）
- **MQ 触发归档** → BatchRecordArchiveService.autoGenerateArchive
- **XXL-Job 触发清理** → BatchRecordArchiveService.removeVerifyArchive

## AI 定位提示

- **计划 CRUD / 审批 / 执行控制** → `info/service/PlanService` + 4 个 `audit*CallBack` / `executeCallBack*`
- **追溯查询（批次/物料/设备/房间/工序）** → `info/service/PlanRetraceService` 7 个 `*TracePage` 方法
- **生产计划下发** → `production/service/ProductionPlanService.issueProductionPlan`
- **指令分解/下发** → `instruction/service/InstructionService.generate` / `.send`
- **批记录模板管理** → `document/service/BatchTemplateService`（注意区分于 record 模块的 `BatchRecord*`）
- **归档生成失败/重生成** → `document/service/BatchRecordArchiveService.{judgeGenerate, reGenerate, generateCallBack}`
- **新增 MQ 订阅** → 实现 `@Consumer` 方法监听对应 Topic
- **新增定时任务** → 在 `document/job/` 加 `@XxlJob` 方法
- **唯一约束冲突错误码不对** → 检查 `PlanConstant` 的索引名映射
- **遇到 `Plan` 类不是 `ProductPlan`** → 是 info 子包的命名习惯，类名 `Plan` 对应表 `bm_product_plan`

## 相关页面

- [[mes-overview]] — mes 服务总览
- [[mes-process-module]] — 工艺执行（Plan 启动后的下游）
- [[mes-record-module]] — 批记录设计（与 plan/document 的 BatchTemplate 区分）
- [[mes-product-module]] — 产品（计划绑定的上游）
- [[service-integration]] — 编码规则等 platform 依赖
- [[database-schema-overview]] — `bm_product_plan_*` / `bm_batch_template_*` / `bm_batch_record_archive_*` 全景
