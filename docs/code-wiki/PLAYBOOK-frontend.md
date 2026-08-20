---
title: Code-Wiki Playbook · 前端篇（骨架）
created: 2026-06-29
updated: 2026-06-29
status: draft
---

# Code-Wiki Playbook · 前端篇（Frontend）

> **状态**：🚧 骨架版。后端篇是从 4 次实战中长出来的，本篇尚未经过实战检验。
> 第一次建前端 wiki 页时**遵循"样板先行"**：先按本骨架建 1~2 页，再回写本文补足细节。
>
> 适用范围：
> - `packages/frontend/apps/web/`（pnpm workspace：12 业务 app + 7 个 `@bmos/*` 共享库）
> - `packages/frontend/apps/{mes-app,lims-app}`（UniApp 多端，17 个构建目标）
>
> 后端建页请读 [[PLAYBOOK-backend]]。两者由 [[PLAYBOOK]] 顶层入口分流。

## 一、建页对象与阈值（待实战校准）

后端用"Java 文件数 ≥ 50"作阈值。前端结构不同，建页对象也不同——下面是**分类清单**，每类的阈值待第一次实战后校准。

### 建页对象（按优先级）

| 对象类型 | 例子 | 单独建页的初步条件（待校准） |
|---|---|---|
| **Web App 总览** | bmos-mes-web / bmos-platform-web / bmos-lims-web 等 12 个 | 每个 app 一页 entity overview（强制） |
| **UniApp App 总览** | mes-app / lims-app | 每个 app 一页 entity overview（强制，需含多端章节） |
| **共享库** | @bmos/auth / axios / components / i18n / icon / messager / utils | 每个共享库一页 entity（强制） |
| **大型功能模块** | 单 app 内的页面集合（如 mes-web 的"批记录管理"） | 路由 ≥ 10 个 OR 文件 ≥ 80 OR 单独 Pinia store | 待校准 |
| **跨 app 共享的页面/逻辑** | 多个 app 复用的复杂业务 | 出现在 ≥ 2 个 app 时考虑建独立页 |
| **构建/部署机制** | Vite 配置、Auto-Import、多端条件编译 | 单独 concept 页 |

### 不建页的情况

- 单文件组件 / 单一工具函数 / 单页路由
- 与业务无关的 UI 工具类
- 仅当前 app 用的辅助 composable

## 二、五步法（沿用后端，扫描和模板替换）

流程不变：扫描 → 选模板 → 抓信号 → 写页 → 集成 4 件套。变的是**扫描命令**和**模板**。

## 三、扫描命令（前端）

> 前端无 Java 注解，主要从配置文件 + import 关系 + 命名约定提取。

### 1. App / 共享库规模

```bash
F=/d/workspace/bmos-monorepo/packages/frontend/apps/web

# 单个 web app 规模
APP=bmos-mes-web
echo "=== $APP ==="
echo ".vue: $(find "$F/apps/$APP/src" -name '*.vue' | wc -l)"
echo ".ts:  $(find "$F/apps/$APP/src" -name '*.ts' | wc -l)"
echo "pages: $(ls "$F/apps/$APP/src/pages" 2>/dev/null | wc -l)"
echo "stores: $(ls "$F/apps/$APP/src/stores" 2>/dev/null | wc -l)"
echo "services: $(find "$F/apps/$APP/src/services" -name '*.ts' 2>/dev/null | wc -l)"
```

### 2. 标准目录结构（web app 应有）

```
apps/<app>/src/
├── main.ts          # 入口
├── bootstrap.ts     # 启动钩子（初始化、权限、登录前置）
├── App.vue
├── router/          # 路由配置
├── stores/          # Pinia stores
├── services/        # 后端接口调用（基于 @bmos/axios）
├── pages/           # 页面
├── components/      # 组件
├── hooks/           # composables
├── plugins/         # Vue 插件
├── directives/      # 自定义指令
├── config/          # 配置（含 menu、env-based）
├── utils/
├── assets/
└── style/
```

### 3. 后端调用关系（前端版的 "@FeignClient"）

```bash
# 找所有 axios 调用（API 路径），定位前端→后端的 context-path 匹配
grep -rEnh "(get|post|put|delete)\(['\"]" "$F/apps/$APP/src/services" --include='*.ts' \
  | grep -oE "['\"]/api/app/[a-z]+/[^'\"]*" | sort -u

# 与后端 service-overview 的 context-path 对照：
#   /api/app/platform → platform 服务（60100）
#   /api/app/mes      → mes 服务（60200）
#   /api/app/lims2    → lims 服务（61001）⚠️ 是 lims2 不是 lims
#   /api/app/wms      → wms 服务（60900）
```

