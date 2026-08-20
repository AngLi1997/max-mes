# MES 物料建模——现状问题与调整方向（对齐文档 · 进行中）

> **定位**：**和辉哥/团队对齐认知的参考文档**，不是施工蓝图。主线 = **现有物料建模的问题 → 调整方向**。
> **怎么读**：**§0 现状与问题（对齐主线，先看）** → §1 起是**调整方向 / 目标设计**（往哪调）→ 其中字段级是**目标参考**，非施工图。
> **日期**：2026-07-21 创建 / 2026-07-23 重构为对齐文档
> **负责人**：物料 owner
> **范围**：物料 + 配方 + 追溯 连贯域
> **参照标准**：ISA-88 (S88) + ISA-95 + 制药 GMP
> **现状代码**：`packages/backend/services/mes`

---

## 0. 现状与问题（对齐主线 · 先看这个）

### 0.1 现状代码事实

基于对 `packages/backend/services/mes` 的代码探查：物料建模**主体已存在且相当完整**（颠覆了"物料没进配方"的初始假设）——

- **物料定义**：`bm_material` + `bm_material_category`（`category_type`：0 原辅包 / 1 中间品 / 2 产品，统一一张表）。
- **物料实例两级**：`bm_storage_material_batch`（批次，含 `hydration`/`noHydrationContent`）→ `bm_storage_material`（件/最小包装）。
- **物料已进配方**：`bm_product_formula` → `_version` → `bm_product_formula_material`（materialId + quantity + 三类允差 + dryPure）。
- **工艺↔物料已关联**：`bm_process_version.productFormulaVersionId`；`bm_procedure_model_material`；`bm_material_trace_template` + `_procedure_step`。
- **执行闭环已具备**：`bm_ingredient_plan` → `_material_batch` → `bm_ingredient_input_record` → `bm_material_trace_history`。
- 主数据来自 platform（`bp_material`）；已有公式引擎（`mes-record-module`）+ platform `audit-engine-starter`。

### 0.2 现有物料建模的问题（我们要调整的根因）

主体虽在，但有 **8 个结构性问题**——正是本要对齐、要调整的：

| # | 问题（现有） | 为什么是问题 | 调整方向（→ 详见 §1 起） |
|---|---|---|---|
| 1 | **主物料/成员物料概念模糊**（有 `principalMaterialId`，但替代/等效语义没讲透） | "同物料多供应商"没法规范管理，编码/替代乱 | 明确**四级模型**（主物料=Material Class / 成员=Definition），主物料独立实体（§4.1/4.8） |
| 2 | **配方与工艺两套结构分裂**（`bm_product_formula_material` 与 `bm_process` 靠引用缝合） | 三套并行（含追溯模板），冗余、易漂移 | **配方并入步骤**（StepInput/StepOutput）（§2/§6.2） |
| 3 | **追溯是单独配的模板，不是派生的**（`bm_material_trace_template` 与配方物料两套并行） | **链条会断**（辉哥原话） | 投入产出进执行记录，**追溯派生**（§2/§6.4） |
| 4 | **缺控制配方层**（只有主配方，没有批次级"实际发生了什么"） | 辉哥说"**没有追溯能力**" | 补 **BatchExecution**（Control Recipe）（§3/§6.4） |
| 5 | **折干折纯概念没理清**（dryPure 在配方、hydration 在批次，结构对但本质没讲透） | "两套规则"（参数源不同）没点破，易混 | 明确**两套规则**：成员等效(物料层·静态) + 批次折算(配方方法·批次动态)（§4.5） |
| 6 | **属性扩展是自由 JSON**（`expandInfo`） | 化药/生物药/中药不同属性塞一起，没结构/没校验 | **Profile 驱动**的属性模板（§6.1 ⑪） |
| 7 | **投料来源单一**（只想到称量） | 配液、跨批次、共享池场景没法表达 | **多态投料来源**（3 维度）（§6.2） |
| 8 | **等效边界无处安放**（"哪些供应商的料能用"没 per-product 控制） | 跟 GMP 注册批准脱节 | 等效边界在 **StepInput**（配方层）（§4.7） |

### 0.3 调整方向总览（一句话）

> **把"配方/工艺/追溯三套并行"收敛成"物料进步骤 + 控制配方层 + 派生谱系"一条主线**，并补齐主物料/成员、两套规则、Profile 扩展、多态投料、等效边界等缺失语义。问题 1-8 的具体调整见 §1 起的目标设计。

---

## 1. 设计前提（目标设计的基底 · 已锁定 ✅）

> §1-§6 是**调整方向 / 目标设计**——针对 §0.2 的 8 个问题，"应该往哪调"。字段级是**目标参考**，非施工图。

| 维度 | 决定 |
|---|---|
| **目标** | 从 S88 第一性原理出发的正确设计，新 spec，不迁就现有代码 |
| **范围** | **C — 物料 + 配方 + 追溯 连贯域全设计**（哪怕与工艺/追溯 owner 的 slice 重叠） |
| **正确标尺** | S88 骨架 + 辉哥业务权威 + GMP 底线（复用 audit-engine）；**流程行业 > S88 batch（A）** |
| **设计方案** | **② 步骤即配方 / 单模型** |

---

## 2. 整体架构（已认 ✅）

四层流转结构——物料从"是什么"流到"能追到"：

