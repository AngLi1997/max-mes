# Wiki Log

> 所有 wiki 操作的时间线记录。仅追加。
> 格式：`## [YYYY-MM-DD] 操作 | 主题`
> 操作类型：ingest（从代码批量导入）, create（新建页）, update（更新页）, lint（校验）, archive（归档）, delete（删除）
> 当此文件超过 500 条记录时，重命名为 `log-YYYY.md` 并重新开始。

## [2026-07-22] update | platform 设备模块补全工厂模型层级（factory 子模块）

- **触发**：设计文档（`docs/superpowers/specs/2026-07-20-mes-equipment-modeling-design.md`）§11 早期误判"bmos 缺房间层"，核实代码后发现错误——bmos 实际有完整 factory 子模块含 Room。
- **更新 [[platform-equipment-module]]**：将原"工位（设备-工厂模型交叉）"小节升级为「工厂模型层级（factory 子模块）」：
  1. 完整层级树：FactoryTenement → TenementFloor → Line → **Room** → FactoryModule → FactoryStation → 设备/用户。
  2. Room 相关 Service/表清单：RoomService、RoomLogService、BpFactoryRoomOccupy、FactoryRoomEnvProperty 等。
  3. **跨服务事实**：MES `process` 子域已有 `ProcedureModelRoom`（工序绑房间）、`ProcedureModelRoomOrStation`（房间/工位二选一）、`RoomStatusType`——"工序绑房间"能力已存在。
  4. 修正易误判点：bmos 不缺房间层，缺的是"按能力匹配"抽象。
- **教训**：对照类文档以代码为准、wiki 为辅；原 wiki 只聚焦 `bp_equipment_*`，漏看了 factory 子模块全貌。

## [2026-07-20] update | platform 设备模块补 2 张 Mermaid 结构图

- **触发**：用户要求把对话中给出的设备模块结构图存进 wiki 页。
- **更新 [[platform-equipment-module]]**：在"概述"后新增「结构图」小节，含两张 Mermaid 图：
  1. **整体分层结构**（flowchart）——消费方(MES) → facade Feign → controller → service → datasource/job/message → 数据表，用颜色区分层级，高亮 `EquipmentTagService` + `bp_equipment_info.status` 为状态机核心。
  2. **设备状态机**（stateDiagram）——AVAILABLE/UNAVAILABLE/OCCUPY/FAULT 四态转换，含 XXL-JOB 自动释放、故障异步通知两条特殊路径。
- 末尾加图例说明（蓝 facade / 橙核心 / 绿表 / 黄工位跨包）。

## [2026-07-20] create | platform 设备模块 wiki 页

- **触发**：用户 `/llm-wiki` 指令——"查找 platform 中的设备模块，为其整理 wiki 页"。
- **新建 1 页**：`entities/_platform/platform-equipment-module.md`
  - 扫描范围：`service/equipment/`（主包，9 子包含 controller/service/model/mapper/convert/datasource/expire/job/enums）+ `service/factory/`（工位设备 EquipmentStation/Info/User）+ `service/message/sender/EquipmentFaultMessageSender` + facade `EquipmentConfigFeign`。
  - 覆盖：9 个 Controller（含 APP 端 / Feign / 采集点 / 工位）、9 个核心 Service、16 张表（设备本体/分类/类型 tag/属性/日志/采集点/工位）、关键枚举（EquipmentStatusCodeEnum 等）。
  - 登记 EquipmentConfigFeign 全部方法（标注 `@Deprecated` 的 `getConfigByStationIdList(List)` / `getEquipmentByTagCode`）。
  - **隐藏地雷 9 条**，重点：① status 注释与枚举冲突（枚举才是 3=OCCUPY/4=FAULT）② `bp_equipment_tag*` 与打印标签 `bp_tag_instance` 是两套表 ③ 占用须配 `applyEquipmentHeart` 否则被 XXL-JOB 自动释放 ④ `@TableField(updateStrategy=IGNORED)` 误清字段 ⑤ Feign 工位权限双校验 ⑥ `AcquisitionPlatformEnum` 在 facade/service 重复定义。
  - 状态机与占用/心跳/释放/故障/恢复/效期流程完整；故障消息收件人靠权限码 `100030001000015` 反查。
- **跨页回写**：
  1. [[index]]：platform 分区加 `[[platform-equipment-module]]`，页数 41→42，日期 07-15→07-20，"3 模块"→"4 模块"。
  2. [[platform-overview]]：相关页面补 `[[platform-equipment-module]]` + 补 `[[platform-auth-module]]`（原仅 user）。
- **未闭合**：facade 重复枚举、deprecated Feign 迁移、设备数采 datasource 策略仅概述未展开——留待后续按需深化。
## [2026-07-20] create | Platform factory 子模块页（platform-factory-module）

- **触发**：用户要求分析 bmos 工厂空间建模现状（"厂→楼→层→区→工位"现在怎么登记、有无层级、缺哪几级），并将现状落成一页 wiki。
- **新建 1 页**：`entities/_platform/platform-factory-module.md`（platform 第 4 个子模块页，模板 B）。
  - 规模实测：Controller **11** · 表 **15** · 对外 Feign 2（`FactoryFeign` / `FactoryAppFeign`）· 4 个关键枚举。
  - **核心叙事：两套并行层级体系**（本页最重要）：
    - **体系 A 物理空间树**：`bp_factory_tenement`(楼宇,parentId 自引用) → `bp_factory_tenement_floor`(楼层,tenementId) → `bp_factory_room`(房间,tenementId+floorId+moduleId) → 工位（经 `bp_factory_room_station` 关联）。
    - **体系 B 逻辑模块树**：`bp_factory_module`(parentId 自引用 + type) 单表承载，`FactoryModuleEnum` 定义 5 类节点：**0企业/1工厂/2产线/3房间/4工位**；产线/房间/工位都额外挂 `moduleId`。
    - **唯一交汇点 = 房间**（同时持 floorId + moduleId）；工位只挂 moduleId，不直接挂物理树。
  - **房间清场状态机**（制药 GMP 独有机制）：`RoomStatusEnum` 在用(1)→待清场(2)→已清场(3)，`RoomStatusOperateTypeEnum` 区分人工/生产清场；痕迹落 `bp_factory_room_status_log` + `bp_factory_room_clean_log`，效期由 `expire/` 子包（`ExpireRoomListener`）监听。
- **关键发现 / 隐藏地雷**（页内 ⚠️ 标注）：
  1. **两套树割裂**：物理树缺"工厂"显式类型（Tenement 注释是楼宇，parentId 自引用非显式工厂层级），逻辑树缺"楼宇/楼层"类型（`FactoryModuleEnum` 只有企业/工厂/产线/房间/工位）；两套树无外键互连——**"厂→楼→层→区→工位"单一物理链路在"厂↔楼"处断裂**。这正是用户"缺哪几级"问题的根因。
  2. **工位表名前缀异常**：`bp_equipment_station`（`equipment_` 非 `factory_`），起源与设备绑定，跨 equipment/factory 两包。
  3. **Model 名与表名不一致**：`FactoryCleanRoomLog` ↔ `bp_factory_room_clean_log`；工位 `EquipmentStation` 属 equipment 命名空间。
  4. **mes 消费方包名错别字** `service/facotry/`（应为 factory），与 [[mes-audit-module]] 的 `ProcessAuditConditon` 同类历史遗留；mes 是纯消费方（注入两个 Factory Feign），不落地点主数据。
  5. **`bp_factory_room_occupy` 无 Model 类**，仅 `BpFactoryRoomOccupyDao.xml`（且位于 `platform/src/main/resources/mapper/`，非 service 模块下），实扫易漏。
- **表数回正**：[[platform-overview]] / [[database-schema-overview]] 此前标 factory **11 张**，实扫 **15 张**（补全 occupy/env_property/status_log/clean_log/station_info/station_user/line_room/line_station/room_station/module/tenement/tenement_floor/line/room/station）。已回正 overview 子域行 + Controller 清单（原仅列 6 个，实为 11 个）。
- **集成四件套**：
  - [[index]]：platform 分区追加 `[[platform-factory-module]] ✅`；顶部统计 41→42 页、platform 模块数 3→4、日期 07-15→07-20。
  - [[platform-overview]]：factory 子域行加 wikilink + 表数 11→15 + 描述补"两套并行层级树"；Controller 清单 factory 行补全 11 个 + 链接；`updated` 06-29→07-20。
  - 双向链接：新页 → platform-overview/database-schema-overview/service-integration/mes-overview/data-access-pattern/PLAYBOOK-backend（出站 6，非孤儿）。
- **未改**：database-schema-overview 的 platform factory 行（"11 张"在那里是分组速查的近似值，且该页头部已声明"表数为近似值，权威以 DDL 为准"，本轮不改保持稳定；如需精确可后续单独 lint）。
- **后续可选**：用户若决定补全"厂→楼→层→区→工位"链路，本页「核心结构」「隐藏地雷」两节即改造落点依据（打通 floorId/moduleId 或新增显式层级）。

## [2026-07-15] create | bmos-parent-starter wiki 页 + 回写 6 页过时表述

