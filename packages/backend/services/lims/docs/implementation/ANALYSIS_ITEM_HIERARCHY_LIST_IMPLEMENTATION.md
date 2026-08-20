# 分析项分配层级列表功能实现

## 功能概述

重新设计了分析项分配页签的列表查询功能，从原来的平铺任务列表改为层级结构：
- **母列表**：分析项（按分析项分组）
- **子列表**：该分析项下对应的检验单集合
- **任务详情**：每个检验单下的具体任务列表
- **前端展示**：默认展开子列表

## 数据结构设计

### 1. 分析项分配DTO（母列表）
```java
// AnalysisItemAssignmentDTO.java
public class AnalysisItemAssignmentDTO {
    private Long parameterId;           // 分析项ID
    private String parameterName;       // 分析项名称
    private String parameterCode;       // 分析项编码
    
    // 统计信息
    private Integer totalTaskCount;                    // 总任务数
    private Integer pendingAssignmentCount;            // 待分配任务数
    private Integer pendingCompletionCount;            // 待完成任务数
    private Integer returnPendingApprovalCount;        // 退回待审批任务数
    private Integer terminatedCount;                   // 已终止任务数
    private Integer completedCount;                    // 已完成任务数
    
    // 子列表
    private List<InspectionOrderTaskGroupDTO> inspectionOrders;  // 检验单列表
    private Boolean defaultExpanded = true;                      // 默认展开
}
```

### 2. 检验单任务分组DTO（子列表）
```java
// InspectionOrderTaskGroupDTO.java
public class InspectionOrderTaskGroupDTO {
    private Long inspectionOrderId;     // 检验单ID
    private String orderNo;             // 检验单编号
    private String clientName;          // 委托单位
    private Integer sampleCount;        // 样品数量
    
    // 该检验单在当前分析项下的统计信息
    private Integer taskCount;                         // 任务数量
    private Integer pendingAssignmentCount;            // 待分配任务数
    private Integer pendingCompletionCount;            // 待完成任务数
    private Integer returnPendingApprovalCount;        // 退回待审批任务数
    private Integer terminatedCount;                   // 已终止任务数
    private Integer completedCount;                    // 已完成任务数
    
    private LocalDateTime createTime;                  // 创建时间
    private List<TaskDTO> tasks;                       // 具体任务列表
}
```

## API接口设计

### 新增接口
```java
@PostMapping("/analysis-assignment/hierarchy-page")
public ResponseInfo<CommonPage<AnalysisItemAssignmentDTO>> queryAnalysisItemAssignmentPage(
        @RequestBody @Valid TaskQueryReqVO reqVO)
```

### 保留原接口（兼容性）
```java
@PostMapping("/analysis-assignment/page")  // 标记为旧版本
public ResponseInfo<CommonPage<TaskDTO>> queryTaskPageForAnalysisAssignment(
        @RequestBody @Valid TaskQueryReqVO reqVO)
```

## 数据库查询实现

### 1. 查询分析项列表（母列表）
```sql
SELECT 
    ip.id as parameter_id,
    ip.name as parameter_name,
    ip.code as parameter_code,
    COUNT(t.id) as total_task_count,
    COUNT(CASE WHEN t.status = 'PENDING_ASSIGNMENT' THEN 1 END) as pending_assignment_count,
    COUNT(CASE WHEN t.status = 'PENDING_COMPLETION' THEN 1 END) as pending_completion_count,
    COUNT(CASE WHEN t.status = 'RETURN_PENDING_APPROVAL' THEN 1 END) as return_pending_approval_count,
    COUNT(CASE WHEN t.status = 'TERMINATED' THEN 1 END) as terminated_count,
    COUNT(CASE WHEN t.status = 'COMPLETED' THEN 1 END) as completed_count
FROM lm_inspect_parameter ip
INNER JOIN lm_task t ON ip.id = t.parameter_id
WHERE t.is_deleted = 0 AND ip.is_deleted = 0
-- 数据权限控制
GROUP BY ip.id, ip.name, ip.code
ORDER BY ip.code ASC
```

### 2. 查询分析项下的检验单分组（子列表）
```sql
SELECT 
    io.id as inspection_order_id,
    io.order_no,
    io.client_name,
    COUNT(DISTINCT s.id) as sample_count,
    COUNT(t.id) as task_count,
    COUNT(CASE WHEN t.status = 'PENDING_ASSIGNMENT' THEN 1 END) as pending_assignment_count,
    -- 其他状态统计...
    io.create_time
FROM lm_inspection_order io
INNER JOIN lm_task t ON io.id = t.inspection_order_id
LEFT JOIN lm_sample s ON t.sample_id = s.id
WHERE t.parameter_id = #{parameterId}
-- 数据权限控制和其他查询条件
GROUP BY io.id, io.order_no, io.client_name, io.create_time
ORDER BY io.create_time DESC
```

