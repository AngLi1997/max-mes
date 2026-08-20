---
title: MES Dataset 模块（数据集 / 采集点 / 批记录文档渲染）
created: 2026-07-02
updated: 2026-07-02
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/dataset/
status: active
---

# MES Dataset 模块

## 概述 / 职责

Dataset 模块承担**两件事**：

1. **数据集 / 采集点模板定义**：管理数据集（Dataset）、数据集分类（树形）、采集点（DataPoint）、采集点与批记录模板的关联关系——为批记录动态数据填报、批签发引用、动态报表提供"数据点"维度的配置。
2. **批记录 / 批签发文档渲染**：把 execute 的表单数据、附件、副本版本、计划信息等**装配**成可渲染的数据结构，再用 Word（docx）/ Excel（xlsx）占位符替换生成最终批记录文档。

它是 [[mes-overview]] 头部子域之一，是"数据 → 文档"链路的最后一环。

- 包路径：`com.bmos.mes.service.dataset/`
- 规模：**Controller 2 · Service 接口 2 · Mapper 4 · Java 91**
- 关键依赖：execute（表单数据/附件/副本版本）、record（批记录组件）、plan（计划）、process（工艺模型）、lotrelease（批签发）
- 独有机制：**文档渲染引擎**（`DocxRenderUtil` / `XlsxRenderUtil` + 占位符正则 + 12 个替换选项）、**数据装配构建器**（`handle/` 下 10 个 Builder）

> 📋 dataset 91 个 Java 文件中，近半是 `handle/`（装配构建器）、`util/`（渲染）、`util/options/`（替换选项）——单看 2 个 Controller / 2 张主表会严重低估复杂度。这正是 [[PLAYBOOK-backend]] 强调"用 Java 文件数而非 Controller 数"的典型。

## 子包速览（按职责分 6 块）

| 子包 | 职责 |
|---|---|
| **controller** | REST 入口（数据集 CRUD + 分类树 + 数据预览） |
| **service / mapper / model / dto / vo / convert** | 数据集/采集点的标准业务层（CRUD + 预览） |
| **common** | 数据集**转换传输对象**（`DatasetTrans*`，装配中间结构）+ 值数据类型枚举 |
| **enums** | 数据集类型 / 动态报表数据类型 |
| **handle** ★ | **数据装配构建器**（10 个 Builder + data 子包），把分散数据组装成渲染所需结构 |
| **util** ★ | **文档渲染**：`DocxRenderUtil` / `XlsxRenderUtil` / `ImageUtil` / 占位符常量 + `options/` 替换选项 |

## 数据模型（4 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_dataset` | `Dataset` | 数据集主表（类型 `DatasetType`、key、绑定工艺 processId） |
| `bm_dataset_category` | `DatasetCategory` | 数据集分类（树形） |
| `bm_dataset_point` | `DatasetPoint` | 采集点（绑定字段 fieldId + 工序步骤 procedureStepId） |
| `bm_dataset_point_template_relation` | `DatasetPointTemplateRelation` | 采集点 ↔ 批记录模板关联 |

> 数据集按 `processId + DatasetType` 维度组织；采集点按 `fieldId + procedureStepId` 定位（与 execute 表单数据对齐）。

## 关键枚举

### `DatasetType`（数据集类型 · 在 `dataset/enums/`）

| code | 中文 | 用途 |
|---|---|---|
| `POINT` | 批记录数据（数据点） | 标准采集点数据集 |
| `LOT_RELEASE_LINK` | 批签发引用 | 关联批签发文档（接 [[mes-requisition-module]] 同级的 lotrelease 子域） |
| `DYNAMIC_REPORT` | 动态数据填报 | 运行时动态填报的数据（类型见下） |

### `DatasetDynamicReportDataType`（动态报表数据类型）

| code | 中文 |
|---|---|
| `NUMBER` | 数值 |
| `TEXT` | 文本 |
| `DATE` | 日期 |

### `DatasetTransValueDataType`（转换值数据类型 · 在 `common/enums/`）

渲染时区分值的呈现形态：

| code | 中文 |
|---|---|
| `ERROR` | 错误 |
| `TEXT` | 文本 |
| `IMAGE` | 图片 |
| `EXCEL` | Excel 引用 |
| `CHECKBOX` | 选择框 |
| `TAKE_PHOTO` | 拍照组件 |
| `IMAGE_CAPTION` | 图片（含图注） |

