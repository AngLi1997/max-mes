---
title: MES Product 模块（产品/物料主数据）
created: 2026-06-29
updated: 2026-06-29
type: entity
service: mes
tags: [backend, mes, module, mybatis]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/product/
status: active
---

# MES Product 模块

## 概述 / 职责

Product 模块管理 mes 端的**产品/物料主数据**：物料定义、分类、扩展字段、变更日志，并提供与 platform 的物料同步、与批记录的绑定能力。是 mes 工艺/批记录/计划等业务的上游依赖。

- 包路径：`com.bmos.mes.service.product/`
- Controller：4 个 · Mapper：4 个 · 表：4 张

## 数据模型（4 张表）

| 表 | Model 类 | 用途 |
|---|---|---|
| `bm_material` | `ProductMaterial` | 物料主表 |
| `bm_material_category` | `ProductMaterialCategory` | 物料分类（支持树形结构） |
| `bm_material_field` | `MaterialField` | 物料扩展字段定义（动态属性） |
| `bm_material_log` | `MaterialLog` | 物料变更日志 |

> `bm_material` 使用 `autoResultMap = true`——含 JSON/复杂类型字段，序列化交给 MyBatis-Plus 处理。同一约定见 [[mes-record-module]]。

## Controller（4 个）

| Controller | 角色 |
|---|---|
| `ProductMaterialController` | 产品物料主入口（业务接口） |
| `MaterialFieldController` | 扩展字段管理 |
| `MaterialLogController` | 变更日志查询 |
| `MaterialFeignController` | 实现对外 Feign（被其它服务调） |

## 对外 Feign 契约

本模块对外暴露物料数据，是 mes 三个 Feign 之一的实现：

- `MaterialFeign`（定义在 `bmos-mes-feign/material/feign/`）→ 由本模块 `MaterialFeignController` 实现
- 跨服务共享 VO：`MaterialFieldInfoFeignVO`

调用方：lims / wms 取 mes 物料信息时走这条。详见 [[service-integration]]。

## Service 核心方法

### ProductMaterialService（物料主服务）

| 方法 | 功能 |
|---|---|
| `save(dto)` / `update(dto)` / `delete(id)` | 基础 CRUD |
| `getPage(query)` / `getDetail(id)` | 分页/详情 |
| `changeStatus(dto)` | 物料状态切换 |
| `issueMaterialAndCategory(RemoteIssueDTO)` | **远程下发**物料与分类（外部系统对接） |
| `syncMaterialAndCategory(SyncMaterialInfoDTO)` | **同步**物料与分类（接收外部数据） |
| `getSyncTree(query)` / `getSyncTreeAll()` | 同步用的物料树 |
| `getProductTree(categoryType)` / `getaLLProductTree(types)` | 产品分类树 |
| `getProductList(categoryType)` / `getFinishProductList(categoryType)` | 产品列表 / 成品列表 |
| `bindBatchRecords(RecordSaveDTO)` | **绑定批记录** → 与 [[mes-record-module]] 联动 |
| `getProductBindRecordIds(productId)` | 查产品绑定的批记录 ID |
| `getPrincipalList(query)` | 物料责任人列表 |
| `existsCategoryMaterial(id)` | 校验分类下是否有物料（删除分类前检查） |

### 其它 Service

- `ProductMaterialCategoryService` — 分类管理
- `MaterialFieldService` — 扩展字段（动态属性）管理
- `MaterialLogService` — 物料变更日志

## 关键依赖与外部对接

- **同步/下发**：通过 `RemoteIssueDTO` / `RemoteSyncDTO` / `SyncMaterialInfoDTO` 与外部主数据系统对接（issue=下发、sync=同步接收）。
- **调 platform**：物料/单位的基础数据通过 platform-facade 取（详见 [[platform-overview]] 的 `PlatformMaterialFeign`）。
- **被批记录依赖**：`bindBatchRecords` 是 product → record 的绑定接口；批记录使用产品配方时需先建立绑定。

## AI 定位提示

- 物料 CRUD / 分类树 → `ProductMaterialController` / `ProductMaterialCategoryController`
- 物料扩展字段（业务方加自定义属性）→ `MaterialField*` 系列
- 外部系统对接（数据同步）→ 关键字 `issue` / `sync` / `remote`，看 `RemoteIssueDTO` `SyncMaterialInfoDTO`
- 改物料前查影响：`existsCategoryMaterial` `getProductBindRecordIds`

## 相关页面

- [[mes-overview]] — mes 服务总览
- [[mes-record-module]] — 批记录（与本模块通过 `bindBatchRecords` 关联）
- [[service-integration]] — `MaterialFeign` 对外契约
- [[platform-overview]] — 上游主数据来源（platform `PlatformMaterialFeign`）
- [[database-schema-overview]] — `bm_*` 表全景
