# 留样功能前端API对接文档

## 文档说明

本文档记录留样功能对前端接口的所有变更，包括新增字段、请求参数、响应数据、错误码等信息。

---

## 一、接口变更概览

### 1.1 涉及的接口

| 接口路径 | 请求方法 | 接口说明 | 变更类型 |
|---------|---------|---------|---------|
| `/inspect/order/save` | POST | 保存检验单（新增/编辑） | 新增字段 |
| `/inspect/order/{id}` | GET | 查询检验单详情 | 新增字段 |
| `/inspect/order/page` | POST | 分页查询检验单列表 | 新增字段 |
| `/inspection-scheme/save` | POST | 保存检验方案（融合保存） | 支持留样取样配置 |
| `/inspection-scheme/update-samplings` | POST | 更新检验方案取样配置 | 支持留样取样配置 |

### 1.2 新增字段汇总

| 字段名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| `retentionRequired` | Boolean | 否 | 是否需要留样，默认false |
| `retentionExpiryDate` | String (Date) | 条件必填* | 有效期至，格式：yyyy-MM-dd |

*条件必填说明：当 `retentionRequired=true` 时，`retentionExpiryDate` 必填

---

## 二、API详细变更说明

### 2.1 保存检验单接口

**接口路径**: `POST /inspect/order/save`

#### 2.1.1 请求参数变更

在原有的 `InspectionOrderSaveVO` 基础上，新增以下字段：

```json
{
  "id": 123,                          // 编辑时传入，新增时不传
  "materialId": 456,                  // 检品ID（必填）
  "schemeVersionId": 789,             // 检验方案版本ID（必填）
  "batchNo": "BATCH001",              // 批次号
  "productionDate": "2026-01-01",     // 生产日期
  "templateId": 111,                  // 请验单模板ID
  "remark": "备注信息",

  // ========== 新增字段 ==========
  "retentionRequired": true,          // 是否需要留样（新增）
  "retentionExpiryDate": "2026-12-31",  // 有效期至（新增，留样时必填）
  // =============================

  "customFields": [...],              // 自定义字段
  "samplingList": [                   // 取样信息列表
    {
      "inspectItemId": 2000000000000000001,  // 留样检验项目ID
      "plannedQuantity": "10",
      "unitId": 1,
      "sampleCount": 3,
      "samplingMethod": "随机取样",
      "samplingLocation": "仓库A区",
      "samplingDescription": "留样取样说明"
    },
    {
      "inspectItemId": 999,            // 其他检验项目
      "plannedQuantity": "5",
      "unitId": 1,
      "sampleCount": 2
    }
  ]
}
```

#### 2.1.2 业务规则（重要）

**当 `retentionRequired = true` 时，需满足以下条件：**

1. **有效期至必填**
   - `retentionExpiryDate` 不能为空
   - 格式：`yyyy-MM-dd`
   - 违反时返回错误码：`83_18_02`

2. **取样信息不能为空**
   - `samplingList` 必须包含至少一条取样信息
   - 违反时返回错误码：`83_18_03`

3. **必须包含留样检验项目**
   - `samplingList` 中必须包含至少一条 `inspectItemId` 为留样检验项目ID的记录
   - 留样检验项目ID可通过检验项目下拉接口获取（code = "SAMPLE_RETENTION"）
   - 违反时返回错误码：`83_18_04`

**当 `retentionRequired = false` 或 `null` 时：**
- 不进行任何留样相关验证
- `retentionExpiryDate` 和 `samplingList` 可选

#### 2.1.3 错误码说明

| 错误码 | 错误信息 | 说明 | 前端处理建议 |
|--------|---------|------|-------------|
| 83_18_02 | 选择留样时，有效期至不能为空 | 选择留样但未填写有效期至 | 提示用户填写有效期至 |
| 83_18_03 | 选择留样时，必须添加留样的取样信息 | 选择留样但取样信息为空 | 提示用户添加取样信息 |
| 83_18_04 | 选择留样时，取样信息中必须包含留样检验项目 | 取样信息中没有留样检验项目 | 提示用户添加留样检验项目的取样信息 |
| 83_18_05 | 系统未配置留样检验项目，请联系管理员 | 系统配置异常 | 提示用户联系管理员 |

