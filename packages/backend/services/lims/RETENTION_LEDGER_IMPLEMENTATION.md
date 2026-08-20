# 留样台账功能实现总结

## 实现日期
2026-02-10

## 功能概述

根据需求文档实现了4个留样台账的完整功能：
1. **留样接收台账** - 记录样品接收信息
2. **留样领用台账** - 记录样品领用信息
3. **留样观察台账** - 记录观察任务信息
4. **留样销毁台账** - 记录样品销毁信息

## 一、数据库层

### 1.1 数据库迁移脚本
**文件**: `V1.1.1_0.0.42__create_retention_ledger_tables.sql`

创建了2个新表：
- `lm_retention_receive_ledger` - 留样接收台账表
- `lm_retention_destruction_ledger` - 留样销毁台账表

说明：
- 留样领用台账表 `lm_sample_collection_ledger` 已存在
- 留样观察台账表 `lm_retention_observation_ledger` 已存在

### 1.2 表字段说明

#### 留样接收台账表
- 样品基本信息：样品ID、样品编号、批号
- 物料信息：物料ID、物料名称、物料编码、物料规格
- 数量信息：样品数量、单位ID
- 取样信息：取样人ID、取样人名称、取样时间
- 接收信息：接收人ID、接收人名称、接收时间、储存位置

#### 留样销毁台账表
- 样品基本信息：样品ID、样品编号、批号
- 物料信息：物料ID、物料名称、物料编码、物料规格
- 销毁信息：销毁数量、销毁方式、销毁地点、销毁时间
- 人员信息：销毁人ID、销毁人名称、监督人ID、监督人名称
- 备注信息

## 二、后端实现

### 2.1 实体类（Entity）

新增文件：
- `RetentionReceiveLedger.java` - 留样接收台账实体
- `RetentionDestructionLedger.java` - 留样销毁台账实体

### 2.2 Mapper接口

新增文件：
- `RetentionReceiveLedgerMapper.java` - 接收台账Mapper
- `RetentionDestructionLedgerMapper.java` - 销毁台账Mapper

修改文件：
- `SampleCollectionLedgerMapper.java` - 添加了分页查询方法
- `RetentionObservationLedgerMapper.java` - 添加了分页查询方法

### 2.3 Mapper XML

新增文件：
- `RetentionReceiveLedgerMapper.xml` - 接收台账查询SQL
- `RetentionDestructionLedgerMapper.xml` - 销毁台账查询SQL
- `SampleCollectionLedgerMapper.xml` - 领用台账查询SQL
- `RetentionObservationLedgerMapper.xml` - 观察台账查询SQL

**查询功能**：
- 支持按物料ID集合、批号、样品编号模糊查询
- 支持按日期范围查询（接收日期/销毁日期/领用日期/观察日期）
- 自动填充单位名称（使用UnitCache）

### 2.4 DTO类

#### 查询条件DTO（4个）
- `RetentionReceiveLedgerPageQueryDTO.java`
- `RetentionDestructionLedgerPageQueryDTO.java`
- `SampleCollectionLedgerPageQueryDTO.java`
- `RetentionObservationLedgerPageQueryDTO.java`

#### 查询结果DTO（4个）
- `RetentionReceiveLedgerListDTO.java`
- `RetentionDestructionLedgerListDTO.java`
- `SampleCollectionLedgerListDTO.java`
- `RetentionObservationLedgerListDTO.java`

#### 其他DTO
- `SampleDestructionDTO.java` - 样品销毁请求DTO

### 2.5 Service层

新增接口：
- `RetentionLedgerService.java` - 统一管理4个台账查询的Service接口

新增实现：
- `RetentionLedgerServiceImpl.java` - 实现4个台账的分页查询，自动填充单位名称

修改文件：
- `RetentionSampleManageService.java` - 添加销毁方法
- `RetentionSampleManageServiceImpl.java` - 实现销毁功能并记录台账

### 2.6 台账记录逻辑

#### 接收台账记录
**位置**: `SampleReceiveServiceImpl.batchReceiveRetentionSamples()`
**时机**: 批量接收留样样品后
**记录内容**:
- 样品基本信息（从Sample获取）
- 物料信息（从InspectionOrder和Material查询）
- 取样信息（从Sample获取）
- 接收信息（当前用户和时间）
- 储存位置

#### 领用台账记录
**位置**: `RetentionSampleManageServiceImpl.collectSample()`
**时机**: 领用样品后
**记录内容**:
- 样品基本信息
- 物料信息（已优化，查询Material获取完整信息）
- 领用数量、单位
- 领用原因、领用人、领用时间

