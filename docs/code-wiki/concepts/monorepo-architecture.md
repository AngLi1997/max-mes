---
title: Monorepo 整体架构
created: 2026-06-30
updated: 2026-06-30
type: concept
service: cross
tags: [backend, frontend-web, frontend-mobile, architecture, deploy, convention]
sources:
  - packages/backend/services/
  - packages/frontend/apps/
  - docs/MONOREPO_INTEGRATION_PLAN.md
  - CLAUDE.md
status: active
---

# Monorepo 整体架构

## TL;DR

- bmos-monorepo 用 **git subtree(`--squash`)** 把 5 个后端微服务 + 前端 Web + 2 移动端整合到一个仓库,**完全隔离、保留历史、独立发展**(源仓库与 monorepo 互不回推)。
- **后端无统一根 POM**:各服务沿用迁移前独立 POM,自行维护依赖(2026-06-29 决策);`bmos.version` 在 1.14.0~1.15.2 间分歧,是已知技术债。
- **前端工作区**用 **pnpm workspace**(`packages/frontend/apps/web/`,有 `pnpm-lock.yaml` + `pnpm-workspace.yaml`,`packageManager: pnpm@8.5.0`)。
- ⚠️ 历史约束:gateway/wms/lims 的工作区 `.java` 曾被 TSD 加密(**已于 2026-06-30 解密**),现均可直接读;mes/platform 一直是明文。

## 顶层结构

```
bmos-monorepo/
├── CLAUDE.md                       # AI 导航入口 + 功能归属决策树
├── docs/
│   ├── MONOREPO_INTEGRATION_PLAN.md  # 架构演进蓝图(12 章,2500+ 行)
│   └── code-wiki/                  # ★ 本 AI 代码知识库
└── packages/
    ├── backend/
    │   ├── services/{gateway,lims,mes,platform,wms}/  # 5 微服务,各自独立 pom
    │   └── shared/                 # bmos-parent-starter(2026-07-15 subtree 引入,见 [[parent-starter-overview]])
    └── frontend/apps/
        ├── web/                    # 工作区根:apps/(12 web app) + packages/(@bmos/* 共享库)
        ├── mes-app/  lims-app/     # UniApp 移动端
        └── app-build-template/  lims-app-build-template/  # 构建模板
```

> **无根级 `pom.xml`**。后端各服务独立构建;前端 pnpm workspace 根在 `packages/frontend/apps/web/`(该目录下有 `package.json` + `pnpm-workspace.yaml`)。

## git subtree 整合方式

- 命令形如 `git subtree add --prefix=<path> <repo-url> <branch> --squash`。
- `--squash`:源仓库历史压缩为 1 个 commit,保持 monorepo log 整洁。
- **完全隔离**:源仓库后续改动不影响 monorepo,monorepo 的改动也不回推。各服务保留独立 pom/版本,可独立部署。
- 来源(见 `MONOREPO_INTEGRATION_PLAN.md` 第 10.2 节):
  - 后端 5 服务从各自源仓库 main 分支导入
  - 前端从 `172.16.0.180` 内网 gitlab 的多个仓库 master 分支导入(web / mes-app / lims-app / 两个模板)

## 后端版本策略(当前:各服务独立)

> **决策(2026-06-29)**:不引入 `packages/backend/pom.xml` 根聚合 POM,各服务沿用独立 POM。详见 `MONOREPO_INTEGRATION_PLAN.md` §4.1。

**为什么暂不统一**:`bmos.version` 控制整套 `com.bmos:*` 框架制品(BOM + `bmos-starter-*`)版本,各服务真实分歧:

| 服务 | bmos.version |
|---|---|
| platform / mes | 1.15.0-SNAPSHOT |
| lims | 1.15.2-SNAPSHOT |
| gateway / wms | 1.14.0-SNAPSHOT |

> 强行统一 = 一次重构顺带升级多服务框架,把"整理依赖"与"升级框架"两件高风险事捆绑。后续目标:先建根 POM 只收口本就一致的依赖,分歧项显式保留在各服务 POM 作为覆盖,**每次只动一个服务并回归**。

wms 内部还有更细分歧:父 pom `1.14.0` 而 `bmos-wms-feign` 模块 `1.15.0`。

