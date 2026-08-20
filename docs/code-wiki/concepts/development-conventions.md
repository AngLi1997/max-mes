---
title: 开发规范（命名 / 提交 / 版本 / 端口）
created: 2026-06-30
updated: 2026-06-30
type: concept
service: cross
tags: [architecture, convention, deploy, backend, frontend-web]
sources:
  - CLAUDE.md
  - docs/code-wiki/SCHEMA.md
status: active
---

# 开发规范（命名 / 提交 / 版本 / 端口）

> bmos-monorepo 全仓通用的工程约定汇总。权威来源:根 `CLAUDE.md` + 各服务 pom/package.json。

## 命名约定

| 维度 | 约定 | 例子 |
|---|---|---|
| 后端服务模块 | `bmos-<service>-<module>` | `bmos-mes-service` / `bmos-platform-facade` |
| Java 包 | `com.bmos.<service>.<module>` | `com.bmos.mes.service` / `com.bmos.lims2.server` ⚠️ lims2 |
| 后端 Nacos 服务名 | `bmos-<service>-service` | `bmos-mes-service`(注册名) |
| Web app | `bmos-<x>-web` | `bmos-mes-web` / `bmos-platform-web` |
| 前端共享库 | `@bmos/<x>` | `@bmos/axios` / `@bmos/components` |
| 数据库表 | `<前缀>_<实体>` | platform `bp_`、mes `bm_`、wms `bw_`、lims `lm_`(见 [[data-access-pattern]]) |

> ⚠️ **lims 命名分裂**:groupId `com.bmos.lims` / artifactId `bmos-lims2` / Java 包 `com.bmos.lims2.*` / 服务名 `bmos-lims2-service` / context-path `/api/app/lims2`——五层不一致。详见 [[lims-overview]]。

## 端口与 context-path

| 服务 | 端口 | context-path |
|---|---|---|
| platform | 60100 | `/api/app/platform` |
| mes | 60200 | `/api/app/mes` |
| gateway | 60300 | `/`(网关入口) |
| wms | 60900 | `/api/app/wms` |
| lims | 61001 | `/api/app/lims2` ⚠️ |

> 前端请求统一走网关 `/api/app/<service>/**`,由 gateway 路由到下游(路由在 Nacos,见 [[api-conventions]])。

## 版本策略

- **无根 POM**:monorepo 不统一后端版本,各服务保留独立 pom.xml 与 bmos.version。
- bmos.version 差异:platform/mes `1.15.0`、lims `1.15.2`、gateway/wms `1.14.0`(详见 [[monorepo-architecture]])。
- 前端共享库用 `workspace:^`(pnpm workspace),版本字段统一 `0.0.0`,源码直引不经预编译。
- 整合方式:后端服务 + 前端 app 均经 **git subtree add --squash**(完全隔离、保留历史)。

## 提交与语言

- **Conventional Commits**(`feat:`/`fix:`/`docs:` 等);前端 web 配 husky + lint-staged + commitlint。
- **语言**:代码标识符英文;注释/文档/AI 回复统一中文。

## 服务边界（硬性）

- 每个后端服务独立部署、独立 pom.xml、独立数据库。
- **服务间只通过 Feign 通信,禁止直连他服务数据库**(见 [[service-integration]])。
- 跨服务取数走 Feign client(platform facade 暴露给 mes/lims/wms)。

## 错误码段位

按服务分配:platform `81`、mes `82`、wms/lims `83`。详见 [[api-conventions]]。

## 配置

- 业务配置(数据源/redis/mybatis-plus/路由/白名单)全托管 **Nacos**,本地仅 `bootstrap.yml` 拉取。
- 服务发现 + 配置 + i18n 消息均在 Nacos。

## 历史约束

- 工作区源码曾受 **TSD 加密**(gateway/wms/lims),已于 2026-06-30 解密,现可直接读。详见 [[monorepo-architecture]]。

## 相关页面
- [[monorepo-architecture]] — subtree 整合、版本策略、TSD 历史
- [[service-integration]] — Feign 调用方向与边界
- [[api-conventions]] / [[data-access-pattern]] — 命名相关的错误码段位/表前缀
- [[service-overview]] — 端口/服务名速查
