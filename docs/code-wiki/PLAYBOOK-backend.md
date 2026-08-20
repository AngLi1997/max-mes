---
title: Code-Wiki Playbook · 后端篇
created: 2026-06-29
updated: 2026-06-30
---

# Code-Wiki Playbook · 后端篇（Backend）

> **适用范围**（重要）：
> - ✅ **services 应用层代码** —— `packages/backend/services/{platform,mes,lims,wms,gateway}` 下的业务服务。本篇所有方法、扫描命令、模板、踩坑均针对此场景。
> - ❌ **不适用于** starter / SDK / 平台库代码 —— 包括但不限于：
>   - 当前已引用但源码未入库的 `bmos-platform-facade`、`bmos-api-feign`、`bmos-audit-engine-starter`、`bmos-orchestrator-starter`、`bmos-scheduler-core` 等（注：`bmos-parent-starter` 含 `bmos-cloud-dependency` BOM 及 16 个 starter 已于 2026-07-15 subtree 引入 `packages/backend/shared/`，见 [[parent-starter-overview]]）
>   - `packages/backend/shared/` 下的共享 starter / SDK（已引入 `bmos-parent-starter`，独立 starter 建页方法论见本文第十节；首次实战即 [[parent-starter-overview]]）
>   - 自动配置类 / `@EnableXxx` 注解 / `META-INF/spring.factories` 这类自动装配代码
>
>   这类代码的抓取信号、建页对象、文档读者动机与 services 应用层完全不同。当第一次有 starter / SDK 源码入库需要建 wiki 时，回到本文末尾的"未覆盖场景 TODO"按真实代码补齐方法论，再实战。
>
> 前端建页请读 [[PLAYBOOK-frontend]]。两者由 [[PLAYBOOK]] 顶层入口分流。
>
> 关系：[[SCHEMA]] 定义「约定与格式」，本文定义「services 应用层的方法与流程」。
> 任何 AI 接 services 应用层任务前先读完本文 + [[SCHEMA]] + [[index]]，三者合起来不超过 600 行。

## 一、决策树：何时建页

### 建页阈值（硬指标）

- **Java 文件数 ≥ 50** 且至少 **2 Service 或 2 表** → 值得独立建子页
- 仅 1 Service + 0~1 表 且 Java < 30 → **不建页**，归并到 overview / concept 页
- Controller 数**不是**主指标（后台支撑型模块 Controller 少但业务重，单看会低估）

> 历史踩坑：首版 mes-overview 仅按 Controller 数分档，把 workflow(65 Java) / audit(90) / execute(76) / dataset(91) 错排尾部。已在 2026-06-29 用 Java 文件数口径修正。

### 不建页的情况

- 工具函数 / 辅助类 / 异常包 / 配置包 / 定时任务壳
- 「调其它服务的本地适配层」（归 [[service-integration]]）
- 单 Controller 单表的轻量集成

### 拆分阈值

- 单页 > 200 行 → 按子主题拆分，wikilink 互联（见 SCHEMA）

## 二、五步法（标准流程）

### Step 1 · 扫描定基线

进入新服务/新模块时，**先跑下面的扫描命令**取真实数据，再决定建几页：

