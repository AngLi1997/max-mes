---
title: LIMS 服务总览
created: 2026-06-30
updated: 2026-06-30
type: entity
service: lims
tags: [backend, lims, module, mybatis, feign]
sources:
  - packages/backend/services/lims/bmos-lims2-server/src/main/java/com/bmos/lims2/server/
  - packages/backend/services/lims/bmos-lims2-feign/src/main/java/com/bmos/lims2/feign/
  - packages/backend/services/lims/bmos-lims2-common/src/main/java/com/bmos/lims2/common/
  - packages/backend/services/lims/bmos-lims2-web/src/main/java/com/bmos/lims2/web/BmosLims2Application.java
  - packages/backend/services/lims/bmos-lims2-server/src/main/java/Lic.java
  - packages/backend/services/lims/bmos-lims2-web/src/main/resources/license.xml
status: active
---

# LIMS 服务总览

## 概述 / 职责

**lims 是实验室信息管理服务**,覆盖检验方案/检验单/样本/录入/留样、稳定性考察、ELN 电子实验记录、报告生成、审批流。是体量仅次于 mes 的第二大服务,与 mes/wms 构成**检验三方联动**。

- 端口:**61001** ｜ Nacos 注册名:`bmos-lims2-service` ｜ context-path:`/api/app/lims2`⚠️
- 启动类:`com.bmos.lims2.web.BmosLims2Application`
- 规模:1463 Java / **63 Controller** / ~84 Service / 93 Mapper / **88 张表**(78 `lm_` + 复用 `bm_`/`bp_`)
- bmos.version:**1.15.2-SNAPSHOT**(各服务中最新)

> ⚠️ **命名分裂**(最易踩坑):Maven `groupId=com.bmos.lims` / `artifactId=bmos-lims2` / **Java 包 `com.bmos.lims2.*`** / 服务名 `bmos-lims2-service` / context-path `/lims2` —— 五层命名不一致。**端点统一 `/api/app/lims2/`(不是 `/lims/`)**。

## Maven 模块结构

```
lims/
├── bmos-lims2-common/   # 常量 + 48 枚举 + i18n 响应码
├── bmos-lims2-feign/    # ★对外 Feign(被 mes 调用:MesInspectFeign、IssueMaterialFeign)
├── bmos-lims2-server/   # 业务实现(entity/mapper/service/feign 客户端 + 默认包 Lic.java)
└── bmos-lims2-web/      # 启动类 + 63 Controller + license.xml(Aspose)
```

> 是 **`-server` + `-web`** 而非 mes 那种 `-service`。`@MapperScan("com.bmos.lims2.server.**.mapper")`。

## 业务子域(server 模块,按 Java 文件数三层分档)

### 头部(>80 Java,核心业务)
| 子域 | Java | Ctrl | 表 | 职责 |
|---|---|---|---|---|
| **inspect** | 366 | 28 | 36 | 检验方案/单/样本/录入/留样(体量最大,见下) |
| **eln** | 186 | 11 | 12(含 3 共享 bm_) | ELN 电子实验记录/批记录数据 |
| **stability** | 139 | 8 | 15 | 稳定性考察(方案/计划/时间点/样本) |
| **audit** | 97 | 2 | 7 | 审批流引擎(`lm_flow_audit*`,见独有机制) |

### 中部(30~80 Java)
| 子域 | Java | Ctrl | 表 | 职责 |
|---|---|---|---|---|
| **report** | 56 | 2 | 7 | 报告模板/版本/生成任务 |
| **platform** | 41 | 0 | 0 | 调 platform 的 Feign 代理(纯代理层,无业务表) |
| **material** | 37 | 4 | 3 | 检验物料/分类/字段 |
| **operate** | 32 | 3 | 3 | 操作规则(`bm_operate_rule*`,⚠️ bm_ 前缀) |

### 尾部(<30 Java)
| 子域 | Java | Ctrl | 表 | 职责 |
|---|---|---|---|---|
| task | 24 | 1 | 2 | 检验任务/状态历史 |
| config / recordprint / permission / active / util | ≤13 | 0~1 | 0~1 | 配置/打印/权限/license 激活/工具 |