## 前端工作区

- **工作区根**:`packages/frontend/apps/web/`,**pnpm workspace**(`pnpm-workspace.yaml` + `package.json.workspaces` = `packages/*`/`apps/*`/`docs`/`openApi`,`packageManager: pnpm@8.5.0`),脚本用 `pnpm --filter ./apps/**` 驱动。
- **无根级 vite 共享配置**:12 个 web app 各自维护 `vite.config.ts`。
- **Auto-Import** 全启用(`unplugin-auto-import` + `components.d.ts`)。
- **移动端**(`mes-app`/`lims-app`)在该 workspace 之外,各自独立。

## 部署 / 构建

- 后端:各服务 `mvn clean package` 产出独立 JAR(finalName = `bmos-<svc>-service`),运行时 `java -jar` 不需要 pom。多层级 POM 对 CI/CD 打包/启动无负面影响(Maven 递归找 parent,打包时依赖已固定)。详见 `MONOREPO_INTEGRATION_PLAN.md` §4.1 FAQ。
- 注册中心:Nacos(`${NACOS_HOST}:${NACOS_PORT:8848}`),服务发现 + 配置中心 + i18n。
- 网关统一入口:前端请求 → gateway(60300)→ `lb://<svc>` 转发,见 [[gateway-overview]]。

## 历史约束:TSD 源码加密(已于 2026-06-30 解密)

gateway / wms / lims 三个服务的工作区 `.java`(及部分 yml)**曾被 TSD 加密工具注入 `%TSD-Header-###%` 头(8192 字节块)**,导致直接 Read/Grep 工作区得到乱码,当时只能 `git show HEAD:<path>` 读源码;mes / platform 一直是明文。

| 服务 | 工作区源码 |
|---|---|
| mes / platform | ✅ 一直明文 |
| gateway / wms / lims | ✅ **现已解密,可直接读**(历史加密期需 `git show HEAD:<path>`) |

**当前状态**:三个服务已解密,IDE / ripgrep / CI / wiki 管线均可直接扫描工作区。若未来新接入服务再遇 `%TSD-Header-###%` 乱码,回退用 `git show HEAD:<path>` 或 `cat`(本页历史描述保留作排障参考)。

## ⚠️ 其它跨服务技术债

- **命名分裂(lims)**:groupId `com.bmos.lims` / artifactId `bmos-lims2` / Java 包 `com.bmos.lims2.*` / 服务名 `bmos-lims2-service` / context-path `/api/app/lims2`。端点统一用 `lims2`。详见 [[lims-overview]]。
- **循环依赖**:wms / lims 的 `application.yml` 显式 `spring.main.allow-circular-references=true`,Bean 循环依赖未根治。
- **跨服务共享表读写归属**:`bm_execute_form_data*` / `bm_batch_record_version` 名义属 mes,但 lims eln 子域也读写(双写,一致性风险)。`bm_log_operation` / `bm_operate_rule*` 用 `bm_` 前缀但归属 lims。详见 [[service-integration]]、[[database-schema-overview]]。
- **版本不一致**:见上方版本策略表。

## AI 定位提示

- **改业务逻辑** → 按 [[index]] 的 service 维度定位到具体服务(决策树见根 `CLAUDE.md`)。
- **读 gateway/wms/lims 源码** → 已解密,直接读工作区(历史加密期才需 `git show HEAD:<path>`)。
- **跨服务取数** → 走 Feign(见 [[service-integration]]),禁止跨库直连。
- **改前后端联调** → 前端 `/api/app/<svc>` 对照 [[service-overview]] 的 context-path。
- **整合演进 / 新服务接入** → 先读 `docs/MONOREPO_INTEGRATION_PLAN.md` + [[PLAYBOOK]]。

## 相关页面

- [[service-overview]] — 5 服务端口/规模速查
- [[service-integration]] — Feign 调用矩阵与方向
- [[database-schema-overview]] — 表前缀→服务映射
- [[frontend-apps-overview]] — 前端 12 web app + 移动端
- [[gateway-overview]] / [[lims-overview]] / [[wms-overview]] — 加密服务详解
- [[development-conventions]] — 命名/提交/分支规范
