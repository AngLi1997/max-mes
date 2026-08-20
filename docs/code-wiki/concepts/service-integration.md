---
title: 服务间集成（Feign 调用链路）
created: 2026-06-29
updated: 2026-07-06
type: concept
service: cross
tags: [backend, integration, feign, architecture, api]
sources:
  - packages/backend/services/platform/bmos-platform-facade/
  - packages/backend/services/mes/bmos-mes-feign/
  - packages/backend/services/mes/bmos-mes-service/src/main/java/com/bmos/mes/service/platform/
  - packages/backend/services/lims/bmos-lims2-feign/
  - packages/backend/services/lims/bmos-lims2-server/src/main/java/com/bmos/lims2/server/platform/
  - packages/backend/services/wms/bmos-wms-feign/
  - packages/backend/services/wms/bmos-wms-service/src/main/java/com/bmos/wms/service/platform/
status: active
---

# 服务间集成（Feign 调用链路）

> bmos-monorepo 后端 5 个服务通过 Spring Cloud **Feign** 同步通信，所有 @FeignClient 通过 Nacos 服务发现（`name` 即 Nacos 注册名）。本页梳理调用方向、契约模块和已知问题。
> 数据来源：源码扫描 `@FeignClient` 注解（2026-06-29）。

## TL;DR

1. **底座方向**：mes / lims / wms → **platform**（最稠密的依赖方向）。
2. **横向方向**：mes ↔ wms（领料/检验）、lims → mes / wms（检验回调）。
3. **反向方向**：platform → mes / lims / wms（消息通知回调）。
4. **AI 在跨服务定位时**：看到 `@FeignClient(name="bmos-xxx-service")` → 直接跳到对应服务源码。

## 通信契约总览

### Nacos 服务名（@FeignClient 调用目标）

| 服务 | Nacos 注册名 | 调用目标写法 |
|------|--------------|--------------|
| platform | `bmos-platform-service` | `@FeignClient(name="bmos-platform-service", ...)` |
| mes | `bmos-mes-service` | `@FeignClient(name="bmos-mes-service", ...)` |
| lims | `bmos-lims2-service` | `@FeignClient(name="bmos-lims2-service", ...)`（**注意是 lims2 不是 lims**） |
| wms | `bmos-wms-service` | `@FeignClient(name="bmos-wms-service", ...)` |
| gateway | `bmos-gateway-service` | （网关本身不接受 Feign 调用） |

> **contextId 必填**：所有 @FeignClient 都加了 `contextId="xxx"` 以避免同名 bean 冲突——多个 client 调用同一目标服务时这是必需的。新建 Feign 接口请遵循此惯例。

### 两种契约模块组织方式

```
模式 A（platform）：facade 模块 = Feign + 共享 DTO/VO/Enum
  bmos-platform-facade/  ← 其它服务 maven 依赖此模块即可调用
    └── auth/feign/ActiveValidFeign.java
    └── system/user/feign/UserFeign.java
    ...

模式 B（mes/lims/wms）：feign 模块 = 纯 Feign 接口
  bmos-mes-feign/        ← 其它服务依赖此模块调 mes
    └── inspect/feign/InspectFeign.java
    └── material/feign/MaterialFeign.java
```

> 命名差异：platform 用 **`-facade`**，业务服务用 **`-feign`**。两者用途相同——对外暴露契约 + 跨服务共享 DTO。

## 调用关系矩阵

> ✅ 表示在源码中找到 `@FeignClient` 调用。括号内为客户端数量。

| 调用方 ↓ \ 被调方 → | platform | mes | lims | wms | 备注 |
|---|---|---|---|---|---|
| **platform** | (自调 14) | ✅ (1) | ✅ (1+1) | ✅ (1) | 反向通知 + 消息回调 |
| **mes** | ✅ (10) | (自调 3) | — | ✅ (1) | 领料调 wms |
| **lims** | ✅ (6) | ✅ (1) | (自调 2) | ✅ (1) | 检验回调 mes/wms |
| **wms** | ✅ (5) | ✅ (1) | — | (自调 1) | — |

### platform-facade 对外暴露的 14 个 Feign（业务服务调入口）

| Feign | 包路径 | 契约用途 |
|-------|--------|----------|
| `ActiveValidFeign` | `facade/auth/feign/` | license 激活校验 |
| `UserFeign` / `UserSignFeign` | `facade/system/user/feign/` | 用户、用户签名 |
| `RoleFeign` / `MenuFeign` / `DeptFeign` | `facade/system/{role,menu,dept}/feign/` | RBAC |
| `DictFeign` | `facade/dict/feign/` | 数据字典 |
| `CodeRuleFeign` | `facade/code/feign/` | 单号生成规则 |
| `BusinessParameterFeign` | `facade/system/execute/parameter/feign/` | 业务参数 |
| `EquipmentConfigFeign` | `facade/equipment/feign/` | 设备配置 |
| `FactoryFeign` / `FactoryAppFeign` | `facade/factory/feign/` | 工厂/产线结构 |
| `PlatformMaterialFeign` | `facade/material/feign/` | 物料 |
| `MessageNotifyFeign` | `facade/notify/` | 消息通知 |

