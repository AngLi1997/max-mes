# 检验录入功能实现文档

## 概述

本文档描述了LIMS2.0系统中检验录入功能的完整实现，包括分析项录入和检验单录入两种视图模式，支持批量录入、检验时间设置、数据历史追踪等功能。

## 功能特性

### 1. 分析项录入
- **列表查询**: 以分析项为主视图，检验单为子视图的三级数据结构
- **数据权限**: 只有当前人员在相关检验项目的班组中才能看到相关任务
- **批量操作**: 支持数据点批量录入和检验时间批量设置
- **状态管理**: 自动计算和更新录入状态（待录入/进行中/待复核）
- **统计功能**: 提供未完成数量和异常数量统计

### 2. 检验单录入
- **列表查询**: 以检验单为主视图，分析项为子视图的三级数据结构
- **批量操作**: 支持跨分析项的批量录入操作
- **统计功能**: 按检验单维度统计未完成和异常数量

### 3. 判定逻辑
- **表达式评估**: 支持配置化的判定表达式计算
- **异常检测**: 自动检测数据点值是否异常
- **状态更新**: 根据录入情况自动更新任务状态

### 4. 数据历史
- **变更追踪**: 记录所有数据点值的修改历史
- **操作记录**: 保存操作人、操作时间、变更原因等信息

## 数据库设计

### 1. 检验录入记录表 (lm_inspection_entry_record)
存储数据点的录入值，包含大量冗余字段便于查询和权限控制：

```sql
- id: 主键ID
- inspection_order_id: 检验单ID
- inspection_order_no: 检验单号(冗余)
- task_id: 分析项任务ID
- scheme_id: 方案ID(冗余)
- scheme_version_id: 方案版本ID(冗余)
- package_config_id: 方案实验包配置ID(冗余)
- item_config_id: 方案检验项目配置ID(冗余)
- parameter_config_id: 方案分析项配置ID(冗余)
- scheme_data_point_id: 方案数据点配置ID(来自lm_inspection_scheme_data_point表)
- original_data_point_id: 原始数据点ID(基础配置，可能为空)
- inspect_item_id: 检验项目ID(冗余)
- inspect_item_code: 检验项目编码(冗余)
- parameter_id: 分析项ID(冗余)
- parameter_code: 分析项编码(冗余)
- data_point_id: 数据点ID(保持兼容性)
- data_point_name: 数据点名称(冗余)
- point_type: 数据点类型(NUMBER/TEXT/OPTION)
- value_text: 文本值/选项值
- value_number: 数值型结果
- test_time: 检验时间
- operator_id: 录入人ID
- operator_name: 录入人姓名
- is_abnormal: 是否判定异常
- remark: 备注
```

**重要说明**: 由于方案配置时的数据点可能是直接配置的，与基础数据点配置可能已经没有直接关系，因此增加了方案配置相关的冗余字段：
- `scheme_data_point_id`: 方案数据点配置的主键ID，这是最重要的关联字段
- `package_config_id`、`item_config_id`、`parameter_config_id`: 方案各层级配置ID
- `original_data_point_id`: 原始基础数据点ID，可能为空
- 唯一索引更新为：`uk_task_scheme_data_point` (`task_id`, `scheme_data_point_id`)

### 2. 检验录入历史表 (lm_inspection_entry_history)
追踪数据点值的所有变更：

```sql
- id: 主键ID
- entry_record_id: 关联的录入记录ID
- task_id: 任务ID(冗余)
- data_point_id: 数据点ID(冗余)
- scheme_data_point_id: 方案数据点配置ID(冗余)
- old_value_text: 旧文本/选项值
- old_value_number: 旧数值
- new_value_text: 新文本/选项值
- new_value_number: 新数值
- change_reason: 变更原因
- operator_id: 操作人ID
- operator_name: 操作人姓名
- operate_time: 操作时间
```

### 3. 任务表扩展字段
在现有任务表中增加录入相关字段：