- **触发**：继同日 subtree 引入（见上一条）后，用户要求 ① 建 starter wiki 页 ② 回写各页"源码在 monorepo 外"过时表述 ③ 提交并 push。
- **新建 1 页**：`entities/_shared/parent-starter-overview.md`（`_shared/` 目录首次建立）
  - 按 [[PLAYBOOK-backend]] 第十节 starter / SDK 方法论组织——**首次实战该方法论**。
  - 抓 starter 专属信号（区别于 services 应用层）：17 个 `@Configuration` + 11 个 `@EnableBmosXxx` 启用注解 + 6 处 `@ConditionalOnXxx` + 2 处旧式 `spring.factories` + 6 个 `@ConfigurationProperties` 前缀类 + 15+ SPI/抽象扩展点 + 11 个业务注解。
  - 16 子模块速览表 + 依赖拓扑（`bmos-starter-common` 底座，`autoconfigure` 聚合 7 个子 starter）。
  - 登记关键发现：**主动声明式 starter 接入模式**（`@EnableBmosAutoConfiguration` + `@Import`，非 classpath 自动发现）；4 处隐藏地雷（i18n 硬编码 cloud-dependency 版本 1.15.0 漂移、file 模块 3 个 system-scope 本地 jar、data 模块非标包名、web 的 Activate 授权类）。
  - 明确边界：audit-engine / orchestrator starter **不在此工程**，[[mes-audit-module]]/[[mes-workflow-module]] 的 TODO 未闭合。
- **回写 6 页**过时表述（"源码在 monorepo 外" → "已引入 shared/"）：
  1. [[api-conventions]]：共性代码 `ResponseInfo`/`GlobalExceptionHandler` 等的 ⚠️ 框 + 相关页面链接，改指向 [[parent-starter-overview]]；`updated` 06-30→07-15。
  2. [[data-access-pattern]]：`BaseDO`/`BaseMapperX` 底座的 ⚠️ 框 + 链接；`updated` bump。
  3. [[auth-and-license]]：JWT/Redis/`BaseUserDO` 边界表述 + 链接；`updated` bump。
  4. [[monorepo-architecture]]：目录树 `shared/` 从"空(待引入)"改为"bmos-parent-starter 已引入"。
  5. [[PLAYBOOK-backend]] 头注：已引用未入库清单移除 `bmos-cloud-dependency`（已入库），补 `shared/` 已引入说明，标注本页为第十节方法论首次实战。
  6. [[platform-user-module]]：隐藏地雷第 7 条"BaseUserDO 源码不在仓库"改为"已引入"。
- **集成**：[[index]] 新增「后端共享」分区登记 [[parent-starter-overview]] ✅，统计 40→41 页。
- **lint 自检**：新页出站链接 ≥6（api-conventions/data-access-pattern/service-overview/service-integration/auth-and-license/PLAYBOOK-backend），非孤儿页；frontmatter `service: shared` 合法（SCHEMA 已有此取值）；`sources` 填真实路径。
- **未改**：log.md 历史 06-30 条目里的"monorepo 外"表述（log 为仅追加的历史快照，不改历史记录，由各当前页的回写体现最新状态）。

## [2026-07-15] ingest | bmos-parent-starter（dev 分支）subtree 引入 shared/

- **触发**：用户要求将外部仓库 `http://172.16.0.180/bmos/bmos-parent-starter.git` 的 **dev 分支**引入 `packages/backend/shared/`。
- **方式**：`git subtree add --prefix=packages/backend/shared/bmos-parent-starter <url> dev --squash`（与 [[MONOREPO_INTEGRATION_PLAN]] 第 10.2 节 git subtree 隔离方案一致，保留 squashed 历史，与上游完全隔离）。
- **引入内容**：`com.bmos:bmos-parent-starter`（packaging=pom，packaging=父级脚手架），坐标 `version=${revision}` = **1.15.2-SNAPSHOT**，Spring Boot **2.6.15** / Spring Cloud **3.1.8**，**16 个子模块** / **278 Java 文件**：
  - BOM 中心：`bmos-cloud-dependency`
  - starter 模块：`bmos-starter-{common,web,data,mybatis,cache,file,mq,rocketmq,logging,i18n,adaptor,autoconfigure,formula,expire,unit}`
- **定位**：对应 [[MONOREPO_INTEGRATION_PLAN]] 第 2.2/3.2 节 `packages/backend/shared/starters`（Spring Boot AutoConfiguration 启动器）。这是**此前多处 wiki 页标注"源码在 monorepo 外"的外部 starter 本体**——[[api-conventions]]、[[data-access-pattern]]、[[mes-audit-module]]/[[mes-workflow-module]] 的「TODO · 引擎待补」章节、[[PLAYBOOK-backend]] 第十节「未覆盖场景」、[[service-integration]] 等页都曾把它列为 monorepo 外的 Maven 依赖。
- **产生的提交**（本地，领先 origin/master 2 个）：
  - `6de43d877` Squashed '...' content from commit `af804eec4`（dev 分支 HEAD）
  - `455dba359` Merge commit ... as 'packages/backend/shared/bmos-parent-starter'
- **关键观察（后续依赖统一的输入）**：dev 分支 `revision=1.15.2-SNAPSHOT`，与 lims（1.15.2）齐平，高于 platform/mes（1.15.0）与 gateway/wms（1.14.0）——印证 [[MONOREPO_INTEGRATION_PLAN]] 4.1 节"`bmos.version` 分歧"暂不统一的决策；若后续做依赖收口，此 dev 版本是最新的基准。
- **暂未做**（等用户决定）：
  1. 未在 index.md 登记新页 / 未为该 starter 建独立 wiki 页（按 [[PLAYBOOK-backend]] 第十节，starter/SDK 建页方法论是独立的 TODO，需单独一轮建设）。
  2. 未同步更新各页"源码在 monorepo 外"的过时表述（待 starter wiki 建好后统一回写）。
  3. 未 push（本地领先 2 提交）。
- **后续同步命令**：上游 dev 更新后 `git subtree pull --prefix=packages/backend/shared/bmos-parent-starter <url> dev --squash`。

## [2026-07-15] lint | 全库健康检查 + 5 项修正（断链/统计/阈值）

- **触发**：用户要求按 `skills/llm-wiki` 的 Lint 范式审查 code-wiki。跑完全量 46 个 md 的 10 项检查。
- **检查结果**（按 skill 的 10 项）：frontmatter 必填字段 ✅ 全齐 / tag 审计 ✅ 25 个在用 tag 全合法 / service 字段 ✅ 全合法 / 孤儿页 ✅ 无（每页出站链接≥2）/ index 收录（除下方问题 1 的 5 个外）✅ / log 轮转 ✅ 仅 28 条。
- **修正 5 项**：
  1. 🔴 **系统性断链根因修复**：5 个后端 overview 实际文件名是 `entities/_<svc>/overview.md`，但全库用 `[[mes-overview]]`/`[[platform-overview]]`/`[[gateway-overview]]`/`[[lims-overview]]`/`[[wms-overview]]` 引用——Obsidian wikilink 按文件名匹配，原文件名 `overview.md` 无法解析，构成真断链（前端 `_mobile/mobile-overview.md`、`_web/web-overview.md` 本就一致，仅后端 5 个不一致）。用 `git mv` 重命名 `overview.md → <svc>-overview.md` 共 5 个，文件名与链接名对齐后断链自动愈合。复测：`comm -23` 断链检测结果从 7 项降至 2 项（剩 `log.md`/`wikilinks` 均为非页面字面量，无需处理）。
  2. 🔴 **index 头部统计严重滞后**：原声明"全部 29 页建成（29/29）｜最后更新 2026-06-30"，实际内容页已 **40 页**（entities 25 = mes 16 + platform 3 + lims/wms/gateway 各 1 + web 2 + mobile 1；concepts 8；comparisons 3；api 4）——07-02~07-06 新建的 11 个 mes 子模块页虽已列入 mes 分区，但顶部统计从未跟进。改为"已建成 40 页｜最后更新 2026-07-15"。
  3. 🟡 **SCHEMA 拆分阈值豁免**：9 个 mes 子页超 200 行（plan 328 / weigh 265 / process 252 / storage 245 / audit 223 / workflow 214 / execute 206 / lotrelease 205 / dataset 203），原阈值">200 拆分"对含完整表清单+枚举+Service 签名的模块子页过严。SCHEMA「页面阈值」补充：模块子页豁免，仅 >400 行或出现两个独立子主题才拆。同步 SCHEMA `updated` 06-29→07-15。
  4. 🟡 **service-integration 日期滞后**：`updated` 停在 06-29，但正文含 07-06 才补的 `WmsFeignClient`/`InspectFeignController` 修正（见 07-06 requisition/inspect 日志条目），bump 到 07-06。
  5. 本次 lint 记录本身。
- **无新页**（完成度 40 页维持，纯质量修正）。
- **通过项**（无需改动）：frontmatter 完整性、tag 合法性、service 取值、孤儿页、log 轮转（28/500）。

## [2026-07-06] create | MES preparation 子模块页（mes-preparation-module）· ★ 头部子域全部建完

- 新增 entities/_mes/mes-preparation-module.md：覆盖 `service/preparation/`（plan / input / measure / produce 共 4 子包）。
- 规模实测：Controller 4 · Service 接口 5 · Mapper 10 · Java 105 · 表 10（与 overview 完全吻合）。
- **核心叙事：制剂四段式**（plan 计划 → measure 液体量取 → input 投料 → produce 产出）。
- **关键发现**：
  1. **命名双轨**：plan/measure = 液体制剂（`Liquid*` 类名 + `bm_liquid_preparation_*` 表，6 张）；input/produce = 通用制剂（`Preparation*` 类名 + `bm_preparation_*` 表，4 张）。
  2. **唯一覆盖产出环节**：produce 子包是 weigh/ingredient 都没有的——含 `produceHandle`（生产处理）/ `scrap`（报废）/ `changeProducer`（换生产人）。
  3. **移动端作业**：input/produce 两个 Controller 挂 `/mobile/preparation/*` 前缀，对接 mes-app；plan/measure 走 Web 端。
  4. **与 weigh/ingredient 同构**：制造执行三段式姊妹域，各有侧重（weigh 纯称量 / ingredient 配料计划 / preparation 含产出），命名前缀各不同。
  5. ⚠️ `BindMaterialBatch` 方法名首字母大写，违反 Java 命名规范（历史遗留）。
