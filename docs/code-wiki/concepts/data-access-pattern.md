---
title: 数据访问规范（MyBatis-Plus + ShardingSphere）
created: 2026-06-30
updated: 2026-07-15
type: concept
service: cross
tags: [architecture, database, mybatis, shardingsphere, backend]
sources:
  - packages/backend/services/mes/bmos-mes-service/src/main/resources/sharding.yaml
  - packages/backend/services/platform/bmos-platform-service/src/main/resources/sharding.yaml
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/config/sharding/
  - packages/backend/services/platform/bmos-platform-service/src/main/java/com/bmos/platform/service/config/sharding/
status: active
---

# 数据访问规范（MyBatis-Plus + ShardingSphere）

> ⚠️ **底座已引入**：`BaseDO`/`BaseMapperX`/`DefaultDBFieldHandler`/`BasePage`/`CommonPage`/`MybatisAutoConfiguration` 均来自 `bmos-parent-starter` 工程的 `bmos-starter-mybatis` / `bmos-starter-common`，已于 2026-07-15 经 git subtree 引入 `packages/backend/shared/bmos-parent-starter/`（见 [[parent-starter-overview]]）。各服务通过 `@EnableBmosAutoConfiguration` 装配。

## 实体基类 `BaseDO`

`com.bmos.mybatis.dataobject.BaseDO`(abstract,6 字段,所有业务实体 `extends BaseDO`):

| 字段 | 类型 | 填充/注解 |
|---|---|---|
| `id` | Long | `@TableId(ASSIGN_ID)` 雪花 ID |
| `createTime` / `updateTime` | LocalDateTime | INSERT / INSERT_UPDATE |
| `createBy` / `updateBy` | String | INSERT / INSERT_UPDATE(取 `SysUserHolder` 当前用户) |
| `deleted` | Boolean | `@TableLogic`,列名 **`is_deleted`** |

> 逻辑删除列名是 `is_deleted`(不是常见的 deleted/del_flag)。**无 `@Version` 乐观锁、无 `@DS` 多数据源、无读写分离**。

## 自动填充 `DefaultDBFieldHandler`(MetaObjectHandler)

- insertFill:createTime/updateTime(当前时间)、createBy/updateBy(当前 userId)、对 BaseDO 子类补 `draft=false`/`status=false`
- updateFill:updateBy/updateTime

## Mapper 基类

`com.bmos.mybatis.mapper.BaseMapperX<T> extends BaseMapper<T>`:扩展 selectOne/selectCount/selectList(lambda 版)、insertBatch/updateBatch/saveOrUpdate。

- @MapperScan:仅 lims 显式声明 `com.bmos.lims2.server.**.mapper`,其余服务靠 starter 自动装配
- wms 少数 Mapper 用裸 `BaseMapper` + `@Mapper`(如 `IStorageMapper`)

## 分页

**PageHelper(MySQL),非 MyBatis-Plus 分页插件**——`MybatisPlusInterceptor` 只挂了 `EscapeUnderlineSelectInterceptor`(见 [[api-conventions]] 分页节)。

## 表名前缀（@TableName 实证）

| 服务 | 前缀 |
|---|---|
| platform | `bp_` |
| mes | `bm_` |
| wms | `bw_` |
| lims | `lm_`(混入 `bm_`/`bp_` 共享表) |

> ⚠️ 跨服务共享表用"双写":如 `bm_execute_form_data*`/`bm_batch_record_version`(mes 主写,lims eln 子域也写);`bm_resource_permission`(数据权限,归属 platform/lims 却用 `bm_` 前缀)。详见 [[database-schema-overview]]。

## ShardingSphere（5.5.1，仅 mes + platform）

单库纯分表(`!SHARDING`+`!SINGLE`),jdbc url `jdbc:shardingsphere:classpath:sharding.yaml`。lims/wms/gateway **不用**(普通数据源)。

**mes 分片表**(`sharding.yaml`,算法类在 `service/config/sharding/`):

| 表 | 分片键 | 算法 | 表数 |
|---|---|---|---|
| `bm_execute_form_data` | product_plan_id | INLINE `$->{%100}` | 100 |
| `bm_operation_log` | create_time | `OperationLogDivideShadingAlgorithm` | 60 |
| `bm_material_log` | operation_time | `MaterialLogDivideShadingAlgorithm` | 60 |
| `bm_storage_material_position_log` | operate_time | `PositionLogDivideShadingAlgorithm` | 60 |

**platform 分片表**:`bp_operation_log`/`bp_signature_log`(各 60)、`bp_login_log`(12,bindingTable)。算法类在 platform `service/config/sharding/`。

## 配置体系

本地 `application.yml` 极瘦(port/context-path/multipart/pagehelper);`spring.datasource`/`mybatis-plus.*`/logic-delete/redis/xxl-job 全在 **Nacos**(`bootstrap.yml` 拉 `application.yaml` 共享 + `${app}-${profile}.yaml` 独有)。

## 相关页面
- [[database-schema-overview]] — 各服务表分组与前缀映射
- [[api-conventions]] — 分页封装 BasePage/CommonPage
- [[parent-starter-overview]] — 数据访问底座本体（已引入 shared/）、Nacos 配置
- [[mes-overview]] — 分片表实例(日志/流水分 60~100 表)
