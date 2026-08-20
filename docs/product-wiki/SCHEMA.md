---
title: Product Wiki Schema
created: 2026-07-27
updated: 2026-07-27
---

# Product Wiki Schema

> 本文件定义 bmos 产品设计知识库（product-wiki）的**约定、frontmatter 模板、标签体系和页面分工规则**。
> 方法论：参考 `skills/llm-wiki/SKILL.md`（Karpathy LLM Wiki 模式）。
> 配套：[[product-overview]]（产品族总览）· [[menu-structure]]（菜单树与编码）。
>
> 关系：与 `docs/code-wiki/`（**代码视角**，sources 指向 `.java/.vue`）互补——本库是**产品视角**，sources 指向 PRD。
> 新建或修改页面前**必须先读本文件**。

## 领域

bmos 制药产品族的产品设计知识库——以 **MES 制造执行（120/121）** 为核心，延伸至与 MES 执行强相关的主数据、仓储交互（WMS）、检验（LIMS）。

目的：让产品、开发、实施、新接手 AI 在理解"产品要做什么、业务流程怎么走、功能怎么设计、合规约束是什么"时，能快速定位到具体功能设计与 PRD 原文，而不必通读 130+ 份散落的 PRD。

数据源：`packages/prd/` 下的 PRD markdown（Layer 1，**只读，永不修改**）。

## raw/ 目录（PRD 不可变副本，Layer 1）

`raw/prd/` 是 `packages/prd/` 中**被引用 PRD 的副本**，纳入 vault 以便在 Obsidian 等 markdown 工具里直接点开 PRD 原文——`packages/prd/` 在 vault（`docs/product-wiki/`）之外，Obsidian 无法跨 vault 跳转，故把被引用的 PRD 复制进 vault。

- **不可变**：与 `packages/prd/` 同为 Layer 1 源，只读；修正与综合在 wiki 页内进行，**不改 raw/ 文件**。
- **NFC 编码**：文件名统一 Unicode NFC，与 wiki 链接一致。⚠️ macOS 文件系统可能把中文文件名存为 NFD，导致链接失效——必须用同步脚本生成（脚本强制 NFC）。
- **同步**（两个脚本配套）：
  - 编辑某页 frontmatter `sources`（增删 PRD 引用）后，先重生成链接段，再同步副本：
    ```bash
    python3 docs/product-wiki/scripts/regen_links.py   # sources → 正文「📎 PRD 原文」链接段
    python3 docs/product-wiki/scripts/sync_raw.py       # 链接段 → raw/prd/ NFC 副本
    ```
  - 仅 `packages/prd/` 内 PRD 内容更新（不改 sources）时，只需重跑 `sync_raw.py`。
  - sync_raw 逻辑：扫描所有 wiki 页「📎 PRD 原文」链接 → 从 packages/prd 复制到 raw/prd → 强制 NFC 文件名 → 清理不再被引用的旧文件。
- **范围**：只复制被 wiki sources 引用的 PRD（约 94 个，均有实质内容）；0 字节空文件与未引用占位不复制。

## 约定

- 文件名：小写、连字符、无空格（如 `product-overview.md`、`feature-weigh-center.md`）；按类型归入子目录 `concepts/` `master-data/` `domains/` `features/` `flows/`
- 所有 wiki 页面以 YAML frontmatter 开头（见下方模板）
- 页面之间使用 `[[wikilinks]]` 互相链接：**每页至少 2 个出站链接；枢纽页（★）至少 3 个**
- `sources:` 必填**真实 PRD 路径**（`packages/prd/XXX.md`）——这是跳转原文的依据
- **每页正文末尾加「## 📎 PRD 原文」段**：把 frontmatter sources 转为可点击的 markdown 相对链接——frontmatter 供机器读、正文链接供人点击跳转 PRD 原文。链接指向 `raw/prd/`（vault 内副本，见下方「raw/ 目录」）；相对路径：子目录页 `../raw/prd/`、顶层页 `raw/prd/`；含空格的文件名（如 `002 参数配置.md`）用 `<URL>` 尖括号包裹以保证链接解析正确
- 更新页面时必须更新 `updated` 日期
- 每个新页面必须添加到 `index.md` 对应分区下
- 每次操作（创建/更新/批量导入）必须追加到 `log.md`
- 所有内容使用**中文**撰写（标识符、字段名、编码用英文）
- **绝不修改 `packages/prd/` 任何文件**——PRD 是不可变源，修正与综合在 wiki 页内进行

