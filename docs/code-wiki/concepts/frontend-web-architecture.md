---
title: 前端 Web 架构
created: 2026-06-30
updated: 2026-06-30
type: concept
service: cross
tags: [frontend-web, architecture, vue, tech-stack]
sources:
  - packages/frontend/apps/web/package.json
  - packages/frontend/apps/web/pnpm-workspace.yaml
  - packages/frontend/apps/web/apps/bmos-mes-web/vite.config.ts
  - packages/frontend/apps/web/apps/bmos-mes-web/src/bootstrap.ts
status: active
---

# 前端 Web 架构

> bmos Web 端的整体架构模式:pnpm workspace 多 app + @bmos/* 共享库分层 + 统一启动/路由/构建约定。实体索引见 [[web-overview]]、[[web-shared-packages]]。

## 架构分层

```
web/ (pnpm workspace 根)
├── apps/bmos-<x>-web/   # 12 个业务 app(SPA;platform-web 为 MPA)
│   └── src/{main,bootstrap,render,router,pages,stores,services,...}
└── packages/@bmos-*/    # 7 个共享库(源码直引,workspace:^)
```

- **包管理**:pnpm(`pnpm-lock.yaml` + `pnpm-workspace.yaml`,`packageManager: pnpm@8.5.0`),workspace = `packages/*` + `apps/*` + `docs` + `openApi`
- **共享库源码直引**:各 @bmos/* 的 `main`/`module` 指向 `src/index.ts`,不经预编译,由 Vite 编译,改库即时生效
- **统一依赖**:11 个 bmos-* app 全套依赖 6 个基础库(axios/components/i18n/icons/messager/utils)

## 统一启动 chain（所有 app 一致）

`index.html → main.ts(仅 import bootstrap) → bootstrap.ts(async IIFE) → render.ts(动态 import 挂载)`:

1. `Auth({})`(@bmos/messager):**SSO 单点登录**——取 token → 调 `/api/app/platform/user/status` 验证 → 写 token/userInfo
2. `asyncMenu()`:调后端菜单接口拿菜单树 → `router.addRoute` 注入
3. `handleLang()`:i18n(@bmos/i18n)初始化
4. `import('./render')`:创建 app + 注册组件库 + 挂载

> 这是跨所有 app 的统一鉴权/启动模式,改启动逻辑先看 `bootstrap.ts`。

## 路由装配（静态 + 动态混合）

- **静态**:各 app `router/` 导出 `asyncRoutes`(按 `meta.id` 预定义所有页面 component 映射) + `constantRoutes`(首页等固定路由)
- **动态**:后端菜单接口下发菜单树 → 按 `meta.id` 匹配 `asyncRoutes` → `router.addRoute('Index', item)` 装配
- **关键**:菜单结构/权限由 **platform 后端**控制,页面组件由各 app 前端静态定义——后端给菜单 id,前端按 id 找组件

> 菜单接口:mes `getMenuList({rootMenuCode:120})`;platform `/api/app/platform/menu/auth/tree`(见 [[platform-user-module]] 动态菜单下发)。

## 共享库职责分层

| 层 | 库 | 角色 |
|---|---|---|
| 传输 | @bmos/axios | HTTP 客户端(拦截器带 token、统一响应解包) |
| 认证 | @bmos/messager | SSO/Auth(入口第一步)、消息桥、锁屏 |
| UI | @bmos/components | 20 个业务组件(Vue 插件全局注册) |
| UI | @bmos/icons | 图标 |
| 国际化 | @bmos/i18n | i18next 单例(`t` 被 auto-import) |
| 工具 | @bmos/utils | 16 子模块(部分 auto-import) |

> @bmos/auth 是 messager 的旧别名(re-export),非独立包。

## Vite 统一插件栈与部署

- **无根 vite 配置**,各 app 自带 `vite.config.ts`,插件栈统一:`@vitejs/plugin-vue`(+jsx)、`unplugin-vue-components`(AntDesignVueResolver 按需)、`unplugin-auto-import`(vue/vue-router + @bmos/i18n/utils)、svg-icons
- **proxy**:`target = VITE_API_HOST`(.env.development,默认测试环境 IP,统一走网关)
- **多 app 部署**:dev server 统一 port **8083**(靠 base 路径 `/app/bmos-<x>` 区分,不能同时起多个);生产 base 同路径
- **auto-import dts 已关**(`dts:false`),`auto-imports.d.ts`/`components.d.ts` 为遗留产物

## 关键架构事实

- **12 app vs 5 后端的不对称**:7 个 app(audit/bims/bsms/dc/el/ems/lisms)后端不在 5 主服务内
- **services 层 OpenAPI 生成**:`bmosapi.json` → `services/types.d.ts`(mes 已接入)
- **bmos-platform-web 是唯一 MPA**(bmos/login/download/kskt 多入口),其余 SPA

## 相关页面

- [[web-overview]] — 12 app 规模与后端映射(实体索引)
- [[web-shared-packages]] — 7 个 @bmos/* 详解
- [[frontend-apps-overview]] — app 速查表
- [[auth-and-license]] — 后端 token 校验(与 messager.Auth 对应)
- [[monorepo-architecture]] — pnpm workspace 整合