> 这个枚举决定了 `util/options/` 里该用哪个替换选项（文本→`DocxTextReplaceOption`、图片→`DocxImageReplaceOption`、拍照→`DocxTakePhotoPicReplaceOption` 等）。

## Controller（2 个）

### `DatasetController`（`@RequestMapping("/dataset")`）

| 类别 | 接口 |
|---|---|
| **数据集 CRUD** | `POST /dataset/createDataset` · `POST /dataset/editDataset` · `DELETE /dataset/delete` · `GET /dataset/queryDatasetDetail` |
| **查询** | `GET /dataset/queryDatasetPage` · `GET /dataset/queryDatasetPointPage`（采集点分页）· `GET /dataset/queryDatasetListByProcessId`（按工艺+类型查） |
| **数据预览** | `POST /dataset/previewDatasetPointData`（分页预览）· `POST /dataset/previewDatasetPointDataList`（列表预览） |

### `DatasetCategoryController`（`@RequestMapping("/dataset/category")`）

- `POST /createCategory` · `PUT /editCategory` · `DELETE /delete` · `GET /tree`（分类树）

## Service 核心方法

### `IDatasetService`（数据集 + 采集点）

| 方法 | 功能 |
|---|---|
| `createDataset(dto)` / `editDataset(dto)` / `deleteDataset(id)` | 数据集 CRUD（含采集点） |
| `queryDatasetPage` / `queryDatasetPointPage` / `queryDatasetDetail` | 分页/详情 |
| `queryByProcessIdAndType(processId, type)` | 按工艺 + 类型查数据集 |
| `queryByDataSetKeyList(keys)` | 按 key 批量查 |
| `listByFieldIdsAndProcedureStepIds(fieldIds, stepIds)` | 按字段 + 步骤查采集点 |
| `previewDatasetPointData(dto)` / `previewDatasetPointListData(dto)` | ★ **采集点数据预览**（装配 execute 表单数据成预览结构） |

### `IDatasetCategoryService`（分类树）

- `createCategory` / `editCategory` / `deleteCategory` / `queryCategoryTree`

> 数据集 Service 方法不多，但 `previewDatasetPointData` 背后调用了整个 `handle/` 装配体系——业务复杂度在 handle 而非 Service 签名。

## 独有机制

### 1. 数据装配构建器（`handle/`，★ 核心复杂度所在）

10 个 Builder + `handle/data/` 下的数据结构，职责是**把分散在 execute/record/plan/process 的数据装配成渲染所需中间结构**：

| Builder | 装配内容 |
|---|---|
| `AssemblePrepareBuilder` / `AssembleDataBuilder` | 组装前置数据 + 主数据（计划、工艺绑定关系） |
| `DataSetProcessBuilder` | 数据集工艺流程装配 |
| `FormLoadingBuilder` | 加载数据集 + 数据集与表单数据关联 |
| `DatasetTransBuilder` / `DatasetTransDataBuilder` | 转换传输结构（含复制数据、动态渲染、批签发链接装载到批次） |
| `PlanAttachmentBuilder` | 拍照取证附件（按工艺/版本/记录项/是否复用查步骤信息） |
| `PlanCopyVersionBuilder` | 批次所有复制版本（接 execute 的 copyVersion） |
| `PlanBatchDocumentHandler` | ★ **批记录/批签发文档处理总入口** |

> `handle/data/` 是配套的数据持有类：`PlanLoadingData` `ExecuteFormLoadingData` `AssemblePrepareData` `AssembleCompleteData` `PlanCopyVersion` `PlanChangeTeamCopyVersion` `PlanAttachment` `PlanBatchDocumentData` `RenderTemplateData` `DynamicRenderingData` `DataSetProcess` `DataSetPointHandleData` 等。

> 改"批记录文档生成"逻辑 → 从 `PlanBatchDocumentHandler` 入手，按 Builder 链路追。详见本文末 AI 定位提示。

### 2. 文档渲染引擎（`util/`）

| 类 | 职责 |
|---|---|
| `DocxRenderUtil` | Word（docx）渲染：扫描占位符 + 按类型替换 |
| `XlsxRenderUtil` | Excel 渲染（基于 **Apache POI**：`XSSFWorkbook` / `SXSSFWorkbook` 流式 / `HSSFWorkbook`） |
| `ImageUtil` | 图片处理（缩放/图注等） |
| `PlaceholderConstants` | ★ **占位符正则**：`${(标识)[n][n][n][n]([n])}`（4~5 个索引段） |
| `DocxRenderConstants` | 批记录生成占位符常量 |