```sql
- entry_status: 录入状态(WAITING/IN_PROGRESS/TO_REVIEW)
- judged_result: 判定结果(1-通过，0-不通过)
- judged_abnormal: 是否异常
- judged_time: 判定时间
```

## 代码架构

### 1. 实体类层 (Entity)
- `InspectionEntryRecord`: 检验录入记录实体
- `InspectionEntryHistory`: 检验录入历史实体
- `EntryStatusEnum`: 录入状态枚举

### 2. 数据传输对象层 (DTO)
- `InspectionEntryRecordDTO`: 录入记录传输对象
- `AnalysisItemEntryDTO`: 分析项录入列表对象
- `InspectionOrderEntryDTO`: 检验单录入列表对象
- `AnalysisItemEntryQueryDTO`: 分析项录入查询条件
- `InspectionOrderEntryQueryDTO`: 检验单录入查询条件
- `BatchEntryDTO`: 批量录入数据对象
- `BatchTestTimeDTO`: 批量设置检验时间对象

### 3. 视图对象层 (VO)
- `AnalysisItemEntryQueryVO`: 分析项录入查询视图对象
- `InspectionOrderEntryQueryVO`: 检验单录入查询视图对象
- `BatchEntryVO`: 批量录入视图对象
- `BatchTestTimeVO`: 批量设置检验时间视图对象

### 4. 数据访问层 (Mapper)
- `InspectionEntryRecordMapper`: 录入记录数据访问接口
- `InspectionEntryHistoryMapper`: 录入历史数据访问接口

### 5. 服务层 (Service)
- `InspectionEntryService`: 检验录入核心服务接口
- `InspectionEntryServiceImpl`: 检验录入服务实现
- `JudgmentExpressionService`: 判定表达式评估服务接口
- `JudgmentExpressionServiceImpl`: 判定表达式评估服务实现

### 6. 控制器层 (Controller)
- `AnalysisItemEntryController`: 分析项录入控制器
- `InspectionOrderEntryController`: 检验单录入控制器

### 7. 转换器 (Convert)
- `InspectionEntryConvert`: VO与DTO之间的转换器

## API接口设计

### 分析项录入接口
```
POST /api/inspection/entry/analysis-item/page - 分析项录入列表查询
GET /api/inspection/entry/analysis-item/records/task/{taskId} - 根据任务ID查询数据点
GET /api/inspection/entry/analysis-item/records/scheme-data-point/{schemeDataPointId} - 根据方案数据点配置ID查询录入记录
POST /api/inspection/entry/analysis-item/batch-save - 批量录入数据点
POST /api/inspection/entry/analysis-item/batch-test-time - 批量设置检验时间
POST /api/inspection/entry/analysis-item/count/incomplete - 统计未完成数量
POST /api/inspection/entry/analysis-item/count/abnormal - 统计异常数量
```

### 检验单录入接口
```
POST /api/inspection/entry/inspection-order/page - 检验单录入列表查询
GET /api/inspection/entry/inspection-order/records - 根据检验单ID和分析项ID查询数据点
GET /api/inspection/entry/inspection-order/records/scheme-data-point/{schemeDataPointId} - 根据方案数据点配置ID查询录入记录
POST /api/inspection/entry/inspection-order/batch-save - 批量录入数据点
POST /api/inspection/entry/inspection-order/batch-test-time - 批量设置检验时间
POST /api/inspection/entry/inspection-order/count/incomplete - 统计未完成数量
POST /api/inspection/entry/inspection-order/count/abnormal - 统计异常数量
```

## 业务流程

### 1. 数据创建流程
1. 分析项任务创建时，调用 `createEntryRecordsForTask()` 方法
2. 根据方案中分析项配置的数据点生成录入记录
3. 设置任务录入状态为"待录入"

### 2. 数据录入流程
1. 用户通过前端界面录入数据点值
2. 调用批量录入接口 `batchSaveEntryRecords()`
3. 验证数据有效性
4. 保存/更新录入记录
5. 记录变更历史
6. 更新任务录入状态和判定结果

