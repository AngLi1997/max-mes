# 留样功能接口文档

## 目录

- [1. 功能概述](#1-功能概述)
- [2. 数据库设计](#2-数据库设计)
- [3. 接口文档](#3-接口文档)
  - [3.1 留样样品接收](#31-留样样品接收)
  - [3.2 留样样品管理](#32-留样样品管理)
  - [3.3 留样台账查询](#33-留样台账查询)
  - [3.4 留样观察](#34-留样观察)
- [4. 数据字典](#4-数据字典)
- [5. 业务流程](#5-业务流程)
- [6. 注意事项](#6-注意事项)

---

## 1. 功能概述

留样功能用于管理需要长期保留的样品，包括样品接收、延期、领用、销毁和定期观察等功能，以及完整的台账查询系统。

### 1.1 主要功能模块

| 模块 | 功能说明 |
|------|---------|
| **留样样品接收** | 接收留样样品，记录储存位置，自动生成观察任务，记录接收台账 |
| **留样样品管理** | 查询、延期、领用、销毁留样样品，记录操作历史 |
| **留样观察** | 定期观察任务管理，记录观察结果和台账 |
| **留样台账查询** | 查询接收、销毁、领用、观察四类台账 |

### 1.2 核心特性

- ✅ 自动生成年度观察任务
- ✅ 储存位置管理
- ✅ 留样期限延期
- ✅ 样品领用及台账记录
- ✅ 样品销毁及台账记录
- ✅ 当前数量动态更新
- ✅ 观察任务顺序校验
- ✅ 操作历史完整记录
- ✅ 四类台账查询（接收、销毁、领用、观察）

---

## 2. 数据库设计

### 2.1 检验单表新增字段 (lm_inspection_order)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| retention_expiry_date | DATE | 留样期限（有效期至），用于留样检验单 |

**说明**：
- 此字段在创建留样检验单时填写，表示样品的有效期至日期
- 在取样确认、样品生成时，系统会自动将样品的留样期限设置为：`检验单的有效期至 + 1年`
- 例如：检验单有效期至 = 2028-06-01，则样品的留样期限 = 2029-06-01

### 2.2 样品表新增字段 (lm_sample)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| current_quantity | VARCHAR(50) | 当前样品数量（领用后会减少） |
| storage_location | VARCHAR(200) | 储存位置（留样样品必填） |
| retention_expiry_date | DATE | 留样期限（有效期至+1年） |
| destroyed | TINYINT(1) | 是否已销毁 |

### 2.3 留样观察任务表 (lm_retention_observation_task)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID |
| sample_id | BIGINT | 样品ID |
| sample_no | VARCHAR(100) | 样品编号 |
| observation_year | INT | 观察年度（第几年） |
| due_date | DATE | 任务到期日期 |
| completed | TINYINT(1) | 是否已完成 |
| observation_result | VARCHAR(50) | 观察结果 |
| observation_remark | TEXT | 观察备注 |
| observer_id | VARCHAR(50) | 观察人ID |
| observer_name | VARCHAR(100) | 观察人名称 |
| observation_time | DATETIME | 观察时间 |

### 2.4 留样观察台账表 (lm_retention_observation_ledger)

| 字段名 | 类型 | 说明 |
|--------|------|------|
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

### 2.5 样品领用台账表 (lm_sample_collection_ledger)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID |
| sample_no | VARCHAR(100) | 样品编号 |
| batch_no | VARCHAR(100) | 批号 |
| material_id | BIGINT | 物料ID |
| material_name | VARCHAR(200) | 物料名称 |
| material_code | VARCHAR(100) | 物料编码 |
| material_spec | VARCHAR(200) | 物料规格 |
| collect_quantity | VARCHAR(50) | 领用数量 |
| unit_id | BIGINT | 单位ID |
| collect_reason | VARCHAR(500) | 领用原因 |
| collector_id | VARCHAR(50) | 领样人ID |
| collector_name | VARCHAR(100) | 领样人名称 |
| collect_time | DATETIME | 领用时间 |

### 2.6 留样接收台账表 (lm_retention_receive_ledger)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID |
| sample_id | BIGINT | 样品ID |
| sample_no | VARCHAR(100) | 样品编号 |
| batch_no | VARCHAR(100) | 批号 |
| material_id | BIGINT | 物料ID |
| material_name | VARCHAR(200) | 物料名称 |
| material_code | VARCHAR(100) | 物料编码 |
| material_spec | VARCHAR(200) | 物料规格 |
| quantity | VARCHAR(50) | 样品数量 |
| unit_id | BIGINT | 单位ID |
| sampler_id | VARCHAR(50) | 取样人ID |
| sampler_name | VARCHAR(100) | 取样人名称 |
| sampling_time | DATETIME | 取样时间 |
| receiver_id | VARCHAR(50) | 接收人ID |
| receiver_name | VARCHAR(100) | 接收人名称 |
| receive_time | DATETIME | 接收时间 |
| storage_location | VARCHAR(200) | 储存位置 |

### 2.7 留样销毁台账表 (lm_retention_destruction_ledger)

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | BIGINT | 主键ID |
| sample_id | BIGINT | 样品ID |
| sample_no | VARCHAR(100) | 样品编号 |
| batch_no | VARCHAR(100) | 批号 |
| material_id | BIGINT | 物料ID |
| material_name | VARCHAR(200) | 物料名称 |
| material_code | VARCHAR(100) | 物料编码 |
| material_spec | VARCHAR(200) | 物料规格 |
| quantity | VARCHAR(50) | 销毁数量 |
| unit_id | BIGINT | 单位ID |
| destruction_method | VARCHAR(100) | 销毁方式 |
| destruction_location | VARCHAR(200) | 销毁地点 |
| destruction_time | DATETIME | 销毁时间 |
| remark | TEXT | 备注 |
| destructor_id | VARCHAR(50) | 销毁人ID |
| destructor_name | VARCHAR(100) | 销毁人名称 |
| supervisor_id | VARCHAR(50) | 监督人ID |
| supervisor_name | VARCHAR(100) | 监督人名称 |

---

## 3. 接口文档

### 3.1 留样样品接收

#### 3.1.1 查询留样接收列表

**接口地址**: `POST /api/app/lims2/sample-receive/retention/page`

**接口说明**: 分页查询待接收的留样样品列表

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "sampleNo": "样品编号（模糊查询，可选）",
  "orderNo": "检验单号（模糊查询，可选）"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| orderNo | String | 否 | 检验单号（模糊查询） |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 5,
    "list": [
      {
        "id": "8888881",
        "sampleNo": "RET-SAMPLE-001",
        "sampleName": "留样样品-原料药A",
        "inspectionOrderNo": "TEST-ORDER-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "500",
        "unitId": "1",
        "unitName": "g",
        "inspectItemName": "留样",
        "sampled": true,
        "received": false,
        "samplerName": "张三",
        "samplingTime": "2026-02-10 09:00:00"
      }
    ]
  }
}
```

#### 3.1.2 批量接收留样样品

**接口地址**: `POST /api/app/lims2/sample-receive/retention/batch-receive`

**接口说明**: 批量接收留样样品，必须指定储存位置

**请求参数**:

```json
{
  "sampleIds": [8888881, 8888882],
  "storageLocation": "Cold Storage Room A-Shelf 05"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleIds | Array\<Long\> | 是 | 样品ID列表 |
| storageLocation | String | 是 | 储存位置（必填） |

**业务逻辑**:

1. 验证样品是否已取样且未接收
2. 验证样品是否为留样检验项目
3. 更新样品状态为已接收
4. 保存储存位置
5. **自动生成留样观察任务**（根据留样期限按年度生成）
6. **记录留样接收台账**（记录接收信息）

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
|--------|------|
| 81000002 | 样品不存在 |
| 81000001 | 样品未取样 |
| 81000001 | 样品已接收 |
| 81000001 | 样品已作废 |
| 81000001 | 不是留样样品 |
| 81000001 | 储存位置不能为空 |

---

### 3.2 留样样品管理

#### 3.2.1 查询留样样品管理列表

**接口地址**: `POST /api/app/lims2/retention-sample-manage/page`

**接口说明**: 分页查询已接收的留样样品，支持按状态筛选

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "sampleNo": "样品编号（可选）",
  "batchNo": "批号（可选）",
  "materialIds": [1, 2, 3],
  "status": "received"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| batchNo | String | 否 | 批号（模糊查询） |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| status | String | 否 | 状态筛选，可选值：<br/>- received: 已接收<br/>- to_be_destroyed: 待销毁<br/>- destroyed: 已销毁 |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 2,
    "list": [
      {
        "id": "8888881",
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "500",
        "currentQuantity": "450",
        "unitId": "1",
        "unitName": "g",
        "retentionTime": "2026-02-10 11:00:00",
        "retentionUserId": "1970011556552871936",
        "retentionUserName": "李四",
        "retentionExpiryDate": "2029-06-01",
        "storageLocation": "Cold Storage Room B-Shelf 05",
        "received": true,
        "toBeDestroyed": false,
        "destroyed": false
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 样品ID |
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 物料名称 |
| materialCode | String | 物料编码 |
| materialSpec | String | 物料规格 |
| quantity | String | 样品数量（初始数量） |
| currentQuantity | String | 当前数量（领用后会减少） |
| unitId | String | 单位ID |
| unitName | String | 单位名称 |
| retentionTime | String | 留样时间（接收时间） |
| retentionUserId | String | 留样人ID |
| retentionUserName | String | 留样人名称 |
| retentionExpiryDate | String | 留样期限 |
| storageLocation | String | 储存位置 |
| received | Boolean | 是否已接收 |
| toBeDestroyed | Boolean | 是否待销毁（留样期限已过且未销毁） |
| destroyed | Boolean | 是否已销毁 |

#### 3.2.2 留样延期

**接口地址**: `POST /api/app/lims2/retention-sample-manage/{sampleId}/extend`

**接口说明**: 延长留样样品的留样期限

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | Long | 是 | 样品ID |

**请求参数**:

```json
{
  "newExpiryDate": "2029-12-31"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| newExpiryDate | String(Date) | 是 | 新的留样期限（格式：yyyy-MM-dd） |

**业务规则**:

1. 样品必须是留样样品
2. 样品必须已接收且未销毁
3. 新期限必须大于原期限
4. **自动判断是否生成新的观察任务** ⭐
   - 如果延期后的年数增加，会自动生成额外的观察任务
   - 例如：原留样期限3年（3个观察任务），延期到5年，自动生成第4年和第5年的观察任务
5. 自动记录操作历史

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
|--------|------|
| 81000002 | 样品不存在 |
| 81000001 | 不是留样样品 |
| 81000001 | 样品未接收 |
| 81000001 | 样品已销毁 |
| 81000001 | 新期限必须大于原期限 |

#### 3.2.3 留样样品领用

**接口地址**: `POST /api/app/lims2/retention-sample-manage/{sampleId}/collect`

**接口说明**: 从留样样品中领用指定数量

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | Long | 是 | 样品ID |

**请求参数**:

```json
{
  "collectQuantity": "50",
  "unitId": 1,
  "collectReason": "质量复检",
  "collectorId": "collector001",
  "collectorName": "王五"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| collectQuantity | String | 是 | 领用数量 |
| unitId | Long | 是 | 单位ID |
| collectReason | String | 否 | 领用原因 |
| collectorId | String | 是 | 领样人ID |
| collectorName | String | 是 | 领样人名称 |

**业务逻辑**:

1. 验证样品是否已接收且未销毁
2. 验证领用数量不能大于当前数量
3. 更新当前数量：`currentQuantity = currentQuantity - collectQuantity`
4. 记录领用台账
5. 记录操作历史

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
|--------|------|
| 81000002 | 样品不存在 |
| 81000001 | 不是留样样品 |
| 81000001 | 样品未接收 |
| 81000001 | 样品已销毁 |
| 81000001 | 领用数量不能大于当前样品数量 |

#### 3.2.4 查询样品操作历史

**接口地址**: `GET /api/app/lims2/retention-sample-manage/{sampleId}/history`

**接口说明**: 查询样品的所有操作历史记录

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | Long | 是 | 样品ID |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": [
    {
      "id": "2021122082397622272",
      "operationType": "领用",
      "detail": "",
      "remark": "",
      "createTime": "2026-02-10 15:20:30",
      "createBy": "李四"
    },
    {
      "id": "2021121950868443136",
      "operationType": "延期",
      "detail": "{\"expireDateUpdate\":\"2029-06-01\"}",
      "remark": "",
      "createTime": "2026-02-10 15:19:58",
      "createBy": "李四"
    }
  ]
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 记录ID |
| operationType | String | 操作类型（延期、领用、销毁等） |
| detail | String | 操作详情（JSON格式） |
| remark | String | 备注 |
| createTime | String | 操作时间 |
| createBy | String | 操作人 |

#### 3.2.5 销毁样品

**接口地址**: `POST /api/app/lims2/retention-sample-manage/{sampleId}/destroy`

**接口说明**: 销毁留样样品，记录销毁信息和销毁台账

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| sampleId | Long | 是 | 样品ID |

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

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| destructionReason | String | 是 | 销毁原因 |
| destructionMethod | String | 是 | 销毁方式（如：焚烧、掩埋等） |
| destructionTime | String(DateTime) | 是 | 销毁时间（格式：yyyy-MM-ddTHH:mm:ss） |
| destructionLocation | String | 是 | 销毁地点 |
| remark | String | 否 | 备注 |
| destructorId | String | 否 | 销毁人ID（不填自动使用当前登录人） |
| destructorName | String | 否 | 销毁人名称（不填自动使用当前登录人） |
| supervisorId | String | 是 | 监督人ID |
| supervisorName | String | 是 | 监督人名称 |

**业务逻辑**:

1. 验证样品是否已接收且未销毁
2. 更新样品状态：`destroyed = true`
3. 记录操作历史（操作类型：销毁，备注为请求中的remark）
4. 记录销毁台账（销毁数量为当前数量）
5. 如果未提供销毁人，自动填充为当前登录用户

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
|--------|------|
| 81000002 | 样品不存在 |
| 81000001 | 不是留样样品 |
| 81000001 | 样品未接收 |
| 81000001 | 样品已销毁 |
| 507 | 必填字段验证失败 |

---

### 3.3 留样台账查询

#### 3.3.1 查询留样接收台账列表

**接口地址**: `POST /api/app/lims2/retention-ledger/receive/page`

**接口说明**: 分页查询留样接收台账，记录样品接收的历史信息

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3],
  "batchNo": "批号（可选）",
  "sampleNo": "样品编号（可选）",
  "receiveStartDate": "2026-01-01",
  "receiveEndDate": "2026-12-31"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| batchNo | String | 否 | 批号（模糊查询） |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| receiveStartDate | String(Date) | 否 | 接收开始日期（格式：yyyy-MM-dd） |
| receiveEndDate | String(Date) | 否 | 接收结束日期（格式：yyyy-MM-dd） |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 15,
    "list": [
      {
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "500",
        "unitName": "g",
        "samplerName": "张三",
        "samplingTime": "2026-02-10T09:00:00",
        "receiverName": "李四",
        "receiveTime": "2026-02-10T11:00:00",
        "storageLocation": "Cold Storage Room A-Shelf 05"
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 检品名称 |
| materialCode | String | 检品编码 |
| materialSpec | String | 规格 |
| quantity | String | 样品数量 |
| unitName | String | 单位名称 |
| samplerName | String | 取样人名称 |
| samplingTime | String | 取样时间 |
| receiverName | String | 接收人名称 |
| receiveTime | String | 接收时间 |
| storageLocation | String | 储存位置 |

#### 3.3.2 查询留样销毁台账列表

**接口地址**: `POST /api/app/lims2/retention-ledger/destruction/page`

**接口说明**: 分页查询留样销毁台账，记录样品销毁的历史信息

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3],
  "batchNo": "批号（可选）",
  "sampleNo": "样品编号（可选）",
  "destructionStartDate": "2026-01-01",
  "destructionEndDate": "2026-12-31"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| batchNo | String | 否 | 批号（模糊查询） |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| destructionStartDate | String(Date) | 否 | 销毁开始日期（格式：yyyy-MM-dd） |
| destructionEndDate | String(Date) | 否 | 销毁结束日期（格式：yyyy-MM-dd） |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 8,
    "list": [
      {
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "450",
        "unitName": "g",
        "destructionMethod": "焚烧",
        "destructionLocation": "销毁室",
        "destructionTime": "2026-02-10T15:00:00",
        "remark": "按规定销毁",
        "destructorName": "张三",
        "supervisorName": "李四"
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 检品名称 |
| materialCode | String | 检品编码 |
| materialSpec | String | 规格 |
| quantity | String | 销毁数量 |
| unitName | String | 单位名称 |
| destructionMethod | String | 销毁方式 |
| destructionLocation | String | 销毁地点 |
| destructionTime | String | 销毁时间 |
| remark | String | 备注 |
| destructorName | String | 销毁人名称 |
| supervisorName | String | 监督人名称 |

#### 3.3.3 查询留样领用台账列表

**接口地址**: `POST /api/app/lims2/retention-ledger/collection/page`

**接口说明**: 分页查询留样领用台账，记录样品领用的历史信息

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3],
  "batchNo": "批号（可选）",
  "sampleNo": "样品编号（可选）",
  "collectStartDate": "2026-01-01",
  "collectEndDate": "2026-12-31"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| batchNo | String | 否 | 批号（模糊查询） |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| collectStartDate | String(Date) | 否 | 领用开始日期（格式：yyyy-MM-dd） |
| collectEndDate | String(Date) | 否 | 领用结束日期（格式：yyyy-MM-dd） |

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 12,
    "list": [
      {
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "collectQuantity": "50",
        "unitName": "g",
        "collectorName": "王五",
        "collectTime": "2026-02-10T14:00:00"
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 检品名称 |
| materialCode | String | 检品编码 |
| materialSpec | String | 规格 |
| collectQuantity | String | 领用数量 |
| unitName | String | 单位名称 |
| collectorName | String | 领用人名称 |
| collectTime | String | 领用时间 |

#### 3.3.4 查询留样观察台账列表

**接口地址**: `POST /api/app/lims2/retention-ledger/observation/page`

**接口说明**: 分页查询留样观察台账，记录观察任务的历史信息

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3],
  "batchNo": "批号（可选）",
  "sampleNo": "样品编号（可选）",
  "observationStartDate": "2026-01-01",
  "observationEndDate": "2026-12-31"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| batchNo | String | 否 | 批号（模糊查询） |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| observationStartDate | String(Date) | 否 | 观察开始日期（格式：yyyy-MM-dd） |
| observationEndDate | String(Date) | 否 | 观察结束日期（格式：yyyy-MM-dd） |

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
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "450",
        "unitName": "g",
        "observationResult": "符合",
        "observationRemark": "观察正常，无异常情况",
        "observerName": "赵六",
        "observationTime": "2027-02-10T10:00:00"
      }
    ]
  }
}
```

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 检品名称 |
| materialCode | String | 检品编码 |
| materialSpec | String | 规格 |
| quantity | String | 样品数量 |
| unitName | String | 单位名称 |
| observationResult | String | 观察结果 |
| observationRemark | String | 备注 |
| observerName | String | 观察人名称 |
| observationTime | String | 观察时间 |

---

### 3.4 留样观察

#### 3.4.1 查询留样观察任务列表

**接口地址**: `POST /api/app/lims2/retention-observation/task/page`

**接口说明**: 分页查询留样观察任务，支持临期任务筛选

**请求参数**:

```json
{
  "pageNum": 1,
  "pageSize": 10,
  "sampleNo": "样品编号（可选）",
  "batchNo": "批号（可选）",
  "materialIds": [1, 2, 3],
  "queryType": "all"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| pageNum | Integer | 是 | 页码 |
| pageSize | Integer | 是 | 每页数量 |
| sampleNo | String | 否 | 样品编号（模糊查询） |
| batchNo | String | 否 | 批号（模糊查询） |
| materialIds | Array\<Long\> | 否 | 物料ID列表 |
| queryType | String | 否 | 查询类型：<br/>- **upcoming**: 临期任务（一周内到期）<br/>- **all**: 全部任务（默认） |

**排序规则**:

- **临期任务** (upcoming): 按到期日期**正序**排序（越近的越靠前）
- **全部任务** (all): 按到期日期**倒序**排序（越晚的越靠前）

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "total": 5,
    "list": [
      {
        "id": "2021121605698195456",
        "sampleId": "8888881",
        "sampleNo": "RET-SAMPLE-001",
        "batchNo": "BATCH-2026-001",
        "materialName": "原料药A",
        "materialCode": "RAW-A-001",
        "materialSpec": "99.9%",
        "quantity": "450",
        "unitId": "1",
        "unitName": "g",
        "retentionTime": "2026-02-10 11:00:00",
        "retentionUserId": "1970011556552871936",
        "retentionUserName": "李四",
        "retentionExpiryDate": "2029-06-01",
        "storageLocation": "Cold Storage Room B-Shelf 05",
        "observationDueDate": "2027-02-10",
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

**字段说明**:

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | String | 任务ID |
| sampleId | String | 样品ID |
| sampleNo | String | 样品编号 |
| batchNo | String | 批号 |
| materialName | String | 物料名称 |
| materialCode | String | 物料编码 |
| materialSpec | String | 物料规格 |
| quantity | String | 样品数量（当前数量） |
| unitId | String | 单位ID |
| unitName | String | 单位名称 |
| retentionTime | String | 留样时间 |
| retentionUserId | String | 留样人ID |
| retentionUserName | String | 留样人名称 |
| retentionExpiryDate | String | 留样期限 |
| storageLocation | String | 储存位置 |
| observationDueDate | String | 观察到期时间 |
| observationYear | Integer | 观察年度（第几年） |
| completed | Boolean | 是否已完成 |
| observationResult | String | 观察结果 |
| observationRemark | String | 观察备注 |
| observerName | String | 观察人名称 |
| observationTime | String | 观察时间 |

#### 3.4.2 提交留样观察结果

**接口地址**: `POST /api/app/lims2/retention-observation/task/{taskId}/submit`

**接口说明**: 提交留样观察结果，记录观察台账

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| taskId | Long | 是 | 任务ID |

**请求参数**:

```json
{
  "observationResult": "符合",
  "observationRemark": "观察正常，无异常情况"
}
```

**参数说明**:

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| observationResult | String | 是 | 观察结果（如：符合、不符合） |
| observationRemark | String | 否 | 观察备注 |

**业务规则**:

1. 任务必须存在且未完成
2. **顺序校验**：必须按时间顺序完成观察任务，不能跳过早期任务
3. 自动记录观察人和观察时间（当前登录用户）
4. 自动生成观察台账
5. 更新任务状态为已完成

**响应示例**:

```json
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**错误码**:

| 错误码 | 说明 |
|--------|------|
| 81000002 | 任务不存在 |
| 81000001 | 任务已完成，不能重复提交 |
| 81000001 | 存在更早日期的留样观察任务未完成，请按照顺序完成 |
| 507 | 观察结果不能为空 |

**顺序校验说明**:

假设样品有3个观察任务：
- 任务1：2027-01-01到期
- 任务2：2028-01-01到期
- 任务3：2029-01-01到期

必须按顺序完成：任务1 → 任务2 → 任务3

如果尝试跳过任务1直接提交任务2，会返回错误："存在更早日期的留样观察任务未完成，请按照顺序完成"

---

## 4. 数据字典

### 4.1 检验项目常量

| 常量名 | 值 | 说明 |
|--------|---|------|
| RETENTION_INSPECT_ITEM_ID | 2000000000000000001 | 留样检验项目ID |
| RETENTION_INSPECT_ITEM_CODE | SAMPLE_RETENTION | 留样检验项目编码 |
| RETENTION_INSPECT_ITEM_NAME | 留样 | 留样检验项目名称 |

### 4.2 状态枚举

#### 留样样品状态

| 状态 | 说明 |
|------|------|
| received | 已接收（正常状态） |
| to_be_destroyed | 待销毁（留样期限已过且未销毁） |
| destroyed | 已销毁 |

#### 观察任务查询类型

| 类型 | 说明 |
|------|------|
| upcoming | 临期任务（一周内到期） |
| all | 全部任务 |

### 4.3 重要业务字段说明

| 字段名 | 说明 | 维护规则 |
|--------|------|----------|
| quantity | 样品数量 | 初始数量，不变 |
| current_quantity | 当前数量 | 初始等于quantity，领用后减少 |
| storage_location | 储存位置 | 留样样品接收时必填 |
| retention_expiry_date（样品） | 留样期限 | 取样确认时自动设置为：检验单.retention_expiry_date + 1年，可延期 |
| retention_expiry_date（检验单） | 有效期至 | 创建留样检验单时填写，表示样品的有效期至日期 |
| observation_year | 观察年度 | 第几年的观察任务 |
| due_date | 任务到期日期 | 接收日期+观察年度 |

---

## 5. 业务流程

### 5.1 留样样品完整流程

```
1. 检验单创建
   └─ 为留样检验单设置留样期限（retention_expiry_date）字段
   └─ 该字段表示样品的有效期至日期（例如：2028-06-01）

2. 取样确认（样品生成） ⭐
   └─ 生成留样检验项目的样品（检验项目ID = 2000000000000000001）
   └─ 样品的retention_expiry_date = 检验单.retention_expiry_date + 1年
   └─ 样品的current_quantity = 取样计划的planned_quantity
   └─ 样品的destroyed = false
   └─ 例如：
      - 检验单有效期至 = 2028-06-01
      - 样品留样期限 = 2029-06-01（自动加1年）

3. 留样样品接收 ⭐
   ├─ 必须指定储存位置（storage_location）
   ├─ 更新样品状态：received = true
   ├─ 记录接收人和接收时间
   └─ 自动生成观察任务 ⭐
      └─ 根据留样期限计算需要生成几个年度任务
      └─ 每年生成一个任务，due_date = receive_time + N年

4. 留样样品管理
   ├─ 查询列表（按状态筛选）
   ├─ 延期：延长retention_expiry_date
   ├─ 领用：减少current_quantity，记录台账
   └─ 销毁：更新destroyed=true，记录销毁台账

5. 留样观察
   ├─ 查询观察任务（临期任务提醒）
   └─ 提交观察结果
      ├─ 顺序校验：必须按时间顺序完成
      ├─ 记录观察结果和备注
      └─ 生成观察台账

6. 留样台账查询
   ├─ 接收台账：查询样品接收记录
   ├─ 销毁台账：查询样品销毁记录
   ├─ 领用台账：查询样品领用记录
   └─ 观察台账：查询观察任务记录
```

### 5.2 观察任务自动生成规则

**计算逻辑**:

```
留样期限 = 2029-01-01
接收日期 = 2026-01-01
年数 = 2029 - 2026 = 3年

生成3个观察任务：
- 任务1：observation_year=1, due_date=2027-01-01
- 任务2：observation_year=2, due_date=2028-01-01
- 任务3：observation_year=3, due_date=2029-01-01
```

### 5.3 current_quantity维护规则

| 操作 | 维护规则 |
|------|----------|
| 样品生成 | current_quantity = quantity |
| 样品接收 | 不变 |
| 样品领用 | current_quantity -= collect_quantity |
| 样品分样 | 原样品：current_quantity = 0<br/>子样品：current_quantity = quantity |

### 5.4 典型业务场景

#### 场景1：接收留样样品

```javascript
// 1. 查询待接收列表
POST /api/app/lims2/sample-receive/retention/page
{
  "pageNum": 1,
  "pageSize": 10
}

// 2. 批量接收样品
POST /api/app/lims2/sample-receive/retention/batch-receive
{
  "sampleIds": [123, 456],
  "storageLocation": "Cold Storage A-01"
}

// 结果：
// - 样品标记为已接收
// - 储存位置保存
// - 自动生成观察任务
```

#### 场景2：留样样品领用

```javascript
// 1. 查询留样样品
POST /api/app/lims2/retention-sample-manage/page
{
  "pageNum": 1,
  "pageSize": 10,
  "sampleNo": "RET-001"
}

// 2. 领用样品
POST /api/app/lims2/retention-sample-manage/123/collect
{
  "collectQuantity": "50",
  "unitId": 1,
  "collectReason": "质量复检",
  "collectorId": "user001",
  "collectorName": "张三"
}

// 结果：
// - current_quantity: 500 → 450
// - 生成领用台账记录
// - 记录操作历史
```

#### 场景3：留样观察

```javascript
// 1. 查询临期任务
POST /api/app/lims2/retention-observation/task/page
{
  "pageNum": 1,
  "pageSize": 10,
  "queryType": "upcoming"
}

// 2. 提交观察结果
POST /api/app/lims2/retention-observation/task/789/submit
{
  "observationResult": "符合",
  "observationRemark": "观察正常"
}

// 结果：
// - 任务标记为已完成
// - 记录观察人和时间
// - 生成观察台账
```

#### 场景4：销毁样品

```javascript
// 1. 查询待销毁样品
POST /api/app/lims2/retention-sample-manage/page
{
  "pageNum": 1,
  "pageSize": 10,
  "status": "to_be_destroyed"
}

// 2. 销毁样品
POST /api/app/lims2/retention-sample-manage/123/destroy
{
  "destructionReason": "留样期限已到",
  "destructionMethod": "焚烧",
  "destructionTime": "2026-02-10T15:00:00",
  "destructionLocation": "销毁室",
  "remark": "按规定销毁",
  "supervisorId": "user002",
  "supervisorName": "李四"
}

// 结果：
// - 样品标记为已销毁
// - 记录操作历史
// - 生成销毁台账
```

#### 场景5：查询台账

```javascript
// 1. 查询接收台账
POST /api/app/lims2/retention-ledger/receive/page
{
  "pageNum": 1,
  "pageSize": 10,
  "receiveStartDate": "2026-01-01",
  "receiveEndDate": "2026-12-31"
}

// 2. 查询销毁台账
POST /api/app/lims2/retention-ledger/destruction/page
{
  "pageNum": 1,
  "pageSize": 10,
  "materialIds": [1, 2, 3]
}

// 3. 查询领用台账
POST /api/app/lims2/retention-ledger/collection/page
{
  "pageNum": 1,
  "pageSize": 10,
  "sampleNo": "RET-001"
}

// 4. 查询观察台账
POST /api/app/lims2/retention-ledger/observation/page
{
  "pageNum": 1,
  "pageSize": 10,
  "batchNo": "BATCH-2026"
}
```

---

## 6. 注意事项

### 6.1 前端开发注意事项

#### 必填字段验证

1. **留样样品接收**
   - `storageLocation` 必填
   - 需要提示用户输入储存位置

2. **留样样品领用**
   - `collectQuantity` 必填，需验证不超过 `currentQuantity`
   - `collectorId` 和 `collectorName` 必填

3. **留样观察提交**
   - `observationResult` 必填
   - 建议提供下拉选项：符合、不符合

#### 状态展示

1. **留样样品列表**
   - `toBeDestroyed=true` 时，需要高亮显示或特殊标记
   - 展示 `currentQuantity` 而不是 `quantity`

2. **观察任务列表**
   - 临期任务（`queryType=upcoming`）需要高亮提示
   - 已完成任务不在列表中显示

#### 错误处理

1. **顺序校验错误**
   - 错误码：81000001
   - 提示："存在更早日期的留样观察任务未完成，请按照顺序完成"
   - 建议跳转到最早的未完成任务

2. **领用数量超限**
   - 错误码：81000001
   - 提示："领用数量不能大于当前样品数量"
   - 显示当前可用数量

### 6.2 业务规则提醒

1. **观察任务必须按顺序完成**
   - 前端可以考虑禁用未到期的任务按钮
   - 只允许提交最早的未完成任务

2. **留样期限延期**
   - 新期限必须大于原期限
   - 前端可以设置日期选择器的最小日期

3. **储存位置**
   - 留样样品接收时必须填写
   - 建议提供常用储存位置的下拉选项

4. **领用台账**
   - 建议在样品详情页展示领用历史
   - 可以调用操作历史接口获取

5. **销毁功能**
   - 销毁样品时必须填写监督人信息
   - 销毁人可以不填，默认为当前登录用户
   - 销毁后样品状态变为已销毁，不可再操作

6. **台账查询**
   - 支持按物料、批号、样品编号、日期范围筛选
   - 日期查询为左闭右闭区间（包含开始日期和结束日期）
   - 单位名称自动填充，无需手动查询

7. **检验单的留样期限**
   - 创建留样检验单时，需要填写"有效期至"字段（retention_expiry_date）
   - 该字段在检验单（lm_inspection_order）表中
   - 取样确认时，系统会自动将样品的留样期限设置为：检验单有效期至 + 1年
   - 前端无需手动输入样品的留样期限，这个字段会自动计算

### 6.3 性能优化建议

1. **列表查询**
   - 使用分页，建议每页10-20条
   - 支持按样品编号、批号快速筛选

2. **临期任务提醒**
   - 建议首页展示临期任务数量
   - 点击跳转到观察任务页面（queryType=upcoming）

3. **数据刷新**
   - 提交操作后刷新列表
   - 可以考虑使用乐观更新提升体验

### 6.4 常见问题

**Q1: 为什么接收样品后会自动生成观察任务？**

A: 留样样品需要定期观察，系统根据留样期限自动生成年度观察任务，确保不会遗漏观察。

**Q2: current_quantity 和 quantity 有什么区别？**

A:
- `quantity`: 样品初始数量，不变
- `current_quantity`: 当前可用数量，领用后会减少

**Q3: 为什么观察任务必须按顺序完成？**

A: 为了确保观察记录的连续性和完整性，必须按年度顺序完成观察任务。

**Q4: 留样期限延期后，观察任务会自动更新吗？**

A: 已生成的观察任务不会被修改，但会自动生成新的观察任务：
- 已生成的观察任务保持不变（年度、到期日期不变）
- 如果延期后的年数增加，系统会自动生成额外的年度观察任务
- 例如：
  - 原留样期限：2029-06-01（3年，已有3个观察任务）
  - 延期到：2031-06-01（5年）
  - 系统会自动生成第4年和第5年的观察任务（2个新任务）
- 延期还会更新样品的留样期限字段

**Q5: 如何判断样品是否为临期（待销毁）？**

A: 系统会在查询时动态计算 `toBeDestroyed` 字段：
- 当前日期 > 留样期限 且 未销毁 → `toBeDestroyed=true`

**Q6: 样品的留样期限（retention_expiry_date）是如何确定的？**

A: 样品的留样期限来源于检验单（lm_inspection_order表）：
- 创建留样检验单时，需要填写"有效期至"字段（retention_expiry_date）
- 在取样确认时，系统会自动生成留样样品
- 样品的留样期限自动计算为：`检验单.retention_expiry_date + 1年`
- 例如：
  - 检验单有效期至 = 2028-06-01
  - 生成样品的留样期限 = 2029-06-01（自动加1年）
- 样品生成后，留样期限可以通过延期接口进行修改

**Q7: 销毁样品后是否可以撤销？**

A: 不可以。样品一旦销毁（destroyed=true），状态不可逆，也不能再进行延期、领用等操作。销毁操作会记录在操作历史和销毁台账中，确保可追溯性。

**Q8: 台账数据什么时候生成？**

A: 台账数据在各个操作完成后自动生成：
- **接收台账**：批量接收留样样品后自动记录
- **领用台账**：领用样品后自动记录
- **观察台账**：提交观察结果后自动记录
- **销毁台账**：销毁样品后自动记录

台账记录失败不会影响主业务流程，但会记录日志便于排查。

**Q9: 如何查询某个样品的完整历史？**

A: 可以通过以下方式查询：
1. 调用操作历史接口（GET /api/app/lims2/retention-sample-manage/{sampleId}/history）查看所有操作记录
2. 根据样品编号在各个台账中查询：
   - 接收台账：查看接收记录
   - 领用台账：查看所有领用记录
   - 观察台账：查看所有观察记录
   - 销毁台账：查看销毁记录

**Q10: 台账查询的日期范围如何工作？**

A: 日期查询采用左闭右闭区间：
- 开始日期：自动拼接 00:00:00
- 结束日期：自动拼接 23:59:59
- 例如：receiveStartDate=2026-01-01, receiveEndDate=2026-01-31
  - 实际查询范围：2026-01-01 00:00:00 到 2026-01-31 23:59:59
  - 包含开始日期和结束日期当天的所有记录

---

## 7. 测试数据

### 7.1 测试账号

| 角色 | 用户名 | 说明 |
|------|--------|------|
| 接收人 | receiver001 | 用于测试样品接收 |
| 领样人 | collector001 | 用于测试样品领用 |
| 观察人 | observer001 | 用于测试观察任务 |

### 7.2 测试样品数据

| 字段 | 示例值 |
|------|--------|
| 样品编号 | RET-SAMPLE-001 |
| 批号 | BATCH-2026-001 |
| 物料名称 | 原料药A |
| 样品数量 | 500 |
| 单位 | g |
| 留样期限 | 2029-01-01 |
| 储存位置 | Cold Storage Room A-Shelf 01 |

---

## 8. 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0 | 2026-02-10 | 初始版本，包含留样接收、管理、观察功能 |
| v1.1 | 2026-02-10 | 新增销毁功能和四类台账查询（接收、销毁、领用、观察） |

---

## 9. 联系方式

如有问题，请联系后端开发团队。

**文档维护**: LIMS后端团队
**最后更新**: 2026-02-10 (v1.1 - 新增销毁功能和台账查询)
