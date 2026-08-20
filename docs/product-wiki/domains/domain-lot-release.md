---
title: 批签发业务域
created: 2026-07-27
updated: 2026-07-28
type: domain
system: mes
domain: 批签发
menu_code: 120-040
iter_status: 规划中
prd_filled: filled
tags: [批签发, 配置端]
sources:
  - packages/prd/040-批签发.md
  - packages/prd/001-数据集管理.md
  - packages/prd/002-批签发模板.md
  - packages/prd/003-批签发管理.md
  - packages/prd/004-批签发审核.md
  - packages/prd/005-批签发文件.md
related_code_wiki: []
status: active
---

# 批签发业务域

> 覆盖菜单区间 **120-040-001 ~ 120-040-006**。本域承担成品生产完成后**批签发文件自动生成、审核、归档**的全链路能力，满足国家药监对成品批号的合规管理要求。
>
> 配置侧链路（数据集/模板）见 [[feature-lot-release-mgmt]]；执行侧链路（生成/审核/文件）同见该 feature。

## 域职责

将生产批记录中的数据按 GMP/公司模板**自动抓取、二次处理、填入批签发模板**，经独立审核流后归档为可预览/打印/导出的批签发文件，全过程受审计追踪与电子签名约束（[[audit-trail-and-logs]]、[[esignature]]）。

## 功能清单

| 菜单编码 | 功能名 | 一句话职责 | iter_status | prd_filled | PRD 文件 |
|---|---|---|---|---|---|
| 120-040-001 | 数据集管理 | 定义数据点-批记录组件绑定关系与二次处理公式，作为模板数据源 | 规划中 | filled | `packages/prd/001-数据集管理.md` + `数据集管理补充需求.md` |
| 120-040-002 | 批签发模板 | 导入 Excel/Luckysheet 模板，设置打印区域，将数据点拖拽到单元格 | 规划中 | filled | `packages/prd/002-批签发模板.md` + `批签发模板补充需求.md` |
| 120-040-003 | 批签发管理 | 选成品批次+模板生成批签发，提交审核；列表数据源为已完成生产批次 | 规划中 | filled | `packages/prd/003-批签发管理.md` + `批签发管理补充需求.md` |
| 120-040-004 | 批签发填报 | （未规划）预留手工填报入口，当前全部走自动抓取 | 未规划 | empty | — |
| 120-040-005 | 批签发审核 | 独立审核流（通过/不通过/退回/转交/抄送/密码认证），三 tab：待办/已办/历史 | 规划中 | filled | `packages/prd/004-批签发审核.md` |
| 120-040-006 | 批签发文件 | 审核通过的批签发文件统一入库，支持预览/打印/导出 Excel | 规划中 | filled | `packages/prd/005-批签发文件.md` |

> 关联功能：**120-050-002 批记录打印** 归[[domain-production-management]]（生产查询），但其输出（批记录 PDF/数据）是本域数据集抓取的上游，二者强相关。

## 域内核心设计要点

1. **链路：数据集 → 模板 → 批签发管理 → 审核 → 文件**。前三步在配置端定义数据来源与排版（实施人员），后两步在使用端执行（批签发管理人员/填报人员/审核人）。展开见 [[feature-lot-release-mgmt]]。
2. **数据点-组件 1:1 绑定**（数据集层）。一个数据点关联且仅关联一个批记录模板组件；组件处理 tab 下另支持一对多二次处理。该约束是后续自动抓取的语义基础。
3. **多数据源标识 + 层级号**。对单工艺下多生产批次场景，数据集打"多数据源"标识，模板拖拽时给每个数据点赋层级号（1/2/3…），生成时按层级号匹配完工时间排序的生产批次（[[flow-batch-production]] 产出）。
4. **独立审核流，非通用审批流**。批签发审核（005）走自己的审核流模型：节点级"分配节点功能"控制按钮（通过/不通过/退回/抄送/转交）显示，节点级"密码认证"勾选后处理前强制电子签名（[[esignature]]）。与 [[approval-flow]] 中通用审批流引擎并行存在，不复用。
5. **成品树与列表数据源**。三个功能（模板/管理/文件）共用产品管理下"产品类型=成品且启用"的二级成品树；批签发管理列表只展示**生产状态=已完成**的成品批次（来源：[[domain-production-management]] 生产批次）。
6. **数据二次处理公式**。数据集层支持 ROUND（四舍五入/四舍六入五成双）、日期解析（年月日时分秒互转）、SUM/AVG/MIN/MAX、CONCAT、阈值判断（NUMCONFIRM，越界标红提示）。公式来源：平台公式配置 + 内置高代码公式。

## 跨域关联

- 配置端实体模型与状态机：[[feature-lot-release-mgmt]]
- 上游成品与工艺：[[domain-production-config]]（工艺/配方）、[[material-master]]（成品物料）
- 上游生产批次：[[domain-production-management]]、[[flow-batch-production]]
- 合规底座：[[audit-trail-and-logs]]、[[esignature]]、[[approval-flow]]
- 批记录打印（强相关）：[[domain-production-management]] 下 120-050-002

## 相关页面

- [[feature-lot-release-mgmt]] — 批签发端到端实体模型与状态机
- [[approval-flow]] — 批签发走独立审核流，与通用审批流对照
- [[domain-production-config]] — 数据集关联工艺/批记录模板的上游


## 📎 PRD 原文

- [001-数据集管理.md](<../raw/prd/001-数据集管理.md>)
- [002-批签发模板.md](<../raw/prd/002-批签发模板.md>)
- [003-批签发管理.md](<../raw/prd/003-批签发管理.md>)
- [004-批签发审核.md](<../raw/prd/004-批签发审核.md>)
- [005-批签发文件.md](<../raw/prd/005-批签发文件.md>)
- [040-批签发.md](<../raw/prd/040-批签发.md>)