### 3. 判定计算流程
1. 当分析项所有数据点都录入完成时触发判定
2. 获取方案配置的判定表达式
3. 构建数据点值映射
4. 评估判定表达式
5. 更新任务的判定结果和异常状态

### 4. 状态流转
- **待录入 (WAITING)**: 所有数据点都未录入
- **进行中 (IN_PROGRESS)**: 部分数据点已录入
- **待复核 (TO_REVIEW)**: 所有数据点都已录入完成

## 权限控制

### 数据权限
- **权限控制机制**：参照任务分配查询的实现方式
- **权限设置方法**：通过 `setAnalysisItemEntryPermission()` 和 `setInspectionOrderEntryPermission()` 方法设置用户权限
- **班组权限查询**：通过 `queryUserInspectionTeamIds()` 方法获取用户所在的检验班组ID列表
- **SQL权限过滤**：使用用户班组ID列表在SQL中进行权限过滤，只显示用户有权限的检验项目相关数据
- **权限范围**：只有当前用户在相关检验项目的班组中才能看到对应的任务和录入记录

### 操作权限
- 录入操作记录操作人信息
- 支持操作审计和追踪

## 扩展功能

### 1. 判定表达式引擎
- 支持复杂的数学运算和逻辑判断
- 变量替换机制：`${dataPoint_ID}` 格式
- 表达式安全评估（建议集成SpEL或MVEL）

### 2. 数据验证
- 数值范围验证
- 选项有效性验证
- 必填项检查
- 数据格式验证

### 3. 异常检测
- 基于配置的异常规则
- 趋势分析
- 统计控制

## 注意事项

1. **性能优化**: 大量数据录入时考虑批量操作优化
2. **并发控制**: 多用户同时录入时的数据一致性
3. **数据完整性**: 确保录入记录与任务状态的一致性
4. **表达式安全**: 判定表达式的安全评估，防止代码注入
5. **审计日志**: 完整的操作历史记录

## 待完善功能

1. 判定表达式的完整实现和配置界面
2. 更详细的异常检测规则
3. 数据点值的格式验证
4. 更完善的权限控制
5. 性能监控和优化

## 重要更新记录

### V1.0.7 - 方案配置字段冗余优化

**背景**: 由于方案配置时的数据点可能是直接配置的，与基础数据点配置可能已经没有直接关系，需要在数据记录表中冗余更多的方案配置ID字段。

**主要变更**:

1. **数据库表结构更新** (V1.0.7__update_inspection_entry_config_fields.sql):
   - 增加方案配置相关字段：`package_config_id`、`item_config_id`、`parameter_config_id`
   - 增加方案数据点配置ID：`scheme_data_point_id` (最重要的关联字段)
   - 增加原始数据点ID：`original_data_point_id` (基础配置，可能为空)
   - 更新唯一索引：`uk_task_scheme_data_point` (task_id, scheme_data_point_id)
   - 历史表同步增加 `scheme_data_point_id` 字段

2. **实体类更新**:
   - `InspectionEntryRecord`: 增加方案配置相关字段
   - `InspectionEntryHistory`: 增加方案数据点配置ID字段

3. **DTO/VO更新**:
   - `InspectionEntryRecordDTO`: 增加方案配置字段的API文档
   - `BatchEntryDTO`/`BatchEntryVO`: 使用 `schemeDataPointId` 替代 `dataPointId`

4. **服务层更新**:
   - 创建任务数据点记录时，使用方案数据点配置ID作为主要关联
   - 批量录入验证使用方案数据点配置ID
   - 历史记录保存增加方案数据点配置ID

5. **数据访问层更新**:
   - MyBatis XML映射文件增加新字段的查询和映射
   - 增加根据方案数据点配置ID查询的方法

**影响范围**:
- 提高了数据追溯的准确性
- 支持方案配置与基础配置的解耦
- 保持了向后兼容性（保留原有data_point_id字段）
- 优化了查询性能（通过方案配置ID直接关联）

### V1.0.8 - 接口返回和接收方案配置ID

