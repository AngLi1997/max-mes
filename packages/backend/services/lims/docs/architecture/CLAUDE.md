# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

### Build
```bash
# Build entire project
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Build specific module
cd bmos-lims2-web && mvn clean package

# Run application (from bmos-lims2-web module)
cd bmos-lims2-web && mvn spring-boot:run
```

### Maven Compiler Configuration
The project uses Maven compiler plugin with Lombok + MapStruct annotation processing. Generated MapStruct implementations are placed in `target/generated-sources/annotations/`.

## Multi-Module Architecture

This is a 4-module Maven project with strict dependency and data flow patterns:

```
bmos-lims2 (parent)
├── bmos-lims2-common    # Constants, enums, exceptions
├── bmos-lims2-feign     # Inter-service communication interfaces
├── bmos-lims2-server    # Business logic & data access (depends on feign + common)
└── bmos-lims2-web       # REST API layer (depends on server, is the deployable artifact)
```

**Module Dependency Rules:**
- `web` → `server` → `feign` → `common`
- Each module only imports what it needs
- `bmos-lims2-web` is the final deployable Spring Boot application

## Data Flow Pattern

The codebase follows a strict 3-layer conversion pattern:

```
HTTP Request → Controller (VO) → Service (DTO) → Mapper (Entity) → Database
                    ↓                  ↓              ↓
              WebConverter      ServerConverter   MyBatis
              (MapStruct)       (MapStruct)       (XML/API)
```

**Critical Rules:**
1. **Controller Layer** (`bmos-lims2-web`):
   - Uses `*ReqVO` for requests, `*RespVO` for responses
   - Must convert VO ↔ DTO using WebConverters (MapStruct)
   - Returns `ResponseInfo<T>` wrapper
   - Only handles HTTP concerns, NO business logic

2. **Service Layer** (`bmos-lims2-server`):
   - Uses `*DTO` for all parameters and returns
   - NEVER accepts VO types
   - Converts Entity ↔ DTO using ServerConverters
   - Contains all business logic

3. **Data Layer** (`bmos-lims2-server`):
   - Mappers extend `BaseMapperX<Entity>` (custom MyBatis-Plus base)
   - Single-table queries return Entity objects
   - Complex/join queries return DTO objects
   - Use `LambdaQueryWrapperX` for type-safe queries

**Example Pattern:**
```java
// Controller (web module)
@PostMapping("/save")
public ResponseInfo<Void> save(@RequestBody @Validated MaterialSaveReqVO vo) {
    MaterialDTO dto = MaterialWebConverter.INSTANCE.toDTO(vo);
    materialService.save(dto);
    return ResponseInfo.success();
}

// Service Interface (server module)
void save(MaterialDTO dto);

// Service Implementation
@Transactional(rollbackFor = Exception.class)
public void save(MaterialDTO dto) {
    Material entity = MaterialConverter.INSTANCE.toEntity(dto);
    materialMapper.insert(entity);
}
```

## Business Domain Organization

Each domain follows the same package structure within `bmos-lims2-server`:

```
com.bmos.lims2.server.[domain]/
├── entity/          # Database entities (@TableName, extends BaseDO)
├── mapper/          # MyBatis mappers (extend BaseMapperX<Entity>)
├── service/         # Service interfaces (use DTOs only)
│   └── impl/        # Service implementations (@Service, @Transactional)
├── dto/             # Data transfer objects
└── converter/       # Entity ↔ DTO converters (MapStruct, non-Spring)
```

Web module mirrors this but only has:
```
com.bmos.lims2.web.[domain]/
├── XxxController.java  # REST endpoints (@RestController, @Api)
├── converter/          # VO ↔ DTO converters (MapStruct with Spring)
└── vo/
    ├── req/            # Request VOs (*ReqVO)
    └── resp/           # Response VOs (*RespVO)
```

**Major Business Domains:**
- `inspect.*` - Core inspection/test management (orders, samples, schemes, audit)
- `eln.*` - Electronic Lab Notebook (records, conclusions, signatures)
- `material.*` - Material/product management
- `sample.*` - Sample tracking & ledgers
- `report.*` - Test report generation & approval
- `audit.*` - Workflow approval engine integration
- `task.*` - Task management
- `operate.*` - Operating procedures

