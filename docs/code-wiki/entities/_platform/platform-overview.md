---
title: Platform 服务总览
created: 2026-06-29
updated: 2026-07-20
type: entity
service: platform
tags: [backend, platform, module, auth, feign]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/
  - packages/backend/services/platform/bmos-platform-facade/src/main/java/com/bmos/platform/facade/
  - packages/backend/services/platform/bmos-platform-common/
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/BmosPlatformApplication.java
status: active
---

# Platform 服务总览

## 概述 / 职责

**platform 是整个 bmos 平台的基础底座**，提供用户、权限、组织、设备、工厂、字典、编码、消息通知、license 等公共能力，并通过 `bmos-platform-facade` 对外暴露 Feign 接口供 mes / lims / wms 调用。

- 端口：**60100** ｜ Nacos 注册名：`bmos-platform-service` ｜ context-path：`/api/app/platform`
- 启动类：`com.bmos.platform.service.BmosPlatformApplication`
- 被依赖程度：mes 97 个、lims 27 个、wms 5 个 Java 文件 import `com.bmos.platform`（全平台依赖最重的服务）
- 规模：57 Controller / 66 Mapper / ~67 张表（前缀 `bp_`）

## Maven 模块结构

```
platform/
├── bmos-platform-common/    # 常量、枚举、工具、公共 DTO
├── bmos-platform-facade/    # ★对外接口（Feign + DTO/VO/Enum），其它服务依赖此模块
└── bmos-platform-service/   # 业务实现 + Controller + Mapper + 启动类
```

> 区别于 mes/wms 的 `*-feign` 模块命名，platform 用 **`*-facade`** 作为对外契约模块——里面同时包含 Feign 接口与共享 DTO/VO/Enum。

## 业务子域（service 模块）

`bmos-platform-service/.../com/bmos/platform/service/` 下：

| 子域 | 包 | 职责 | 核心表 |
|------|----|------|--------|
| 系统/权限 | `system` `permission` | 用户、角色、菜单、部门、资源权限 | `bp_user*` `bp_role*` `bp_auth_role_menu` `bp_menu` `bp_dept*` `bp_resource_permission` |
| 设备 | `equipment` | 设备台账、分类、工位、状态/操作日志、标签 | `bp_equipment_*`（13 张） |
| 工厂/产线 | `factory` | 工厂、车间、房间、产线、模块（**两套并行层级树**：物理空间 + 逻辑模块） | `bp_factory_*` + `bp_equipment_station*`（**15 张**，详见 [[platform-factory-module]]） |
| 字典 | `dict` | 数据字典 | `bp_dict*` |
| 标签 | `tag` | 标签定义/实例/场景/类型 | `bp_tag*` `bp_equipment_tag*` |
| 物料 | `material` | 物料及扩展单位 | `bp_material*` |
| 单位 | `unit` | 计量单位 | — |
| 消息 | `message` | 通知消息 | `bp_message*` |
| 签名 | `signature` | 电子签名 | `bp_signature` |
| 配置 | `config` | 业务参数、编码规则 | `bp_business_parameter` `code_rule*` |
| 日志 | `log` | 平台操作日志 | `bp_operation*` `bp_login*` |
| 插件/适配 | `plugin` `adaptor` `rest` `execute` | 插件、外部适配、REST、执行 | — |
| 其它 | `feign` `util` `utils` `typeHandler` `signature` | Feign 客户端、工具、类型处理器 | `bp_active`(license) |

## 对外暴露的 Feign 接口（facade）

`bmos-platform-facade` 暴露 14 个 Feign，供其它服务调用（**这是 platform 作为底座的核心契约**）：

| Feign 接口 | 用途 |
|-----------|------|
| `UserFeign` / `UserSignFeign` | 用户信息、用户签名 |
| `RoleFeign` / `MenuFeign` / `DeptFeign` | 角色、菜单、部门 |
| `DictFeign` | 数据字典 |
| `CodeRuleFeign` | 编码规则（生成业务单号） |
| `BusinessParameterFeign` | 业务参数 |
| `EquipmentConfigFeign` | 设备配置 |
| `FactoryFeign` / `FactoryAppFeign` | 工厂/产线结构 |
| `PlatformMaterialFeign` | 物料 |
| `MessageNotifyFeign` | 消息通知 |
| `ActiveValidFeign` | license 激活校验 |

> 调用方向：mes/lims/wms → platform（单向依赖底座）。详见 [[service-integration]]。

## 核心 Controller（57 个，部分）

- 用户/权限：`UserController` `UserMobileController` `RoleController` `MenuController` `DeptController` `ResourcePermissionController`
- 设备：`EquipmentController` `EquipmentInfoController` `EquipmentCategoryController` `EquipmentStationController` `EquipmentLogController` `EquipmentAppController`
- 工厂：`LineController` `RoomController` `FactoryTenementController` `FactoryTenementFloorController` `EquipmentStationController` `RoomAppController` `RoomLogController` `LineModuleController` `RoomModuleController` `StationModuleController`（11 个，详见 [[platform-factory-module]]）
- 字典/编码/参数：`DictController` `CodeRuleController` `CodeRuleVersionController` `BusinessParameterController` `ParamController`
- 标签：`TagDefineController` `TagInstanceController` `TagSceneController` `TagTypeController`
- 其它：`MaterialController` `UnitController` `MessageController` `NotifyMessageController` `SignatureController` `MinioFileController` `I18nController` `PlatformLogController`
- `*FeignController`：对应 Feign 接口的服务端实现（如 `UserFeignController` `RoleFeignController` `DictFeignController`）

## 关键约定

- 其它服务获取用户/权限/字典/设备/工厂等基础数据，**必须走 platform-facade 的 Feign**，禁止跨库直连 `bp_*` 表。
- `ActiveValidFeign` / `bp_active` 是 license 校验入口，各服务启动/运行时校验；流程见 [[auth-and-license]]。
- 文件存储经 `MinioFileController` → MinIO。

## 相关页面

- [[service-overview]] — 5 服务速查（端口/规模）
- [[database-schema-overview]] — `bp_*` 表分组
- [[service-integration]] — Feign 调用链路（platform 为被调底座）
- [[platform-user-module]] — 用户/角色/菜单/部门/权限
- [[platform-auth-module]] — 登录/会话/license
- [[platform-equipment-module]] — 设备台账/类型(tag)/状态机/采集点/工位（mes 高频调用）
- [[mes-overview]] / [[lims-overview]] / [[wms-overview]] — 依赖 platform 的业务服务