**背景**: 查询和录入时需要返回和接收方案配置的关联ID，确保前后端数据一致性。

**主要变更**:

1. **DTO/VO接口扩展**:
   - `BatchEntryDTO.EntryItemDTO`: 增加方案配置ID字段（packageConfigId、itemConfigId、parameterConfigId、originalDataPointId）
   - `BatchEntryVO.EntryItemVO`: 同步增加相应的前端接口字段
   - 所有查询接口返回完整的方案配置信息

2. **服务层增强**:
   - 新增 `getEntryRecordsBySchemeDataPointId()` 方法，支持通过方案数据点配置ID查询
   - 批量录入时接收和保存所有方案配置ID
   - 确保录入记录包含完整的配置追溯信息

3. **控制器接口扩展**:
   - 增加 `GET /records/scheme-data-point/{schemeDataPointId}` 接口
   - 支持通过方案数据点配置ID进行精确查询
   - 批量录入接口接收完整的配置信息

**影响范围**:
- 前端可以获取完整的方案配置信息，用于数据回显和验证
- 录入时携带完整配置ID，确保数据完整性
- 支持更精确的数据查询和追溯
- 为后续的配置变更管理提供基础

### V1.0.9 - 数据权限实现修正

**背景**: 参照任务分配查询的数据权限实现方式，修正检验录入的权限控制逻辑。

**主要变更**:

1. **权限DTO字段修正**:
   - `AnalysisItemEntryQueryDTO`: 修正 `currentUserId` 类型为 `Long`，增加 `currentUserTeamIds` 字段
   - `InspectionOrderEntryQueryDTO`: 同步修正权限相关字段

2. **权限设置方法**:
   - 新增 `setAnalysisItemEntryPermission()` 和 `setInspectionOrderEntryPermission()` 方法
   - 参照任务分配的权限设置模式，通过用户班组ID列表控制权限
   - 新增 `queryUserInspectionTeamIds()` 方法查询用户所在班组

3. **SQL权限过滤优化**:
   - 修正SQL中的权限过滤逻辑，使用班组ID列表而不是单个用户ID
   - 通过 `lm_inspection_team` 表直接关联，提升查询效率
   - 移除冗余的 `lm_inspection_team_member` 表查询

4. **控制器层权限应用**:
   - 所有查询和统计接口都使用权限设置方法
   - 确保数据权限在控制器层统一应用
   - 移除服务层中的权限设置代码，避免重复设置

**影响范围**:
- 权限控制更加准确和高效
- 与任务分配模块保持一致的权限实现方式
- 提升了权限查询的性能
- 确保了数据安全性和权限边界的清晰

### V1.0.10 - 权限查询SQL完全修正

**背景**: 基于用户反馈，发现MyBatis XML中的数据权限查询语句存在错误，需要完全对齐任务分配模块的SQL实现。

**主要变更**:

1. **权限过滤SQL修正**:
   - 将权限查询从 `lm_inspection_team` 表改为 `lm_inspection_scheme_item_teams` 表
   - 添加方案版本ID关联：`sit.version_id = io.scheme_version_id`
   - 通过检验单关联获取正确的方案版本ID进行权限控制

2. **用户班组查询修正**:
   - `selectUserInspectionTeamIds` 方法改为基于 `lm_inspection_scheme_item_teams` 表查询
   - 确保获取的是方案配置中实际生效的班组ID列表
   - 通过班组成员关系获取用户有权限的班组

3. **权限控制逻辑对齐**:
   - 完全参照任务分配模块的 `TaskMapper.xml` 实现
   - 使用相同的表关联逻辑和权限过滤条件
   - 确保权限控制的一致性和准确性

**修正后的SQL特点**:
- 基于方案配置的班组关系进行权限控制
- 通过方案版本ID确保权限的时效性
- 与任务分配模块完全一致的权限实现

**影响范围**:
- 权限查询完全准确，避免权限漏洞
- 与现有任务分配模块保持完全一致
- 确保只有方案配置中指定班组的用户才能看到相关数据
- 提升系统整体的数据安全性