## MapStruct Converter Patterns

**Web Converters** (Spring-managed):
```java
@Mapper(componentModel = "spring")
public interface InspectionOrderWebConverter {
    InspectionOrderWebConverter INSTANCE = Mappers.getMapper(InspectionOrderWebConverter.class);

    @Mapping(source = "fieldA", target = "fieldB")
    InspectionOrderDTO toDTO(InspectionOrderReqVO vo);
}
```

**Server Converters** (not Spring-managed):
```java
@Mapper  // No componentModel
public interface InspectionOrderConverter {
    InspectionOrderConverter INSTANCE = Mappers.getMapper(InspectionOrderConverter.class);

    InspectionOrder toEntity(InspectionOrderDTO dto);
}
```

Generated implementations are in `target/generated-sources/annotations/`.

## Entity & Database Rules

**All entities must:**
1. Extend `BaseDO` (provides id, createTime, updateTime, createBy, updateBy, deleted)
2. Use `@TableName("table_name")` for table mapping
3. Use `@TableId(type = IdType.ASSIGN_ID)` for Snowflake ID generation
4. Use `@TableLogic` on the `deleted` field
5. Use `@TableField(fill = FieldFill.INSERT)` for auto-filled fields

**MyBatis Mapper Rules:**
1. Extend `BaseMapperX<Entity>` for CRUD operations
2. Use `@Mapper` annotation
3. Simple queries use MyBatis-Plus API with `LambdaQueryWrapperX`
4. Complex queries use XML mappers in `src/main/resources/mapper/[domain]/`
5. Join queries must return DTO types, not entities
6. XML queries must specify main table fields in SELECT for common columns (e.g., `t1.id, t1.create_time`)

## Flyway Database Migrations

Scripts are in `src/main/resources/init/db/` following strict naming:

```
V<major>.<minor>.<patch>_<revision>__<description>.sql
Example: V1.0.0_0.0.1__bmos_lims_db_init.sql
```

**All tables must have base fields:**
```sql
id bigint(20) NOT NULL COMMENT '主键ID',
create_time datetime NOT NULL COMMENT '创建时间',
update_time datetime NOT NULL COMMENT '更新时间',
create_by varchar(50) NOT NULL COMMENT '创建人',
update_by varchar(50) NOT NULL COMMENT '更新人',
is_deleted tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除'
```

**Naming conventions:**
- Tables: `lm_[domain]_[entity]` (e.g., `lm_inspect_material`)
- Indexes: `idx_[field]` (normal), `uk_[field]` (unique)
- Columns: lowercase with underscores

## Audit Engine Integration

The codebase integrates with BMOS Audit Engine (0.0.3-SNAPSHOT) for workflow approvals.

**Strategy Pattern for Audit Categories:**
- Defined in `AuditCategoryServiceEnum`
- Each category (方案审批, 样品审核, 报告审批, etc.) has a strategy class
- Strategies extend `AbstractAuditMessageStrategy`
- Examples: `InspectSchemeStrategy`, `SampleAuditStrategy`, `ReportAuditStrategy`

**Event Listeners** (registered in configuration):
- `AuditFlowExecutionEndListener` - USER_TASK completion
- `AuditFlowProcessEndListener` - Process completion
- `AuditFlowProcessRejectEndListener` - Rejection handling
- `AuditFlowNodeExecutionListener` - Node execution tracking
- `AuditFlowBackToPrevListener` - Back-to-previous actions

**Approval Flow:**
1. Service initiates audit via AuditMessageSender
2. Strategy determines approval users based on data permissions
3. Listeners handle state changes
4. Business entities updated on completion/rejection

## Feign Client Integration

**Internal Feign** (`bmos-lims2-feign` module):
```java
@FeignClient(name = "bmos-lims2-service", contextId = "bmos-lims2-material")
public interface IssueMaterialFeign {
    @PostMapping("/material/issueMaterialAndCategory")
    ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueFeignDTO dto);
}
```

**External Platform Feign Clients** (in `bmos-lims2-server/platform`):
- `PlatformMaterialFeignClient` - Material sync
- `PlatformCodeFeignClient` - Code generation rules
- `PlatformUserOpenFeign` - User information
- `MessageNotifyFeign` - Message notifications