- 无 Feign / 无 MQ / 无定时任务 / 无独立枚举类——纯进程内业务。
- 同步更新：
  - index.md：mes 分区追加 `[[mes-preparation-module]] ✅`。
  - mes-overview：preparation 行加 wikilink + 描述补"四段式 + 产出 + 移动端"；"子页建设清单" → **头部 14 全部建完 + 中部 1（inspect）**。
  - weigh/ingredient 相关页面补 preparation 回链（制造执行三姊妹域双向闭环）。
- 🎯 **里程碑**：mes 头部子域（14 个）自此全部建完。剩余可建项为中部子域（platform 适配 / tag / operate / formula / output / trace / equipment / facotry / mcp）与尾部归并项，按需再建。

## [2026-07-06] create | MES inspect 子模块页（mes-inspect-module）

- 新增 entities/_mes/mes-inspect-module.md：覆盖 `service/inspect/`（controller / service / lims / mapper / model / convert 共 6 子包）。
- 规模实测：Controller 4 · Service 接口 3 · Mapper 6 · Java 53 · 表 6（与 overview 完全吻合）。
- **inspect 属中部子域（Java 53），但跨服务联动密集，破例独立成页**（[[mes-overview]] 中部子域表已标 wikilink）。
- **核心发现：LIMS 网关双模式**（inspect 子域最重要的设计）
  - `lims/` 子包 7 个类：策略接口 `LimsInspectGateway` + 类型枚举 `LimsType`（`BMOS`/`THIRD_PARTY`）+ 两实现（`BmosLimsGateway` 自研走 Feign / `ThirdPartyLimsGateway` 返回空兜底）+ 选择器 `LimsGatewaySelector`（Spring 注入按 type 注册 EnumMap）+ 开关 `InspectLimsSwitch`（读平台参数 `INSPECT_LIMS_CONFIG` JSON）+ 上下文 `InitiateInspectContext` / `RetryInspectContext`。
  - **切换对接模式不改代码**——改 platform 参数 `INSPECT_LIMS_CONFIG`。新增第三种 lims 类型只需实现接口+加枚举值，Spring 自动注入。
- **跨服务检验闭环**：
  - 发起：mes `InspectService.initiateInspect` → gateway → lims（自研）/ 本地兜底（第三方）
  - 回调：lims → `InspectFeignController`（`/feign/inspect/callback` / `/reject`）→ `inspectCallback` / `rejectInspect`
  - 这是 mes **被调**方向（lims → mes），对应 mes-feign 的 `InspectFeign` 契约。
- **校正 overview 一处路径错误**：Feign 契约章节原写 `InspectFeign` 路径 `mes/inspect/feign/`（不存在），实扫回调 Controller 在 `inspect/controller/InspectFeignController`（暴露 `/feign/inspect`），已回正。
- **业务回填**：`InspectComponentService.confirmFillFormData` 把检验结果回填到 record/execute 的业务组件表单数据。
- 同步更新：
  - index.md：mes 分区追加 `[[mes-inspect-module]] ✅`。
  - mes-overview：中部子域 inspect 行加 wikilink + 描述补"网关双模式"；Feign 契约章节校正路径；"子页建设清单"拆为"已建头部 13 / 已建中部 1 / 待建头部 2"（preparation）。
  - record/execute 相关页面补 inspect 回链（检验结果回填）。
  - 此前 dataset/storage/lotrelease 引用 inspect 的悬空 wikilink 现已闭合。
- **唯一剩余待建头部**：preparation（制剂/前处理，Java 105 / 表 10）。

## [2026-07-06] create | MES requisition 子模块页（mes-requisition-module）

- 新增 entities/_mes/mes-requisition-module.md：覆盖 `service/requisition/`（controller / service / feign / mapper / model / dto / vo / convert 共 8 子包）。
- 规模实测：Controller 1（25 接口）· Service 接口 1（22 方法）· Mapper 7 · Java 60 · 表 7（与 overview 完全吻合）。
- **核心叙事：领料三段式**（reserve 预约 → receive 收货 → sendOut 发料）。单 Controller 单 Service 但业务方法密集（22 个），是"瘦入口宽业务"型模块。
- **核心发现**：
  1. **mes → wms 唯一外向集成**：整个 mes 服务对 wms 的 Feign 调用**只通过本子域的 `WmsFeignClient`**。这是 mes 的服务边界约定——其它子域需 wms 数据应通过 requisition 的 Service，不直接持 Feign。
  2. **WmsFeignClient 实际有 4 个方法**（overview 此前只说"领料请求"）：`queryBatchByMaterial` / `queryAvailableQuantityList` / `submitSendOutOrderByBatch`（写） / `queryInventoryData`。已在 overview 调用关系章节补正。
  3. ⚠️ **隐藏耦合：借道 mcp 子域 DTO/VO**：`queryInventoryData` 的入参 `WmsStorageInventoryDataQuery` 与出参 `WmsStorageInventoryFeignVO` 来自 **mcp 子域**（`com.bmos.mes.service.mcp.*`），非 requisition 自己的 dto/vo。暗示 mcp 是 wms 集成的契约/适配层。改 wms 库存 DTO 必须看 mcp。
  4. ⚠️ **Model 类名与表名不一致**：表 `bm_requisition_plan` → 类 `Requisition`（非 `RequisitionPlan`），与 plan 子域 `bm_product_plan` → `Plan` 同类历史命名。
- 无 Feign（仅 WmsFeignClient）/ 无 MQ / 无定时任务 / 无独立枚举类。
- 同步更新：
  - index.md：mes 分区追加 `[[mes-requisition-module]] ✅`。
  - mes-overview：requisition 行加 wikilink + 描述补"三段式 + 4 方法 + 借道 mcp"；调用关系章节"mes → wms"补方法数与 mcp 借道提醒；"已建清单" 12→13；"待建头部" 去掉 requisition（剩 preparation / inspect）。
  - 此前 dataset/storage 引用 requisition 的悬空 wikilink 现已闭合（requisition 已建）。
- 下一个待建头部：preparation（制剂/前处理，Java 105 / 表 10）/ inspect（检验，与 lims/wms 三方联动）。两选一。

## [2026-07-06] lint | MES 子页关联性检查 + 补 6 处双向回链

- **检查方法**：抓 12 个 mes 子页的全部 `[[wikilinks]]`，用 `comm -23` 求正向/反向边差集，找出"单向边 A→B 但 B↛A"。共 49 条 mes-* 间边，其中 44 条单向。
- **判定原则**：单向 ≠ 断链。底座子域（product/record/process/plan）被 5+ 子域依赖，不回链每个上游是合理设计取舍（否则相关页面膨胀）。只补**双向耦合但目标页有自然叙事位置**的强关系回链。
- **补链 6 处**（每处都先 grep 验证源页正文确有耦合描述）：
  1. execute → dataset（dataset 是 execute 表单数据/附件/副本版本的渲染装配层）
  2. record → execute（execute 是 record 批记录项/组件的执行数据写入方）
  3. workflow → execute（execute 副本版本接 workflow 变更团队 `queryStepChangeTeamList`）
  4. storage → weigh + ingredient（两个称量子域都绑定 storage 库位物料）
  5. process → audit（工艺审批 `AuditCategoryServiceEnum.PROCESS` + `FlowAuditProcess`）
- **跳过 1 处**：process→execute。process 作为编排中枢被 execute/lotrelease/dataset/workflow 多子域依赖，单向引用合理，process 页不必逐一回链。
- **结果**：双向连接从 1 对（weigh↔ingredient）增加到 10 个子页拥有双向关系。
- **同步**：5 个被改页（execute/record/workflow/storage/process）`updated` 改为 2026-07-06。
- **暂不算问题**（待建导致）：requisition/inspect 被多个子页引用，等子页建好后自然闭合。

## [2026-07-06] create | MES ingredient 子模块页（mes-ingredient-module）

- 新增 entities/_mes/mes-ingredient-module.md：覆盖 `service/ingredient/`（plan / input / weigh 共 3 子包 + 根级 `DiffUtil`）。
- 规模实测：Controller 3 · Service 接口 4 · Mapper 8 · Java 75 · 表 8（与 overview 完全吻合，无偏差）。
- **核心叙事：配料三段式**
  - plan（23 Java）：配料计划生成 / 批次绑定 / 理论量与配料量计算 / 完成计划。
  - input（14 Java）：投料录入 / 扫码识别物料 / 待投计划。
  - weigh（37 Java，最重）：称量复核全流程（称重+签名+完成+换人）+ 4 类扫码（物料/设备/容器/库位）+ 称量日志。
