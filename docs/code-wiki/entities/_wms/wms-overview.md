---
title: WMS 服务总览
created: 2026-06-30
updated: 2026-06-30
type: entity
service: wms
tags: [backend, wms, module, mybatis, feign]
sources:
  - packages/backend/services/wms/bmos-wms-service/src/main/java/com/bmos/wms/service/
  - packages/backend/services/wms/bmos-wms-feign/src/main/java/com/bmos/wms/inspect/feign/
  - packages/backend/services/wms/bmos-wms-common/src/main/java/com/bmos/wms/common/
  - packages/backend/services/wms/bmos-wms-service/src/main/java/com/bmos/wms/service/BmosWmsApplication.java
  - packages/backend/services/wms/bmos-wms-service/src/main/java/com/bmos/wms/service/job/ScheduleJob.java
status: active
---

# WMS 服务总览

## 概述 / 职责

**wms 是仓库管理服务**,负责库存(货品/批次/货位)、出入库操作、请验单(与 lims 联动)、发料(与 mes 联动)。是 5 个后端服务中**最轻量**的业务服务。

- 端口:**60900** ｜ Nacos 注册名:`bmos-wms-service` ｜ context-path:`/api/app/wms`
- 启动类:`com.bmos.wms.service.BmosWmsApplication`
- 规模:219 Java / 14 Controller / 12 Service / 17 Mapper / **17 张表(全部 `bw_` 前缀)**
- bmos.version:**1.14.0-SNAPSHOT**(父 pom;feign 模块为 1.15.0,版本不一致,见地雷)

## Maven 模块结构

```
wms/
├── bmos-wms-common/   # 枚举 + 常量 + 异常(com.bmos.wms.common)
├── bmos-wms-feign/    # ★对外 Feign 契约(groupId=com.bmos,被 lims2 回调)
└── bmos-wms-service/  # 主服务(Controller/Mapper/Service/启动类)
```

> ⚠️ **groupId 混用**:feign 模块 groupId 为 `com.bmos`,common/service 为 `com.bmos.wms`。
> 启动类 `@EnableFeignClients(basePackages={"com.bmos.wms","com.bmos.lims2"})`(主动调 lims 契约来自 `bmos-lims2-feign` jar)。

## 业务子域(service 模块,按 Java 文件数)

| 子域 | Java | Ctrl | 表 | 职责 |
|---|---|---|---|---|
| platform | 32 | 1 | 1 | 调 platform 的 Feign 代理层(物料/编码/参数/用户/权限),非业务表 |
| inventory | 32 | 1 | 2 | 库存件/批次(出入库核心) |
| inspect | 29 | 2 | 3 | 请验单主/明细/结果(与 lims 联动) |
| cargo | 25 | 2 | 2 | 货品主数据/分类(含平台物料同步) |
| businessLog | 16 | 2 | 2 | 货品/货位库存变动流水 |
| sendout | 14 | 1 | 2 | 发料单主/明细(与 mes 联动) |
| log | 13 | 1 | 1 | WMS 操作日志 |
| storage | 10 | 1 | 1 | 存储区域树(车间/区域/存储区/货位) |
| position | 9 | 1 | 1 | 货位主数据 |
| config | 8 | 1 | 1 | license 激活(bw_active) |
| unit / reserve / mes / utils / job | ≤2 | 0~1 | 0~1 | 见下方说明 |

> **子域性质说明**:
> - `unit`(2 文件):**平台 unit 模块的本地 REST 代理** —— 仅 `UnitController`+`UnitVO`,无 Service/Mapper,直接读外部 `com.bmos.unit.service.UnitCache`。
> - `reserve`:仅 `IInventoryReserveMapper`+`InventoryReserve` 实体,**无 Service/Controller** —— 发料流程扣减可用量的预占表(`bw_inventory_reserve`)。
> - `mes`:wms 调 mes 的 Feign 客户端所在;`job`:XxlJob 入口(见独有机制)。

## 数据模型(17 张表,`bw_` 前缀,Model 继承 `BaseDO`)

| 域 | 表 | Model | 用途 |
|---|---|---|---|
| 库存 | `bw_inventory` `bw_inventory_batch` | Inventory / InventoryBatch | 库存件(货品+批次+货位)/ 批次 |
| 货品 | `bw_cargo` `bw_cargo_category` | Cargo / CargoCategory | 货品主数据 / 分类 |
| 货位/存储 | `bw_cargo_position` `bw_storage` | CargoPosition / Storage | 货位 / 存储区域树 |
| 请验 | `bw_inspect` `bw_inspect_info` `bw_inspect_result` | Inspect/Info/Result | 请验单主/明细/结果回传 |
| 发料 | `bw_send_out_order` `bw_send_out_order_item` | SendOutOrder/Item | 发料单主/明细 |
| 流水 | `bw_cargo_log` `bw_position_log` `bw_operation_log` | CargoLog/PositionLog/WmsLogModel | 货品/货位/操作日志 |
| 预留 | `bw_inventory_reserve` | InventoryReserve | 库存预占(发料扣减) |
| 其它 | `bw_active` `bw_resource_permission` | Active / ResourcePermission | license 激活 / 资源权限 |

