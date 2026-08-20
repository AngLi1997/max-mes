---
title: MES LotRelease 模块（批放行 / 批签发 · 制药质量门禁）
created: 2026-07-02
updated: 2026-07-02
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/lotrelease/
status: active
---

# MES LotRelease 模块

## 概述 / 职责

LotRelease（批放行 / 批签发）模块是 mes 的**制药质量门禁**：管理批放行**模板**（含版本、分类、工艺绑定）和批放行**单据**（生成、提交审核、审批回调、作废、下载），把生产计划的数据按模板渲染成批签发文档，经审核流程后生效。是批次放行出厂前的最后质量关卡。

- 包路径：`com.bmos.mes.service.lotrelease/`
- 规模：**Controller 3 · Service 接口 3 · Mapper 7 · Java 61**
- 关键依赖：dataset（渲染引擎 `XlsxRenderUtil` + 装配结构 `AssembleCompleteData` + 采集点关联）、plan（计划）、process（工艺）、product（物料分类树）
- 独有机制：**审批回调**（`auditCallback`）、**模板版本管理**（默认版本/确认/作废）、Excel 上传下载

> 📋 模块分两个对等子包 `manage`（单据侧）和 `template`（模板侧），各有独立 controller/service/mapper/model/enums。Java 文件数 61 但表 7 张、枚举 4 个——状态机和数据模型密度高。

## 子包速览（2 个对等子包）

| 子包 | 职责 | Controller |
|---|---|---|
| **manage** | 批放行**单据**：生成（按计划+模板渲染）、提交审核、审批回调、作废、版本查询、Excel 上传下载 | `LotReleaseController`（`/lotRelease/manage`） |
| **template** | 批放行**模板**：模板 CRUD、版本管理（确认/默认/作废）、分类树、工艺绑定 | `LotReleaseTemplateController`（`/lotRelease/template`）+ `LotReleaseTemplateCategoryController`（`/lotRelease/template/category`） |

## 数据模型（7 张表）

### manage（单据侧）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_lot_release` | `LotRelease` | 批放行单据主表（状态 `LotReleaseStatus`、关联计划/模板版本） |
| `bm_lot_release_history` | `LotReleaseHistory` | 批放行操作历史（生成/审核/作废/上传等流水） |

### template（模板侧）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_lot_release_template` | `LotReleaseTemplate` | 批放行模板主表 |
| `bm_lot_release_template_version` | `LotReleaseTemplateVersion` | 模板版本（状态 `LotReleaseTemplateVersionStatus`，含默认版本标记） |
| `bm_lot_release_template_category` | `LotReleaseTemplateCategory` | 模板分类（树形） |
| `bm_lot_release_template_process` | `LotReleaseTemplateProcessRelation` | 模板 ↔ 工艺绑定关系 |
| `bm_lot_release_template_history` | `LotReleaseTemplateHistory` | 模板操作历史 |

> 模板按"分类树 + 版本 + 工艺绑定"三维组织；单据生成时按计划的工艺找到绑定模板的默认版本渲染。

## 关键枚举（4 个，状态机丰富）

### `LotReleaseStatus`（批放行单据状态 · manage）

| code | 中文 | 流转方向 |
|---|---|---|
| `EDIT` | 编辑 | 初始态（生成后） |
| `PROCESSING` | 审批中 | `submit` 提交审核后 |
| `EFFECTIVE` | 生效 | `auditCallback(pass=true)` 审核通过 |
| `SCRAPED` | 作废 | `scrap` 作废 |

> 流转：EDIT →（submit）→ PROCESSING →（auditCallback）→ EFFECTIVE / SCRAPED

### `LotReleaseOperateType`（批放行操作类型 · 单据历史分类）

| code | 中文 |
|---|---|
| `GENERATE` | 生成批签发 |
| `RE_GENERATE` | 重新生成 |
| `SUBMIT` | 提交审核 |
| `PASS` | 审核通过 |
| `NOT_PASS` | 审核不通过 |
| `SCRAP` | 作废 |
| `DOWNLOAD` | 下载 |
| `UPLOAD` | 上传 |

### `LotReleaseTemplateVersionStatus`（模板版本状态 · template）

| code | 中文 |
|---|---|
| `EDIT` | 编辑 |
| `MAKE_SURE` | 确认 |
| `SCRAP` | 作废 |

