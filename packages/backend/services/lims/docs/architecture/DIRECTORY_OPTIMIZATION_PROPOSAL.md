# 目录结构优化方案

## 当前结构问题分析

### 1. **命名不一致问题**
```
bmos-lims2-common/
├── constant/     ❌ 与 constants 重复
├── constants/    ❌ 命名不统一
└── utils/        ❌ 与 util 命名混用
```

### 2. **Feign客户端分散**
```
bmos-lims2-feign/          ✓ 只有1个Feign（IssueMaterialFeign）
bmos-lims2-server/platform/ ❌ 包含大量Feign客户端
├── material/PlatformMaterialFeignClient
├── system/code/PlatformCodeFeign
├── unit/PlatFormUnitFeignClient
├── expression/PlatformExpressionFeignClient
└── expression/user/PlatformUserOpenFeign
```
**问题**：外部平台Feign客户端放在server模块，违反模块职责分离原则

### 3. **配置类分散**
```
bmos-lims2-web/    # 包含配置类（MinioConfig, SwaggerConfig等）
bmos-lims2-server/ # 包含配置类（AsyncConfig等）
```
**问题**：配置类应该集中管理，便于维护

### 4. **文档散落**
```
根目录: 8个.md文件
bmos-lims2-server/: 5个.md文件
```
**问题**：文档缺乏组织，难以查找

### 5. **启动类位置**
```
bmos-lims2-web/BmosLims2Application.java
```
**问题**：按照Spring Boot惯例，启动类应该在独立的启动模块或根包

### 6. **缺少集成层**
**问题**：平台集成（platform包）、审计引擎、流程引擎的适配器代码混在server模块

---

## 优化方案

### 方案A：渐进式优化（推荐，风险低）

适合现有项目快速改进，不改变模块数量。

#### 1. 统一命名规范

**bmos-lims2-common模块重组：**
```
bmos-lims2-common/
└── src/main/java/com/bmos/lims2/common/
    ├── constants/           # 统一：合并constant和constants
    │   ├── RedisConstants
    │   ├── BusinessConstants
    │   └── ...
    ├── enums/              # 保持不变
    │   ├── ExecuteMethodEnum
    │   ├── TaskStatusEnum
    │   └── ...
    ├── exception/          # 新增：集中异常定义
    │   ├── BusinessException
    │   ├── LimsResponseCode
    │   └── ...
    ├── i18n/              # 保持不变
    ├── model/             # 重命名为 dto （更准确）
    │   └── base/          # 基础DTO
    │       ├── BaseDO
    │       ├── BaseDTO
    │       └── BaseVO
    └── util/              # 统一：重命名为 util（单数）
        ├── DateUtil
        ├── StringUtil
        └── ...
```

**改进点：**
- ✅ 合并 constant 和 constants 为统一的 constants
- ✅ 重命名 utils → util
- ✅ 新增 exception 包，集中管理异常
- ✅ model → dto/base，语义更清晰

#### 2. Feign客户端整合

**bmos-lims2-feign模块重组：**
```
bmos-lims2-feign/
└── src/main/java/com/bmos/lims2/feign/
    ├── internal/                    # 内部服务间调用
    │   └── material/
    │       ├── IssueMaterialFeign
    │       └── dto/
    │           ├── RemoteIssueFeignDTO
    │           └── MaterialIssueFeignDTO
    │
    ├── platform/                    # 平台集成（从server迁移）
    │   ├── material/
    │   │   ├── PlatformMaterialFeignClient
    │   │   └── dto/
    │   │       ├── MaterialIssueDTO
    │   │       ├── SyncMaterialInfoDTO
    │   │       └── ...
    │   ├── code/
    │   │   ├── PlatformCodeFeignClient
    │   │   └── dto/
    │   │       ├── CodeRuleVO
    │   │       └── NextCodeVO
    │   ├── unit/
    │   │   ├── PlatformUnitFeignClient
    │   │   └── dto/
    │   ├── user/
    │   │   ├── PlatformUserOpenFeign
    │   │   └── dto/
    │   ├── expression/
    │   │   ├── PlatformExpressionFeignClient
    │   │   └── vo/
    │   └── parameter/
    │       ├── PlatformParameterClient
    │       └── vo/
    │
    └── external/                    # 外部系统集成（预留）
        └── mes/
            └── MesFeignClient
```

**迁移步骤：**
1. 将 `bmos-lims2-server/platform/*FeignClient` 移至 `bmos-lims2-feign/platform/`
2. 保留 `bmos-lims2-server/platform/util/` (工具类留在server)
3. 更新import路径