### V1.0.11 - 分析项录入列表数据结构修正

**背景**: 基于用户反馈，分析项录入列表的数据结构存在概念错误。录入状态和判定结果应该是每个检验单的状态，而不是分析项级别的状态，因为不同检验单可能使用不同的检验方案，相同分析项在不同方案中可能有不同的数据点和判定条件。

**核心问题**:
- 分析项录入列表用于前端方便对不同检验单中的相同分析项进行快捷录入
- 每个检验单的录入状态和判定结果都是独立的
- 不同检验单可能使用不同的检验方案，配置不同的数据点和判定条件

**主要变更**:

1. **DTO结构调整**:
   - `AnalysisItemEntryDTO`: 移除分析项级别的状态字段（entryStatus、abnormal、judgedResult、judgedTime）
   - `InspectionOrderEntryItemDTO`: 添加检验单级别的状态字段（taskId、entryStatus、abnormal、judgedResult、judgedTime）

2. **SQL查询重构**:
   - `selectAnalysisItemEntryList`: 改为按分析项分组查询，只返回分析项基本信息
   - 新增 `selectInspectionOrdersByAnalysisItem`: 查询特定分析项在各个检验单中的详细信息
   - 每个检验单包含独立的录入状态和判定结果

3. **Service层逻辑优化**:
   - `getAnalysisItemEntryPage`: 重构查询逻辑，先获取分析项列表，再为每个分析项查询其在各检验单中的状态
   - 删除不再需要的 `getInspectionOrdersForAnalysisItem` 方法
   - 确保每个检验单的数据点都正确关联

4. **Mapper接口扩展**:
   - 新增 `selectInspectionOrdersByAnalysisItem` 方法
   - 支持按分析项查询其在不同检验单中的状态信息

**修正后的数据结构特点**:
- 分析项作为分组维度，包含检验项目和分析项基本信息
- 每个检验单作为独立的录入单元，包含完整的状态信息
- 支持相同分析项在不同检验方案下的差异化处理
- 为前端提供清晰的层次结构用于快捷录入

**影响范围**:
- 前端展示逻辑需要相应调整，按新的层次结构展示数据
- 录入操作基于检验单维度，每个检验单独立处理
- 统计功能继续基于任务维度，保持准确性
- 为不同检验方案的差异化处理提供了正确的数据基础

### V1.0.12 - 批量设置检验时间接口修正

**背景**: 基于用户反馈，检验时间应该是整个分析项（任务）的检验时间，而不是单个数据点的检验时间。批量设置检验时间应该操作任务级别，而不是录入记录级别。

**核心问题**:
- 检验时间是分析项级别的概念，对应后台的任务(Task)
- 原实现错误地将检验时间设置在数据点级别
- 批量设置检验时间应该更新任务表而不是录入记录表

**主要变更**:

1. **Task实体扩展**:
   - 添加 `testTime` 字段，用于存储分析项的检验时间
   - 使用 `@TableField(updateStrategy = FieldStrategy.IGNORED)` 注解

2. **数据库结构调整**:
   - 新增Flyway脚本 `V1.0.7__add_test_time_to_task.sql`
   - 为 `lm_task` 表添加 `test_time` 字段和相应索引

3. **DTO/VO结构修正**:
   - `BatchTestTimeDTO`: 将 `entryRecordIds` 改为 `taskIds`
   - `BatchTestTimeVO`: 同步修改字段名和注释
   - 接口参数含义更加明确

4. **业务逻辑重构**:
   - `batchUpdateTestTime` 方法改为更新任务表的检验时间
   - 移除Mapper中不再需要的 `batchUpdateTestTime` 方法和对应SQL
   - 直接使用TaskMapper更新任务信息

5. **接口语义优化**:
   - 批量设置检验时间现在正确地操作任务级别
   - 一个任务对应一个分析项的检验时间
   - 前端传递任务ID列表而不是录入记录ID列表

**修正后的特点**:
- 检验时间在正确的业务层级（任务/分析项级别）
- 数据模型与业务概念完全对应
- 批量操作更加高效和语义明确
- 为后续的检验时间相关功能提供正确的数据基础