#### 观察台账记录
**位置**: `RetentionObservationServiceImpl.submitObservation()`
**时机**: 提交观察结果后
**记录内容**:
- 样品基本信息
- 物料信息
- 当前数量、单位
- 观察结果、观察备注
- 观察人、观察时间

#### 销毁台账记录
**位置**: `RetentionSampleManageServiceImpl.destroySample()`
**时机**: 销毁样品后
**记录内容**:
- 样品基本信息
- 物料信息
- 销毁数量（当前数量）
- 销毁方式、销毁地点、销毁时间
- 备注
- 销毁人、监督人

## 三、Web层实现

### 3.1 请求VO（4个）
- `RetentionReceiveLedgerPageReqVO.java`
- `RetentionDestructionLedgerPageReqVO.java`
- `SampleCollectionLedgerPageReqVO.java`
- `RetentionObservationLedgerPageReqVO.java`

### 3.2 响应VO（4个）
- `RetentionReceiveLedgerListRespVO.java`
- `RetentionDestructionLedgerListRespVO.java`
- `SampleCollectionLedgerListRespVO.java`
- `RetentionObservationLedgerListRespVO.java`

### 3.3 销毁功能VO
- `SampleDestructionReqVO.java` - 销毁请求VO（包含验证注解）

### 3.4 Controller

#### RetentionLedgerController（新增）
提供4个台账查询接口：
- `POST /api/app/lims2/retention-ledger/receive/page` - 查询接收台账
- `POST /api/app/lims2/retention-ledger/destruction/page` - 查询销毁台账
- `POST /api/app/lims2/retention-ledger/collection/page` - 查询领用台账
- `POST /api/app/lims2/retention-ledger/observation/page` - 查询观察台账

#### RetentionSampleManageController（修改）
新增销毁接口：
- `POST /api/app/lims2/retention-sample-manage/{sampleId}/destroy` - 销毁样品

## 四、接口文档

### 4.1 留样接收台账查询

**接口**: `POST /api/app/lims2/retention-ledger/receive/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2],
  "batchNo": "批号",
  "sampleNo": "样品编号",
  "receiveStartDate": "2026-01-01",
  "receiveEndDate": "2026-12-31"
}
```

**响应字段**:
- 样品编号、批号
- 检品名称、检品编码、规格
- 样品数量、单位名称
- 取样人名称、取样时间
- 接收人名称、接收时间

### 4.2 留样销毁台账查询

**接口**: `POST /api/app/lims2/retention-ledger/destruction/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2],
  "batchNo": "批号",
  "sampleNo": "样品编号",
  "destructionStartDate": "2026-01-01",
  "destructionEndDate": "2026-12-31"
}
```

**响应字段**:
- 样品编号、批号
- 检品名称、检品编码、规格
- 销毁数量、单位名称
- 销毁方式、销毁地点、销毁时间
- 备注
- 销毁人名称、监督人名称

### 4.3 留样领用台账查询

**接口**: `POST /api/app/lims2/retention-ledger/collection/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2],
  "batchNo": "批号",
  "sampleNo": "样品编号",
  "collectStartDate": "2026-01-01",
  "collectEndDate": "2026-12-31"
}
```

**响应字段**:
- 样品编号、批号
- 检品名称、检品编码、规格
- 领用数量、单位名称
- 领用人名称、领用时间

### 4.4 留样观察台账查询

**接口**: `POST /api/app/lims2/retention-ledger/observation/page`

**请求参数**:
```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2],
  "batchNo": "批号",
  "sampleNo": "样品编号",
  "observationStartDate": "2026-01-01",
  "observationEndDate": "2026-12-31"
}
```

**响应字段**:
- 样品编号、批号
- 检品名称、检品编码、规格
- 样品数量、单位名称
- 观察结果、备注
- 观察人名称、观察时间

### 4.5 销毁样品

**接口**: `POST /api/app/lims2/retention-sample-manage/{sampleId}/destroy`

**请求参数**:
```json
{
  "destructionReason": "留样期限已到",
  "destructionMethod": "焚烧",
  "destructionTime": "2026-02-10T15:00:00",
  "destructionLocation": "销毁室",
  "remark": "按规定销毁",
  "destructorId": "user001",
  "destructorName": "张三",
  "supervisorId": "user002",
  "supervisorName": "李四"
}
```

**必填字段**:
- destructionReason - 销毁原因
- destructionMethod - 销毁方式
- destructionTime - 销毁时间
- destructionLocation - 销毁地点
- supervisorId - 监督人ID
- supervisorName - 监督人名称