**改进点：**
- ✅ Feign客户端职责清晰：internal（内部） | platform（平台） | external（外部）
- ✅ 所有远程调用接口集中管理
- ✅ DTO随Feign接口组织，便于版本管理

#### 3. 配置类集中管理

**bmos-lims2-server/config重组：**
```
bmos-lims2-server/
└── src/main/java/com/bmos/lims2/server/config/
    ├── async/
    │   ├── AsyncConfig
    │   └── ReportAsyncProperties
    ├── audit/
    │   ├── AuditEngineConfiguration
    │   └── AuditSendMessageConfiguration
    ├── cache/
    │   └── CacheConfiguration (预留)
    ├── datasource/
    │   └── DataSourceConfiguration (预留)
    ├── expression/
    │   └── ExecutionDataCalculateConfiguration
    ├── minio/
    │   ├── MinioConfig
    │   └── MinioBucket
    ├── mybatis/
    │   └── MyBatisPlusConfiguration (预留)
    ├── security/
    │   └── SecurityConfiguration (预留)
    └── user/
        └── UserUtilConfiguration
```

**bmos-lims2-web/config：**
```
bmos-lims2-web/
└── src/main/java/com/bmos/lims2/web/config/
    ├── swagger/
    │   ├── SwaggerConfiguration
    │   └── BmosQuerySwaggerProcessor
    ├── web/
    │   ├── CorsConfiguration
    │   └── WebMvcConfiguration
    └── exception/
        ├── GlobalExceptionHandler
        ├── MesExceptionHandler
        └── MesAuditExceptionHandler
```

**原则：**
- server/config: 业务相关配置（数据库、缓存、业务引擎）
- web/config: Web层配置（Swagger、CORS、异常处理）

#### 4. 文档结构整理

**根目录重组：**
```
bmos-lims2-service/
├── docs/                              # 新建文档目录
│   ├── architecture/
│   │   ├── CLAUDE.md                  # 开发指南
│   │   ├── ELN_LIMS_INTEGRATION.md   # ELN集成说明
│   │   └── DIRECTORY_STRUCTURE.md     # 目录结构说明
│   ├── api/
│   │   ├── API_DOCUMENTATION.md
│   │   ├── INSPECTION_SCHEME_DROPDOWN_API.md
│   │   └── ...
│   ├── implementation/
│   │   ├── INSPECTION_ENTRY_IMPLEMENTATION.md
│   │   ├── ANALYSIS_ITEM_HIERARCHY_LIST_IMPLEMENTATION.md
│   │   ├── ASSIGNABLE_USERS_QUERY_IMPLEMENTATION.md
│   │   └── MAPPER_REFACTOR_SUMMARY.md
│   └── technical/
│       ├── PDF_IMPLEMENTATION_GUIDE.md
│       ├── PDF_DEPENDENCIES.md
│       ├── PLATFORM_CODE_RULE_INTEGRATION.md
│       └── RECORD_MODULE_SPEC.md
│
├── README.md                          # 项目总览
├── CHANGELOG.md                       # 变更日志（新建）
└── pom.xml
```

**改进点：**
- ✅ 文档分类清晰：架构/API/实现/技术
- ✅ 便于查找和维护
- ✅ 支持文档版本化

#### 5. 业务域模块标准化

**每个业务域统一结构：**
```
bmos-lims2-server/[domain]/
├── entity/              # 实体类
│   ├── [Entity].java
│   └── ...
├── mapper/              # MyBatis Mapper
│   ├── [Entity]Mapper.java
│   └── xml/            # 复杂SQL（可选子目录）
│       └── [Entity]Mapper.xml
├── service/             # 服务接口
│   ├── [Entity]Service.java
│   └── impl/
│       └── [Entity]ServiceImpl.java
├── dto/                 # 数据传输对象
│   ├── [Entity]DTO.java
│   ├── [Entity]SaveDTO.java
│   ├── [Entity]QueryDTO.java
│   └── ...
├── converter/           # Entity ↔ DTO 转换
│   └── [Entity]Converter.java
├── constant/            # 领域常量（可选）
│   └── [Domain]Constants.java
└── enums/              # 领域枚举（可选）
    └── [Domain]Enum.java
```

**Web模块标准化：**
```
bmos-lims2-web/[domain]/
├── [Entity]Controller.java
├── converter/                    # VO ↔ DTO 转换
│   └── [Entity]WebConverter.java
└── vo/
    ├── req/                      # 请求VO
    │   ├── [Entity]SaveReqVO.java
    │   ├── [Entity]UpdateReqVO.java
    │   └── [Entity]QueryReqVO.java
    └── resp/                     # 响应VO
        ├── [Entity]RespVO.java
        └── [Entity]DetailRespVO.java
```