## Frontmatter 模板

```yaml
---
title: 页面标题
created: YYYY-MM-DD
updated: YYYY-MM-DD
type: domain | feature | flow | master-data | concept
system: mes | mes-app | wms | lims | platform | audit | cross
domain: 生产配置 | 生产管理 | 批签发 | 称量 | 仓储交互 | 检验 | 系统基础 | 主数据 | 跨域 | 移动执行
menu_code: 120-030-001          # 来自 菜单功能.md 的 5 级编码；无菜单归属则省略
iter_status: 规划中 | 待测试 | 已发布 | 未规划   # 产品迭代状态（来自菜单功能.md 的 🔴🟡🔵🟢）
prd_filled: filled | placeholder | empty        # PRD 源文件实质内容
tags: [从标签体系选，≤3 个]
sources:
  - packages/prd/XXX.md
related_code_wiki: [mes-audit-module]   # 可选，指向 docs/code-wiki 的对偶页 wikilink
status: active                  # active | deprecated
---
```

### 字段说明（重要）

- **`iter_status` 与 `prd_filled` 是两个正交维度，不可合并**：
  - `iter_status`：该功能在**产品路线图**上的状态（菜单功能.md 的图例 🔴未规划/🟡规划中/🔵待测试/🟢已发布）。
  - `prd_filled`：该功能的 **PRD 文件是否有实质内容**——`filled`=有完整需求；`placeholder`=仅占位/链接/图片；`empty`=0 字节空文件。
  - 两者常不一致：例如 `007-标签管理` 菜单标 🔴规划中，但 PRD 有 19KB 实质内容（`iter_status: 规划中, prd_filled: filled`）；又如 `120-制造执行系统` PRD 为空（`iter_status: 已发布, prd_filled: empty`）。
- **`menu_code`**：5 级编码 = 系统编号(3) + 父菜单(3) + 子菜单(3) + 权限分类(3) + 权限(3)。跨菜单的功能（如称量）取主要归属或省略。
- **`related_code_wiki`**：指向 `docs/code-wiki/` 的对偶页（如审计→`mes-audit-module`、审批流→`mes-workflow-module`），结构化交叉引用，不要散在正文。
- **`system`** 多选时取主导系统；跨域页用 `cross`。

## 标签体系（≤25，先注册再用）

新增标签前必须先添加到此处，禁止随意创建。**只允许"已建页实体名"或下表分类做标签**，删除无对应页的实体标签防膨胀。

| 分类 | 标签 |
|---|---|
| 业务域 | `生产配置` · `生产管理` · `批签发` · `称量` · `仓储` · `检验` · `系统基础` |
| 主数据 | `物料` · `设备` · `货位` · `配方-BOM` · `记录-数据集` |
| 合规 | `审计追踪` · `电子签名` · `审批流` · `版本发布` · `数据权限` |
| 规范 | `交互规范` · `编号规则` |
| 端 | `配置端` · `使用端` · `移动端` |
| 流程 | `批生产` · `物料流转` · `合规追溯` |

## 页面分工规则（消除重叠，严格执行）

同一主题**只允许在一类页面展开**，他处用 `[[wikilink]]` 引用。这是本库防止内容重复与矛盾的核心约束。

| 页面类型 | 只写什么 | 不写什么 |
|---|---|---|
| **domain**（业务域） | 菜单功能目录 + 每功能一行职责 + iter_status/prd_filled + 域内核心设计要点 | 不展开实体关系模型；不写运行时序贯步骤 |
| **feature**（核心功能） | 实体关系模型 + 语义 + 状态机 + 关键配置 | 不列菜单目录；不写端到端时序 |
| **flow**（业务流程） | 运行时端到端序贯步骤（时序+输入输出+状态流转） | 不重复实体字段定义 |
| **master-data**（主数据） | 实体字段 + 版本机制 + 主数据边界（platform/mes） | 不写业务操作流程 |
| **concept**（概念规范） | 横切关注点的机制与规则（被全站引用） | 不依赖单一域；不展开单一功能细节 |

