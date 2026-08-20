---
title: MES Execute 模块（执行表单数据 / 批记录数据主写方）
created: 2026-07-02
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/execute/
status: active
---

# MES Execute 模块

## 概述 / 职责

Execute 模块是 mes 批记录的**数据执行层**：负责工序步骤表单数据的录入、修订、更新、废弃，公式/计算预览，业务组件数据保存，记录项的副本版本（copyVersion）管理，工序步骤锁定，附件上传，以及趋势分析、计划修订统计等查询。它是 [[mes-overview]] 强调的**跨服务复用表 `bm_execute_form_data` 的唯一写入方**，lims 只读复用。

- 包路径：`com.bmos.mes.service.execute/`
- 规模：**Controller 2 · Service 接口 5 · Mapper 4 · Java 76**
- 关键依赖：record（批记录项/组件定义）、process（步骤模型/公式计算）、plan（生产计划）；详见下方耦合点
- 独有机制：**副本版本（copyVersion）**、**步骤锁（Redis）**、公式计算、趋势分析

> ⚠️ **跨服务复用表**：`bm_execute_form_data` 在 **lims 中也被引用**，是 mes→lims 的数据流接口表。**写入归属在 mes（本模块）**，lims 只读，且 lims 有自己的 `ExecuteFormData` entity + 4 个 ExtInfo 扩展信息类（`bmos-lims2-common/.../model/execute/`）。改本表结构必须同步通知 lims。详见 [[service-integration]] 与 [[database-schema-overview]]。

## 数据模型（4 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_execute_form_data` | `ExecuteFormData` | ★ 执行表单数据主表（数据值、录入类型 `ExecuteFormDataType`、副本版本号 copyVersion、修订计数等）—— **跨服务复用，mes 写 / lims 读** |
| `bm_execute_attachment` | `ExecuteAttachment` | 执行附件（表单数据关联的图片/文件，含备注） |
| `bm_execute_record_copy` | `ExecuteRecordCopy` | 记录副本版本档案（每个副本版本的元信息：计划/步骤/换班/版本号） |
| `bm_execute_subsidiary_record` | `ExecuteSubsidiaryRecord` | 副记录（子记录文档） |

> `bm_execute_form_data` 含 `value`（数据值）+ 多个前端扩展字段，按 `productPlanId + recordItemId + procedureStepId + copyVersion` 维度定位一条执行数据。

## 关键枚举与常量

### `ExecuteFormDataType`（执行数据录入类型 · 在 `execute/enums/`）

| code | 中文 | value |
|---|---|---|
| `SAVE` | 录入 | `save` |
| `MODIFY` | 修订 | `modify` |
| `UPDATE` | 更新 | `update` |

> 区分首次录入、修订（带历史版本）、更新（原位更新）三种写入语义，影响修订计数 `handlePlanModifyCount` 与副本版本逻辑。

### `ExecuteFormDataConstant`（关键常量）

| 常量 | 值 | 含义 |
|---|---|---|
| `FORMULA_PROCEDURE_STEP_ID` | `0L` | 公式专用步骤 ID 占位（公式不属于具体步骤时用 0） |
| `DEFAULT_COPY_VERSION` | `0L` | 默认副本版本号 |
| `CALCULATE_DEFAULT_COPY_VERSION` | `Integer.MAX_VALUE` | 计算时用的默认副本版本（区分计算上下文） |
| `OPERATION_USER_SYSTEM` | `"system"` | 系统操作的占位用户 |

### Redis Key / 分布式锁

| Key | 模式 | 用途 |
|---|---|---|
| `ExecuteRedisKeyDefine.LOCK_STEP` | `bmos:execute:lock:%s` | **工序步骤锁**（STRING，永久）—— `lockProcedureStep`/`unLockProcedureStep` 用，防并发录入 |
| `RedissionKeyConstant.EXECUTE_EXPRESS` | `mes:execute:express:receive:%s` | 表达式/公式接收的 Redisson 锁 |

> 改步骤并发逻辑必须看这两个 Key，与 `LOCK_STEP` 配套。

## Controller（2 个）

### `ExecuteFormDataController`（`@RequestMapping("/execute")`，23 个接口）

