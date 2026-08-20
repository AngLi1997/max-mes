---
title: 前端移动端架构
created: 2026-06-30
updated: 2026-06-30
type: concept
service: cross
tags: [frontend-mobile, uniapp, architecture]
sources:
  - packages/frontend/apps/mes-app/src/manifest.json
  - packages/frontend/apps/mes-app/src/pages.json
  - packages/frontend/apps/mes-app/src/main.js
  - packages/frontend/apps/app-build-template/
status: active
---

# 前端移动端架构

> bmos 移动端架构:UniApp Vue3 多端 + 条件编译 + hybrid webview 桥接 + 原生打包壳。实体索引见 [[mobile-overview]]。

## 工程定位

- 两个 UniApp 工程:`mes-app`(主力)、`lims-app`(mes-app 的副本,未独立化)
- **独立于 web 的 @bmos/* 体系**:package.json 无 `@bmos/*`/`workspace:*`,自有 BMComponents(35 组件)/BMUtils
- 经 git subtree 整合,与 web 工程隔离

## 多端与条件编译

UniApp 3.0.0-alpha + Vue 3.4.6 + Vite 4.0.3。**一套代码多端**,靠 `#ifdef`/`#ifndef` 区分:

| 平台 | `#ifdef` 计数 | 状态 |
|---|---|---|
| H5 | ×170 | ✅ 主力 |
| APP-PLUS | ×153 | ✅ 主力(Android) |
| MP-WEIXIN/DINGTALK/ALIPAY | ×18/14/8 | ⏳ 适配代码在,appid 未配置 |

> 改业务逻辑时**必须带 `#ifdef` 上下文**一起看——同一段代码可能只在某端生效(如锁屏广播只在 APP-PLUS)。

## 横屏 + 原生深度集成

- `pageOrientation: landscape`、`screenOrientation: landscape-*`、`fullscreen:true`(横屏锁定)
- Android 16 项权限(CAMERA/串口扫码/REQUEST_INSTALL_PACKAGES…)
- `App.vue onLaunch` 的 `#ifdef APP-PLUS`:注册 Android `ACTION_SCREEN_OFF` 广播 → 跳 `/pages/lockPage`(锁屏)

## hybrid webview 桥接

App-Plus 端通过 webview 加载本地 HTML 实现 web 端难做的能力:

- `hybrid/html/`:快捷录入、趋势分析本地页
- `hybrid/html/pdf/`:内置完整 **pdf.js viewer**(PDF 预览,非 uni 原生组件)
- 通过 `uni.webview.js` SDK 与 uni 通信

## 页面与导航

- **132 页**(主包,无分包);**无静态 tabBar**——由 `stores/tabbar.js` 运行时动态管理(看板/待办/工作台/个人)
- 主要域:businessComponents(领料/配料/称量/产出/配液/清场,最大)、称量五域、inventoryManagement、production、pleaseVerify

## 启动 chain

`src/main.js`(createSSRApp + i18n + Pinia,UniApp 要求显式返回 Pinia;`#ifdef H5` 重写 navigateBack)→ `App.vue`(onLaunch 初始化 i18n + APP-PLUS 锁屏监听 + 对时)→ 首页 `pages/home/index`。

## 后端联动

- `/api/app/mes` ×535(主体) · `/api/app/platform` ×70(用户/权限/参数)
- **不直连 lims/wms**——经 mes 服务 Feign 间接交互(遵循服务边界)

## 打包链（build-template）

UniApp H5 产出 → 原生打包壳(`app-build-template`/`lims-app-build-template`,非 npm 工程):

- `andriod/`(原文拼写):Gradle + HBuilder 离线 SDK + keystore → **APK**
- `electron/`:electron-builder(electron 28) → **Windows exe**(mes=`Bmos-Mes` / lims=`Bmos-Lims`)

## 架构地雷 ⚠️

1. **lims-app 未独立化**:与 mes 共用 appid(`__UNI__FD40210`)/包名/后端。
2. **不接 @bmos/* 共享库**,与 web 隔离,自有组件库。
3. **小程序端全空 appid**,目标保留未启用。
4. **mqtt 实时通信**(电子秤/设备数采)依赖连接生命周期管理。

## 相关页面

- [[mobile-overview]] — mes-app/lims-app 实体索引
- [[frontend-apps-overview]] — 移动端速查
- [[mes-overview]] — 后端主体
- [[monorepo-architecture]] — subtree 整合、工程隔离
