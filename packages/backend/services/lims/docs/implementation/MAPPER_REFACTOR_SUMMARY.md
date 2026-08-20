# TaskMapper重构总结 - 移除Map返回类型

## 重构概述

本次重构的目标是将TaskMapper中所有返回Map类型的方法改为返回具体的DTO对象，提高类型安全性和代码可维护性。

## 重构内容

### 1. 新增DTO类

#### 任务状态统计DTO
```java
// TaskStatusCountDTO.java
public class TaskStatusCountDTO {
    private TaskStatusEnum status;
    private Long count;
}
```

#### 检验单任务DTO
```java
// InspectionOrderTaskDTO.java  
public class InspectionOrderTaskDTO {
    private Long inspectionOrderId;
    private String orderNo;
    private Integer sampleCount;
    private Integer totalTaskCount;
    // ... 其他统计字段
    private List<TaskDTO> tasks;
}
```

#### 用户待完成任务数量DTO
```java
// UserPendingTaskCountDTO.java
public class UserPendingTaskCountDTO {
    private Long userId;
    private Long pendingTaskCount;
}
```

#### 可分配用户DTO
```java
// AssignableUserDTO.java
public class AssignableUserDTO {
    private String userId;
    private String userName;
    private Long teamId;
    private String teamName;
    private Long pendingTaskCount;
}
```

#### 检验方案分析项DTO
```java
// SchemeParameterDTO.java
public class SchemeParameterDTO {
    private Long schemeItemId;
    private Long inspectItemId;
    private String inspectItemName;
    private String inspectItemCode;
    private Long parameterId;
    private String parameterName;
    private String parameterCode;
    private Boolean isExecutable;
    private Boolean isReportable;
}
```

#### 任务DTO
```java
// TaskDTO.java
public class TaskDTO {
    private Long id;
    private String taskNo;
    private Long inspectionOrderId;
    private Long sampleId;
    // ... 完整的任务字段
}
```

### 2. 重构的Mapper方法

#### 原方法签名 → 新方法签名

```java
// 任务状态统计
List<Map<String, Object>> selectTaskStatusCount(@Param("query") TaskQueryDTO queryDTO);
↓
List<TaskStatusCountDTO> selectTaskStatusCount(@Param("query") TaskQueryDTO queryDTO);

// 检验单任务状态统计
List<Map<String, Object>> selectInspectionOrderTaskStatusCount(@Param("query") TaskQueryDTO queryDTO);
↓
List<TaskStatusCountDTO> selectInspectionOrderTaskStatusCount(@Param("query") TaskQueryDTO queryDTO);

// 用户待完成任务数量
List<Map<String, Object>> selectUserPendingTaskCount(@Param("userIds") List<Long> userIds);
↓
List<UserPendingTaskCountDTO> selectUserPendingTaskCount(@Param("userIds") List<Long> userIds);

// 班组用户列表
List<Map<String, Object>> selectUsersByTeamIds(@Param("teamIds") List<Long> teamIds);
↓
List<AssignableUserDTO> selectUsersByTeamIds(@Param("teamIds") List<Long> teamIds);

// 检验方案分析项配置
List<Map<String, Object>> selectSchemeItemsAndParameters(@Param("schemeVersionId") Long schemeVersionId);
↓
List<SchemeParameterDTO> selectSchemeItemsAndParameters(@Param("schemeVersionId") Long schemeVersionId);
```

### 3. 重构的Service方法

#### TaskService接口更新
```java
// 任务状态统计
Map<String, Long> queryTaskStatusCount(TaskQueryDTO queryDTO);
↓
List<TaskStatusCountDTO> queryTaskStatusCount(TaskQueryDTO queryDTO);

// 检验单任务状态统计
Map<String, Long> queryInspectionOrderTaskStatusCount(TaskQueryDTO queryDTO);
↓
List<TaskStatusCountDTO> queryInspectionOrderTaskStatusCount(TaskQueryDTO queryDTO);

// 可分配人员查询
List<Map<String, Object>> queryAssignableUsers(List<Long> taskIds);
↓
List<AssignableUserDTO> queryAssignableUsers(List<Long> taskIds);
```

### 4. 实现类更新

#### TaskServiceImpl中的关键更新