All Feign responses wrapped in `ResponseInfo<T>`.

## Coding Conventions

**Naming:**
- Classes: PascalCase (`MaterialService`, `MaterialController`)
- Methods: camelCase (`getById`, `saveInspectionProduct`)
- Constants: UPPER_SNAKE_CASE (`DEFAULT_PAGE_SIZE`)
- Packages: lowercase, singular form (`material`, not `materials`)

**Annotations:**
- Controllers: `@RestController`, `@RequestMapping`, `@Api`, `@Validated`
- Services: `@Service`, `@Slf4j`, `@Transactional(rollbackFor = Exception.class)`
- Entities: `@Getter`, `@Setter`, `@TableName`
- VOs/DTOs: `@Data`, `@ApiModel`, `@ApiModelProperty`

**Parameter Validation:**
- Controller: `@Valid`, `@NotBlank`, `@NotNull`, `@Length`
- Service: business rule validation with `BusinessException`
- Never use more than 5 parameters - create a DTO/VO wrapper

**Transaction Management:**
- All multi-table operations must use `@Transactional(rollbackFor = Exception.class)`
- Avoid nested transactions
- Keep transaction boundaries tight

**Class-Level Comments:**
```java
/**
 * @Description: 检品信息业务实现类
 * @Author: yigaohui
 * @Date: 2024/02/26 20:14
 */
```

**Method-Level Comments:**
```java
/**
 * 根据条件分页查询检品信息
 * @param queryDTO 查询条件
 * @return 分页结果
 */
```

## Application Configuration

**Server:**
- Port: 61001
- Context Path: `/api/app/lims2`
- Max Upload Size: 20MB

**Spring Boot Annotations:**
```java
@EnableBmosAutoConfiguration
@EnableBmosExpressionAutoConfiguration
@EnableFeignClients(basePackages = {"com.bmos.lims2.*"})
@EnableDiscoveryClient
@EnableBmosAuth
@EnableBmosLogAutoConfiguration
@EnableAuditEngineAutoConfiguration
@EnableProcessEngineAutoConfiguration
@MapperScan(basePackages = {"com.bmos.lims2.server.**.mapper"})
```

**Configuration Sources:**
- Nacos configuration center (bootstrap.yml)
- Imports: `application.yaml`, `bmos-redis.yaml`, `bmos-xxl-job.yaml`, `log-properties.yaml`

## Key Technology Stack

- **Framework:** Spring Boot 2.6.15, Spring Cloud
- **Database:** MySQL 8.0.32, MyBatis-Plus 3.5.3.2
- **Service Discovery:** Nacos 2.0+
- **Object Mapping:** MapStruct 1.4.1.Final
- **Code Generation:** Lombok 1.18.20
- **API Docs:** Swagger/OpenAPI
- **Caching:** Redis 6.0+
- **Messaging:** RocketMQ
- **File Storage:** MinIO 8.2.2
- **Workflow:** BMOS Audit Engine 0.0.3, BMOS Flow Engine 0.0.5
- **Excel:** EasyExcel 3.3.2
- **PDF:** Aspose Words, Flying Saucer, OpenPDF, PDFBox 2.0.27

## Common Pitfalls

1. **DO NOT** use VO types in Service layer - always use DTO
2. **DO NOT** put business logic in Controller - only parameter conversion
3. **DO NOT** return Entity from join queries - create a DTO
4. **DO NOT** forget `@Transactional(rollbackFor = Exception.class)` on multi-table operations
5. **DO NOT** use circular references between converters
6. **DO NOT** skip WebConverter when passing data between Controller and Service
7. **DO NOT** use magic numbers - define constants in separate classes
8. **DO NOT** batch operations in loops - use MyBatis-Plus batch methods
9. **DO NOT** forget to specify main table fields in JOIN queries for common columns (id, create_time, etc.)
10. **DO NOT** use ComponentModel "spring" for server-side converters - only for web converters

## Exception Handling

- Business exceptions: `BusinessException` with `LimsResponseCode`
- Audit engine exceptions: caught by `MesAuditExceptionHandler`
- Flow engine exceptions: caught by `MesExceptionHandler`
- Global exception handler provides unified `ResponseInfo` error responses