### 4. 路由清单

```bash
# 静态路由
find "$F/apps/$APP/src/router" -name '*.ts' -exec grep -E "path:\s*['\"]" {} \;

# 动态路由：来自后端 base_sys_menu（如有），由权限模块拉取
```

### 5. Pinia stores（前端的"状态机/Service 层"）

```bash
ls "$F/apps/$APP/src/stores"
# 每个 store 抓三类信号：
#  - state 字段（特别是 status / loading / current* 之类的状态机）
#  - 主要 actions 方法签名
#  - 是否依赖其它 store（getter / 跨 store action）
```

### 6. 共享库依赖图

```bash
# 当前 app 用了哪些 @bmos/* 共享库
grep -E '"@bmos/' "$F/apps/$APP/package.json"

# 反向：哪些 app 用了某个共享库
grep -rln '"@bmos/axios"' "$F/apps/*/package.json"
```

### 7. Vite 关键配置

```bash
# proxy 代理（开发期前后端串联）
grep -A 10 'proxy' "$F/apps/$APP/vite.config.ts"

# Auto-Import 范围（决定不显式 import 也能用的 API）
grep -A 5 'AutoImport\|Components(' "$F/apps/$APP/vite.config.ts"

# 多 app 共享配置（很可能在 web 根 vite.config）
ls "$F"/*.config.ts 2>/dev/null
```

### 8. UniApp 多端构建（仅 *-app）

```bash
APP=mes-app
F2=/d/workspace/bmos-monorepo/packages/frontend/apps

# 17 个构建目标速览（dev:* / build:*）
grep -E '"(dev|build):' "$F2/$APP/package.json"

# 平台条件编译（#ifdef H5 / APP-PLUS / MP-WEIXIN 等）
grep -rln '#ifdef\|#ifndef\|#endif' "$F2/$APP/src" --include='*.vue' --include='*.ts' --include='*.js' \
  | head -10

# pages.json（uniapp 的路由 + tabbar）
head -50 "$F2/$APP/src/pages.json"

# manifest.json（应用名/版本/各端配置）
grep -E '"(name|appid|versionName)":' "$F2/$APP/src/manifest.json"
```

### 9. 启动入口实扫

```bash
# 启动 chain（main.ts → bootstrap.ts → App.vue → router → 权限拦截 → 第一个路由）
cat "$F/apps/$APP/src/main.ts"
cat "$F/apps/$APP/src/bootstrap.ts"
```

### 10. i18n / 主题 / 设备适配

```bash
# i18n locales 文件
find "$F/apps/$APP/src" -path '*/i18n/*' -o -path '*/locale*' | head

# 是否使用 @bmos/i18n 还是自建
grep -E '"@bmos/i18n"' "$F/apps/$APP/package.json"
```

## 四、页面模板（前端版）

### 模板 W1 · Web App Overview

```markdown
---
title: <app-name> Web 应用总览
type: entity
service: web
tags: [frontend-web, vue, app]
sources: [packages/frontend/apps/web/apps/<app>/]
---

## 概述 / 职责
（应用名、对应后端服务、context-path、用户角色、规模 .vue/.ts/pages/stores）

## 启动 chain
（main.ts → bootstrap.ts → 初始化什么 → 进入哪个路由）

## 路由与菜单
- 静态路由清单（按业务域分组）
- 是否使用后端动态菜单（来自 platform 的 `base_sys_menu` 或类似）
- 路由守卫做了什么（鉴权/license/loading）

## Pinia stores
（每个 store：state 关键字段 + 主要 actions + 跨 store 依赖）

## 后端调用（services/）
- 使用的 context-path 与对应后端服务（对照 [[service-overview]]）
- 主要业务接口模块

## 依赖的共享库
（@bmos/axios / auth / components / i18n / icon / messager / utils 用了哪些，做什么）

## Vite 关键配置
- proxy 代理目标
- Auto-Import / Components 自动导入范围（影响代码搜索）
- 别名 / 构建产物特殊配置

## 关键页面 / 业务模块
（哪些 pages/ 子目录是核心业务）

## AI 定位提示
- 找 UI / 页面 → pages/<域>/
- 找请求 → services/<域>/
- 找状态 → stores/<域>.ts
- 找鉴权 → router/guards 或 bootstrap.ts
- 找接口签名 → 后端服务（按 context-path 对照 [[service-overview]]）

## 相关页面
```