### `LotReleaseTemplateOperateType`（模板操作类型 · 模板历史分类）

| code | 中文 |
|---|---|
| `CREATE` | 新增模板 |
| `CREATE_VERSION` | 新增版本 |
| `UPLOAD` / `DOWNLOAD` | 上传 / 下载 |
| `VALIDATE` | 验证 |
| `MAKE_DEFAULT` | 设为默认 |
| `MAKE_SURE` | 确认 |
| `SCRAP` | 作废 |

## Controller（3 个）

### `LotReleaseController`（`/lotRelease/manage`，单据侧）

| 类别 | 接口 |
|---|---|
| **生成** | `POST /generate`（生成批签发，返回渲染结果）· `GET /getGeneratePreviewList`（预览）· `POST /getDynamicReportItem`（动态报表项） |
| **审核流程** | `PUT /submit`（提交审核）· `PUT /scrap`（作废）·（审批回调走 service `auditCallback`，由审批引擎回调） |
| **查询** | `GET /productTree`（按模板查产品分类树）· `GET /queryPlanPage`（可放行计划分页）· `GET /queryPage`（单据分页）· `GET /queryVersionPage`（版本分页）· `GET /queryAuditPage`（审核分页）· `GET /history`（操作历史） |
| **Excel** | `POST /uploadExcel`· `POST /updateExcelFile`· `POST /download`· `POST /downloadByUrl` |

### `LotReleaseTemplateController`（`/lotRelease/template`，模板侧）

| 类别 | 接口 |
|---|---|
| **模板/版本 CRUD** | `POST /createTemplate`· `POST /createTemplateVersion`· `POST /updateTemplateFile`· `POST /uploadTemplate`· `POST /downloadTemplate` |
| **版本操作** | `PUT /makeSure`（确认）· `PUT /makeDefault`（设默认）· `PUT /scrap`（作废） |
| **工艺绑定** | `PUT /bindProcess`· `GET /listByProcessId`· `GET /listProcessIdByTemplateId`· `GET /listVersionByTemplateId` |
| **查询** | `GET /queryPage`· `GET /queryVersionPage`· `GET /history` |

### `LotReleaseTemplateCategoryController`（`/lotRelease/template/category`）

- `POST /createCategory` · `PUT /editCategory` · `DELETE /delete` · `GET /tree`

## Service 核心方法

### `ILotReleaseService`（单据侧 · 含审批回调）

| 方法 | 功能 |
|---|---|
| `generate(LotReleaseGenerateDTO)` | ★ 生成批签发（按计划+模板渲染，返回渲染结果字符串） |
| `getGeneratePreviewList(processId)` | 生成预览（按工艺查可用模板/数据集） |
| `renderTemplate(AssembleCompleteData)` | ★ **渲染模板**（接收 dataset 装配好的 `AssembleCompleteData`，复用 `XlsxRenderUtil`） |
| `submit(id)` | 提交审核（EDIT → PROCESSING） |
| `auditCallback(id, pass, comment, auditorId)` | ★ **审批回调**（PROCESSING → EFFECTIVE/SCRAPED，由审批引擎回调） |
| `scrap(id)` | 作废 |
| `updateExcelFile(dto)` / `getDynamicReportItem(dto)` | Excel 更新 / 动态报表项 |
| `queryPlanPage` / `queryPage` / `queryVersionPage` / `queryAuditPage` | 各维度分页查询 |
| `getLotReleaseProductTreeByTemplateId(categoryType, templateId)` | 按模板查产品分类树 |
| `showHistory(id)` | 操作历史 |
| `selectAuditBusinessKey(deptIdList)` | 取审批业务 key（接审批引擎） |
| `selectOneById(businessId)` | 按业务 ID 查单条 |

### `ILotReleaseTemplateService`（模板侧）

| 方法 | 功能 |
|---|---|
| `createTemplate(dto)` / `createTemplateVersion(dto)` | 新建模板 / 新建版本 |
| `updateTemplateFile(dto)` | 更新模板文件 |
| `bindProcess(dto)` | 绑定工艺（模板 ↔ 工艺多对多） |
| `makeSure(id)` / `makeDefault(id)` / `scrap(id)` | 版本确认 / 设默认 / 作废 |
| `listByProcessId(processId)` / `listProcessIdByTemplateId(templateId)` | 工艺 ↔ 模板互查 |
| `listVersionByTemplateId(templateId)` | 模板的版本列表 |
| `queryPage` / `queryVersionPage` / `showHistory` | 分页/历史 |

