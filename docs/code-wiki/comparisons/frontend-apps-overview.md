---
title: 前端应用速查
created: 2026-06-30
updated: 2026-06-30
type: comparison
service: cross
tags: [frontend-web, frontend-mobile, architecture, vue, uniapp]
sources:
  - packages/frontend/apps/web/apps/
  - packages/frontend/apps/web/packages/
  - packages/frontend/apps/mes-app/
  - packages/frontend/apps/lims-app/
  - packages/frontend/apps/web/package.json
status: active
---

# 前端应用速查

> bmos-monorepo 前端:12 个 Web 应用 + 2 个 UniApp 移动端 + 2 个构建模板。
> 数据来自 `src/services` 的 `/api/app/X` 调用统计、`package.json` 依赖与文件计数(2026-06-30)。

## TL;DR · 后端服务 → Web App 映射

| 后端服务(context-path) | 对接的 Web App |
|---|---|
| **mes** `/api/app/mes` | bmos-mes-web、bmos-lims-web(⚠️ 命名错配,见地雷) |
| **wms** `/api/app/wms` | bmos-wms-web、bmos-bims-web、bmos-bsms-web、bmos-lisms-web |
| **platform** `/api/app/platform` | bmos-platform-web、bmos-audit-web |
| **ems** `/api/app/ems` | bmos-ems-web(⚠️ 仓库内无 ems 后端服务,外部部署) |
| **dc** `/api/app/dc` | bmos-dc-web、bmos-el-web(⚠️ 仓库内无 dc 后端服务,外部部署) |

> ⚠️ `ems` / `dc` 两个后端**不在本 monorepo**(`packages/backend/services/` 仅 gateway/lims/mes/platform/wms)。前端独立对接,排障时注意后端不在本仓库。

## 12 个 Web App 总表

规模分档:大 ≥150 .vue ｜ 中 60~149 ｜ 小 <60。

| App | .vue | .ts | stores | 主后端 | 职责 | 档 |
|---|---|---|---|---|---|---|
| **bmos-mes-web** | 311 | 387 | 3 | mes | 制造执行(批记录/放行/配方/生产/异常) | 大 |
| **bmos-bsms-web** | 294 | 460 | 4 | wms | 血液系统(血浆/检疫/标本/分拣/质检) | 大 |
| **bmos-lisms-web** | 149 | 222 | 5 | platform+wms | 实验室标本/检测/资源/物料/报表 | 中 |
| **bmos-bims-web** | 112 | 245 | 4 | wms | 生物样本(标本/物料/单据/汇总/报表) | 中 |
| **bmos-lims-web** | 91 | 189 | 3 | mes ⚠️ | 实验室基本数据/检验管理 | 中 |
| **bmos-platform-web** | 85 | 114 | 3 | platform | 平台管理(区域/设备/权限/系统/物料) | 中 |
| **bmos-ems-web** | 76 | 119 | 2 | ems | 设备管理(设备/日志/区域) | 中 |
| **bmos-dc-web** | 73 | 121 | 2 | dc | 数字中心(能源/环境/冷链/标牌/调度) | 中 |
| **bmos-wms-web** | 65 | 125 | 2 | wms | 仓库管理(配置/管理/查询) | 中 |
| **bmos-el-web** | 40 | 102 | 2 | dc | 数字大屏/看板(Home/Main/Outside) | 小 |
| **bmos-audit-web** | 31 | 47 | 2 | platform | 审计(日志/物料/质量/系统) | 小 |
| **demo** | 22 | 20 | 1 | — | 组件库演示站 | 小 |

## 共享库(@bmos/*)

`packages/frontend/apps/web/packages/` 下,**11 个业务 web app 全部共享 6 个基础库**:

| 共享库 | 用途 |
|---|---|
| `@bmos/axios` | HTTP 客户端封装(拦截器/统一响应) |
| `@bmos/components` | 通用组件(Form/Table/ModalForm/Select/PageComponent…) |
| `@bmos/i18n` | 国际化 |
| `@bmos/icons` | 图标 |
| `@bmos/messager` | 消息/通知 |
| `@bmos/utils` | 工具函数 |

