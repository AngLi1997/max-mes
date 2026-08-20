---
title: Platform 用户权限模块
created: 2026-06-30
updated: 2026-06-30
type: entity
service: platform
tags: [backend, platform, module, auth]
sources:
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/user/controller/UserController.java
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/role/
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/menu/
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/system/dept/
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/permission/
status: active
---

# Platform 用户权限模块

## 概述 / 职责

platform 的**用户/角色/权限/菜单/部门**管理体系。提供动态菜单下发(前端路由来源)、三层权限模型(功能/可授权/数据)、跨服务用户反查 Feign。

- 所属服务:platform(60100),代码在 `system/{user,role,menu,dept}` + 独立 `permission` 包
- 认证(登录/会话)在同包,见 [[platform-auth-module]]

## 目录结构

| 模块 | Controller | 核心 Service | 主表 |
|---|---|---|---|
| user | UserController / UserFeignController | UserService/Impl | bp_user |
| role | RoleController / RoleFeignController | RoleService/Impl | bp_role |
| menu | MenuController / MenuFeignController | MenuService/Impl | bp_menu |
| dept | DeptController / DeptFeignController | DeptService/Impl | bp_dept |
| permission(独立包) | ResourcePermissionController | ResourcePermissionService/Impl | bm_resource_permission |

## 数据模型（前缀 `bp_`，数据权限 `bm_`）

| 表 | 实体 | 用途 |
|---|---|---|
| `bp_user` | User(extends BaseUserDO) | 用户主表 |
| `bp_user_role` | UserRelateRole | 用户-角色 |
| `bp_user_dept` | DeptRelateUser | 用户-部门 |
| `bp_role` | Role(id=1 为 admin) | 角色 |
| `bp_role_type` | RoleType | 角色类型(自引用树) |
| `bp_role_menu` | RoleRelateMenu | **功能权限**(角色能用哪些菜单/按钮) |
| `bp_auth_role_menu` | AuthRoleMenu | **可授权权限**(角色能下发哪些给下级) |
| `bp_menu` | Menu(is_menu 区分菜单/按钮) | 菜单+功能(含 terminal_type) |
| `bp_dept` | Dept(code 为祖先链) | 部门树 |
| `bp_dept_role` | DeptRole | 部门-角色 |
| `bm_resource_permission` | ResourcePermission | **数据权限**(资源→部门) |
| `bp_active` | Active | 激活码 |
| `bp_password_history` | PasswordHistory | 密码历史(防重用) |
| `bp_login_log` / `bp_operation_log` | — | 登录/操作日志 |
| `bp_user_sign` / `bp_user_signature_pwd` | — | 手写签名 / 签名密码 |
| `bp_business_parameter` | — | 业务参数(密码规则/锁定时长来源) |

> **表前缀纠正**:platform 业务表是 `bp_`(非 base_sys_),数据权限表是 `bm_resource_permission`。

## 三层权限模型

1. **功能/按钮权限**:与菜单同表 `bp_menu`(`is_menu` 区分),关系存 `bp_role_menu`。查询链 `bp_user_role→bp_role_menu→bp_menu`。
2. **可授权权限**:`bp_auth_role_menu`(平行于 bp_role_menu),决定角色能授予他人哪些权限。
3. **数据权限(部门维度)**:`bm_resource_permission`(resourceId→deptId),把业务资源授权给部门可见。

## 核心 API

**用户(前缀 `/user`)**:`/user/page` 分页 · `/user/save`(默认密码 `Bmos1018`) · `/user/start` 启停(停用级联删角色/部门关联) · `/user/resetPwd` 重置 · `/user/relate-role-save`/`/user/relate-dept-save`(先删后增) · `/user/permission` 按钮权限码下发。

**角色(前缀 `/role`)**:`/role/menu/save` 功能授权(差集 + 补齐根菜单) · `/role/auth/menu/save` 可授权权限(先删后建)。