### `ILotReleaseTemplateCategoryService`（分类树）

- `createCategory` / `editCategory` / `deleteCategory` / `queryCategoryTree`

## 独有机制

### 审批回调（质量门禁核心）

批放行的生效必须经审批：`submit` 提交后状态转 `PROCESSING`，由审批引擎（platform 侧）回调 `auditCallback(id, pass, comment, auditorId)` 决定 `EFFECTIVE`（通过）或维持/退回。`selectAuditBusinessKey` 提供业务 key 给审批引擎注册。详见 [[service-integration]] 与 [[platform-overview]]。

### 模板版本管理

模板支持多版本，每个版本有 `EDIT/MAKE_SURE/SCRAP` 三态；一个模板可设一个**默认版本**（`makeDefault`），单据生成时默认取默认版本。改默认版本会影响后续新生成单据的渲染基准。

### 渲染复用 dataset

`renderTemplate(AssembleCompleteData)` 直接接收 [[mes-dataset-module]] 装配好的 `AssembleCompleteData`（来自 dataset 的 `handle/AssembleDataBuilder`），复用 `XlsxRenderUtil` 渲染——即**批签发文档与批记录文档共用同一套装配+渲染引擎**，只是模板和数据集类型不同（`DatasetType.LOT_RELEASE_LINK`）。

### Excel 上传下载

模板和单据都支持 Excel 文件上传（`uploadTemplate`/`uploadExcel`，经 MinIO）与下载，用于模板文件维护和生成的批签发文档导出。

## 与其它子域 / 服务的耦合点

- **← dataset**：渲染引擎（`XlsxRenderUtil`）、装配结构（`AssembleCompleteData`）、采集点关联（`IDatasetPointTemplateRelationMapper`）。**强耦合**——批签发渲染完全建立在 dataset 之上。详见 [[mes-dataset-module]]。
- **← plan**：批放行单据绑定生产计划（`Plan` / `PlanMapper`）。详见 [[mes-plan-module]]。
- **← process**：模板按工艺绑定（`Process` / `ProcessMapper`）。详见 [[mes-process-module]]。
- **← product**：产品分类树（`ProductCategoryTreeNodeVO` / `ProductMaterialService`）。详见 [[mes-product-module]]。
- **← weigh**：复用树工具（`BmosTreeUtil` / `BmosTreeNode`，来自 weigh centre config）。
- **↔ 审批引擎（platform）**：`submit` / `auditCallback` / `selectAuditBusinessKey` 对接审批。

## AI 定位提示

- 批签发生成/重新生成不对 → `LotReleaseService.generate` / `renderTemplate`（追到 dataset 的 `AssembleCompleteData` 装配 + `XlsxRenderUtil` 渲染）
- 审批状态卡住 → `LotReleaseStatus` 状态机 + `auditCallback`（确认审批引擎是否回调、pass 参数）
- 模板版本/默认版本问题 → `LotReleaseTemplateVersionStatus` + `makeDefault` / `makeSure`
- 模板找不到/工艺绑定错 → `bindProcess` / `listByProcessId` / `listProcessIdByTemplateId`
- 产品分类树不对 → `getLotReleaseProductTreeByTemplateId`（依赖 product）
- Excel 上传/下载 → `upload*` / `download*`（经 MinIO）
- 操作历史追溯 → `showHistory`（单据用 `LotReleaseOperateType`，模板用 `LotReleaseTemplateOperateType`）

## 相关页面

- [[mes-overview]] — mes 服务总览（lotrelease 为头部子域，制药质量门禁）
- [[mes-dataset-module]] — 装配 + 渲染引擎（lotrelease 强依赖，共用 `AssembleCompleteData` + `XlsxRenderUtil`）
- [[mes-plan-module]] — 生产计划（批放行单据归属）
- [[mes-process-module]] — 工艺（模板绑定维度）
- [[mes-product-module]] — 物料分类树（产品树查询）
- [[service-integration]] — 审批引擎回调链 + MinIO 文件
- [[platform-overview]] — 审批引擎上游
- [[database-schema-overview]] — `bm_lot_release*` 表归属