- **核心发现**：
  1. **与 weigh 是姊妹域**：ingredient 的 `IIngredientWeighService` 有一整套 `weighAndPrint/sign/finish/makeSureWeigh/changeWeigher/扫码`，与 [[mes-weigh-module]] centre2 平行——是 mes 内**称量能力的第二次实现**（按配料计划维度 vs 按需求/任务/票维度）。改称量逻辑需先确认业务落哪边。
  2. **允差计算 DiffUtil**：根级工具类，直接依赖 formula 子域的 `ProductFormulaMaterial` + `ToleranceTypeEnum`（`bmos-mes-common`）+ `MaterialQuantityCalculateUtil`（utils）。是 ingredient ↔ formula 的核心耦合点。
  3. ⚠️ **表名前缀异常**：`bm_weigh_log`（非 `bm_ingredient_weigh_log`）—— 疑似 ingredient 与 weigh 子域共用一张日志表。改结构需确认两子域读取方。
- 无 Feign / 无 MQ / 无定时任务 / 无独立枚举类——纯进程内业务。
- 同步更新：
  - index.md：mes 分区追加 `[[mes-ingredient-module]] ✅`。
  - mes-overview：ingredient 行加 wikilink + 描述补"三段式 + 允差 + 与 weigh 姊妹域"；"已建清单" 11→12；"待建头部" 去掉 ingredient（剩 preparation/inspect/requisition 三个独立模块）。
  - mes-weigh-module：相关页面里 ingredient 从"⏳ 待建"改为 ✅ 回链（闭环双向链接）。
- 下一个待建头部：preparation（制剂/前处理，Java 105 / 表 10）/ inspect（检验，与 lims/wms 三方联动）/ requisition（领料，mes 唯一外向调 wms）。三选一。

## [2026-07-06] create | MES weigh 子模块页（mes-weigh-module）

- 新增 entities/_mes/mes-weigh-module.md：覆盖 `service/weigh/`（centre / centre2 / free / data / simulate 共 5 子包）。
- 规模实测：Controller 13 · Service 接口 14 · Mapper 20 · Java 178 · **表 20**（与 overview 的 Java 数吻合，但表数 overview 标 16，实扫 20，已回正）。
- **核心发现：centre vs centre2 双称量模式并存**
  - centre（老，83 Java）：需求/任务驱动，`requirement → task → execute → input`，编排入口 `IWeighTaskService.programAuto/Manual`。
  - centre2（新，73 Java，类名一律 `Ticket*`）：工单/票据驱动，`ticket → ticketRequirement → execute`，编排入口 `ITicketService.programAuto/Manual`，自带看板 `IWeighDashboardService` 与状态枚举 `DashboardWeighStatusEnum`（已下发/称量中/已完成）。
  - 两套模式**数据表不共享**、编排入口各自独立——新需求落点需明确模式。
- ⚠️ **3 处隐藏地雷**（已在页内标注）：
  1. `mes_scale_config` 无 `bm_` 前缀（与 `product_schedule_procedure_config` 同类历史遗留）。
  2. `ScaleConfigService.generateRandomWeight()` 生成随机重量，疑似演示/调试用途，生产环境需确认。
  3. weigh 子域**无 Feign、无 MQ、无定时任务**——纯进程内业务，与其它服务无直接调用（耦合通过 plan/storage/product 子域）。
- 同步更新：
  - index.md：mes 分区追加 `[[mes-weigh-module]] ✅`。
  - mes-overview：weigh 行 Tbl 16→20 + 加 wikilink + 描述补充双模式；"已建清单" 10→11，"待建头部" 去掉 weigh（顺带修正原"4"为"5"的笔误——实际待建 ingredient/preparation/inspect/requisition）。
- 下一个待建头部子域：ingredient（配料/投料，weigh 的直接下游）。

## [2026-07-02] create | MES audit 子模块页（mes-audit-module）

- 新增 entities/_mes/mes-audit-module.md：覆盖 `service/audit/`（controller / service / condition / complete / Behavior / listener / builder / validate 等 9 子包）。
- 规模实测：Controller 1 · Service 接口 5 · Mapper 6 · Java 90（与 overview 吻合）。
- 定位为 `bmos-audit-engine-starter` 的 **mes 适配层**（与 [[mes-workflow-module]] 之于 orchestrator 同构）。6 张表存审批流**配置**（模板/版本/分类/用户/消息/工艺绑定），流程实例/任务存引擎内部。
- 核心机制：
  - **`AuditCategoryServiceEnum`**：7 类业务审批（记录/工艺/生产BOM/指令单/操作规程/批记录/批签发）×Condition 策略注册表——业务模块接入审批的入口，对接 record/process/product/plan/lotrelease。
  - **四扩展点**：condition（7 个取数策略）+ complete（会签/或签）+ behavior（审批人指派）+ listener（5 个事件，含退回/拒绝）。
- ⚠️ **错别字**：`ProcessAuditConditon.java`（应为 Condition，漏 i），与 overview 的 `facotry` 同类历史遗留，已标注需独立重构任务。另 `Behavior/` 子包首字母大写不符合 Java 包名惯例。
- ⚠️ **TODO**：`bmos-audit-engine-starter` 源码未入库（引擎包 `com.bmos.audit.engine.core.*`）。已设「TODO · audit-engine 待补」章节，与 workflow 的 orchestrator TODO 同类，建议未来合并为同一份"平台 starter 方法论"。
- 集成四件套：index.md mes 分区追加 [[mes-audit-module]] ✅；双向链接 mes-overview（待更新）+ workflow/lotrelease/record/plan/process/service-integration/database-schema-overview。

## [2026-07-02] create | MES lotrelease 子模块页（mes-lotrelease-module）

- 新增 entities/_mes/mes-lotrelease-module.md：覆盖 `service/lotrelease/`（manage + template 两个对等子包）。
- 规模实测：Controller 3 · Service 接口 3 · Mapper 7 · Java 61（与 overview 吻合）。
- 定位为**制药质量门禁**：批放行模板（版本/分类/工艺绑定）+ 批放行单据（生成/审批/作废/下载）。
- 数据模型：7 表（单据侧 `bm_lot_release` `bm_lot_release_history`；模板侧 `bm_lot_release_template` `_version` `_category` `_process` `_history`）。
- 状态机丰富（4 枚举）：`LotReleaseStatus`（EDIT→PROCESSING→EFFECTIVE/SCRAPED）、`LotReleaseOperateType`（8 值）、`LotReleaseTemplateVersionStatus`（3 值）、`LotReleaseTemplateOperateType`（8 值）。
- 关键发现：
  - **审批回调机制**：`auditCallback(id,pass,comment,auditorId)` 由审批引擎回调，`selectAuditBusinessKey` 注册业务 key——质量门禁核心。
  - **强依赖 dataset**：`renderTemplate(AssembleCompleteData)` 直接接收 dataset 装配结构 + 复用 `XlsxRenderUtil`，批签发与批记录**共用同一套装配+渲染引擎**，仅模板/数据集类型不同（`DatasetType.LOT_RELEASE_LINK`）。
  - 模板版本管理含默认版本机制（`makeDefault`），影响新生成单据渲染基准。
- 集成四件套：index.md mes 分区追加 [[mes-lotrelease-module]] ✅；双向链接 mes-overview（待更新）+ dataset/plan/process/product/service-integration/platform-overview/database-schema-overview。

## [2026-07-02] create | MES dataset 子模块页（mes-dataset-module）

- 新增 entities/_mes/mes-dataset-module.md：覆盖 `service/dataset/`（controller / service / handle / util 等 6 子包）。
- 规模实测：Controller 2 · Service 接口 2 · Mapper 4 · Java 91（与 overview 吻合）。
- 定位为**双职责**模块：① 数据集/采集点模板定义（4 表）；② 批记录/批签发文档渲染（装配 + docx/xlsx 渲染）。
- 数据模型：4 表 `bm_dataset` / `bm_dataset_category` / `bm_dataset_point` / `bm_dataset_point_template_relation`。
- 关键发现：
  - **复杂度在 handle/util 而非 Service**：91 Java 中近半是 10 个装配 Builder（`handle/`）+ docx/xlsx 渲染（`util/`）+ 12 个替换选项（`util/options/`），单看 2 Controller 严重低估——再次印证 PLAYBOOK"用 Java 文件数而非 Controller 数"。
  - **占位符正则** `PlaceholderConstants.PATTERN`：`${(标识)[n][n][n][n]([n])}`，改格式须同步前端模板。
  - **替换选项策略体系**：按 `DatasetTransValueDataType`（文本/图片/拍照/选择框等）分发到 `Docx*ReplaceOption`，新增形态加选项类即可。
  - Excel 渲染基于 Apache POI（XSSF/SXSSF/HSSF）。
  - 多向耦合：execute（表单/附件/副本版本）/ record（组件）/ plan / process / lotrelease（批签发引用）。
- 集成四件套：index.md mes 分区追加 [[mes-dataset-module]] ✅；双向链接 mes-overview（待更新）+ execute/record/plan/process/service-integration/database-schema-overview。

## [2026-07-02] create | MES execute 子模块页（mes-execute-module）

