# APP-ELN任务接口返回状态修改说明

## 修改时间
2026-02-06

## 修改内容

### 需求说明
APP-ELN完成任务和复核任务两个接口在调用后，需要将任务的状态返回给前端，用对象封装返回任务ID和状态。

### 涉及接口

1. **APP-ELN完成任务（录入完成，置为待复核）**
   - 路径：`POST /app/task/entry/complete`
   - 功能：将任务状态从"待完成/进行中/样品审核不通过"更新为"待复核"

2. **APP-ELN复核任务（仅支持单任务复核通过）**
   - 路径：`POST /app/task/review/approve`
   - 功能：将任务状态从"待复核"更新为"复核通过"

### 修改详情

#### 1. 新增响应VO类

**文件：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/entry/vo/AppTaskStatusRespVO.java`

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("APP-ELN任务状态响应")
public class AppTaskStatusRespVO {
    @ApiModelProperty(value = "任务ID", required = true)
    private Long taskId;

    @ApiModelProperty(value = "任务状态", required = true)
    private TaskStatusEnum status;
}
```

#### 2. 修改Service接口

**TaskService.java**
```java
// 修改前
void completeTaskForAppEln(Long taskId);

// 修改后
TaskStatusEnum completeTaskForAppEln(Long taskId);
```

**InspectionReviewService.java**
```java
// 修改前
void appReviewApprove(Long taskId, String reviewerId);

// 修改后
TaskStatusEnum appReviewApprove(Long taskId, String reviewerId);
```

#### 3. 修改Service实现

**TaskServiceImpl.java**
- 方法返回值改为 `TaskStatusEnum`
- 在方法末尾返回 `TaskStatusEnum.TO_REVIEW`（待复核）

**InspectionReviewServiceImpl.java**
- 方法返回值改为 `TaskStatusEnum`
- 在方法末尾返回 `TaskStatusEnum.REVIEW_PASSED`（复核通过）

#### 4. 修改Controller

**AppTaskQueryController.java**

**完成任务接口：**
```java
// 修改前
@PostMapping("/entry/complete")
public ResponseInfo<Void> completeElnTask(@RequestBody @Valid AppTaskCompleteReqVO reqVO) {
    taskService.completeTaskForAppEln(reqVO.getTaskId());
    return ResponseInfo.success();
}

// 修改后
@PostMapping("/entry/complete")
public ResponseInfo<AppTaskStatusRespVO> completeElnTask(@RequestBody @Valid AppTaskCompleteReqVO reqVO) {
    TaskStatusEnum status = taskService.completeTaskForAppEln(reqVO.getTaskId());
    AppTaskStatusRespVO respVO = new AppTaskStatusRespVO(reqVO.getTaskId(), status);
    return ResponseInfo.success(respVO);
}
```

**复核任务接口：**
```java
// 修改前
@PostMapping("/review/approve")
public ResponseInfo<Void> approveElnTask(@RequestBody @Valid AppTaskReviewReqVO reqVO) {
    inspectionReviewService.appReviewApprove(reqVO.getTaskId(), reqVO.getReviewerId());
    return ResponseInfo.success();
}

// 修改后
@PostMapping("/review/approve")
public ResponseInfo<AppTaskStatusRespVO> approveElnTask(@RequestBody @Valid AppTaskReviewReqVO reqVO) {
    TaskStatusEnum status = inspectionReviewService.appReviewApprove(reqVO.getTaskId(), reqVO.getReviewerId());
    AppTaskStatusRespVO respVO = new AppTaskStatusRespVO(reqVO.getTaskId(), status);
    return ResponseInfo.success(respVO);
}
```

### 返回数据示例

#### 完成任务接口响应
```json
{
  "code": "200",
  "message": "操作成功",
  "data": {
    "taskId": 12345,
    "status": "TO_REVIEW"
  }
}
```

#### 复核任务接口响应
```json
{
  "code": "200",
  "message": "操作成功",
  "data": {
    "taskId": 12345,
    "status": "REVIEW_PASSED"
  }
}
```

### 任务状态枚举说明

`TaskStatusEnum` 枚举值：
- `TO_REVIEW` - 待复核（完成任务后的状态）
- `REVIEW_PASSED` - 复核通过（复核任务后的状态）

其他可能的任务状态：
- `PENDING_ASSIGNMENT` - 待分配
- `RETURN_PENDING_APPROVAL` - 退回待审批
- `PENDING_COMPLETION` - 待完成
- `IN_PROGRESS` - 进行中
- `REVIEW_REJECTED` - 复核不通过
- `SAMPLE_AUDIT_PENDING` - 待样品审核
- `SAMPLE_AUDIT_REJECTED` - 样品审核不通过
- `TERMINATED` - 已终止
- `COMPLETED` - 已完成

### 修改的文件清单

1. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/entry/vo/AppTaskStatusRespVO.java` - 新增
2. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/task/service/TaskService.java` - 修改
3. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/task/service/impl/TaskServiceImpl.java` - 修改
4. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/review/service/InspectionReviewService.java` - 修改
5. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/review/service/impl/InspectionReviewServiceImpl.java` - 修改
6. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/eln/entry/controller/AppTaskQueryController.java` - 修改

### 编译验证

所有修改已通过编译验证：

```
[INFO] bmos-lims2-server .................................. SUCCESS [ 23.382 s]
[INFO] bmos-lims2-web ..................................... SUCCESS [ 11.122 s]
[INFO] BUILD SUCCESS
```

### 兼容性说明

此修改**不影响**现有功能：
- 两个接口仍然执行原有的业务逻辑
- 只是在原有返回值基础上增加了任务ID和状态信息
- 前端可以获取更多信息以便更新UI状态

### 注意事项

1. 前端调用这两个接口后，可以直接从返回数据中获取任务的最新状态，无需再次查询
2. 返回的状态与数据库中实际保存的状态完全一致
3. 状态值是枚举类型，在JSON中会序列化为字符串形式（如"TO_REVIEW"、"REVIEW_PASSED"）
