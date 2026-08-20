---
title: 数据库表全景速查
created: 2026-06-29
updated: 2026-06-30
type: comparison
service: cross
tags: [backend, database, mybatis, architecture]
sources:
  - packages/backend/services/platform/
  - packages/backend/services/mes/
  - packages/backend/services/lims/
  - packages/backend/services/wms/
status: active
---

# 数据库表全景速查

> bmos-monorepo 各后端服务核心表按业务域分组速查。
> 数据来自源码中 `@TableName` 注解扫描（2026-06-29），用于 AI 快速判断「某张表/某业务属于哪个服务」。
> 表名是定位的关键线索——见到表前缀即可反推服务归属。

## 表前缀 → 服务映射（最重要）

| 前缀 | 服务 | 说明 |
|------|------|------|
| `bp_` | **platform** | 平台基础（用户/权限/设备/工厂/部门） |
| `bm_` | **mes** | 制造执行（批记录/称量/工序/配料） |
| `lm_` | **lims** | 实验室（检验/稳定性/留样/报告） |
| `bw_` | **wms** | 仓库（库存/出库/检验） |
| `bm_resource_permission` / `bw_resource_permission` / `lm_resource*` | 各服务 | 资源权限表（各服务独立一份） |

> 跨服务复用表：`bp_active`（license 激活）在 mes/lims/wms 中均被引用；`bm_execute_form_data*`、`bm_batch_record_version` 表名属 mes 命名空间，但 **lims 的 eln 子域也持有实体并读写（双写，非单纯 mes→lims 单向）**，存在一致性风险。另有 `bm_log_operation`、`bm_operate_rule*` 用 `bm_` 前缀但归属 lims（命名空间污染）。写入归属需特别注意。

## platform（~67 张，前缀 `bp_`）

| 业务域 | 表数 | 代表表 |
|--------|------|--------|
| 设备 equipment | 13 | `bp_equipment_info` `bp_equipment_category` `bp_equipment_status_log` `bp_equipment_station*` |
| 工厂/产线 factory | 11 | `bp_factory_line` `bp_factory_room` `bp_factory_module` `bp_factory_line_station` |
| 用户 user | 5 | `bp_user*` |
| 标签 tag | 4 | `bp_tag*` `bp_equipment_tag*` |
| 角色/权限 role/auth | 3+ | `bp_role*` `bp_auth_role_menu` `bp_menu` `bp_dept*` |
| 消息/物料/其它 | — | `bp_message*` `bp_material*` `bp_business_parameter` `bp_active`(license) `bp_signature` `bp_login*` |

详见 [[platform-overview]]。

## mes（~169 张，前缀 `bm_`）

| 业务域 | 表数 | 代表表 |
|--------|------|--------|
| 称量 weigh | 14 | `bm_weigh*` `bm_free_weigh_history` |
| 工序 procedure | 12 | `bm_procedure*` |
| 批记录/批模板 batch | 10 | `bm_batch_record_*` `bm_batch_template_*` `bm_batch_record_version` |
| 批次 lot | 9 | `bm_lot*` |
| 工艺 process | 8 | `bm_process*` |
| 配料投料 ingredient | 7 | `bm_ingredient_plan` `bm_ingredient_input_record` |
| 液体/制剂 liquid/preparation | 10 | `bm_liquid*` `bm_preparation*` |
| 检验 inspect | 6 | `bm_inspect*` |
| 领料 requisition | 5 | `bm_requisition*` |
| 执行记录 execute | 5 | `bm_execute_record*` `bm_execute_form_data` `bm_execute_exception` |
| 产出/数据集 output/dataset | 8 | `bm_output*` `bm_dataset*` |

详见 [[mes-overview]]。

## lims（~88 张，前缀 `lm_`，部分复用 `bm_`/`bp_`）

| 业务域 | 表数 | 代表表 |
|--------|------|--------|
| 检验单 inspection | 18 | `lm_inspection_order*` `lm_inspection_entry_record` |
| 稳定性 stability | 15 | `lm_stability*` |
| 检验项/方法/参数 inspect | 11 | `lm_inspect_item` `lm_inspect_parameter*` `lm_inspect_method*` |
| 报告 report | 7 | `lm_report*` |
| 留样 retention | 4 | `lm_retention*` |
| 样品 sample | 3 | `lm_sample*` |
| 文档配置 document | 3 | `lm_document_config*` `lm_document_material` |
| ELN/方案/任务 | — | `lm_eln_attachment` `lm_scheme*` `lm_task*` `lm_package` `lm_item` |
| 复用自 mes | 2 | `bm_execute_form_data*`（mes→lims 数据流） |

详见 [[lims-overview]]。

## wms（17 张，前缀 `bw_`）

| 业务域 | 表 | 说明 |
|---|---|---|
| 库存 | `bw_inventory` `bw_inventory_batch` `bw_inventory_reserve` | 库存件 / 批次 / 预留 |
| 货品 | `bw_cargo` `bw_cargo_category` | 货品主数据 / 分类 |
| 货位·存储 | `bw_cargo_position` `bw_storage` | 货位 / 存储区域树 |
| 请验 | `bw_inspect` `bw_inspect_info` `bw_inspect_result` | 请验单主 / 明细 / 结果 |
| 发料 | `bw_send_out_order` `bw_send_out_order_item` | 发料单主 / 明细 |
| 流水 | `bw_cargo_log` `bw_position_log` `bw_operation_log` | 货品 / 货位 / 操作日志 |
| 其它 | `bw_active` `bw_resource_permission` | license 激活 / 资源权限 |

> 此前记为 ~11 张，实扫为 17 张（[[wms-overview]]）。

详见 [[wms-overview]]。

## 技术约定

- ORM：统一 **MyBatis-Plus**，实体通过 `@TableName` 映射；Mapper 接口数远多于表数（一表多 Mapper / 复合查询）。
- 分库分表：部分服务引入 **ShardingSphere 5.5.1**（见各服务 pom），大表分片策略详见 [[data-access-pattern]]。
- 表数为 `@TableName` 去重统计的近似值，权威以数据库实际 DDL / migration 为准。

## 相关页面

- [[service-overview]] — 服务端口/规模速查
- [[data-access-pattern]] — MyBatis-Plus + ShardingSphere 规范
- [[service-integration]] — 跨服务数据流与 Feign
- [[monorepo-architecture]] — TSD 加密历史（gateway/wms/lims 已于 2026-06-30 解密，表扫描可直读）
