---
title: 后端服务速查
created: 2026-06-29
updated: 2026-06-30
type: comparison
service: cross
tags: [backend, architecture, integration, deploy]
sources:
  - packages/backend/services/platform/
  - packages/backend/services/mes/
  - packages/backend/services/lims/
  - packages/backend/services/wms/
  - packages/backend/services/gateway/
status: active
---

# 后端服务速查

> bmos-monorepo 5 个后端 Spring Boot 微服务的端口、注册名、规模与职责一览。
> 数据来自各服务 `application.yml` / `bootstrap.yml` 与源码扫描（2026-06-29）。

## 服务总表

| 服务 | 目录 | 端口 | Nacos 注册名 | context-path | bmos.version | Java 包根 |
|------|------|------|--------------|--------------|--------------|-----------|
| **platform** | `services/platform` | 60100 | `bmos-platform-service` | `/api/app/platform` | 1.15.0-SNAPSHOT | `com.bmos.platform` |
| **mes** | `services/mes` | 60200 | `bmos-mes-service` | `/api/app/mes` | 1.15.0-SNAPSHOT | `com.bmos.mes` |
| **lims** | `services/lims` | 61001 | `bmos-lims2-service` | `/api/app/lims2` | 1.15.2-SNAPSHOT | `com.bmos.lims2` |
| **wms** | `services/wms` | 60900 | `bmos-wms-service` | `/api/app/wms` | 1.14.0-SNAPSHOT | `com.bmos.wms` |
| **gateway** | `services/gateway` | 60300 | `bmos-gateway-service` | —（路由入口） | 1.14.0-SNAPSHOT | `com.bmos.gateway` |

> 注：各服务 `revision` 均为 `1.0-SNAPSHOT`，`bmos.version` 指依赖的 BMOS 平台框架（bmos-cloud-dependency BOM）版本，故各服务略有差异（1.14.0 ~ 1.15.2），是后续统一依赖的关注点。
> 注册中心：Nacos（`${NACOS_HOST}:${NACOS_PORT:8848}`），含服务发现 + 配置中心 + i18n（`backend-i18n` group）。

## 规模与模块结构

| 服务 | Maven 子模块 | Controller | Mapper | 表数(@TableName) | 表前缀 |
|------|-------------|-----------|--------|------------------|--------|
| platform | common / facade / service | 57 | 66 | ~67 | `bp_` `bm_resource_` |
| mes | common / feign / service | 101 | 170 | ~169 | `bm_` |
| lims | common / feign / server / web | 63 | 93 | ~88 | `lm_`（部分复用 `bm_`/`bp_`） |
| wms | common / feign / service | 14 | 17 | 17 | `bw_` |
| gateway | 单模块（`src`） | 0 | 0 | 0 | —（无持久层） |

> mes 体量最大（101 Controller / 170 Mapper），lims 次之；wms 较轻量；platform 提供基础能力并对外暴露 `facade`。
> lims 模块命名为 `bmos-lims2-*`，对外服务模块叫 `-server` 和 `-web`（与其它服务的 `-service` 不同），注意路径差异。

## 职责定位

- **platform（平台基础）**：用户、角色、权限、部门、设备/工位（`bp_equipment_*` / `bp_factory_*`）、业务参数、license 激活（`bp_active`）。是其它服务的依赖底座，通过 `bmos-platform-facade` 对外暴露接口。详见 [[platform-overview]]。
- **mes（制造执行）**：批记录（`bm_batch_record_*`）、批模板（`bm_batch_template_*`）、执行记录（`bm_execute_*`）、配料投料（`bm_ingredient_*`）、称量、流程审计。详见 [[mes-overview]]。
- **lims（实验室信息）**：检验项/方法/参数（`lm_inspect_*`）、检验单（`lm_inspection_order_*`）、ELN 电子实验记录（`lm_eln_*`）、文档配置（`lm_document_*`）。详见 [[lims-overview]]。
- **wms（仓库管理）**：库存（`bw_inventory_*`）、出库单（`bw_send_out_order_*`）、检验（`bw_inspect_*`）。详见 [[wms-overview]]。
- **gateway（API 网关）**：统一入口、路由转发、鉴权。前端所有请求经 gateway 转发到 `/api/app/<service>`。详见 [[gateway-overview]]。

## 服务间依赖

- 业务服务（mes/lims/wms）依赖 **platform-facade** 获取用户/权限/设备等基础数据。
- 服务间同步调用统一走 **Feign**（各服务 `*-feign` 模块定义客户端），禁止跨库直连。调用链路与方向详见 [[service-integration]]。
- 跨服务复用的表（如 `bp_active`、`bm_batch_record_version`、`bm_execute_form_data` 在 lims 中出现）需关注数据归属边界。

## 相关页面

- [[database-schema-overview]] — 各服务核心表分组速查
- [[service-integration]] — Feign 调用链路
- [[monorepo-architecture]] — 整体架构与 subtree 整合