**inspect 子域再分层**(366 文件):scheme 88(方案/版本/判定)· entry 44(录入/数据点)· parameter 41 · retention 36(留样/观察/台账)· sample 30 · order 30(检验单)· document 18 · pack 14 · item 14 · team 13 · query 11 · mes 5(MES 联动)· division 5 · review 3 · receive 3。

> inspect(366 Java)远超 50 文件阈值,**未来值得独立建子页**(本次仅门户 overview)。

## 数据模型(88 张表,前缀 `lm_`)

- **inspect**(36):`lm_inspection_order*` · `lm_inspection_scheme*`(方案/版本/项/参数/判定/取样)· `lm_inspect_item/parameter/method*` · `lm_sample*` · `lm_retention*` · `lm_document_config*` · `lm_task*`
- **eln**(12):`lm_eln_attachment` 等 + **跨服务共享(双写)**:`bm_execute_form_data` / `bm_execute_form_data_annotation` / `bm_batch_record_version`(与 mes 共享)
- **stability**(15):`lm_stability_scheme/version/item/parameter/plan/timepoint*`
- **audit**(7):`lm_flow_audit*`(审批流,见独有机制)
- **report**(7):`lm_report_template/version/generate_task*`
- **命名空间污染**:operate 的 `bm_operate_rule*`、审计 `bm_log_operation`、激活 `bp_active` 均用 `bm_`/`bp_` 前缀但归属 lims。

## 核心 Service 方法(头部子域接口)

| 子域 | Service | 关键方法(分类) |
|---|---|---|
| inspect/order | `InspectionOrderService` | **流转**:`confirmInspectionOrder`/`batchConfirm`/`terminateInspectionOrder`;**CRUD**:`saveInspectionOrder`;**查询**:`getInspectionOrderPage`/`getById`;**PDF**:`generateInspectionOrderPdf` |
| inspect/entry | `InspectionEntryService` | **录入**:`batchSaveEntryRecords`/`batchUpdateJudgment`;**ELN 同步**:`upsertEntryRecordsFromEln`;**流转**:`updateTaskJudgment`/`updateTaskStatus`;**统计**:`countIncompleteAnalysisItems` |
| inspect/scheme | `InspectionSchemeService` | **CRUD**:`saveInspectionScheme`/`saveInspectionSchemeFusion`;**配置增量**:`initInspectionItemsByPackage`/`updateInspectionSchemeItems`;**校验**:`validateJudgmentConfigConsistency` |
| eln/record | `BatchRecordService` | **文件**:`fileUpload`/`recordItemUpload`;**CRUD**:`saveRecord`/`saveSingleItem`;**公式绑定**:`bindExpression`;**查询**:`getRecordPage` |
| stability/scheme | `StabilitySchemeService` | `saveStabilityScheme`/`pageStabilityScheme`/`savePermissions`/`validateJudgmentConfigConsistency` |
| audit | `FlowAuditService` | **流程**:`flowAuditStart`/`flowAuditComplete`/`flowAuditBackToPrev`;**部署**:`checkoutDeployment`/`saveFlowAudit`;**代办**:`queryToDoListByCategory`;详见独有机制 |

## 关键枚举(状态机,`bmos-lims2-common/.../enums/`,共 48 个)