| 类别 | 接口 |
|---|---|
| **数据写入** | `POST /execute/batch/save`（批量录入）· `POST /execute/modify`（修订）· `POST /execute/update`（更新）· `PUT /execute/discard`（废弃） |
| **业务组件** | `POST /execute/business/save/batch`（业务组件批量保存）· `GET /execute/business/saved`（校验是否已保存） |
| **计算** | `POST /execute/calculation/preview`（计算预览）· `GET /execute/calculate/date`（日期计算） |
| **查询** | `GET /execute/field/data/list`· `GET /execute/item/latest/data`（记录项最新值）· `GET /execute/intact/merge/list`（完整合并列表）· `GET /execute/subsidiary/list`· `GET /execute/procedure/view`· `GET /execute/field/trend/analysis`（趋势分析）· `GET /execute/plan/modify/list`（计划修订统计）· `GET /execute/server/time` |
| **副本版本** | `GET /execute/copyVersion/list`· `GET /execute/copyVersion/existedList`· `GET /execute/stepVersionList`· `POST /execute/copy/recordItem`（生成副本） |
| **步骤锁** | `PUT /execute/lock/step`· `DELETE /execute/unLock/step` |
| **附件** | `POST /execute/upload`· `GET /execute/picture/list` |

### `ExecuteAttachmentController`（`@RequestMapping("/execute/attachment")`）

- `POST /upload`（附件上传，经 MinIO）· `POST /addRemark`（附件备注）· `GET /list`

## Service 核心方法

### `ExecuteFormDataService`（核心 · 数据写入与查询）

按业务类别归组：

**数据写入与版本**
| 方法 | 功能 |
|---|---|
| `saveBatch(FormDataBatchSaveDTO)` | 批量录入表单数据 |
| `modify(FormDataModifyDTO)` | 修订（带历史版本） |
| `update(FormDataUpdateDTO)` | 原位更新 |
| `discardRecordItem(FormDataDiscardDTO)` | 废弃记录项 |
| `insertBatch(list)` / `saveResultsAndHandleRelationComponentData(...)` | 批量插入 + 处理关联组件数据（含 `filterNull` 重载） |
| `selectMaxRev(productPlanId, fields)` | 取最大修订号 |
| `existHistoryData(list)` | 是否存在历史数据 |
| `handlePlanModifyCount(productPlanId)` | 维护计划修订计数 |

**副本版本（copyVersion）**
| 方法 | 功能 |
|---|---|
| `copyRecordItem(RecordCopySaveDTO)` | 生成记录项副本（返回新版本号） |
| `getRecordItemLatestData(dto)` | 取记录项最新数据 |
| `getIntactMergedList(dto)` | 完整合并（跨版本）查询 |

**计算**
| 方法 | 功能 |
|---|---|
| `calculateData(saveData, CalculateDataQueryDTO)` | 公式/计算（依赖 process 的 `CalculateDataQueryDTO`） |
| `getCalculationPreview(dto)` | 计算预览（不入库） |
| `getCalculateDate(CalculateDateDTO)` | 日期计算 |

**查询**
| 方法 | 功能 |
|---|---|
| `getFieldList(dto)` / `getDataByPlanAndItemIds(...)` / `selectByProductPlanIdAndItemIds[AndCopyVersions]` | 按计划/项/版本查数据 |
| `componentTrendAnalysis(dto)` | 组件趋势分析 |
| `queryProcedureViewVO(dto)` | 工序视图 |
| `queryPlanModifyList(dto)` | 计划修订统计分页 |
| `getSubsidiaryDocList(id)` | 副记录文档 |
| `selectProcessAndProcedureByFormDataIds(ids)` | 由表单数据反查工序/流程 |

**步骤锁**
| 方法 | 功能 |
|---|---|
| `lockProcedureStep(LockStepDTO)` / `unLockProcedureStep(LockStepDTO)` | 工序步骤加锁/解锁（Redis `LOCK_STEP`） |

### `ExecuteRecordCopyService`（副本版本档案）

| 方法 | 功能 |
|---|---|
| `copyRecordItem(dto)` / `save(copy)` | 创建/保存副本档案 |
| `getCopyVersionList(dto)` / `getVersionMaxValue(dto)` | 版本列表/最大版本号 |
| `existCopy(dto)` | 是否存在副本 |
| `getCurrentStepCopies(dto)` / `getCurrentChangeRecord(planId, stepModelId, copyVersion)` | 当前步骤副本/当前变更记录 |
| `getListByRecordItemIds` / `getListByRecordVersion` / `getByPlanIdList` | 按项/版本/计划查 |
| `discardRecordItem(dto)` | 废弃 |
| `queryStepChangeTeamList(dto)` | 步骤换班列表（接 [[mes-workflow-module]] 换班） |

### `ExecuteFormDataHandleService`