```bash
# 0. 设变量
S=/d/workspace/bmos-monorepo/packages/backend/services/<svc>

# 1. 服务整体规模（Controller / Service / Mapper / Java 总数）
find "$S" -name '*Controller.java' | wc -l
find "$S" -name '*.java' | wc -l

# 2. 子域多维指标（按 Java 数排序，决定建哪些子页）
for d in $(ls "$S/<service-pkg>/src/main/java/com/bmos/<svc>/service"); do
  [ -d "$S/.../$d" ] || continue
  ctrl=$(find "$S/.../$d" -name '*Controller.java' | wc -l)
  svc=$(find "$S/.../$d" -name '*Service.java' | grep -v Impl | wc -l)
  mapr=$(find "$S/.../$d" -name '*Mapper.java' | wc -l)
  tbl=$(grep -rh '@TableName' "$S/.../$d" | sed -E 's/.*@TableName\("?//; s/[",].*//' | sort -u | wc -l)
  java=$(find "$S/.../$d" -name '*.java' | wc -l)
  printf "%-5d %-4d %-4d %-4d %-5d  %s\n" "$ctrl" "$svc" "$mapr" "$tbl" "$java" "$d"
done | sort -k5 -rn

# 3. 配置（端口/注册名/context-path）
grep -niE 'port:|name:|context-path:' "$S/.../resources/application.yml"
grep -niE 'name:|server-addr:' "$S/.../resources/bootstrap.yml"

# 4. 启动类
grep -rl '@SpringBootApplication' "$S" --include='*.java'

# 5. 关键 starter
grep -oE '<artifactId>bmos-[a-z-]+</artifactId>' "$S/.../service/pom.xml" | sort -u
```

### Step 2 · 选页类型 + 应用模板

按 Step 1 数据，决定建哪种页（见第四节模板）：

| 数据特征 | 页类型 | 模板 |
|---|---|---|
| 新服务（5000+ Java，多模块） | entity | **Service Overview** |
| 服务内重模块（≥50 Java） | entity | **Module 子页** |
| 跨服务机制（Feign / Auth / 数据流） | concept | **Concept 跨服务** |
| 多服务/模块横向对比 | comparison | **Comparison 速查** |

### Step 3 · 抓取「重点信号」

不要平铺所有代码，**只抓下面 6 类信号**——这是让 AI 后续能定位问题的关键。详见第五节。

### Step 4 · 写页

- 用第四节模板
- frontmatter 必须含 `service` 字段
- `sources:` 写**真实相对路径**
- 每页 ≥ 2 个出站 wikilink

### Step 5 · 集成

写完后必须做的 4 件事（缺一不可）：

- [ ] **更新 `index.md`**：对应条目 ⏳ → ✅；新页未列则补登记
- [ ] **追加 `log.md`**：写明「哪些代码 → 哪些页」，含关键发现
- [ ] **双向链接**：新页指向相关页（已存在的）；相关页也回链到新页（如 mes-overview 头部子域行加链接）
- [ ] **若发现新踩坑/模式**：回写本 PLAYBOOK（第六节、第七节）

## 三、扫描命令速查（按需复制）

> 路径占位 `<X>` 替换为实际服务/模块路径。

### 表名（@TableName）

```bash
grep -rh '@TableName' "<X>" | sed -E 's/.*@TableName\("?//; s/[",].*//' | sort -u
```

### Feign 调用关系（@FeignClient）

```bash
grep -rEnH '@FeignClient' "<X>" --include='*.java' \
  | sed -E "s#:[0-9]+:#:: #"
```

### Service 接口方法签名（不进 Impl）

```bash
grep -E '^\s+[A-Za-z<].*\(' "<X>/<ServiceName>.java"
```

### 关键枚举内容

```bash
for f in "<X>/enums/"*.java; do
  echo "--- $(basename "$f" .java) ---"
  grep -E '^\s+[A-Z_]+\(' "$f"
done
```

### MQ Topic / Consumer

```bash
grep -rln '@Topic\|@Consumer\|BaseMqTopic' "<X>" --include='*.java'
```

### 定时任务

```bash
grep -rln '@XxlJob\|@Scheduled' "<X>" --include='*.java'
```

### 跨服务依赖（被谁依赖、依赖谁）

```bash
# 在 mes/lims/wms 中 import 当前服务的频次
grep -rl 'com.bmos.<svc>' /path/to/other-services --include='*.java' | wc -l
```

### 抽象基类 / 策略接口

```bash
grep -rn 'abstract class\|^public interface' "<X>" --include='*.java' \
  | grep -vE '(Service|Mapper|Controller)\.java'
```

## 四、页面模板

### 模板 A · Service Overview

适用：新服务的总览页（如 mes-overview / platform-overview）。

