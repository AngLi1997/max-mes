# Product Wiki Log

> 所有 wiki 操作的按时间顺序记录。仅追加。
> 格式：`## [YYYY-MM-DD] action | subject`
> Actions: create | ingest | update | lint | archive | delete
> 超过 500 条时轮转：重命名为 `log-YYYY.md`，重新开始。

## [2026-07-27] create | Product Wiki 初始化

- 领域：bmos 制药产品族产品设计知识库（MES 为核心 + 强相关跨域）
- 方法论：参考 `skills/llm-wiki/SKILL.md`（Karpathy LLM Wiki）+ 对齐 `docs/code-wiki/`
- 位置：`docs/product-wiki/`
- 规划依据：3 个 Explore agent 深读全部核心 PRD + 1 个 Plan agent 审查（采纳全部 blocking 项）

**阶段 A：治理与枢纽入口**
- `SCHEMA.md` — 约定/frontmatter/标签体系(≤25)/页面分工规则/lint 项
- `index.md` — 31 页完整目录（分区 + 摘要 + 状态）
- `log.md`（本文件）
- `concepts/product-overview.md` — 产品族总览 + 多系统编号 + 主数据边界 + 两端模型
- `concepts/menu-structure.md` — 菜单树 + 5 级编码 + 状态图例 + PRD 文件名歧义说明

## [2026-07-27] ingest | 阶段 B：8 agent 并行填充 26 页

> 8 个 agent 各注入 SCHEMA 摘要 + 页面分工规则 + 全局 wikilink slug 清单 + 域 PRD 清单。
> 全部页面 frontmatter 双状态字段（iter_status/prd_filled）正交填写，中文，每页 ≥2 出站（枢纽 ≥3）。

**Agent-1 配置域（4 页）**
- `domains/domain-production-config.md`（77 行）— 020 生产配置域 10 功能清单 + 6 条核心设计
- `master-data/recipe-bom-master.md`（102 行）— 配方+BOM 量值模型 + 五类允差 + 版本状态机
- `master-data/record-dataset-master.md`（140 行）— 电子记录(Word→组件) + 数据集(数据点-组件绑定)
- `features/feature-process-and-execution-model.md`（173 行）— 工艺/配方/BOM/记录四者关系 + 工作流 + 移动端执行段

**Agent-2 生产管理+批生产流（2 页）**
- `domains/domain-production-management.md`（141 行）— 030 生产管理 + 050 生产查询 + 060 异常
- `flows/flow-batch-production.md`（172 行）— 11 步端到端时序 + 11 状态 + 7 异常分支

**Agent-3 批签发域（2 页）**
- `domains/domain-lot-release.md`（66 行）— 040 批签发域功能清单 + 6 条核心设计
- `features/feature-lot-release-mgmt.md`（193 行）— 数据集→模板→生成→审核→文件 + 4 状态机

**Agent-4 称量域（3 页）**
- `domains/domain-weighing.md`（92 行）— 称量域 11 功能 + 四模式分工
- `features/feature-weigh-center.md`（182 行）— 四种称量模式 + 4 状态机 + 秤具模型
- `flows/flow-weighing-execution.md`（155 行）— 7 步执行时序 + 余料/签名合规点

**Agent-5 仓储+检验+物料流（5 页）**
- `master-data/material-master.md`（118 行）— 物料 platform 真相源 + 下发/同步 + 量值模型 + 五态质量
- `master-data/location-master.md`（107 行）— WMS 货位 vs MES 暂存货位 + 货品件↔物料件转换
- `domains/domain-warehouse-interaction.md`（96 行）— 仓储 13 功能 + WMS/MES 边界 + 三动作衔接
- `domains/domain-inspection.md`（78 行）— 请验单配置 + 检验结果组件 + LIMS 边界
- `flows/flow-material-flow.md`（143 行）— 9 步物料流转主链 + 物料平衡公式 + 追溯

**Agent-6a 设备+系统基础（2 页）**
- `master-data/equipment-master.md`（133 行）— 设备类/功能点模板/设备实例/状态图/数采映射五对象
- `domains/domain-system-base.md`（120 行）— 系统基础 11 功能 + 登录/消息/权限六维/标签

**Agent-6b 合规日志+签名（3 页）**
- `concepts/audit-trail-and-logs.md`（92 行）— 按钮清单埋点 + 四大日志 + 111 审计中台
- `concepts/esignature.md`（108 行）— 13 条签名触发点表 + 签名对象拼接规则
- `flows/flow-compliance-traceability.md`（115 行）— 6 步合规追溯时序

