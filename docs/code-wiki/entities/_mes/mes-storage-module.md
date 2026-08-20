---
title: MES Storage 模块（mes 端储位/物料批次/库存）
created: 2026-07-02
updated: 2026-07-06
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/storage/
status: active
---

# MES Storage 模块

## 概述 / 职责

Storage 模块是 mes 端的**物料/批次/库存视图**：管理储位与货位配置、物料批次档案、物料库存（入库/出库/移库/退库/盘点/预定/拆包）、投料充电与回收、物料批次动态字段，以及物料临期预警。它是 [[mes-overview]] 头部子域之一，与 **wms 的库存概念必须区分**（见下方"与 wms 的边界"）。

- 包路径：`com.bmos.mes.service.storage/`
- 规模：**Controller 8 · Service 接口 8 · Mapper 9 · Java 107**
- 关键依赖：枚举在 `bmos-mes-common`（`StorageOperateTypeEnum` / `MaterialQualityStatusEnum` / `WeighSignStatus`），不在本包内
- 独有机制：物料临期定时任务 `MaterialExpireForeWarningJob`、编号序列 `getSerial`

> ⚠️ **与 wms 的边界**：storage 是 **mes 工序现场**的物料/批次视图（储位 → 货位 → 物料 → 批次 → 预定），服务于称量/配料/产出等工序作业；wms 是**仓库管理**的库位/出入库单据视图。两者表与模型不共享，仅通过 [[mes-requisition-module]] 的 `WmsFeignClient`（领料）单向衔接。详见 [[service-integration]]。

## 子包速览（3 个）

storage 内部按职责分三个子包，建页时按这三块组织：

| 子包 | 职责 | 核心表 | 核心 Service |
|---|---|---|---|
| **config** | 储位/货位**配置**（树形储位 + 货位 + 权限绑定） | `bm_storage` `bm_cargo_position` | `IStorageConfigService` `ICargoPositionService` |
| **manage** ★ | 物料批次/库存**作业**（CRUD + 入/出/移/退/盘/预定/拆包 + 充电回收 + 临期） | `bm_storage_material*` `bm_charge_recycle` 等 6 张 | `IStorageMaterialService` 等 5 个 |
| **log** | 储位物料**操作日志**（流水追溯） | `bm_storage_material_position_log` | `IStorageMaterialPositionLogService` |

> `manage` 是业务重心：8 个 Service 中 5 个在此，9 张表中 6 张在此。

## 数据模型（9 张表）

### config（储位/货位配置）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_storage` | `Storage` | 储位主表，**树形结构**（parentId 自关联，`IStorageMapper` 有 `queryAllChildren`/路径拼接逻辑） |
| `bm_cargo_position` | `CargoPosition` | 货位（绑定到储位下，含启用/禁用、编码、权限用户） |

### manage（物料批次/库存作业）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_storage_material` | `StorageMaterial` | ★ 物料库存主表（可用量/预定量/消耗量；含 `availableValidate`/`outboundValidate` 校验、`WeighSignStatus` 签收态） |
| `bm_storage_material_batch` | `StorageMaterialBatch` | 物料批次档案（批次号、质量状态 `MaterialQualityStatusEnum`、有效期；过期/不可用会抛异常） |
| `bm_material_batch_field` | `MaterialBatchField` | 批次动态扩展字段（FieldType/FieldName/FieldValue，存储在 `entity/` 子目录） |
| `bm_storage_material_reserve` | `StorageMaterialReserve` | 物料预定记录（按生产计划 productPlanId 维度预定/取消预定） |
| `bm_storage_material_charge_recycle` | `StorageMaterialChargeRecycle` | 投料充电/回收记录（投料与回收流水） |
| `bm_charge_recycle` | `ChargeRecycleComponent` | 投料回收的组件维度汇总（按组件查充电列表/回收列表） |

### log（操作日志）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_storage_material_position_log` | `StorageMaterialPositionLog` | 储位物料操作流水（按 `StorageOperateTypeEnum` 记录每类作业，`log/` 子包查询） |

> 与 [[mes-overview]] 中 `bm_execute_form_data*`（mes 写 / lims 读）不同，storage 表均在 mes 内部，无跨服务复用。

## 关键枚举（状态机，在 mes-common）

> storage 自身**不含枚举目录**，状态机依赖 `bmos-mes-common` 的三个枚举。这是排查"物料状态卡住/操作类型"问题的速查键。

### `StorageOperateTypeEnum`（物料作业类型 · 入库日志分类）

