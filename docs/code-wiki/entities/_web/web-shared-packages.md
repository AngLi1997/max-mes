---
title: Web 共享库 @bmos/*
created: 2026-06-30
updated: 2026-06-30
type: entity
service: shared
tags: [frontend-web, shared-lib, vue]
sources:
  - packages/frontend/apps/web/packages/
status: active
---

# Web 共享库 @bmos/*

## 概述 / 职责

Web 工程的 7 个 `@bmos/*` 共享库,位于 `packages/frontend/apps/web/packages/`。被 11 个 bmos-* app 统一全套依赖(`workspace:^`),源码直引不经预编译(`main`/`module` 指向 `./src/index.ts`,由 Vite 编译)。版本字段统一 `0.0.0`。

## 库清单

### @bmos/axios — HTTP 客户端
封装 axios 实例 + `./axios`、`./common` 全部导出。各 app `services/` 基于它调后端。

### @bmos/messager — 认证/SSO/消息桥（核心）
所有 app bootstrap 的**第一步** `Auth({})`:从 URL/userStorage 取 token,无则跳 SSO 登录页,调 `/api/app/platform/user/status` 验证。导出:`Auth`(默认认证函数)、`sso`、`app`、`registerMessage`、`lockScreen`、`getUserInfo`/`getUserToken`。

> `@bmos/auth` 实为 messager 的旧别名(`index.ts` 仅 `export * from './messager'`),非独立包。

### @bmos/components — 业务组件库
Vue 插件(全局注册),按 `all.ts` 批量导出 20 组件:AtInput/Button/ConfigProvider/Descriptions/Ellipsis/Form/Icon/MenuClose/ModalForm/NormalModalForm/PageComponent/PasswordInput/Printer/ScreenFrame/SearchTree/Select/StateTag/Table/TableTitle/TreeSelect。默认导出 `{ install }`。

### @bmos/icons — 图标
`BMIcons` 组件 + types。

### @bmos/i18n — 国际化
基于 i18next 单例。导出:`init`、`t`、`customizeT`、`changeLanguage`、`getLanguage`、`currentLng`(ref)、`I18nLanguageEnum`。`t` 被 auto-import 全局注入。

### @bmos/utils — 通用工具
聚合 16 子模块:RSA/async-series/common/date/element/file/func/indexDB/load-event/message/number/object/tree/types/url/zoom。含 `sendMessage`、`MessageType`、`cloneDeep`、`encrypt`、`loopSelectableNotValueTree`(部分被 auto-import 注入)。

## 被依赖关系

11 个 bmos-* app(`bmos-mes/lims/platform/wms/bsms/bims/ems/dc/el/audit/lisms-web`)dependencies **全部统一依赖** `@bmos/axios @bmos/components @bmos/i18n @bmos/icons @bmos/messager @bmos/utils`;`demo` 仅依赖 components/i18n/utils。

> 反查某 app 用了哪些库:`grep '"@bmos/' apps/<app>/package.json`。反查某库被谁用:`grep -rln '"@bmos/axios"' apps/*/package.json`。

## 关键约定

- **入口一致**:所有 app bootstrap 走 `Auth(messager) → asyncMenu → i18n → render`。
- **token 注入**:@bmos/messager 的 Auth 负责,axios 拦截器据此带 token。
- **auto-import**:@bmos/i18n 的 `t`、@bmos/utils 的部分函数被 unplugin-auto-import 全局注入(无需显式 import)。
- **源码直引**:`main`/`module` 指向 `src/index.ts`,无预编译产物,改共享库即时生效。

## 隐藏地雷 ⚠️

1. **@bmos/auth 是别名非独立包**(re-export messager),易误解为独立认证库。
2. **版本统一 0.0.0**:workspace:^ 不固定具体版本,lock 后回查需看 lockfile。
3. **@bmos/open** 在根脚本出现(`npx @bmos/open open`)但 packages/ 下无目录,可能外部/已移除。
4. **auto-import dts 已关**(见 [[web-overview]]),被注入的 API 类型靠遗留 d.ts。

## AI 定位提示

- 改 **HTTP 拦截/请求封装** → `packages/axios/`
- 改 **SSO/登录/token/锁屏** → `packages/messager/`(Auth)
- 改 **全局业务组件** → `packages/components/`(all.ts 批量导出)
- 改 **i18n 文案/语言切换** → `packages/i18n/`
- 改 **通用工具/RSA/加密** → `packages/utils/`

## 相关页面

- [[web-overview]] — Web 工程总览(12 app + workspace)
- [[frontend-apps-overview]] — app 与共享库速查
- [[frontend-web-architecture]] — 前端架构概念页(待建)
- [[auth-and-license]] — 后端 token 校验(与 messager.Auth 对应)