- `fillFormDataAndFilter(FormDataFilterDTO)` — 填充表单数据并过滤（lims 侧也复用同款 DTO，见跨服务）

### `ExecuteCommonService`（执行设备）

| 方法 | 功能 |
|---|---|
| `getExecuteComponentEquipmentList(dto)` | 执行组件的设备列表 |
| `getEquipmentByCode(dto)` | 按编码取设备 |

### `ExecuteAttachmentService`（附件）

- `upload(dto)` / `getList(dto)` / `getListByProductPlanId` / `getListByIdList` / `getListByPlanIdAndItemIdAndStepId` / `saveOrUpdateBatch` / `addRemark(dto)` / `queryByIds`

## 独有机制

### 副本版本（copyVersion）

execute 的核心机制：记录项数据可生成**副本版本**（`bm_execute_record_copy` 记录档案，`bm_execute_form_data.copyVersion` 标记所属版本）。计算上下文用 `CALCULATE_DEFAULT_COPY_VERSION = Integer.MAX_VALUE` 区分。用于"同一记录项多次录入/修订/换班"场景的历史保留。

### 公式计算

`calculateData` / `getCalculationPreview` 依赖 process 子域的公式能力（`CalculateDataQueryDTO` 来自 `service/process/dto/query/`），公式步骤 ID 用占位 `FORMULA_PROCEDURE_STEP_ID = 0`。详见 [[mes-process-module]]。

### 趋势分析

`componentTrendAnalysis` 跨计划/版本聚合组件数据做趋势，是数据查询层的扩展能力。

### 步骤锁

`lockProcedureStep` 用 Redis `bmos:execute:lock:%s` 防止并发录入同一工序步骤。

## 与其它子域 / 服务的耦合点

- **← record**：执行数据按"批记录项（RecordItem）/组件（BatchRecordComponent）"组织（`BatchRecordItemService` / `BatchRecordComponentService`）。详见 [[mes-record-module]]。
- **← process**：步骤模型（`ProcedureStepModel`）、计算（`CalculateDataQueryDTO`）、表达式。详见 [[mes-process-module]]。
- **← plan**：执行数据绑定生产计划（`Plan` / `PlanMapper`）。详见 [[mes-plan-module]]。
- **↔ workflow**：副本版本接换班（`queryStepChangeTeamList`）。详见 [[mes-workflow-module]]。
- **→ lims（跨服务复用）**：`bm_execute_form_data` 被 lims 只读引用（lims 有独立 `ExecuteFormData` entity + ExtInfo 扩展类），是 mes→lims 数据流接口表。**写入只在 mes**。

## AI 定位提示

- 工序表单数据录入/修订/更新/废弃 → `ExecuteFormDataService` 的 `saveBatch/modify/update/discardRecordItem`
- 计算公式不生效 → `calculateData` / `getCalculationPreview`（依赖 process 的 `CalculateDataQueryDTO`，公式步骤 ID=0）
- 副本版本/历史版本错乱 → `ExecuteRecordCopyService` + copyVersion 字段
- 步骤录入被锁/解锁异常 → `lockProcedureStep` / Redis `bmos:execute:lock:%s`
- 趋势分析 / 计划修订统计 → `componentTrendAnalysis` / `queryPlanModifyList`
- 业务组件保存校验 → `saveBusinessComponentsData` / `checkBusinessComponentsSaved`
- 附件 → `ExecuteAttachmentService`（经 MinIO）
- **改 `bm_execute_form_data` 表结构** → ⚠️ 必须同步检查 lims 的 `ExecuteFormData` entity 及 ExtInfo 类（`bmos-lims2-common/.../model/execute/`），避免 lims 读取报错

## 相关页面

- [[mes-overview]] — mes 服务总览（execute 为头部子域；`bm_execute_form_data` 跨服务复用提醒）
- [[mes-record-module]] — 批记录项/组件定义（execute 的数据载体）
- [[mes-process-module]] — 步骤模型与公式计算（execute 的计算依赖）
- [[mes-plan-module]] — 生产计划（execute 数据的归属维度）
- [[mes-workflow-module]] — 换班与副本版本衔接
- [[mes-dataset-module]] — 批记录/批签发文档渲染（装配 execute 的表单数据/附件/副本版本）
- [[mes-inspect-module]] — 检验结果回填业务组件表单数据（`confirmFillFormData`）
- [[service-integration]] — `bm_execute_form_data` 的 mes 写 / lims 读 跨服务数据流
- [[database-schema-overview]] — `bm_execute_*` 表归属与跨服务复用标注