```
① 定义层 「有什么」   物料定义(主物料/成员物料/分类) + 工厂/设备/人员(引用)
    ↓ 被引用
② 模板层 「怎么生产」=配方   工艺→工序→步骤，物料投入产出长在步骤上；步骤间 DAG(支持重叠/提前)
    ↓ 创建批次按模板跑
③ 执行层 「实际在跑」=控制配方   批次执行→步骤执行→投料记录/产出记录
    ↓ 执行记录天然形成
④ 谱系层 「能追到」   投入产出图，正追/反追；GMP 审计复用 platform audit-engine
```

### 两个核心"正确性"动作

1. **配方并入步骤**（消灭配方/工艺分裂）：没有独立"配方"实体，配方 = 步骤树的物料面（`StepInput`/`StepOutput` 取代独立 `ProductFormulaMaterial`）。
2. **追溯派生而非单独建模**（消灭追溯模板冗余）：没有独立"谱系"实体，谱系由执行层投入产出记录**计算**而来（现有 `bm_material_trace_template` 退化为纯展示配置）。

---

## 3. 核心实体清单（已认 ✅，含主物料修正）

| 层 | 实体 | 职责 | 归属 |
|---|---|---|---|
| ① 定义层 | **主物料 PrincipalMaterial** | 替代等效组（= ISA-95 Material Class） | [物] |
| ① 定义层 | **成员物料 MaterialDefinition** | 具体供应商+规格，独立编码（= ISA-95 Material Definition） | [物] |
| ① 定义层 | 物料分类 MaterialCategory | 分类树（原辅包/中间品/成品） | [物] |
| ① 定义层 | **物料属性模板 MaterialPropertyProfile** | 按物料种类定义扩展属性集（化药/生物药/中药/辅料…），成员继承（§6 ⑪） | [物] |
| ② 模板层 | 工艺 Process / 工序 Procedure | 工艺结构（版本化） | [艺] |
| ② 模板层 | **步骤 Step** | 配方载体：挂设备功能 + 投入 + 产出 | [艺]+[物] |
| ② 模板层 | **步骤投入 StepInput** | 引用主物料 + 用量 + 允差 + 折算规则 + **允许成员边界** | [物] |
| ② 模板层 | **步骤产出 StepOutput** | 产出（中间品/成品）+ 预期量 + 收率 | [物] |
| ② 模板层 | 步骤依赖 StepDependency | 步骤间 DAG（流程行业） | [艺] |
| ③ 执行层 | 批次执行 BatchExecution | 批次按工艺模板跑（= S88 控制配方层，辉哥要补的） | [艺]/[追] |
| ③ 执行层 | 步骤执行 StepExecution | 批次下步骤的实际执行 | [艺]/[追] |
| ③ 执行层 | **投料记录 MaterialCharge** | 实际投的成员物料+批次+件+实际量+时间/人/终端 | [物]+[追] |
| ③ 执行层 | **产出记录 MaterialOutput** | 产出新物料实例（触发赋码+谱系指针） | [物]+[追] |
| ③ 执行层 | 物料批次 MaterialLot | 物料实例-批次级（批号+质量状态+效期+检验值） | [物] |
| ③ 执行层 | 物料件 MaterialUnit | 物料实例-件级（件号+量+货位=最小包装） | [物] |
| ④ 谱系层 | *（无独立实体）* | 由 MaterialCharge + MaterialOutput 派生 | [追] |
| ④ 谱系层 | 追溯视图配置 TraceViewConfig（可选） | 只管展示，不是数据源 | [追] |

> 物料 owner 的 [物] 横跨定义/模板/执行三层——物料建模是贯穿主线，不是孤岛。

---

## 4. 物料模型的正确性决策（深入讨论 ✅）

### 4.1 物料是四级，不是三级（ISA-95 映射）

| 我们的设计 | ISA-95 术语 | 含义 |
|---|---|---|
| **主物料 PrincipalMaterial** | Material Class | 物料类/替代等效组，属性可继承 |
| **成员物料 MaterialDefinition** | Material Definition | 来自**特定供应商**的特定物料，对应 Material Master 一条，独立编码 |
| 物料批次 MaterialLot | Material Lot | 一批，有 lot ID |
| 物料件 MaterialUnit | Material Sublot | 批次的细分（最小包装） |

> ISA-95 原话（OPC 10030 §8.4）：Material Definition 是"来自特定供应商的特定物料，对应 Material Master 一条"——与"成员物料=不同供应商/规格、编码不同"一字不差。

### 4.2 主物料/成员物料 = 替代/等效机制

- **解决的问题**：同一物料，不同供应商/不同**包装规格** → 物料编码必须不同（GMP 可追溯）；但配方不该绑死某供应商。
- **机制**：BOM/`StepInput` 引用**主物料（Class）**；执行时该主物料下的成员物料批次可投（**默认全部可投，可按配方收敛**——见 §4.7 等效边界）。
- **ISA-95 支撑**：引用到 Class 级 = 结构上允许替代（不需要单独的"替代表"）。
- **包装规格 vs 化学规格**（消歧）：
  - **包装规格** → `innerPackingSpecification`，影响"怎么称/怎么领"（扫码代称、多次累积），**不参与折算**。
  - **化学规格**（纯度/含量/效价，成员间）→ 一般**不存在**（成员=同种物料）；若要兼容，见 4.4 的等效规则 seam。