- `@bmos/open` —— 仅 bmos-audit-web 与 demo 用(大屏/门户框架,不在 `packages/` 下,疑外部发布)
- `@bmos/auth` —— 目录存在但**未被任何 app 依赖列表引用**(待确认是否启用)

> 详见 [[web-shared-packages]]。Web 工程总览见 [[web-overview]]。

## 移动端(UniApp)

| 项 | mes-app | lims-app |
|---|---|---|
| 构建目标数 | **18**(dev/build 各 18) | 18(与 mes-app 完全相同) |
| 目标清单 | app/app-android/app-ios/h5/h5:ssr/mp-alipay/mp-baidu/mp-qq/mp-toutiao/mp-weixin/… | 同左 |
| pages.json 页数 | 132 | 132(与 mes-app 全等) |
| tabBar | 无(自定义首页导航) | 无 |
| manifest name/appid | `BMOS MES` / `__UNI__FD40210` | **完全相同** ⚠️ |
| #ifdef 条件编译文件 | 140 | 140 |
| 后端 | mes(535)+platform(70) | 同左 |

> ⚠️ **重大发现**:`lims-app` 当前是 `mes-app` 的**直接拷贝**(manifest/pages.json/#ifdef/API 调用全等,描述都还是 "mes-安卓App"),**尚未独立化为 LIMS 业务**。改动时两者需一起评估,或确认 lims-app 是否真在用。
> 技术栈:uni-app + uni-ui + uv-ui + wot-design-uni(`wd-*`),组件经 `pages.json` 的 `easycom` 自动注册(非 auto-import)。

## 工作区与构建机制

- **工作区根**:`packages/frontend/apps/web/`,**pnpm workspace**(`pnpm-lock.yaml` + `pnpm-workspace.yaml`,`packageManager: pnpm@8.5.0`):`"workspaces": ["packages/*", "apps/*", "docs", "openApi"]`。脚本用 `pnpm --filter ./apps/**` 驱动。
- **构建脚本**:`build:mes`/`build:wms`/`build:platform`/`build:audit`/`build:dc`/`build:ems`/`build:lims` 等单 app 别名;**未**为 bmos-bims/bsms/lisms/el/demo 提供单独构建脚本。
- **无根级 vite 共享配置**:每个 web app 各自维护 `vite.config.ts`。
- **Auto-Import**:全部 11 个业务 app 启用(`unplugin-auto-import` + `components.d.ts`)—— grep 找不到 import 时查这两个 `.d.ts`。
- **移动端**在 `packages/frontend/apps/`,位于该 workspace 之外,各自独立。
- **API 端口**:多数 app 连网关 `:60300`;bmos-lims-web 用 `:60200`(直连 mes)。

## 隐藏地雷 ⚠️

1. **bmos-lims-web 命名错配**:名为 lims,实际调 `/api/app/mes`(接口沿用 mes,业务归属未拆分)。改 lims 前端接口时去 mes 后端找。
2. **lims-app = mes-app 拷贝**:两者代码全等,未独立化。
3. **ems/dc 后端不在本仓库**:对应 app 的后端服务超出 monorepo 范围。
4. **Auto-Import 让 grep 失效**:抓 import 关系时必须看 `auto-imports.d.ts` / `components.d.ts`。
5. **5 个 app 无独立构建脚本**:bims/bsms/lisms/el/demo 改动后需确认 CI 如何打包。

## 相关页面

- [[frontend-web-architecture]] — Web 端 pnpm workspace + @bmos/* + Ant Design Vue 架构(待建)
- [[frontend-mobile-architecture]] — UniApp 多端构建与部署(待建)
- [[service-overview]] — 后端 context-path 对照
- [[monorepo-architecture]] — 前后端整合、git subtree