**典型重叠的处理**：
- 工艺配置：`domain-production-config` 列一行 → `feature-process-and-execution-model` 展开四者关系模型
- 暂存间：`domain-production-config`(配置) 与 `domain-production-management`(管理) 各一行 → `location-master` 展开实体
- 物料：`domain-warehouse-interaction`/`domain-production-management` 各一行 → `material-master` 展开实体+量值模型
- 日志：`domain-production-management`(050 查询入口) 一行 → `flow-compliance-traceability` 展开四大日志体系

## 页面阈值

- **创建页面**：当一个业务域/主数据实体/核心功能/端到端流程承担独立产品语义
- **添加到已有页面**：当信息是某已有实体的补充细节
- **不创建页面**：单个按钮交互、工具组件、临时逻辑、与产品领域无关内容
- **拆分页面**：超过 200 行时按子主题拆分并用 wikilink 互联；feature 页因含完整状态机/配置可至 250 行，超 300 必拆
- **空 PRD 处理**：`prd_filled: empty/placeholder` 的功能，在 domain 页内一行标注「PRD 未填充」，**不臆造内容**；不为其单独建 feature 页

## 各类页面内容规范

### domain 页（业务域总览）
1. 域概述（覆盖的菜单编号区间 + 域职责）
2. 功能清单表（菜单编码 | 功能名 | 一句话职责 | iter_status | prd_filled | PRD 文件）
3. 域内核心设计要点（3-6 条，不展开成 feature）
4. 跨域关联（[[wikilink]] 到相关 feature/master-data/flow/concept）
5. sources（该域全部 PRD 文件）

### feature 页（核心功能深化）
1. 概述与核心实体
2. 实体关系模型（图或结构化描述）
3. 关键状态机
4. 配置要点与规则
5. 与其他实体/域的关系（[[wikilink]]）
6. sources

### flow 页（端到端流程）
1. 流程概述（触发条件、输入、输出）
2. 序贯步骤（编号 + 每步动作 + 状态流转 + 涉及实体/功能）
3. 关键分支与异常
4. 串联的 feature/domain/master-data（[[wikilink]]）
5. sources

### master-data 页（主数据实体）
1. 实体概述与归属系统（**主数据边界**：platform 真相源 vs MES/WMS 只读引用）
2. 分类层级与编码规则
3. 关键字段表
4. 版本/启停/同步/下发机制
5. 量值模型（物料/货品类适用）
6. 与其他主数据/业务的关系（[[wikilink]]）
7. sources

### concept 页（横切概念/规范）
1. 定义与适用范围
2. 机制与规则
3. 关键参数/触发点/状态
4. 被哪些域/功能引用（[[wikilink]]）
5. sources

## 更新策略

当 PRD 变更导致 wiki 内容过时时：
1. 对照 PRD 确认变更范围
2. 更新页面内容，更新 `updated` 日期
3. 破坏性变更在 frontmatter 标 `breaking: true`
4. 在 `log.md` 追加 ingest/update 记录，写明「哪些 PRD → 更新了哪些页」
5. 若 `iter_status`/`prd_filled` 变化，同步更新双状态字段

## Lint 检查项（收口阶段执行）

1. 孤立页：无入站 `[[wikilink]]` 的页面（枢纽页除外）
2. 断链：`[[link]]` 指向不存在的页面
3. index 完整性：每页都在 `index.md`
4. frontmatter：title/created/updated/type/system/domain/iter_status/prd_filled/tags/sources 齐全；tags 在 taxonomy 内
5. 双状态字段：iter_status 与 prd_filled 均填写且语义正确
6. 出站链接：每页 ≥2（枢纽 ≥3）
7. 空 PRD 标注：empty/placeholder 功能均已标注，未臆造内容
8. 页面体量：超 200 行（feature 250）标候选拆分
