# 可分配人员查询功能实现说明

## 概述

本文档详细说明了任务管理系统中"查询可分配人员"功能的实现。该功能用于在分配任务时，根据任务对应的检验项目，查询出有权限接收这些任务的人员列表，并显示每个人员当前的待完成任务数量。

## 业务需求

在任务分配时，需要满足以下条件：
1. 只有在相关检验项目的班组中的人员才能被分配任务
2. 如果选择了多个不同检验项目的任务，只有同时在所有相关检验项目班组中的人员才能被分配
3. 需要显示每个可分配人员当前的待完成任务数量，便于合理分配工作负载

## 实现方案

### 1. 数据库设计

#### 相关表结构
```sql
-- 任务表
lm_task (
    id, inspect_item_id, parameter_id, status, owner_id, ...
)

-- 检验方案检验项目班组关系表
lm_inspection_scheme_item_teams (
    inspect_item_id, team_id, ...
)

-- 检验班组用户关系表
lm_inspection_team_user (
    inspection_team_id, user_id
)

-- 检验班组表
lm_inspection_team (
    id, name, status, is_deleted, ...
)
```

### 2. 核心算法

#### 班组交集计算算法
```java
private List<Long> calculateTeamIntersection(List<Long> taskIds, List<Long> inspectItemIds) {
    if (inspectItemIds.size() == 1) {
        // 单个检验项目，直接返回该项目的班组
        return taskMapper.selectTeamIdsByInspectItemIds(inspectItemIds);
    }

    // 获取第一个检验项目的班组作为初始交集
    Set<Long> intersectionSet = new HashSet<>(
        taskMapper.selectTeamIdsByInspectItemIds(Collections.singletonList(inspectItemIds.get(0)))
    );

    // 依次与其他检验项目的班组求交集
    for (int i = 1; i < inspectItemIds.size(); i++) {
        List<Long> currentItemTeams = taskMapper.selectTeamIdsByInspectItemIds(
            Collections.singletonList(inspectItemIds.get(i))
        );
        
        intersectionSet.retainAll(currentItemTeams); // 求交集
        
        if (intersectionSet.isEmpty()) {
            break; // 交集为空，提前退出
        }
    }

    return new ArrayList<>(intersectionSet);
}
```

### 3. 实现步骤

#### 步骤1: 查询任务对应的检验项目
```sql
SELECT DISTINCT t.inspect_item_id
FROM lm_task t
WHERE t.id IN (任务ID列表)
AND t.is_deleted = 0
```

#### 步骤2: 查询检验项目对应的班组
```sql
SELECT DISTINCT sit.team_id
FROM lm_inspection_scheme_item_teams sit
WHERE sit.inspect_item_id IN (检验项目ID列表)
AND sit.is_deleted = 0
```

#### 步骤3: 计算班组交集
使用Java Set集合的retainAll方法计算多个检验项目班组的交集。

#### 步骤4: 查询班组中的人员
```sql
SELECT DISTINCT
    itu.user_id,
    it.id as team_id,
    it.name as team_name
FROM lm_inspection_team_user itu
INNER JOIN lm_inspection_team it ON itu.inspection_team_id = it.id
WHERE itu.inspection_team_id IN (交集班组ID列表)
AND it.is_deleted = 0
AND it.status = 1
ORDER BY itu.user_id
```

#### 步骤5: 查询人员当前待完成任务数量
```sql
SELECT 
    t.owner_id as user_id,
    COUNT(*) as pending_task_count
FROM lm_task t
WHERE t.owner_id IN (人员ID列表)
AND t.status = 'PENDING_COMPLETION'
AND t.is_deleted = 0
GROUP BY t.owner_id
```

### 4. 代码实现

#### TaskMapper接口
```java
List<Long> selectInspectItemIdsByTaskIds(@Param("taskIds") List<Long> taskIds);
List<Long> selectTeamIdsByInspectItemIds(@Param("inspectItemIds") List<Long> inspectItemIds);
List<Map<String, Object>> selectUsersByTeamIds(@Param("teamIds") List<Long> teamIds);
List<Map<String, Object>> selectUserPendingTaskCount(@Param("userIds") List<Long> userIds);
```

