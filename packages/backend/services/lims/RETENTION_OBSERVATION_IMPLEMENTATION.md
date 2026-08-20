# 留样观察功能实现文档

## 功能概述

留样观察功能用于对留样样品进行定期观察和记录，确保留样样品在留样期间的质量状态符合要求。

## 实现内容

### 1. 数据库设计

#### 1.1 留样观察任务表 (lm_retention_observation_task)

存储留样样品的观察任务信息，每个留样样品在留样期限内按年度生成多个观察任务。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | BIGINT | 主键ID |
| sample_id | BIGINT | 样品ID |
| sample_no | VARCHAR(100) | 样品编号 |
| observation_year | INT | 观察年度（第几年） |
| due_date | DATE | 任务到期日期 |
| completed | TINYINT(1) | 是否已完成 |
| observation_result | VARCHAR(50) | 观察结果（符合/不符合） |
| observation_remark | TEXT | 观察备注 |
| observer_id | VARCHAR(50) | 观察人ID |
| observer_name | VARCHAR(100) | 观察人名称 |
| observation_time | DATETIME | 观察时间 |

**索引**:
- idx_sample_id: 样品ID索引
- idx_sample_no: 样品编号索引
- idx_due_date: 到期日期索引
- idx_completed: 完成状态索引

#### 1.2 留样观察台账表 (lm_retention_observation_ledger)

记录每次观察的详细信息，包括样品信息、观察结果等。

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | BIGINT | 主键ID |
| task_id | BIGINT | 任务ID |
| sample_no | VARCHAR(100) | 样品编号 |
| batch_no | VARCHAR(100) | 批号 |
| material_id | BIGINT | 物料ID |
| material_name | VARCHAR(200) | 物料名称 |
| material_code | VARCHAR(100) | 物料编码 |
| material_spec | VARCHAR(200) | 物料规格 |
| quantity | VARCHAR(50) | 样品数量 |
| unit_id | BIGINT | 单位ID |
| observation_result | VARCHAR(50) | 观察结果 |
| observation_remark | TEXT | 备注 |
| observer_id | VARCHAR(50) | 观察人ID |
| observer_name | VARCHAR(100) | 观察人名称 |
| observation_time | DATETIME | 观察时间 |

**索引**:
- idx_task_id: 任务ID索引
- idx_sample_no: 样品编号索引
- idx_batch_no: 批号索引
- idx_observation_time: 观察时间索引

### 2. 任务自动生成

#### 2.1 生成时机

留样样品接收后自动生成观察任务。在 `SampleReceiveServiceImpl.batchReceiveRetentionSamples()` 方法中调用观察任务生成服务。

#### 2.2 生成规则

1. 验证样品是否为留样样品（检验项目ID为 `InspectItemConstants.RETENTION_INSPECT_ITEM_ID`）
2. 验证样品是否已接收且有接收时间
3. 验证样品是否有留样期限
4. 计算需要生成多少年的观察任务：
   - 从样品接收日期到留样期限的年数
   - 每年生成一个观察任务
5. 设置任务到期日期为：接收日期 + N年（N为观察年度）

#### 2.3 实现位置

- Service: `RetentionObservationServiceImpl.generateObservationTasks()`
- 调用位置: `SampleReceiveServiceImpl.batchReceiveRetentionSamples()` (行256-263)

### 3. 列表查询功能

#### 3.1 查询条件

- 物料ID集合 (materialIds)
- 批号 (batchNo) - 模糊查询
- 样品编号 (sampleNo) - 模糊查询
- 查询类型 (queryType):
  - `upcoming`: 临期任务（一周内到期）
  - `all`: 全部任务

#### 3.2 列表字段

- 任务信息: 任务ID、观察年度、观察到期时间、是否已完成
- 样品信息: 样品ID、样品编号、样品数量、单位
- 物料信息: 批号、物料名称、物料编码、规格
- 留样信息: 留样时间、留样人、留样期限、储存位置
- 观察信息: 观察结果、观察备注、观察人名称、观察时间

#### 3.3 排序规则

- **临期任务** (queryType=upcoming):
  - 按到期日期正序排序（越近的排在前面）
  - 相同日期按样品编号正序排序
- **全部任务** (queryType=all):
  - 按到期日期倒序排序
  - 相同日期按样品编号正序排序

#### 3.4 实现位置

- Mapper XML: `RetentionObservationTaskMapper.xml.selectObservationTaskPageList()`
- Service: `RetentionObservationServiceImpl.getObservationTaskPageList()`
- Controller: `RetentionObservationController.getObservationTaskPageList()`

### 4. 观察功能

#### 4.1 提交数据

- 任务ID (taskId) - 必填
- 观察结果 (observationResult) - 必填，值为"符合"或"不符合"
- 观察备注 (observationRemark) - 可选

#### 4.2 业务逻辑

1. 验证任务是否存在
2. 验证任务是否已完成（不能重复提交）
3. **顺序校验**: 检查该样品是否存在更早日期的未完成任务
   - 如果存在，返回错误："存在更早日期的留样观察任务未完成，请按照顺序完成"
4. 查询样品、检验单、物料信息
5. 更新任务状态:
   - 设置为已完成
   - 记录观察结果和备注
   - 记录观察人（当前登录用户）
   - 记录观察时间（当前时间）
6. 创建观察台账记录:
   - 记录样品编号、批号
   - 记录物料名称、编码、规格
   - 记录样品数量、单位
   - 记录观察结果、备注
   - 记录观察人、观察时间

#### 4.3 实现位置

