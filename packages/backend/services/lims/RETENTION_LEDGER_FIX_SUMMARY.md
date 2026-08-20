# 留样样品台账字段修复及批量销毁功能实现

## 修改内容总结

### 1. 数据库字段修复

#### 1.1 样品领用台账表 (lm_sample_collection_ledger)
**问题：** 缺少 `sample_id` 和 `material_name` 字段

**解决方案：**
- 创建迁移脚本：`V1.1.1_0.0.43__fix_retention_ledger_fields.sql`
- 添加字段：
  - `sample_id BIGINT` - 样品ID
  - `material_name VARCHAR(200)` - 物料名称
- 添加索引：`idx_sample_id`

**修改文件：**
- `bmos-lims2-web/src/main/resources/init/db/V1.1.1_0.0.40__add_retention_sample_manage_fields.sql` - 更新建表语句
- `bmos-lims2-web/src/main/resources/init/db/V1.1.1_0.0.43__fix_retention_ledger_fields.sql` - 新增修复脚本

#### 1.2 DTO字段补充

**SampleCollectionLedgerListDTO**
- 添加字段：`collectReason` (领用原因)
- 文件：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/dto/SampleCollectionLedgerListDTO.java`

**RetentionReceiveLedgerListDTO**
- 添加字段：`storageLocation` (储存位置)
- 文件：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/dto/RetentionReceiveLedgerListDTO.java`

#### 1.3 Mapper XML查询修复

**SampleCollectionLedgerMapper.xml**
- 添加查询字段：`l.collect_reason AS collectReason`
- 文件：`bmos-lims2-server/src/main/resources/mapper/retention/SampleCollectionLedgerMapper.xml`

**RetentionReceiveLedgerMapper.xml**
- 添加查询字段：`l.storage_location AS storageLocation`
- 文件：`bmos-lims2-server/src/main/resources/mapper/retention/RetentionReceiveLedgerMapper.xml`

### 2. 批量销毁功能实现

#### 2.1 新增批量销毁请求VO
**文件：** `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/retention/vo/req/BatchSampleDestructionReqVO.java`

**字段说明：**
```java
- List<Long> sampleIds         // 样品ID列表（必填）
- String destructionReason      // 销毁原因（必填）
- String destructionMethod      // 销毁方式（必填）
- LocalDateTime destructionTime // 销毁时间（必填）
- String destructionLocation    // 销毁地点（必填）
- String remark                 // 备注（可选）
- String destructorId           // 销毁人ID（可选，默认当前登录人）
- String destructorName         // 销毁人名称（可选，默认当前登录人）
- String supervisorId           // 监督人ID（必填）
- String supervisorName         // 监督人名称（必填）
```

#### 2.2 Service层修改

**RetentionSampleManageService 接口**
- 新增方法：`void batchDestroySamples(List<Long> sampleIds, SampleDestructionDTO destructionDTO)`
- 文件：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/RetentionSampleManageService.java`

**RetentionSampleManageServiceImpl 实现类**
- 实现批量销毁方法：
  - 遍历样品ID列表
  - 为每个样品调用单个销毁方法
  - 记录成功/失败统计
  - 如果有失败，抛出异常并返回详细错误信息
- 文件：`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/retention/service/impl/RetentionSampleManageServiceImpl.java`

#### 2.3 Controller层修改

**RetentionSampleManageController**
- 保留原有单个销毁接口：`POST /{sampleId}/destroy`
- 新增批量销毁接口：`POST /batch-destroy`
- 文件：`bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/retention/RetentionSampleManageController.java`

### 3. API接口说明

#### 3.1 单个样品销毁（保留）
```
POST /retention-sample-manage/{sampleId}/destroy
```
**请求参数：** SampleDestructionReqVO
**说明：** 销毁单个样品

#### 3.2 批量样品销毁（新增）
```
POST /retention-sample-manage/batch-destroy
```
**请求参数：** BatchSampleDestructionReqVO
**说明：** 批量销毁多个样品

**请求示例：**
```json
{
  "sampleIds": [1, 2, 3],
  "destructionReason": "留样期满",
  "destructionMethod": "焚烧",
  "destructionTime": "2026-02-11 14:00:00",
  "destructionLocation": "销毁室",
  "remark": "批量销毁留样样品",
  "supervisorId": "user123",
  "supervisorName": "监督人"
}
```

### 4. 数据库迁移步骤

执行以下SQL脚本（按顺序）：
1. `V1.1.1_0.0.40__add_retention_sample_manage_fields.sql` - 建表（已更新）
2. `V1.1.1_0.0.43__fix_retention_ledger_fields.sql` - 修复字段（新增）

### 5. 验证清单

- [ ] 数据库迁移脚本执行成功
- [ ] lm_sample_collection_ledger 表包含 sample_id 和 material_name 字段
- [ ] 样品领用台账查询接口正常返回 collectReason 字段
- [ ] 留样接收台账查询接口正常返回 storageLocation 字段
- [ ] 单个样品销毁接口正常工作
- [ ] 批量样品销毁接口正常工作
- [ ] 批量销毁失败时能正确返回错误信息

### 6. 注意事项

1. **数据库迁移：** 在已有数据库上执行迁移脚本前，建议先备份数据
2. **批量销毁：** 批量销毁采用事务处理，如果某个样品销毁失败，会记录错误信息并继续处理其他样品
3. **错误处理：** 批量销毁完成后会返回成功和失败的统计信息
4. **向后兼容：** 保留了原有的单个销毁接口，不影响现有功能

## 修改文件清单

### 新增文件
1. `V1.1.1_0.0.43__fix_retention_ledger_fields.sql` - 数据库修复脚本
2. `BatchSampleDestructionReqVO.java` - 批量销毁请求VO

### 修改文件
1. `V1.1.1_0.0.40__add_retention_sample_manage_fields.sql` - 更新建表语句
2. `SampleCollectionLedgerListDTO.java` - 添加 collectReason 字段
3. `RetentionReceiveLedgerListDTO.java` - 添加 storageLocation 字段
4. `SampleCollectionLedgerMapper.xml` - 添加 collectReason 查询
5. `RetentionReceiveLedgerMapper.xml` - 添加 storageLocation 查询
6. `RetentionSampleManageService.java` - 添加批量销毁接口方法
7. `RetentionSampleManageServiceImpl.java` - 实现批量销毁逻辑
8. `RetentionSampleManageController.java` - 添加批量销毁接口