**占位符正则**（`PlaceholderConstants.PATTERN`）：

```
\$\{\((.\d*\.\d*[^)]+)\)(\[\d*]){4,5}}
```

形如 `${(标识)[0][1][2][3]}` 或 `${(标识)[0][1][2][3][4]}` —— 括号内是数据点标识，方括号是索引（批次/版本/项/字段等维度）。改占位符格式必须同步改正则与前端模板。

### 3. 替换选项体系（`util/options/`，12 个）

按 `DatasetTransValueDataType` 分发，每个选项处理一种占位符替换：

| 替换选项 | 处理类型 |
|---|---|
| `DocxTextReplaceOption` | 文本 |
| `DocxErrorReplaceOption` | 错误 |
| `DocxImageReplaceOption` / `DocxImageCaptionReplaceOption` | 图片 / 图片含图注 |
| `DocxCheckboxReplaceOption` | 选择框 |
| `DocxEvidencePhotoReplaceOption` / `DocxPatternEvidencePhotoReplaceOption` | 拍照取证（普通/模式） |
| `DocxTakePhotoPicReplaceOption` / `DocxTakePhotoLegendReplaceOption` / `DocxPatternTakePhotoPicReplaceOption` | 拍照图片（普通/图例/模式） |

配套数据类：`ChangeNumberPhotoData` / `ProcedureTakePhotoData` / `ProcessTakePhotoData` / `DocxRenderConstants`。

> 这是典型的**策略体系**（同 [[mes-record-module]] 的 ComponentStrategy）：新增一种占位符呈现形态 → 加一个 `Docx*ReplaceOption` + 对应 data 类，并在分发处登记。**只列入口，不展开实现**。

## 与其它子域 / 服务的耦合点

- **← execute**：表单数据（`ExecuteFormData`）、附件（`ExecuteAttachment` / `ExecuteAttachmentMapper`）、副本版本（`ExecuteRecordCopy`）。详见 [[mes-execute-module]]。
- **← record**：批记录组件（`BatchRecordComponent` / `BatchRecordComponentService`）。详见 [[mes-record-module]]。
- **← plan**：生产计划（`Plan` / `PlanService`）。详见 [[mes-plan-module]]。
- **← process**：工艺模型（`Process` / `ProcedureModel` / `ProcedureStepModel`）。详见 [[mes-process-module]]。
- **← lotrelease**：批签发引用（`DatasetType.LOT_RELEASE_LINK`）。
- **→ MinIO**：附件/图片读取（经平台 MinIO 能力，图片渲染前需取图）。

## AI 定位提示

- 数据集/采集点配置 CRUD → `IDatasetService` / `IDatasetCategoryService`（`/dataset/**`）
- **批记录文档生成不正确** → 从 `PlanBatchDocumentHandler` 入手 → 追 `handle/` Builder 链路 → 到 `util/` 渲染
- 占位符没替换上 → `PlaceholderConstants.PATTERN` 正则 + `Docx*ReplaceOption` 分发（按 `DatasetTransValueDataType` 选）
- Excel 渲染问题 → `XlsxRenderUtil`（POI，注意 SXSSF 流式 vs XSSF 内存）
- 图片/拍照取证渲染 → `DocxEvidencePhoto*ReplaceOption` / `DocxTakePhoto*ReplaceOption` + `PlanAttachmentBuilder`
- 副本版本数据缺失 → `PlanCopyVersionBuilder`（接 execute copyVersion）
- 动态报表填报类型 → `DatasetDynamicReportDataType`（数值/文本/日期）
- 新增占位符呈现形态 → 加 `Docx*ReplaceOption` + data 类 + 分发登记，**不要改现有选项**

## 相关页面

- [[mes-overview]] — mes 服务总览（dataset 为头部子域）
- [[mes-execute-module]] — 表单数据/附件/副本版本（dataset 装配的数据源）
- [[mes-record-module]] — 批记录组件与模板（dataset 采集点关联）
- [[mes-plan-module]] — 生产计划（dataset 渲染的归属维度）
- [[mes-process-module]] — 工艺模型（数据集按工艺组织）
- [[service-integration]] — MinIO 文件取用
- [[database-schema-overview]] — `bm_dataset*` 表归属