#### 2.1.4 成功响应示例

```json
{
  "code": 200,
  "message": "success",
  "data": 123456789  // 返回检验单ID
}
```

---

### 2.2 查询检验单详情接口

**接口路径**: `GET /inspect/order/{id}`

#### 2.2.1 响应数据变更

在原有的 `InspectionOrderRespVO` 基础上，新增以下字段：

```json
{
  "id": 123,
  "orderNo": "QY202602060001",
  "materialId": 456,
  "materialName": "测试物料",
  "materialCode": "MAT001",
  "orderStatus": "PENDING_CONFIRM",
  "batchNo": "BATCH001",

  // ========== 新增字段 ==========
  "retentionRequired": true,          // 是否需要留样（新增）
  "retentionExpiryDate": "2026-12-31",  // 有效期至（新增）
  // =============================

  "createTime": "2026-02-06 10:00:00",
  "updateTime": "2026-02-06 10:00:00",
  "samplingList": [
    {
      "id": 111,
      "inspectionOrderId": 123,
      "inspectItemId": 2000000000000000001,  // 留样检验项目ID
      "inspectItemName": "留样",
      "plannedQuantity": "10",
      "unitId": 1,
      "unitName": "kg",
      "sampleCount": 3
    },
    {
      "id": 112,
      "inspectionOrderId": 123,
      "inspectItemId": 999,
      "inspectItemName": "其他检验项目",
      "plannedQuantity": "5",
      "unitId": 1,
      "unitName": "kg",
      "sampleCount": 2
    }
  ]
}
```

#### 2.2.2 字段说明

| 字段名 | 类型 | 可能的值 | 说明 |
|-------|------|---------|------|
| `retentionRequired` | Boolean | true / false / null | 是否需要留样，null表示未设置（默认false） |
| `retentionExpiryDate` | String | "2026-12-31" / null | 有效期至，格式：yyyy-MM-dd |

---

### 2.3 分页查询检验单列表接口

**接口路径**: `POST /inspect/order/page`

#### 2.3.1 响应数据变更

列表中的每条记录都会包含新增的留样字段：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "list": [
      {
        "id": 123,
        "orderNo": "QY202602060001",
        "materialName": "测试物料",
        "orderStatus": "PENDING_CONFIRM",

        // ========== 新增字段 ==========
        "retentionRequired": true,          // 是否需要留样（新增）
        "retentionExpiryDate": "2026-12-31 23:59:59",  // 有效期至（新增）
        // =============================

        "createTime": "2026-02-06 10:00:00",
        "createBy": "张三"
      }
    ]
  }
}
```

---

## 三、前端开发建议

### 3.1 UI交互流程

```
1. 用户勾选"是否留样"复选框
   ↓
2. 当勾选时，显示"有效期至"日期时间选择器（必填状态）
   ↓
3. 在取样信息表格中，至少添加一条留样检验项目的取样记录
   ↓
4. 保存时进行前端验证：
   - 如果勾选留样，检查有效期至是否填写
   - 如果勾选留样，检查取样信息是否包含留样检验项目
   ↓
5. 调用保存接口
   ↓