**菜单(前缀 `/menu`)**:★ **`/menu/auth/tree` 动态菜单下发**(前端路由来源)——admin 走 `selectMenuAdminList` 全量;普通用户走 `selectMenuList`(三表 join 按 userId 过滤)→ buildTree + i18n。

**部门(前缀 `/dept`)**:`/dept/tree-all` · `/dept/partition/tree`(数据权限分区树) · `/dept/user/tree`。

## Feign 暴露（facade，跨服务反查）

| Feign | contextId | 高频方法 |
|---|---|---|
| UserFeign | platform-system-user | `listByUserIds`(批量查用户)、`getUserByName`、`listByMenuIdAndResourceId`(按菜单+资源查数据权限用户) |
| RoleFeign | platform-system-role | `authUserList(authCode)`(按权限码查用户,**审批人查询高频**) |
| MenuFeign | platform-system-menu | `getAllChildMenuIdList` |
| DeptFeign | platform-system-dept | `tree-all` / `user/tree` |

> **鉴权不走 Feign**:token/当前用户校验由 facade auth SDK 在调用方本地完成(见 [[platform-auth-module]])。facade 只暴露**反向**用户查询(菜单/角色/部门/权限码 → 用户)。

## 关键枚举

- `UserStatusEnum`:ON(1)/OFF(0),字段 `state`
- `ActiveEnum`:TO_BE_ACTIVATE(0)/ACTIVATE(1)/PASSWORD_EXPIRED(2)/PASSWORD_LOCK(3),字段 `active_status`
- `menu/enums/TypeEnum`(终端):PLATFORM_WEB(1,100)/PRODUCTION_WEB(2,120)/PRODUCTION_MOBILE(3,121)/AUDIT_WEB(4,111)
- `MenuConstant`:IS_MENU=1/NOT_MENU=0;菜单 id 层级上限 999/999999/99999999
- `RoleConstant`:DEFAULT_ROLE="1"(admin)

## 隐藏地雷 ⚠️

1. **硬编码默认密码** `Bmos1018`(`UserConstant.USER_PASSWORD`)。
2. **admin 强约定**:用户/角色 id=1,SQL 普遍 `id != 1` 硬过滤;`AdminUtil` 在外部 jar。删 id=1 破坏权限。
3. **密码 DES 对称加密**(非单向哈希)。
4. **级联授权靠 menuId 字符串去尾 3 位**推父 id(`cascadeAddMenuRole`),依赖固定 3 位层级编码。
5. **授权保存"先删后建"**(`saveAuthRoleMenu`/`saveMenuRole`),并发/中断可能丢权限(部分有 @DistributedLock)。
6. **停用用户级联删关联**,重新启用需重绑角色/部门。
7. **BaseUserDO/BaseDO 源码已引入**（`bmos-parent-starter` 的 `bmos-starter-mybatis` / `bmos-starter-common`，2026-07-15 引入 shared/，见 [[parent-starter-overview]]）。

## AI 定位提示

- 改 **用户 CRUD/启停/重置密码** → `system/user/`(UserController/UserServiceImpl)
- 改 **角色授权/功能权限** → `system/role/`(`saveRoleMenu` 操作 bp_role_menu)
- 改 **动态菜单下发** → `system/menu/MenuController.getCurrentMenuTree` + `MenuMapper.{selectMenuAdminList,selectMenuList}`
- 改 **数据权限** → `permission/`(ResourcePermissionService,bm_resource_permission)
- 改 **部门树** → `system/dept/`(`getPartitionTree` 按 code 祖先链 startsWith)
- 找 **跨服务查用户/审批人** → facade `UserFeign.listByUserIds` / `RoleFeign.authUserList`

## 相关页面

- [[platform-auth-module]] — 登录/会话/license(同 system/user 包)
- [[auth-and-license]] — 认证全景
- [[service-integration]] — facade Feign 矩阵
- [[database-schema-overview]] — bp_ 表分组
- [[data-access-pattern]] — BaseDO/BaseUserDO 继承