---

### 方案B：模块化重构（激进，适合长期规划）

增加新模块，职责更清晰，但需要较大改动。

#### 新增模块结构

```
bmos-lims2-service/
├── bmos-lims2-common/           # 公共基础
├── bmos-lims2-api/              # 🆕 API接口定义（DTO/VO）
│   ├── feign-api/              # Feign接口定义
│   └── web-api/                # REST API接口定义
├── bmos-lims2-integration/      # 🆕 外部集成层
│   ├── platform-integration/   # 平台集成
│   ├── audit-engine/           # 审计引擎适配
│   ├── flow-engine/            # 流程引擎适配
│   └── mes-integration/        # MES集成
├── bmos-lims2-domain/           # 🆕 领域模型（实体+值对象）
├── bmos-lims2-infrastructure/   # 🆕 基础设施层
│   ├── persistence/            # 持久化（Mapper）
│   ├── cache/                  # 缓存实现
│   └── mq/                     # 消息队列
├── bmos-lims2-application/      # 🆕 应用服务层（原server）
│   ├── inspect/
│   ├── eln/
│   └── ...
├── bmos-lims2-web/              # Web接口层
└── bmos-lims2-starter/          # 🆕 启动模块
    └── BmosLims2Application.java
```

**优势：**
- 符合DDD分层架构
- 职责边界清晰
- 便于独立演进和测试

**劣势：**
- 改动量大
- 需要团队统一认识
- 需要重构大量import

---

## 实施建议

### 推荐路径：渐进式优化

**Phase 1：快速改进（1-2周）**
1. ✅ 统一命名（constant/constants → constants）
2. ✅ 整理文档到 docs/ 目录
3. ✅ 配置类分类整理

**Phase 2：模块重组（2-3周）**
4. ✅ Feign客户端迁移到 feign 模块
5. ✅ Platform工具类整理
6. ✅ 异常类集中到 common/exception

**Phase 3：标准化（持续）**
7. ✅ 业务域目录结构标准化
8. ✅ Converter命名和位置标准化
9. ✅ 补充缺失的README和CHANGELOG

### 不推荐（当前阶段）

❌ 方案B的模块化重构
- 原因：改动量大，风险高，ROI低
- 建议：仅作为长期架构演进方向

---

## 具体操作步骤

### 1. 统一命名（低风险）

**Step 1.1: 合并 constant 和 constants**
```bash
# 1. 创建新目录
mkdir -p bmos-lims2-common/src/main/java/com/bmos/lims2/common/constants

# 2. 合并文件（检查是否有重复类名）
# 手动审查两个目录的内容，合并到 constants/

# 3. 全局替换import
# 使用IDE的"Replace in Path"功能
# 旧：com.bmos.lims2.common.constant.
# 新：com.bmos.lims2.common.constants.

# 4. 删除旧目录
rm -rf bmos-lims2-common/src/main/java/com/bmos/lims2/common/constant
```

**Step 1.2: 重命名 utils → util**
```bash
# 1. 重命名目录
mv bmos-lims2-common/src/main/java/com/bmos/lims2/common/utils \
   bmos-lims2-common/src/main/java/com/bmos/lims2/common/util

# 2. 全局替换import
# 旧：com.bmos.lims2.common.utils.
# 新：com.bmos.lims2.common.util.
```

### 2. Feign客户端迁移（中等风险）

**Step 2.1: 迁移Platform Feign**
```bash
# 1. 在feign模块创建platform包
mkdir -p bmos-lims2-feign/src/main/java/com/bmos/lims2/feign/platform

# 2. 移动文件（保持子包结构）
mv bmos-lims2-server/src/main/java/com/bmos/lims2/server/platform/material/*Feign* \
   bmos-lims2-feign/src/main/java/com/bmos/lims2/feign/platform/material/

# 重复上述步骤迁移其他Feign客户端：code, unit, user, expression, parameter

# 3. 更新import路径
# 旧：com.bmos.lims2.server.platform.material.PlatformMaterialFeignClient
# 新：com.bmos.lims2.feign.platform.material.PlatformMaterialFeignClient

# 4. 更新maven依赖
# 在使用这些Feign的模块(server, web)的pom.xml中确保依赖feign模块
```

**Step 2.2: 保留platform/util**
```bash
# platform/util包含工具类，不是Feign，保留在server模块
# 只迁移 *Feign* 和 *FeignClient 类
```

### 3. 文档整理（零风险）

