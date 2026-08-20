---
title: Wiki Schema
created: 2026-06-29
updated: 2026-07-15
---

# Wiki Schema

> 本文件定义 bmos-monorepo 代码知识库（code-wiki）的**约定、frontmatter 模板和标签体系**。
> 配套文档：
> - [[PLAYBOOK]] —— 顶层入口，按任务（前端/后端）分流到具体分册
> - [[PLAYBOOK-backend]] —— **后端**建设方法论（扫描命令 / 页面模板 / 踩坑库）
> - [[PLAYBOOK-frontend]] —— **前端**建设方法论（骨架，含 Web app 与 UniApp 移动端）
>
> 关系：**SCHEMA 管"长什么样"，PLAYBOOK 管"怎么做出来"**。
> 新建或修改 wiki 页面前必须先读 SCHEMA + 对应端的 PLAYBOOK 分册。

## 领域

bmos-monorepo 代码知识库 —— 覆盖整合后的 Monorepo 架构、后端微服务（platform / mes / lims / wms / gateway）、前端 Web 应用群（pnpm workspace，12 个 app + @bmos/* 共享库）、移动端（UniApp：mes-app / lims-app）的模块职责、服务间通信、数据流、技术选型和开发规范。

目的：让 AI 在新需求开发、Bug 修复时，能根据 context **快速定位到具体服务/应用和代码路径**。

## 约定

- 文件名：小写、连字符、无空格（如 `mes-overview.md`、`service-integration.md`）
- 实体页按服务/应用命名空间归入子目录：`entities/_platform/`、`entities/_mes/`、`entities/_lims/`、`entities/_wms/`、`entities/_gateway/`、`entities/_web/`、`entities/_mobile/`
- 所有 wiki 页面以 YAML frontmatter 开头（见下方模板）
- 页面之间使用 `[[wikilinks]]` 互相链接（每页至少 2 个出站链接）
- `sources:` 必须填**真实代码路径**——这是 AI 跳转到代码的依据
- 更新页面时必须更新 `updated` 日期
- 每个新页面必须添加到 `index.md` 对应分区下
- 每次操作（创建/更新/批量导入）必须追加到 `log.md`
- 所有内容使用中文编写

## Frontmatter 模板

```yaml
---
title: 页面标题
created: YYYY-MM-DD
updated: YYYY-MM-DD
type: entity | concept | comparison | api
service: platform | mes | lims | wms | gateway | web | mobile | shared | cross
tags: [从下方标签体系中选择]
sources:
  - packages/backend/services/<service>/...
status: active        # active | deprecated
---
```

> 相比纯通用 wiki，bmos 强制要求 `service` 字段——多服务场景下这是 AI 缩小定位范围的核心索引维度。

## 标签体系

新增标签前必须先添加到此处，禁止随意创建标签。

### 服务层
- `platform`: 平台基础服务（认证、用户、配置、license）
- `mes`: 制造执行系统
- `lims`: 实验室信息管理系统
- `wms`: 仓库管理系统
- `gateway`: API 网关 / 服务路由

### 端层
- `backend`: 后端 Spring Boot 服务
- `frontend-web`: Web 端（Vue3 + Vite + Ant Design Vue）
- `frontend-mobile`: 移动端（UniApp）
- `shared-lib`: 共享库（@bmos/* 或后端 starter）

### 技术层
- `feign`: 服务间 Feign 调用
- `mybatis`: MyBatis-Plus 数据访问
- `shardingsphere`: 分库分表
- `minio`: 文件存储
- `vue`: Vue3
- `uniapp`: UniApp 多端
- `antd`: Ant Design Vue

### 能力/开发层
- `architecture`: 系统架构、整体设计
- `module`: 业务模块
- `auth`: 认证/权限
- `license`: license 校验
- `api`: 接口/协议
- `database`: 数据库/Entity
- `integration`: 服务间集成
- `convention`: 开发规范/约定
- `config`: 配置相关
- `deploy`: 部署/构建/subtree
- `tech-stack`: 技术栈选型

## 页面阈值

- **创建页面**：当一个服务/模块/共享库/核心流程承担独立职责
- **添加到已有页面**：当信息是某个已有实体的补充细节
- **不创建页面**：工具函数、辅助类、临时逻辑、与领域无关的内容
- **拆分页面**：超过 200 行时，按子主题拆分并用 wikilink 互联（**模块子页豁免**：后端服务模块子页因天然含完整表清单/枚举/Service 签名，常达 200–330 行，属合理体量，无需强制拆分；仅当单页超过 400 行或出现两个以上独立子主题时才拆）
- **归档页面**：代码已删除或完全重构后，frontmatter 改 `status: deprecated`，从 index 移除

## 页面类型规范

### Entity 页面（服务/模块/应用/共享库）
- 概述 / 职责
- 目录结构和关键文件路径（对应 `sources:`）
- 核心 API / 接口 / 对外暴露的能力
- 与其他服务/模块的关系（[[wikilinks]]，尤其 Feign 调用方向）
- 数据模型（核心表/Entity）

### Concept 页面（架构模式/数据流/规范）
- 定义 / 解释
- 工作流程（文字或图表）
- 关键代码路径
- 相关概念（[[wikilinks]]）

### Comparison 页面（横向速查）
- 对比维度（表格优先）
- 结论或综合判断
- 来源

### API 页面（按服务）
- 网关前缀 / 路由规则
- 核心接口清单
- 统一响应与异常码

## 更新策略

当代码变更导致 wiki 内容过时时：
1. 检查 git log 确认变更范围与时间
2. 更新页面内容，更新 `updated` 日期
3. 破坏性变更标注 `breaking: true` 到 frontmatter
4. 在 `log.md` 追加一条 ingest/update 记录，写明「哪些代码 → 更新了哪些页」
