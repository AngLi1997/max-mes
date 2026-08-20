---
title: BMOS 产品族总览
created: 2026-07-27
updated: 2026-07-27
type: concept
system: cross
domain: 跨域
iter_status: 待测试
prd_filled: filled
tags: [系统基础]
sources:
  - packages/prd/菜单功能.md
  - packages/prd/007-标签管理.md
related_code_wiki: [monorepo-architecture]
status: active
---

# BMOS 产品族总览

> 本页是 product-wiki 的**顶层入口**。先读本页 + [[menu-structure]]，再按域深入。
> 与 `docs/code-wiki/[[monorepo-architecture]]` 互补：那里讲代码架构，这里讲产品设计。

## 产品定位

**BMOS** 是面向制药行业的**多业务系统产品族**，覆盖制造执行、仓储、实验室、质量、设备、血源管理等全链条，受药企 **GAMP 5 / GMP** 合规约束（电子签名、审计追踪、版本发布、批签发）。系统间通过 Feign 通信，主数据收口在平台层。

## 多系统编号体系

产品族按「系统编号」划分子系统。编号规则见 [[menu-structure]]。

| 编号 | 系统 | 编码 | 应用 | 主数据边界 |
|---|---|---|---|---|
| 100 | 平台管理 | platform | bmos-platform | **统一主数据真相源**：物料、单位、设备类/功能点模板、标签、用户/部门/角色/权限、字典、编号规则、审计、流程配置 |
| 101 | 审计管理 | audit | bmos-audit | 登录/操作/签名日志、审批流追溯（统一日志中台） |
| 120 | 制造执行系统 | mes | bmos-mes | 自有：班组、暂存货位、配方/工艺/批记录、物料件/物料批次、领料、物料追溯、批签发 |
| 121 | 制造执行 APP | mes-app | — | 移动端执行（库存管理、称量、领料接收、生产前确认） |
| 130 | 实验室信息管理 | lims | bmos-lims | 检品、试剂耗材、标准品、请验/检验结果 |
| 140 | 质量管理 | qms | bmos-qms | （预留） |
| 150 | 仓储管理 | wms | bmos-wms | 货品/货品批次/货品件、货位、库存、仓库发料 |
| 160 | 设备管理 | ems | bmos-ems | 设备类、设备、功能点、状态图 |
| 170/180 | 血源管理/血源实验室 | bsms/bims | bmos-bsms/bmos-bims | 血源业务 |
| 190~230 | 服务监控/大屏/集中lims/外部链接/Agent | — | BM-HM/BM-DC/bm-el/bmos-agent | 辅助与集成 |

> 本知识库**以 120/121 为核心**，纳入与 MES 执行强相关的 platform 主数据、wms 仓储交互、lims 检验交互。其余系统暂不展开。

## 主数据边界（关键）

```
platform（单一真相源）
   │ 物料 / 单位 / 设备类 / 功能点模板 / 标签 / 用户权限 / 字典 / 编号规则
   │
   ├──下发(platform → 业务)──► MES：产品/原辅包/中间品（落地为停用态记录）
   │                          WMS：货品（落地为停用态记录）
   │                          LIMS：检品/试剂耗材
   │
   └──同步(业务 ← platform)──► 业务侧【同步】按钮拉取启用且未同步的物料
```

- **platform 是主数据唯一真相源**；MES/WMS/LIMS 不自建物料，只**只读引用 + 业务态启停**。
- 「下发」与「同步」是两个方向：下发从平台推到业务（多选），同步从业务侧主动拉取。
- 详见 [[material-master]]、[[equipment-master]]。

> 此边界与项目「通用主数据收口」决策一致：platform 单一真相源，mes 改只读引用，收掉 mes 的物料/设备/工厂/标签/单位配置能力。

## 两端模型（配置端 vs 使用端）

产品正从「平台 + N 业务系统」演进到**「实施配置端 + 客户使用端」两端模型**：

| 维度 | 配置端（实施） | 使用端（客户） |
|---|---|---|
| 职责 | 定义/模板/主数据配置 | 运行/执行/查询 |
| 流转 | 草稿 → 版本 → 验证 → **审批** → 发布（GAMP 5 真分离） | 引用已发布的配置执行 |
| 典型对象 | 工艺/配方/BOM/记录/数据集/批签发模板/操作规程/流程模型 | 生产计划/指令单/称量工单/批签发记录/物料件 |
| 使用端分域 | 配置端统一 | mes用 / lims用 / wms用 |

- 配置对象均走统一**版本发布范式**，见 [[version-publish-mechanism]]。
- 审批接入统一审批流引擎，见 [[approval-flow]]。

## 核心业务链路（四大链路）

1. **批生产执行链** [[flow-batch-production]]：计划→指令单分解→生产前确认→工艺执行→投料→产出→审核→归档
2. **物料流转主链** [[flow-material-flow]]：平台注册→下发→WMS 入库→发料→领料接收→暂存→消耗→产出→追溯
3. **称量执行流** [[flow-weighing-execution]]：配料计划→称量需求→工单规划→执行(签名)→物料件生效→投料
4. **合规追溯链** [[flow-compliance-traceability]]：业务操作→四大日志→审计中台 + 签名/审批流追溯

## 如何使用本知识库

1. **找功能**：读 [[menu-structure]] 按菜单编码定位 → 进入对应 [[domain-production-config]] 等域页 → 跳 sources 读 PRD 原文
2. **理解模型**：读 feature 页（如 [[feature-process-and-execution-model]]）看实体关系
3. **理流程**：读 flows/ 看端到端时序
4. **查合规**：读 concepts/（[[audit-trail-and-logs]]、[[esignature]]、[[approval-flow]]、[[version-publish-mechanism]]）
5. **跳代码**：用 `related_code_wiki` 字段跳 `docs/code-wiki/` 对偶页

## 相关页面

- [[menu-structure]] — 完整菜单树与 5 级编码
- [[version-publish-mechanism]] — 配置端版本发布范式
- [[material-master]] — 主数据边界（物料）
- [[common-interaction-spec]] — 通用交互规范


## 📎 PRD 原文

- [007-标签管理.md](<../raw/prd/007-标签管理.md>)
- [菜单功能.md](<../raw/prd/菜单功能.md>)