- **分类（MaterialCategory）vs 主物料组（PrincipalMaterial）= 两个正交维度**（消歧）：
  - **分类**回答"这是什么料"（原辅料/中间品/成品 + 子类，树形归类）；
  - **主物料组**回答"哪些成员能互相替代"（等效组）；
  - 一个成员物料属于**一个分类**（与同组其他成员同分类）+ **一个主物料组**；主物料本身也归属一个分类，成员继承。

### 4.3 折干折纯 = 批次级，与成员替代正交

- **触发**：化药场景，**同一物料不同批次**的水分/含量不同，按**检验结果**折算。
- **范围**：折算**水分 + 含量**。
- **与成员替代正交**：成员替代解决"投哪种"（成员级）；折干折纯解决"这批投多少"（批次级）。两者可叠加：先选成员的批次，再按该批次水分折算。

### 4.4 折算规则 = 可配置函数，4 种 form

seam 不是"一个常数"，是**一个可配置的折算函数**：

| form | 含义 | 例子 | 默认 |
|---|---|---|---|
| **常数** | 固定值 | 等效 = 1.0 | ✅ 默认（=1，隐身） |
| **公式** | 表达式（线/非线性） | `用量/(1-水分)`、`用量×标称/实测` | |
| **查表/分段** | 区间 → 值 | 效价∈[98,100]→1.0；∈[95,98)→1.02 | ← dose banding / bracketing |
| **曲线/插值** | 标定点拟合 | 标定 (水分,系数) 点集插值 | ← 非线性 |

- **查表/分段 = dose banding / bracketing / 查表法**，制药（生化药、化药按效价投料）**主流做法**，非特例。
- **默认 form=常数、值=1**，零负担；需要时切公式/查表/曲线。
- **复用现有公式引擎**：`mes-record-module` 的 `FormulaConfig` + platform 表达式服务；查表/曲线是给引擎加两种新 rule form。

### 4.5 两套规则，不合并（重要修正 ✅）

物料层和批次层是**两套不同的规则**，关键差别在**参数从哪来**：

| | 物料层：成员等效规则 | 批次层：批次折算规则 |
|---|---|---|
| 算什么 | 不同成员物料之间的等效 | 同一物料某批次的实际折算 |
| 配置的是什么 | 方法 **+ 参数**（都在物料主数据，静态） | **只配置"计算方式"**（方法） |
| 参数从哪来 | 物料定义属性（标称效价等，配置时定死） | **具体批次的检验值**（水分/含量，运行时绑定） |
| 挂在哪 | Material Definition（成员物料） | 方法挂 StepInput（配方），参数从 batch Lot Property 读 |

- **共同点**：两层都支持 4 种 form（常数/公式/查表/曲线）。
- **区别**：参数绑定源不同（物料主数据 vs 批次检验）→ **两套独立规则、两个家、两个参数源，不能合并**。
- **现有代码批次层已分对**：`dryPureType/dryPureParam`（方法）在 `ProductFormulaMaterial`（配方级），`hydration/noHydrationContent`（参数）在 `StorageMaterialBatch`（批次级）。成员等效规则是新增 seam。

### 4.6 效价因子（potency factor）的诚实边界

| 设计元素 | 依据 |
|---|---|
| 四级物料模型（主物料/成员/批次/件） | **ISA-95 Part 1 物料模型**（直接） |
| StepInput 引用主物料(Class) → 允许替代 | **ISA-95 引用层级机制**（直接） |
| 成员等效规则 / 批次检验值 = Property | **ISA-95 Property 模型**（直接，property 是一等对象） |
| 按效价折算投料量（potency-adjusted ratio） | **制药 GMP 行业实践**（用 ISA-95 原语表达，非标准条款） |

> **结论**：结构踩 ISA-95；"效价折算"是行业公认做法，借 ISA-95 的 property + relationship 落地。分清标准 vs 行业惯例。

### 4.7 等效边界 = 配方控制，不在物料层（已决 ✅）

- **决定**：等效边界（哪些成员物料可替代）**不在物料配置层控制**，而在**具体生产工艺/配方（`StepInput`）控制**。
- **理由**：制药 GMP 中"哪些供应商/成员可用"是 **per-product / 注册批准**的——同一主物料在产品 X 可能批准 M1/M2，在产品 Y 可能只批准 M1。所以边界天然是配方/产品级，物料层无从得知"我在哪个产品里被批准了哪些成员"。
- **模型后果**：
  - 物料层（主物料/成员物料）：只定义等效**组**（潜在替代成员的"宇宙"），**不带约束**，保持简单；
  - `StepInput`（配方）：引用主物料 + 用量 + 折算规则 + **允许成员边界**（这一步允许哪些成员，默认全部或显式批准清单）；
  - 执行（`MaterialCharge`）：实际选用的成员**必须在 `StepInput` 边界内**，越界拒绝。
- **边界 vs 计算 分工**（与 §4.5 呼应）：
  - **边界**（which 成员允许）→ `StepInput`（配方）〔本决定〕
  - **计算**（conversion how 折算）→ `Material Definition` 成员等效规则〔§4.5〕
  - 两者分离：物料层管"怎么折算"（成员内禀），配方层管"允不允许用"（注册/验证）。
  - （✅ 已确认：计算挂成员物料、内禀——**选项 1**；不为边缘场景预留配方级覆盖 seam）
