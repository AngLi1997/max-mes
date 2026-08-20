# 留样观察批量操作及临期任务查询功能总结

## 修改说明

本次修改新增了以下功能：
1. 留样观察结果批量提交
2. 查询临期任务数量接口

## 新增功能

### 1. 批量提交留样观察结果

#### 新增 DTO

**BatchRetentionObservationSubmitDTO.java**
- 路径：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/dto/`
- 说明：批量提交观察结果的数据传输对象

**字段说明：**
```java
private List<ObservationTaskItem> tasks;  // 观察任务列表

ObservationTaskItem:
  - Long taskId                  // 任务ID（必填）
  - String observationResult     // 观察结果（必填）
  - String observationRemark     // 观察备注（可选）
```

**BatchRetentionObservationSubmitReqVO.java**
- 路径：`bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/retention/vo/req/`
- 说明：批量提交观察结果的请求VO

#### Service 接口和实现

**RetentionObservationService.java** - 新增方法
```java
/**
 * 批量提交观察结果
 * @param batchSubmitDTO 批量提交数据
 */
void batchSubmitObservation(BatchRetentionObservationSubmitDTO batchSubmitDTO);
```

**RetentionObservationServiceImpl.java** - 实现逻辑
- 遍历任务列表，逐个调用单个提交方法
- 记录成功/失败统计
- 如果有失败任务，抛出异常并返回详细错误信息

#### Controller 接口

**接口路径：** `POST /retention-observation/task/batch-submit`

**请求示例：**
```json
{
  "tasks": [
    {
      "taskId": 1,
      "observationResult": "符合",
      "observationRemark": "样品状态良好"
    },
    {
      "taskId": 2,
      "observationResult": "符合",
      "observationRemark": ""
    }
  ]
}
```

**响应示例（全部成功）：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null
}
```

**响应示例（部分失败）：**
```json
{
  "code": 81_00_0003,
  "msg": "批量提交完成，成功1个，失败1个。失败详情：任务ID 2 提交失败: 该任务已完成，不能重复提交; ",
  "data": null
}
```

### 2. 查询临期任务数量

#### Mapper 方法

**RetentionObservationTaskMapper.java** - 新增方法
```java
/**
 * 查询临期任务数量（指定天数内到期且未完成的任务）
 * @param days 天数
 * @return 临期任务数量
 */
Long countUpcomingTasks(@Param("days") Integer days);
```

**RetentionObservationTaskMapper.xml** - SQL 实现
```xml
<select id="countUpcomingTasks" resultType="java.lang.Long">
    SELECT COUNT(*)
    FROM lm_retention_observation_task t
    INNER JOIN lm_sample s ON t.sample_id = s.id
    WHERE t.is_deleted = 0
      AND s.is_deleted = 0
      AND s.discarded = 0
      AND s.destroyed = 0
      AND t.completed = 0
      AND t.due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL #{days} DAY)
</select>
```

**查询条件：**
- 任务未删除
- 样品未删除、未作废、未销毁
- 任务未完成
- 到期日期在指定天数内

#### Service 接口和实现

**RetentionObservationService.java** - 新增方法
```java
/**
 * 查询临期任务数量（指定天数内到期的任务）
 * @param days 天数
 * @return 临期任务数量
 */
Long countUpcomingTasks(Integer days);
```

**RetentionObservationServiceImpl.java** - 实现逻辑
```java
@Override
public Long countUpcomingTasks(Integer days) {
    if (days == null || days <= 0) {
        days = 7; // 默认查询7天内
    }
    return retentionObservationTaskMapper.countUpcomingTasks(days);
}
```

#### Controller 接口

**接口路径：** `GET /retention-observation/task/upcoming/count`

**请求参数：**
- `days`：天数（可选，默认7天）

**请求示例：**
```
GET /retention-observation/task/upcoming/count?days=7
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "操作成功",
  "data": 5
}
```

## 修改文件清单

### 新增文件（2个）