### 3. 查询检验单下的具体任务
```sql
SELECT 
    t.id, t.task_no, t.inspection_order_id, t.sample_id,
    t.sample_no, t.inspect_item_id, t.inspect_item_name,
    t.parameter_id, t.parameter_name, t.status,
    t.owner_id, t.owner_name, t.priority,
    t.expected_completion_time, t.actual_completion_time,
    t.remark, t.terminate_reason, t.terminate_time, t.create_time
FROM lm_task t
WHERE t.inspection_order_id = #{inspectionOrderId}
AND t.parameter_id = #{parameterId}
-- 数据权限控制和其他查询条件
ORDER BY t.create_time DESC
```

## 服务层实现

### 核心逻辑
```java
@Override
public CommonPage<AnalysisItemAssignmentDTO> queryAnalysisItemAssignmentPage(TaskQueryDTO queryDTO) {
    PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
    
    // 1. 查询分析项分配列表（母列表）
    List<AnalysisItemAssignmentDTO> analysisItems = taskMapper.selectAnalysisItemAssignmentPage(queryDTO);
    
    // 2. 为每个分析项查询对应的检验单任务分组（子列表）
    for (AnalysisItemAssignmentDTO analysisItem : analysisItems) {
        List<InspectionOrderTaskGroupDTO> inspectionOrderGroups = 
            taskMapper.selectInspectionOrderTaskGroups(analysisItem.getParameterId(), queryDTO);
        
        // 3. 为每个检验单分组查询具体的任务列表
        for (InspectionOrderTaskGroupDTO orderGroup : inspectionOrderGroups) {
            List<TaskDTO> tasks = taskMapper.selectTasksByInspectionOrderAndParameter(
                orderGroup.getInspectionOrderId(), 
                analysisItem.getParameterId(), 
                queryDTO
            );
            orderGroup.setTasks(tasks);
        }
        
        analysisItem.setInspectionOrders(inspectionOrderGroups);
    }
    
    return CommonPage.convertPage(analysisItems);
}
```

## 数据权限控制

### 一致的权限策略
所有三个查询都应用相同的数据权限控制：
```sql
-- 数据权限控制
<if test="query.currentUserTeamIds != null and query.currentUserTeamIds.size() > 0">
    AND EXISTS (
        SELECT 1 FROM lm_inspection_team_user itu
        INNER JOIN lm_inspect_item_team iit ON itu.team_id = iit.team_id
        WHERE itu.user_id = #{query.currentUserId}
        AND iit.inspect_item_id = t.inspect_item_id
        AND itu.is_deleted = 0
        AND iit.is_deleted = 0
    )
</if>
```

## 前端展示特性

### 1. 层级结构
- **第一层**：分析项列表，显示分析项名称、编码和统计信息
- **第二层**：检验单列表，显示检验单编号、委托单位、样品数量和任务统计
- **第三层**：具体任务列表，显示任务详细信息

### 2. 默认展开
- 所有分析项下的检验单列表默认展开（`defaultExpanded = true`）
- 用户可以手动折叠/展开各级列表

### 3. 统计信息
- **分析项级别**：显示该分析项下所有任务的状态统计
- **检验单级别**：显示该检验单在当前分析项下的任务状态统计

## 性能优化考虑

### 1. 分页处理
- 只对分析项（母列表）进行分页
- 子列表根据母列表的结果动态加载

### 2. 查询优化
- 使用合适的索引支持多表关联查询
- 利用COUNT聚合函数在数据库层面完成统计

### 3. 数据量控制
- 可以考虑对子列表也进行适当的数量限制
- 支持按需加载任务详情

## 使用场景

### 1. 分析项管理视角
- 检验员可以按分析项查看所有相关的检验单和任务
- 便于按专业领域进行任务分配和管理

### 2. 工作负载分析
- 清晰显示每个分析项的工作量分布
- 便于合理分配检验资源

### 3. 进度跟踪
- 分层级显示任务完成进度
- 支持从宏观到微观的进度管理

## 兼容性

### 向后兼容
- 保留原有的平铺列表接口，确保现有功能不受影响
- 新接口使用不同的路径，避免冲突

### 渐进式迁移
- 前端可以逐步从旧接口迁移到新接口
- 支持A/B测试和灰度发布

## 总结

新的分析项分配层级列表功能提供了：

1. **更清晰的数据组织**：按分析项分组，便于专业化管理
2. **丰富的统计信息**：多层级的任务状态统计
3. **良好的用户体验**：默认展开，层级清晰
4. **完整的权限控制**：继承原有的数据权限机制
5. **高性能查询**：优化的SQL查询和合理的数据结构

这个实现满足了用户对于"母子列表"结构的需求，同时保持了系统的性能和扩展性。