定义在 `bmos-mes-common/.../enums/storage/StorageOperateTypeEnum.java`，是 storage 写日志和分类的**中枢枚举**，含 39 个值。每个值四元组：`(code, showType, 中文名, 操作/复核人称谓)`。按业务类别分组：

| 业务类别 | 枚举值 | 中文 | 展示类（showType） |
|---|---|---|---|
| **基础库内作业** | `INBOUND` / `OUTBOUND` / `SEND_BACK` / `MOVE` / `RECEIVE` | 物料入库/出库/退库/移库/接收 | INBOUND / OUTBOUND |
| **盘点** | `PLUS` / `MINUS` | 盘增 / 盘减 | 物料盘点 |
| **预定** | `RESERVE` / `CANCEL_RESERVE` / `MATERIAL_RESERVE` / `MATERIAL_CANCEL_RESERVE` | 物料预定 / 取消 / 暂存预定 | — |
| **拆包** | `SPLIT_PACKAGE` / `SPLIT_PACKAGE_NEW` | 拆包出库 | OUTBOUND |
| **称量消耗** | `WEIGH_CONSUME` / `MATERIAL_WEIGH_CONSUME` / `MATERIAL_ODD_WEIGH_CONSUME` | 配料称量/物料称量/余料称量 | — |
| **配液量取** | `MEASURE_CONSUME` / `MEASURE_WEIGH` / `MEASURE_ODD` | 配液量取/物料入库 | INBOUND |
| **投料/回收** | `CHARGE` / `RECYCLE` / `BATCHING_INPUT` / `PREPARATION_INPUT` / `MATERIAL_INPUT` | 投料/回收/配料投入/配液投入/物料投入 | — |
| **产出/作废** | `MANUAL_OUTPUT` / `PREPARATION_PRODUCE` / `WEIGH_SCRAP` / `PREPARATION_SCRAP` | 手动产出/配液产出/产出作废 | INBOUND / OUTBOUND |
| **称量入库** | `INGREDIENT_WEIGHT` / `MATERIAL_WEIGHT` / `MATERIAL_ODD_WEIGHT` / `OUTPUT_WEIGHT` | 配料/物料/余料/产出 称量入库 | INBOUND |
| **其它消耗** | `SEND_BACK_AND_CONSUME` / `DESTROY_AND_CONSUME` / `USE_AND_CONSUME` | 退库消耗/销毁消耗/使用消耗 | SEND_BACK / DESTROY / USE |

> 配套展示枚举 `StorageOperateTypeShowEnum`（INBOUND/OUTBOUND/PLUS/MINUS/MOVE/SEND_BACK/DESTROY/USE 等），`log/` 子包按 showType 归并查询。

### `MaterialQualityStatusEnum`（物料质量状态）

`bmos-mes-common/.../enums/material/MaterialQualityStatusEnum.java`，批次可用性的判定依据：

| code | 中文 |
|---|---|
| `QUARANTINE` | 待验 |
| `QUALIFIED` | 合格 |
| `UNQUALIFIED` | 不合格 |
| `SAMPLED` | 已取样 |
| `RESTRICTED_RELEASE` | 限制性放行 |

> `StorageMaterialBatch` 在出库/预定校验时根据质量状态抛 `STORAGE_MATERIAL_BATCH_CANT_USE`。

### `WeighSignStatus`（称量签收状态）

`bmos-mes-common/.../enums/ingredient/WeighSignStatus.java`。`StorageMaterial`/`StorageMaterialVO` 的"是否可用/有效数量"判断中，需 `signStatus == SIGNED` 才计入可用。详见 [[mes-record-module]] 与 ingredient（待建）。

## Controller（8 个）

| Controller | 子包 | 职责 |
|---|---|---|
| `StorageConfigController` | config | 储位树 CRUD（createStorage/editStorage/deleteStorage/queryTree/queryTreeWithCargoPosition） |
| `CargoPositionController` | config | 货位 CRUD + 启用禁用 + 绑定用户/权限查询 |
| `StorageMaterialController` | manage | ★ 物料库存主入口：入/出/移/退/盘/预定/取消/拆包 + 移动端变体 + 标签打印 |
| `StorageMaterialManageController` | manage | 物料批次后台管理（addBatch/editBatch/add/queryBatchDetail + 组件字段保存） |
| `StorageMaterialBatchController` | manage | 物料批次查询（分页/按物料查/详情） |
| `StorageMaterialBatchFieldController` | manage | 批次动态字段查询 |
| `ChargeRecycleController` | manage | 投料充电/回收 + 组件维度列表 |
| `StorageMaterialPositionLogController` | log | 储位物料操作流水分页查询 |