```markdown
---
title: <Service> 服务总览
type: entity
service: <svc>
tags: [backend, <svc>, module, ...]
sources: [包路径]
---

## 概述 / 职责
（端口、注册名、context-path、启动类、规模、被依赖程度）

## Maven 模块结构
（common / facade-or-feign / service）

## 关键 starter 与能力
（pom 中的 bmos-* starter 表格）

## 业务子域全景（N 个）
（Java 文件数分层：头部表格 / 中部表格 / 尾部清单 + 归并去向）

## 对外 Feign 契约
（暴露给其它服务的 Feign 清单）

## 调用关系
（→ 谁 / 被谁调）

## 数据库表概要
（表数、前缀、跨服务复用表）

## AI 处理任务时的入口约定
（按场景给出包路径）

## 相关页面
```

### 模板 B · Module 子页

适用：服务内的重模块（如 mes-process-module / mes-plan-module）。

```markdown
---
title: <Service> <Domain> 模块（简述）
type: entity
service: <svc>
tags: [backend, <svc>, module, mybatis, ...]
sources: [子域包路径]
---

## 概述 / 职责
（业务定位、规模 Ctrl/Svc/Mapper/Tbl、关键依赖、独有机制）

## [可选] 子包速览
（子域内若按多子包组织——如 plan 的 7 子包——先讲整体结构）

## 数据模型（N 张表）
（按子主题分组的表 + Model 类名 + 用途；autoResultMap 字段标注；跨服务复用表标注 ⚠️）

## 关键枚举
（状态机第一优先：列出每个枚举的 code / value / 含义；复合枚举要展开多列）

## Controller（N 个）
（按职责分类）

## Service 核心方法
（头部 Service 的方法签名，分类归组；标注与外部回调的对接点）

## [可选] 扩展点 / 策略体系
（如 record 的 ComponentStrategy / process 的 ConditionChangeType）
- **只列接口签名**，不展开实现
- 策略实现按业务类别**分组速览**（不要平铺所有类）

## [可选] 独有机制
（MQ、定时任务、Repository、Word 解析、公式引擎等——只标入口）

## 关键常量 / Redis Key / 配置映射
（包含「改库结构必须同步改本类」类警告）

## 与其它子域的耦合点
（→ process 的事件 / → record 的绑定 / → platform 的 Feign）

## AI 定位提示
（按典型场景给入口：改 X 看 Y、排查 X 查 Y）

## 相关页面
（mes-overview + 同服务相关模块 + 跨服务概念）
```

### 模板 C · Concept 跨服务

适用：跨服务机制（如 service-integration / auth-and-license）。

```markdown
---
title: <Concept Name>
type: concept
service: cross
tags: [...]
sources: [多个服务路径]
---

## TL;DR
（3-4 条最重要的结论）

## 核心契约 / 抽象
（接口签名 / 注解 / 命名约定）

## 关系矩阵 / 调用图
（表格优先）

## 关键发现 / 已知问题
（含 ⚠️ 标注的陷阱：错别字、悬空依赖、技术债）

## 关键约定（新代码遵循）

## AI 定位提示

## 相关页面
```

### 模板 D · Comparison 速查

适用：横向速查表（如 service-overview / database-schema-overview）。

```markdown
---
title: <Topic> 速查
type: comparison
service: cross
tags: [...]
sources: [...]
---

## TL;DR / 最重要的映射
（如表前缀 → 服务映射）

## 主表
（多列对比，按规模/重要性排序）

## 分组明细
（按业务域 / 类别分组的细分表）

## 技术约定 / 注意事项

## 相关页面
```

## 五、重点信号清单（抓取页面内容时聚焦）

**不要逐文件复制代码**——AI 后续能读源码，wiki 只需提供"该读哪里"的导航。重点抓以下 6 类信号：

### 信号 1 · 数据模型（表 + Model 类名映射）