- Service: `RetentionObservationServiceImpl.submitObservation()`
- Controller: `RetentionObservationController.submitObservation()`

### 5. API接口

#### 5.1 查询留样观察任务列表

**接口地址**: `POST /api/app/lims2/retention-observation/task/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3],
  "batchNo": "批次001",
  "sampleNo": "样品001",
  "queryType": "upcoming"  // upcoming-临期任务, all-全部任务
}
```

**响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 20,
    "list": [
      {
        "id": 1,
        "sampleId": 100,
        "sampleNo": "S20260206001",
        "batchNo": "B20260101",
        "materialName": "原料药A",
        "materialCode": "M001",
        "materialSpec": "99.9%",
        "quantity": "100",
        "unitId": 1,
        "unitName": "g",
        "retentionTime": "2026-01-01T10:00:00",
        "retentionUserId": "001",
        "retentionUserName": "张三",
        "retentionExpiryDate": "2029-01-01",
        "storageLocation": "冷藏室A-01",
        "observationDueDate": "2027-01-01",
        "observationYear": 1,
        "completed": false,
        "observationResult": null,
        "observationRemark": null,
        "observerName": null,
        "observationTime": null
      }
    ]
  }
}
```

#### 5.2 提交留样观察结果

**接口地址**: `POST /api/app/lims2/retention-observation/task/{taskId}/submit`

**路径参数**:
- taskId: 任务ID（必填）

**请求参数**:
```json
{
  "observationResult": "符合",
  "observationRemark": "样品外观正常，无异常气味"
}
```

**响应示例**:
```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误响应示例**:
```json
{
  "code": 10001,
  "message": "存在更早日期的留样观察任务未完成，请按照顺序完成",
  "data": null
}
```

## 文件清单

### 数据库文件
- `V1.1.1_0.0.41__create_retention_observation_tables.sql` - 创建留样观察任务表和台账表

### Server层

**Entity**:
- `RetentionObservationTask.java` - 留样观察任务实体
- `RetentionObservationLedger.java` - 留样观察台账实体

**Mapper**:
- `RetentionObservationTaskMapper.java` - 留样观察任务Mapper接口
- `RetentionObservationLedgerMapper.java` - 留样观察台账Mapper接口
- `RetentionObservationTaskMapper.xml` - 留样观察任务Mapper XML

**DTO**:
- `RetentionObservationTaskPageQueryDTO.java` - 分页查询DTO
- `RetentionObservationTaskListDTO.java` - 列表DTO
- `RetentionObservationSubmitDTO.java` - 提交观察结果DTO

**Service**:
- `RetentionObservationService.java` - 留样观察Service接口
- `RetentionObservationServiceImpl.java` - 留样观察Service实现类

### Web层

**Controller**:
- `RetentionObservationController.java` - 留样观察控制器

**Request VO**:
- `RetentionObservationTaskPageReqVO.java` - 分页查询请求VO
- `RetentionObservationSubmitReqVO.java` - 提交观察请求VO

**Response VO**:
- `RetentionObservationTaskListRespVO.java` - 列表响应VO

### 修改的文件
- `SampleReceiveServiceImpl.java` - 在留样样品接收后调用生成观察任务

## 关键技术点

### 1. 单位名称处理

使用全局缓存 `UnitCache` 填充单位名称，而不是在SQL中使用LEFT JOIN：

```java
if (!CollectionUtils.isEmpty(list)) {
    list.forEach(item -> {
        if (item.getUnitId() != null) {
            item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
        }
    });
}
```

### 2. 观察任务顺序校验

确保观察任务按时间顺序完成，查询同一样品的所有早于当前任务且未完成的任务：

```xml
<select id="selectEarlierUncompletedTasks">
    SELECT *
    FROM lm_retention_observation_task
    WHERE sample_id = #{sampleId}
      AND id != #{taskId}
      AND completed = 0
      AND is_deleted = 0
      AND due_date &lt; (
          SELECT due_date
          FROM lm_retention_observation_task
          WHERE id = #{taskId}
      )
    ORDER BY due_date ASC
</select>
```

### 3. 临期任务查询

使用SQL的CASE WHEN和日期函数计算临期任务（一周内到期）：

```xml
<when test="query.queryType == 'upcoming'">
    AND t.due_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
    ORDER BY t.due_date ASC, t.sample_no ASC
</when>
```

### 4. 观察任务自动生成

根据留样期限自动计算需要生成的观察任务年数：

```java
int years = expiryDate.getYear() - receiveDate.getYear();
if (expiryDate.isBefore(receiveDate.plusYears(years))) {
    years--;
}

for (int year = 1; year <= years; year++) {
    RetentionObservationTask task = new RetentionObservationTask();
    task.setSampleId(sampleId);
    task.setSampleNo(sample.getSampleNo());
    task.setObservationYear(year);
    task.setDueDate(receiveDate.plusYears(year));
    task.setCompleted(false);
    tasks.add(task);
}
```

## 测试说明

由于服务需要重启才能加载新的类，建议执行以下步骤：

1. 重启应用服务
2. 执行数据库迁移脚本
3. 创建留样样品并接收（会自动生成观察任务）
4. 测试查询接口
5. 测试提交观察结果接口

## 注意事项

1. 观察任务必须按时间顺序完成，不能跳过早期任务
2. 任务完成后不能重复提交
3. 只有留样样品接收后才会生成观察任务
4. 观察任务的生成数量由留样期限决定（每年一个任务）
5. 临期任务定义为一周内到期的未完成任务