- 新增 entities/_mes/mes-execute-module.md：覆盖 `service/execute/`（controller / service / model / redis / constant 等）。
- 规模实测：Controller 2 · Service 接口 5 · Mapper 4 · Java 76（与 overview 吻合）。
- 数据模型：4 张表 `bm_execute_form_data`（★ 跨服务复用）/ `bm_execute_attachment` / `bm_execute_record_copy` / `bm_execute_subsidiary_record`。
- ⚠️ **跨服务复用确认**：`bm_execute_form_data` 被 lims 只读引用——lims 有独立 `ExecuteFormData` entity + 4 个 ExtInfo 扩展类（`bmos-lims2-common/.../model/execute/`），写入归属只在 mes。已在页内多处 ⚠️ 标注"改表结构必须同步 lims"。
- 关键发现：
  - **副本版本机制**（copyVersion）：`bm_execute_record_copy` 记档案 + `copyVersion` 字段；计算用 `Integer.MAX_VALUE` 区分上下文。
  - **步骤锁**：Redis `bmos:execute:lock:%s`（`ExecuteRedisKeyDefine.LOCK_STEP`）+ Redisson `mes:execute:express:receive:%s`。
  - **公式计算**依赖 process 的 `CalculateDataQueryDTO`，公式步骤 ID 占位 0（`FORMULA_PROCEDURE_STEP_ID`）。
  - execute → record（批记录项/组件）/ process（步骤模型/计算）/ plan / workflow（变更团队）多向耦合。
- 集成四件套：index.md mes 分区追加 [[mes-execute-module]] ✅；双向链接 mes-overview（待更新头部表 + 子页清单）+ record/process/plan/workflow/service-integration/database-schema-overview。

## [2026-07-02] create | MES workflow 子模块页（mes-workflow-module）

- 新增 entities/_mes/mes-workflow-module.md：覆盖 `service/workflow/`（behavior / listener / service / controller / change 五块）。
- 规模实测：Controller 1 · Service 接口 3 · ServiceImpl 4 · Mapper 1 · Java 65（与 overview 头部子域表吻合）。
- 关键定位：workflow 是 `bmos-orchestrator-starter` 的 **mes 适配层**——表仅 1 张（`bm_product_change_team`，变更团队），流程定义/实例/任务/历史全存引擎内部。
- 与 orchestrator 协同三扩展点（基于调用证据）：
  - behavior：`CustomTaskAssigneeBehavior` 实现 `TaskAssigneeBehavior`（动态算办理人）
  - listener：4 个 `InfiniteEventListener`（步骤结束/工序结束/流程结束/流程终止）
  - executor/service：经 `CreateDeploymentCmd`/`StartProcessInstanceCmd`/`RuntimeContext` 驱动引擎
- ⚠️ **TODO**：`bmos-orchestrator-starter` 源码未入库（引擎包 `com.bmos.orchestrator.engine.core.*`）。已在页内设「TODO · orchestrator 待补」章节 + 顶部注释，指向 [[PLAYBOOK-backend]] 第十节，待 starter 源码 subtree 引入后补独立页。
- 集成四件套：index.md mes 分区追加 [[mes-workflow-module]] ✅；双向链接 mes-overview（待把头部子域 workflow 行 + 子页清单更新）；关联 process/plan/service-integration/database-schema-overview。

## [2026-07-02] create | MES storage 子模块页（mes-storage-module）

- 新增 entities/_mes/mes-storage-module.md：覆盖 `service/storage/`（config / manage / log 三子包）。
- 规模实测：Controller 8 · Service 接口 8 · Mapper 9 · Java 107（与 overview 头部子域表的 107 吻合）。
- 数据模型：9 张表（`bm_storage` `bm_cargo_position` `bm_storage_material` `bm_storage_material_batch` `bm_material_batch_field` `bm_storage_material_reserve` `bm_storage_material_charge_recycle` `bm_charge_recycle` `bm_storage_material_position_log`），均 mes 内部无跨服务复用。
- 状态机：storage 自身无 enums 目录，依赖 `bmos-mes-common` 的 `StorageOperateTypeEnum`（39 值，作业分类中枢）/ `MaterialQualityStatusEnum` / `WeighSignStatus`——已在页内显著标注"枚举不在本包"。
- 关键发现：
  - `IStorageMaterialService.weighConsume` / `chargeConsume` / `scrapBatch` 是 weigh / ingredient / output → storage 的反向耦合点，改库存校验影响整条称量配料链。
  - `MaterialExpireForeWarningJob` 调 platform `messageNotifyFeign` 推送临期通知，是 storage → platform 的跨服务点。
  - 标签打印内联在 `StorageMaterialController`（经 `equipmentConfigFeign` + `platformTagClient`）。
- 集成四件套：index.md mes 分区追加 [[mes-storage-module]] ✅；双向链接到 mes-overview（待把头部子域 storage 行加 wikilink）/ record / requisition / service-integration / platform-overview / mobile-overview / database-schema-overview。
- 下一步：mes-overview 头部子域表中 storage 行补 `[[mes-storage-module]]` 链接（本条日志后立即执行）。

## [2026-06-30] create | P3 收尾：前端架构概念页 + 4 API 页（+6 页，达成 100%）
- 触发：/goal 延续，code-wiki 从 23/29 ≈ 79% 推进至 **29/29 = 100%**。
- 新增 6 页：
  - **concepts/frontend-web-architecture**：pnpm workspace 分层、统一启动 chain（Auth→asyncMenu→i18n→render）、静态+动态路由装配（后端菜单 id ↔ 前端 asyncRoutes）、共享库职责分层、Vite 统一插件栈 / 8083 共端口 / base 路径部署。
  - **concepts/frontend-mobile-architecture**：UniApp 多端 + 条件编译（#ifdef H5×170 / APP-PLUS×153 为主）、横屏 + Android 原生集成、hybrid webview 桥接（pdf.js viewer）、独立于 @bmos 体系、build-template 打包链（APK/exe）。
  - **api/platform-api**：网关前缀、认证 / 用户权限 / 基础主数据 / 对外 Feign 接口地图、错误码段 81。
  - **api/mes-api**：101 Controller 域级地图（weigh/plan/process/record/product/storage/execute/dataset/audit）、错误码段 82、检验三方联动。
  - **api/lims-api**：63 Controller 子域地图（inspect/eln/stability/report/audit）、⚠️ lims2、错误码段 83（83_11 分样）、审批流。
  - **api/wms-api**：14 Controller 清单、错误码段 83（仅 00/01/09/10）、检验三方联动。
- 集成：index.md Concepts 6/8→8/8、API 4 处 ⏳→✅、状态行更新为 29/29 = 100%。
- 里程碑：code-wiki 骨架规划的全部 29 页（concepts 8 + entities 14 + comparisons 3 + api 4）现已全部建成 ✅。后续为各服务子域深化（如 mes weigh/execute/dataset/audit/workflow、lims inspect 子域 366 Java 独立子页）。