1. **BatchRetentionObservationSubmitDTO.java**
   - 路径：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/dto/`
   - 说明：批量提交观察结果DTO

2. **BatchRetentionObservationSubmitReqVO.java**
   - 路径：`bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/retention/vo/req/`
   - 说明：批量提交观察结果请求VO

### 修改文件（5个）

1. **RetentionObservationService.java**
   - 新增：`batchSubmitObservation()` 方法
   - 新增：`countUpcomingTasks()` 方法

2. **RetentionObservationServiceImpl.java**
   - 实现：`batchSubmitObservation()` 方法（50行）
   - 实现：`countUpcomingTasks()` 方法（6行）

3. **RetentionObservationTaskMapper.java**
   - 新增：`countUpcomingTasks()` 方法

4. **RetentionObservationTaskMapper.xml**
   - 新增：`countUpcomingTasks` SQL 查询

5. **RetentionObservationController.java**
   - 修改：`submitObservation()` 方法（增加"单个"标注）
   - 新增：`batchSubmitObservation()` 接口
   - 新增：`countUpcomingTasks()` 接口

## API 接口总览

### 留样观察相关接口

| 接口 | 方法 | 路径 | 说明 |
|-----|------|------|------|
| 查询任务列表 | POST | /retention-observation/task/page | 分页查询留样观察任务 |
| 提交观察结果（单个） | POST | /retention-observation/task/{taskId}/submit | 提交单个任务的观察结果 |
| 批量提交观察结果 | POST | /retention-observation/task/batch-submit | 批量提交多个任务的观察结果 |
| 查询临期任务数量 | GET | /retention-observation/task/upcoming/count | 查询指定天数内到期的任务数量 |

## 业务逻辑说明

### 批量提交流程

1. 接收批量提交请求，包含多个任务的观察结果
2. 遍历每个任务，调用单个提交方法
3. 单个提交方法会进行以下校验：
   - 任务是否存在
   - 任务是否已完成（不能重复提交）
   - 是否存在更早的未完成任务（必须按顺序完成）
4. 记录成功/失败统计
5. 如果有失败任务，返回详细错误信息

### 临期任务查询

**查询条件：**
- 任务在指定天数内到期（默认7天）
- 任务未完成
- 样品未删除、未作废、未销毁

**使用场景：**
- 首页展示临期任务提醒
- 任务列表显示临期任务标记
- 定时任务提醒功能

## 优势

### 批量提交功能

1. ✅ **提高效率** - 一次性处理多个观察任务，减少操作次数
2. ✅ **事务管理** - 每个任务独立事务，失败不影响其他任务
3. ✅ **错误追踪** - 详细的成功/失败统计和错误信息
4. ✅ **复用逻辑** - 调用单个提交方法，保持业务逻辑一致

### 临期任务查询

1. ✅ **快速统计** - 直接查询数据库获取数量，性能高
2. ✅ **灵活配置** - 可以自定义天数，默认7天
3. ✅ **数据准确** - 排除已删除、已作废、已销毁的样品
4. ✅ **易于集成** - 简单的GET接口，便于前端调用

## 使用示例

### 批量提交示例

```javascript
// 前端调用示例
const tasks = [
  { taskId: 1, observationResult: '符合', observationRemark: '样品状态良好' },
  { taskId: 2, observationResult: '符合', observationRemark: '' },
  { taskId: 3, observationResult: '不符合', observationRemark: '发现异物' }
];

const response = await axios.post('/retention-observation/task/batch-submit', {
  tasks: tasks
});
```

### 查询临期任务示例

```javascript
// 查询7天内到期的任务数量
const count = await axios.get('/retention-observation/task/upcoming/count?days=7');
console.log(`临期任务数量：${count.data}`);

// 查询30天内到期的任务数量
const count30 = await axios.get('/retention-observation/task/upcoming/count?days=30');
console.log(`30天内到期任务数量：${count30.data}`);
```

## 验证

- ✅ 代码编译通过
- ✅ 批量提交接口完整实现
- ✅ 临期任务查询接口完整实现
- ✅ 保持单个提交接口向后兼容
- ✅ 遵循项目现有规范
