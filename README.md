# BMOS Monorepo

BMOS 是面向制造、实验室和仓储业务的企业级业务平台。本仓库将平台基础服务、MES、LIMS、WMS、API 网关、Web 管理端、UniApp 移动端、共享组件和数据库脚本整合到一个 Monorepo 中，便于统一检索、协作和跨服务开发。

> 当前仓库处于 Monorepo 整合与持续演进阶段。后端服务仍然保持独立 `pom.xml`、独立版本和独立部署；仓库根目录暂未提供统一的 Maven 聚合入口或一键启动脚本。

## 目录

- [项目说明](#项目说明)
- [架构说明](#架构说明)
- [仓库结构](#仓库结构)
- [运行环境](#运行环境)
- [启动说明](#启动说明)
- [构建与测试](#构建与测试)
- [常见问题](#常见问题)
- [文档与开发约定](#文档与开发约定)

## 项目说明

### 主要业务域

- **Platform**：认证、用户、角色、菜单、组织、设备、物料、参数和 license 等平台基础能力。
- **MES**：生产计划、批记录、工艺/配方/BOM、称量、配料投料、生产执行、批签发和审计追溯。
- **LIMS**：检验项目、检验单、实验室数据和 ELN 电子实验记录。
- **WMS**：仓储配置、库存、货位、发料、领料及仓储检验。
- **Gateway**：统一 API 入口、路由转发和鉴权。
- **Web 管理端**：按业务域拆分的 Vue 管理应用，共 12 个 Web app。
- **移动端**：基于 UniApp 的 MES/LIMS 移动工程，支持 H5、App 和小程序等目标。

### 技术栈

| 层次 | 技术 |
| --- | --- |
| 后端 | Java 8、Spring Boot 2.6.15、Spring Cloud 2021、Spring Cloud Alibaba、Maven |
| 数据访问 | MyBatis-Plus、ShardingSphere、MariaDB/MySQL |
| 服务通信 | Nacos 服务发现与配置、OpenFeign、Spring Cloud Gateway |
| 基础设施 | Redis、RocketMQ、MinIO、XXL-Job（按服务使用） |
| Web | Vue 3.5、Vite 5、TypeScript、Ant Design Vue 4、Pinia、Vue Router |
| 移动端 | UniApp、Vue 3、Vite，配合 Android/Electron 构建模板 |

## 架构说明

### 整体架构

```text
                         ┌──────────────────────┐
                         │  Web / 移动端客户端    │
                         └──────────┬───────────┘
                                    │ HTTP / WebSocket
                                    ▼
                         ┌──────────────────────┐
                         │ Gateway :60300       │
                         │ 统一入口 / 路由 / 鉴权 │
                         └──────────┬───────────┘
                                    │ Nacos 服务发现
             ┌──────────────────────┼──────────────────────┐
             ▼                      ▼                      ▼
      Platform :60100       MES :60200              LIMS :61001
      平台基础能力            制造执行                 实验室信息
             │                      │                      │
             └──────────────┬───────┴──────────────┬───────┘
                            ▼                      ▼
                     WMS :60900              外部基础设施
                     仓库管理          Nacos / DB / Redis / MQ / MinIO
```

### 后端服务

后端服务通过 Feign 进行同步调用，通过 Nacos 完成服务发现和配置读取，服务之间禁止直接访问其它服务的数据库。

| 服务 | 目录 | 端口 | Nacos 服务名 | Context Path | 职责 |
| --- | --- | ---: | --- | --- | --- |
| Platform | `packages/backend/services/platform` | 60100 | `bmos-platform-service` | `/api/app/platform` | 平台基础能力 |
| MES | `packages/backend/services/mes` | 60200 | `bmos-mes-service` | `/api/app/mes` | 制造执行 |
| Gateway | `packages/backend/services/gateway` | 60300 | `bmos-gateway-service` | 网关入口 | 路由、鉴权 |
| WMS | `packages/backend/services/wms` | 60900 | `bmos-wms-service` | `/api/app/wms` | 仓库管理 |
| LIMS | `packages/backend/services/lims` | 61001 | `bmos-lims2-service` | `/api/app/lims2` | 实验室信息管理 |

> LIMS 的 Maven artifact、Java 包、Nacos 服务名和 context path 中仍保留 `lims2` 命名，这是现有兼容约束。

后端服务的依赖版本当前并未完全统一：Platform/MES 使用 `bmos.version=1.15.0-SNAPSHOT`，LIMS 使用 `1.15.2-SNAPSHOT`，Gateway/WMS 使用 `1.14.0-SNAPSHOT`。因此构建时应以对应服务目录下的 `pom.xml` 为准。

### Web 前端

Web 工程位于 `packages/frontend/apps/web/`，是独立的 pnpm workspace：

```text
packages/frontend/apps/web/
├── apps/       # 12 个 Web 应用
├── packages/   # @bmos/* 共享库
├── docs/       # 前端工程文档
├── openApi/    # OpenAPI 相关文件
├── package.json
└── pnpm-workspace.yaml
```

Web app 统一使用 Vue + Vite，公共能力通过 `@bmos/axios`、`@bmos/components`、`@bmos/i18n`、`@bmos/icons`、`@bmos/messager`、`@bmos/utils` 等 workspace 包复用。应用启动时通常依次完成 SSO 鉴权、菜单/权限加载、国际化初始化和页面挂载。

当前 Web app 清单：

`bmos-platform-web`、`bmos-mes-web`、`bmos-lims-web`、`bmos-wms-web`、`bmos-audit-web`、`bmos-bims-web`、`bmos-bsms-web`、`bmos-lisms-web`、`bmos-ems-web`、`bmos-dc-web`、`bmos-el-web`、`demo`。

大多数应用通过 `VITE_API_HOST` 代理到 Gateway；其中部分应用对应的后端服务不在当前 Monorepo 中（例如 EMS、DC 等），联调前需确认外部服务是否可用。

### 移动端

`packages/frontend/apps/mes-app` 和 `packages/frontend/apps/lims-app` 是独立的 UniApp 工程，不属于 Web workspace，也不复用 `@bmos/*` 共享库。两者可以构建 H5、Android、iOS 和多个小程序目标，并配套 `app-build-template`、`lims-app-build-template` 进行 Android/Electron 封装。

## 仓库结构

```text
bmos-monorepo/
├── packages/
│   ├── backend/
│   │   ├── services/
│   │   │   ├── gateway/
│   │   │   ├── lims/
│   │   │   ├── mes/
│   │   │   ├── platform/
│   │   │   └── wms/
│   │   └── shared/bmos-parent-starter/
│   ├── frontend/apps/
│   │   ├── web/
│   │   ├── mes-app/
│   │   ├── lims-app/
│   │   └── *-build-template/
│   └── script/                 # 数据库、参数、菜单和版本脚本
├── docs/
│   ├── code-wiki/              # 代码架构、服务和模块知识库
│   ├── product-wiki/           # 产品、业务域和流程知识库
│   └── MONOREPO_INTEGRATION_PLAN.md
├── CLAUDE.md                   # AI 开发导航与硬性约定
└── README.md
```

## 运行环境

### 必需工具

- Git
- JDK 8
- Maven（后端各服务独立构建）
- Node.js `>=20.15`（Web workspace 的 `package.json` 要求）
- pnpm `8.5.0`

移动端开发还需要根据目标平台安装 HBuilderX/UniApp 相关工具、Android SDK 或 Electron 构建环境。

### 外部依赖

后端业务配置主要从 Nacos 读取，至少需要准备：

- Nacos：服务发现、配置中心和国际化配置；默认端口 `8848`，默认账号配置中为 `nacos/nacos`。
- MariaDB/MySQL：各服务业务数据库及初始化脚本。
- Redis：缓存、会话及部分分布式能力。
- RocketMQ：MES、Platform 等服务使用消息能力，默认 NameServer 端口 `9876`。
- MinIO：文件、附件和对象存储能力。
- XXL-Job：部分服务从 Nacos 读取 XXL-Job 配置。

不同环境的数据库、Redis、MinIO、路由和业务参数通常保存在 Nacos 配置中，仓库内的 `bootstrap.yml` 只负责指定 Nacos 地址及配置 data id。请向环境维护人员获取对应的 Nacos 配置和数据库脚本，不要直接把生产配置提交到仓库。

## 启动说明

### 1. 获取代码并准备配置

```bash
git clone <仓库地址> bmos-monorepo
cd bmos-monorepo
```

先启动 Nacos、数据库、Redis、RocketMQ、MinIO 等依赖，并准备对应的 Nacos 配置。为避免不同服务内置的默认地址不一致，建议显式设置：

```bash
export NACOS_HOST=127.0.0.1
export NACOS_PORT=8848
export NACOS_ENABLE_SERVICE=true
export ROCKETMQ_HOST=127.0.0.1
```

如果基础设施不在本机，请将上述地址替换为实际环境地址。服务默认使用 `prod` profile，配置名通常形如 `bmos-<service>-service-prod.yaml`，并会读取 `application.yaml`、`bmos-redis.yaml`、`bmos-xxl-job.yaml` 等共享配置。

### 2. 启动后端服务

每个服务都应在自己的聚合 POM 目录执行 Maven 命令。以 MES 为例：

```bash
cd packages/backend/services/mes
mvn clean package -DskipTests
java -jar bmos-mes-service/target/bmos-mes-service.jar
```

其它服务的构建和启动方式如下：

```bash
# Platform
cd packages/backend/services/platform
mvn clean package -DskipTests
java -jar bmos-platform-service/target/bmos-platform-service.jar

# LIMS
cd packages/backend/services/lims
mvn clean package -DskipTests
java -jar bmos-lims2-web/target/bmos-lims2-web.jar

# WMS
cd packages/backend/services/wms
mvn clean package -DskipTests
java -jar bmos-wms-service/target/bmos-wms-service.jar

# Gateway
cd packages/backend/services/gateway
mvn clean package -DskipTests
java -jar target/bmos-gateway-service.jar
```

本地缺少 `bmos-*` SNAPSHOT 依赖时，先构建并安装共享 starter：

```bash
cd packages/backend/shared/bmos-parent-starter
mvn clean install -DskipTests
```

部分服务还依赖其它服务发布的 `facade`/`feign` 制品；如果本地 Maven 仓库没有对应版本，应使用公司内部 Maven 仓库，或按依赖顺序先在对应服务中执行 `mvn install`。

建议启动顺序为：`Nacos/基础设施 → Platform → MES/LIMS/WMS → Gateway`。Gateway 启动后，前端统一通过 `http://127.0.0.1:60300` 访问后端。

### 3. 启动 Web 前端

```bash
cd packages/frontend/apps/web
corepack enable
corepack prepare pnpm@8.5.0 --activate
pnpm install
```

每个 app 的 Vite 开发服务器默认使用 `8083`，因此本地通常一次启动一个 app。单独启动 MES 或 Platform：

```bash
pnpm dev:mes
pnpm dev:plat
```

也可以使用 workspace filter 启动任意应用：

```bash
pnpm --filter bmos-wms-web dev
pnpm --filter bmos-lims-web dev
```

确认目标 app 的 `.env.development.local` 中配置了 Gateway 地址，例如：

```dotenv
VITE_API_HOST=http://127.0.0.1:60300
```

应用访问路径由各自 Vite `base` 配置决定，例如 MES 通常为 `/app/bmos-mes`，Platform 通常为 `/app/bmos-platform/`。如果需要同时调试多个 app，请通过 Vite CLI `--port` 覆盖端口，并同步检查 Platform 门户中的本地代理配置。

### 4. 启动移动端

```bash
cd packages/frontend/apps/mes-app
pnpm install

# H5 开发
pnpm dev:h5

# Android 开发
pnpm dev:app-android
```

`lims-app` 使用相同的命令结构。H5、App、小程序的完整脚本可查看各工程的 `package.json`；原生安装包还需要对应的签名、SDK 和构建模板配置。

## 构建与测试

### 后端

```bash
# 构建单个服务
cd packages/backend/services/mes
mvn clean package

# 跳过测试以加快本地打包
mvn clean package -DskipTests

# 执行测试
mvn test
```

仓库根目录没有统一后端 Maven 聚合 POM，不能从根目录直接假设 `mvn clean package` 会构建全部后端服务。

### Web

```bash
cd packages/frontend/apps/web

# 构建所有 workspace app
pnpm build

# 构建指定 app
pnpm build:mes
pnpm build:platform
pnpm build:wms
pnpm --filter bmos-bims-web build

# 类型检查/代码检查（以具体 app 的 scripts 为准）
pnpm --filter bmos-mes-web type-check
pnpm --filter bmos-mes-web lint
```

部分 app 没有独立的 `type-check` 或 `lint` 脚本，执行前请先查看对应 `package.json`。

### 数据库脚本

数据库脚本集中在 `packages/script/`，按 `MES`、`BSMS`、`CLIMS`、`AGENT` 和开发脚本/标准版本/项目版本分类。脚本可能包含菜单权限、参数、审批流程、字典和数据初始化内容，执行前应确认目标版本、数据库和回滚方案。

## 常见问题

### 服务启动后连不上 Nacos

检查 `NACOS_HOST`、`NACOS_PORT`、Nacos 账号以及服务所需的 data id/group。Platform、MES、LIMS 的配置文件历史上存在内网地址默认值，WMS、Gateway 默认值为 `127.0.0.1`，本地运行时建议显式设置 `NACOS_HOST`。

### 前端请求 404 或跨域

检查：

1. Gateway 是否运行在 `60300`。
2. app 的 `.env.development.local` 是否配置了正确的 `VITE_API_HOST`。
3. 请求路径是否使用正确的 context path：`platform`、`mes`、`wms`、`lims2`。
4. Nacos 中下游服务是否已注册。

### Maven 找不到内部 SNAPSHOT 制品

确认 Maven `settings.xml` 已配置公司内部仓库和凭据；或者先安装 `packages/backend/shared/bmos-parent-starter` 及对应服务的本地模块。不同服务的 `bmos.version` 存在差异，不建议未经确认直接统一版本。

### 多个 Web app 无法同时启动

各 app 的 Vite 默认端口都是 `8083`。单 app 调试可直接使用默认端口；多 app 并行时为每个进程传入不同的 `--port`，并检查 `bmos-platform-web/vite.config.ts` 中指向其它本地 app 的代理端口。

## 文档与开发约定

- [代码知识库目录](docs/code-wiki/index.md)：服务、模块、接口、架构和开发约定。
- [产品知识库目录](docs/product-wiki/index.md)：产品概览、业务域、功能和端到端流程。
- [Monorepo 整合方案](docs/MONOREPO_INTEGRATION_PLAN.md)：仓库整合背景、演进计划和已知决策。
- [AI 开发导航](CLAUDE.md)：代码定位、服务边界和修改约束。

开发时请遵守以下原则：

- 后端按服务边界修改，跨服务调用使用 Feign，不直接访问其它服务数据库。
- Web 应用依赖共享库使用 workspace 包，保持 `@bmos/*` 的分层边界。
- 新增或修改服务配置时同步确认 Nacos data id、group 和部署环境。
- 提交信息使用 Conventional Commits，例如 `feat:`、`fix:`、`docs:`。
- 代码标识符使用英文，注释和文档使用中文。
