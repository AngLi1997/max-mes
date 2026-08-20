# Product Wiki Index

> bmos 产品设计知识库内容目录。每个 wiki 页面按【类型】列出，附一行摘要。
> **AI 查询时先读 [[product-overview]] + [[menu-structure]]**，再按域深入，用 sources 跳 PRD 原文。
> 最后更新：2026-07-27 | 总页数：31 | 已建：31 ✅ 已建成（lint 通过：零断链 / 零孤立页 / 双状态字段齐全）

## 如何使用本知识库

1. 先读 [[SCHEMA]]（约定）+ [[product-overview]]（产品族）+ [[menu-structure]]（菜单编码）
2. 按业务域定位：[[domain-production-config]] / [[domain-production-management]] / [[domain-lot-release]] / [[domain-weighing]] / [[domain-warehouse-interaction]] / [[domain-inspection]] / [[domain-system-base]]
3. 理解实体模型：features/ 页；理流程：flows/ 页；查合规规范：concepts/ 页
4. 跳代码：用页面 `related_code_wiki` 字段跳 `docs/code-wiki/`

> 图例：✅ 已完成 · ★ 枢纽页（被全站引用）

---

## Concepts（概念 / 合规 / 规范）

- [[product-overview]] ★ — BMOS 产品族总览 + 多系统编号 + 主数据边界 + 两端模型。✅
- [[menu-structure]] ★ — 完整菜单树 + 5 级编码规则 + 状态图例。✅
- [[version-publish-mechanism]] ★ — 版本发布统一范式（编辑→确认→审批→发布→生效→失效）。✅
- [[audit-trail-and-logs]] — 审计追踪按钮清单模式 + 四大日志 + 111 审计中台。✅
- [[esignature]] — 电子签名 + 签名触发点表 + 第二签名者范围。✅
- [[approval-flow]] — 审批流统一架构 + MES 内置流程类型 + 会签/或签。✅
- [[data-permission]] — 部门维度数据权限 + 首次保存授权。✅
- [[numbering-rule]] — 编号规则引擎 + 应用范围。✅
- [[common-interaction-spec]] ★ — 通用交互/组件规范。✅

## Master-data（主数据实体）

- [[material-master]] ★ — 物料(platform 真相源 + 下发/同步 + 量值模型)。✅
- [[equipment-master]] — 设备(设备类 + 功能点模板 + 秤具/打印机分组 + 数采映射)。✅
- [[location-master]] — 货位与暂存货位(WMS 货位 vs MES 暂存货位 + 货品件↔物料件转换)。✅
- [[recipe-bom-master]] — 配方 + 生产 BOM(数量类型 + 允差 + 折干折纯)。✅
- [[record-dataset-master]] — 电子记录(Word→组件) + 数据集(数据点-组件绑定)。✅

## Domains（业务域总览）

- [[domain-production-config]] — 020 生产配置（流程/编号/操作规程/记录/配方/工艺/BOM/公式/暂存间配置）。✅
- [[domain-production-management]] — 030 生产管理 + 050 生产查询（计划/指令单/班组/配料/暂存间/异常 + 历史/批记录打印/日志查询）。✅
- [[domain-lot-release]] — 040 批签发（数据集/模板/管理/审核/文件）。✅
- [[domain-weighing]] — 称量（称量中心/工单/配料称量/物料称量/称量数据/配液）。✅
- [[domain-warehouse-interaction]] — 仓储交互 WMS+MES（货品/货位/库存/发料/领料/出入库）。✅
- [[domain-inspection]] — 检验 LIMS 交互（请验单/检验结果组件）。✅
- [[domain-system-base]] — 系统基础（登录门户/消息/字典/权限/参数/应用/标签）。✅

## Features（核心功能深化）

- [[feature-process-and-execution-model]] — 工艺/配方/BOM/记录四者关系 + 工序步骤 + 工作流 + 移动端执行。✅
- [[feature-weigh-center]] — 四种称量模式 + 称量工单生命周期 + 双签名。✅
- [[feature-lot-release-mgmt]] — 批签发端到端（数据集→模板→生成→审核→文件）。✅

## Flows（端到端业务流程）

- [[flow-batch-production]] — 批生产全流程（计划→分解→执行→投料→产出→归档）。✅
- [[flow-weighing-execution]] — 称量执行流程（配料→需求→工单→签名→投料）。✅
- [[flow-material-flow]] — 物料流转主链 + 追溯（注册→下发→入库→发料→领料→消耗→产出→追溯）。✅
- [[flow-compliance-traceability]] — 合规追溯链（四大日志→审计中台 + 签名/审批流追溯）。✅

## 治理文件

- [[SCHEMA]] — 约定 + frontmatter + 标签 + 页面分工规则。✅
- index.md（本文件）· log.md（操作审计）