> 移动端方法后缀 `Mobile`（`outboundMobile` / `sendBackMobile` / `moveMobile` / `queryInfoByMaterialNo`），与 mes-app 联调。详见 [[mobile-overview]]。

## Service 核心方法

### `IStorageConfigService`（储位配置）

| 方法 | 功能 |
|---|---|
| `createStorage` / `editStorage` / `deleteStorage` | 储位 CRUD（树形，删除前查子节点） |
| `queryList(parentId, keyword)` / `queryTree(parentId)` | 储位列表/树 |
| `queryTreeWithCargoPosition(parentId)` | 带货位的储位树 |
| `selectBatchIds(storageIdSet)` | 批量取储位（供其它子域引用） |

### `ICargoPositionService`（货位）

| 方法 | 功能 |
|---|---|
| `createCargoPosition` / `editCargoPosition` / `enable/disableCargoPosition` / `deleteCargoPosition` | 货位 CRUD + 启停 |
| `queryPage` / `queryInfoById` / `queryInfoByCode` | 分页/详情/按编码 |
| `queryPositionBoundUserList(positionId)` / `...ByPermissionCode(positionId, code)` | **货位绑定用户/权限**（数据权限相关） |
| `getByIdWithPermission` / `getByCodeWithPermission` | 带权限校验的查询 |
| `queryAllEnabledChildrenByStorageId(storageId)` | 取储位下所有启用货位 |
| `getCargoPositionPathMap(idList, slash)` | 货位路径（用于展示"仓/区/位"全路径） |

### `IStorageMaterialService`（物料库存 · 核心作业 Service）

按业务类别归组（方法最多，是模块中枢）：

**基础库内作业**
| 方法 | 功能 |
|---|---|
| `inbound(dto)` / `sendBack(dto)` / `outbound(dto)` / `move(dto)` / `check(dto)` | 入库/退库/出库/移库/盘点 |
| `outboundMobile` / `sendBackMobile` / `moveMobile` | 移动端对应方法 |
| `reserve(dto)` / `reserveBatch(dtos)` / `cancelReserve(dto)` / `cancelReserveByProductPlanId(planId)` | 预定/批量预定/取消预定（按生产计划维度） |
| `splitPackage(dto)` | 拆包（返回新编号） |

**查询**
| 方法 | 功能 |
|---|---|
| `queryPage` / `queryInfoById` / `queryInfoByIds` / `queryInfoList` | 库存分页/详情/列表 |
| `queryListByBatchId(batchNoId, storageMaterialNo)` | 按批次查物料部件 |
| `queryInfoByMaterialNo(materialNo, validateAvailable, codeType)` | 按物料号查（移动端扫码常用） |
| `getBatchReservedMaterial(dto)` / `getReservedAvailableStorageMaterial(dto)` | 查批次预定情况/可用库存 |
| `queryByMaterialNo(no, available)` / `queryByMaterialNoIgnoreAvailable(no)` | 按号查（可校验可用性） |
| `getByContainerId(id)` / `queryByContainerNo(no)` / `selectStorageMaterialByContainerId(id)` | 按容器查库存 |

**消耗/产出对接**（★ 被其它子域调用的关键方法）
| 方法 | 功能 | 调用方 |
|---|---|---|
| `weighConsume(list, remark, plan, operateType)` | 称量消耗 | weigh / ingredient |
| `chargeConsume(storageMaterialIdList, operatorId, productPlanId)` | 投料消耗 | ingredient |
| `inventoryMaterialInbound(dto, plan)` | 盘点入库 | （盘点回写） |
| `recycleStorageMaterial(dto)` | 回收物料 | ChargeRecycle |
| `scrapBatch(list, weigherId, reCheckerId, remark, planId)` | 产出作废 | output |

**编号序列**
| 方法 | 功能 |
|---|---|
| `getSerial()` / `batchGetSerial(size)` / `confirmSerial(serial)` / `batchConfirmSerial(list)` | 物料编号序列生成与确认（防并发重复） |

> `weighConsume` / `chargeConsume` 是 storage → weigh/ingredient 的**反向耦合点**：称量配料完成时调 storage 扣减库存。改库存校验逻辑会影响整条称量配料链。

### `IStorageMaterialManageService`（物料批次后台）

| 方法 | 功能 |
|---|---|
| `queryBatchPage` / `queryPage` / `queryBatchDetail` | 批次/库存分页与详情 |
| `addBatch(dto)` / `editBatch(dto)` / `add(dto)` | 新建批次/编辑批次/新建库存 |
| `saveMaterialComponentValue(dto)` | 保存物料组件字段值 |
| `queryExpireWarningList()` / `updateBatchExpireFlag(batchIds, flag)` | ★ 临期预警查询与标记（被 Job 调用） |