**可选字段**:
- remark - 备注
- destructorId、destructorName - 销毁人（不填写自动使用当前登录人）

## 五、关键优化

### 5.1 物料信息完善
修改了领用、接收、销毁台账记录逻辑，从Material表查询完整的物料信息：
- materialName (原来是直接取name)
- materialCode (原来是直接取code)
- materialSpec (原来是直接取specification)

### 5.2 单位名称填充
所有台账查询都使用UnitCache.getGlobalUnitName()填充单位名称，避免JOIN查询。

### 5.3 日期范围查询
前端传入日期（LocalDate），后端自动拼接时间：
- 开始日期：`CONCAT(#{startDate}, ' 00:00:00')`
- 结束日期：`CONCAT(#{endDate}, ' 23:59:59')`

### 5.4 错误处理
所有台账记录逻辑都使用try-catch包裹，记录失败不影响主流程。

## 六、新增文件清单

### 数据库
- V1.1.1_0.0.42__create_retention_ledger_tables.sql

### 实体类（2个）
- RetentionReceiveLedger.java
- RetentionDestructionLedger.java

### Mapper接口（2个新增 + 2个修改）
- RetentionReceiveLedgerMapper.java
- RetentionDestructionLedgerMapper.java
- SampleCollectionLedgerMapper.java（修改）
- RetentionObservationLedgerMapper.java（修改）

### Mapper XML（4个）
- RetentionReceiveLedgerMapper.xml
- RetentionDestructionLedgerMapper.xml
- SampleCollectionLedgerMapper.xml
- RetentionObservationLedgerMapper.xml

### DTO（9个）
- RetentionReceiveLedgerPageQueryDTO.java
- RetentionReceiveLedgerListDTO.java
- RetentionDestructionLedgerPageQueryDTO.java
- RetentionDestructionLedgerListDTO.java
- SampleCollectionLedgerPageQueryDTO.java
- SampleCollectionLedgerListDTO.java
- RetentionObservationLedgerPageQueryDTO.java
- RetentionObservationLedgerListDTO.java
- SampleDestructionDTO.java

### Service（2个新增 + 2个修改）
- RetentionLedgerService.java
- RetentionLedgerServiceImpl.java
- RetentionSampleManageService.java（修改）
- RetentionSampleManageServiceImpl.java（修改）

### Web层VO（9个）
- RetentionReceiveLedgerPageReqVO.java
- RetentionReceiveLedgerListRespVO.java
- RetentionDestructionLedgerPageReqVO.java
- RetentionDestructionLedgerListRespVO.java
- SampleCollectionLedgerPageReqVO.java
- SampleCollectionLedgerListRespVO.java
- RetentionObservationLedgerPageReqVO.java
- RetentionObservationLedgerListRespVO.java
- SampleDestructionReqVO.java

### Controller（1个新增 + 1个修改）
- RetentionLedgerController.java
- RetentionSampleManageController.java（修改）

### 修改的其他文件
- SampleReceiveServiceImpl.java - 添加接收台账记录逻辑

## 七、测试建议

### 7.1 台账记录测试
1. 接收留样样品 → 查询接收台账，验证记录是否生成
2. 领用样品 → 查询领用台账，验证记录是否生成
3. 提交观察结果 → 查询观察台账，验证记录是否生成
4. 销毁样品 → 查询销毁台账，验证记录是否生成

### 7.2 台账查询测试
1. 测试分页功能
2. 测试按物料ID集合筛选
3. 测试按批号模糊查询
4. 测试按样品编号模糊查询
5. 测试按日期范围查询
6. 验证单位名称是否正确显示

### 7.3 销毁功能测试
1. 测试销毁人默认填充（不传destructorId）
2. 测试必填字段验证
3. 测试样品状态更新（destroyed=true）
4. 测试操作历史记录
5. 测试销毁台账记录

## 八、注意事项

1. **日期处理**: 前端传入LocalDate，后端自动拼接时间为00:00:00和23:59:59
2. **物料字段**: Material实体使用name、code、specification，不是materialName、materialCode等
3. **单位填充**: 使用UnitCache而非JOIN查询
4. **错误容忍**: 台账记录失败不影响主业务流程
5. **销毁人**: 如果前端不传destructorId和destructorName，会自动使用当前登录人

## 九、完成情况

✅ 数据库表创建
✅ 实体类和Mapper
✅ Service层实现
✅ 台账记录逻辑集成
✅ Web层接口
✅ 销毁功能
✅ Git提交准备

所有代码已完成并添加到Git暂存区。