## 核心 Service 方法(头部子域接口)

| 子域 | Service | 关键方法(分类) |
|---|---|---|
| inventory | `IInventoryService` | **业务**:`inbound`入库 / `outbound`出库 / `move`移库 / `check`盘点;**批次**:`addInventoryBatch`/`editInventoryBatch`;**查询**:`queryBatchPage`/`queryPageByBatchId`/`queryAvailableQuantityList` |
| cargo | `ICargoService` | **CRUD**:`create`/`edit`/`enable`/`disable`;**物料同步**:`getSyncTree`/`syncMaterialAndCategory`/`issueMaterialAndCategory` |
| inspect | `IInspectService` | **发起**:`initiateInspect`(返 LIMS 单号)/`retryInitiateInspect`(作废+新建);**回调**:`inspectCallback`/`rejectInspect`;**查询**:`queryPage`/`queryDetail`/`querySchemesByBatchId` |
| sendout | `ISendOutOrderService` | **业务**:`submitSendOutOrderByBatch`(MES 提交领料)/`sendOut`/`cancelSendOut`;**查询**:`queryPage`/`queryDetail` |
| 日志 | `WmsOperationLogService` | `extends OperationLogService<WmsLogModel>`;`getPage`/`exportOperationLog`/`getDetail` |

> inspect 子域 Service 注释明确"mirror MES InspectService"——与 mes 的请验逻辑同构。

## 关键枚举(状态机,`bmos-wms-common/.../enums/`)

| 枚举 | 完整 code → 含义 |
|---|---|
| `InspectStatusEnum` | 1=PENDING 检验中 / 2=FINISHED 已完成 / 3=REJECTED 已退回 |
| `MaterialQualityStatusEnum` | QUARANTINE 待验(新批次默认) / QUALIFIED 合格 / UNQUALIFIED 不合格 / SAMPLED 已取样 / RESTRICTED_RELEASE 限制性放行 |
| `CargoInventoryOperateType` | 1=INBOUND 入库 / 2=OUTBOUND 出库 / 3=ADD 新增 / 4=CHECK 盘点 |
| `CargoInventoryOperateLogType` | 每个值带两步子文案:入库(接收/递交)、出库(发放/领用)、盘点(盘点/复核)、移库(移出/移入)、发料(发料/复核)、新增 |
| `PositionInventoryOperateType` | 1 入库 / 2 出库 / 3 新增 / 4=CHECK_PLUS 盘增 / 5=CHECK_MINUS 盘减 |
| `StorageLevelEnum` | 1=WORKSHOP 车间 / 2=AREA 区域 / 3=STORAGE 存储区 / 4=POSITION 货位(树形层级,含 `increaseLevel`) |
| `SendOrderStatus` | 0=PENDING 待发料 / 1=FINISHED 已发料 / 2=CANCELED 已取消 |
| `SendOrderType` | 1=BATCH 按批次 / 2=CARGO 按货品 |
| `TimeUnitEnum` | 0=HOUR 时 / 1=DAY 天 / 2=MONTH 月(批次有效期单位) |

## Controller 清单(14 个,`@RequestMapping` 前缀)

`/cargo`·`/cargo/category`·`/inventory`·`/material/position`·`/storage/config`·`/inspect`(前端)·`/feign/inspect`(LIMS 回调入向)·`/sendOut`·`/log/cargo`·`/log/position`·`/log`·`/unit`·`/user`(license 激活)·`/resource/permission`(数据权限)

> `InspectController`(前端 UI)与 `InspectFeignController`(实现 `InspectFeign` 供 LIMS 反调)职责不同。

## 独有机制

### XxlJob 定时任务(`job/ScheduleJob.java`)
- `@XxlJob("refreshInventoryBatchAvailable")` → 查到期批次(`queryPendingRefreshAvailableBatch(today)`),把 `InventoryBatch.available` 置 false。**按到期日自动标记过期批次不可用**。全 wms 仅此 1 个 `@XxlJob`,无 `@Scheduled`、无 MQ。

