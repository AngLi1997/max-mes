# Wiki Index

> bmos-monorepo 代码知识库内容目录。
> 每个 wiki 页面按【类型】和【服务】列出，并附一行摘要。
> **AI 查询时先读此文件定位相关页面**，再用对应页 frontmatter 的 `sources:` 跳转到代码。
> 最后更新：2026-07-20 | 状态：✅ 已建成 42 页（骨架规划 29 页 + mes 头部 16 子域深化 + platform 子模块（auth/user/equipment）+ 后端共享 starter）—— 后端 5 服务（含 mes 16 子域、platform 4 模块）+ 后端共享脚手架（bmos-parent-starter）+ 8 概念页 + 前端 Web/移动端 + 3 速查 + 4 API 页
> 最后更新：2026-07-20 | 状态：✅ 已建成 42 页（骨架规划 29 页 + mes 头部 16 子域深化 + platform 4 子模块 + 后端共享 starter）—— 后端 5 服务（含 mes 16 子域、platform 4 模块）+ 后端共享脚手架（bmos-parent-starter）+ 8 概念页 + 前端 Web/移动端 + 3 速查 + 4 API 页

---

## 如何使用本知识库

> **新 AI 接手前必读**：
> - [[PLAYBOOK]] — 顶层入口（按前端/后端分流）
> - [[SCHEMA]] — 约定与格式（前后端通用）
> - 按任务读对应分册：[[PLAYBOOK-backend]] 或 [[PLAYBOOK-frontend]]
>
> 后端单分册 + SCHEMA + 本文 < 600 行。读完即可建页。

1. 任何任务先读本文件 + 根目录 `CLAUDE.md`
2. 按 `service` 维度缩小到具体服务/应用（见下方分区）
3. 打开对应 entity 页，用 `sources:` 路径定位代码
4. 跨服务调用先查 [[service-integration]]
5. **新增页面 / 新服务接入**：按对应 PLAYBOOK 分册的五步法 + checklist 操作

---

## Concepts（概念 / 跨服务）

> 架构模式、数据流、开发规范。状态：8/8 全部建成 ✅

- [[monorepo-architecture]] - 整体架构、git subtree 整合、Maven/pnpm 版本策略、TSD 加密约束。✅
- [[service-integration]] - 服务间 Feign 调用矩阵、调用方向、契约模块和悬空依赖。✅
- [[auth-and-license]] - 认证与 license 校验流程（platform 主导）。✅
- [[frontend-web-architecture]] - Web 端 pnpm workspace + @bmos/* 共享库 + Ant Design Vue 架构。✅
- [[frontend-mobile-architecture]] - UniApp 多端（H5/App/小程序）构建与部署。✅
- [[data-access-pattern]] - MyBatis-Plus + ShardingSphere 数据访问规范。✅
- [[api-conventions]] - 网关路由、统一响应、异常码约定。✅
- [[development-conventions]] - 命名 / 提交 / 分支 / 代码风格统一规范。✅

## Entities（实体 / 按服务）

### 后端服务

- **platform**：[[platform-overview]] ✅ · [[platform-auth-module]] ✅ · [[platform-user-module]] ✅ · [[platform-equipment-module]] ✅
- **platform**：[[platform-overview]] ✅ · [[platform-auth-module]] ✅ · [[platform-user-module]] ✅ · [[platform-factory-module]] ✅
- **mes**：[[mes-overview]] ✅ · [[mes-product-module]] ✅ · [[mes-record-module]] ✅ · [[mes-process-module]] ✅ · [[mes-plan-module]] ✅ · [[mes-storage-module]] ✅ · [[mes-workflow-module]] ✅ · [[mes-execute-module]] ✅ · [[mes-dataset-module]] ✅ · [[mes-lotrelease-module]] ✅ · [[mes-audit-module]] ✅ · [[mes-weigh-module]] ✅ · [[mes-ingredient-module]] ✅ · [[mes-requisition-module]] ✅ · [[mes-inspect-module]] ✅ · [[mes-preparation-module]] ✅
- **lims**：[[lims-overview]] ✅
- **wms**：[[wms-overview]] ✅
- **gateway**：[[gateway-overview]] ✅

### 后端共享（`packages/backend/shared/`）

- **shared**：[[parent-starter-overview]]（bmos-parent-starter 父级脚手架，16 子模块 + BOM，2026-07-15 subtree 引入）✅

### 前端

- **web**：[[web-overview]]（12 个 app 总览）✅ · [[web-shared-packages]]（@bmos/auth|axios|components|i18n|icon|messager|utils）✅
- **mobile**：[[mobile-overview]]（mes-app / lims-app，UniApp 多端）✅

## Comparisons（对比 / 速查）

- [[service-overview]] - 5 个后端服务端口/注册名/规模/职责速查。✅
- [[database-schema-overview]] - 各服务核心表按业务域分组 + 表前缀→服务映射速查。✅
- [[frontend-apps-overview]] - 12 个 web app + 2 个移动端速查。✅

## API（接口规范 / 按服务）

- [[platform-api]] ✅ · [[mes-api]] ✅ · [[lims-api]] ✅ · [[wms-api]] ✅

---

## 服务/应用真实清单（骨架阶段已确认）

> 以下为从代码仓库实际扫描结果，作为后续建页的依据。

**后端服务**（`packages/backend/services/`）：`gateway` · `lims` · `mes` · `platform` · `wms`

**Web 应用**（`packages/frontend/apps/web/apps/`）：
`bmos-audit-web` · `bmos-bims-web` · `bmos-bsms-web` · `bmos-dc-web` · `bmos-el-web` · `bmos-ems-web` · `bmos-lims-web` · `bmos-lisms-web` · `bmos-mes-web` · `bmos-platform-web` · `bmos-wms-web` · `demo`

**Web 共享库**（`packages/frontend/apps/web/packages/`）：
`@bmos/auth` · `@bmos/axios` · `@bmos/components` · `@bmos/i18n` · `@bmos/icon` · `@bmos/messager` · `@bmos/utils`

**移动端 / 模板**（`packages/frontend/apps/`）：
`mes-app` · `lims-app` · `app-build-template` · `lims-app-build-template`

---

> 图例：⏳ 待创建 · ✅ 已完成 · 🚧 进行中