- `@TableName` 抓全部表
- 关注：`autoResultMap` 标注（含 JSON 字段）、表名前缀异常（如 mes 内 `product_schedule_procedure_config` 无 `bm_` 前缀）、跨服务复用表
- **写法**：表 / Model 类 / 用途 三列。Model 类名与表名不一致时**显著标注**（如 plan 的 `Plan` 对应 `bm_product_plan`）

### 信号 2 · 状态机枚举

- 抓 `enums/` 目录所有枚举
- 不抓字段定义，**抓 code / value / 中文含义 + 状态流转方向**
- 复合枚举（一个枚举值含多维度，如 mes `ProductionStatusEnum`）必须展开多列
- 这是 AI 排查"状态卡住"类问题的速查键

### 信号 3 · Service 接口方法签名

- 只抓接口（`*Service.java`），不抓 Impl
- 头部 Service 完整列方法签名并**分类归组**（CRUD / 审批回调 / 执行控制 / 查询）
- 其它 Service 一行带过即可

### 信号 4 · 扩展点（接口 + 策略体系）

- 抽象基类、策略接口 → **只列接口签名**
- 策略实现 → **按业务类别分组速览**（如 mes-record 的 57 个 ComponentStrategy 分 15 类）
- 永远不展开具体策略实现代码

### 信号 5 · 独有机制（MQ / Job / Repository / 表达式）

- 只标入口位置和约定，不展开内部实现
- 如：「Word 文档解析入口：DocxValidator + DocxSplitUtil2，依赖 docx4j。详细解析逻辑略，需要时直接读源码。」

### 信号 6 · 隐藏地雷 / 跨服务约定

- 命名陷阱（`Plan` vs `ProductPlan`）
- 包名错别字（mes `facotry`）
- 跨服务复用表的写入归属（mes 写 / lims 读）
- 唯一索引名 → 业务错误码的映射（`PlanConstant`）
- 悬空 Feign 依赖（platform 的 plasma / centralization-lims）

**这些都必须 ⚠️ 标注**——AI 后续修复时会用到。

## 六、节奏控制原则

- **不做 ≠ 做错**：体量大就拆，单页 > 200 行立即停笔考虑拆分
- **门户优先**：先 overview 再 module 子页；先核心服务（platform/mes）再外围（lims/wms/gateway）
- **跨页引用闭环优先**：建一页就消除已存在的 ⏳ 引用，避免悬空 wikilink 越积越多
- **样板先行**：第一次建某类页时，先出 1~2 个样板与用户对齐，再批量建
- **不重复造**：第二个服务/模块写之前先看第一个的模板；模板需调整就回写本 PLAYBOOK

## 七、已知踩坑库（持续积累）

> 每条都是真实犯过的错或发现的坑。新增坑回写到本节。