### LIMS 对接策略体系(`inspect/lims/`)
- `LimsInspectGateway` 接口(`type()`/`queryConfig`/`querySchemes`/`initiate`/`retry`)—— 唯一实现 `BmosLimsGateway`(`LimsType.BMOS`)。
- `LimsGatewaySelector`:把所有实现注入 `EnumMap<LimsType, ...>`,`require()` 取当前网关。
- `InspectLimsSwitch`:读平台参数 `INSPECT_LIMS_CONFIG`(JSON: enabled+type)决定开关,解析失败兜底"不对接"。
- ⚠️ **WMS 当前只支持自研 BMOS LIMS**:`type != BMOS` 抛 `83_11_002`,未启用抛 `83_11_001`,`THIRD_PARTY` 枚举保留但显式拒绝。
- 出向 DTO 的 `sourceSystem="WMS"`,LIMS 据此回调 wms。

### License 激活链路(`config/active/`)
- `ActiveValidFeignClient`(`@Component`,**非 Feign 接口**,命名误导)封装平台 facade `com.bmos.platform.facade.auth.feign.ActiveValidFeign`。
- `ActiveService extends ActiveApiAdaptor`:`save(activeStr)`/`actived()` → 远程调平台 `activeValid(LicenseParamDTO)` 返回 `RsaVO{active,date}` → 写 `bw_active`。
- ⚠️ 是**激活码模型**(本机激活、平台校验,非功能/流量 license);开关由平台控制。Controller `/user/active`、`/user/actived`、`/user/mac`。

## Feign 调用关系

**暴露(bmos-wms-feign,被 lims 回调)**:`InspectFeign`(`/feign/inspect/callback` 结果回传、`/reject` 退回),实现端 `InspectFeignController`。

**调用其它服务**:
| Feign | 目标 | 用途 |
|---|---|---|
| `MesFeignClient` | mes | 发料/取消发料 `/requisition/receive/sendOut` |
| `PlatformMaterialFeignClient` | platform | 物料 CRUD、`getSyncTree`、`issueMaterialAndCategory` |
| `PlatformCodeFeign` | platform | 编码取号 `getNextNo`/`confirmNo` |
| `PlatformUserOpenFeign` / `PlatformParameterClient` | platform | 用户 / 业务参数 |
| `MesInspectFeign`(lims2-feign jar) | lims2 | 经 `BmosLimsGateway` 发起请验 |

> 检验三方联动:wms 发起请验 → lims 检验 → lims 回调 wms `InspectFeign.callback`。详见 [[service-integration]]、[[lims-overview]]。

## 隐藏地雷 ⚠️

1. **历史 TSD 加密(已解密)**:工作区 `.java` 曾被 `%TSD-Header-###%` 头加密(ripgrep 零命中),**2026-06-30 已解密可直接读**。见 [[monorepo-architecture]]。
2. **版本不一致**:父 pom `bmos.version=1.14.0-SNAPSHOT`,而 `bmos-wms-feign/pom.xml` 内 `1.15.0-SNAPSHOT`。
3. **`spring.main.allow-circular-references=true`**:Bean 循环依赖技术债(疑似 active/inspect 平台适配器双向引用,需 runtime dump 确认)。
4. **命名误导**:`ActiveValidFeignClient`、`PlatformParameterClient` 类名带 FeignClient 后缀但**不是 `@FeignClient`**(普通封装类/接口)。
5. **pom 拼写错误**:父 pom `<mybatis-puls.version>`(应为 plus)。
6. **WMS 不支持三方 LIMS**:`THIRD_PARTY` 枚举保留但 `require()` 显式抛错。

## AI 定位提示

- 改 **出入库/盘点** → `service/inventory/`(`CargoInventoryOperateType` 是状态键)
- 改 **请验联动** → `service/inspect/lims/`(`BmosLimsGateway` 发起、`InspectFeignController` 回调入口、`InspectLimsSwitch` 开关)
- 改 **发料** → `service/sendout/` + `mes/feigns/MesFeignClient`
- 排查 **批次过期** → `job/ScheduleJob.refreshInventoryBatchAvailable`
- 改 **license 激活** → `config/active/`(`ActiveController` `/user/active`)
- 找 **存储树** → enums `StorageLevelEnum`(4 级)+ `service/storage/`

## 相关页面

- [[service-overview]] — 端口/规模速查
- [[database-schema-overview]] — `bw_*` 表分组(17 张)
- [[service-integration]] — wms↔mes↔lims 检验三方联动
- [[lims-overview]] — 检验回传方
- [[mes-overview]] — 发料接收方
- [[monorepo-architecture]] — TSD 加密历史、版本不一致
