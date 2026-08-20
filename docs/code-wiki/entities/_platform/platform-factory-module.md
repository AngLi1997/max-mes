---
title: Platform 工厂空间模块
created: 2026-07-20
updated: 2026-07-20
type: entity
service: platform
tags: [backend, platform, module, mybatis, database]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/factory/
  - packages/backend/services/platform/bmos-platform-facade/src/main/java/com/bmos/platform/facade/factory/
  - packages/backend/services/platform/bmos-platform-common/src/main/java/com/bmos/platform/common/enums/factory/
status: active
---

# Platform 工厂空间模块

## 概述 / 职责

platform 的**工厂空间建模与产线/工位主数据**管理。承担物理空间（楼宇/楼层/房间）+ 逻辑分组（企业/工厂/产线/房间/工位模块树）两套层级，并承载制药 GMP 的**房间清场状态机与清洁效期**。

- 所属服务：platform（60100），代码在 `service/factory/`（service 模块）+ `facade/factory/`（对外契约）+ `common/enums/factory/`（共享枚举）
- 规模：Controller **11** · 表 **15**（[[platform-overview]] 此前标 11，实扫 15，已回正）· 对外 Feign 2 个（`FactoryFeign` / `FactoryAppFeign`）
- 被依赖：mes `service/facotry/`（⚠️ 错别字）通过两个 Feign 远程消费，做房间清场校验/状态切换/移动端房间查询；lims/wms 亦反查地点结构

## 核心结构：两套并行层级（本页最重要）

地点建模有**两套正交的树**，仅在一个点（房间）交汇：

```
【体系 B · 逻辑模块树】bp_factory_module（parentId 自引用，type 区分节点类型）
  FIRM(0) 企业
    └─ FACTORY(1) 工厂
         ├─ LINE(2) 产线 ── bp_factory_line (moduleId)
         │     ├─ bp_factory_line_room     ──→ 房间
         │     └─ bp_factory_line_station  ──→ 工位
         ├─ ROOM(3) 房间   ── bp_factory_room (moduleId)
         └─ STATION(4) 工位 ── bp_equipment_station (moduleId)

【体系 A · 物理空间树】
  bp_factory_tenement 楼宇 (parentId 自引用)
    └─ bp_factory_tenement_floor 楼层 (tenementId)
         └─ bp_factory_room 房间 ★ 唯一交汇点 (tenementId + floorId + moduleId)
               └─ bp_factory_room_station ──→ 工位
```

- **交汇点**：`bp_factory_room` 同时持有 `floorId`（挂物理树）和 `moduleId`（挂逻辑树）。
- **工位不直接挂物理树**：`bp_equipment_station` 只有 `moduleId`，落到物理空间需经 `bp_factory_room_station` 关联到房间。
- 两套树详细缺口与割裂影响见末尾「隐藏地雷」。

## 数据模型（15 张表，前缀 `bp_factory_` + 工位 `bp_equipment_station*`）

### 物理空间树（4 张）

| 表 | Model | 关键外键 | 用途 |
|---|---|---|---|
| `bp_factory_tenement` | `FactoryTenement` | `parentId`（自引用） | 楼宇（实体注释"楼宇"） |
| `bp_factory_tenement_floor` | `FactoryTenementFloor` | `tenementId` | 楼层 |
| `bp_factory_room` | `FactoryRoom` | `tenementId` + `floorId` + `moduleId` ★ | 房间（≈洁净区），含 status/cleanLevel/timeLimit/expireTime/threeDModelId |
| `bp_factory_room_occupy` | —（仅 `BpFactoryRoomOccupyDao.xml`） | roomId | 房间占用 |

### 逻辑模块树（1 张）

| 表 | Model | 关键字段 | 用途 |
|---|---|---|---|
| `bp_factory_module` | `FactoryModule` | `parentId`（自引用）+ `type` | 模型分类节点树，承载 企业/工厂/产线/房间/工位 5 类（`FactoryModuleEnum`） |

### 业务节点（2 张）

| 表 | Model | 外键 | 用途 |
|---|---|---|---|
| `bp_factory_line` | `FactoryLine` | `moduleId` | 产线 |
| `bp_equipment_station` | `EquipmentStation` | `moduleId` | 工位 ⚠️ 表名前缀 `equipment_` 非 `factory_` |