| 坑 | 教训 | 防御 |
|---|---|---|
| `cd <dir>` 后所有相对路径漂移到新 cwd | Bash 工作目录持久但不可见 | 跨多次扫描时用**绝对路径变量** `B=/d/workspace/...` |
| 单看 Controller 数分档 | 后台支撑型模块被低估（workflow/audit/execute/dataset） | 用 **Java 文件数 ≥ 50** 阈值，Ctrl/Svc/Tbl 多维参考 |
| 假设"mes 表全部 `bm_` 前缀" | `product_schedule_procedure_config` 无前缀，整合前遗留 | 用 `@TableName` 实扫，不靠前缀推断 |
| 假设"Model 类名 = 表名驼峰" | `bm_product_plan` → 类名 `Plan` 不是 `ProductPlan` | 实扫 `model/` 目录，类名与表名不一致时显著标注 |
| 假设"lims Nacos 名 = bmos-lims-service" | 实际是 `bmos-lims2-service` | 实扫 `@FeignClient(name=...)`，不靠服务名推断 |
| 假设"Feign 接口集中在 facade/feign 模块" | mes/lims/wms 在 `service/platform/.../feign/` 重复定义 21 个 Platform* client，与 facade 14 个 Feign 功能重叠 | 全量扫 `@FeignClient`，新代码统一走 facade |
| 假设"标准章节就够" | Neta entity 页有"关键字段速查 / 核心 Service 方法 / 主运行路径"才到位 | 用本文模板 B 的完整结构 |
| 假设"业务子域分类一次到位" | 单维度判断（仅 Controller）会漏判 | 第一版 overview 出完，**用户 review 是必经步骤**，按反馈回写 |
| 抽象基类被当成枚举处理 | mes process 的 `ConditionChangeType` 是抽象类不是 enum，9 个事件子类继承它 | 看到 `event/` 目录先 `head -20` 一个文件确认形态 |
| 平铺策略实现 | mes record 57 个 ComponentStrategy 平铺会爆页 | **按业务类别分组速览**，绝不展开实现 |
| 假设"工作区源码可直接读" | gateway/wms/lims 工作区 `.java` **曾**被 TSD 加密（`%TSD-Header-###%` 头，8192 字节块），ripgrep/Read 得乱码、`@TableName`/`@FeignClient` 零命中。**2026-06-30 已解密，现可直接读** | 实扫前先判断服务：正常直接读；**若遇 `%TSD-Header-###%` 乱码（历史或新接入服务），回退 `git show HEAD:<path>` 或 `cat`**。详见 [[monorepo-architecture]] |
| 假设"overview 头部子域表的 Tbl 列准确" | mes audit overview 标 Tbl=2，实扫配置侧 6 张表（`bm_flow_audit` 系列，另有历史/版本/分类/用户/消息/工艺绑定） | 建子页时**以 `@TableName` 实扫为准**，发现偏差顺手回正 overview |
| 类名错别字不只是 `facotry` | mes audit 的 `ProcessAuditConditon`（漏 i，应为 Condition）与 `facotry` 同类历史遗留；改名涉及枚举引用与 import 路径 | 扫到 `Conditon`/`facotry`/`Recieve` 等可疑拼写时 ⚠️ 标注，归入"包名错别字"独立重构任务清单 |


## 八、新服务/新模块上手 checklist

```
新服务/新模块进入 monorepo 时，按顺序执行：

[ ] 1. 读本 PLAYBOOK + SCHEMA + index（< 600 行，10 分钟内）
[ ] 2. 跑第三节扫描命令，拿到服务规模和子域多维指标
[ ] 3. 按第一节决策树判断要建几页
[ ] 4. 先出门户级 overview（模板 A）
[ ] 5. 用户 review 后再出模块子页（模板 B）
[ ] 6. 头部子域按 Java 文件数降序逐个建
[ ] 7. 每建完一页：更新 index + 追加 log + 双向链接（Step 5 四件套）
[ ] 8. 发现新踩坑/新模式 → 回写本 PLAYBOOK 第六/七节
[ ] 9. 用户接受后，把对应 ⏳ 改为 ✅
```

## 九、本文档自身的维护

- **触发更新场景**：发现新踩坑 / 新模板需求 / 新扫描技巧 / 节奏控制经验
- **不更新的场景**：单次任务的具体内容、与方法论无关的事实
- **更新者**：每次执行本 PLAYBOOK 的 AI，如有改进**必须**回写
- **审计**：本文修改也需在 `log.md` 追加一条 `update | PLAYBOOK ...`

---

## 十、未覆盖场景 TODO（待实战补齐）

> 本节列出**本篇尚未覆盖**的场景，引入对应代码时**回到这里按真实代码补足方法论**，再开始建页。
> 这是诚实声明，避免后续 AI 误用 services 应用层方法论处理其它类型代码。

### 1. Bmos 平台 starter / SDK 源码（最优先）

**触发条件**：以下任一情况出现时启动本 TODO：
- `packages/backend/shared/` 下出现实际代码（当前为空目录）
- 任一 starter 源码以 git subtree 形式引入 monorepo
- 例：`bmos-cloud-dependency` / `bmos-platform-facade` / `bmos-api-feign` / `bmos-audit-engine-starter` / `bmos-orchestrator-starter` / `bmos-scheduler-core` 等