| 枚举 | 完整 code → 含义 |
|---|---|
| `InspectionOrderStatusEnum` | PENDING_CONFIRM 待确认 / CONFIRMED 已确认 / TERMINATED 已终止 / SAMPLE_AUDIT_PENDING 样品待审核 / SAMPLE_AUDIT_REJECTED 审核未过 / COMPLETED 已完成 |
| `TaskStatusEnum`(11 态) | PENDING_ASSIGNMENT 待分配 / RETURN_PENDING_APPROVAL 退回待审批 / PENDING_COMPLETION 待完成 / IN_PROGRESS / TO_REVIEW 待复核 / REVIEW_PASSED / REVIEW_REJECTED / SAMPLE_AUDIT_PENDING / SAMPLE_AUDIT_REJECTED / TERMINATED / COMPLETED |
| `SamplingStatusEnum` | PENDING_SAMPLING / PARTIAL_SAMPLING / SAMPLING_COMPLETED |
| `InspectionSchemeVersionStatusEnum` | EDITING / APPROVING / ACTIVE 生效 / COMPLETED / INACTIVE / VOIDED |
| `ReportLifecycleStatusEnum` | PENDING_CONFIRM / PENDING_APPROVAL / APPROVING / EFFECTIVE / VOIDED |
| `StabilityTimepointTaskStatusEnum` | NOT_STARTED / WAITING_SAMPLE / IN_PROGRESS / COMPLETED / TERMINATED |
| `InspectionOrderSourceEnum` | REGULAR 常规请验 / STABILITY 稳定性考察 |
| `FlowStateEnum`(实例态) | 1=ACTIVE 审批中 / 2=BACK_TO_PREV 退回 / 4=COMPLETE 通过 / 5=APPROVE_REJECT 不通过 |
| `AuditCategoryCodeEnum`(审批分类→流程 code) | SCHEME_AUDIT `120020002` / STABILITY_SCHEME_AUDIT `120020003` / SAMPLE_AUDIT `120020010` / REPORT_AUDIT `120020020` / METHOD_AUDIT `120020030` / OPERATE_RULE_* 等 |

> 另有 `FlowAuditCodeEnum`(节点规则:会签/或签/策略)、`FlowAuditStateEnum`(版本状态 1 编辑/2 生效/3 失效)等。

## Controller(63 个,按子域分组)

`eln`(11):`/record`·`/record/manage`·`/record/expression`·`/record/audit`·`/signature`·`/mobile/signature`·`/dSignature`·`/app/conclusion`·`/app/eln/entry`·`/app/task`·`/app/eln/pic` ｜ `inspect`(28):`/inspect/order`·`/inspection-scheme*`·`/inspect/item|method|parameter|sampling|query|trend`·`/sample*`·`/sample-audit`·`/sample-receive`·`/retention-*`·`/division`·`/team`·`/document/config`·`/mes/inspect` ｜ `stability`(7):`/stability-*` ｜ `report`(2)·`operate`(3)·`material`(4)·`task`(1)·`audit`(2)·`permission`(1)·`recordprint`(1)·`active`(1)·`unit`(1)

## 独有机制

### 审批流引擎(`audit/` + `bmos-audit-engine-starter`)
- `lm_flow_audit*` 表:FlowAudit(实例)/FlowAuditVersion(版本,FlowAuditStateEnum)/FlowAuditCategory(分类树,AuditCategoryCodeEnum)/FlowAuditProcess(部署绑定 categoryCode→processId)/FlowAuditMessage/FlowAuditUser。
- 编排 Service `FlowAuditService`:业务(方案/样品/报告/方法/操作规程/稳定性)发起审批 → 按 `AuditCategoryCodeEnum.code` 在 `lm_flow_audit_process` 找绑定流程 → 调引擎 `flowAuditStart` → 节点流转用 `FlowAuditCodeEnum`(会签/或签)→ 实例状态用 `FlowStateEnum`。
- 扩展点:`MesAuditExceptionHandler`(捕引擎异常)、`UserTaskPayloadValidateRule`(节点校验)。

### 定时任务(XxlJob,仅 1 个,无 MQ)
- `stability/plan/scheduler/StabilityScheduler.java`:`@XxlJob("stabilityTriggerDueTimepointTasks")` 每天 02:00,把 `planned_date ≤ 今日` 且 `NOT_STARTED` 的时间点任务创建为检验单。
- lims **不使用 MQ/事件总线**,跨服务全走同步 Feign。

### 文档转换与 Aspose license(`license.xml`)
- lims 深度依赖 **Aspose.Words** 做 DOCX↔PDF 转换:检验单 PDF(`InspectionOrderServiceImpl`)、报告生成(`ReportDocTemplateProcessorImpl`)、批记录打印(`BatchRecordServiceImpl`/`RecordPrintServiceImpl`)、水印。
- `license.xml` 是 **Aspose.Total 商业授权**(去水印/解除试用,SubscriptionExpiry=20991231),加载于 `DocxToPdfConverter`/`AsposeInspectionOrderPdfBuilder.ensureLicense()` 等。⚠️ `PlanArchiveServiceImpl` 硬编码 `D://test/license.xml`(调试残留)。