6. 根据返回的错误码显示相应提示
```

### 3.2 前端验证逻辑（建议）

```javascript
// 保存前验证
function validateRetention(formData) {
  // 如果选择了留样
  if (formData.retentionRequired === true) {

    // 1. 验证有效期至
    if (!formData.retentionExpiryDate) {
      return {
        valid: false,
        message: '选择留样时，有效期至不能为空'
      };
    }

    // 2. 验证取样信息不为空
    if (!formData.samplingList || formData.samplingList.length === 0) {
      return {
        valid: false,
        message: '选择留样时，必须添加留样的取样信息'
      };
    }

    // 3. 验证是否包含留样检验项目
    const RETENTION_ITEM_ID = 2000000000000000001; // 留样检验项目ID
    const hasRetentionItem = formData.samplingList.some(
      item => item.inspectItemId === RETENTION_ITEM_ID
    );

    if (!hasRetentionItem) {
      return {
        valid: false,
        message: '选择留样时，取样信息中必须包含留样检验项目'
      };
    }
  }

  return { valid: true };
}
```

### 3.3 错误处理示例

```javascript
// 保存接口调用
async function saveInspectionOrder(formData) {
  try {
    const response = await api.post('/inspect/order/save', formData);

    if (response.code === 200) {
      message.success('保存成功');
      return response.data;
    }

  } catch (error) {
    // 根据错误码显示不同提示
    const errorCode = error.code;

    switch (errorCode) {
      case 83_18_02:
        message.error('选择留样时，有效期至不能为空');
        break;
      case 83_18_03:
        message.error('选择留样时，必须添加留样的取样信息');
        break;
      case 83_18_04:
        message.error('选择留样时，取样信息中必须包含留样检验项目');
        break;
      case 83_18_05:
        message.error('系统未配置留样检验项目，请联系管理员');
        break;
      default:
        message.error(error.message || '保存失败');
    }
  }
}
```

### 3.4 获取留样检验项目ID

留样检验项目是一个特殊的**系统内置**检验项目，其固定ID为 `2000000000000000001`，code为 `SAMPLE_RETENTION`。

**重要说明：留样检验项目不会在前端的检验项目列表中显示**

- 后端已在检验项目查询接口中自动过滤了系统内置项目（`is_system = 1`）
- 前端调用检验项目列表接口时，不会返回留样检验项目
- 前端无需手动过滤

**获取留样检验项目ID的方式：**

**方式一：使用固定ID（强烈推荐）**
```javascript
const RETENTION_INSPECT_ITEM_ID = 2000000000000000001;
const RETENTION_INSPECT_ITEM_CODE = 'SAMPLE_RETENTION';
const RETENTION_INSPECT_ITEM_NAME = '留样';
```

**方式二：调用专门的接口获取（如果后端提供）**
```javascript
// 如果后端提供了获取系统内置检验项目的专门接口
const retentionItem = await api.get('/inspect/item/system/SAMPLE_RETENTION');
```

**在取样信息中使用留样检验项目：**

```javascript
// 添加取样信息时，直接使用固定ID
const samplingItem = {
  inspectItemId: 2000000000000000001,  // 留样检验项目ID
  inspectItemName: '留样',              // 留样检验项目名称（用于显示）
  plannedQuantity: "10",
  unitId: 1,
  sampleCount: 3
};

formData.samplingList.push(samplingItem);
```

---

## 三A、检验方案接口变更说明

### 3A.1 检验方案中的留样取样配置

检验方案中也支持配置留样的取样信息。在检验方案的取样配置中，可以添加留样检验项目的取样配置。

**接口路径**: `POST /inspection-scheme/save`

#### 请求参数示例

```json
{
  "basic": {
    "name": "铁矿石检验方案",
    "versionNo": "1.0",
    "description": "包含留样配置的检验方案",
    "material": {
      "materialId": 123,
      "materialName": "铁矿石",
      "materialCode": "FYS-001"
    },
    "packageInfo": {
      "packageId": 456,
      "packageName": "标准检验包",
      "packageCode": "PKG-001"
    }
  },
  "itemUpdates": [
    {
      "inspectItemId": 100,
      "isRequired": true,
      "sort": 1
    }
  ],
  "samplingUpdates": [
    {
      "inspectItemId": 2000000000000000001,  // 留样检验项目ID
      "samplingAmount": 100,
      "samplingUnit": "g",
      "samplingCount": 3
    },
    {
      "inspectItemId": 100,  // 其他检验项目
      "samplingAmount": 50,
      "samplingUnit": "g",
      "samplingCount": 2
    },
    {
      "inspectItemId": null,  // 整体取样
      "samplingAmount": 200,
      "samplingUnit": "g",
      "samplingCount": 5
    }
  ]
}
```

#### 取样配置说明

**InspectionSchemeSamplingUpdateReqVO 结构：**

| 字段名 | 类型 | 是否必填 | 说明 |
|-------|------|---------|------|
| `inspectItemId` | Long | 否 | 检验项目ID，为null表示整体取样，为2000000000000000001表示留样 |
| `samplingAmount` | Number | 是 | 取样量，必须大于0 |
| `samplingUnit` | String | 是 | 取样单位，如：g、mL、个等 |
| `samplingCount` | Integer | 是 | 取样份数，必须大于0 |

#### 单独更新取样配置接口

**接口路径**: `POST /inspection-scheme/update-samplings`

```json
[
  {
    "schemeId": 1,
    "versionId": 1,
    "samplingConfigId": null,           // 新增时为null
    "inspectItemId": 2000000000000000001,  // 留样检验项目ID
    "samplingAmount": 100,
    "samplingUnit": "g",
    "samplingCount": 3
  }
]
```

#### 前端实现建议

**1. 在检验方案配置页面中支持留样**

```javascript
// 添加留样取样配置
function addRetentionSampling() {
  const retentionSampling = {
    inspectItemId: 2000000000000000001,  // 留样检验项目ID
    inspectItemName: '留样',              // 固定显示
    samplingAmount: '',                  // 由用户填写
    samplingUnit: '',                    // 由用户选择
    samplingCount: 1                     // 默认1份
  };

  // 添加到方案的取样配置列表
  schemeData.samplingUpdates.push(retentionSampling);
}

