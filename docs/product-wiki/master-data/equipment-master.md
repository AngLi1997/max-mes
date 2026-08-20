---
title: 设备主数据
created: 2026-07-27
updated: 2026-07-27
type: master-data
system: cross
domain: 主数据
menu_code: 100-060
iter_status: 规划中
prd_filled: filled
tags: [设备]
sources:
  - packages/prd/060-设备管理.md
  - packages/prd/002-设备类型管理.md
  - packages/prd/001-功能点模板管理.md
  - packages/prd/026-设备数采绘图组件.md
related_code_wiki: [platform-equipment-module]
status: active
---

# 设备主数据

> 本页讲清「设备类 / 功能点模板 / 设备实例 / 状态图 / 数采映射」五类对象的产品模型与归属边界。
> 设备是 [[feature-weigh-center]]（秤具）、[[feature-process-and-execution-model]]（设备数采绘图组件）的执行载体。

## 主数据边界（关键）

设备主数据由 **平台 EMS（160）统一管理**，MES/WMS 只读引用：

| 子域 | 平台侧管理（真相源） | 业务侧引用 |
|---|---|---|
| 功能点模板（001） | ✅ 平台统一维护模板库 | 业务侧选源 |
| 设备类/设备类型（002） | ✅ 平台统一管理 | MES 引用设备实例 |
| 设备实例（060） | ✅ 平台统一管理基础信息+状态+映射 | MES 通过工位/产线数据权限可见 |
| 设备数采映射（003） | ✅ 平台配置三方数采点映射 | 运行时由数采平台消费 |
| 状态/状态图（060-3.2.6） | ✅ 平台管理状态位与流转图 | 设备实例绑定状态图 |

> **菜单歧义说明**：菜单清单中 `060-003 设备映射` 与 `060-004 设备管理` 标 empty，但实质内容在 `060-设备管理.md`（1456 行）的 3.2.4 / 3.2.5 节展开；本页以此为准。

## 分类层级

```
设备类型分组（内置 + 自定义）
└─ 设备类（equipment_class）
   ├─ 设备类功能点（来自功能点模板库 + 配置）
   │  └─ 平台功能点 ↔ 数采平台数采点 映射
   └─ 设备实例（equipment）
      ├─ 基础信息 + 房间绑定
      ├─ 状态图配置（1..N，可"无"）
      ├─ 称量配置（仅"称具"分组）
      └─ 打印配置（仅"打印机"分组）
```

**设备类型分组**（`equipment_type_group`）系统内置两种，决定设备实例额外配置属性：
- `打印机` → 设备实例创建页增加**打印配置**（IP/端口）
- `称具` → 设备实例创建页增加**称量配置**（量程/精度/校准/误差/方式/单位）
- 其他自定义分组 → 无附加配置

## 关键对象字段

### 设备类（equipment_class）

| 字段 | 类型 | 校验 | 说明 |
|---|---|---|---|
| equipment_class_code | VARCHAR(10) | 必填+唯一 | 创建后不可编辑 |
| equipment_class_name | VARCHAR(10) | 必填+唯一 | — |
| equipment_type_group | VARCHAR(8) | 必填 | 内置：打印机/称具；支持新增 |
| description | VARCHAR(50) | — | — |
| enabled_status | enum | — | 启用/停用；已被启用设备引用时不可停用 |

### 功能点模板（function_point_template）

| 字段 | 类型 | 校验 | 说明 |
|---|---|---|---|
| template_type | enum | 必填 | 数据采集 / 设备控制 / 事件（枚举写死） |
| template_name | VARCHAR(10) | 必填 | — |
| identifier | VARCHAR(8) | 必填+唯一 | 标识符；创建后不可编辑 |
| description | VARCHAR(50) | — | — |

> 设备类功能点（`class_function_point`）= 从模板选择 → 在设备类侧配置（功能点类型/名称/标识符可再编辑）→ 进入"功能点映射"建立与数采平台数采点的关联。

### 设备实例（equipment）