**影响范围**:
- 前端需要调整批量设置检验时间的参数，传递任务ID而不是录入记录ID
- 检验时间的展示和查询基于任务维度
- 提升了数据一致性和业务逻辑的准确性
- 为检验报告等功能提供了正确的检验时间数据源

### V1.0.13 - 接口整合优化

**背景**: 分析项录入和检验单录入控制器中存在重复的接口，包括批量录入数据点、批量设置检验时间等操作。由于这些操作的业务逻辑完全一致，应该整合为统一的接口，避免代码重复。

**重复接口识别**:
- 批量录入数据点 (`/batch-save`) - 完全相同的业务逻辑
- 批量设置检验时间 (`/batch-test-time`) - 完全相同的业务逻辑
- 根据方案数据点配置ID查询录入记录 - 完全相同的查询逻辑
- 根据任务ID查询录入记录 - 完全相同的查询逻辑

**主要变更**:

1. **新增统一操作控制器**:
   - 创建 `InspectionEntryOperationController` 统一处理录入操作
   - 路径前缀：`/api/inspection-entry/operations`
   - 包含所有通用的录入操作接口

2. **接口迁移**:
   - `POST /operations/batch-save` - 批量录入数据点
   - `POST /operations/batch-test-time` - 批量设置检验时间
   - `GET /operations/records/scheme-data-point/{id}` - 根据方案数据点配置ID查询
   - `GET /operations/records/task/{id}` - 根据任务ID查询
   - `GET /operations/records` - 根据检验单ID和分析项ID查询

3. **原控制器简化**:
   - `AnalysisItemEntryController` 保留分析项录入列表查询和统计功能
   - `InspectionOrderEntryController` 保留检验单录入列表查询和统计功能
   - 移除重复的操作接口和相关import

4. **代码优化**:
   - 减少了代码重复，提高了可维护性
   - 统一的接口路径，更加清晰的API结构
   - 简化了各个控制器的职责分工

**整合后的API结构**:
```
/api/inspection-entry/
├── analysis-items/          # 分析项录入列表查询
│   ├── page                 # 分页查询
│   ├── records/task/{id}    # 按任务查询记录
│   └── count/               # 统计接口
├── inspection-orders/       # 检验单录入列表查询  
│   ├── page                 # 分页查询
│   ├── records              # 按条件查询记录
│   └── count/               # 统计接口
└── operations/              # 统一录入操作
    ├── batch-save           # 批量录入
    ├── batch-test-time      # 批量设置检验时间
    └── records/             # 各种查询接口
```

**优化效果**:
- 消除了代码重复，减少了维护成本
- API结构更加清晰，职责分工明确
- 前端调用更加统一，减少了接口数量
- 提高了代码的可读性和可维护性

**影响范围**:
- 前端需要调整API调用路径，使用统一的操作接口
- 减少了接口维护的复杂性
- 提升了系统整体的代码质量
- 为后续功能扩展提供了更好的基础结构

### V1.0.14 - 方案数据点查询接口优化

**背景**: 根据方案数据点配置ID查询录入记录的接口需要增加任务ID参数，因为前端查看录入记录是按任务维度来查看的，需要精确定位到特定任务的数据点记录。

**问题分析**:
- 原接口只根据方案数据点配置ID查询，可能返回多个任务的数据
- 前端需要按任务维度查看录入记录，需要更精确的查询
- 业务场景要求按任务分开查看数据点的录入情况

**主要变更**:

1. **接口参数优化**:
   - 接口路径保持不变：`GET /records/scheme-data-point/{schemeDataPointId}`
   - 增加必需的请求参数：`taskId`
   - 接口描述更新为"根据方案数据点配置ID和任务ID查询录入记录"

2. **Service层扩展**:
   - 新增 `getEntryRecordsBySchemeDataPointIdAndTaskId` 方法
   - 保留原有的 `getEntryRecordsBySchemeDataPointId` 方法以兼容其他场景
   - 添加参数校验确保两个参数都不为空