### 多对多关联（3 张）

| 表 | Model | 连接 |
|---|---|---|
| `bp_factory_line_room` | `FactoryLineRoom` | 产线 ↔ 房间 |
| `bp_factory_line_station` | `FactoryLineStation` | 产线 ↔ 工位 |
| `bp_factory_room_station` | `FactoryRoomStation` | 房间 ↔ 工位 |

### 工位扩展（2 张）

| 表 | Model | 用途 |
|---|---|---|
| `bp_equipment_station_info` | `EquipmentStationInfo` | 工位 ↔ 设备（`bp_equipment_info`） |
| `bp_equipment_station_user` | `EquipmentStationUser` | 工位 ↔ 用户 |

### 房间业务/日志（3 张）

| 表 | Model | 用途 |
|---|---|---|
| `bp_factory_room_env_property` | `FactoryRoomEnvProperty` | 房间环境属性（设备数据编码 → 环境属性编码，如温湿度） |
| `bp_factory_room_status_log` | `FactoryRoomStatusLog` | 房间状态变更日志（含工序/批号/产品/操作人/复核人） |
| `bp_factory_room_clean_log` | `FactoryCleanRoomLog` | 清场日志（⚠️ Model 名 `CleanRoomLog` ↔ 表名 `room_clean_log`） |

## 关键枚举

| 枚举 | 位置 | 值 |
|---|---|---|
| `FactoryModuleEnum` | `common` | **0企业 / 1工厂 / 2产线 / 3房间 / 4工位** — 逻辑模块树节点类型 |
| `RoomStatusEnum` | `facade` | **1在用 / 2待清场 / 3已清场** — 房间状态机 |
| `RoomStatusOperateTypeEnum` | `facade` | `MANUAL_INPUT` 人工清场 / `AUTO_RECOGNITION` 生产清场 |
| `TenementFloorStatusEnums` | `service` | `ENABLE` / `DISABLE`（⚠️ String 型，`@EnumValue` 在 value 而非 name） |

## 独有机制：房间清场状态机（制药 GMP）

房间（`bp_factory_room`）除层级外键，承载一整套清场合规字段：`status`（`RoomStatusEnum`）→ `timeLimit`（清洁时限秒）→ `expireTime`（清洁效期）→ `cleanLevel`（洁净等级）。

- 状态流转：**在用(1) → 待清场(2) → 已清场(3)**，由 `RoomStatusOperateTypeEnum` 区分人工/生产触发。
- 痕迹留存：每次变更落 `bp_factory_room_status_log`（含工序/批号/产品/操作人/复核人），清场作业落 `bp_factory_room_clean_log`（含起止时间/效期/复核时间）。
- 效期到期：`expire/` 子包有 `ExpireRoomListener` + `RoomExpireInitialization`，房间清洁效期监听/初始化。
- 移动端作业：`RoomAppController` + `FactoryAppFeign` 暴露移动端房间状态切换/分页（对接 mes-app）。

## Controller（11 个）

| 职责 | Controller |
|---|---|
| 楼宇/楼层 | `FactoryTenementController` · `FactoryTenementFloorController` |
| 房间 | `RoomController` · `RoomAppController`（移动端）· `RoomLogController` |
| 模块分类树 | `RoomModuleController` · `LineModuleController` · `StationModuleController` |
| 产线 | `LineController` |
| 工位 | `EquipmentStationController` |
| Feign 服务端 | `FactoryFeignController` |

## 前端树形态（4 种 TreeNodeVO）

| 树 VO | 节点 |
|---|---|
| `TenementTreeNodeVO` | 楼宇分类树（parentId + children） |
| `RoomTreeNodeVO` | 房间分类树（parentId + children + `infoList`） |
| `StationTreeNodeVO` | 工位分类树（parentId + children + `infoList`） |
| `FactoryTreeNodeVO` | 产线/房间/工位**混合树**，靠 `type`（2/3/4）区分 |

> `RoomTreeNodeVO` / `StationTreeNodeVO` 自带 `parentId + children`，即房间与工位各自又支持一层基于 `bp_factory_module` 的分类树，与物理树独立渲染。

## 对外 Feign 契约（facade，跨服务反查地点）