**Agent-6c 横切规范概念（5 页）**
- `concepts/version-publish-mechanism.md`（126 行）— 版本发布统一范式 + 状态机分层 + 9 类对象
- `concepts/approval-flow.md`（137 行）— 审批流 SDK + 6 位编码 + MES 内置流程类型
- `concepts/data-permission.md`（112 行）— 部门数据权限 + 操作/数据权限双轨
- `concepts/numbering-rule.md`（127 行）— 编号规则字典驱动模型 + 应用范围
- `concepts/common-interaction-spec.md`（172 行）— 通用交互/组件规范 + 全局默认+偏离声明

## [2026-07-27] lint | 阶段 C 收口

- **文件清点**：31/31（9 concepts + 7 domains + 3 features + 4 flows + 5 master-data + 3 治理）✅
- **wikilink 断链**：内部零断链（`link`/`wikilink`/`wikilinks` 为 SCHEMA 语法说明文本，`monorepo-architecture` 为指向 code-wiki 的有效跨库引用）✅
- **出站链接**：28 内容页全部 ≥4（log.md 审计文件除外）；5 枢纽页（product-overview/menu-structure/version-publish/common-interaction/material-master）出站 5-16 ✅
- **frontmatter 双状态字段**：28 内容页 iter_status + prd_filled 齐全且正交 ✅
- **孤立页**：零（所有内容页均有入站）✅
- **related_code_wiki 修正**：批量重映射 8 类推断 slug → 真实 code-wiki slug（mes-weighing→mes-weigh、mes-process-config→mes-process、mes-execution→mes-execute、frontend-shared-components→web-shared-packages 等）；删除 2 个无对偶的字段（numbering-rule / version-publish-mechanism）✅
- **关键事实校正**：`001-库存管理.md` 实为 WMS 网页端、`004-库存管理.md` 实为 MES 移动端（与早期摘要相反，已按 PRD 真实内容落 domain-warehouse-interaction 页）
- **index.md / log.md**：统一状态标注（全 ✅）与 ingest 记录格式

## [2026-07-28] lint | 内容完整性核查与补强（五维度）

> 用户要求"校验核对内容是否完整"。对照 PRD 源与菜单树做五维度核查，发现 3 处实质遗漏并补强。

- **维度① PRD 覆盖率**：132 份 PRD 中有实质内容的基本全覆盖；未引用的 3 个均合理（`010-审计追踪`=111 的复制故只引一份、`物料接收`=华兰 EWM 专项、`001-生产信息`=本次已补入 record-dataset）。
- **维度② 菜单功能覆盖**：对照 `菜单功能.md` 核查——`domain-system-base` 原缺 100 平台多行 + 111 审计整段（已补齐至 19+6 行）；其余 6 个 domain 完整。
- **维度③ 内容深度**（最重要发现）：**配液链路（019/020/021/022，合计 150KB）原仅 domain 一行带过，feature/flow 深度不足**——已补 `feature-weigh-center` 配液子链路小节（浓度求解/配液单/量取状态机/产出作废）、`flow-weighing-execution` 物料件消耗多入口对比、`feature-process` 业务组件表（配料投入/配液投入/配液产出 3 行 + 投料类组件对比）。
- **维度④ sources 完整性**：补全 10 条 sources（配液 020/021/022、配料投入 009、货位日志 002/004、货品日志 001、008-流程配置、040-批签发、001-数据迁移、001-生产信息）。
- **维度⑤ 事实准确性**：lint 零断链、双字段齐全、最大页 207 行（≤250）；核查 agent 准确跳过 2 个会错位的引用（002-货位日志属 WMS 仓库查询，不能塞进 MES 的 050 货位日志；001-生产信息是业务组件，归 record-dataset 而非 material-master）。
- **改动文件（11 页，均刷 updated: 2026-07-28）**：domain-weighing / feature-weigh-center / flow-weighing-execution / feature-process-and-execution-model / approval-flow / domain-production-config / flow-compliance-traceability / domain-warehouse-interaction / domain-lot-release / domain-system-base / record-dataset-master。

## [2026-07-28] update | 为每页追加「📎 PRD 原文」可点击链接段

- **诉求**：frontmatter 的 `sources:` 是纯文本路径，markdown 渲染里不可直接点击；用户要求至少能通过链接点开 PRD 原文查看。
- **做法**：每个内容页正文末尾追加「## 📎 PRD 原文」段，把 frontmatter sources 转为 markdown 相对链接（frontmatter 保留供机器读）。相对路径按目录深度：子目录页 `../../../packages/prd/`、顶层页 `../../packages/prd/`；含空格文件名用 `<URL>` 包裹。
- **精度修正**：首版脚本误抓正文说明文字与表格合并写法（SCHEMA/domain-lot-release/menu-structure 出现 10 个伪断链），改用 awk 仅从 frontmatter `sources:` 段提取后重生成。
- **结果**：28 个内容页共 148 个 PRD 链接，**零断链**（每条相对路径均解析到真实文件）；含空格的 `002 参数配置.md`/`009 字典管理.md` 链接经验证可解析。
- 约定已写入 `SCHEMA.md`「约定」段。