3. **Mapper层增强**:
   - 新增 `selectBySchemeDataPointIdAndTaskId` 方法
   - 添加对应的SQL查询，同时过滤方案数据点配置ID和任务ID
   - 按创建时间排序，确保数据有序展示

4. **SQL查询优化**:
   - 增加 `task_id` 过滤条件：`AND ier.task_id = #{taskId}`
   - 简化排序逻辑，只按 `create_time` 排序
   - 保持完整的字段映射和表关联

**优化后的查询特点**:
- 精确定位到特定任务的特定方案数据点记录
- 避免返回不相关任务的数据，提高查询精度
- 支持前端按任务维度展示和操作数据点
- 保持良好的查询性能和数据一致性

**影响范围**:
- 前端调用此接口时需要传递 `taskId` 参数
- 查询结果更加精确，只返回指定任务的数据点记录
- 提升了接口的业务语义准确性
- 为按任务维度的数据点操作提供了更好的支持

### V1.0.15 - 列表查询结构重构

**背景**: 基于用户反馈，分析项录入和检验单录入列表查询功能应当参照任务分配的实现，因为它们本质上都是基于任务的查询，只是检验录入需要额外查询每个任务关联的数据点信息。原有实现存在结构性错误。

**核心问题**:
- 分析项录入和检验单录入本质上都是任务查询
- 应该参照任务分配的"母列表+子列表"模式
- 需要为每个任务额外查询关联的数据点信息
- 原有实现的数据结构和查询逻辑不符合业务模式

**主要变更**:

1. **分析项录入列表重构**:
   - `AnalysisItemEntryDTO`: 参照 `AnalysisItemAssignmentDTO` 结构，作为母列表
   - `InspectionOrderEntryItemDTO`: 参照 `InspectionOrderTaskGroupDTO` 结构，作为子列表
   - 查询逻辑：先查分析项列表，再为每个分析项查询检验单任务分组，最后为每个任务查询数据点

2. **检验单录入列表重构**:
   - `InspectionOrderEntryDTO`: 参照 `InspectionOrderAssignmentDTO` 结构，作为母列表
   - `AnalysisItemEntryItemDTO`: 参照 `TaskDTO` 结构，作为子列表
   - 查询逻辑：先查检验单列表，再为每个检验单查询任务列表，最后为每个任务查询数据点

3. **SQL查询重构**:
   - `selectAnalysisItemEntryList`: 改为查询分析项分组，参照任务分配的分析项查询
   - `selectInspectionOrdersByAnalysisItem`: 查询特定分析项的检验单任务分组
   - `selectInspectionOrderEntryList`: 改为查询检验单分组，参照任务分配的检验单查询
   - `selectTasksByInspectionOrder`: 查询特定检验单下的任务列表

4. **Service层逻辑优化**:
   - 完全参照任务分配的查询模式：母列表 → 子列表 → 数据点
   - 删除不再需要的辅助方法
   - 确保每个任务都正确关联其数据点信息

**重构后的查询特点**:
- 与任务分配模块保持完全一致的查询模式
- 正确的"母列表+子列表"层次结构
- 每个任务包含完整的状态信息和数据点信息
- 支持按任务维度的录入操作

**数据结构对应关系**:
```
任务分配 → 检验录入
AnalysisItemAssignmentDTO → AnalysisItemEntryDTO
InspectionOrderTaskGroupDTO → InspectionOrderEntryItemDTO
InspectionOrderAssignmentDTO → InspectionOrderEntryDTO
TaskDTO → AnalysisItemEntryItemDTO
```

**影响范围**:
- 前端需要适配新的数据结构，按正确的层次关系展示数据
- 查询结果更加准确，符合业务逻辑
- 为录入操作提供了正确的任务和数据点关联
- 与任务分配模块保持了架构一致性

---

本实现遵循了项目的代码规范，使用了统一的架构模式，确保了代码的可维护性和扩展性。[[memory:4321603]] [[memory:4321591]]