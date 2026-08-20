---
title: 移动端工程总览（UniApp）
created: 2026-06-30
updated: 2026-06-30
type: entity
service: mobile
tags: [frontend-mobile, uniapp]
sources:
  - packages/frontend/apps/mes-app/package.json
  - packages/frontend/apps/mes-app/src/pages.json
  - packages/frontend/apps/mes-app/src/manifest.json
  - packages/frontend/apps/lims-app/
  - packages/frontend/apps/app-build-template/
status: active
---

# 移动端工程总览（UniApp）

## 概述 / 职责

bmos 的移动端工程,位于 `packages/frontend/apps/`。含 `mes-app`、`lims-app`(UniApp Vue3 工程)及两个原生打包模板 `app-build-template`/`lims-app-build-template`。

> ⚠️ **关键现状**:`lims-app` 是 `mes-app` 的近乎完全副本(package.json/manifest/pages.json 三者相同,连 UniApp appid `__UNI__FD40210`、`name:"BMOS MES"` 都没改),后端仍调 `/api/app/mes`,**LIMS 对接尚未接入**。两者**独立工程,不接入 `@bmos/*` 共享库**(与 web 端隔离)。

## 技术栈（mes-app / lims-app 通用）

UniApp **3.0.0-3090920231225001**(2023-12-25 快照) · Vue **3.4.6** · Vite **4.0.3** · Pinia **2.0.27** · vue-i18n **9** · echarts 5 · mqtt 3。UI 三套 easycom:`uni-ui`/`uv-ui`/`wot-design-uni`。包名仍是官方 preset `uni-preset-vue`。

## 多端构建目标（各 20 个 dev:*/build:*）

**实际主力仅 H5 + App-Plus(Android)**;小程序 appid 未配置,预留未启用。

| 平台 | 脚本 | 状态 |
|---|---|---|
| H5 | `dev:h5`/`build:h5` | ✅ 主用 |
| App(Android) | `dev:app`/`build:app`、`app-android`/`app-ios` | ✅ 主力(Android) |
| 微信/支付宝/百度/抖音/QQ/京东/快手/飞书/小红书 小程序 | `dev:mp-*` | ⏳ appid 留空,未启用 |
| 快应用 | `quickapp-webview*` | ⏳ 预留 |

> 条件编译统计(`#ifdef`):H5 ×170、APP-PLUS ×153 占 90%+;MP-WEIXIN ×18、MP-DINGTALK ×14、MP-ALIPAY ×8 仅有适配代码。

## mes-app 规模

- **132 页**(主包,无分包);**无静态 tabBar**(由 `stores/tabbar.js` 运行时动态管理:看板/待办/工作台/个人)
- **横屏锁定**:`pageOrientation: landscape`、`screenOrientation: landscape-*`、`fullscreen:true`
- **appid** `__UNI__FD40210`、versionName `BMOS.0409`、Android 16 项权限(含 CAMERA/串口扫码)
- 主要页面域:login/home · inventoryManagement(库存) · businessComponents(领料/配料/称量/产出/配液/清场,最大) · materialWeighing/weighingCenter/weighingWorkOrder(称量五域) · production · pleaseVerify(请验) · webview*(hybrid 桥接)

## 启动 chain

`src/main.js`(createSSRApp + i18n + Pinia,`#ifdef H5` 重写 navigateBack)→ `App.vue`(`onLaunch` 初始化 i18n + `#ifdef APP-PLUS` 注册 Android 锁屏广播 → 跳 `/pages/lockPage`)→ 首页 `pages/home/index`。

## 后端调用

- `/api/app/mes` ×**535 处**(绝对主体) · `/api/app/platform` ×70(用户/权限/参数)
- **无 `/api/app/lims` 或 `/api/app/wms`**——经 mes 服务 Feign 间接交互,不直连他服务

## 本地库（独立于 @bmos/*）

- **BMComponents**(约 35 组件):BasicPage/Scan/ScanSerialPort(串口扫码)/Sign/Table/Tree/Modal 等
- **BMUtils**:`BMFunc`、`BMMqtt`、`useBMBalanceMqtt`(电子秤 MQTT)、`useBMScan`(扫码)
- **hybrid/**:webview 桥接,内置本地 HTML(快捷录入/趋势分析)+ 完整 **pdf.js viewer**(PDF 预览)

## lims-app 差异（仅 10 文件）

`stores/tabbar.js`(某 tab 显隐开关) + 零星 businessComponents hooks + `BMComponents/Loading`(lims 缺失)。**核心配置未改**:仍用 mes 的 appid/包名/后端。

## 两个 build-template（非 npm 工程）

`app-build-template`/`lims-app-build-template`:UniApp H5 产物的**原生打包壳**。

- `andriod/`(原文拼写如此):Gradle 原生壳 + HBuilder 离线打包 SDK + keystore → APK
- `electron/`:electron-builder(electron 28) → Windows exe;mes=`Bmos-Mes`、lims=`Bmos-Lims`

## 隐藏地雷 ⚠️

1. **lims-app 未真正差异化**:与 mes 共用 appid/包名/后端,LIMS 后端对接待补。
2. **不接入 @bmos/* 共享库**:与 web 端完全隔离,自有 BMComponents/BMUtils。
3. **小程序端 appid 全空**,构建目标保留但未实际启用。
4. **横屏锁定 + Android 深度集成**:改 UI 需考虑横屏布局;锁屏广播只在 APP-PLUS。
5. **hybrid PDF** 靠 webview 内置 pdf.js,非 uni 原生 PDF 组件。
6. **mqtt 实时通信**:电子秤/设备数采依赖 mqtt,注意连接生命周期。

## AI 定位提示

- 找 **页面** → `src/pages/<域>/`(businessComponents 最大)
- 找 **称量/电子秤** → `src/pages/materialWeighing*` + `BMUtils/useBMBalanceMqtt`
- 找 **扫码** → `BMComponents/Scan*` + `BMUtils/useBMScan`
- 找 **原生适配/锁屏** → `App.vue` + 全局 grep `#ifdef APP-PLUS`
- 找 **接口** → `src/api/`(34 文件,全 `/api/app/mes|platform`)
- 找 **PDF/hybrid** → `src/hybrid/`(webview + pdf.js)

## 相关页面

- [[frontend-apps-overview]] — 12 web app + 2 移动端速查
- [[mes-overview]] — mes-app 的后端主体(535 处 API)
- [[frontend-mobile-architecture]] — 移动端架构概念页(待建)
- [[web-overview]] — web 端(隔离,共享库体系不同)
- [[monorepo-architecture]] — subtree 整合