// 判断是否已包含留样配置
function hasRetentionSampling(samplingList) {
  return samplingList.some(
    item => item.inspectItemId === 2000000000000000001
  );
}
```

**2. 显示取样配置时的特殊处理**

```javascript
// 在取样配置列表中，对留样项目特殊显示
function renderSamplingItemName(item) {
  if (item.inspectItemId === 2000000000000000001) {
    return '<el-tag type="warning" size="small">系统</el-tag> 留样';
  } else if (item.inspectItemId === null) {
    return '<el-tag type="info" size="small">整体</el-tag> 整体取样';
  }
  return item.inspectItemName;  // 普通检验项目
}
```

**3. 检验项目下拉选择时的处理**

```html
<el-select v-model="sampling.inspectItemId" placeholder="选择检验项目">
  <!-- 整体取样选项 -->
  <el-option :value="null" label="整体取样"></el-option>

  <!-- 留样选项（手动添加，不从接口获取） -->
  <el-option
    :value="2000000000000000001"
    label="留样">
    <span>
      <el-tag type="warning" size="small">系统</el-tag>
      留样
    </span>
  </el-option>

  <!-- 普通检验项目（从接口获取，会自动过滤掉留样项目） -->
  <el-option
    v-for="item in inspectItems"
    :key="item.id"
    :value="item.id"
    :label="item.name">
  </el-option>