- **GMP 对齐**：即制药 "per-product approved suppliers" 模式。
- **迁移提示**：现有配方可能直接绑**具体物料**（`ProductFormulaMaterial.materialId`），新设计 `StepInput` 改绑**主物料 + 允许成员边界**——结构性变化，记入迁移 TODO。

### 4.8 主物料 = 独立实体（已决 ✅）

- **决定**：主物料是 **first-class 实体**（有自己的标识和属性），不是分组标签。
- **理由**：
  1. **基准属性要有家**——基准效价/标准单位/默认储存条件等组级属性，被 §4.5 成员等效计算（成员相对主物料"基准"折算）直接逼出来，必须挂在主物料上；
  2. **ISA-95 对齐**：Material Class 本身就是一等实体，成员继承其属性；
  3. **StepInput 要引用主物料**（§4.7 边界）——引用需要标识；
  4. **现有代码已如此**：`bm_material.principalMaterialId` 让成员指向一条主物料记录。
- **字段级分支（已决 ✅ — A2 单表继承）**：主物料与成员物料**同一张表**（`bm_material`），靠 `definitionLevel` 字段区分（PRINCIPAL / MEMBER），成员用 `principalMaterialId` 指向主物料——与现有代码一致。选 A2 的理由：主物料/成员**共享大量通用属性**（code/name/单位/分类/储存），差异少，单表继承（single-table inheritance）是公认模式、团队好理解、迁移最小；不为概念纯净多搞一张表多一次 join。

### 4.9 追溯粒度 = 成员 + 供应商 + 批次（已决 ✅）

- **决定**：**数据层**追溯粒度 = 成员物料 + 供应商 + 批次（GMP 强制）；**显示层**可上卷到主物料做概览（上卷只是视图，不丢数据）。
- **理由**：成员物料独立编码的初衷就是 GMP 可追溯（区分"哪家供的"）；**召回场景**——供应商某批原料出问题，必须能反查到所有用了该批的成品，停在主物料层就查不出来。
- **现有代码已如此**：`bm_material_trace_history` 记成员+批次级实际投入产出。
- **中间品不涉及此选择**：中间品自带批次号（产出时生成），追溯直接走中间品批次。
- **两个必须支持的追溯方向（核心验收标准）**：
  - **正追（物料 → 生产批次）**：从物料（lot 或物料定义）→ 找所有用了它的生产批次/成品。**场景=召回**：供应商某批原料出问题 → 反查所有受影响成品/批次。
  - **反追（产品 → 所有物料）**：从成品 → **递归**找出所有用过的原料 + 中间品。**场景=质量调查**：成品异常 → 挖出全部投入谱系。
  - 两方向都靠执行记录链路（`MaterialCharge`/`MaterialOutput` ↔ `StepExecution` ↔ `BatchExecution`）**派生**，链路字段落地见 §6.4。

---

## 5. 折算链（最终式 ✅）

```
实际投料量 = 用量                                  ← 配方定义（起点）
           ∘ 成员等效规则(·)   挂 Material Definition，参数来自物料属性（静态），默认常数 1
           ∘ 批次折算规则(·)   方法挂 StepInput，参数来自 batch 检验值（动态），默认按水分公式
```

两套规则各自独立配置、各自独立选 form、各自独立绑定参数源。

---

## 6. 字段级数据模型（PM + 架构视角 · 待确认 🚧）

> **设计原则（适用于本节所有字段模型）**：定义"**正确的字段**"——从**产品经理（业务能力）+ 系统架构师（干净领域模型）**视角出发，**不掺实现细节**。数据来源/同步、与现有代码的映射、赋码生成规则、审计轨迹——都是**集成/迁移/横切关注点**，不进核心实体字段（另见 §7）。

### 6.1 物料定义（主物料 + 成员物料）

> A2 单表：主物料/成员同表 `bm_material`，靠 `definitionLevel` 区分。成员特有字段在主物料记录上留空，反之亦然。按**业务能力**组织（不是按现有表结构反推）：

### ① 身份与标识
- `id`
- `definitionLevel`：**PRINCIPAL（主物料）/ MEMBER（成员物料）**——替代层级（≠ categoryType）
- `code`：物料码（业务唯一标识，三级码第 1 级）
- `name`：名称
- `specification`：规格（描述性，如"注射级 / 试剂级"）

### ② 分类
- `materialCategoryId` → MaterialCategory：分类树归属
- `categoryType`（冗余，便于查询约束）：原辅料 / 中间品 / 成品

### ③ 替代归属（成员特有）
- `principalMaterialId` → 主物料：成员 NOT NULL；主物料为 NULL

### ④ 供应商（成员特有）
- `supplier`：供应商（成员 = 特定供应商的料，供应商是其身份的一部分，故在定义层）

### ⑤ 计量与包装（喂称量中心）
- `unitId`：基本单位
- `unitExtendId`：扩展单位（辅助，如瓶/盒）
- `packingSpecification`：包装规格
- `innerPackingSpecification`：**最小包装量**（扫码代称、多次累积称量的依据）
- `preTareWeight`：预置皮重（去皮称量）

