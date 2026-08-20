---
title: MES Record 模块（批记录）
created: 2026-06-29
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis, audit]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/record/
status: active
---

# MES Record 模块（批记录）

## 概述 / 职责

Record 模块管理**批记录**（Batch Record）——制药/制造业核心**合规文档**，记录一个批次从投料到产出的全部执行细节。是 mes 业务复杂度最高的子域之一。

- 包路径：`com.bmos.mes.service.record/`
- 规模：4 Controller / 9 Mapper / 9 张表 / 8 Service / **57 个组件策略类** / 内置 Word 文档解析
- 与 audit-engine-starter 强绑（变更全留痕）
- 通过 `bm_batch_record_version` 与 lims 形成数据流（lims 只读复用）

## 数据模型（9 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_batch_record` | `BatchRecord` | 批记录主表 |
| `bm_batch_record_version` | `BatchRecordVersion` | **批记录版本**（核心：版本即审计快照）⚠️ lims 复用 |
| `bm_batch_record_category` | `BatchRecordCategory` | 批记录分类 |
| `bm_batch_record_product` | `BatchRecordProduct` | 批记录与产品的绑定关系 |
| `bm_batch_record_item` | `BatchRecordItem` | 批记录条目（一份记录的最小单位） |
| `bm_batch_record_component` | `BatchRecordComponent` | 批记录**组件**（如配料组件、称量组件、签名组件） |
| `bm_batch_record_component_detail` | `BatchRecordComponentDetail` | 组件明细配置 |
| `bm_batch_record_expression` | `BatchRecordExpression` | 表达式绑定（公式/规则） |
| `bm_batch_record_parse` | `BatchRecordParse` | 文档解析结果缓存 |

> 关键字段约定：`bm_batch_record` / `bm_batch_record_component*` 使用 `autoResultMap = true`，含 JSON 字段（组件配置）。
> ⚠️ 跨服务复用：`bm_batch_record_version` 在 lims 中只读引用，**写入归属 mes**。

## 关键枚举

### RecordStateEnum（批记录状态）

| 枚举 | code | value | 说明 |
|---|---|---|---|
| `EDIT` | 编辑 | `"1"` | 编辑中 |
| `AUDIT` | 审批 | `"2"` | 审批中 |
| `CERTAIN` | 确定 | `"3"` | 已确定（生效） |
| `CANCEL` | 作废 | `"4"` | 作废 |

> 状态机：EDIT → AUDIT → CERTAIN，任意状态可走 CANCEL。

### 其它枚举

- `RecordFormatType` — 文档格式化类型（HYPER_LINK / BOOKMARKS / HIDDEN_TEXT / DISTRACT_TEXT / TEXT_WRAPPING / TABLE_TEXT_ROTATION）——Word 文档转换时的兼容性标记
- `ComponentFormulaTypeEnum` — 公式组件类型

## Controller（4 个）

| Controller | 角色 |
|---|---|
| `BatchRecordController` | 批记录主入口（编辑/查询） |
| `BatchRecordManageController` | 批记录管理（生命周期、版本、复制） |
| `BatchRecordAuditController` | 批记录审批 |
| `BatchRecordFeignController` | 实现对外 Feign（被调入口） |

## Service 核心方法

### BatchRecordService（主服务）

| 方法 | 功能 |
|---|---|
| `fileUpload(file)` | **上传 Word 文档**作为批记录模板（入口） |
| `saveRecord(dto)` | 保存批记录 |
| `getRecordPage(query)` | 分页查询 |
| `updateVersion(dto)` | 升级版本 |
| `recordItemUpload(file)` | 上传条目（Word） |
| `copyRecordItem(itemId, name)` / `deleteRecordItem(itemId)` | 条目复制/删除 |
| `queryListRecordByProductId(productId)` | 查产品绑定的批记录 |
| `saveSingleItem(dto)` / `editSingleItem(dto)` | 单条目编辑 |
| `bindExpression(dto)` / `expressionBindBatchRecord(dto)` | **表达式绑定**（公式/校验） |
| `getExpressionTreeByRecordId(id)` / `getRecordTreeByExpressionId(id)` | 表达式与批记录的双向查询 |
| `downloadByUrl(response, url)` | 文档下载 |

### BatchRecordItemService（条目）

`saveOrUpdateItem` / `selectItemList(versionId)` / `selectItem(id)` / `queryByRecordId(versionId)` / `selectItemMaxSort(versionId)` / `getHeaderFooterByRecordVersionIds(versionIds)` 等——大量按 `versionId` 维度的查询，说明**条目以版本为隔离边界**。

### 其它 Service

- `BatchRecordCategoryService` — 分类管理
- `BatchRecordProductService` — 产品绑定（与 [[mes-product-module]] 的 `bindBatchRecords` 对接）
- `BatchRecordVersionService` — 版本管理
- `BatchRecordComponentService` — 组件管理（依赖策略体系）
- `BatchRecordParseService` — 文档解析（依赖 Word 解析工具）
- `BatchRecordManageService` — 批记录生命周期管理

## 组件策略体系（57 个 ComponentStrategy）

批记录由一系列**组件**（Component）组成，每种组件用一个策略类处理填充。**全部实现同一接口**：

### BusinessComponentStrategy（策略接口）

入口：`record/business/BusinessComponentStrategy.java`

