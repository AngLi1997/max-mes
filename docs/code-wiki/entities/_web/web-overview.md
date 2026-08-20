---
title: Web 前端工程总览
created: 2026-06-30
updated: 2026-06-30
type: entity
service: web
tags: [frontend-web, vue, architecture, tech-stack]
sources:
  - packages/frontend/apps/web/package.json
  - packages/frontend/apps/web/pnpm-workspace.yaml
  - packages/frontend/apps/web/apps/
  - packages/frontend/apps/web/packages/
status: active
---

# Web 前端工程总览

## 概述 / 职责

bmos 的 Web 管理端工程,位于 `packages/frontend/apps/web/`。**pnpm workspace**(`pnpm-lock.yaml` + `pnpm-workspace.yaml`,`packageManager: pnpm@8.5.0`),含 12 个业务 app + 7 个 `@bmos/*` 共享库。

> ⚠️ **更正**:此前 wiki 记为"npm/yarn workspaces"有误,实测为 **pnpm workspace**(有 `pnpm-lock.yaml` 与 `pnpm-workspace.yaml`)。

## 技术栈

Vue **3.5.12** · Vite **^5.3.1** · Ant Design Vue **^4.2.6** · Pinia **^2.1.6** · Vue Router **^4.2.4** · TypeScript **~5.2.0**。构建用 terser(生产 drop_console/drop_debugger)。

## workspace 结构

```
web/
├── apps/        # 12 个业务 app(bmos-*-web + demo)
├── packages/    # 7 个 @bmos/* 共享库
├── docs/ · openApi/
└── pnpm-workspace.yaml   # packages/* + apps/* + docs + openApi
```

- 根脚本:`dev`=`pnpm --filter ./apps/** run dev`(并行起所有 app);单 app 快捷 `dev:mes`/`build:mes` 等
- registry:淘宝镜像(`.npmrc`);Node:`.nvmrc` v16.20 但 `engines.node>=20.15`(矛盾)

## 12 个业务 app（规模 .vue / 业务域）

| app | 对应后端 | .vue | 业务域 |
|---|---|---|---|
| bmos-mes-web | mes | 311 | 制造执行(最大) |
| bmos-bsms-web | bsms* | 294 | 设备/基础管理 |
| bmos-lisms-web | lims 衍生 | 149 | 实验室样品/采样 |
| bmos-bims-web | bims* | 112 | BIMS |
| bmos-lims-web | lims | 91 | 实验室信息管理 |
| bmos-platform-web | platform | 85 | 平台(认证/用户/菜单)·多入口 MPA |
| bmos-ems-web | ems* | 76 | 设备/能源 |
| bmos-dc-web | dc* | 73 | 数据采集(mqtt) |
| bmos-wms-web | wms | 65 | 仓库管理 |
| bmos-el-web | el* | 40 | EL(mqtt) |
| bmos-audit-web | audit* | 31 | 设计追溯 |
| demo | 无 | 21 | 组件演示 |

> ⚠️ 标 * 的 7 个 app(audit/bims/bsms/dc/el/ems/lisms)后端**不在 5 个主服务**(platform/mes/lims/wms/gateway)内,归属待核实(可能是 mes/lims 子模块或独立服务)。

## 7 个共享库 @bmos/*（详见 [[web-shared-packages]]）

| 库 | 职责 |
|---|---|
| @bmos/axios | HTTP 客户端封装 |
| @bmos/messager | **认证/SSO/消息桥**(Auth 是所有 app 入口第一步) |
| @bmos/components | 业务组件库(20 组件,Vue 插件全局注册) |
| @bmos/icons | 图标组件 |
| @bmos/i18n | 国际化(i18next 单例) |
| @bmos/utils | 通用工具(16 子模块,部分 auto-import) |
| @bmos/auth | messager 的旧别名(re-export,非独立包) |

> 11 个 bmos-* app 统一全套依赖前 6 个库(`workspace:^`)。

## 统一启动 chain

`index.html → main.ts(仅 import bootstrap) → bootstrap.ts → render.ts`:

1. `Auth({})`(@bmos/messager):SSO 鉴权 → 取 token → 调 `/api/app/platform/user/status` 验证
2. `asyncMenu()`:调后端菜单接口拿菜单树 → `router.addRoute` 注入
3. `handleLang()`:i18n 初始化
4. `import('./render')`:渲染

## 路由来源（静态 + 动态混合）

- **静态**:各 app `router/` 导出 `asyncRoutes`(按 `meta.id` 预定义所有页面 component 映射) + `constantRoutes`
- **动态**:后端菜单接口(mes `getMenuList({rootMenuCode:120})`、platform `/menu/auth/tree`)下发菜单树 → 按 `meta.id` 匹配 `asyncRoutes` 装配
- **菜单/权限由 platform 后端控制,页面组件由各 app 前端静态定义**

## Vite 关键配置

- **无根 vite 配置**,各 app 自带 `vite.config.ts`,插件栈统一:`@vitejs/plugin-vue`(+jsx)、`unplugin-vue-components`(AntDesignVueResolver 按需)、`unplugin-auto-import`(vue/vue-router + @bmos/i18n/utils 注入)、svg-icons
- **proxy**:`target = VITE_API_HOST`(.env.development,默认测试环境 IP,走网关)
- **dev server** 统一 port **8083**(多 app 不能同时 dev,靠 base 路径区分)
- **base**:`/app/bmos-<x>`(部署路径区分各 app)
- 别名:`@ → ./src`
- ⚠️ `auto-imports.d.ts`/`components.d.ts` 存在但 vite 配置 `dts:false`(遗留产物,不再生成)

## 隐藏地雷 ⚠️

1. **pnpm 而非 npm/yarn**(此前 wiki 误判)。
2. **Node 版本矛盾**:`.nvmrc` v16.20 vs `engines>=20.15`。
3. **7 个 app 后端归属不明**(不在 5 主服务内)。
4. **services 层 OpenAPI 自动生成**(`bmosapi.json` → `services/types.d.ts`,mes 已接入,types.d.ts 达 2000+ 行)。
5. **多 app 共端口 8083**:dev 时靠 base 路径区分,不能同时起多个。
6. **auto-import dts 已关**,类型声明靠遗留文件/手动维护。

## AI 定位提示

- 找 **UI/页面** → `apps/<app>/src/pages/<域>/`
- 找 **请求** → `apps/<app>/src/services/`(注意可能 OpenAPI 生成)
- 找 **状态** → `apps/<app>/src/stores/`
- 找 **鉴权/SSO/启动** → `bootstrap.ts` + `@bmos/messager`
- 找 **路由/菜单装配** → `router/`(asyncRoutes) + `utils/asyncMenu.ts`
- 找 **后端接口签名** → 按 context-path 对照 [[service-overview]]

## 相关页面

- [[web-shared-packages]] — 7 个 @bmos/* 共享库详解
- [[frontend-apps-overview]] — 12 app + 2 移动端速查
- [[frontend-web-architecture]] — 前端架构概念页(待建)
- [[service-overview]] — context-path → 后端服务映射
- [[monorepo-architecture]] — workspace 整合