```bash
# 1. 创建docs目录结构
mkdir -p docs/{architecture,api,implementation,technical}

# 2. 移动文档
mv CLAUDE.md docs/architecture/
mv ELN_LIMS_INTEGRATION.md docs/architecture/
mv API_DOCUMENTATION.md docs/api/
mv INSPECTION_SCHEME_DROPDOWN_API.md docs/api/
mv INSPECTION_ENTRY_IMPLEMENTATION.md docs/implementation/
mv ANALYSIS_ITEM_HIERARCHY_LIST_IMPLEMENTATION.md docs/implementation/
mv ASSIGNABLE_USERS_QUERY_IMPLEMENTATION.md docs/implementation/
mv MAPPER_REFACTOR_SUMMARY.md docs/implementation/
mv RECORD_MODULE_SPEC.md docs/technical/
mv bmos-lims2-server/PDF_IMPLEMENTATION_GUIDE.md docs/technical/
mv bmos-lims2-server/PDF_DEPENDENCIES.md docs/technical/
mv bmos-lims2-server/PLATFORM_CODE_RULE_INTEGRATION.md docs/technical/
mv bmos-lims2-server/INSPECTION_EXECUTION_LIST.md docs/technical/
mv bmos-lims2-server/INSPECTION_SCHEME_SNAPSHOT.md docs/technical/

# 3. 创建docs索引
cat > docs/README.md << 'EOF'
# BMOS LIMS2 文档目录

## 架构文档 (architecture/)
- [CLAUDE.md](architecture/CLAUDE.md) - 开发指南
- [ELN_LIMS_INTEGRATION.md](architecture/ELN_LIMS_INTEGRATION.md) - ELN与LIMS集成

## API文档 (api/)
- [API_DOCUMENTATION.md](api/API_DOCUMENTATION.md)
- [INSPECTION_SCHEME_DROPDOWN_API.md](api/INSPECTION_SCHEME_DROPDOWN_API.md)

## 实现文档 (implementation/)
- 各功能实现细节说明

## 技术文档 (technical/)
- 技术方案和集成指南
EOF
```

### 4. 配置类整理（低风险）

**Step 4.1: Server配置分类**
```bash
# 在server/config下创建子目录
cd bmos-lims2-server/src/main/java/com/bmos/lims2/server/config
mkdir -p async audit expression minio user

# 移动配置类到对应子目录（需手动操作）
# 移动后更新package声明和import
```

**Step 4.2: Web配置分类**
```bash
# 在web模块创建config目录（如不存在）
cd bmos-lims2-web/src/main/java/com/bmos/lims2/web
mkdir -p config/{swagger,web,exception}

# 移动配置类（需手动操作）
```

---

## 验证清单

### 编译验证
```bash
# 1. 清理并重新编译
mvn clean install

# 2. 检查编译错误
# 主要关注import错误

# 3. 运行测试（如果有）
mvn test
```

### 功能验证
- [ ] 启动应用成功
- [ ] Swagger文档可访问
- [ ] Feign调用正常
- [ ] 数据库操作正常
- [ ] 业务功能正常

### 代码检查
- [ ] 无未使用的import
- [ ] 包名和目录结构一致
- [ ] 命名符合规范
- [ ] 文档链接有效

---

## 预期收益

### 短期收益（Phase 1-2）
1. ✅ 命名统一，减少混淆
2. ✅ 文档易于查找和维护
3. ✅ Feign客户端集中管理
4. ✅ 配置分类清晰

### 长期收益
1. ✅ 新人上手更快（文档完善）
2. ✅ 模块职责清晰（易于测试）
3. ✅ 代码可维护性提升
4. ✅ 为微服务拆分打基础

---

## 风险评估

| 操作 | 风险等级 | 影响范围 | 缓解措施 |
|------|---------|---------|---------|
| 统一命名 | 低 | 全局import | 使用IDE重构功能，分支验证 |
| Feign迁移 | 中 | Feign调用方 | 先在dev分支验证，充分测试 |
| 文档整理 | 零 | 无代码影响 | 直接执行 |
| 配置整理 | 低 | 配置扫描 | 确保Spring能扫描到 |
| 模块重构 | 高 | 整体架构 | 不推荐当前执行 |

---

## 总结

**推荐执行：方案A（渐进式优化）**
- 低风险，高收益
- 不破坏现有架构
- 可分阶段实施
- 立即改善代码质量

**关键原则：**
1. 🔥 **优先整理，再重构**
2. 🔥 **小步快跑，快速验证**
3. 🔥 **文档先行，降低风险**
4. 🔥 **团队共识，统一执行**