```java
public interface BusinessComponentStrategy {
    void handleBusinessComponent(
        List<ExecuteFormData> results,        // 组装的执行表单数据
        ComponentListVO component,            // 要处理的组件
        ProductionDetailInfo info,            // 生产详细信息
        Map<Long, BusinessComponentConfigDetailVO> configMap,  // 组件配置
        Integer index
    );
}
```

### 策略实现（57 个，分类速览，不展开具体逻辑）

| 类别 | 代表策略 | 数量 |
|---|---|---|
| 批拣选 BatchPick | `BatchPickComponentStrategy` `BatchPickMaterialBatchComponentStrategy` `BatchPickMaterialSumComponentStrategy` | 3 |
| 料头回收 ChargeRecycle | `ChargeRecycle*Strategy`（Component / Detail / Sum） | 3 |
| 清洁 Clean | `CleanCheckComponentStrategy` `CleanExecuteComponentStrategy` `CleanInfoComponentStrategy` | 3 |
| 设备 Equipment | `EquipmentCodeComponentStrategy` `EquipmentInfoComponentStrategy` `EquipmentNameComponentStrategy` `EquipmentCustomFieldComponentStrategy` `EquipmentExpandTableStrategy` 等 | 8 |
| 成品产出 FinishedProductOutput | `FinishedProductOutput*Strategy`（Component / Detail / Summary） | 3 |
| 公式 Formula | `FormulaInfoComponentStrategy` `FormulaMaterialComponentStrategy` | 2 |
| 配料投料 Ingredient | `Ingredient*Strategy`（Input/Plan/Weigh × Component/Detail/Summary） | 8 |
| 液体计量 LiquidMeasure | `LiquidMeasure*Strategy`（Component / Detail / Summary） | 3 |
| 领料 MaterialPick | `MaterialPick*Strategy`（Component / Material） | 2 |
| 物料预留 MaterialReserve | `MaterialReserve*Strategy`（Component / Batch / Summary） | 3 |
| 产出称量 OutputWeigh | `OutputWeigh*Strategy`（Component / Detail / Summary） | 3 |
| 领料签收 PickingReceiving | `PickingReceiving*Strategy`（Component / Batch / MaterialSum） | 3 |
| 制剂 Preparation | `Preparation*Strategy`（Input/Plan/Produce × Component/Detail/Summary/Batch/Sum） | 12 |
| 生产信息 Production | `ProductionInfoComponentStrategy` | 1 |
| 签名 Sign | `UserReviewSignComponentStrategy` `UserSubmitSignComponentStrategy` | 2 |

> AI 提示：定位某种组件处理逻辑，直接看对应 `*ComponentStrategy.handleBusinessComponent(...)`。新增组件类型 = 新增一个 Strategy 实现类。

### 策略入参的辅助模型

`record/business/model/` 下：`ProcessDetailInfo` `ProductionDetailInfo` `ProductFormulaInfo` `StorageMaterialDetailInfo` `HandleSignInfo` `ExpandTableInfo`，以及 `preparation/` 子目录的 `PreparationInput*` `PreparationProduce*`（制剂场景特化）。

## 公式引擎

入口：`record/model/formula/` 包

- `FormulaConfig` — 公式配置基类
- `ComponentFormulaConfig` — 组件级公式
- `AssociationPatternConfig` — 关联模式
- `DateCalculateConfig` — 日期计算
- `NumericalJudgmentConfig` — 数值判断
- `StringJoinConfig` — 字符串拼接

> 与 platform 的 `PlatformExpressionFeignClient` 联动（mes 调 platform 的表达式服务），是表达式绑定能力的来源。

## Word 文档解析

入口：`record/DocxValidator.java` 与 `record/util/DocxSplitUtil2.java`

> 用于批记录模板的 Word 文档解析与拆分。详细解析逻辑略，需要时直接读源码。底层依赖：`docx4j 8.3.8/8.3.9`（见 mes pom）。

## Redis Key 规则

入口：`record/redis/RecordRedisKeyDefine.java` — 批记录相关的 Redis 键命名集中定义。

## AI 定位提示

- **改组件处理逻辑** → `record/business/strategy/<类别>*ComponentStrategy.java`
- **加新组件类型** → 实现 `BusinessComponentStrategy` 接口
- **批记录状态流转问题** → 查 `RecordStateEnum`（1→2→3，可走 4）
- **版本相关查询** → `BatchRecordVersionService` + `bm_batch_record_version`
- **Word 上传/下载问题** → `BatchRecordService.fileUpload` / `DocxValidator` / `DocxSplitUtil2`
- **公式/表达式问题** → `BatchRecordExpression` + `model/formula/` + `PlatformExpressionFeignClient`
- **跨服务（lims 报错说找不到 record_version）** → 确认 lims 是否在做只读复用，写入归属仍在 mes

## 相关页面

- [[mes-overview]] — mes 服务总览
- [[mes-product-module]] — 产品/物料主数据（通过 `bindBatchRecords` 与本模块绑定）
- [[mes-execute-module]] — 批记录项/组件的执行数据写入方（记录项数据载体）
- [[mes-inspect-module]] — 检验结果回填业务组件（`confirmFillFormData`）
- [[service-integration]] — 跨服务 Feign（含 lims/wms 调 mes 的检验回调）
- [[database-schema-overview]] — `bm_batch_record_*` 9 张表全景
- [[platform-overview]] — 表达式/单号规则等基础能力来源