1. **可分配人员查询逻辑**
```java
// 原来使用Map操作
List<Map<String, Object>> users = taskMapper.selectUsersByTeamIds(intersectionTeamIds);
List<Long> userIds = users.stream()
    .map(user -> Long.valueOf((String) user.get("user_id")))
    .collect(Collectors.toList());

// 重构后使用DTO
List<AssignableUserDTO> users = taskMapper.selectUsersByTeamIds(intersectionTeamIds);
List<Long> userIds = users.stream()
    .map(user -> Long.valueOf(user.getUserId()))
    .collect(Collectors.toList());
```

2. **任务生成逻辑**
```java
// 原来使用Map
List<Map<String, Object>> schemeItemsAndParameters = taskMapper.selectSchemeItemsAndParameters(...);
for (Map<String, Object> parameterConfig : matchedParameters) {
    Long parameterId = (Long) parameterConfig.get("parameter_id");
    // ...
}

// 重构后使用DTO
List<SchemeParameterDTO> schemeItemsAndParameters = taskMapper.selectSchemeItemsAndParameters(...);
for (SchemeParameterDTO parameterConfig : matchedParameters) {
    Long parameterId = parameterConfig.getParameterId();
    // ...
}
```

3. **创建任务方法**
```java
// 原来的参数类型
private Task createTask(Sample sample, InspectionOrder inspectionOrder, Map<String, Object> parameterConfig)

// 重构后的参数类型
private Task createTask(Sample sample, InspectionOrder inspectionOrder, SchemeParameterDTO parameterConfig)
```

## 重构带来的优势

### 1. 类型安全
- **编译时检查**：使用具体DTO类型，编译器可以检查字段访问的正确性
- **IDE支持**：更好的代码补全和重构支持
- **减少运行时错误**：避免因Map中key拼写错误导致的问题

### 2. 代码可读性
- **明确的数据结构**：DTO类清晰地定义了数据结构
- **自文档化**：字段名和类型一目了然
- **更好的维护性**：修改数据结构时影响范围更明确

### 3. 性能优化
- **减少类型转换**：避免频繁的Map操作和类型转换
- **更好的内存使用**：DTO对象比Map更节省内存
- **JVM优化**：具体类型更容易被JVM优化

### 4. 开发效率
- **更少的错误**：减少因字段名错误导致的bug
- **更快的开发**：IDE的智能提示和代码生成
- **更容易测试**：具体对象更容易进行单元测试

## 兼容性考虑

### 1. 数据库映射
- MyBatis的resultType需要相应更新
- 确保DTO字段名与SQL查询结果列名匹配
- 可能需要使用@Results注解进行字段映射

### 2. 现有代码影响
- 调用这些Mapper方法的代码需要相应更新
- Web层的Controller可能需要调整返回类型
- 前端可能需要适配新的数据结构

### 3. 测试更新
- 单元测试需要更新mock对象
- 集成测试需要验证新的数据结构

## 后续优化建议

### 1. XML映射优化
```xml
<!-- 使用resultMap提高映射性能 -->
<resultMap id="TaskStatusCountMap" type="TaskStatusCountDTO">
    <result column="status" property="status" typeHandler="org.apache.ibatis.type.EnumTypeHandler"/>
    <result column="count" property="count"/>
</resultMap>

<select id="selectTaskStatusCount" resultMap="TaskStatusCountMap">
    SELECT status, COUNT(*) as count
    FROM lm_task
    WHERE ...
    GROUP BY status
</select>
```

### 2. 缓存优化
- 对于频繁查询的DTO对象，可以考虑添加缓存
- 使用Spring Cache或Redis进行结果缓存

### 3. 分页支持
- 为复杂查询的DTO提供分页支持
- 使用PageHelper或MyBatis-Plus的分页功能

### 4. 验证注解
- 在DTO中添加验证注解，确保数据完整性
- 使用@Valid注解进行自动验证

## 总结

通过将TaskMapper中的Map返回类型重构为具体的DTO对象，我们显著提高了代码的类型安全性、可读性和维护性。虽然需要创建更多的DTO类，但这些投入在长期维护中会得到回报。

这次重构遵循了以下最佳实践：
- **单一职责原则**：每个DTO只负责一种数据结构
- **类型安全**：使用强类型替代弱类型的Map
- **可维护性**：清晰的数据结构便于理解和修改
- **性能考虑**：减少不必要的类型转换和Map操作

重构后的代码更加健壮、易于维护，为后续的功能扩展提供了良好的基础。
