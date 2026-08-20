# 留样样品响应码重构总结

## 修改说明

按照您的要求，将留样样品管理和留样观察模块中所有硬编码的错误提示信息改为在 `LimsResponseCode` 中定义的专用响应码，而不是复用通用的 `INVALID_PARAM` 和 `DATA_NOT_EXISTS`。

## 新增响应码列表

### 留样样品管理 (83_18_06 ~ 83_18_16)

| 响应码 | 错误码值 | 错误信息 | 说明 |
|--------|---------|---------|------|
| RETENTION_SAMPLE_NOT_RETENTION | 83_18_06 | 样品不是留样样品 | 验证样品类型 |
| RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_EXTEND | 83_18_07 | 样品未接收，无法延期 | 延期操作前置校验 |
| RETENTION_SAMPLE_DESTROYED_CANNOT_EXTEND | 83_18_08 | 样品已销毁，无法延期 | 延期操作前置校验 |
| RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_COLLECT | 83_18_09 | 样品未接收，无法领用 | 领用操作前置校验 |
| RETENTION_SAMPLE_DESTROYED_CANNOT_COLLECT | 83_18_10 | 样品已销毁，无法领用 | 领用操作前置校验 |
| RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_DESTROY | 83_18_11 | 样品未接收，无法销毁 | 销毁操作前置校验 |
| RETENTION_SAMPLE_ALREADY_DESTROYED | 83_18_12 | 样品已销毁，无法重复销毁 | 销毁操作前置校验 |
| RETENTION_EXPIRY_MUST_AFTER_OLD | 83_18_13 | 新的留样期限必须大于原留样期限 | 延期业务规则校验 |
| RETENTION_COLLECT_QUANTITY_EXCEED | 83_18_14 | 领用数量不能大于当前样品数量 | 领用业务规则校验 |
| RETENTION_SAMPLE_IDS_REQUIRED | 83_18_15 | 样品ID列表不能为空 | 批量操作参数校验 |
| RETENTION_SAMPLE_ID_REQUIRED | 83_18_16 | 样品ID不能为空 | 参数校验 |

### 留样观察 (83_18_17 ~ 83_18_20)

| 响应码 | 错误码值 | 错误信息 | 说明 |
|--------|---------|---------|------|
| RETENTION_OBSERVATION_TASK_ID_REQUIRED | 83_18_17 | 任务ID不能为空 | 参数校验 |
| RETENTION_OBSERVATION_TASK_NOT_EXIST | 83_18_18 | 任务不存在 | 任务查询校验 |
| RETENTION_OBSERVATION_TASK_ALREADY_COMPLETED | 83_18_19 | 该任务已完成，不能重复提交 | 任务状态校验 |
| RETENTION_OBSERVATION_EARLIER_TASK_UNCOMPLETED | 83_18_20 | 存在更早日期的留样观察任务未完成，请按照顺序完成 | 任务顺序校验 |

## 修改文件清单

### 1. LimsResponseCode.java
**路径：** `bmos-lims2-common/src/main/java/com/bmos/lims2/common/i18n/LimsResponseCode.java`

**修改内容：**
- 新增留样样品管理相关响应码 11 个 (83_18_06 ~ 83_18_16)
- 新增留样观察相关响应码 4 个 (83_18_17 ~ 83_18_20)

### 2. RetentionSampleManageServiceImpl.java
**路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/impl/RetentionSampleManageServiceImpl.java`

**修改内容：**
- `extendRetention()` 方法：替换 5 处异常抛出
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）
  - 样品不是留样样品：改用 `RETENTION_SAMPLE_NOT_RETENTION`
  - 样品未接收：改用 `RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_EXTEND`
  - 样品已销毁：改用 `RETENTION_SAMPLE_DESTROYED_CANNOT_EXTEND`
  - 新期限必须大于原期限：改用 `RETENTION_EXPIRY_MUST_AFTER_OLD`

- `collectSample()` 方法：替换 5 处异常抛出
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）
  - 样品不是留样样品：改用 `RETENTION_SAMPLE_NOT_RETENTION`
  - 样品未接收：改用 `RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_COLLECT`
  - 样品已销毁：改用 `RETENTION_SAMPLE_DESTROYED_CANNOT_COLLECT`
  - 领用数量超限：改用 `RETENTION_COLLECT_QUANTITY_EXCEED`

- `destroySample()` 方法：替换 5 处异常抛出
  - 样品ID不能为空：改用 `RETENTION_SAMPLE_ID_REQUIRED`
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）
  - 样品不是留样样品：改用 `RETENTION_SAMPLE_NOT_RETENTION`
  - 样品未接收：改用 `RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_DESTROY`
  - 样品已销毁：改用 `RETENTION_SAMPLE_ALREADY_DESTROYED`

- `batchDestroySamples()` 方法：替换 1 处异常抛出
  - 样品ID列表不能为空：改用 `RETENTION_SAMPLE_IDS_REQUIRED`

### 3. RetentionObservationServiceImpl.java
**路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/impl/RetentionObservationServiceImpl.java`

**修改内容：**
- `generateObservationTasks()` 方法：替换 2 处异常抛出
  - 样品ID不能为空：改用 `RETENTION_SAMPLE_ID_REQUIRED`
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）

- `submitObservation()` 方法：替换 4 处异常抛出
  - 任务ID不能为空：改用 `RETENTION_OBSERVATION_TASK_ID_REQUIRED`
  - 任务不存在：改用 `RETENTION_OBSERVATION_TASK_NOT_EXIST`
  - 任务已完成：改用 `RETENTION_OBSERVATION_TASK_ALREADY_COMPLETED`
  - 存在更早未完成任务：改用 `RETENTION_OBSERVATION_EARLIER_TASK_UNCOMPLETED`
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）

- `generateAdditionalTasksForExtension()` 方法：替换 2 处异常抛出
  - 样品ID不能为空：改用 `RETENTION_SAMPLE_ID_REQUIRED`
  - 样品不存在：`DATA_NOT_EXISTS`（无需改动）

## 使用示例

### 修改前
```java
throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品已销毁，无法领用");
```

### 修改后
```java
throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_DESTROYED_CANNOT_COLLECT);
```

## 优势

1. **代码规范化**：每个业务错误都有独立的响应码，便于前端统一处理
2. **国际化支持**：响应码定义在 `LimsResponseCode` 中，便于后续支持多语言
3. **错误追踪**：通过响应码可以快速定位具体的业务错误场景
4. **前端友好**：前端可以根据具体的响应码进行针对性的错误提示和处理
5. **可维护性**：集中管理错误码，避免硬编码导致的维护困难

## 注意事项

1. 所有新增的响应码都遵循现有的编码规范
2. 响应码区间 83_18_06 ~ 83_18_20 专用于留样相关功能
3. `DATA_NOT_EXISTS` 这类通用响应码保持不变，只替换了业务相关的错误码
4. 所有修改都向后兼容，不影响现有功能
