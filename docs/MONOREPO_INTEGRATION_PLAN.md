# BMOS 微服务整合 Monorepo 方案设计

**文档版本**: 1.0
**更新时间**: 2026-06-25
**适用范围**: 整个 BMOS 系统（MES、LIMS、WMS 等多个微服务 + 前端 + 脚手架 + 流程引擎）

---

## 一、方案概述

### 1.1 目标与愿景

将分布在多个代码仓库中的微服务、前端应用、共享工具库、流程引擎等整合为**统一的 Monorepo 架构**，建立**代码 Wiki 知识库**，实现：

- **统一的开发体验**: 一个仓库、一个工作流，减少切换和上下文成本
- **AI驱动的开发效率**: 通过结构化文档和设计文档，使 AI 快速理解系统并高效开发/修复
- **知识的沉淀和复用**: 设计文档、架构决策、代码约定等集中管理，避免重复讨论
- **跨服务依赖管理**: 依赖树、共享库管理（注：依赖版本统一为后续目标，当前各服务各自维护，详见 [4.1](#41-后端-maven-配置)）
- **便捷的本地开发**: 本地启动多个服务进行集成测试、调试

### 1.2 核心原则

1. **渐进式迁移**: 不是一次性迁移所有仓库，而是分阶段、分模块进行
2. **保留独立性**: 每个微服务仍能独立部署和版本管理
3. **文档优先**: 代码 Wiki 是 AI 高效工作的基础，不是事后补充
4. **工具链统一**: 统一的 build、test、lint、ci/cd 配置
5. **向后兼容**: 迁移过程中保持现有工作流的稳定性

---

## 二、整合目标仓库结构

### 2.1 当前分散的仓库架构

```
当前状态（多仓库）:
├── bmos-mes/                    # MES 微服务（当前位置）
├── bmos-platform/               # 平台微服务（独立仓库）
├── bmos-lims/                   # LIMS 微服务（独立仓库）
├── bmos-wms/                    # WMS 微服务（独立仓库）
├── bmos-flow-engine/            # 流程引擎（独立仓库）
├── bmos-admin-ui/               # 后台前端（独立仓库）
├── bmos-mes-ui/                 # MES 前端（独立仓库）
├── bmos-cloud-scaffold/         # Spring Boot 脚手架（独立仓库）
└── ...其他服务和工具
```

### 2.2 整合后的 Monorepo 结构

```
bmos-monorepo/
├── .github/
│   └── workflows/               # GitHub Actions 统一 CI/CD
├── .spec/
│   ├── design/                  # 设计文档库
│   │   ├── architecture.md       # 整体架构设计
│   │   ├── data-flow.md          # 数据流设计
│   │   ├── api-gateway.md        # API 网关设计
│   │   ├── auth.md               # 认证授权设计
│   │   └── ...
│   ├── steering/                # 代码导航和概览（codebase-analysis自动生成）
│   │   ├── product.md            # 产品功能概览
│   │   ├── tech.md               # 技术栈和关键技术决策
│   │   └── structure.md          # 项目结构详解
│   ├── context/                 # AI Context 文档
│   │   ├── entity-relationships.md    # 核心实体关系
│   │   ├── business-rules.md          # 业务规则
│   │   ├── integration-points.md      # 服务间集成点
│   │   └── common-patterns.md         # 常用代码模式
│   ├── api/                     # API 文档
│   │   ├── mes-api.md
│   │   ├── platform-api.md
│   │   └── ...
│   ├── database/                # 数据库设计文档
│   │   ├── erd.md
│   │   └── migrations/
│   └── wiki/                    # 快速参考和常见问题
│       ├── quick-start.md
│       ├── local-dev.md
│       ├── troubleshooting.md
│       └── faq.md
│
├── packages/
│   ├── backend/
│   │   ├── services/            # 微服务集合
│   │   │   ├── mes/
│   │   │   │   ├── pom.xml
│   │   │   │   ├── src/
│   │   │   │   └── README.md
│   │   │   ├── platform/
│   │   │   ├── lims/
│   │   │   ├── wms/
│   │   │   └── ...
│   │   ├── shared/              # 后端共享库
│   │   │   ├── common/          # 通用工具
│   │   │   ├── feign-clients/   # Feign 客户端定义
│   │   │   ├── models/          # 共享数据模型
│   │   │   └── exceptions/      # 异常定义
│   │   ├── engines/             # 引擎和框架
│   │   │   ├── flow-engine/     # 流程引擎
│   │   │   ├── audit-engine/    # 审计引擎
│   │   │   └── ...
│   │   └── scaffold/            # Spring Boot 脚手架
│   │       └── spring-boot-starter-*
│   │
│   ├── frontend/
│   │   ├── apps/
│   │   │   ├── admin-ui/        # 后台管理系统
│   │   │   ├── mes-ui/          # MES 系统前端
│   │   │   ├── lims-ui/         # LIMS 前端
│   │   │   └── ...
│   │   ├── shared/
│   │   │   ├── components/      # 共享组件库
│   │   │   ├── hooks/           # 共享 Hooks
│   │   │   ├── utils/           # 共享工具
│   │   │   └── types/           # 共享类型定义
│   │   ├── package.json         # Monorepo 根 package.json
│   │   └── pnpm-workspace.yaml  # Pnpm 工作区配置
│   │
│   └── devops/                  # DevOps 和基础设施
│       ├── docker/              # Dockerfile 集合
│       ├── k8s/                 # Kubernetes manifests
│       ├── terraform/           # IaC 配置
│       └── scripts/             # 自动化脚本
│
├── docs/
│   ├── CONTRIBUTION.md          # 贡献指南
│   ├── SETUP.md                 # 本地开发环境设置
│   ├── TESTING.md               # 测试策略
│   ├── DEPLOYMENT.md            # 部署指南
│   └── ARCHITECTURE.md          # 整体架构文档
│
├── tools/
│   ├── ai-context-gen/          # AI Context 自动生成工具
│   ├── sync-scripts/            # 从外部仓库同步脚本
│   └── ...
│
├── README.md                    # Monorepo 根文档
├── MONOREPO_INTEGRATION_PLAN.md # 本方案文档
├── pom.xml                      # 后端 Maven 聚合
├── package.json                 # 前端 Npm 根配置
├── pnpm-workspace.yaml          # Pnpm 工作区配置
└── .gitignore
```

---

## 三、整合分阶段计划

### 阶段 1: 基础架构建设（第 1-2 周）

#### 目标

建立 Monorepo 的基础框架和文档体系，为后续迁移铺路。

#### 任务清单

1. **创建 Monorepo 根目录结构**

   - 初始化 Monorepo 的目录框架
   - 配置 .gitignore（排除各类构建产物）
   - 配置 .editorconfig（统一代码格式）
2. **建立后端 Maven 聚合**（⚠️ 当前阶段暂缓，各服务各自维护依赖）

   - ~~创建根 pom.xml，定义聚合模块~~（暂不创建，详见 [4.1 当前决策](#41-后端-maven-配置)）
   - ~~提取共享依赖管理 (dependencyManagement)~~（后续目标）
   - ~~配置统一的插件管理和编译配置~~（后续目标）
   - 当前：各服务（mes/platform/lims/wms/gateway）沿用独立 POM，保证可独立构建
3. **建立前端工作区**

   - 选定包管理器（推荐 pnpm）
   - 配置 pnpm-workspace.yaml
   - 创建前端根 package.json
4. **建立文档和知识库结构**

   - 创建 .spec 目录框架
   - 初始化各个文档分类的 README
   - 创建 docs/ 文档目录
5. **设置 CI/CD 框架**

   - 创建 .github/workflows 目录
   - 定义通用的构建、测试、发布流程

#### 交付物

- Monorepo 目录结构完整
- 根级 pom.xml 和 package.json 配置完成
- 文档框架齐全
- CI/CD 模板完成

---

### 阶段 2: 核心文档和设计文档编写（第 2-4 周）

#### 目标

建立完善的设计文档和 AI Context 文档，为开发和 AI 辅助提供清晰的参考。

#### 2.1 设计文档编写

| 文档                    | 内容                               | 负责人         | 截止日期 |
| ----------------------- | ---------------------------------- | -------------- | -------- |
| architecture.md         | 整体微服务架构、模块划分、通信方式 | 架构师         | W2       |
| data-flow.md            | 主要业务流程的数据流、事件流       | 业务负责人     | W2       |
| api-gateway.md          | API 网关设计、路由规则、限流       | 网关负责人     | W3       |
| auth.md                 | 认证、授权、权限体系设计           | 安全负责人     | W3       |
| database-erd.md         | 核心表结构关系、分库分表策略       | DBA/后端负责人 | W3       |
| integration-protocol.md | 服务间通信协议、RPC、MQ            | 中间件负责人   | W3       |

#### 2.2 代码理解文档编写

| 文档                                  | 内容                                         | 生成方式             |
| ------------------------------------- | -------------------------------------------- | -------------------- |
| .spec/steering/product.md             | 系统产品功能概览                             | 自动生成（spec-mcp） |
| .spec/steering/tech.md                | 技术栈、框架、关键决策                       | 自动生成（spec-mcp） |
| .spec/steering/structure.md           | 代码树、包结构、关键类                       | 自动生成（spec-mcp） |
| .spec/context/entity-relationships.md | 核心实体（Order、Material、Process等）的关系 | 手动编写             |
| .spec/context/business-rules.md       | 业务规则（工单流程、物料批次等）             | 手动编写             |
| .spec/context/common-patterns.md      | 常用开发模式、工具方法、最佳实践             | 手动编写             |
| .spec/context/service-integration.md  | 服务间调用、事件驱动、异步处理               | 手动编写             |

#### 2.3 快速参考文档

| 文档                          | 内容              |
| ----------------------------- | ----------------- |
| .spec/wiki/quick-start.md     | 5分钟快速启动指南 |
| .spec/wiki/local-dev.md       | 本地开发环境配置  |
| .spec/wiki/common-patterns.md | 常见代码模式示例  |
| .spec/wiki/troubleshooting.md | 常见问题排查      |

#### 交付物

- 完整的设计文档（12+ 份）
- 自动生成的代码导航文档
- 快速参考文档
- 可供 AI 直接使用的 Context 文档

---

### 阶段 3: 后端服务迁移（第 4-6 周）

#### 目标

将后端微服务逐个迁移到 Monorepo。

#### 3.1 迁移策略

**逐个微服务迁移**:

1. **第一个服务**: mes（当前仓库，基础工作最少）
2. **第二个服务**: platform（关键依赖，需注意版本同步）
3. **第三个服务**: flow-engine（流程相关，影响面大）
4. **其他服务**: lims, wms, 等依次迁移

**每个服务迁移步骤**:

```
步骤 1: 复制源码到 packages/backend/services/[service-name]
步骤 2: 调整 pom.xml
  - 删除重复的依赖管理（用聚合 pom 的）
  - 更新父模块配置
  - 更新内部依赖版本
步骤 3: 配置共享库依赖
  - 将通用代码抽取到 packages/backend/shared/
  - 更新导入路径
步骤 4: 集成测试
  - 独立运行单个服务测试
  - 与其他已迁移服务联调
步骤 5: 更新 CI/CD 配置
步骤 6: 文档化（README、迁移说明）
```

#### 3.2 共享库抽取

在迁移过程中，识别并抽取以下共享库：

```
packages/backend/shared/
├── common/                  # 通用工具
│   ├── utils/              # 字符串、时间、集合工具
│   ├── exceptions/         # 自定义异常
│   ├── constants/          # 常量定义
│   └── pom.xml
├── models/                 # 共享数据模型
│   ├── dto/                # 各服务通用 DTO
│   ├── vo/                 # 各服务通用 VO
│   └── pom.xml
├── feign-clients/          # Feign 服务调用客户端
│   ├── mes-feign-client/
│   ├── platform-feign-client/
│   └── pom.xml
├── starters/               # Spring Boot AutoConfiguration 启动器
│   ├── starter-data/
│   ├── starter-file/
│   ├── starter-rocketmq/
│   └── pom.xml
└── pom.xml                 # 聚合 shared 模块
```

#### 3.3 依赖版本管理

> **当前策略：各服务各自维护依赖版本，暂不统一。**
>
> 各服务保留迁移前独立的 `<properties>` 与 `<dependencyManagement>`，自行维护第三方库与 `bmos.version` 等框架版本。**不引入**后端根 POM 做统一约束。原因详见 [4.1 后端 Maven 配置](#41-后端-maven-配置) 的当前决策说明。

**后续目标（待专门窗口推进）**：建立后端根 `pom.xml`，将各服务**本就一致**的依赖版本收口到 `<dependencyManagement>`，分歧项保留在各服务 POM 中覆盖。统一后的目标形态如下（仅作设计参考，当前未落地）：

```xml
<!-- 后续目标：根 pom.xml 维护各服务一致的依赖版本 -->
<dependencyManagement>
  <dependencies>
    <!-- 平台依赖 -->
    <dependency>
      <groupId>com.bmos</groupId>
      <artifactId>bmos-cloud-dependency</artifactId>
      <version>${bmos.version}</version>
    </dependency>
  
    <!-- 内部共享库 -->
    <dependency>
      <groupId>com.bmos.mes</groupId>
      <artifactId>bmos-mes-common</artifactId>
      <version>${project.version}</version>
    </dependency>
  
    <!-- 第三方库版本统一定义 -->
  </dependencies>
</dependencyManagement>
```

#### 交付物

- 所有后端微服务迁移到 Monorepo
- 共享库提取完毕
- 各服务独立可构建、可测试、可运行
- 汇总文档更新

---

### 阶段 4: 前端应用迁移（第 6-8 周）

#### 目标

将前端应用集成为 pnpm 工作区。

#### 4.1 前端工作区配置

```yaml
# pnpm-workspace.yaml
packages:
  - 'packages/frontend/apps/*'
  - 'packages/frontend/shared'
```

#### 4.2 应用迁移

逐步迁移各个前端应用：

- admin-ui
- mes-ui
- lims-ui
- wms-ui
- ...

#### 4.3 共享库建设

```
packages/frontend/shared/
├── components/              # 共享 UI 组件库
│   ├── Button/
│   ├── Table/
│   ├── Form/
│   └── ...
├── hooks/                   # 共享 Hooks
│   ├── useRequest.ts
│   ├── useAuth.ts
│   └── ...
├── utils/                   # 共享工具
├── types/                   # 共享 TypeScript 类型
├── styles/                  # 共享样式变量
└── package.json
```

#### 交付物

- 前端 Monorepo 工作区完成
- 共享组件库提取
- 各应用独立可构建、可运行

---

### 阶段 5: 工具和流程完善（第 8-10 周）

#### 5.1 开发工具

1. **AI Context 自动生成工具**

   ```
   tools/ai-context-gen/
   ├── index.js                    # 主入口
   ├── generators/
   │   ├── entity-analyzer.js      # 实体关系分析
   │   ├── api-extractor.js        # API 文档提取
   │   ├── pattern-detector.js     # 代码模式检测
   │   └── dependency-mapper.js    # 依赖关系映射
   ├── templates/
   │   ├── entity-relationships.ejs
   │   ├── common-patterns.ejs
   │   └── ...
   └── README.md
   ```
2. **本地开发启动脚本**

   ```bash
   # scripts/local-dev-start.sh
   # 一键启动所有必要的本地服务（数据库、缓存、消息队列）
   # 配置环境变量
   # 启动选定的后端服务
   ```
3. **服务依赖检查工具**

   ```bash
   # scripts/check-service-deps.sh
   # 检查服务间依赖、循环依赖、版本冲突
   ```

#### 5.2 CI/CD 管道

```yaml
# .github/workflows/
├── build.yml               # 构建所有服务
├── test.yml                # 运行所有测试
├── lint.yml                # 代码质量检查
├── security-scan.yml       # 安全扫描
├── deploy-staging.yml      # 部署到测试环境
├── deploy-production.yml   # 部署到生产环境
└── documentation.yml       # 更新文档站点
```

#### 5.3 Monorepo 管理命令

```bash
# 在根目录定义统一命令
{
  "scripts": {
    "build": "mvn clean package",              # 构建所有后端
    "build:backend": "cd packages/backend && mvn clean package",
    "build:frontend": "cd packages/frontend && pnpm -r build",
    "test": "mvn verify && pnpm -r test",
    "test:backend": "mvn verify",
    "test:frontend": "pnpm -r test",
    "lint": "mvn spotless:check && pnpm -r lint",
    "docs": "npm run docs:api && npm run docs:design",
    "dev": "scripts/local-dev-start.sh",
    "check-deps": "scripts/check-service-deps.sh"
  }
}
```

#### 交付物

- AI Context 生成工具
- 本地开发脚本和指南
- 完整的 CI/CD 流程
- 统一的 Monorepo 管理命令

---

### 阶段 6: Code Wiki 知识库建设（第 10-12 周）

#### 目标

建立持续更新的代码 Wiki，作为 AI 开发的知识库。

#### 6.1 知识库结构

```
.spec/wiki/
├── 快速导航/
│   ├── quick-start.md          # 5分钟快速开始
│   ├── local-dev.md            # 本地开发设置
│   ├── folder-structure.md     # 目录结构速览
│   └── key-modules.md          # 关键模块速览
│
├── 架构与设计/
│   ├── system-architecture.md  # 系统整体架构
│   ├── service-boundaries.md   # 服务边界定义
│   ├── data-flow.md            # 数据流
│   ├── communication.md        # 服务通信方式
│   └── design-patterns.md      # 设计模式
│
├── 核心概念/
│   ├── domain-model.md         # 领域模型
│   ├── entities.md             # 核心实体详解
│   ├── business-rules.md       # 业务规则集合
│   ├── workflows.md            # 主要工作流
│   └── integration-points.md   # 服务集成点
│
├── 开发指南/
│   ├── coding-standards.md     # 编码规范
│   ├── api-guidelines.md       # API 设计规范
│   ├── testing-strategy.md     # 测试策略
│   ├── database-design.md      # 数据库设计规范
│   ├── frontend-guidelines.md  # 前端开发规范
│   └── common-patterns.md      # 常见代码模式
│
├── 实战示例/
│   ├── add-new-feature.md      # 新功能开发流程
│   ├── fix-bug.md              # Bug 修复流程
│   ├── cross-service-call.md   # 跨服务调用示例
│   ├── async-processing.md     # 异步处理示例
│   └── error-handling.md       # 错误处理示例
│
├── 工具和命令/
│   ├── useful-commands.md      # 常用命令
│   ├── debug-tips.md           # 调试技巧
│   ├── performance-tuning.md   # 性能优化
│   └── troubleshooting.md      # 问题排查
│
├── FAQ/
│   ├── general-qa.md           # 常见问题
│   ├── backend-qa.md           # 后端 FAQ
│   ├── frontend-qa.md          # 前端 FAQ
│   └── devops-qa.md            # DevOps FAQ
│
└── 发布日志/
    ├── migration-log.md        # Monorepo 迁移日志
    ├── breaking-changes.md     # 重大变更
    └── deprecations.md         # 废弃功能
```

#### 6.2 知识库维护策略

1. **内容来源**

   - 设计评审会议记录
   - Code Review 反馈
   - 技术债讨论
   - Bug 复盘总结
   - 新特性开发记录
2. **更新频率**

   - 关键设计文档: 需求/架构变更时更新
   - 代码示例: 代码风格变化时更新
   - FAQ: 每月汇总新问题
   - 快速参考: 每个版本发布时更新
3. **版本控制**

   - 所有文档纳入 Git 版本管理
   - 重要变更配合 Changelog
   - 定期审视和整理（每季度）

#### 6.3 与 AI 的集成

在与 Claude AI 交互时：

```markdown
# 与 AI 协作的最佳实践

1. **引用文档**
   - 在 prompt 中链接相关文档
   - 例：请参考 `.spec/context/entity-relationships.md` 来理解订单实体

2. **Context 提供**
   - 为具体任务提供相关的 context 文件
   - AI 将自动加载相关文档进行理解

3. **生成文档**
   - 新功能开发完成后，要求 AI 生成/更新相关文档
   - 使用 `mcp__spec-mcp__generate-tasks` 自动生成任务清单

4. **知识积累**
   - 每个 review 或修复后，更新相关文档
   - 形成正反馈循环
```

#### 交付物

- 完整的 Code Wiki（50+ 篇文档）
- Wiki 维护指南
- AI 最佳实践指南
- 文档模板和检查清单

---

## 四、技术细节和最佳实践

### 4.1 后端 Maven 配置

> **⚠️ 当前决策（2026-06-29）：后端各微服务暂时各自维护依赖，不做统一管理。**
>
> 经评估，当前阶段**不引入** `packages/backend/pom.xml` 根聚合 POM，各服务（mes、platform、lims、wms、gateway）继续沿用迁移前的独立 POM，各自维护 `<properties>`、`<dependencyManagement>` 与构建插件配置。
>
> **为什么暂不统一：**
> - 各服务的核心框架版本 `bmos.version` 存在真实分歧（gateway/wms 为 `1.14.0`、platform/mes 为 `1.15.0`、lims 为 `1.15.2`），`bmos.version` 控制的是一整套 `com.bmos:*` 内部框架制品（`bmos-cloud-dependency` BOM、各类 `bmos-starter-*`）的版本。强行统一等于在一次 POM 重构中顺带升级多个服务的框架版本，会把"整理依赖"与"升级框架"两件高风险的事捆绑，难以隔离问题。
> - 当前各服务刚通过 git subtree 完成整合，优先保证**各服务可独立构建、行为不变**，降低迁移期风险。
> - 依赖统一收益（去重、版本一致性）真实存在，但属于可延后的优化，不应阻塞整合主线。
>
> **后续目标（依赖统一，待专门窗口推进）：**
> 1. 先建立 `packages/backend/pom.xml` 作为后端根 POM，**只收口各服务本就一致的依赖**（lombok、mapstruct、mybatis-plus、easyexcel、jackson、poi、snakeyaml、shardingsphere 等）及统一的构建插件（compiler、flatten）、`distributionManagement`，零运行时变化。
> 2. 分歧项（`bmos.version`、flow/audit-engine、platform-facade、docx4j 等）**显式保留在各服务 POM 中作为覆盖**，使分歧可见、可追踪，而非被根 POM 悄悄抹平。
> 3. 后续逐个服务对齐 `bmos.version` 等框架版本，**每次只动一个服务并做回归测试**，风险可控。
> 4. 整个过程采用渐进式：先 mes 作为样板验证（`mvn validate` / `dependency:resolve` 确认 POM 结构与依赖解析无误），再批量推广到其余服务。
>
> **本章节下文描述的"版本集中管理 / 根 POM 统一 dependencyManagement"方案，是上述后续目标的设计参考，当前尚未落地。**

后端与前端的对称设计：frontend 有根级 `package.json`，backend 也有根级 `pom.xml`。

#### 架构层级

```
Monorepo 根目录
├── pom.xml                          # 前端前端聚合（可选，用于统一构建）
├── package.json
├── packages/
│   ├── backend/
│   │   ├── pom.xml                 # 后端根 POM ⭐ 关键
│   │   ├── shared/                 # 共享库
│   │   │   ├── common/
│   │   │   │   └── pom.xml
│   │   │   ├── models/
│   │   │   │   └── pom.xml
│   │   │   └── feign-clients/
│   │   │       └── pom.xml
│   │   └── services/               # 微服务
│   │       ├── mes/
│   │       │   └── pom.xml
│   │       ├── platform/
│   │       │   └── pom.xml
│   │       └── ...
│   └── frontend/
│       ├── package.json             # 前端根 package.json ⭐ 对称
│       ├── pnpm-workspace.yaml
│       ├── apps/
│       └── shared/
```

#### packages/backend/pom.xml 结构

**关键职责：**

1. **版本集中管理** - 所有依赖版本在这里定义一次，所有子模块继承
2. **模块聚合** - 定义 shared 和 services 的构建顺序
3. **依赖约束** - `<dependencyManagement>` 确保版本一致性
4. **插件标准化** - 编译、测试、打包等插件配置集中管理

**核心配置示例：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.bmos</groupId>
  <artifactId>bmos-monorepo-backend</artifactId>
  <version>${revision}</version>
  <packaging>pom</packaging>
  <name>BMOS Monorepo Backend Root</name>

  <!-- ============================================ -->
  <!-- 版本属性：集中管理 -->
  <!-- ============================================ -->
  <properties>
    <!-- 项目版本使用 CI Friendly Versions -->
    <revision>1.0-SNAPSHOT</revision>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>

    <!-- 内部版本管理：所有微服务和共享库统一版本 -->
    <bmos.internal.version>${revision}</bmos.internal.version>

    <!-- BMOS 平台依赖版本 -->
    <bmos.version>1.15.0-SNAPSHOT</bmos.version>
    <bmos-flow-engine.version>0.0.5-SNAPSHOT</bmos-flow-engine.version>
    <bmos-audit-engine.version>0.0.3-SNAPSHOT</bmos-audit-engine.version>
  
    <!-- Spring Boot 版本 -->
    <spring-boot.version>2.6.15</spring-boot.version>

    <!-- 数据库驱动版本 -->
    <mariadb.version>3.0.9</mariadb.version>
    <mysql.version>8.0.32</mysql.version>

    <!-- ORM 和数据访问 -->
    <mybatis-plus.version>3.5.3.2</mybatis-plus.version>
    <shardingsphere.version>5.5.1</shardingsphere.version>

    <!-- 代码生成和处理 -->
    <lombok.version>1.18.20</lombok.version>
    <mapstruct.version>1.4.1.Final</mapstruct.version>

    <!-- 文件处理 -->
    <poi.version>5.2.3</poi.version>
    <easyexcel.version>3.3.2</easyexcel.version>

    <!-- Maven 插件版本 -->
    <maven-compiler-plugin.version>3.8.1</maven-compiler-plugin.version>
    <spring-boot-maven-plugin.version>2.6.15</spring-boot-maven-plugin.version>
    <flatten-maven-plugin.version>1.5.0</flatten-maven-plugin.version>
  </properties>

  <!-- ============================================ -->
  <!-- 模块聚合：定义构建顺序 -->
  <!-- ============================================ -->
  <modules>
    <!-- 共享库优先构建（被其他服务依赖） -->
    <module>shared/common</module>
    <module>shared/models</module>
    <module>shared/feign-clients</module>
    <module>shared/starters</module>

    <!-- 微服务（依赖共享库）-->
    <module>services/mes</module>
    <!-- <module>services/platform</module> -->
    <!-- <module>services/lims</module> -->
    <!-- <module>services/wms</module> -->

    <!-- 引擎和框架 -->
    <!-- <module>engines/flow-engine</module> -->
  </modules>

  <!-- ============================================ -->
  <!-- 依赖管理：集中定义所有依赖版本 -->
  <!-- ============================================ -->
  <dependencyManagement>
    <dependencies>
      <!-- 平台依赖 -->
      <dependency>
        <groupId>com.bmos</groupId>
        <artifactId>bmos-cloud-dependency</artifactId>
        <version>${bmos.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>

      <!-- Spring Boot -->
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring-boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>

      <!-- 内部共享库 -->
      <dependency>
        <groupId>com.bmos</groupId>
        <artifactId>bmos-mes-common</artifactId>
        <version>${bmos.internal.version}</version>
      </dependency>

      <dependency>
        <groupId>com.bmos</groupId>
        <artifactId>bmos-mes-models</artifactId>
        <version>${bmos.internal.version}</version>
      </dependency>

      <dependency>
        <groupId>com.bmos</groupId>
        <artifactId>bmos-mes-feign-clients</artifactId>
        <version>${bmos.internal.version}</version>
      </dependency>

      <!-- ORM -->
      <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus</artifactId>
        <version>${mybatis-plus.version}</version>
      </dependency>

      <!-- 代码生成（Lombok + MapStruct） -->
      <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <scope>provided</scope>
      </dependency>

      <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
      </dependency>

      <!-- 文件处理 -->
      <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>${poi.version}</version>
      </dependency>

      <!-- 其他第三方库... -->
    </dependencies>
  </dependencyManagement>

  <!-- ============================================ -->
  <!-- 插件管理：统一 build 配置 -->
  <!-- ============================================ -->
  <build>
    <pluginManagement>
      <plugins>
        <!-- 编译插件：处理 Lombok + MapStruct 组合 -->
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <version>${maven-compiler-plugin.version}</version>
          <configuration>
            <source>8</source>
            <target>8</target>
            <annotationProcessorPaths>
              <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>${lombok.version}</version>
              </path>
              <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${mapstruct.version}</version>
              </path>
            </annotationProcessorPaths>
          </configuration>
        </plugin>

        <!-- Spring Boot 打包插件 -->
        <plugin>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-maven-plugin</artifactId>
          <version>${spring-boot-maven-plugin.version}</version>
          <configuration>
            <includeSystemScope>true</includeSystemScope>
          </configuration>
        </plugin>

        <!-- Flatten 插件：处理 CI Friendly Versions -->
        <plugin>
          <groupId>org.codehaus.mojo</groupId>
          <artifactId>flatten-maven-plugin</artifactId>
          <version>${flatten-maven-plugin.version}</version>
          <configuration>
            <flattenMode>resolveCiFriendliesOnly</flattenMode>
            <updatePomFile>true</updatePomFile>
          </configuration>
          <executions>
            <execution>
              <goals>
                <goal>flatten</goal>
              </goals>
              <phase>process-resources</phase>
            </execution>
          </executions>
        </plugin>
      </plugins>
    </pluginManagement>

    <!-- 通用插件（所有子模块默认应用） -->
    <plugins>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>flatten-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

#### 子模块 pom.xml 简化示例

每个微服务或共享库只需指定 parent，无需重复依赖版本：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>
  
  <!-- 指向 backend 根 POM -->
  <parent>
    <groupId>com.bmos</groupId>
    <artifactId>bmos-monorepo-backend</artifactId>
    <version>${revision}</version>
    <relativePath>../../../pom.xml</relativePath>
  </parent>

  <artifactId>bmos-mes-service</artifactId>
  <packaging>jar</packaging>
  <name>MES Service</name>

  <dependencies>
    <!-- ✅ 无需写版本号，从根 pom 继承 -->
    <dependency>
      <groupId>com.bmos</groupId>
      <artifactId>bmos-mes-common</artifactId>
    </dependency>

    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus</artifactId>
    </dependency>

    <!-- 测试依赖 -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <!-- Spring Boot 应用才需要这个配置 -->
  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>

</project>
```

#### Maven 命令

```bash
# 从 Monorepo 根目录构建整个后端
cd /path/to/monorepo
mvn clean package

# 或只构建特定服务
mvn clean package -pl packages/backend/services/mes

# 构建一个服务及其依赖
mvn clean package -pl packages/backend/services/mes -am

# 跳过测试快速构建
mvn clean package -DskipTests
```

#### 版本管理说明

使用 **CI Friendly Versions** (`${revision}`)：

```bash
# Maven 自动处理版本替换
# properties 中定义：<revision>1.0-SNAPSHOT</revision>
# 所有模块自动使用这个版本，无需逐个修改

# 发布时，CI/CD 可以直接覆盖：
mvn clean deploy -Drevision=1.0.0
```

这比手动维护每个模块的版本号更安全高效。

#### ❓ 常见问题：多层级 POM 对 CI/CD 打包启动的影响

**问题**: 最顶层目录（Monorepo 根）的 pom 管理所有依赖版本和定义，在做 CI/CD 时，是否会影响打包启动？

**答案**: **不会有负面影响**。这是 Maven Monorepo 的标准实践。以下是详细说明：

##### 架构层级回顾

```
Monorepo 根目录 (可选，用于统一构建所有项目)
├── pom.xml                      # ⭐ 可选：统一聚合所有 backend/frontend
├── packages/
│   ├── backend/
│   │   ├── pom.xml             # ⭐ 核心：管理所有后端依赖版本
│   │   ├── shared/             # 依赖 backend/pom.xml
│   │   └── services/           # 依赖 backend/pom.xml
│   └── frontend/
│       └── package.json
```

##### 1. 打包过程（CI/CD 构建）

**命令**: `mvn clean package` （在 Monorepo 根或 packages/backend 目录执行）

**过程**:

```
① Maven 读取子模块的 pom.xml
   └─> 发现 <parent> 指向 packages/backend/pom.xml
   
② Maven 加载 packages/backend/pom.xml
   └─> 读取 <dependencyManagement> 中的版本定义
   └─> 读取 <properties> 中的版本号
   
③ Maven 解析依赖
   └─> 如果依赖未指定版本，从 <dependencyManagement> 获取
   └─> 如果依赖指定了版本，使用指定版本（可覆盖）
   
④ Maven 下载依赖到本地 ~/.m2/repository
   └─> 这是纯 Maven 行为，与目录结构无关
   
⑤ Maven 编译、打包生成 JAR/WAR
   └─> 打包时已经解析完所有依赖，与 pom 层级无关
   
⑥ 输出到 target/ 目录
   └─> 与上层 pom 位置无关
```

**关键点**:

- ✅ Maven 会递归查找 parent pom，不受目录深度影响
- ✅ 依赖版本在编译期确定，打包时已固定
- ✅ 最终生成的 JAR 是独立的，不依赖上层 pom 文件

##### 2. 运行启动（CI/CD 部署）

**场景**: 部署到服务器或容器

```bash
# Docker 容器启动
java -jar bmos-mes-service-1.0-SNAPSHOT.jar

# 此时根本不需要 pom.xml 文件
# JAR 包内已经包含了所有依赖信息和编译后的字节码
```

**关键点**:

- ✅ 运行时不需要 pom.xml，只需要 JAR 文件
- ✅ 打包后的 JAR 是完全独立的
- ✅ CI/CD 流程中，只在构建阶段需要 pom.xml

##### 3. CI/CD 流程示例

```yaml
# GitHub Actions 示例
name: Build and Deploy

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      # 步骤 1: 拉取代码（包括所有 pom.xml）
      - uses: actions/checkout@v2
  
      # 步骤 2: 设置 Java 环境
      - uses: actions/setup-java@v2
        with:
          java-version: '8'
  
      # 步骤 3: 构建（需要 pom.xml）
      - name: Build with Maven
        run: |
          cd packages/backend
          mvn clean package -DskipTests
  
      # ✅ 此时 pom.xml 已完成任务
      # ✅ 生成的 JAR 文件在 target/ 目录中
  
      # 步骤 4: 构建 Docker 镜像
      - name: Build Docker Image
        run: |
          docker build \
            -t bmos-mes:latest \
            -f packages/backend/services/mes/Dockerfile \
            packages/backend/services/mes/target/
  
      # ✅ Dockerfile 只使用 JAR，不需要 pom.xml
  
      # 步骤 5: 推送镜像
      - name: Push Docker Image
        run: docker push bmos-mes:latest
  
      # 步骤 6: 部署
      - name: Deploy
        run: |
          kubectl set image deployment/mes-service \
            mes-service=bmos-mes:latest
  
      # ✅ 运行时完全不需要 pom.xml
```

##### 4. 各层级 POM 的作用时间

| 阶段                      | 何时使用               | 需要的 POM      | 说明                         |
| ------------------------- | ---------------------- | --------------- | ---------------------------- |
| **本地开发**        | 开发时                 | backend/pom.xml | IDE 使用，解析依赖、代码补全 |
| **编译阶段**        | `mvn compile`        | backend/pom.xml | 必需，确定依赖版本           |
| **打包阶段**        | `mvn package`        | backend/pom.xml | 必需，打包依赖进 JAR         |
| **测试阶段**        | `mvn test`           | backend/pom.xml | 必需，执行单元测试           |
| **CI/CD 构建**      | Jenkins/GitHub Actions | backend/pom.xml | 必需，构建流程使用           |
| **Docker 构建**     | `docker build`       | ❌ 不需要       | 只需要 JAR 文件              |
| **容器运行**        | `docker run`         | ❌ 不需要       | JAR 已包含所有信息           |
| **Kubernetes 部署** | `kubectl apply`      | ❌ 不需要       | 只部署 Docker 镜像           |

##### 5. 版本覆盖策略（CI/CD 动态版本）

在 CI/CD 中，可以动态覆盖版本号，而不影响本地开发：

```bash
# 本地开发（使用 pom.xml 中的 revision）
mvn clean package

# CI/CD 构建（动态覆盖版本号）
mvn clean package -Drevision=1.0.20260626.123

# 参数说明：
# -Drevision=版本号    # 覆盖 <revision> 属性
# -DskipTests          # 跳过测试加快构建
# -X                   # 调试模式，查看 Maven 详细日志
```

##### 6. 常见 CI/CD 场景下的最佳实践

**场景 A: 部署单个服务**

```bash
# ✅ 只构建指定服务及其依赖
cd Monorepo 根目录
mvn clean package \
  -pl packages/backend/services/mes \
  -am \
  -Drevision=1.0.20260626.123 \
  -DskipTests

# 输出: packages/backend/services/mes/target/bmos-mes-service-1.0.*.jar
```

**场景 B: 部署多个服务**

```bash
# ✅ 构建整个后端
cd packages/backend
mvn clean package \
  -Drevision=1.0.20260626.123 \
  -DskipTests

# 输出:
# services/mes/target/bmos-mes-service-1.0.*.jar
# services/platform/target/bmos-platform-service-1.0.*.jar
# ...
```

**场景 C: 按需部署**

```bash
# ✅ 使用 git diff 确定哪些服务改变，只构建改变的服务
CHANGED_MODULES=$(git diff --name-only HEAD~1 HEAD | \
  grep -o 'packages/backend/services/[^/]*' | sort -u)

for module in $CHANGED_MODULES; do
  mvn clean package -pl $module -am -DskipTests
done
```

##### 7. 潜在风险及防控

| 风险                        | 影响               | 防控措施                                                 |
| --------------------------- | ------------------ | -------------------------------------------------------- |
| **版本不一致**        | 依赖冲突           | ✅ 使用 dependencyManagement 统一版本                    |
| **构建失败传播**      | 整体构建失败       | ✅ 使用`-pl` 单独构建，`-am` 构建依赖                |
| **本地 .m2 缓存过期** | 构建使用旧依赖     | ✅ CI/CD 中使用`mvn dependency:purge-local-repository` |
| **网络问题**          | 依赖下载失败       | ✅ 配置私有 Maven 仓库或镜像                             |
| **跨时间发布**        | 相同版本号不同内容 | ✅ 使用 timestamp 或 build number                        |

##### 8. 优化建议

```yaml
# Dockerfile 最佳实践
FROM maven:3.8-jdk-8 as builder

WORKDIR /build

# 只复制 pom 文件，充分利用 Docker 缓存
COPY pom.xml ./
COPY packages/backend/pom.xml ./packages/backend/
COPY packages/backend/shared ./packages/backend/shared
COPY packages/backend/services/mes ./packages/backend/services/mes

# 构建（pom 缓存会被复用，加快构建）
RUN cd packages/backend && \
    mvn clean package \
      -DskipTests \
      -Drevision=${VERSION:-1.0-SNAPSHOT}

# 运行阶段：不需要 Maven 或 pom.xml
FROM openjdk:8-jre-slim

COPY --from=builder /build/packages/backend/services/mes/target/bmos-mes-*.jar /app/

ENTRYPOINT ["java", "-jar", "/app/bmos-mes-*.jar"]
```

##### 总结

| 问题                 | 答案                                                                  |
| -------------------- | --------------------------------------------------------------------- |
| 打包时会受影响吗？   | ❌**不会**。Maven 会递归查找 parent pom，打包时已解析完所有依赖 |
| 启动时会受影响吗？   | ❌**不会**。JAR 文件是独立的，运行时不需要 pom.xml              |
| CI/CD 会受影响吗？   | ✅**不受影响**。构建阶段需要 pom.xml，但这是标准流程            |
| 版本管理会出问题吗？ | ❌**不会**。这是业界最佳实践，Google、Facebook 等大公司都用     |
| 性能会下降吗？       | ❌**不会**。多层级 pom 略增加初始解析时间，但可忽略不计         |

**最终结论**: 这种多层级 POM 架构是 **Maven Monorepo 的标准做法**，对构建、打包、运行都没有负面影响，反而能够：

- ✅ 统一版本管理
- ✅ 减少配置重复
- ✅ 提高依赖一致性
- ✅ 便于跨服务协调

### 4.2 前端 pnpm 配置

#### 根 package.json

```json
{
  "name": "bmos-monorepo-frontend",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "build": "pnpm -r build",
    "test": "pnpm -r test",
    "lint": "pnpm -r lint",
    "dev": "pnpm -r --parallel dev",
    "type-check": "pnpm -r type-check"
  },
  "devDependencies": {
    "@types/node": "^18.0.0",
    "@typescript-eslint/eslint-plugin": "^5.0.0",
    "eslint": "^8.0.0",
    "prettier": "^2.8.0",
    "typescript": "^4.9.0"
  }
}
```

#### pnpm-workspace.yaml

```yaml
packages:
  - 'packages/frontend/apps/*'
  - 'packages/frontend/shared'
```

### 4.3 .gitignore 配置

```
# Maven
target/
*.class
*.jar
*.war
*.nar
*.zip
*.tar.gz
*.rar
.m2/

# Node
node_modules/
dist/
build/
*.log
npm-debug.log*
yarn-debug.log*
yarn-error.log*
.npm
.yarn
pnpm-debug.log

# IDE
.idea/
.vscode/
*.swp
*.swo
*~
.DS_Store

# OS
.DS_Store
Thumbs.db

# Build
/build
/dist

# Environment
.env
.env.local
.env.*.local

# Logs
logs/
*.log

# Database
/data
/database

# Cache
.cache/
*.cache
```

### 4.4 本地开发环境配置

#### scripts/local-dev-start.sh

```bash
#!/bin/bash

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

set -e

echo "${GREEN}=== BMOS Monorepo 本地开发环境启动 ===${NC}"

# 1. 检查依赖
echo "检查依赖..."
command -v mvn >/dev/null 2>&1 || { echo "${RED}Maven 未安装${NC}"; exit 1; }
command -v pnpm >/dev/null 2>&1 || { echo "${RED}pnpm 未安装${NC}"; exit 1; }
command -v docker >/dev/null 2>&1 || { echo "${RED}Docker 未安装${NC}"; exit 1; }

# 2. 启动基础服务（Docker）
echo "启动基础服务..."
docker-compose -f docker-compose.local.yml up -d

# 3. 等待服务就绪
echo "等待数据库服务就绪..."
sleep 5

# 4. 构建共享库
echo "构建后端共享库..."
cd packages/backend/shared
mvn clean install -q

# 5. 启动后端服务（可选）
read -p "是否启动后端服务？(y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    cd ../services/mes
    mvn spring-boot:run &
fi

# 6. 安装前端依赖
echo "安装前端依赖..."
cd packages/frontend
pnpm install

echo "${GREEN}=== 启动完成 ===${NC}"
echo "后端服务地址: http://localhost:8080"
echo "前端开发: cd packages/frontend/apps/[app-name] && pnpm dev"
```

#### docker-compose.local.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0.32
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: bmos
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

  rabbitmq:
    image: rabbitmq:3.12-management
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    ports:
      - "5672:5672"
      - "15672:15672"

volumes:
  mysql_data:
```

---

## 五、AI 开发工作流

### 5.1 使用场景

#### 场景 1: 新功能开发

```
用户需求 → 查阅设计文档 (设计、API规范)
          → 了解领域模型 (entity-relationships.md)
          → 查阅相似功能实现 (common-patterns.md)
          → AI 自动生成代码框架
          → 代码审查 + 文档更新
          → 完成
```

#### 场景 2: Bug 修复

```
Bug 报告 → 查阅相关业务规则 (business-rules.md)
        → 查阅受影响的代码流程 (data-flow.md)
        → AI 定位问题并生成修复
        → 修复验证 + 回归测试
        → 文档更新（如有重要发现）
        → 完成
```

#### 场景 3: 代码重构

```
重构需求 → 查阅架构设计 (architecture.md)
        → 理解依赖关系 (dependency-mapper输出)
        → AI 生成重构方案
        → 审查并迭代方案
        → 实施重构 + 测试
        → 文档更新
        → 完成
```

### 5.2 AI Prompt 模板

#### 模板 1: 新功能开发

```markdown
## 任务：[功能名称]

### 背景信息
- 参考文档：`.spec/design/[相关设计文档]`
- 相关实体：见 `.spec/context/entity-relationships.md`
- 业务规则：见 `.spec/context/business-rules.md`

### 需求描述
[具体需求]

### 实现指南
1. 查阅 `.spec/context/common-patterns.md` 中的相似实现
2. 按照 `.spec/wiki/add-new-feature.md` 的流程进行
3. 参考 API 规范文档：`.spec/api/[service]-api.md`

### 预期输出
- 代码实现（包含测试）
- 更新相关文档
```

#### 模板 2: Bug 修复

```markdown
## Bug：[Bug描述]

### 问题分析
- 预期行为：[描述]
- 实际行为：[描述]
- 相关文档：`.spec/context/business-rules.md`

### 调查步骤
1. 查阅数据流：`.spec/design/data-flow.md`
2. 理解工作流：`.spec/wiki/workflows.md`
3. 查找相关代码
```

### 5.3 文档使用指南

AI 在开发过程中应该：

1. **首先阅读 `.spec/steering/`**

   - 快速理解项目规模和技术栈
   - 定位相关的服务和模块
2. **查阅 `.spec/design/`**

   - 了解系统整体设计
   - 理解架构决策
3. **查阅 `.spec/context/`**

   - 深入理解业务逻辑
   - 学习代码模式
4. **查阅 `.spec/wiki/`**

   - 查看具体实现示例
   - 了解工具和命令

---

## 六、迁移风险和应对

### 6.1 主要风险

| 风险                     | 影响                   | 应对措施                       |
| ------------------------ | ---------------------- | ------------------------------ |
| **依赖循环**       | 构建失败、部署问题     | 提前做依赖分析、建立依赖委员会 |
| **版本冲突**       | 运行时问题、兼容性问题 | 统一版本管理、定期依赖审计     |
| **性能下降**       | 构建/测试时间增加      | 优化 Maven/npm 配置、并行构建  |
| **开发效率阵痛**   | 学习成本、开发速度变慢 | 充分文档、培训、循序渐进迁移   |
| **现有工作流断裂** | 生产力受影响           | 平行运行、完整的回退计划       |

### 6.2 风险缓解策略

1. **渐进式迁移**

   - 不一次性迁移所有仓库
   - 保留原仓库的完整功能，Monorepo 仅作为参考
   - 计划至少半年的过渡期
2. **平行运行**

   - 新 Monorepo 与旧仓库并行存在
   - 定期将更改同步到新结构
   - 确认工作流完全可行后再全量迁移
3. **完整的回退计划**

   - 保留所有原始仓库和分支
   - 文档化回退步骤
   - 准备应急方案
4. **充分的团队培训**

   - 提前准备培训材料
   - 组织工作坊和讨论
   - 建立答疑机制

---

## 七、实施时间表

| 阶段         | 周数    | 主要工作                     | 关键交付       |
| ------------ | ------- | ---------------------------- | -------------- |
| 1: 基础设施  | W1-W2   | 目录结构、pom、工作区配置    | Monorepo 框架  |
| 2: 文档建设  | W2-W4   | 设计文档、context 文档、wiki | Code Wiki v1.0 |
| 3: 后端迁移  | W4-W6   | 各微服务迁移、共享库提取     | 后端服务完整   |
| 4: 前端迁移  | W6-W8   | 应用迁移、工作区配置         | 前端工作区完整 |
| 5: 工具完善  | W8-W10  | AI Context工具、CI/CD、脚本  | 开发工具完整   |
| 6: Wiki 完善 | W10-W12 | 知识库补充、示例完善         | Code Wiki v2.0 |

---

## 八、度量指标

### 8.1 项目成功指标

| 指标                  | 目标                | 度量方式                   |
| --------------------- | ------------------- | -------------------------- |
| **构建时间**    | <10分钟（完整构建） | `mvn clean package` 耗时 |
| **文档完整性**  | >90% 模块有文档     | 覆盖度检查                 |
| **测试覆盖率**  | >70% 代码覆盖       | jacoco 报告                |
| **知识库规模**  | >50 篇文档          | 文档计数                   |
| **AI 工作效率** | 功能开发提速 50%    | 对比开发周期               |
| **开发满意度**  | >4/5 分             | 团队问卷                   |

### 8.2 持续改进

- 每月检查一次度量指标
- 根据反馈调整流程
- 季度 review 和优化

---

## 九、后续维护

### 9.1 日常维护

1. **定期整理文档**

   - 每个 sprint 结束更新相关文档
   - 每月审视 FAQ 和常见问题
2. **依赖管理**

   - 每月检查依赖更新
   - 定期扫描安全漏洞
3. **性能监控**

   - 监控构建、测试时间趋势
   - 定期优化瓶颈
4. **架构评审**

   - 每季度一次架构评审
   - 讨论重大变更和优化

### 9.2 版本管理

```
版本策略：语义版本 MAJOR.MINOR.PATCH

主版本 (MAJOR)：
  - 架构重大调整
  - 依赖库重大升级
  - API 不兼容变更

副版本 (MINOR)：
  - 新功能
  - 重构
  - 向后兼容的改进

补丁版本 (PATCH)：
  - Bug 修复
  - 文档更新
```

---

## 十、检查清单

### 初始化检查清单

- [ ] 创建 Monorepo 根目录结构
- [ ] 配置 .gitignore 和 .editorconfig
- [ ] 创建根 pom.xml 和 package.json
- [ ] 初始化文档框架
- [ ] 设置 CI/CD 基础配置

### 文档检查清单

- [ ] 完成所有设计文档（12+ 份）
- [ ] 运行 codebase-analysis 生成导航文档
- [ ] 编写 context 文档（实体、规则、模式）
- [ ] 编写快速参考文档
- [ ] 编写 API 文档

### 迁移检查清单（每个服务）

- [ ] 复制源码到 Monorepo
- [ ] 调整 pom.xml（或 package.json）
- [ ] 更新内部依赖引用
- [ ] 运行构建和测试
- [ ] 验证与其他服务的集成
- [ ] 更新文档

### 完成检查清单

- [ ] 所有后端微服务可独立构建
- [ ] 所有前端应用可独立构建
- [ ] CI/CD 流程完整可用
- [ ] 本地开发脚本可用
- [ ] Code Wiki 完整（>50 篇文档）
- [ ] 团队培训完成
- [ ] 文档和流程稳定运行 2 周以上

---

## 十、Monorepo 初始化详细步骤

这一章描述如何从零开始初始化一个 Monorepo，即使你已有多个独立的服务仓库。

### 10.1 初始化前的准备

#### 检查清单

- [ ] 确认所有源仓库的位置（git 地址或本地路径）
- [ ] 确认需要纳入 Monorepo 的分支（通常是 master/main 或开发分支）
- [ ] 备份所有原始仓库（防止意外丢失）
- [ ] 确认团队成员已知晓迁移计划
- [ ] 准备足够的磁盘空间（Monorepo 会更大）

#### 所需的源仓库清单

```
源仓库列表：
├── bmos-mes/              # 当前位置，已有
├── bmos-platform/         # 待拉取
├── bmos-lims/            # 待拉取
├── bmos-wms/             # 待拉取
├── bmos-flow-engine/     # 待拉取
├── bmos-cloud-scaffold/  # 待拉取
├── bmos-admin-ui/        # 待拉取
├── bmos-mes-ui/          # 待拉取
└── ...其他仓库
```

### 10.2 Monorepo 初始化工作流（使用 Git ==Subtree== 完全隔离方案）

**核心原则**：使用 `git subtree add` 完全导入各服务，保留完整 Git 历史，完全隔离独立发展。

#### 步骤 1: 初始化空的 Monorepo 仓库

```bash
# 创建新的空仓库
mkdir bmos-monorepo-final
cd bmos-monorepo-final
git init
git config user.email "team@bmos.com"
git config user.name "BMOS Team"

# 创建 packages 目录结构
mkdir -p packages/backend/{services,shared,engines}
mkdir -p packages/frontend/{apps,shared}
mkdir -p packages/devops

# 创建文档目录
mkdir -p .spec/{design,steering,context,api,database,wiki}
mkdir -p docs
mkdir -p tools/ai-context-gen
mkdir -p scripts
```

#### 步骤 2: 用 ==Subtree== 导入各个后端服务

**关键参数说明**：

- `--prefix=<path>`：目标路径
- `<repo-url>`：源仓库 URL
- `main`：从源仓库的 `main` 分支导入（稳定版本）
- `--squash`：压缩源仓库的历史为一个 commit（保持 Monorepo git log 清洁）

```bash
# 所有服务从 main 分支导入（生产稳定版本）

# 2.1 导入 MES 服务
git subtree add --prefix=packages/backend/services/mes \
  https://github.com/user/bmos-mes.git main --squash
echo "✅ MES imported"

# 2.2 导入 Platform 服务
git subtree add --prefix=packages/backend/services/platform \
  https://github.com/user/bmos-platform.git main --squash
echo "✅ Platform imported"

# 2.3 导入 LIMS 服务
git subtree add --prefix=packages/backend/services/lims \
  https://github.com/user/bmos-lims.git main --squash
echo "✅ LIMS imported"

# 2.4 导入 WMS 服务
git subtree add --prefix=packages/backend/services/wms \
  https://github.com/user/bmos-wms.git main --squash
echo "✅ WMS imported"

# 2.5 导入流程引擎（可选，如需要）
# git subtree add --prefix=packages/backend/engines/flow-engine \
#   https://github.com/user/bmos-flow-engine.git main --squash

# 2.6 导入脚手架（可选）
# git subtree add --prefix=packages/backend/shared/starters \
#   https://github.com/user/bmos-cloud-scaffold.git main --squash
```

**导入结果**：

- 每个服务的完整代码被合并到指定路径
- 源仓库的所有 commit 历史被保留
- 使用 `--squash` 后，源仓库的历史被压缩成 1 个 commit（保持 Monorepo 整洁）
- **完全隔离**：源仓库的后续改动不影响 Monorepo，Monorepo 的改动也不回推

#### 步骤 3: 导入前端应用

```bash
# 3.1 导入 App-Build-Template（MES 应用脚手架模板）
git subtree add --prefix=packages/frontend/apps/app-build-template \
  http://172.16.0.180/bmos/front-end/bmos-app-build-template.git master --squash
echo "✅ App-Build-Template imported"

# 3.2 导入 LIMS-App-Build-Template（LIMS 应用脚手架模板）
git subtree add --prefix=packages/frontend/apps/lims-app-build-template \
  http://172.16.0.180/bmos/front-end/lims-app-build-template.git master --squash
echo "✅ LIMS-App-Build-Template imported"

# 3.3 导入 MES-App（MES 业务应用）
git subtree add --prefix=packages/frontend/apps/mes-app \
  http://172.16.0.180/bmos/bmos-mes-app.git master --squash
echo "✅ MES-App imported"

# 3.4 导入 LIMS-App（LIMS 业务应用）
git subtree add --prefix=packages/frontend/apps/lims-app \
  http://172.16.0.180/bmos/front-end/bmos-lims-app.git master --squash
echo "✅ LIMS-App imported"

# 3.5 导入 Web（统一 Web 门户）
git subtree add --prefix=packages/frontend/apps/web \
  http://172.16.0.180/bmos/bmos-web.git master --squash
echo "✅ Web imported"
```

#### 步骤 4: 创建 Monorepo 根配置文件

```bash
# 4.1 创建根 pom.xml（后端聚合 POM）
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.bmos</groupId>
  <artifactId>bmos-monorepo</artifactId>
  <version>${revision}</version>
  <packaging>pom</packaging>
  <name>BMOS Monorepo Root</name>
  
  <properties>
    <revision>1.0-SNAPSHOT</revision>
  </properties>
  
  <modules>
    <module>packages/backend</module>
  </modules>
</project>
EOF

# 4.2 创建根 package.json（前端工作区）
cat > package.json << 'EOF'
{
  "name": "bmos-monorepo",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "build": "pnpm -r build",
    "test": "pnpm -r test",
    "lint": "pnpm -r lint",
    "dev": "pnpm -r --parallel dev"
  },
  "devDependencies": {
    "@types/node": "^18.0.0",
    "eslint": "^8.0.0",
    "prettier": "^2.8.0"
  }
}
EOF

# 4.3 创建 .gitignore
cat > .gitignore << 'EOF'
# Maven
target/
*.class
*.jar
*.war
*.nar
*.zip
*.tar.gz
*.rar

# Node
node_modules/
dist/
pnpm-debug.log

# IDE
.idea/
.vscode/
*.swp
*.swo

# OS
.DS_Store
Thumbs.db

# Logs
*.log

# Environment
.env
.env.local
EOF

# 4.4 创建 README.md
cat > README.md << 'EOF'
# BMOS Monorepo

统一的微服务和前端整合仓库。

## 项目结构

- `packages/backend/services/` - 后端微服务
- `packages/backend/shared/` - 后端共享库
- `packages/frontend/apps/` - 前端应用
- `.spec/` - 设计和文档库

## 初始化说明

本仓库通过 git subtree 导入各个源仓库，并完全隔离独立发展。

所有源仓库的完整 Git 历史已被保留，可通过以下命令查看：
\`\`\`bash
git log --all --oneline -- packages/backend/services/mes/
\`\`\`

## 快速开始

### 后端构建
\`\`\`bash
cd packages/backend
mvn clean package
\`\`\`

### 前端构建
\`\`\`bash
pnpm install
pnpm build
\`\`\`

详见各子项目 README.md
EOF
```

#### 步骤 5: 创建 Monorepo 根级设计和文档

```bash
# 5.1 创建 .spec 目录下的初始文档
touch .spec/design/README.md
touch .spec/steering/README.md
touch .spec/context/README.md
touch .spec/api/README.md
touch .spec/database/README.md
touch .spec/wiki/README.md

# 5.2 复制迁移计划文档
cp MONOREPO_INTEGRATION_PLAN.md .spec/

# 5.3 创建 docs 目录下的用户指南
touch docs/SETUP.md
touch docs/CONTRIBUTION.md
touch docs/TESTING.md
```

#### 步骤 6: 创建后端根 POM（Maven 聚合）

```bash
# 创建 packages/backend/pom.xml（聚合后端所有模块）
cat > packages/backend/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>
  <parent>
    <groupId>com.bmos</groupId>
    <artifactId>bmos-monorepo</artifactId>
    <version>${revision}</version>
    <relativePath>../../pom.xml</relativePath>
  </parent>
  
  <artifactId>bmos-monorepo-backend</artifactId>
  <packaging>pom</packaging>
  <name>BMOS Monorepo Backend Root</name>
  
  <properties>
    <revision>1.0-SNAPSHOT</revision>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>8</maven.compiler.source>
    <maven.compiler.target>8</maven.compiler.target>
    <bmos.internal.version>${revision}</bmos.internal.version>
    <spring-boot.version>2.6.15</spring-boot.version>
  </properties>
  
  <modules>
    <!-- 导入后的服务 -->
    <module>services/mes</module>
    <module>services/platform</module>
    <module>services/lims</module>
    <module>services/wms</module>
  
    <!-- 共享库 -->
    <module>shared/common</module>
    <module>shared/models</module>
  </modules>
  
  <dependencyManagement>
    <!-- 统一版本管理... -->
  </dependencyManagement>
</project>
EOF
```

#### 步骤 7: 调整各服务的 pom.xml

```bash
# 导入后，每个服务的 pom.xml 需要调整以指向新的 parent

# 例如 packages/backend/services/mes/pom.xml 应改为：
# <parent>
#   <groupId>com.bmos</groupId>
#   <artifactId>bmos-monorepo-backend</artifactId>
#   <version>${revision}</version>
#   <relativePath>../../../pom.xml</relativePath>
# </parent>

# 注意 relativePath 需要从 3 级改为 3 级（从 mes 到 backend 根 pom）
```

#### 步骤 8: 验证构建

```bash
# 8.1 从后端根目录验证 POM 结构
cd packages/backend
mvn validate

# 8.2 检查依赖树
mvn dependency:tree -pl services/mes

# 8.3 执行第一次完整构建
mvn clean install -DskipTests

# 8.4 验证生成的 JAR
ls -lh services/mes/target/bmos-mes-service-*.jar
```

#### 步骤 9: 初始化前端工作区

```bash
# 9.1 创建前端 pnpm-workspace.yaml
cat > packages/frontend/pnpm-workspace.yaml << 'EOF'
packages:
  - 'apps/*'
  - 'shared'
EOF

# 9.2 创建前端根 package.json
cat > packages/frontend/package.json << 'EOF'
{
  "name": "bmos-monorepo-frontend",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "build": "pnpm -r build",
    "test": "pnpm -r test",
    "lint": "pnpm -r lint",
    "dev": "pnpm -r --parallel dev"
  },
  "devDependencies": {
    "@types/node": "^18.0.0",
    "eslint": "^8.0.0",
    "prettier": "^2.8.0"
  }
}
EOF

# 9.3 安装前端依赖
cd packages/frontend
pnpm install
```

#### 步骤 10: 首次提交

```bash
# 回到 Monorepo 根目录
cd /path/to/bmos-monorepo-final

# 添加所有文件
git add .

# 首次提交
git commit -m "feat: initialize bmos monorepo with git subtree

Import all services and applications from their original repositories.

Backend services (from main branch):
- MES service (packages/backend/services/mes)
- Platform service (packages/backend/services/platform)
- LIMS service (packages/backend/services/lims)
- WMS service (packages/backend/services/wms)

Frontend applications (from master branch):
- App-Build-Template (packages/frontend/apps/app-build-template)
- LIMS-App-Build-Template (packages/frontend/apps/lims-app-build-template)
- MES-App (packages/frontend/apps/mes-app)
- LIMS-App (packages/frontend/apps/lims-app)
- Web (packages/frontend/apps/web)

Architecture:
- Git Subtree with --squash for complete isolation
- All original Git histories preserved
- Independent development: source repos and monorepo won't affect each other
- Maven aggregation for backend
- pnpm workspace for frontend
- Unified version management (CI Friendly Versions)

This is the primary development repository. Original repositories remain
as backups and will not be updated."

# 验证首次提交
git log --oneline | head -15
```

# 7.2 检查依赖树

mvn dependency:tree -pl services/mes

# 7.3 运行第一次完整构建

mvn clean install -DskipTests

# 如果成功输出：

# [INFO] BUILD SUCCESS

# [INFO] Total time: XX.XXs

# 则表示 Monorepo 结构初步成功

# 7.4 验证生成的 JAR

ls -lh services/mes/target/bmos-mes-service-*.jar
ls -lh shared/common/target/bmos-mes-common-*.jar

```

#### 步骤 8: 提取前端代码

```bash
# 假设前端仓库已在 /tmp/monorepo-sources

# 8.1 复制前端应用
cp -r /tmp/monorepo-sources/bmos-admin-ui \
      packages/frontend/apps/admin-ui
cp -r /tmp/monorepo-sources/bmos-mes-ui \
      packages/frontend/apps/mes-ui

# 8.2 创建前端根 package.json
cat > packages/frontend/package.json << 'EOF'
{
  "name": "bmos-monorepo-frontend",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "build": "pnpm -r build",
    "test": "pnpm -r test",
    "lint": "pnpm -r lint",
    "dev": "pnpm -r --parallel dev"
  },
  "devDependencies": {
    "@types/node": "^18.0.0",
    "eslint": "^8.0.0",
    "prettier": "^2.8.0"
  }
}
EOF

# 8.3 创建 pnpm-workspace.yaml
cat > packages/frontend/pnpm-workspace.yaml << 'EOF'
packages:
  - 'apps/*'
  - 'shared'
EOF

# 8.4 验证前端结构
ls -la packages/frontend/apps/
```

#### 步骤 9: 初始化根级 POM（可选）

```bash
# 如果需要从根目录统一构建所有项目
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.bmos</groupId>
  <artifactId>bmos-monorepo</artifactId>
  <version>${revision}</version>
  <packaging>pom</packaging>
  <name>BMOS Monorepo Root</name>

  <properties>
    <revision>1.0-SNAPSHOT</revision>
  </properties>

  <!-- 仅包含后端，前端单独管理 -->
  <modules>
    <module>packages/backend</module>
  </modules>

</project>
EOF
```

#### 步骤 10: 初始化 Git 和文档

```bash
# 10.1 创建 .gitignore
cat > .gitignore << 'EOF'
# Maven
target/
*.class
*.jar

# Node
node_modules/
dist/
pnpm-debug.log

# IDE
.idea/
.vscode/

# OS
.DS_Store
Thumbs.db

# Logs
*.log
EOF

# 10.2 创建初始 README
cat > README.md << 'EOF'
# BMOS Monorepo

统一的微服务和前端整合仓库。

## 项目结构

- `packages/backend/` - 后端微服务
- `packages/frontend/` - 前端应用
- `.spec/` - 设计和文档库

## 快速开始

### 后端
\`\`\`bash
cd packages/backend
mvn clean package
\`\`\`

### 前端
\`\`\`bash
cd packages/frontend
pnpm install
pnpm build
\`\`\`

详见各子项目 README.md
EOF

# 10.3 复制设计文档
cp MONOREPO_INTEGRATION_PLAN.md .spec/

# 10.4 提交初始化
git add .
git commit -m "feat: Initialize monorepo structure

- Extract backend services to packages/backend/services
- Setup shared libraries in packages/backend/shared
- Setup frontend workspaces in packages/frontend
- Add monorepo root pom.xml and package.json
- Add initial documentation"
```

### 10.3 初始化快速检查清单

完成上述步骤后，依次检查：

```bash
# ✅ 检查后端目录结构
tree -L 3 packages/backend/

# ✅ 检查前端目录结构
tree -L 3 packages/frontend/

# ✅ 检查所有 pom.xml 文件
find packages/backend -name "pom.xml" | wc -l

# ✅ 验证 POM 继承关系
cd packages/backend
mvn help:describe -Dplugin=org.apache.maven.plugins:maven-compiler-plugin

# ✅ 构建测试
mvn clean install -DskipTests
echo $?  # 输出 0 表示成功

# ✅ 查看依赖树
mvn dependency:tree -pl services/mes | head -30

# ✅ 检查 Git 状态
git status
git log --oneline | head -5

# ✅ 验证文档
ls -la .spec/
ls -la docs/
```

### 10.4 常见问题处理

#### 问题 1: 编译失败 - 找不到共享库

```
[ERROR] Failed to execute goal on project bmos-mes-service: 
Could not resolve dependencies for project com.bmos:bmos-mes-service

解决方案:
1. 确认 shared/common 已编译并发布到 .m2
   mvn clean install -pl shared/common

2. 或者用 -am 选项：
   mvn clean install -pl services/mes -am

3. 检查 services/mes/pom.xml 的 dependency 是否正确
```

#### 问题 2: 版本不一致导致冲突

```
[WARNING] Overriding managed version 1.4.1 for org.springframework.boot:spring-boot-starter-web

解决方案:
1. 检查 packages/backend/pom.xml 的 dependencyManagement
2. 移除子模块中重复定义的版本号
3. 所有版本都应该从根 pom 的 <dependencyManagement> 继承
```

#### 问题 3: 构建顺序问题

```
[ERROR] The project cannot be built before its dependency org.bmos:bmos-mes-common

解决方案:
1. 检查 packages/backend/pom.xml 的 <modules> 顺序
2. 共享库必须在服务之前定义
3. 依赖的模块必须在依赖者之前
```

### 10.5 初始化完成标志

当以下条件都满足时，初始化完成：

- ✅ 所有后端服务目录位于 `packages/backend/services/`
- ✅ 所有共享库位于 `packages/backend/shared/`
- ✅ 所有服务的 pom.xml 已更新，parent 指向 `packages/backend/pom.xml`
- ✅ `packages/backend/pom.xml` 中的 `<modules>` 列出所有子模块
- ✅ 从 `packages/backend` 目录运行 `mvn clean install -DskipTests` 成功
- ✅ 所有前端应用目录位于 `packages/frontend/apps/`
- ✅ 前端根目录配置了 `package.json` 和 `pnpm-workspace.yaml`
- ✅ Git 已提交初始化变更，提交信息清晰
- ✅ README 和基础文档已创建
- ✅ 团队成员已获知新的代码库位置和工作流程

### 10.6 初始化后的后续工作

初始化完成后，还需要进行的工作：

| 任务                                            | 优先级 | 时间   |
| ----------------------------------------------- | ------ | ------ |
| 更新各服务的 bootstrap.yml 和配置               | 高     | 1 天   |
| 提取和整理共享库中的重复代码                    | 高     | 2-3 天 |
| 编写设计文档（architecture.md 等）              | 中     | 3-5 天 |
| 编写 Context 文档（entity-relationships.md 等） | 中     | 3-5 天 |
| 编写快速参考文档（wiki/）                       | 中     | 2-3 天 |
| 配置 CI/CD 流程（GitHub Actions / GitLab CI）   | 高     | 2 天   |
| 测试本地开发环境启动脚本                        | 中     | 1 天   |
| 团队培训和反馈收集                              | 低     | 1 天   |

---

## 十一、相关资源和参考

### 推荐工具和框架

1. **后端**

   - Maven 聚合构建
   - Spring Boot Actuator（服务健康检查）
   - Swagger/OpenAPI（API 文档）
2. **前端**

   - pnpm（工作区管理）
   - Monorepo 工具链：Turbo, Nx（可选）
   - Storybook（组件文档）
3. **文档**

   - MkDocs（文档生成）
   - PlantUML（架构图）
   - Mermaid（流程图）
4. **CI/CD**

   - GitHub Actions
   - GitLab CI
   - Jenkins（可选）

### 参考链接

- [Apache Maven Aggregator Projects](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
- [pnpm Workspaces](https://pnpm.io/workspaces)
- [Monorepo Best Practices](https://www.atlassian.com/git/articles/monorepos)

---

## 十二、附录

### A. 示例：如何在 Monorepo 中添加新的后端微服务

```bash
# 1. 在 packages/backend/services 下创建新服务
mkdir -p packages/backend/services/my-service/src/{main,test}/{java,resources}

# 2. 创建 pom.xml（参考现有服务）
cat > packages/backend/services/my-service/pom.xml << 'EOF'
<project>
  <parent>
    <groupId>com.bmos</groupId>
    <artifactId>bmos-monorepo</artifactId>
    <version>${revision}</version>
    <relativePath>../../../../pom.xml</relativePath>
  </parent>
  
  <artifactId>bmos-my-service</artifactId>
  <name>My Service</name>
  
  <dependencies>
    <!-- 使用共享库和根 pom 定义的版本 -->
    <dependency>
      <groupId>com.bmos.mes</groupId>
      <artifactId>bmos-mes-common</artifactId>
    </dependency>
  </dependencies>
</project>
EOF

# 3. 在根 pom.xml 中添加 <module>
# <module>packages/backend/services/my-service</module>

# 4. 构建验证
cd /path/to/monorepo
mvn clean package -pl packages/backend/services/my-service
```

### B. 示例：如何在 Monorepo 中添加新的前端应用

```bash
# 1. 在 packages/frontend/apps 下创建新应用
mkdir -p packages/frontend/apps/my-app
cd packages/frontend/apps/my-app

# 2. 创建 package.json
cat > package.json << 'EOF'
{
  "name": "@bmos/my-app",
  "version": "1.0.0",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "@bmos/shared-components": "workspace:*",
    "react": "^18.0.0"
  }
}
EOF

# 3. 回到前端根目录，安装依赖
cd packages/frontend
pnpm install

# 4. 启动应用
pnpm --filter @bmos/my-app dev
```

### C. 文档模板

```markdown
# [功能/模块名称]

## 概述
简要说明这个功能/模块的目的。

## 相关文档
- 设计文档：[链接]
- 相关实体：[链接]
- 业务规则：[链接]

## 核心概念
解释关键概念。

## 使用示例
```code
示例代码
```

```

---

## 最后的话

这个方案是一份**动态文档**，应根据实际情况灵活调整。建议：

1. **立即行动**：开始第一阶段的基础设施建设
2. **定期评审**：每个阶段结束后评审进度和问题
3. **充分沟通**：与团队保持透明的沟通，收集反馈
4. **持续改进**：根据实践不断优化流程和文档

祝顺利！🚀
```