### 其它 Service

- `IStorageMaterialBatchService` — 批次档案查询（分页/按物料/详情/预留批次查询 `queryReservedBatch`）
- `MaterialBatchFieldService` — 批次动态字段（save/delete/queryMaterialField/queryMaterialAndBatchField）
- `ChargeRecycleService` — 投料充电/回收（`chargeStorageMaterial` / `recycleStorageMaterial` / `getComponentChargeRecycleList` / `scanMaterialOrDevice` 扫码）
- `IStorageMaterialPositionLogService` — 操作日志（`saveLog` / `saveLogs` / `queryPage`）

## 独有机制

### 物料临期定时任务（`MaterialExpireForeWarningJob`）

`manage/job/MaterialExpireForeWarningJob.java`：
1. 调 `storageMaterialManageService.queryExpireWarningList()` 取临期批次
2. 构造 `MaterialForeWarningMessage`（物料编码/批次号/名称/时间）
3. 调 **platform** 的 `messageNotifyFeign.materialExpireForeWarning(msg)` 推送通知（跨服务，见 [[service-integration]]）
4. 调 `updateBatchExpireFlag(batchIds, true)` 标记已提醒，避免重复

### 标签打印（在 StorageMaterialController 内）

Controller 内联了标签打印逻辑：通过 `equipmentConfigFeign.getConfigByEquipmentId`（platform）取打印机 IP/端口，构造 `PrintCommonDTO` 调 `platformTagClient.printTag`。即 **storage → platform** 的打印对接点。

### 物料编号序列

`getSerial/batchGetSerial/confirmSerial` 提供并发安全的物料编号生成（拆包、入库产新号用），需配套 `confirm` 确认。

## 与其它子域 / 服务的耦合点

- **→ platform**：储位货位权限（`resourcePermissionService.getDeptListByResourceId`）、打印机配置（`equipmentConfigFeign`）、标签打印（`platformTagClient`）、临期通知（`messageNotifyFeign`）。详见 [[platform-overview]]。
- **→ wms**：仅经 [[mes-requisition-module]] 的 `WmsFeignClient`（领料），storage 本身不直连 wms。
- **weigh / ingredient → storage**：称量与配料通过 `weighConsume` / `chargeConsume` 反向扣减库存。
- **output → storage**：产出经 `scrapBatch` / `MANUAL_OUTPUT` 入库或作废。
- **StorageOperateTypeEnum** 是 storage 与 weigh/ingredient/execute/output 共同的"作业类型词典"——任何库存变动都落一条日志并归类。

## AI 定位提示

- 改储位/货位配置、数据权限 → `config/`（`IStorageConfigService` / `ICargoPositionService`）
- 物料库存入/出/移/退/盘/预定/拆包 → `IStorageMaterialService`（注意区分 PC 与 `*Mobile` 方法）
- 物料批次档案/动态字段 → `IStorageMaterialManageService` / `MaterialBatchFieldService`
- 投料充电/回收、扫码投料 → `ChargeRecycleService`
- 操作流水/追溯某次库存变动 → `log/` 的 `StorageMaterialPositionLog`，按 `StorageOperateTypeEnum` 筛选
- 物料"卡住/不可用/已过期"报错 → 查 `StorageMaterial.availableValidate` / `StorageMaterialBatch` 的 `MaterialQualityStatusEnum` 校验
- 临期预警不生效/重复提醒 → `MaterialExpireForeWarningJob` + `updateBatchExpireFlag`
- 物料作业类型/日志分类 → `StorageOperateTypeEnum`（在 mes-common，不在 storage 包）

## 相关页面

- [[mes-overview]] — mes 服务总览（storage 为头部子域）
- [[mes-record-module]] — 批记录（共用 `WeighSignStatus` 签收态）
- [[mes-requisition-module]] — 领料（mes 唯一外向调 wms 的子域，storage 的上游入库来源之一）
- [[mes-weigh-module]] — 称量（绑定 storage 库位物料执行称重）
- [[mes-ingredient-module]] — 配料（投料/称量复核绑定 storage 物料批次）
- [[service-integration]] — storage → platform（权限/打印/通知）调用矩阵
- [[platform-overview]] — 权限、设备配置、消息通知、标签打印的上游底座
- [[mobile-overview]] — `*Mobile` 方法对应的 mes-app 联调
- [[database-schema-overview]] — `bm_storage*` / `bm_material_batch_field` 表归属