详见 [[platform-overview]]。

### mes/lims/wms 自定义的 Platform Feign（重复定义 ⚠️）

各业务服务在 `service/platform/<域>/feign/` 下又**重新定义**了一份 Feign 调 platform：

| 业务服务 | 自定义 Platform Feign | 备注 |
|---|---|---|
| mes (10) | `PlatformCodeFeign` `DictClient` `PlatformExpressionFeignClient` `PlatformParameterClient` `PlatformCodeRuleClient` `PlatformTagClient` `PlatformMaterialFeignClient` `PlatformRoleOpenFeign` `PlatformUnitFeignClient` `PlatformUserOpenFeign` | 调 `bmos-platform-service` |
| lims (6) | `PlatformExpressionFeignClient` `PlatformUserOpenFeign` `PlatformMaterialFeignClient` `PlatformParameterClient` `PlatformCodeFeign` `PlatFormUnitFeignClient` | 调 `bmos-platform-service` |
| wms (5) | `PlatformCodeFeign` `PlatformMaterialFeignClient` `PlatformParameterClient` `PlatformUserOpenFeign` `PlatformCodeFeign` | 调 `bmos-platform-service` |

> ⚠️ **技术债**：这些和 `platform-facade` 的 14 个标准 Feign **功能重叠**（如自定义的 `PlatformUserOpenFeign` vs facade 的 `UserFeign`；自定义的 `PlatformMaterialFeignClient` vs facade 的 `PlatformMaterialFeign`）。是迁移期/历史遗留产物，**新代码统一走 facade**，旧的逐步迁移收敛。

### 横向调用

| 来源 → 目标 | 客户端类 | 业务场景 |
|---|---|---|
| **mes → wms** | `mes-service/.../requisition/feign/WmsFeignClient.java` | 领料请求 |
| **wms → mes** | `wms-service/.../service/mes/feigns/MesFeignClient.java` | （反向集成） |
| **lims → mes** | `lims2-server/.../inspect/mes/client/MesInspectCallbackClient.java` | 检验结果回调 mes |
| **lims → wms** | `lims2-server/.../inspect/mes/client/WmsInspectCallbackClient.java` | 检验结果回调 wms |

### 反向调用（platform → 业务服务）

`platform-service/.../service/feign/` 与 `.../system/message/feign/`：

| 客户端类 | 调用目标 | 用途 |
|---|---|---|
| `MesFeignClient` | `bmos-mes-service` | 反向取数 |
| `LimsFeignClient` | `bmos-lims2-service` | 反向取数 |
| `WmsFeignClient` | `bmos-wms-service` | 反向取数 |
| `MesMessageFeignClient` | `bmos-mes-service` | 消息推送 |
| `LimsMessageFeignClient` | `bmos-centralization-lims-service` ⚠️ | 见下方悬空依赖 |
| `PlasmaMessageFeignClient` | `bmos-plasma-service` ⚠️ | 见下方悬空依赖 |

## ⚠️ 已知问题：悬空 Feign 依赖

platform 内有 2 个 @FeignClient 指向**当前 monorepo 不存在**的服务：

| 客户端类 | 调用目标 | 说明 |
|---|---|---|
| `LimsMessageFeignClient` | `bmos-centralization-lims-service` | 不在当前 5 服务清单中 |
| `PlasmaMessageFeignClient` | `bmos-plasma-service` | 不在当前 5 服务清单中 |

> 这些是 Monorepo 整合前外部服务的调用契约。运行时若 Nacos 未注册对应服务，调用会失败但不影响主流程（消息通知降级）。后续整合更多服务时需补齐，或移除调用代码。

## 关键约定

1. **新建 Feign 接口**：
   - 放在调用方服务的 `*-feign` 或 `*-facade` 模块（不要放在 `-service` 内部）
   - `@FeignClient(name="<Nacos注册名>", contextId="<唯一-id>")` 两个属性必填
   - `contextId` 命名建议：`<本服务>-<业务域>` 或 `<目标服务>-<业务域>`，全局不重复
2. **跨服务取数据**：禁止直连其它服务的数据库表，统一走 Feign
3. **优先使用 facade**：调 platform 优先用 `bmos-platform-facade` 的标准 Feign，不要自己再定义 `Platform*Client`
4. **错误处理**：Feign 调用应有 Fallback 或全局熔断（具体策略见 bmos-cloud-dependency 的 Feign autoconfig）

## AI 定位提示

- 看到日志/错误中出现 Nacos 服务名（`bmos-xxx-service`）→ 查本页矩阵反查目标服务
- 看到 `@FeignClient(name="bmos-platform-service", ...)` → 实现在 platform 的 `*FeignController`（如 `UserFeignController` 实现 `UserFeign`）
- 排查跨服务调用问题：先看 `contextId` 唯一性，再看 Nacos 是否注册，再看接口路径是否匹配

## 相关页面

- [[service-overview]] — 服务端口/注册名速查
- [[platform-overview]] — platform 作为被调底座的详细 facade 清单
- [[auth-and-license]] — `ActiveValidFeign` 在 license 校验中的具体使用
- [[database-schema-overview]] — 跨服务复用表（与 Feign 调用形成互补的数据流）