</el-select>
```

#### 注意事项

1. **留样检验项目不会在检验项目列表中返回**，需要手动添加到下拉选项中
2. **方案和请验单都支持留样取样配置**，配置方式一致
3. **inspectItemId 为 null** 表示整体取样，**为 2000000000000000001** 表示留样
4. 取样配置是可选的，不是必填项

---

## 四、前端页面修改清单

### 4.1 请验单新增/编辑页面

需要添加的UI组件：

1. **是否留样复选框**
   - 字段名：`retentionRequired`
   - 类型：Checkbox
   - 默认值：false

2. **有效期至日期选择器**
   - 字段名：`retentionExpiryDate`
   - 类型：DatePicker
   - 格式：yyyy-MM-dd
   - 显示条件：当 `retentionRequired === true` 时显示
   - 必填：是（当显示时）

3. **取样信息表格**
   - 在检验项目下拉列表中，需要包含"留样"检验项目
   - 当选择留样时，建议自动添加一条留样检验项目的取样记录

### 4.2 请验单详情页面

需要显示的信息：

1. **是否留样**
   - 显示：是 / 否

2. **有效期至**
   - 格式：yyyy-MM-dd
   - 显示条件：当 `retentionRequired === true` 时显示

### 4.3 请验单列表页面

可选添加的列：

1. **是否留样**
   - 可作为筛选条件
   - 可在列表中显示（显示：是/否）

2. **有效期至**
   - 可在列表中显示
   - 可作为排序字段

### 4.4 检验方案配置页面

需要修改的功能：

1. **取样配置表格**
   - 在检验项目下拉列表中，手动添加"留样"选项（固定ID: 2000000000000000001）
   - 支持添加留样检验项目的取样配置
   - 对留样取样配置添加特殊标识（系统标签）

2. **取样配置的检验项目选择**
   ```
   下拉选项包括：
   - 整体取样 (inspectItemId = null)
   - 留样 (inspectItemId = 2000000000000000001) - 手动添加
   - 其他检验项目 (从接口获取，已自动过滤留样)
   ```

---

## 五、测试用例建议

### 5.1 正向测试

| 测试场景 | 操作步骤 | 期望结果 |
|---------|---------|---------|
| 不选择留样 | retentionRequired=false，不填写其他留样字段 | 保存成功 |
| 正常留样流程 | retentionRequired=true，填写有效期至，添加留样取样信息 | 保存成功 |
| 编辑留样信息 | 编辑已有请验单，修改留样相关字段 | 保存成功 |

### 5.2 异常测试

| 测试场景 | 操作步骤 | 期望结果 |
|---------|---------|---------|
| 选择留样但不填有效期至 | retentionRequired=true，retentionExpiryDate为空 | 返回错误码83_18_02 |
| 选择留样但取样信息为空 | retentionRequired=true，samplingList为空 | 返回错误码83_18_03 |
| 选择留样但无留样检验项目 | retentionRequired=true，samplingList中无留样项目 | 返回错误码83_18_04 |

### 5.3 检验方案留样配置测试

| 测试场景 | 操作步骤 | 期望结果 |
|---------|---------|---------|
| 方案中添加留样取样配置 | 在取样配置中选择"留样"检验项目，填写取样量、单位、份数 | 保存成功 |
| 方案中同时配置多个取样 | 配置整体取样、留样取样、项目级取样 | 保存成功，所有配置正确存储 |
| 方案取样配置中手动选择留样 | 在检验项目下拉中选择"留样"（手动添加的选项） | 可正常选择，保存成功 |
| 编辑方案的留样取样配置 | 修改留样取样的数量、单位等 | 更新成功 |

---

## 十一、版本信息

| 版本 | 更新日期 | 更新内容 | 更新人 |
|-----|---------|---------|--------|
| 1.3 | 2026-02-06 | 有效期至字段从DateTime改为Date类型，格式从"yyyy-MM-dd HH:mm:ss"改为"yyyy-MM-dd" | 后端开发 |
| 1.2 | 2026-02-06 | 增加检验方案接口支持留样取样配置说明 | 后端开发 |
| 1.1 | 2026-02-06 | 增加留样检验项目不在前端显示的说明 | 后端开发 |
| 1.0 | 2026-02-06 | 初始版本，留样功能API变更 | 后端开发 |

---

## 十二、更新日志

### v1.2 (2026-02-06)

**新增内容：**
1. 添加"检验方案接口变更说明"章节（三A）
2. 说明检验方案中如何配置留样取样信息
3. 提供检验方案保存接口的完整示例
4. 增加检验方案页面修改建议
5. 增加检验方案相关测试用例

**技术说明：**
- 检验方案的 `InspectionSchemeSampling` 实体已支持通过 `inspectItemId` 关联检验项目
- 留样检验项目（ID: 2000000000000000001）可直接作为取样配置的检验项目
- `/inspection-scheme/save` 和 `/inspection-scheme/update-samplings` 接口无需修改，已支持留样配置

### v1.1 (2026-02-06)

**新增内容：**
1. 添加"留样检验项目特殊说明"章节，详细说明系统内置项目的特性
2. 说明留样检验项目不会在前端列表中显示的机制
3. 提供前端使用留样检验项目的完整代码示例
4. 增加UI建议和操作限制说明
5. 新增5个常见问题解答

**技术变更：**
- 数据库表 `lm_inspect_item` 新增字段 `is_system` 用于标识系统内置项目
- 检验项目查询接口自动过滤 `is_system=1` 的记录
- 留样检验项目在创建时设置 `is_system=1`

---

## 十三、联调注意事项

1. **留样检验项目ID**：固定为 `2000000000000000001`，需要在数据库迁移脚本执行后才会存在

2. **日期格式**：前端传给后端的日期格式必须为 `yyyy-MM-dd`

3. **字段可空性**：
   - `retentionRequired` 可为 null，后端会将 null 视为 false
   - `retentionExpiryDate` 只在 `retentionRequired=true` 时才验证

4. **向后兼容**：
   - 新增字段对现有请验单完全兼容
   - 前端不传留样字段时，后端使用默认值（retentionRequired=false）

5. **数据库迁移**：
   - 联调前需要确保已执行迁移脚本 `V1.1.1_0.0.37__add_retention_fields_to_inspection_order.sql`
   - 迁移脚本会创建留样检验项目

---

## 九、留样检验项目特殊说明

### 9.1 系统内置项目

留样检验项目是**系统内置**的特殊检验项目，具有以下特点：

| 属性 | 值 | 说明 |
|-----|---|------|
| ID | 2000000000000000001 | 固定的雪花ID |
| code | SAMPLE_RETENTION | 检验项目编码 |
| name | 留样 | 检验项目名称 |
| isSystem | 1 | 系统内置标识 |

### 9.2 前端不可见

**重要：留样检验项目不会在前端的检验项目列表中显示**

后端已在以下接口中自动过滤系统内置项目：
- 检验项目分页查询接口
- 检验项目列表（下拉）接口
- 检验项目搜索接口

前端无需手动过滤，调用这些接口时不会返回留样检验项目。

### 9.3 前端如何使用

虽然留样检验项目不在列表中显示，但前端需要在以下场景使用：

**场景1：用户选择留样时**
```javascript
// 当用户勾选"是否留样"时，自动添加留样取样信息
function onRetentionRequiredChange(checked) {
  if (checked) {
    // 自动添加留样检验项目的取样信息
    const retentionSampling = {
      inspectItemId: 2000000000000000001,
      inspectItemName: '留样',  // 固定显示为"留样"
      plannedQuantity: '',      // 由用户填写
      unitId: null,             // 由用户选择
      sampleCount: 1,           // 默认1份
      samplingMethod: '',
      samplingLocation: '',
      samplingDescription: '留样取样'
    };

    formData.samplingList.push(retentionSampling);
  } else {
    // 取消留样时，移除留样检验项目的取样信息
    formData.samplingList = formData.samplingList.filter(
      item => item.inspectItemId !== 2000000000000000001
    );
  }
}
```

**场景2：显示取样信息时**
```javascript
// 在取样信息列表中，留样检验项目显示为"留样"
function renderInspectItemName(item) {
  if (item.inspectItemId === 2000000000000000001) {
    return '留样';  // 特殊显示
  }
  return item.inspectItemName;  // 其他检验项目正常显示
}
```

**场景3：编辑请验单时**
```javascript
// 编辑时，检查是否包含留样取样信息
function loadInspectionOrderDetail(data) {
  formData.retentionRequired = data.retentionRequired;
  formData.retentionExpiryDate = data.retentionExpiryDate;
  formData.samplingList = data.samplingList;

  // 如果取样列表中包含留样检验项目，确保留样复选框被勾选
  const hasRetention = data.samplingList.some(
    item => item.inspectItemId === 2000000000000000001
  );

  if (hasRetention && !formData.retentionRequired) {
    console.warn('数据异常：包含留样取样信息但未勾选留样');
    formData.retentionRequired = true;  // 自动修正
  }
}
```

### 9.4 UI建议

**取样信息表格中的留样项目：**

建议在取样信息表格中，对留样检验项目做特殊标识：

```html
<!-- 示例：在检验项目名称列显示特殊标记 -->
<template>
  <el-table :data="samplingList">
    <el-table-column label="检验项目" prop="inspectItemName">
      <template slot-scope="scope">
        <span v-if="scope.row.inspectItemId === 2000000000000000001">
          <el-tag type="warning" size="small">系统</el-tag>
          {{ scope.row.inspectItemName }}
        </span>
        <span v-else>
          {{ scope.row.inspectItemName }}
        </span>
      </template>
    </el-table-column>
    <!-- 其他列 -->
  </el-table>