### 模板 W2 · 共享库 @bmos/* Entity

```markdown
---
title: <@bmos/xxx> 共享库
type: entity
service: shared
tags: [frontend-web, shared-lib]
sources: [packages/frontend/apps/web/packages/<name>/]
---

## 概述 / 职责
（提供什么能力、被哪些 app 用、规模）

## 对外 API
（暴露的函数 / 组件 / hooks，含签名）

## 内部结构
（src/ 主要文件、构建产物）

## 关键约定
（使用方需要遵循的规则，如 axios 的拦截器顺序、auth 的 token 注入位置）

## 被依赖
（grep "@bmos/<name>" 找用了它的 app，按需列出）

## AI 定位提示
## 相关页面
```

### 模板 M1 · UniApp 移动端 Overview

```markdown
---
title: <name>-app 移动端总览
type: entity
service: mobile
tags: [frontend-mobile, uniapp]
sources: [packages/frontend/apps/<name>-app/]
---

## 概述 / 职责
（对应后端、用户角色、当前主要构建目标）

## 多端构建目标速览
- H5：dev:h5 / build:h5
- 原生 App：dev:app / build:app（iOS/Android）
- 微信小程序：dev:mp-weixin / build:mp-weixin
- 其它（按需）
- ⚠️ 部分目标可能未真正使用，以 CI/CD 实际打包脚本为准

## pages.json
- 主页面清单
- tabbar 结构
- 是否使用分包

## manifest.json
- appid / versionName
- 各端特有配置（iOS bundle / 微信 mp appid / Android 权限）

## 平台条件编译
- 使用了哪些 `#ifdef`（H5 / APP-PLUS / MP-WEIXIN）
- 关键差异点（如某功能只在 App 端有原生模块）

## 启动 chain
（main.js → App.vue → 第一个 page）

## Pinia stores

## 后端调用
（与 web 版的对照——通常调同一个后端 context-path）

## BMComponents / BMUtils
（mes-app 内的本地组件/工具库，与共享库的关系）

## AI 定位提示
- 找页面 → src/pages/
- 找原生能力适配 → src/hybrid/
- 找平台特有逻辑 → 全局 grep `#ifdef`
- 找接口 → src/api/

