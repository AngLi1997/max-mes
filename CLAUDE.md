# CLAUDE.md

> 本文件是 AI（Claude Code 等）在 bmos-monorepo 中工作的**导航入口与硬性规则**。
> 任何任务开始前必须先读本文件，再读 `docs/code-wiki/index.md` 定位。
> 回复统一使用中文。

## 项目概述

bmos-monorepo 是将原本分散在多个仓库的微服务、前端应用、共享库整合后的 **Monorepo**。

- **后端**：Spring Boot 2.6.15 微服务 ×5，Maven 构建，通过 git subtree 整合（各服务保留独立 pom.xml 与版本）。
  - `platform`（平台基础：认证/用户/配置/license）、`mes`（制造执行）、`lims`（实验室信息）、`wms`（仓库管理）、`gateway`（API 网关）
  - 技术栈：MyBatis-Plus、ShardingSphere、Feign（服务间调用）、MinIO（文件）、EasyExcel/POI
- **前端 Web**：`packages/frontend/apps/web/` 是 pnpm workspace，含 12 个业务 app + 7 个 `@bmos/*` 共享库。
  - 技术栈：Vue 3.5 + Vite 5 + Ant Design Vue 4 + Pinia + TypeScript
- **前端移动端**：`packages/frontend/apps/{mes-app,lims-app}` 基于 UniApp（H5 / App / 小程序多端）+ Electron 桌面模板。

详细架构演进见 `docs/MONOREPO_INTEGRATION_PLAN.md`。

## AI 处理流程（强制）

1. 先读 `docs/code-wiki/index.md` 定位相关页面
2. 按 `service` 字段缩小到具体服务/应用
3. 用对应 entity 页 frontmatter 的 `sources:` 路径直接打开代码
4. 跨服务调用先查 `[[service-integration]]`（Feign 链路与依赖方向）
5. 改完代码后，在 `docs/code-wiki/log.md` 追加记录，并更新受影响页的 `updated`

> **新建 wiki 页 / 新服务接入**：先读 `docs/code-wiki/PLAYBOOK.md`（顶层入口，按前端/后端分流）+ `docs/code-wiki/SCHEMA.md`（约定与格式）+ 对应分册（`PLAYBOOK-backend.md` 或 `PLAYBOOK-frontend.md`）。后端单分册流程下读完不到 600 行即可建页。

## 功能归属决策树

```
需要实现/修复什么？
├─ 后端业务逻辑（数据处理、规则判断）
│   └─ → packages/backend/services/<对应服务>/...-service/
│         业务归属：制造→mes，实验室→lims，仓库→wms，用户/认证/license→platform
├─ 跨服务取数 / 调用其它服务
│   └─ → 走 Feign client（见 [[service-integration]]），禁止直连他服务数据库
├─ API 网关路由 / 鉴权
│   └─ → packages/backend/services/gateway/
├─ Web 管理界面
│   └─ → packages/frontend/apps/web/apps/bmos-<x>-web/
├─ 移动端 / 小程序
│   └─ → packages/frontend/apps/<x>-app/（UniApp）
└─ 共享 UI / 工具 / HTTP / i18n
    └─ → packages/frontend/apps/web/packages/（@bmos/*）
```

## 关键约定

- **服务边界**：每个后端服务独立部署、独立 pom.xml；服务间只通过 Feign 通信，不跨库直连。
- **数据访问**：统一 MyBatis-Plus；分库分表用 ShardingSphere。
- **前端依赖**：web 应用引用共享库用 `workspace:*`；包名 `@bmos/<x>`。
- **命名**：后端 `bmos-<service>-<module>`（如 `bmos-mes-service`）；Java 包 `com.bmos.<service>.<module>`；web app `bmos-<x>-web`。
- **语言**：代码标识符英文；注释/文档/AI 回复中文。
- **提交**：Conventional Commits。

## 知识库结构

```
docs/
├── MONOREPO_INTEGRATION_PLAN.md   # 架构整合/演进蓝图
└── code-wiki/                     # ★ AI 代码知识库
    ├── SCHEMA.md                  # 约定 + frontmatter 模板 + 标签体系
    ├── index.md                   # ★ 总目录（查询第一入口）
    ├── log.md                     # 操作审计（仅追加）
    ├── concepts/                  # 跨服务架构/数据流/规范
    ├── entities/_<service>/       # 按服务/应用的实体页
    ├── comparisons/               # 横向速查表
    └── api/                       # 接口规范（按服务）
```

> 当前知识库处于**骨架初始化**阶段：治理文件与导航已就绪，具体页面以 `index.md` 中 ⏳ 标注，按 P1→P4 逐步填充。