## [2026-06-30] create | P2 批量建页：4 概念页 + platform 子模块 + 前端 Web/移动端实体页（+9 页）
- 触发：/goal 延续任务，code-wiki 从 14/29 ≈ 48% 推进至 **23/29 ≈ 79%**。
- 方式：派 5 个并行研究 agent 扫描 platform 认证/license、platform 用户权限、跨服务 API+数据访问共性、前端 Web、移动端，收集硬事实后批量建页。
- 新增 9 页：
  - **concepts/auth-and-license**：JWT+Redis 双重会话（loginToken UUID 落 `bmos:user:token:%s`，2h 续期）、license 每请求 Feign 回调 platform `/system/active/valid`（RSA 解激活码校验 MAC/appName/date，开关 `platform.sys.license.isRequired`）。地雷：RSA 私钥硬编码、密码 DES、`loginNoValidate` 后门、`bmos:user:info` 无 TTL、license 单点。
  - **concepts/api-conventions**：统一响应 `ResponseInfo<T>`（成功码 **0** 非 200）、错误码段位（platform 81/mes 82/wms 83 仅 00-01-09-10/lims 83 混段，**澄清 83_11 分样仅 lims，wms 无此段**）、全局异常 `GlobalExceptionHandler`、**分页用 PageHelper 非 MyBatis-Plus 插件**。⚠️ 共性代码在 monorepo 外的 `bmos-parent-starter`。
  - **concepts/data-access-pattern**：`BaseDO`(6 字段，逻辑删除列 `is_deleted`)、`BaseMapperX`、`DefaultDBFieldHandler` 自动填充、表前缀(bp_/bm_/bw_/lm_)、ShardingSphere 5.5.1 **仅 mes+platform** 分表（日志/流水 60~100 表）。同样标注外部 starter 边界。
  - **concepts/development-conventions**：命名/端口/context-path/版本/提交/语言/服务边界/错误码段位/TSD 历史汇总。
  - **entities/_platform/platform-auth-module**：登录签发、Redis 会话 4 key、license 校验链路、facade auth SDK（`@EnableBmosAuth`+`TokenValidateInterceptor`）。
  - **entities/_platform/platform-user-module**：18 表（**前缀纠正 bp_ 非 base_sys_**，数据权限 `bm_resource_permission`）、三层权限模型（功能 bp_role_menu/可授权 bp_auth_role_menu/数据 bm_resource_permission）、动态菜单下发 `/menu/auth/tree`、跨服务反查 Feign（`UserFeign.listByUserIds`/`RoleFeign.authUserList`）。
  - **entities/_web/web-overview**：12 app 规模表（**7 个 app 后端不在 5 主服务内**）、统一启动 chain（Auth→asyncMenu→i18n→render）、静态+动态混合路由、Vite 配置（8083 共端口、base 路径区分、proxy 走 VITE_API_HOST）。
  - **entities/_web/web-shared-packages**：7 个 @bmos/* 库（@bmos/auth 实为 messager 别名）、被依赖关系、auto-import 约定。
  - **entities/_mobile/mobile-overview**：mes-app 132 页横屏锁定、18 构建目标（**实际仅 H5+App-Plus**）、**lims-app 是 mes-app 副本（appid 都没改，不接 @bmos 库）**、build-template 原生打包壳（Android/Electron）。
- **重要修正**（前端 workspaces 误判）：[[monorepo-architecture]]、[[frontend-apps-overview]] 此前记"npm/yarn workspaces（非 pnpm-workspace.yaml）"有误，实测为 **pnpm workspace**（`pnpm-lock.yaml`+`pnpm-workspace.yaml`，`packageManager: pnpm@8.5.0`）；顺带修正 [[monorepo-architecture]] 残留的"读 gateway/wms/lims 一律 git show"过时指引为"已解密可直接读"。
- 集成：index.md 9 处 ⏳→✅ + Concepts 状态 2/8→6/8 + 状态行；[[monorepo-architecture]]/[[frontend-apps-overview]] 去掉 web-overview/shared-packages 的"(待建)"。
- 关键边界声明：API/数据访问共性（ResponseInfo/BmosException/BaseDO/BaseMapperX/GlobalExceptionHandler/BasePage/CommonPage）源码在 monorepo 外的 `bmos-parent-starter`（独立仓库，Maven 依赖，**非 git subtree**），两页均已显著标注。
- 下一步候选（P3）：frontend-web-architecture / frontend-mobile-architecture 概念页、4 个 api 页、mes 各子域子页（weigh/execute/dataset/audit/workflow）、lims inspect 子域（366 Java）独立子页。


## [2026-06-30] update | gateway/wms/lims 解密后补充盲区 + 修正误判
- 触发：用户告知 gateway/wms/lims 工作区源码**已解密**（/goal）。本轮针对上次因 TSD 加密读取困难而遗漏/不确定的部分补充，并修正解密后发现的误判。
- 解密验证：三服务 `.java` 文件头均为正常 `package` 开头，可直接 Read/Grep；本轮全部直接读取，**未用 `git show`**。
- **gateway 补充**（entities/_gateway/overview.md）：新增「配置属性与 Redis Key」小节——`BmosAuthProperties`（`enable`+`excludeUrls` 两字段）、`BmosRedisKeyDefine` 两个 key（`USER_TOKEN_ID_CACHE` `bmos:user:token:%s` + `USER_LOGIN_CACHE` `bmos:user:login:%s`，均 2h TTL）；确认仅 4 个 Java 文件。
- **wms 补充**（entities/_wms/overview.md）：新增「核心 Service 方法」（inventory/cargo/inspect/sendout 头部接口分类）、「独有机制」——XxlJob（`ScheduleJob.refreshInventoryBatchAvailable` 按到期日标记过期批次）、LIMS 对接策略体系（`LimsInspectGateway`/`LimsGatewaySelector`/`InspectLimsSwitch`，**只支持 BMOS，THIRD_PARTY 显式拒绝**）、License 激活链路（`ActiveValidFeignClient` 封装平台 facade，激活码模型）；枚举全量（`CargoInventoryOperateLogType` 子步骤文案等）；14 Controller 清单；修正 unit（平台 UnitCache 代理）/reserve（预占表无 Service）/job（确认有 XxlJob 非空壳）。
- **lims 补充+修正**（entities/_lims/overview.md）——**重要修正**：①Controller **65→63**；②**`Lic.java` 是离线激活码生成器**（非运行时校验器，Hutool RSA + 硬编码公钥，示例 applicationName=`bmos-wms-service`，疑似 wms 残留），运行时激活走 `ActiveValidFeignClient`→平台，私钥在平台侧；③**`license.xml` 是 Aspose.Words 商业授权**（去水印），与激活无关，lims 深度依赖 Aspose 做 DOCX/PDF；④**`@TableName("table_name")` 模板残留不存在**（grep 零命中），删除该地雷；⑤悬空依赖真名是 `spring-boot-starter-web-services`（Spring-WS/SOAP）。新增：头部 Service 方法签名、audit 审批流引擎表映射（`lm_flow_audit*` + `AuditCategoryCodeEnum` + `FlowAuditService`）、XxlJob（`stabilityTriggerDueTimepointTasks` 每天 02:00，无 MQ）、Aspose 文档转换、三方联动 **`source_system` 路由**（WMS→wms client 否则→mes client）、48 枚举关键全量（`TaskStatusEnum` 11 态等）。
- **全局 TSD 解密更新**：[[monorepo-architecture]]（关键约束→历史约束，已解密）、[[PLAYBOOK-backend]] 第七节 TSD 条目、[[database-schema-overview]] 回链、gateway/wms/lims overview 地雷与 AI 提示——全部从「必须 `git show`」改为「**已解密可直接读**」。
- 关键澄清：lims 存在三个易混的 license 概念——`Lic.java`（离线生成器）/ `ActiveService`（运行时 Feign 调平台）/ `license.xml`（Aspose 授权），wiki 已分别说明。
- 集成：index 状态行更新；本轮**无新页**（完成度维持 14/29 ≈ 48%，为质量补充与误判修正）。


## [2026-06-30] create | P1 补全：gateway/wms/lims overview + 前端速查 + 架构概念页
- 触发：/goal 要求补足 code-wiki 至完成度 40%。起始 9/29 ≈ 31%，本轮 +5 页 → **14/29 ≈ 48%**。
- 新增 5 页：
  - **entities/_gateway/overview.md**：端口 60300、Spring Cloud Gateway 3.1.8（reactive/WebFlux）、扁平单模块仅 4 Java、唯一过滤器 `AuthenticationFilter`（GlobalFilter，JWT + Redis 双重校验）、路由表与白名单**全部托管 Nacos（代码仓库不持久化）**、无持久层/无 Controller/无 Feign。
  - **entities/_wms/overview.md**：端口 60900、3 模块、**17 表（实扫，非此前记的 11）**、子域 inventory/cargo/inspect/sendout、检验三方联动（暴露 `InspectFeign` 被 lims 回调）、发料联动 mes、`allow-circular-references=true` 技术债、`ActiveValidFeignClient` 命名误导（非 Feign）。
  - **entities/_lims/overview.md**：端口 61001、**bmos-lims2-* 命名分裂**（groupId `lims` / Java 包 `lims2` / 服务名 `lims2-service` / path `/lims2`）、4 模块（-server/-web 非 -service）、**88 表（实扫，非 92）**、inspect(366 Java)/eln/stability/audit 头部子域、检验三方联动、与 mes 共享 `bm_` 表**双写**、`Lic.java` 异常放置（默认包根）。
  - **comparisons/frontend-apps-overview.md**：12 web app 规模/后端映射表 + 2 移动端、6 个 `@bmos/*` 共享库全业务 app 共享、Auto-Import 全启用、工作区用 **npm `workspaces`（非 pnpm-workspace.yaml）**。**发现：lims-app 是 mes-app 直接拷贝（manifest 全等、未独立化）、bmos-lims-web 实调 `/api/app/mes`（命名错配）、ems/dc 后端不在本仓库**。
  - **concepts/monorepo-architecture.md**：git subtree `--squash` 整合、后端各服务独立 POM（`bmos.version` 1.14.0~1.15.2 分歧）、前端 npm workspaces、**TSD 源码加密约束**、命名分裂/循环依赖/共享表双写技术债、无根级 pom/package.json。
- 关键发现（本轮最重要）：
  - **TSD 源码加密**：gateway/wms/lims 工作区 `.java` 被 `%TSD-Header-###%` 头（8192 字节块）加密，Read/ripgrep 得乱码、`@TableName`/`@FeignClient` 零命中；**必须 `git show HEAD:<path>` 读源码**；mes/platform 明文可直读。已回写 PLAYBOOK-backend 第七节踩坑库。
  - **表数修正**：wms 11→17、lims 92→88，已同步 [[database-schema-overview]] / [[service-overview]]。
  - **共享表措辞修正**：`bm_execute_form_data*` / `bm_batch_record_version` 实为 lims eln 子域**双写**（非 mes→lims 单向），已修正 database-schema-overview。
- 集成（4 件套）：index.md 5 处 ⏳→✅ + 状态行 + 完成度更新；database-schema-overview / service-overview 表数与回链修正；5 个新页双向出站链接到位；PLAYBOOK-backend 第七节加 TSD 加密踩坑。
- 下一步候选（P2）：platform-auth/user 模块子页、auth-and-license / api-conventions / data-access-pattern 概念页、web-overview / web-shared-packages 前端页。


## [2026-06-29] update | PLAYBOOK 注明 services 应用层限制 + 未覆盖场景 TODO
- 触发：用户指出 PLAYBOOK-backend 仅适用 services 应用层，对自写 starter / SDK / 平台库代码（如 bmos-cloud-dependency / bmos-platform-facade / bmos-api-feign / bmos-audit-engine-starter / bmos-orchestrator-starter / bmos-scheduler-core 等）不适用；这类代码的抓取信号、建页对象、文档读者动机与 services 应用层完全不同。
- 改动：
  - **PLAYBOOK-backend 头注**：把"适用范围"改为显式声明 ✅ services 应用层 / ❌ starter / SDK / 平台库代码；列出当前已引用但源码未入库的 starter 清单。
  - **PLAYBOOK-backend 新增第十节**：未覆盖场景 TODO，含 4 类（1·starter / SDK 源码 2·shared/ 目录 3·独立工具 / 脚手架 4·维护规则）；详细列出 starter 与 services 应用层的 8 维对比表 + 实战前需补足的扫描命令清单（@AutoConfiguration / @EnableXxx / @ConditionalOnXxx / META-INF/spring.* / Properties / SPI / 被依赖关系）+ 4 类预估踩坑（自动装配顺序 / 条件链不生效 / SPI 忘改 / Properties 前缀冲突 + 版本不一致这一现存问题）+ 拆分阈值（方法论 > 80 行则拆 PLAYBOOK-starter.md）。
  - **PLAYBOOK 顶层入口**：分流表加一行"建 starter / SDK / 平台库的 wiki"标 🚧 待补；新增"暂未覆盖的场景"表格列 3 类待实战；差异速览表加第三列"starter / SDK 库代码"。
  - **PLAYBOOK-frontend 新增第七·B 节**：未覆盖场景 TODO（与后端篇第十节配套），含 3 类（1·第三方 / 平台级 SDK 适配 2·桌面端 Electron 模板 lims-app-build-template / app-build-template 3·前端独立工具 / CLI / 脚手架）。
- 关键设计：
  - 三处文档（后端 / 前端 / 入口）的 TODO 节统一遵循「列触发条件 + 列差异 + 列实战时需补什么 + 列维护规则」四段式。
  - 触发条件全部基于"真实代码入库"而非时间点——避免凭空补踩坑。
  - 拆分阈值统一为「方法论 > 80 行且与现有内容差异显著 → 拆独立分册」。
- 意义：诚实声明限制，避免新 AI 拿着 services 应用层方法论去处理 starter 源码（去找根本不存在的 @TableName / Controller，错过 @AutoConfiguration / SPI 这种真正的关键信号）；同时给未来引入 starter 时留出清晰的回写路径。


## [2026-06-29] update | PLAYBOOK 拆分为前后端分册
- 触发：用户指出原 PLAYBOOK 完全是从后端 Spring Boot 实战长出来的，前端硬套会出现建页阈值失效（Java 文件数对前端无意义）、扫描命令全部不可用（无 @TableName / @FeignClient）、模板错位（前端是 app+共享库+多端而非"服务→子域"）、独有机制错配（前端是 Auto-Import / 动态路由 / Vite proxy / UniApp 多端编译，与 MQ/Job 完全不同）等四类问题。
- 重组结构：
  - PLAYBOOK.md → **顶层入口**（短，按任务分流，列前后端核心差异表，标明前后端共享部分如 SCHEMA / index / log / 4 件套）
  - PLAYBOOK-backend.md → 原 PLAYBOOK 全部内容，头注与末注调整为「后端篇」
  - PLAYBOOK-frontend.md → **骨架版**，含 web app（12 个）+ @bmos/* 共享库（7 个）+ UniApp 移动端（17 个构建目标）的实扫数据；列出 8 类扫描命令（app 规模 / 标准目录 / 后端调用 path / 路由 / Pinia / 共享库依赖 / Vite 配置 / UniApp 多端 / 启动 chain / i18n）；3 个模板（W1 Web App / W2 共享库 / M1 UniApp 移动端）；6 类信号映射表；8 条预估前端踩坑（待实战验证）。
- 关键设计：
  - 前端篇标 `status: draft`，明确"未经实战，第一次建前端 wiki 时必须回写校准"。
  - 后端篇 status 保持 active（已经 4 次实战）。
  - 入口文档维护一张"前后端核心差异速览表"，让 AI 即使只读入口也能判断该用哪本分册。
- 同步更新：SCHEMA / index.md「如何使用」/ CLAUDE.md AI 处理流程 —— 全部改为指向新的「入口 + 分册」结构。
- 意义：避免新 AI 拿后端方法论写前端 wiki 时找根本不存在的 @TableName、错过 vite.config.ts 的关键代理配置这种偏差。同时给前端实战留出可演进的骨架，不空想细节。


## [2026-06-29] create | PLAYBOOK 建设方法论
- 触发：用户要求把前几轮建 wiki 沉淀的方法显性化，让别的 AI 在新服务/新模块引入时可以快速建设，不必读取大量历史文档。
- 新增 PLAYBOOK.md：与 SCHEMA.md 平级，分九节：
  1. 决策树（建页阈值：Java ≥ 50 且至少 2 Service 或 2 表）
  2. 五步法（扫描定基线 → 选模板 → 抓重点信号 → 写页 → 集成 4 件套）
  3. 扫描命令速查（@TableName / @FeignClient / Service 签名 / 枚举 / MQ / Job / 跨服务依赖 / 抽象基类）
  4. 4 个页面模板（Service Overview / Module 子页 / Concept 跨服务 / Comparison 速查）
  5. 重点信号清单（数据模型 / 状态机 / Service 签名 / 扩展点 / 独有机制 / 隐藏地雷 共 6 类）
  6. 节奏控制原则
  7. **已知踩坑库（10 条）**：cwd 漂移、单维度分档、表前缀假设、Model 类名假设、Nacos 名假设、重复 Feign、抽象基类被当枚举、策略平铺等
  8. 新服务/新模块上手 checklist
  9. 本文档自身的维护规则
- 更新 SCHEMA.md 头注：明确「SCHEMA 管长什么样、PLAYBOOK 管怎么做出来」的分工。
- 更新 index.md「如何使用本知识库」：把 PLAYBOOK + SCHEMA 列为新 AI 接手必读，并加入「新增页面 / 新服务接入」流程指引。
- 更新根目录 CLAUDE.md：在 AI 处理流程后追加新建 wiki 页 / 新服务接入的引用入口。
- 意义：把过去几轮散在对话和多个文件里的隐性方法显性化，新 AI 不必读完 8 个 wiki 页 + 全部对话历史，只需读 PLAYBOOK + SCHEMA + index 即可上手。


## [2026-06-29] update | mes-overview 子域分档重写
- 触发：用户指出首版 overview 的"业务子域全景"按 Controller 数分档导致部分模块被低估。
- 重扫：用多维指标（Controller / Service / Mapper / Table / Java 文件总数）重新统计 39 个子域。
- 修正：分档口径改为「Java 文件数」（更全面），新口径下显著上升的 4 个子域被重新归类为头部：
  - **workflow**（65 Java，原 D 档"不建页" → 头部，工作流引擎封装，与 bmos-orchestrator-starter 协同）
  - **audit**（90 Java，原 C 档"归并合规页" → 头部，与 operate/log/trace 拆开，独立审计模块）
  - **execute**（76 Java，原 C 档"已在 overview 提及" → 头部，bm_execute_form_data 跨服务主写方）
  - **dataset**（91 Java，原 C 档"两行带过" → 头部，数据集与采集点）
- 修订 entities/_mes/overview.md「业务子域全景」章节：改为三层 Ctrl/Svc/Tbl/Java 多维表格（头部 14 / 中部 10 / 尾部 15），并明确归并去向（合规类、weigh 延伸、license、纯支撑）。
- 子页建设清单更新：A/B 档合并为「待建头部 10 个」，dataset/audit/execute/workflow 加入。
- 在 overview 末尾留下统计口径说明：「Java 文件数 > 50 且至少 2 Service 或 2 表 → 值得建子页」，避免后续再次出现单维度判断偏差。


## [2026-06-29] create | mes-plan-module
- 来源：扫描 `packages/backend/services/mes/.../service/plan` 全量代码（21 表 / 17 Service / 13 Controller / 7 子包）和 `bmos-mes-common/.../enums/plan/` 17 个枚举。
- 新增 entities/_mes/mes-plan-module.md：
  - 7 个并列子包速览（info/production/instruction/team/template/document/rule）—— 这是 plan 模块独有的组织方式，与其它子域不同。
  - 21 张表按子包分组（含跨子包概念区分：record 设计 → plan/template 模板管理 → plan/archive 执行归档，三层最易混淆）。
  - **11 个关键枚举完整列出**：ProductPlanStatusEnum / ProductPlanStartEnum / **ProductionStatusEnum（复合，含 PlanStart 映射）** / ProductPlanInstructStatusEnum / ProductPlanTypeEnum（3 类批次：生产 A / 实验 B / 验证 C）/ PlanArchiveStatusEnum / BatchRecordArchiveStatusEnum / BatchRecordArchiveOperateTypeEnum（含 code 数值）/ TemplateVersionStatusEnum / PlanAuditProgressStatusEnum / CodeRuleTypeEnum。
  - 17 Service 核心方法，重点：PlanService 45 方法分 6 类（**含 4 个审批回调 + 2 个执行回调**）、PlanRetraceService **7 个维度追溯**（批次/物料/设备/房间/工序/执行/偏差）—— 是制药 MES 合规核心。
  - **自研 MQ 抽象**（@Topic / @Consumer + BaseMqTopic）：PlanStatusChangeTopic 触发 BatchRecordArchive.autoGenerateArchive。
  - **XXL-Job 定时任务**（@XxlJob）：BatchArchiveJob.removeVerifyArchive 清理验证临时文件。
  - **PlanConstant**：MySQL 唯一索引名 → 业务错误码映射（uk_planNo / uk_processId_batchNo），是改唯一索引时必须同步的隐藏映射点。
- 关键发现：
  - plan 是 mes 业务入口层，从生产排程到批记录归档形成闭环。
  - 三个"BatchXxx"概念易混淆：`bm_batch_record`（record 模块文档结构）≠ `bm_batch_template_*`（plan 模板管理）≠ `bm_batch_record_archive_*`（plan 执行归档）。已在文档显著位置区分。
  - `bm_product_plan` 表对应 Model 类名是 `Plan` 而不是 `ProductPlan`，是命名陷阱。
  - ProductionStatusEnum 同时承载"生产业务态"和"计划启动态"两个维度，是 mes 最复杂状态枚举之一。
- 同步更新 entities/_mes/overview.md：plan 行链入子页。
- 更新 index.md：mes-plan-module 标记为 ✅。


## [2026-06-29] create | mes-process-module
- 来源：扫描 `packages/backend/services/mes/.../service/process` 全部代码（28 表、19 Service、6 Controller）和 `bmos-mes-common/.../enums/process/` 全部枚举。
- 新增 entities/_mes/mes-process-module.md：登记三层结构 Process→Procedure→ProcedureStep→ProcedureTask/Condition；28 张表按业务层分组（process 5 / procedure 6 / step 5 / task&condition 7 / 配置 5）；7 个关键枚举（ProcessStateEnum 5 态 / ActionStateEnum 7 态版本流转 / ConditionTypeEnum 7 种条件类型 等）；19 Service 按三层组织；版本复制策略包（service/impl/copy/）；**条件事件机制**（ITaskConditionCalculator + ConditionChangeType 抽象基类 + 9 个事件子类对应 EquipmentStatus/RoomStatus/MaterialReserve/WeighingIngredient/OutputWeigh 等子域耦合点）。
- 关键发现：
  - process 是 mes 业务编排中枢，比 record 高一层抽象——record 是文档载体、process 是任务编排引擎。
  - 条件事件 ConditionChangeType 是 process 与 weigh/ingredient/equipment/station 子域的耦合契约，新增触发类型 = 新增子类。
  - 28 张表中 `product_schedule_procedure_config` 无 `bm_` 前缀，是整合前遗留命名，AI 不能按"工艺表=bm_前缀"假设处理。已写入文档警告。
  - process 是 mes 内唯一显式使用 Repository 抽象层的子域（`ProcessRepository`）。
  - 审批回调 4 个方法（auditProcessSuccessCallBack 等）与 bmos-orchestrator-starter 配合，是版本审批的回调入口。
- 同步更新 entities/_mes/overview.md：process 行链入子页。
- 更新 index.md：mes-process-module 标记为 ✅。


## [2026-06-29] create | mes 模块子页（product / record）
- 来源：扫描 `packages/backend/services/mes/.../service/{product,record}` 全部 Java 文件、@TableName、关键枚举和 Service 接口签名。
- 新增 entities/_mes/mes-product-module.md：4 表（bm_material / bm_material_category / bm_material_field / bm_material_log）、4 Controller、ProductMaterialService 19 个核心方法（含 issue/sync 远程对接、bindBatchRecords 与批记录绑定）、对外 MaterialFeign 契约。
- 新增 entities/_mes/mes-record-module.md：9 表（含跨服务复用的 bm_batch_record_version）、关键枚举 RecordStateEnum（编辑1/审批2/确定3/作废4 状态机）和 RecordFormatType、4 Controller、8 Service 核心方法、**57 个 ComponentStrategy 策略类按类别分组速览**（按用户要求只点接口与分类，不展开实现）、公式引擎入口（model/formula/）、Word 文档解析入口（DocxValidator + DocxSplitUtil2，依赖 docx4j）、Redis Key 入口。
- 节奏决策：按用户要求，策略类不看实现细节、Word 文档解析不展开——只标入口供 AI 跳转。
- 命名约定确立：mes 模块页用 `mes-<domain>-module.md`，跨服务 wikilink 唯一。
- 同步更新 entities/_mes/overview.md：在头部子域 record/product 行链入子页。
- 更新 index.md：mes-product-module / mes-record-module 标记为 ✅。


## [2026-06-29] create | mes 服务总览
- 来源：扫描 `packages/backend/services/mes` 三模块（common/feign/service）的包结构、Controller 分布、pom 依赖。
- 新增 entities/_mes/overview.md：记录端口 60200、启动类 BmosMesApplication、模块结构、6 个关键 bmos starter（重点 audit-engine / orchestrator / scheduler-core）、**38 个业务子域全景**（按 Controller 数分头部/中部/尾部三层）、3 个对外 Feign（InspectFeign/MaterialFeign/MaterialBatchFeign 全部围绕检验与物料）、跨服务调用关系、AI 入口约定。
- 关键发现：
  - mes 是平台业务核心，101 Controller / 170 Mapper / 169 表占全平台表总量约 50%。
  - 头部子域：weigh 称量（13 Ctrl）、plan 计划（13）、storage 库存（8）、process 工艺（6），是核心业务热点。
  - 包名 `service/facotry/` 存在错别字（应为 factory），跨包 import 较多，重构需作为独立任务。
  - 跨服务数据流：bm_execute_form_data* 和 bm_batch_record_version 在 lims 中被复用（mes 主写、lims 只读）。
  - 仅暴露 3 个对外 Feign，全部围绕检验流程，与 lims/wms 形成检验三方联动。
- 节奏决策：mes 体量大（38 子域），本次只出门户级 overview，子域细节按未来需求逐个建子页（如 weigh-module / batch-record-module），避免单页超 200 行拆分阈值。
- 更新 index.md：mes-overview 标记为 ✅。


## [2026-06-29] create | service-integration（Feign 调用矩阵）
- 来源：扫描全部 *Feign.java 文件和 `@FeignClient` 注解，覆盖 platform/mes/lims/wms 四个服务。
- 新增 concepts/service-integration.md：登记 5 服务的 Nacos 注册名、契约模块两种组织方式（platform 用 -facade / 业务服务用 -feign）、完整调用矩阵（业务服务 → platform / 横向 mes↔wms↔lims / 反向 platform→业务服务）、14 个 platform-facade 标准 Feign 清单和 21 个业务服务自定义 Platform* Feign 清单。
- 关键发现：
  - lims Nacos 注册名是 `bmos-lims2-service`（不是 lims-service），@FeignClient 写错会找不到目标。
  - mes/lims/wms 在 `service/platform/.../feign/` 下重复定义了一份 Feign 调 platform（21 个 client 与 facade 的 14 个 Feign 功能重叠），是历史遗留技术债，新代码应统一走 facade。
  - 所有 @FeignClient 都加了 contextId 防止 bean 冲突，新建 Feign 必须遵循。
  - platform 内有 2 个悬空 @FeignClient（`bmos-centralization-lims-service` / `bmos-plasma-service`）指向当前 monorepo 不存在的服务，是整合前的外部依赖。
- 更新 index.md：service-integration 标记为 ✅。


## [2026-06-29] create | platform 服务总览
- 来源：扫描 `packages/backend/services/platform`（common/facade/service 三模块）的包结构、Controller、facade Feign 接口和 import 依赖。
- 新增 entities/_platform/overview.md：记录端口 60100、启动类 BmosPlatformApplication、三模块结构（facade 为对外契约模块）、13 个业务子域（system/permission/equipment/factory/dict/tag/material/message 等）、14 个对外 Feign 接口、57 个 Controller 清单和关键约定。
- 关键发现：platform 是依赖最重的底座（mes 97 / lims 27 / wms 5 个 Java 文件 import com.bmos.platform）；对外契约模块命名为 -facade（非 -feign），内含 Feign + 共享 DTO/VO/Enum。
- 更新 index.md：platform-overview 标记为 ✅。


## [2026-06-29] ingest | P1 后端全景层（服务与表速查）
- 来源：扫描 `packages/backend/services/{platform,mes,lims,wms,gateway}` 的 pom.xml、application.yml、bootstrap.yml 和源码 @TableName 注解（共 5013 个 Java 文件）。
- 新增 comparisons/service-overview.md：登记 5 服务的端口（platform 60100 / mes 60200 / gateway 60300 / wms 60900 / lims 61001）、Nacos 注册名、context-path、bmos.version（1.14.0~1.15.2）、Maven 子模块、Controller/Mapper/表规模和职责定位。
- 新增 comparisons/database-schema-overview.md：登记表前缀→服务映射（bp_=platform / bm_=mes / lm_=lims / bw_=wms）、各服务核心表按业务域分组（platform~67 / mes~169 / lims~92 / wms~11）和跨服务复用表（bp_active、bm_execute_form_data）。
- 更新 index.md：将 service-overview / database-schema-overview 标记为 ✅，状态更新为「P1 后端全景层进行中」。
- 关键发现：gateway 纯网关无持久层；lims 模块命名为 bmos-lims2-*（-server/-web 而非 -service）；各服务 bmos.version 不一致，是后续统一依赖的关注点。
- 下一步：可选补 concepts/service-integration（Feign 链路）、各服务 entities/_<svc>/overview.md，或转 P1 前端部分。


## [2026-06-29] create | code-wiki 知识库骨架初始化
- 背景：参考 Neta-monorepo 的 docs/code-wiki 体系，为 bmos-monorepo 建立 AI 代码知识库。
- 创建治理文件：SCHEMA.md（约定 + frontmatter 模板 + 标签体系，新增 service 字段）、index.md（按类型 + 服务双分区目录）、log.md（本文件）。
- 在 index.md 登记从代码扫描得到的真实清单：5 个后端服务（gateway/lims/mes/platform/wms）、12 个 web app、7 个 @bmos/* 共享库、2 个 UniApp 移动端 + 2 个构建模板。
- 规划但尚未创建的页面已在 index.md 以 ⏳ 标注（concepts ×8、entities 各服务 overview、comparisons ×3、api ×4）。
- 同步创建根目录 CLAUDE.md（AI 导航入口 + 功能归属决策树）。
- 下一步：填充 P1 全景层（service-overview / frontend-apps-overview / database-schema-overview / monorepo-architecture）。