### ⑥ 质量属性与质量标准（喂折算 / 检验放行）
- `nominalPotency`：标称效价/含量（成员的标称质量参数，成员等效计算的输入）
- `baselinePotency`：基准效价（主物料组级基准，可选——等效折算的参照）
- `qualityStandardId` → 质量标准/检验规格：该物料的**检验项 + 限度集**（= ISA-95 Material Test Specification；放行判定依据，对接 LIMS/检验）

### ⑦ 折算行为（成员特有）
- `equivalenceRule`：**成员等效规则**——可配置函数（4 form：常数/公式/查表/曲线），默认常数 1（§4.4/§4.5/§4.7）

### ⑧ 储存 / 效期 / 运输
- `storageCondition`：储存条件
- `transportCondition`：**运输条件**（≠ 储存；冷链/常温——生物药冷链命门）
- `defaultShelfLife`：默认效期
- `retestPeriod`：**复检期**（到期复检合格可延长，API/辅料常见；与效期二选一或并存，可空）
- `dyingPeriod`：临期天数（到期前预警）
- `productionCycle`：生产周期
- `storageLifeDays`：**暂存期**（中间品独有：产出后多久内必须用，超期降级/销毁）

### ⑨ 安全与防护（EHS）
- `hazardClass`：危险性分类（高活性 HPAPI / 易燃 / 腐蚀 / 普通…）
- `sdsReference`：SDS/MSDS 安全数据表引用
- `handlingRequirement`：防护/操作要求（containment、PPE）

### ⑩ 生命周期状态
- `status`：草稿 / 启用 / 停用

### ⑪ 扩展（**Profile 驱动**，承载化药/生物药/中药等场景差异）
- 不再用自由 JSON。改为 **`MaterialPropertyProfile`（物料属性模板）** 驱动：
  - profile 绑定到**分类/物料种类**，定义该类物料的属性集（每项：code/name/dataType/unit/required/default/校验）；
  - 成员物料继承所属分类的 profile，按 profile **结构化填值**（可校验、可查询）；
  - 例：[化药原料] profile = CAS、分子式、分子量、晶型、粒径、残留溶剂…；[生物药原料] profile = 细胞库、表达系统、HCP、残留 DNA、内毒素…；[中药] profile = 药材基原、产地、指标成分、农残…
  - **新场景（基因治疗/ADC/…）= 新增 profile，核心表 `bm_material` 不动**；
  - ISA-95 对齐：profile = Material Class 的属性集，成员（Definition）继承。
- 实体见 §3 `MaterialPropertyProfile`。

### ⑫ GMP 合规
- 复用 `audit-engine`，**本表不加审计字段**（审计是横切关注点）

### 剥离到集成/迁移层的关注点（不在本表）
- **数据来源/同步**（如来自 platform/ERP 的关联标识）→ 集成层管理，非物料定义的领域属性；
- **赋码生成规则**（物料码/批次码/件码怎么生成）→ 单独的编码规则配置；
- **审计轨迹**（谁何时改了什么）→ `audit-engine` 横切。

### 6.2 StepInput / StepOutput（模板层 · 配方物料面）

> 配方并入步骤（§2 核心动作 1）的落地：步骤的物料投入产出直接挂在 Step 上，**没有独立"配方"实体**。`StepInput`=投入（理论）、`StepOutput`=产出（预期）；执行时分别落地为 `MaterialCharge` / `MaterialOutput`（§3）。

#### StepInput（步骤投入 · 理论）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `stepId`→Step；`materialTarget` | **主物料**（原辅料，可替代）或**具体定义**（中间品，无替代） |
| ② 用量 | `quantity`/`quantityType`(固定/按批量/计算)/`unitId`/`scale`/`rounding` | 理论用量 + 精度修约 |
| ③ 批次折算 | `batchConversionRule` | **方法挂这**，参数运行时从批次检验来（§4.5）；4 form；编码用量基准 |
| ④ 允差 | `tolerances` | 拆包/配料/余料允差（各 lower/upper/type）—— GMP 投料偏差 |
| ⑤ 等效边界 | `allowedMembers` | 仅当 target=主物料（§4.7）：允许成员清单（默认全部或显式批准） |
| ⑥ 投料来源与制备（维度1） | `supplyMode` + `preparationRequirement` | `supplyMode`=称量中心(含扫码代称/整包装领用) / 配液中心 / 前置工序；制备需求按来源——称量需求/配液需求/前置步骤引用。（**设备自动进料(OT)暂不纳入**；公用介质如工艺用水、气体为连续供给，特殊另议） |
| ⑦ 批次选择与绑定（维度2·**核心配置**） | `batchBindingRule` | `bindingMode`=同批次 / **跨批次** / 共享池 / 库存策略；`selectionStrategy`=FIFO/FEFO/指定/手动；`allowMultiBatchMerge`；`sourceStepRef`（前置工序时）。**默认按来源**：前置工序→同批次、称量/配液→库存(FEFO)；跨批次/共享池需显式配 |
| ⑧ 投料方式（维度3） | `chargeMethod` | 单批 / 多批合并 / 分次投料 |
| ⑨ 顺序/时机 | `sequence`/`timing` | 步骤内投料顺序与时机 |

