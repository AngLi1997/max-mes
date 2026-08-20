# 留样样品操作历史返回值规范化总结

## 修改说明

将留样样品管理的操作历史返回值改为与方案版本操作历史一致的标准格式 `List<ListLogVO>`，而不是原来的 `List<Map<String, Object>>`。

## 问题分析

### 修改前
留样样品的操作历史返回的是 `List<Map<String, Object>>`，手动构建 Map 对象：
```java
List<Map<String, Object>> result = new ArrayList<>();
for (AuditOperationLogEntity log : logs) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", log.getId());
    map.put("operationType", log.getOperationType());
    map.put("detail", log.getDetail());
    map.put("remark", log.getRemark());
    map.put("createTime", log.getCreateTime());
    map.put("createBy", log.getCreateBy());
    result.add(map);
}
```

### 修改后
使用标准的 `ListLogVO` 对象，与方案版本操作历史保持一致：
```java
return auditOperationLogMapper.listLogByBusinessIdAndModule(
    sampleId,
    AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName()
);
```

## ListLogVO 对象结构

**文件路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/audit/operationlog/vo/ListLogVO.java`

### 字段说明

| 字段 | 类型 | 说明 |
|-----|------|------|
| operationType | OperationType | 操作类型（枚举） |
| operationTypeName | String | 操作类型名称（自动根据枚举获取） |
| createTime | LocalDateTime | 操作时间 |
| createBy | String | 操作人ID |
| createByName | String | 操作人名称（自动查询用户表获取） |
| createUsername | String | 操作人用户名（自动查询用户表获取） |
| remark | String | 备注 |
| nodeName | String | 节点名称 |
| comment | String | 操作说明 |
| detail | String | 详情（JSON字符串） |

### 自动填充字段

1. **operationTypeName** - 根据 `operationType` 枚举值自动获取名称
2. **createByName** - 根据 `createBy` 查询用户表自动填充
3. **createUsername** - 根据 `createBy` 查询用户表自动填充

## 修改文件清单

### 1. AuditOperationLogMapper.java
**路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/audit/operationlog/mapper/AuditOperationLogMapper.java`

**新增方法：**
```java
/**
 * 查询指定业务ID和模块的操作日志
 * @param businessId 业务ID
 * @param module 业务模块
 * @return 操作日志列表
 */
List<ListLogVO> listLogByBusinessIdAndModule(Long businessId, String module);
```

### 2. OperationLogMapper.xml
**路径：** `bmos-lims2-server/src/main/resources/mapper/audit/OperationLogMapper.xml`

**新增查询：**
```xml
<select id="listLogByBusinessIdAndModule" resultType="com.bmos.lims2.server.audit.operationlog.vo.ListLogVO">
    select
        id,
        module,
        operation_type,
        remark,
        create_by,
        create_time,
        node_name,
        comment,
        detail
    from bm_log_operation
    where
        is_deleted = 0
        and business_id = #{businessId}
        and module = #{module}
    order by create_time desc
</select>
```

### 3. RetentionSampleManageService.java
**路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/RetentionSampleManageService.java`

**修改前：**
```java
List<Map<String, Object>> getOperationHistory(Long sampleId);
```

**修改后：**
```java
List<ListLogVO> getOperationHistory(Long sampleId);
```

### 4. RetentionSampleManageServiceImpl.java
**路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/impl/RetentionSampleManageServiceImpl.java`

**修改前：**
```java
@Override
public List<Map<String, Object>> getOperationHistory(Long sampleId) {
    // 查询该样品的所有操作历史
    List<AuditOperationLogEntity> logs = auditOperationLogMapper.selectList(
        new LambdaQueryWrapper<AuditOperationLogEntity>()
            .eq(AuditOperationLogEntity::getBusinessId, sampleId)
            .eq(AuditOperationLogEntity::getModule, AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
            .orderByDesc(AuditOperationLogEntity::getCreateTime)
    );

    List<Map<String, Object>> result = new ArrayList<>();
    for (AuditOperationLogEntity log : logs) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", log.getId());
        map.put("operationType", log.getOperationType());
        map.put("detail", log.getDetail());
        map.put("remark", log.getRemark());
        map.put("createTime", log.getCreateTime());
        map.put("createBy", log.getCreateBy());
        result.add(map);
    }

    return result;
}
```

**修改后：**
```java
@Override
public List<ListLogVO> getOperationHistory(Long sampleId) {
    // 查询该样品的所有操作历史
    return auditOperationLogMapper.listLogByBusinessIdAndModule(
        sampleId,
        AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName()
    );
}
```

### 5. RetentionSampleManageController.java
**路径：** `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/retention/RetentionSampleManageController.java`

**修改前：**
```java
@ApiOperation("查询样品操作历史")
@GetMapping("/{sampleId}/history")
public ResponseInfo<List<Map<String, Object>>> getOperationHistory(
        @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId) {

    List<Map<String, Object>> history =
        retentionSampleManageService.getOperationHistory(sampleId);
    return ResponseInfo.success(history);
}
```

**修改后：**
```java
@ApiOperation("查询样品操作历史")
@GetMapping("/{sampleId}/history")
public ResponseInfo<List<ListLogVO>> getOperationHistory(
        @PathVariable @NotNull(message = "样品ID不能为空") Long sampleId) {

    List<ListLogVO> history = retentionSampleManageService.getOperationHistory(sampleId);
    return ResponseInfo.success(history);
}
```

## API返回格式示例

### 修改前
```json
[
  {
    "id": 1,
    "operationType": "retentionExtend",
    "detail": "{\"expireDateUpdate\":\"2027-01-01\"}",
    "remark": "",
    "createTime": "2026-02-11T10:00:00",
    "createBy": "user123"
  }
]
```

### 修改后
```json
[
  {
    "operationType": "RETENTION_EXTEND",
    "operationTypeName": "延期",
    "createTime": "2026-02-11T10:00:00",
    "createBy": "user123",
    "createByName": "张三-zhangsan",
    "createUsername": "张三-zhangsan",
    "remark": "",
    "nodeName": null,
    "comment": null,
    "detail": "{\"expireDateUpdate\":\"2027-01-01\"}"
  }
]
```

## 优势

1. ✅ **统一标准** - 与项目中其他模块（如方案版本）的操作历史返回格式保持一致
2. ✅ **类型安全** - 使用强类型 VO 对象，而不是弱类型 Map
3. ✅ **自动填充** - 操作类型名称和操作人信息自动填充，无需手动处理
4. ✅ **代码简洁** - 从20多行代码简化为3行，逻辑更清晰
5. ✅ **前端友好** - 返回结构统一，前端可以复用现有的展示组件
6. ✅ **可维护性** - 集中在 Mapper 层处理查询逻辑，便于统一维护

## 验证

- ✅ 代码编译通过
- ✅ 返回格式与方案版本操作历史一致
- ✅ 自动填充字段功能正常
- ✅ 遵循项目现有规范

## 相关参考

可参考以下接口查看标准的操作历史实现：
- **方案版本操作历史：** `/audit/operation/history/list/{businessId}`
- **Controller：** `AuditOperationHistoryController.java`
- **Service：** `AuditOperationLogService.listRecordLog()`