#### TaskService实现
```java
@Override
public List<Map<String, Object>> queryAssignableUsers(List<Long> taskIds) {
    // 1. 查询任务对应的检验项目
    List<Long> inspectItemIds = taskMapper.selectInspectItemIdsByTaskIds(taskIds);
    
    // 2. 计算班组交集
    List<Long> intersectionTeamIds = calculateTeamIntersection(taskIds, inspectItemIds);
    
    // 3. 查询班组中的人员
    List<Map<String, Object>> users = taskMapper.selectUsersByTeamIds(intersectionTeamIds);
    
    // 4. 查询人员当前待完成任务数量
    List<Long> userIds = users.stream()
        .map(user -> Long.valueOf((String) user.get("user_id")))
        .collect(Collectors.toList());
    
    List<Map<String, Object>> userTaskCounts = taskMapper.selectUserPendingTaskCount(userIds);
    
    // 5. 组装返回结果
    return assembleResult(users, userTaskCounts);
}
```

### 5. 返回数据格式

```json
[
    {
        "user_id": "user001",
        "team_id": 100,
        "team_name": "理化检测班组",
        "pending_task_count": 5
    },
    {
        "user_id": "user002", 
        "team_id": 100,
        "team_name": "理化检测班组",
        "pending_task_count": 3
    }
]
```

### 6. 性能优化

#### 6.1 SQL优化
- 使用DISTINCT避免重复数据
- 在关键字段上建立索引
- 使用INNER JOIN优化查询性能

#### 6.2 算法优化
- 提前退出：交集为空时立即停止计算
- 单项目优化：只有一个检验项目时直接返回
- 内存优化：使用HashSet进行交集计算

#### 6.3 建议索引
```sql
-- 任务表索引
CREATE INDEX idx_task_inspect_item ON lm_task (inspect_item_id);
CREATE INDEX idx_task_owner_status ON lm_task (owner_id, status);

-- 班组关系表索引
CREATE INDEX idx_scheme_item_teams ON lm_inspection_scheme_item_teams (inspect_item_id, team_id);
CREATE INDEX idx_team_user ON lm_inspection_team_user (inspection_team_id, user_id);
```

### 7. 异常处理

#### 7.1 边界情况处理
- 空任务列表：抛出参数异常
- 任务不存在：返回空列表
- 无对应检验项目：返回空列表
- 班组交集为空：返回空列表
- 班组无人员：返回空列表

#### 7.2 日志记录
```java
log.info("开始查询可分配人员，任务数量：{}", taskIds.size());
log.debug("查询到检验项目ID列表：{}", inspectItemIds);
log.debug("计算班组交集结果：{}", intersectionTeamIds);
log.warn("计算班组交集为空，无法分配人员");
log.info("查询可分配人员完成，人员数量：{}", result.size());
```

### 8. 测试用例

#### 8.1 正常情况测试
- 单个任务查询可分配人员
- 多个同类任务查询可分配人员
- 多个不同类任务查询可分配人员（有交集）

#### 8.2 边界情况测试
- 空任务列表测试
- 无对应检验项目测试
- 班组无交集测试
- 班组无人员测试

#### 8.3 性能测试
- 大量任务查询性能测试
- 多个检验项目交集计算性能测试

### 9. 扩展性考虑

#### 9.1 缓存优化
- 可以缓存检验项目与班组的关系
- 可以缓存用户待完成任务数量
- 班组人员关系变更时及时清除缓存

#### 9.2 功能扩展
- 支持按工作负载排序
- 支持按专业技能筛选
- 支持按地理位置筛选
- 支持按工作时间筛选

## 总结

该功能通过多步查询和交集计算，确保只有具备相应权限的人员才能被分配任务，同时提供任务负载信息帮助合理分配工作。实现考虑了性能优化和异常处理，具备良好的扩展性。