#### StepOutput（步骤产出 · 预期）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `stepId`→Step；`outputMaterialId` | 产出物料定义（中间品/成品），**具体绑定，无替代** |
| ② 预期量 | `expectedQuantity`/`type`/`unitId` | |
| ③ 收率 | `yieldRate` + `yieldBounds`(min/max) | 喂 BOM 收率计算 + 工艺优化分析 |
| ④ 产出类型 | `outputType` | 中间品/成品（或由物料定义 `categoryType` 推导） |

#### 关键设计点
1. **StepInput 引用分两种**：主物料（原辅料，配 `allowedMembers`，可供应商替代）vs 具体定义（中间品，直接绑定，无替代）——产出中间品是自产的、无供应商选择。
2. **理论 vs 实际分离**：`StepInput`/`StepOutput`=模板（理论/预期）→ 执行落地为 `MaterialCharge`/`MaterialOutput`（实际）。
3. **三不单列**（避免重复）：成员等效规则→在成员物料（§4.5/4.7 选项1）；折干折纯→就是 ③ 批次折算；用量基准→编码在 `batchConversionRule`。
4. **投料来源是多态的**（不限于称量）：固体原辅料→**称量中心**、液体/需配制料→**配液中心**、中间品→**前置工序产出**。`supplyMode` + `preparationRequirement` 取代单一的"称量需求"。**称量中心 / 配液中心是并列的供给中心**（对接边界见 §7；现有 `MaterialOperationTypeEnum` 已含"配液量取/配液投入/配液产出"，佐证此分类）。
5. **投料三正交维度**：① 来源（称量/配液/前置）② **批次绑定**（同批次/跨批次/共享池/库存）③ 方式（单批/合并/分次）。其中**批次绑定是核心配置**——前置工序默认"同批次"自动绑定（内联流转零配置），但中间品可**跨生产批次**（暂存中间品跨批用）或走**共享池**（buffer/培养基），需显式配 `bindingMode=CROSS_BATCH/SHARED_POOL`。理论规则挂 `StepInput`，实际批次记录挂 `MaterialCharge`。

### 6.3 物料实例（MaterialLot / MaterialUnit · 运行时实例）

> 物料定义的**运行时实例**——"哪批、多少、什么状态、放哪、何时到期、检验值、从哪来"。两级：批次（共享属性）→ 件（细分）。是 §6.4 执行层投料/产出记录的操作对象，也是追溯的实际载体。

#### MaterialLot（物料批次 · 三级码第 2 级）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `materialDefinitionId`→成员物料；`lotNo`；`originalLotNo`/`supplierLotNo` | 批号；采购料有供应商批号，中间品无 |
| ② 数量 | `quantity`/`availableQuantity`/`unitId` | 批次总量 / 可用量 |
| ③ 质量状态（GMP 核心） | `qualityStatus` | 待验/合格/不合格/已取样/限制性放行（状态机管控放行） |
| ④ 效期与复检 | `produceDate`/`expiredDate`/`retestDate`/`expireWarningFlag` | 到期/复检/临期 |
| ⑤ 检验值（批次折算参数源，§4.5） | `testResults`(KV: 水分/含量/效价) + `inspectionReportId` | 折算用关键值 denormalized；完整结果引用 LIMS 检验报告 |
| ⑥ 来源 | `sourceType`(采购入库/产出/退料/结转) + `sourceRef` | 批次怎么来的 |
| ⑦ 直接谱系指针 | `producedByStepExecutionId` | 仅中间品：哪个步骤执行产出它（**多跳谱系派生自执行记录**，见设计点 3） |
| ⑧ 位置 | `storageLocationId`→库房/暂存间 | 标准库 / 线边库 |
| ⑨ 扩展 | `lotExpandInfo` / 批次动态字段 |  |

#### MaterialUnit（物料件 · 三级码第 3 级 · 最小包装）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `materialLotId`→批次；`unitNo` | 件属于哪批 + 件号 |
| ② 数量 | `initQuantity`/`availableQuantity`/`consumedQuantity`/`reservedQuantity` | 初始/可用/已消耗/已预订 |
| ③ 容器 | `containerId`/`container` |  |
| ④ 位置 | `materialPositionId`→货位 | 件级位置（比批次更细） |
| ⑤ 状态 | `status`/`signStatus` | 可用/已消耗/已退/已预订；称量签名状态 |
| ⑥ 来源 | `sourceType`(入库/产出/拆包/合并) + `sourceRef` |  |

#### 关键设计点
1. **批次（共享）vs 件（细分）**：批次承载质量/效期/检验/总量；件承载件号/量/货位/容器。一件一码（扫码单位 = 三级码第 3 级）。
2. **检验值在批次**（§4.5）——批次折算规则运行时读 `testResults`，印证"两套规则、参数从批次来"。
3. **谱系派生，批次只存直接来源**：`producedByStepExecutionId` 是直接来源指针；**多跳追溯链（父批/子批/成品）派生自执行记录**（`MaterialCharge`/`MaterialOutput`，§6.4），不在批次上冗余 `parentLotIds`——与 §2 核心动作 2（追溯派生而非建模）一致。
4. **质量状态机是 GMP 放行核心**：未放行批次不可投料（执行时校验 `qualityStatus`）。
5. **来源 `sourceType` 印证投料三维度**：采购入库（→称量/配液源）、产出（→前置工序源）、退料/结转（→跨批次源）。