## Feign 三方联动(mes / wms)

**暴露(bmos-lims2-feign,被 mes 调)**:`MesInspectFeign`(`/mes/inspect/document-config` GET、`/schemes` GET、`/order` POST 建单、`/order/retry` POST),Provider `MesInspectProviderController` 委托 `MesInspectAdapterService`。

**回调 mes/wms(检验完成回传)**:
| Client | 目标 | 端点 | 触发 |
|---|---|---|---|
| `MesInspectCallbackClient` | mes | `/feign/inspect/callback`、`/reject` | 样品审核通过时 |
| `WmsInspectCallbackClient` | wms | `/feign/inspect/callback`、`/reject` | 样品审核通过时 |

> **路由选择**:`SampleAuditServiceImpl` 按 `lm_inspection_order.source_system` 决定回调方 —— `WMS` 走 wms client,否则走 mes client。

**调 platform**:物料/单位/参数/表达式/用户/编码 6 个 Feign。联动链路:mes/wms 发起请验 → lims 检验 → lims 按 source_system 回调。详见 [[service-integration]]。

## 隐藏地雷 ⚠️

1. **历史 TSD 加密(已解密)**:全 lims `.java` 曾被 `%TSD-Header-###%` 头加密(ripgrep 零命中),**2026-06-30 已解密可直接读**。见 [[monorepo-architecture]]。
2. **与 mes 共享表双写**:`bm_execute_form_data*` / `bm_batch_record_version` 表名属 mes 命名空间,但 lims 的 eln 子域也读写(`server/eln/entry/entity/ExecuteFormData.java`),存在一致性风险。
3. **`Lic.java` 是离线激活码生成器(非运行时校验器)**:位于默认包根(`src/main/java/Lic.java`),用 Hutool RSA + 硬编码公钥生成激活码,**运行时不调用**(示例 applicationName=`bmos-wms-service`、mac 硬编码,疑似从 wms 拷来的脚手架残留)。运行时激活走 `ActiveService`→`ActiveValidFeignClient`→平台,私钥在平台侧。
4. **`license.xml` ≠ 产品激活**:是 Aspose.Words 商业授权(去水印),与激活无关(见独有机制)。
5. **`spring-boot-starter-web-services`(Spring-WS/SOAP)悬空**:全 lims 无 `@Endpoint`/`@WebMethod` 等真实 SOAP 端点,可安全移除。
6. **`allow-circular-references: true`**:循环依赖技术债。
7. **88 表 vs 口述 92**:迁移建表 88 张为准确值。
8. **migration 编号不连续**:V1.1.1 跳过 0.0.25/0.0.43/0.0.59,Flyway baseline 注意。

## AI 定位提示

- 改 **检验单/方案** → `server/inspect/order|scheme/`(状态键 InspectionOrderStatusEnum / InspectionSchemeVersionStatusEnum)
- 改 **检验任务流转** → `server/task/` + `TaskStatusEnum`(11 态)
- 改 **审批流** → `server/audit/FlowAuditService` + `AuditCategoryCodeEnum`(分类→流程 code)
- 排查 **检验三方联动** → `MesInspectFeign`(入口)/ `MesInspectCallbackClient`+`WmsInspectCallbackClient`(按 source_system 路由回调)
- 改 **稳定性考察** → `server/stability/`(含 XxlJob `StabilityScheduler`)
- 改 **报告/检验单 PDF** → `server/report/` + Aspose 转换(`license.xml`)
- 改 **ELN/批记录数据** → `server/eln/`(⚠️ 共享 `bm_` 表,mes 也写)
- ⚠️ 包名 `com.bmos.lims2.*`,路径 `/api/app/lims2/`

## 相关页面

- [[service-overview]] — 端口/规模速查(lims 模块 -server/-web 命名差异)
- [[database-schema-overview]] — `lm_*` 表分组(88 张)
- [[service-integration]] — 检验三方联动 Feign 矩阵
- [[mes-overview]] — 共享表主写方、请验发起方
- [[wms-overview]] — 检验回传另一端(WMS 走 wms client)
- [[monorepo-architecture]] — TSD 加密历史、lims2 命名分裂、版本不一致