| Feign | contextId | 高频用途 |
|---|---|---|
| `FactoryFeign` | platform-factory | `selectByRoomIds`（按房间 id 批量查房间信息）、产线详情、产线/房间模块树、工位权限 |
| `FactoryAppFeign` | platform-factory-app | 移动端房间分页、房间状态切换（`ChangeRoomStatusFeignDTO` / `MobileChangeRoomStatusFeignDTO`）、清洁效期查询 |

> 配套跨服务 DTO/VO 均在 `facade/factory/{dto,vo}`：`LineInfoFeignDTO` `RoomAuthUserDTO` `RoomMobilePageFeignDTO` `FactoryLineDetailFeignVO` `RoomInfoFeignVO` `LineModuleTreeNodeFeignVO` 等。

## 消费方（mes）

mes `service/facotry/`（⚠️ 包名错别字）是纯消费方，注入 `FactoryFeign` + `FactoryAppFeign`（`FactoryServiceImpl`），**不落地点主数据**，主要做：

- 房间信息查询（`getRoomInfo` → `factoryFeign.selectByRoomIds`）
- 房间状态切换 / 清洁效期校验（对接 mes 工序执行）
- 移动端房间分页
- 把房间/工位与工序（`ProcedureModelRoom`）、产线（`ProcessProductionLine`）、批记录组件绑定

## 隐藏地雷 ⚠️

1. **两套树割裂**：物理树（Tenement/Floor/Room）**缺"工厂"显式类型**（Tenement 实体注释是"楼宇"，虽有 `parentId` 自引用但非显式工厂层级）；逻辑树（`FactoryModuleEnum`）**缺"楼宇/楼层"类型**（只有 企业/工厂/产线/房间/工位）。两套树无外键互连，**从"工厂"节点无法沿树走到"楼宇→楼层"**——"厂→楼→层→区→工位"单一物理链路在"厂↔楼"处断裂。
2. **工位表名前缀异常**：`bp_equipment_station`（前缀 `equipment_`，非 `factory_`），起源与设备绑定，工位相关结构跨 `equipment`/`factory` 两包。改工位表结构需同时看 equipment 模块。
3. **Model 名与表名不一致**：`FactoryCleanRoomLog` ↔ `bp_factory_room_clean_log`；工位 `EquipmentStation` 起源 equipment 命名空间。
4. **mes 消费方包名错别字** `service/facotry/`（应为 `factory`），与 [[mes-audit-module]] 的 `ProcessAuditConditon` 同类历史遗留，改名涉及跨包 import，需独立重构任务。
5. **`bp_factory_room_occupy` 无 Model 类**，仅 `BpFactoryRoomOccupyDao.xml`（位于 `platform/src/main/resources/mapper/`，非 service 模块下），实扫易漏。
6. **overview 表数低估**：[[platform-overview]] / [[database-schema-overview]] 此前标 factory 11 张表，实扫 **15 张**，已回正 overview。

## AI 定位提示

- 改 **地点主数据**（楼宇/楼层/房间/产线/工位 CRUD）→ `service/factory/{controller,service,model}`
- 改 **房间清场状态机/效期** → `RoomController` + `RoomStatusEnum` + `bp_factory_room_status_log` / `bp_factory_room_clean_log` + `expire/` 子包
- 改 **两套模块分类树** → `FactoryModule` + `FactoryModuleEnum` + `*ModuleController`（Room/Line/StationModule）
- 改 **工位-设备/用户绑定** → `EquipmentStationInfo` / `EquipmentStationUser`
- 跨服务查地点 → facade `FactoryFeign` / `FactoryAppFeign`；mes 侧入口在 `service/facotry/`（注意错别字）
- 排查 **"工厂接不上楼宇"** 类需求 → 先看本页「核心结构」两套树割裂说明，落点改造需打通 `floorId`/`moduleId` 或新增显式层级

## 相关页面

- [[platform-overview]] — platform 服务总览（factory 子域行已回链）
- [[database-schema-overview]] — `bp_*` 表分组
- [[service-integration]] — `FactoryFeign` / `FactoryAppFeign` 调用矩阵
- [[mes-overview]] — mes `facotry` 消费方（错别字子域）
- [[data-access-pattern]] — `BaseDO` / MyBatis-Plus 约定
- [[PLAYBOOK-backend]] — Module 子页模板与本页建页依据