**与 services 应用层的核心差异**（建页时按此调整）：

| 维度 | services 应用层 | starter / SDK |
|---|---|---|
| 主要代码 | Controller / Service / Mapper / 业务表 | `@AutoConfiguration` / `@Configuration` / Properties / SPI |
| 数据信号 | `@TableName` | 通常无表；元配置可能落 `META-INF/` |
| 接口契约 | Controller / `@FeignClient` | `@EnableXxx` 注解 / `BeanPostProcessor` / 拦截器 |
| 装配信号 | `@SpringBootApplication` | `META-INF/spring.factories` / `META-INF/spring/*.imports` / `@AutoConfiguration` / 条件链 `@ConditionalOnXxx` |
| 配置项 | application.yml | `@ConfigurationProperties(prefix=...)` 类 + 默认值 + 校验 |
| 用户视角 | "改业务逻辑去哪" | "怎么用 / 起什么作用 / 配什么参数 / 启用/禁用条件 / 影响范围" |
| 调用方 | 前端 + 其它服务 | 各业务 services 的 pom 引入；影响面 ≥ 1 个服务 |

**实战前需补足的内容**（实战时回写到本 PLAYBOOK 或拆为 `PLAYBOOK-starter.md`，视复杂度决定）：

- 建页阈值（基于"被几个服务依赖" + "暴露多少配置项" + "源码规模"多维）
- 扫描命令：
  - 自动配置类清单：`grep -rn '@AutoConfiguration\|@Configuration' src/main/java`
  - `@EnableXxx` 入口注解
  - `@ConditionalOnXxx` 条件链
  - Properties 类：`grep -rln '@ConfigurationProperties' src`
  - SPI / 装配元数据：`find . -path '*META-INF/spring*'` 及 `META-INF/services/`
  - 暴露的核心 Bean / 拦截器 / Filter / AOP 切面
  - 被依赖关系：`grep -rln "<artifactId>$STARTER</artifactId>" <repo>`
- 页面模板（Starter Entity）：
  - 概述（提供什么能力，被哪些服务依赖）
  - 启用方式（`@EnableXxx` 或自动装配）
  - 配置项清单（前缀、键、默认值、校验）
  - 核心 Bean / 扩展点
  - 条件链（什么时候生效）
  - 影响面（拦截器顺序、被注入的 Spring 上下文）
  - 与业务服务的耦合点
- 预估踩坑：
  - 自动装配顺序问题
  - 条件链不生效（`@ConditionalOnMissingBean` 优先级）
  - SPI 文件忘改导致新 AutoConfiguration 不加载
  - Properties 前缀冲突
  - 版本不一致（mes/wms 用 1.14、lims 用 1.15.2 已是现存问题）

### 2. `packages/backend/shared/` 目录（当前为空）

**触发条件**：`shared/` 下出现实际代码。
**处理**：按"共享库"思路建页，参考前端篇 W2 共享库模板的精神（暴露 API / 被依赖关系 / 关键约定）改造为后端版。

### 3. 独立工具 / 脚手架代码

**触发条件**：出现独立的代码生成器、CLI、构建插件等非业务非 starter 代码。
**处理**：实战时按代码形态新设页型，可能需要新模板。

### 4. 维护规则

- 引入对应代码 → **在本节对应条目下写一行"已实战，方法论已落地到 X 节"或拆出新分册**
- 拆分阈值：若某类代码的方法论 > 80 行且与 services 应用层差异显著 → **拆出 `PLAYBOOK-starter.md` 等独立分册**，并更新 [[PLAYBOOK]] 入口的分流表
- 不要凭空补：**只在真实代码入库后基于扫描结果回写**，避免空想踩坑库

---

> 本 PLAYBOOK（后端篇）与 [[SCHEMA]] 一起构成 code-wiki 后端建设的"宪法"。先读它们 + [[PLAYBOOK]] 顶层入口，再读 [[index]] 找具体内容。前端建页见 [[PLAYBOOK-frontend]]。
