# 留样操作日志枚举规范化总结

## 修改说明

将留样样品管理中操作日志记录的硬编码字符串改为使用枚举类，遵循项目规范。

## 新增枚举值

### 1. AuditBusinessModule 枚举（业务模块）

**文件路径：** `bmos-lims2-common/src/main/java/com/bmos/lims2/common/enums/AuditBusinessModule.java`

**新增枚举：**
```java
// 留样管理
RETENTION_SAMPLE_MANAGE("RETENTION_SAMPLE_MANAGE", "留样样品管理");
```

**字段说明：**
- `code`: RETENTION_SAMPLE_MANAGE（枚举编码）
- `name`: 留样样品管理（中文名称）

### 2. OperationType 枚举（操作类型）

**文件路径：** `bmos-lims2-common/src/main/java/com/bmos/lims2/common/enums/OperationType.java`

**新增枚举：**
```java
// 留样操作类型
RETENTION_EXTEND("延期", "retentionExtend"),
RETENTION_COLLECT("领用", "retentionCollect"),
RETENTION_DESTROY("销毁", "retentionDestroy")
```

**字段说明：**
- RETENTION_EXTEND: 延期操作
  - `name`: 延期
  - `value`: retentionExtend
- RETENTION_COLLECT: 领用操作
  - `name`: 领用
  - `value`: retentionCollect
- RETENTION_DESTROY: 销毁操作
  - `name`: 销毁
  - `value`: retentionDestroy

## 修改代码位置

### RetentionSampleManageServiceImpl.java

**文件路径：** `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/impl/RetentionSampleManageServiceImpl.java`

#### 1. 导入枚举类
```java
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
```

#### 2. 延期操作日志（extendRetention 方法）

**修改前：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module("留样样品管理")
    .operationType("延期")
    .detail("{\"expireDateUpdate\":\"" + newExpiryDate + "\"}")
    .remark("")
    .build();
```

**修改后：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
    .operationType(OperationType.RETENTION_EXTEND.getValue())
    .detail("{\"expireDateUpdate\":\"" + newExpiryDate + "\"}")
    .remark("")
    .build();
```

#### 3. 领用操作日志（collectSample 方法）

**修改前：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module("留样样品管理")
    .operationType("领用")
    .detail("")
    .remark("")
    .build();
```

**修改后：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
    .operationType(OperationType.RETENTION_COLLECT.getValue())
    .detail("")
    .remark("")
    .build();
```

#### 4. 销毁操作日志（destroySample 方法）

**修改前：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module("留样样品管理")
    .operationType("销毁")
    .detail(detailBuilder.toString())
    .remark(destructionDTO.getRemark() != null ? destructionDTO.getRemark() : "")
    .build();
```

**修改后：**
```java
AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
    .businessId(sampleId)
    .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
    .operationType(OperationType.RETENTION_DESTROY.getValue())
    .detail(detailBuilder.toString())
    .remark(destructionDTO.getRemark() != null ? destructionDTO.getRemark() : "")
    .build();
```

#### 5. 查询操作历史（getOperationHistory 方法）

**修改前：**
```java
List<AuditOperationLogEntity> logs = auditOperationLogMapper.selectList(
    new LambdaQueryWrapper<AuditOperationLogEntity>()
        .eq(AuditOperationLogEntity::getBusinessId, sampleId)
        .eq(AuditOperationLogEntity::getModule, "留样样品管理")
        .orderByDesc(AuditOperationLogEntity::getCreateTime)
);
```

**修改后：**
```java
List<AuditOperationLogEntity> logs = auditOperationLogMapper.selectList(
    new LambdaQueryWrapper<AuditOperationLogEntity>()
        .eq(AuditOperationLogEntity::getBusinessId, sampleId)
        .eq(AuditOperationLogEntity::getModule, AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
        .orderByDesc(AuditOperationLogEntity::getCreateTime)
);
```

## 使用规范

### 业务模块（module）
使用 `AuditBusinessModule` 枚举的 `getName()` 方法：
```java
.module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.getName())
```

### 操作类型（operationType）
使用 `OperationType` 枚举的 `getValue()` 方法：
```java
.operationType(OperationType.RETENTION_EXTEND.getValue())
.operationType(OperationType.RETENTION_COLLECT.getValue())
.operationType(OperationType.RETENTION_DESTROY.getValue())
```

## 优势

1. ✅ **代码规范化** - 遵循项目统一的枚举规范
2. ✅ **类型安全** - 编译期检查，避免拼写错误
3. ✅ **可维护性** - 集中管理，便于统一修改
4. ✅ **可读性** - 枚举值语义明确，代码更易理解
5. ✅ **数据一致性** - 避免硬编码导致的数据不一致

## 数据库存储值

### module 字段存储值
```
留样样品管理
```

### operationType 字段存储值
```
retentionExtend  - 延期
retentionCollect - 领用
retentionDestroy - 销毁
```

## 修改文件清单

1. **AuditBusinessModule.java** - 新增 1 个业务模块枚举
2. **OperationType.java** - 新增 3 个操作类型枚举
3. **RetentionSampleManageServiceImpl.java** - 修改 5 处日志记录代码

## 验证

- ✅ 代码编译通过
- ✅ 枚举值与数据库字段匹配
- ✅ 遵循项目现有规范