## 相关页面
```

## 五、重点信号清单（前端版）

后端 6 类信号在前端的**对应映射**：

| 信号 | 后端抓取位置 | 前端抓取位置 |
|---|---|---|
| 数据模型 | `@TableName` | TS 接口/类型定义（`types/`） + Pinia store `state` 字段 |
| 状态机 | Java enum 类 | TS enum + Pinia store 中的 status 字段 + 业务态字面量联合类型 |
| 接口签名 | Service 接口 | `services/*.ts` 的导出函数 + 调用的后端 API 路径 |
| 扩展点 | 抽象基类 + 策略 | composables / Vue 插件 / 全局 directive / slot 约定 |
| 独有机制 | MQ / XXL-Job | 动态路由 / Auto-Import / Vite proxy / 多端条件编译 / i18n fallback |
| 隐藏地雷 | 表前缀异常等 | **见第七节** |

## 六、节奏控制原则（与后端一致）

- 单页 > 200 行立即拆分
- 门户优先（每个 app 先出 overview，再考虑功能模块子页）
- 样板先行（**本骨架就是约定：第一次建前端页时必须出 1~2 个样板与用户对齐**）
- 跨页 wikilink 引用闭环优先

## 七、前端踩坑库（持续积累，待实战补充）

> 后端踩坑库 10 条全部从实战中来。本节先列出**预估的前端潜在坑**（待第一次实战验证），实战中确认后保留并标注，未发生的删除。

### 预估坑（待验证）

| 预估坑 | 防御 |
|---|---|
| Auto-Import 让 grep 找不到 import 语句 | 抓信号时把 `auto-imports.d.ts` 和 `components.d.ts` 一并看，里面有自动导入清单 |
| Vite proxy 在 dev/build 行为不同 | overview 里明确写出 dev 代理目标和生产环境真实路径 |
| UniApp `#ifdef` 让某段代码只在某端生效 | grep 业务逻辑时**必须**带 `#ifdef` 上下文一起看 |
| 共享库 workspace:* 版本不固定 | 文档中标注共享库当前真实版本（来自共享库 package.json）以备 lock 后回查 |
| 多 app 复用同一份 services/.ts 复制造成漂移 | 建议在 overview 注明是否复用、复用来源 |
| context-path 写错（lims2 vs lims） | 已在扫描命令 3 显式列出对照表 |
| Pinia 持久化插件 → 状态在 localStorage / cookie | overview 注明哪些 store 持久化、key 命名 |
| 后端动态菜单覆盖前端静态路由 | overview 注明路由来源（静态 / 后端 / 混合） |

### 实战确认坑（暂空，第一次建前端页后填）

待补。

## 七·B 未覆盖场景 TODO（待实战补齐）

> 与后端篇第十节配套。本节列出**前端篇尚未覆盖**的场景，引入对应代码时回到本节按真实代码补足方法论，再开始建页。

### 1. 第三方 / 平台级 SDK 适配

**触发条件**：
- 前端引入需要独立适配层的第三方 SDK（如复杂的图表 SDK、3D 引擎、原生桥）
- npm 私有包发布到 `packages/frontend/sdk/` 或类似目录
- `@bmos/*` 共享库之外出现新型库代码组织

**与 `@bmos/*` 共享库的核心差异**：
- 共享库是**自家代码**，对外 API 由我们设计
- SDK 适配层是**包装第三方**，需文档化"被包装的 SDK 版本 / 兼容性 / 已知问题"
- 升级路径、breaking change 跟踪、平台条件适配是文档重点

**实战时需补足**：
- 扫描命令（被包装 SDK 的版本 + 适配点 + 兼容性矩阵）
- 模板（W3 · SDK 适配层）
- 踩坑（SDK 升级 / 平台差异 / 类型声明）

### 2. 桌面端 Electron 模板

**触发条件**：`app-build-template` / `lims-app-build-template` 进入实际使用，或新建独立 Electron app。

**与 web/UniApp 的差异**：
- 主进程 / 渲染进程分离
- 原生能力调用（文件系统 / 系统托盘 / 自动更新）
- 打包配置（electron-builder）

**实战时**：考虑新增 `PLAYBOOK-desktop.md` 或在本篇加 M2 模板。

### 3. 前端独立工具 / CLI / 脚手架

**触发条件**：出现独立的 codegen / 构建插件 / dev 工具。
**处理**：实战时按代码形态新设页型。

### 4. 维护规则

- 引入对应代码 → **在本节对应条目下写一行"已实战，方法论已落地到 X 节"或拆出新分册**
- 拆分阈值：方法论 > 80 行且与现有内容差异显著 → **拆出独立分册**（如 `PLAYBOOK-desktop.md`），并更新 [[PLAYBOOK]] 入口的分流表与差异速览
- 不要凭空补：**只在真实代码入库后基于扫描结果回写**

## 八、新前端 app / 模块 / 共享库上手 checklist

```
[ ] 1. 读本 PLAYBOOK-frontend + SCHEMA + index + PLAYBOOK 入口
[ ] 2. 跑第三节扫描命令（1~10）拿到 app 真实数据
[ ] 3. 判断建页对象：app overview / 共享库 / 功能模块
[ ] 4. 用模板 W1 / W2 / M1 出第一版（先 1~2 个样板）
[ ] 5. 用户 review，按反馈回写本 PLAYBOOK 第一节阈值、第七节踩坑库
[ ] 6. 按反馈批量建剩余 app / 共享库
[ ] 7. 每页完成：更新 index + 追加 log + 双向链接
[ ] 8. 若发现共享约定（前后端皆有）→ 回写 [[PLAYBOOK]] 顶层入口
```

## 九、本文档自身的维护

- 当前为**骨架版**，第一次建前端 wiki 页时**必须**回写实战调整：
  - 第一节"建页对象与阈值"——把"待校准"改为具体阈值
  - 第七节预估坑——保留真实发生的，删除未发生的，补充新发现的
  - 模板 W1/W2/M1——按实际页结构调整
- 维护规则其余部分与后端篇一致

---

> 本骨架与 [[PLAYBOOK]] 顶层入口 + [[SCHEMA]] 一起，构成前端建页的起点。后端建页见 [[PLAYBOOK-backend]]。