### 6.4 执行层（BatchExecution / StepExecution / MaterialCharge / MaterialOutput · 控制配方落地）

> = S88 **Control Recipe** 落地（辉哥说"缺的那层"）。把模板（§6.2）冻结成"**这一批实际怎么跑**"，记下实际投入产出——**整条追溯链的源头**就在这。理论（§6.2）↔ 实际（本节）。

#### BatchExecution（批次执行 · Control Recipe）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 标识 | `batchNo`；`processVersionId`→工艺版本 | 生产批号 + 执行的工艺版本 |
| ② 配方冻结 | `formulaSnapshot` | **执行时冻结的 StepInput/StepOutput 快照**（Control Recipe = Master 副本，不受后续主配方修改影响） |
| ③ 状态 | `status`(未开始/进行中/完成/中止/放行)；`startTime`/`endTime` | 批次状态机 |
| ④ 批量 | `batchQuantity` | 本批批量（驱动按批量比例的用量） |
| ⑤ 关联 | `productionOrderId`→生产指令单；`equipmentId` | 计划/设备 |

#### StepExecution（步骤执行）

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `batchExecutionId`→BatchExecution；`stepId`→Step | 批次下的步骤执行 |
| ② 状态 | `status`；`startTime`/`endTime`；`operatorId`；`terminalId` | 操作人 + **操作终端**（§4 终端绑定） |

#### MaterialCharge（投料记录 · 实际投入 · 追溯源头）⭐

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `stepExecutionId`；`stepInputId`→StepInput | 对应步骤执行 + 模板投入项（理论） |
| ② 实际投入实例 | `materialLotId`→Lot；`materialUnitId`→件(可选)；`materialDefinitionId`(denormalize) | **实际投了哪个批/件**；denormalize 物料定义为**正追加速** |
| ③ 实际量 | `actualQuantity`；`unitId`；折算过程(theoretical/成员系数/批次系数) | 实际投料量 + 折算留痕（GMP 审计） |
| ④ 来源 | `supplyMode`(称量/配液/前置)；`supplyRef`(称量任务/配液任务/前置 StepExecution) | 实际从哪来 |
| ⑤ GMP 元数据 | `chargeTime`/`operatorId`/`terminalId` | 投料时间/人/终端 |

#### MaterialOutput（产出记录 · 实际产出 · 触发新批次）⭐

| 能力 | 字段 | 说明 |
|---|---|---|
| ① 归属 | `stepExecutionId`；`stepOutputId`→StepOutput | 对应步骤执行 + 模板产出项（预期） |
| ② 产出实例 | `materialLotId`→Lot(新建/追加)；`materialDefinitionId` | **产出哪个批次**（这次产出创建/追加的 lot） |
| ③ 实际量 | `actualQuantity`；`yieldRate`(实际收率) | 实际产出量 |
| ④ GMP 元数据 | `outputTime`/`operatorId`；赋码信息 | 新批次批号生成 |

#### 追溯链落地（§4.9 两个方向）

- **反追（产品→物料）**：`MaterialOutput.lot` → `stepExecution` → 该执行的 `MaterialCharge` → 投入 lot → 递归
- **正追（物料→批次）**：`MaterialCharge.materialDefinitionId/lot` → `stepExecution` → `BatchExecution` → 成品
- **谱系边** = （StepExecution 的 MaterialCharge 投入集）→（其 MaterialOutput 产出）——lot 是节点、执行记录是边（§6.3 设计点 3）

#### 关键设计点
1. **BatchExecution = Control Recipe**：冻结配方快照，辉哥要补的层；执行不受后续主配方改动影响。
2. **MaterialCharge/MaterialOutput 是追溯唯一源头**：记实际 lot + denormalize 物料定义（正追加速）+ 折算留痕（GMP）。
3. **理论↔实际配对**：`StepInput`↔`MaterialCharge`、`StepOutput`↔`MaterialOutput`。
4. **多批合并**：一个 `StepInput` 可对应多条 `MaterialCharge`（每批一条）。

---

## 7. 尚未讨论 / 待确认（TODO）

### 已完成 ✅
- ~~a. 等效边界~~ → §4.7（配方层控制）
- ~~c. 主物料本体~~ → §4.8（独立实体）
- ~~d. 追溯粒度~~ → §4.9（成员+供应商+批次；两个追溯方向已列为验收标准）
- ~~字段级 StepInput / StepOutput~~ → §6.2
- ~~字段级 MaterialLot / MaterialUnit~~ → §6.3
- ~~字段级 MaterialCharge / MaterialOutput + BatchExecution / StepExecution~~ → §6.4
- ~~谱系派生机制~~ → §6.3 设计点 3 + §6.4 追溯链落地

### 仍待办（集成 / 迁移层，非核心域）
- **边界接口**：platform（主数据/审计）、wms（仓库）、lims（检验值回写批次）、称量/配液中心
- **迁移意识**：与现有代码（`bm_material`/`bm_product_formula`/`bm_process`/`bm_material_trace_template` 等）的映射与缺口
- **BOM 派生**：从 `StepInput`/`StepOutput` 派生 BOM 喂称量中心的细节
- **赋码规则**：归 platform 还是 MES 本地可配置（待定）
- **回收料 / 退料再投**：边缘投料场景（可在现有模型内表达，待细化）
- **设备自动进料（OT）**：暂不纳入，未来扩展

