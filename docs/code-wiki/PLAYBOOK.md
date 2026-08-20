---
title: Code-Wiki Playbook（入口）
created: 2026-06-29
updated: 2026-06-29
---

# Code-Wiki Playbook · 入口

> bmos-monorepo 是**后端微服务 + 前端 Web 应用群 + UniApp 移动端**的混合 Monorepo。
> 后端和前端在 **建页阈值 / 扫描信号 / 页面模板 / 踩坑模式** 上几乎完全不同。
> 本文是分流入口，按你要做的事选择对应分册。

## 按任务分流

| 你要做的事 | 读哪本 | 适用范围 |
|---|---|---|
| 建后端服务 / 模块的 wiki | [[PLAYBOOK-backend]] | `packages/backend/services/{platform,mes,lims,wms,gateway}` |
| 建前端 Web 应用 / 共享库的 wiki | [[PLAYBOOK-frontend]] | `packages/frontend/apps/web/` 下 12 app + @bmos/* 共享库 |
| 建 UniApp 移动端的 wiki | [[PLAYBOOK-frontend]]（含移动端章节） | `packages/frontend/apps/{mes-app,lims-app}` |
| **建 starter / SDK / 平台库的 wiki** | **🚧 待补**（见下方"暂未覆盖"） | `packages/backend/shared/` 或未来引入的 starter 源码 |
| 跨服务概念页 / 速查表 | 两本都看「Concept / Comparison 模板」 | 详见 [[SCHEMA]] 的 4 种 type |

## 暂未覆盖的场景

> 诚实声明：以下场景**当前没有 PLAYBOOK 支持**，等真实代码入库后再实战补足，不空想。

| 场景 | 状态 | 触发条件 | 临时处理 |
|---|---|---|---|
| Bmos 平台 starter / SDK 源码 | 待实战 | `packages/backend/shared/` 出现代码 / starter 源码以 subtree 引入 | 详见 [[PLAYBOOK-backend]] 第十节 TODO |
| 第三方 / 平台级 SDK 适配 | 待实战 | 出现新的 SDK 适配层（前端 npm 私包等） | 详见 [[PLAYBOOK-frontend]] 第七节 |
| 独立工具 / 脚手架 / CLI | 待实战 | 出现 codegen / build plugin 等非业务非 starter 代码 | 实战时按代码形态新设页型 |

## 两本共享的部分（不要重复读）

- [[SCHEMA]] — frontmatter 格式、标签体系、文件命名约定（前后端一致）
- [[index]] — 总目录，按类型 + 服务分区
- [[log.md|log]] — 操作审计，仅追加
- 每页 ≥ 2 出站 wikilink 的约定（适用所有页）
- 操作落地 4 件套（更新 index / 追加 log / 双向链接 / 回写 PLAYBOOK）

## 前后端的核心差异（速览）

> 这是分册存在的理由。详细规则见各分册。

| 维度 | 后端 services 应用层 | 前端 | starter / SDK 库代码（🚧 待实战） |
|---|---|---|---|
| 建页阈值 | Java 文件数 ≥ 50 | 待定（路由/页面/组件多维，见前端篇） | 多维：被几服务依赖 + 配置项数 + 源码规模 |
| 数据模型抓取 | `@TableName` | 无表，看 TS 类型 + Pinia store state | 通常无表；元配置在 `META-INF/` |
| 接口契约抓取 | `@FeignClient` / Service 签名 | `@bmos/axios` 调用 + 后端 context-path 对照 | `@EnableXxx` / `@AutoConfiguration` / Properties |
| 状态机抓取 | Java enum 类 | TS enum + Pinia store status 字段 | 配置项默认值 + `@ConditionalOnXxx` 条件链 |
| 模块组织 | 服务 → 子域（单层包） | app + 共享库 + 路由/页面/组件 + 多端条件 | 库 → 自动配置类 + SPI |
| 扩展点 | 抽象基类 + 策略实现 | composables / Vue 插件 / slots | Bean 注入点 / AOP 切面 / 拦截器 |
| 入口 | `@SpringBootApplication` | `main.ts` / Vite 配置 / vue-router | `@EnableXxx` 注解 / 自动装配元数据 |
| 独有机制 | MQ / XXL-Job / Repository | 动态路由 / Auto-Import / i18n / Vite proxy / 多端编译 | 自动装配条件链 / SPI / Properties 校验 |
| 用户视角 | "改业务逻辑去哪" | "改 UI / 加页面 / 联调接口去哪" | "怎么用 / 配什么参数 / 启用条件 / 影响面" |

## 不变的部分

无论前端后端，wiki 的目的不变：**让 AI 在新需求 / Bug 修复时根据 context 快速定位代码**。所以两本分册共享 6 类「重点信号」抽取的思路（数据模型 / 状态机 / 接口签名 / 扩展点 / 独有机制 / 隐藏地雷），只是抓取位置和命令不同。

## 维护

- 新增分册（如未来 `PLAYBOOK-desktop.md`）→ 更新本入口表格
- 发现某条约定**前后端共有**且现在不在本文 → 回写本文「共享部分」
- 发现某条约定**只在一端有** → 写入对应分册的"踩坑库"