## [2026-07-28] update | PRD 原文纳入 vault（raw/prd/ 副本）

- **问题**：用户反馈 Obsidian 里 PRD 链接点不开——诊断发现 vault 是 `docs/product-wiki/`，而 PRD 在 `packages/prd/`（vault 外），Obsidian 安全限制不允许跨 vault 跳转。
- **方案**（用户选定）：把被引用的 PRD 复制进 `docs/product-wiki/raw/prd/`（Layer 1 不可变副本，llm-wiki 标准），页面链接改指向 vault 内的 `raw/prd/`。
- **两个坑**：
  1. 前缀深度：子目录页到 `raw/prd/` 只需**一级** `..`（`../raw/prd/`），非两级；首版误写 `../../raw/prd/` 导致全部断链，已修。
  2. **macOS Unicode 规范化**：shell `cp` 与 awk 提取的副本文件名出现 NFC/NFD 不一致，导致中文链接「文件存在但找不到」；改用 Python 复制并强制 NFC 文件名解决。同时发现 awk 提取 sources 只拿到 52 个（漏 33 个），Python 正则从链接段提取到完整 **85 个**唯一文件。
- **结果**：`raw/prd/` 含 85 个 NFC 规范化 PRD 副本；148 个链接 **零断链**；Obsidian 可直接点开。
- **同步脚本**：`scripts/sync_raw.py`——packages/prd 更新后重跑 `python3 docs/product-wiki/scripts/sync_raw.py` 刷新副本。
- **约定固化**：SCHEMA 新增「raw/ 目录」节 + 链接路径规则改为 `../raw/prd/`。

## [2026-07-28] fix | 链接段补全（awk → Python）+ raw 同步补齐

- **发现**：核对"只同步非空内容"时发现，之前用 awk 生成「📎 PRD 原文」链接段有 bug，漏抓了部分 frontmatter sources 项，导致链接段不全、raw 也跟着少复制。
- **漏掉的 9 个核心 PRD**（已在 sources 但链接段/raw 缺失）：称量工单执行(65KB)、批签发模板补充需求、批签发管理补充需求、登录日志、005-称量日志、001-生产历史、007-物料管理、006-清场日志、业务组件。
- **修复**：新增 `scripts/regen_links.py`（Python 精确解析 frontmatter sources 重新生成链接段，替代 awk）；修正 `sync_raw.py` 的 ROOT 路径（少算一级 dirname）。
- **结果**：链接段 148→169 个；raw/prd 85→94 个；**169 链接 0 断链**。
- **最终未同步 38 个**（全部合理）：21 个 0 字节空文件 + 13 个 ≤120B 占位 + 4 个有内容但合理跳过（`010-审计追踪`=111 复制、`参数配置`=002 早期版、`物料接收`=EWM 专项、`010-待办`=占位图）。
- 同步流程固化为两脚本：`regen_links.py`（sources→链接段）+ `sync_raw.py`（链接段→raw 副本）。

## [2026-07-28] lint | 查漏补缺深度审计（第二轮）

用户要求再次核对完整性与正确性。结构性脚本 + 2 agent 深核 + 跨页一致性扫描，发现并修复 3 处真问题。

- **iter_status 修正**：发现建页时 28 页全标"已发布"（不准）。对照 `菜单功能.md` 图例改为多值——**已发布 1**（common-interaction-spec）/ **待测试 16**（称量·工艺·记录·检验·权限·生产管理等 🔵 主导）/ **规划中 11**（批签发·仓储·系统基础·配方·设备·物料等含 🔴）。规则：含🔴不超规划中、含🟡不超待测试、全🟢才已发布。
- **menu_code 补全**：3→18 页（domain 填父菜单级如 `120-040`，feature/flow/master-data 填功能级如 `120-020-006`）；10 个 concept 横切页无单一菜单归属，不强加。
- **内容事实核查**（5 高风险页）：material-master / feature-weigh-center / flow-material-flow / domain-warehouse-interaction **4 页正确无误**（含 001/004 库存归属、物料平衡公式、量值模型、签名状态机均与 PRD 一致）；**feature-process-and-execution-model 工艺版本状态机「审批不通过」目标态错**（写"编辑"，PRD 006_7 L834 实为"确认"）——已修状态机图+表。
- **跨页一致性**：量值模型 / 物料件签名状态机 / 主数据边界 / 版本发布机制 4 个跨页概念描述一致（均回链权威页）。修正 `location-master` 库存管理编号错误（MES 移动端 002→**004**，并理顺货位管理表述）。
- **最终 lint 全绿**：iter_status 多值准确；frontmatter 字段 28/28 齐全；wikilink 0 断链；raw 169 链接 0 断链；库存管理描述全库一致（001=WMS网页端 / 004=MES移动端）。