---

## 8. 关键参考

- 现状代码探查：`packages/backend/services/mes`（`product`/`storage`/`formula`/`process`/`ingredient`/`trace` 子域）
- [OPC 10030 §8.4 Material Information](https://reference.opcfoundation.org/specs/OPC-10030/8.4)
- [OPC 10030 §4.2.4 Modelling Approach of ISA-95](https://reference.opcfoundation.org/specs/OPC-10030/4.2.4)
- [Rhize: Material Models in ISA-95](https://docs.rhize.com/isa-95/resources/material/)
- [ANSI/ISA-95.00.04-2018 Part 4 目录](https://www.isa.org/getmedia/802388c8-0b4f-4420-9cd7-18e7e69db7a3/ISA-95-00-04-2018_toc.pdf)

---

## 9. 标准合规对照（S88 / S95 架构校验）

> 本节从架构 + ISA-88(S88) / ISA-95 角度系统校验设计。标注 ✅ 合规 / ⚠️ 需说明范围。

### 9.1 ISA-95（物料域）— 强对齐 ✅

| 设计元素 | ISA-95 对应 | 状态 |
|---|---|---|
| 主物料/成员物料/批次/件 四级 | Material Class / Definition / Lot / Sublot | ✅ 直接映射（§4.1） |
| `StepInput` 引用主物料(Class) | 引用层级机制 → 结构上允许替代 | ✅（§4.2 / §4.7） |
| 成员等效规则 / 批次检验值 | Property（一等对象，挂 Definition / Lot） | ✅（§4.5 / §4.6） |
| 检验值回写批次 | Material Test Specification → Material Lot Property | ✅（现有 `hydration`/`noHydrationContent` 已是 Lot Property） |
| `qualityStandardId`（质量标准引用，§6 ⑥） | Material Test Specification（Definition 级：检验项+限度） | ✅ 放行判定依据 |
| `MaterialPropertyProfile`（属性模板，§6 ⑪） | Material Class Property 集，Definition 继承 | ✅ 化药/生物药/中药场景扩展的标准放法 |

### 9.2 ISA-88（配方/批次域）— 对齐，两处需说明范围 ⚠️

| 设计元素 | ISA-88 对应 | 状态 |
|---|---|---|
| `StepInput`/`StepOutput`（物料长在步骤） | Recipe 的 Formula（Input/Output）；物料投入控制在 Operation/Phase 级 | ✅ **强对齐**——S88 本就把物料投入绑定在 Operation/Phase，"步骤即配方"正是这一层级，不是另起炉灶 |
| `BatchExecution`（批次执行） | Control Recipe 的执行 = Batch | ✅ 补上了辉哥说的"缺控制配方层"（§3） |
| 工艺 / 工序 / 步骤 | S88 Procedural 模型：Procedure → Unit Procedure → Operation → Phase | ⚠️ 命名是中文习惯（非严格 S88 术语）；**映射**：步骤 Step ≈ Operation/Phase，工序 Procedure ≈ Unit Procedure。字段下钻时补正式映射 |
| 配方四层（General / Site / Master / Control） | S88 定义 4 层 recipe | ⚠️ **本设计只做 Master（工艺模板）+ Control（BatchExecution）两层**；General/Site（跨工厂/企业级）暂不做——单工厂 MES 通常 Master+Control 足够，留作未来扩展 |
| 流程行业（工序重叠/提前开始，§1 决定 A） | S88 Part 1 同时定义 Batch / Continuous / Semi-continuous | ✅ 用 S88 的 Continuous/Semi-continuous 概念表达流程行业，**不是违反 S88**，是其连续模式的落地 |

### 9.3 架构一致性校验

- ✅ 四层（定义/模板/执行/谱系）职责清晰、单向流转，无环、无悬空引用；
- ✅ 两个核心动作（配方并入步骤、追溯派生）内部一致，全文无矛盾；
- ✅ 折算链（§5）= 两套规则（§4.5）+ 边界vs计算分工（§4.7）+ 计算归属（§4.7 选项1）相互吻合，无冲突；
- ✅ 追溯粒度（§4.9）与投料记录（§3 `MaterialCharge` 记成员+批次）一致；
- ✅ §6 字段模型已重建（核心 universal 字段 + Profile 驱动扩展 + 补齐质量标准/复检期/运输/安全），待用户确认；与 §4 决策一致。

### 9.4 已识别的合规风险 / 待办（非缺陷，已知范围）

1. **配方只到 Master+Control**：若未来多工厂/集团部署，需补 General/Site recipe 层；
2. **步骤命名非严格 S88**：字段下钻时补 Procedural 模型（Operation/Phase）正式映射；
3. **检验 → Lot Property 的 LIMS 接口**：数据流正确（§4.5 批次折算参数从检验来），但接口本身待设计（§7 边界 TODO）；
4. **赋码规则**（物料码/批次码/件码）的可配置归属（platform vs MES 本地）未定（§7 TODO）。

### 9.5 校验结论

**物料域（ISA-95）完全踩在标准上；配方/批次域（ISA-88）结构对齐，两处为"已知范围限制"（不做 General/Site、步骤用中文命名），非合规缺陷。** 整体设计经得起 S88/S95 + 架构角度的复核。