| 字段 | 类型 | 必填 | 说明 |
|---|---|---|---|
| equipment_code | VARCHAR | Y | 全局唯一；克隆时加后缀 `-kl` |
| equipment_name | VARCHAR | Y | 全局唯一；克隆时加后缀 `-克隆` |
| specification_model | VARCHAR | N | 规格型号 |
| manufacturer | VARCHAR | N | 设备厂商 |
| room | ref → 房间管理 | Y | 房间名称字段；继承房间树 |
| production_date | DATE | N | 生产日期 |
| equipment_weight | VARCHAR | N | 单位固定 KG（来源：单位管理） |
| equipment_image | upload | N | 支持上传/重新上传/删除 |
| status_graph_config | ref → 状态图 | Y | 支持多个；可选"无"；唯一默认不可删 |
| weighing_config | object | 条件必填 | **仅"称具"分组** |
| printing_config | object | 条件必填 | **仅"打印机"分组** |

**称量配置（weighing_config）字段**：称量范围 / 精度 / 校准目标重量 / 称重单位 / 误差（%，仅数字）/ 称量方式。来源：基础配置或数据字典。

**打印配置（printing_config）字段**：IP 地址 / 端口。

### 状态位与状态图

- **状态位**（status）：`status_code` / `status_name` / `status_type`（房间使用状态 / 设备使用状态 / 设备生产状态，支持新增）/ 有效时长 / 提醒时长 / 描述。启用态不可编辑/删除。
- **状态图**（status_graph）：基于工作流底层；`graph_code` / `graph_name` / `graph_type` / `version` / `description`。
  - 节点类型：事件节点（开始/结束）、活动节点（状态组件/条件判断）、网关（排他/并行/包含）。
  - 状态类型示例：设备使用（待CIP/已CIP/ECIP/待SIP/已SIP/ESIP）、设备生产（待用/在用/不可用）、房间使用（待清场/已清场/使用中）、称具校准/清洁/使用。

## 版本与启停机制

- **设备类/功能点模板/设备实例**：启用/停用二态，无版本概念；启用态下被引用的对象不可停用/删除/编辑。
- **状态图**：版本化对象。新建起始 V1.0，每次保存升版 +0.1（逢 1 进 1，逢 10 进 10）。
  - 启用态编辑 → 升版为新版本（停用态），原启用版本保留。
  - 停用态编辑 → 若从未启用过则覆盖继承版本号；若启用过则升版。
  - 同一状态图同时只能有一个启用版本；已被设备使用的状态图不可停用/删除。
- **设备克隆**：所有状态可克隆，克隆设备默认停用，编码加 `-kl`，名称加 `-克隆`，需编辑后才可启用。

## 与其他主数据/业务的关系

- **房间（[[location-master]]）**：设备实例必须绑定房间；房间树作为设备列表左侧树形区数据源。
- **工位（[[domain-system-base]] 权限管理）**：设备绑定工位，工位绑定人员，构成"作业权限"维度（用于 [[data-permission]] 与设备数采绘图组件执行权限）。
- **秤具 → 称量（[[feature-weigh-center]]）**：称量配置（精度/量程/校准）决定秤具能否被称量任务/工单/配料称量组件选用，校准状态影响称量合规。
- **设备数采绘图组件（批记录组件）**：执行时按"工位 → 设备"过滤可选设备；数采参数来源为设备类功能点配置；折线图保存历史版本，回写批记录（详见 026）。
- **物料件标签（[[domain-system-base]] 标签管理 04）**：设备标签场景类型为 `04`，字段含设备编号/名称/规格型号/厂商/购置日期/打印日期。
- **审计（[[audit-trail-and-logs]]）**：设备启停/克隆/状态修改/映射配置均落操作日志；设备故障触发[[domain-system-base]] 消息中心"告警-设备故障"。

## 相关页面

- [[location-master]] — 房间/货位主数据（设备实例绑定）
- [[feature-weigh-center]] — 秤具设备用于称量执行
- [[feature-process-and-execution-model]] — 设备数采绘图组件
- [[domain-system-base]] — 工位/标签/数据权限
- [[data-permission]] — 工位绑定决定作业权限维度
- [[audit-trail-and-logs]] — 设备操作日志与故障告警


## 📎 PRD 原文

- [001-功能点模板管理.md](<../raw/prd/001-功能点模板管理.md>)
- [002-设备类型管理.md](<../raw/prd/002-设备类型管理.md>)
- [026-设备数采绘图组件.md](<../raw/prd/026-设备数采绘图组件.md>)
- [060-设备管理.md](<../raw/prd/060-设备管理.md>)