</template>
```

**留样取样信息的操作限制：**

建议对留样检验项目的取样信息做以下限制：
- 当选择留样时，留样取样信息不允许删除（可以编辑）
- 当取消留样时，自动删除留样取样信息
- 检验项目列下拉选择时，不显示留样选项（因为由系统自动添加）

```javascript
// 示例：删除取样信息时的限制
function removeSamplingItem(index) {
  const item = formData.samplingList[index];

  // 如果是留样检验项目，且当前勾选了留样，不允许删除
  if (item.inspectItemId === 2000000000000000001 && formData.retentionRequired) {
    this.$message.warning('留样取样信息不允许删除，请先取消勾选"是否留样"');
    return;
  }

  formData.samplingList.splice(index, 1);
}
```

---

## 十、常见问题FAQ

**Q1: 留样检验项目的ID是多少？**
A: 固定ID为 `2000000000000000001`，code为 `SAMPLE_RETENTION`，名称为"留样"。

**Q2: 有效期至的格式是什么？**
A: 格式为 `yyyy-MM-dd`，例如：`2026-12-31`。

**Q3: 如果用户取消勾选留样，已填写的有效期至需要清空吗？**
A: 建议清空，但不是强制的。后端只在 `retentionRequired=true` 时才验证留样字段。

**Q4: 留样时长如何计算？**
A: 留样时长 = 有效期至 + 1年。这个逻辑由样品管理模块实现，请验模块只负责存储有效期至。

**Q5: 一个请验单可以添加多条留样取样信息吗？**
A: 可以。只要至少包含一条留样检验项目的取样信息即可，可以有多条。

**Q6: 旧数据如何处理？**
A: 数据库迁移脚本会为所有现有请验单设置 `retentionRequired=0`（不留样），`retentionExpiryDate=null`。

**Q7: 为什么在检验项目列表中看不到留样检验项目？**
A: 留样检验项目是系统内置的特殊项目（`is_system=1`），后端已在查询接口中自动过滤，不会返回给前端。前端应使用固定ID（`2000000000000000001`）来使用留样检验项目，无需从列表中查询。

**Q8: 如果需要显示留样检验项目怎么办？**
A: 在取样信息表格中显示时，检验项目名称固定为"留样"。前端不需要从检验项目列表接口获取，直接硬编码即可：
```javascript
const RETENTION_ITEM = {
  id: 2000000000000000001,
  code: 'SAMPLE_RETENTION',
  name: '留样'
};
```

**Q9: 用户能否手动添加留样检验项目的取样信息？**
A: 不建议。建议由系统自动添加/删除留样取样信息：
- 勾选"是否留样"时，自动添加
- 取消勾选时，自动删除
- 在检验项目下拉列表中隐藏留样选项，避免用户手动选择

**Q10: 编辑请验单时，如何判断是否需要显示留样相关字段？**
A: 检查 `retentionRequired` 字段或检查取样信息列表中是否包含留样检验项目（`inspectItemId === 2000000000000000001`）。

---

## 十一、版本信息

| 版本 | 更新日期 | 更新内容 | 更新人 |
|-----|---------|---------|--------|
| 1.1 | 2026-02-06 | 增加留样检验项目系统内置机制说明 | 后端开发 |
| 1.0 | 2026-02-06 | 初始版本，留样功能API变更 | 后端开发 |

---

## 十二、后端技术变更记录

### 12.1 数据库变更

**新增表字段：**

1. `lm_inspect_item` 表
   - 新增字段：`is_system` TINYINT(1) DEFAULT 0 COMMENT '是否为系统内置项目'

2. `lm_inspection_order` 表
   - 新增字段：`retention_required` TINYINT(1) DEFAULT 0 COMMENT '是否需要留样'
   - 新增字段：`retention_expiry_date` DATETIME NULL COMMENT '有效期至'

**新增数据：**
- 插入留样检验项目记录（ID: 2000000000000000001, code: SAMPLE_RETENTION, is_system: 1）

### 12.2 实体类变更

**新增/修改的类：**
1. `InspectItem.java` - 新增 `isSystem` 字段
2. `InspectItemDTO.java` - 新增 `isSystem` 字段
3. `InspectionOrder.java` - 新增 `retentionRequired`、`retentionExpiryDate` 字段
4. `InspectionOrderDTO.java` - 新增留样相关字段
5. `InspectionOrderSaveDTO.java` - 新增留样相关字段
6. `InspectionOrderSaveVO.java` - 新增留样相关字段
7. `InspectionOrderRespVO.java` - 新增留样相关字段

### 12.3 错误码变更

**新增错误码：**（在 `LimsResponseCode.java` 中）
- `83_18_02` - RETENTION_EXPIRY_DATE_REQUIRED
- `83_18_03` - RETENTION_SAMPLING_REQUIRED
- `83_18_04` - RETENTION_INSPECT_ITEM_REQUIRED
- `83_18_05` - RETENTION_INSPECT_ITEM_NOT_CONFIGURED

### 12.4 查询逻辑变更

**检验项目查询过滤：**
- `InspectItemMapper.selectByParam` - 添加过滤条件：`AND (ii.is_system IS NULL OR ii.is_system = 0)`
- `InspectItemMapper.selectIdsByParam` - 添加相同过滤条件

**影响的接口：**
- 检验项目分页查询
- 检验项目列表查询
- 检验项目下拉查询

---

## 十三、前端开发检查清单

在开发留样功能时，请确保以下事项：

**UI组件：**
- [ ] 添加"是否留样"复选框
- [ ] 添加"有效期至"日期选择器（联动显示/隐藏）
- [ ] 日期格式设置为 `yyyy-MM-dd`

**自动化处理：**
- [ ] 勾选留样时，自动添加留样取样信息
- [ ] 取消留样时，自动删除留样取样信息
- [ ] 留样取样信息的检验项目名称固定显示为"留样"

**代码实现：**
- [ ] 使用固定ID `2000000000000000001` 而不是从接口查询
- [ ] 实现前端验证逻辑（有效期至必填、取样信息必填等）
- [ ] 处理4个新增错误码（83_18_02 ~ 83_18_05）
- [ ] 编辑时正确显示留样相关字段
- [ ] 确保不在检验项目下拉中显示留样选项

**页面显示：**
- [ ] 列表页可选添加留样相关列
- [ ] 详情页显示留样相关信息
- [ ] 留样取样信息添加特殊标识（建议）

**交互限制：**
- [ ] 限制留样取样信息的删除操作（建议）
- [ ] 检验项目下拉选择时过滤留样选项（建议）

---

## 十四、迁移脚本说明

**脚本文件：** `V1.1.1_0.0.37__add_retention_fields_to_inspection_order.sql`

**执行内容：**

1. 为 `lm_inspect_item` 表添加 `is_system` 字段
2. 为 `lm_inspection_order` 表添加留样相关字段
3. 插入留样检验项目数据（如果不存在）

**执行前提：**
- 数据库连接正常
- 有足够的权限执行ALTER TABLE和INSERT语句

**验证方法：**

```sql
-- 验证字段是否添加成功
DESCRIBE lm_inspect_item;
DESCRIBE lm_inspection_order;

-- 验证留样检验项目是否创建成功
SELECT * FROM lm_inspect_item WHERE code = 'SAMPLE_RETENTION';
```

**预期结果：**
- `lm_inspect_item` 表应有 `is_system` 字段
- `lm_inspection_order` 表应有 `retention_required` 和 `retention_expiry_date` 字段
- 应查询到一条留样检验项目记录，`is_system = 1`

---

## 联系方式

如有疑问，请联系后端开发团队。
