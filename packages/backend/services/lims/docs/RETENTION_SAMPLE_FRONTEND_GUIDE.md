# 留样功能前端对接快速指南

## 快速开始

本文档提供留样功能的快速对接指南，详细接口文档请参考：[RETENTION_SAMPLE_API_DOCUMENTATION.md](./RETENTION_SAMPLE_API_DOCUMENTATION.md)

---

## 1. 功能概述

留样功能包含3个主要模块，共8个接口：

| 模块 | 接口数量 | 核心功能 |
|------|---------|---------|
| 留样样品接收 | 2个 | 查询待接收列表、批量接收（自动生成观察任务） |
| 留样样品管理 | 4个 | 列表查询、延期、领用、操作历史 |
| 留样观察 | 2个 | 观察任务列表、提交观察结果 |

### 1.1 业务背景

**留样期限的来源**：

留样样品的期限来源于检验单（请验单），具体流程如下：

```
1. 创建留样检验单时
   └─ 填写"有效期至"字段（retention_expiry_date）
   └─ 例如：2028-06-01

2. 取样确认时（样品生成）
   └─ 系统自动计算样品留样期限 = 检验单有效期至 + 1年
   └─ 例如：样品留样期限 = 2029-06-01

3. 样品接收时
   └─ 必须填写储存位置
   └─ 自动生成年度观察任务
```

**重要提示**：
- 检验单的"有效期至"字段用于留样检验单
- 样品的留样期限在生成时自动计算，无需前端输入
- 样品接收后才会生成观察任务

---

## 2. 接口清单

### 2.1 留样样品接收

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询接收列表 | POST | `/api/app/lims2/sample-receive/retention/page` | 分页查询待接收样品 |
| 批量接收 | POST | `/api/app/lims2/sample-receive/retention/batch-receive` | 接收并指定储存位置 ⭐ |

### 2.2 留样样品管理

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询管理列表 | POST | `/api/app/lims2/retention-sample-manage/page` | 分页查询已接收样品 |
| 延期 | POST | `/api/app/lims2/retention-sample-manage/{sampleId}/extend` | 延长留样期限 |
| 领用 | POST | `/api/app/lims2/retention-sample-manage/{sampleId}/collect` | 领用样品 ⭐ |
| 操作历史 | GET | `/api/app/lims2/retention-sample-manage/{sampleId}/history` | 查询操作记录 |

### 2.3 留样观察

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 查询任务列表 | POST | `/api/app/lims2/retention-observation/task/page` | 分页查询观察任务 |
| 提交观察结果 | POST | `/api/app/lims2/retention-observation/task/{taskId}/submit` | 提交观察 ⭐ |

⭐ 标记的接口包含重要业务逻辑

---

## 3. 核心接口示例

### 3.1 接收留样样品

**重要**：接收时必须指定储存位置，接收后会自动生成观察任务

```javascript
// POST /api/app/lims2/sample-receive/retention/batch-receive
{
  "sampleIds": [8888881, 8888882],
  "storageLocation": "Cold Storage Room A-Shelf 05"  // 必填
}

// 响应
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**业务逻辑**：
- 更新样品状态为已接收
- 保存储存位置
- **自动生成观察任务**（根据留样期限按年度生成）

### 3.2 查询留样样品列表

```javascript
// POST /api/app/lims2/retention-sample-manage/page
{
  "pageNum": 1,
  "pageSize": 10,
  "status": "received"  // 可选：received, to_be_destroyed, destroyed
}

// 响应（关键字段）
{
  "code": 0,
  "data": {
    "total": 2,
    "list": [
      {
        "id": "8888881",
        "sampleNo": "RET-SAMPLE-001",
        "quantity": "500",           // 初始数量
        "currentQuantity": "450",    // 当前数量 ⭐
        "retentionExpiryDate": "2029-06-01",
        "storageLocation": "Cold Storage Room B-Shelf 05",
        "toBeDestroyed": false       // 是否待销毁 ⭐
      }
    ]
  }
}
```

**重要字段**：
- `currentQuantity`: 当前可用数量（领用后减少）
- `toBeDestroyed`: 是否待销毁（需要高亮显示）

### 3.3 领用留样样品

```javascript
// POST /api/app/lims2/retention-sample-manage/{sampleId}/collect
{
  "collectQuantity": "50",
  "unitId": 1,
  "collectReason": "质量复检",
  "collectorId": "collector001",
  "collectorName": "王五"
}

// 响应
{
  "code": 0,
  "message": "success",
  "data": null
}
```

**业务逻辑**：
- 验证领用数量 ≤ 当前数量
- 更新 `currentQuantity` = 原值 - 领用数量
- 生成领用台账
- 记录操作历史

### 3.4 查询观察任务

**临期任务查询**（一周内到期）：

```javascript
// POST /api/app/lims2/retention-observation/task/page
{
  "pageNum": 1,
  "pageSize": 10,
  "queryType": "upcoming"  // upcoming=临期任务, all=全部任务
}
```

**排序规则**：
- 临期任务（upcoming）：按到期日期**正序**（越近越靠前）
- 全部任务（all）：按到期日期**倒序**（越晚越靠前）

### 3.5 提交观察结果

**重要**：必须按时间顺序提交，不能跳过早期任务

```javascript
// POST /api/app/lims2/retention-observation/task/{taskId}/submit
{
  "observationResult": "符合",  // 必填
  "observationRemark": "观察正常，无异常情况"
}

// 成功响应
{
  "code": 0,
  "message": "success"
}

// 顺序错误响应
{
  "code": 81000001,
  "message": "存在更早日期的留样观察任务未完成，请按照顺序完成"
}
```

**业务规则**：
1. 任务必须按时间顺序完成
2. 自动记录观察人和时间
3. 生成观察台账
4. 更新任务状态为已完成

---

## 4. 前端开发重点

### 4.1 必须处理的业务逻辑

#### ✅ 1. 储存位置必填
接收留样样品时，`storageLocation` 为必填项，建议：
- 提供常用储存位置下拉选项
- 支持手动输入

#### ✅ 2. 领用数量验证
领用时需要验证：
```javascript
if (collectQuantity > currentQuantity) {
  alert('领用数量不能大于当前样品数量：' + currentQuantity);
}
```

#### ✅ 3. 观察任务顺序校验
- 前端禁用未到期或跳过的任务
- 只允许提交最早的未完成任务
- 遇到错误码81000001时，提示用户按顺序完成

#### ✅ 4. 待销毁样品高亮
当 `toBeDestroyed=true` 时：
```javascript
// 建议样式
<div className={item.toBeDestroyed ? 'highlight-warning' : ''}>
  {item.toBeDestroyed && <Tag color="orange">待销毁</Tag>}
</div>
```

### 4.2 数据展示重点

#### quantity vs currentQuantity

| 字段 | 说明 | 使用场景 |
|------|------|----------|
| quantity | 初始数量，不变 | 仅展示参考 |
| currentQuantity | 当前可用数量 | **用于业务操作** ⭐ |

在列表和详情页，应该优先展示 `currentQuantity`。

#### 状态筛选

留样样品管理列表支持3种状态筛选：

| 状态值 | 说明 | 建议展示 |
|--------|------|----------|
| received | 已接收（正常） | 默认选项 |
| to_be_destroyed | 待销毁（留样期限已过） | 橙色标签 |
| destroyed | 已销毁 | 灰色标签 |

#### 临期任务提醒

建议在首页或导航栏展示临期任务数量：
```javascript
// 查询临期任务
const { total } = await fetchUpcomingTasks({
  pageNum: 1,
  pageSize: 1,
  queryType: 'upcoming'
});

// 显示Badge
<Badge count={total}>留样观察</Badge>
```

### 4.3 错误处理

#### 常见错误码

| 错误码 | 说明 | 前端处理 |
|--------|------|----------|
| 81000001 | 业务逻辑错误 | 显示message内容 |
| 81000002 | 数据不存在 | 提示样品不存在 |
| 507 | 参数验证失败 | 显示字段验证错误 |

#### 顺序校验错误处理

```javascript
try {
  await submitObservation(taskId, data);
  message.success('观察结果提交成功');
} catch (error) {
  if (error.code === 81000001 && error.message.includes('更早日期')) {
    message.error('存在更早日期的观察任务未完成，请按顺序完成');
    // 可选：跳转到最早的未完成任务
  }
}
```

---

## 5. 页面设计建议

### 5.1 留样样品接收页面

**页面元素**：
- [ ] 待接收样品列表（分页）
- [ ] 样品编号筛选
- [ ] 批量选择功能
- [ ] 储存位置输入框（必填） ⭐
- [ ] 批量接收按钮

**交互流程**：
1. 勾选待接收样品
2. 输入储存位置
3. 点击"批量接收"
4. 提示：接收成功，已自动生成N个观察任务

### 5.2 留样样品管理页面

**页面元素**：
- [ ] 已接收样品列表（分页）
- [ ] 状态筛选（received/to_be_destroyed/destroyed）
- [ ] 样品编号、批号筛选
- [ ] 待销毁样品高亮显示 ⭐
- [ ] 操作按钮：延期、领用、查看历史

**列表字段**：
- 样品编号
- 批号
- 物料名称
- 当前数量 / 初始数量 ⭐
- 留样期限
- 储存位置
- 状态标签

**延期弹窗**：
- [ ] 原留样期限（只读）
- [ ] 新留样期限（日期选择，最小日期=原期限+1天）
- [ ] 确认按钮
- [ ] 提示信息：延期后如果年数增加，系统会自动生成新的观察任务 ⭐

**领用弹窗**：
- [ ] 当前可用数量（只读，显著展示） ⭐
- [ ] 领用数量（必填，验证≤当前数量）
- [ ] 单位选择
- [ ] 领用原因（可选）
- [ ] 领样人选择（必填）
- [ ] 确认按钮

### 5.3 留样观察页面

**页面元素**：
- [ ] 观察任务列表（分页）
- [ ] 查询类型切换（临期任务/全部任务） ⭐
- [ ] 样品编号筛选
- [ ] 任务状态标签（未完成/已完成）
- [ ] 提交观察按钮（只对未完成任务显示）

**列表字段**：
- 样品编号
- 批号
- 物料名称
- 观察年度（第N年）
- 观察到期时间
- 储存位置
- 状态

**观察提交弹窗**：
- [ ] 样品信息展示（只读）
- [ ] 观察结果（必填，下拉选择：符合/不符合） ⭐
- [ ] 观察备注（可选，文本域）
- [ ] 观察人（自动填充当前用户，只读）
- [ ] 确认按钮

**临期任务提示**：
```
⚠️ 提示：该任务将在3天后到期，请及时完成观察
```

---

## 6. 测试检查清单

### 6.1 留样样品接收

- [ ] 能够查询待接收列表
- [ ] 能够勾选多个样品
- [ ] 储存位置为空时不能提交
- [ ] 接收成功后列表刷新
- [ ] 接收成功后提示自动生成了N个观察任务

### 6.2 留样样品管理

- [ ] 列表能够按状态筛选
- [ ] 待销毁样品有明显标记
- [ ] 延期时新期限必须大于原期限
- [ ] 延期后如果年数增加，自动生成新的观察任务 ⭐
- [ ] 领用时验证数量不超过当前数量
- [ ] 领用后当前数量正确减少
- [ ] 能够查看操作历史

### 6.3 留样观察

- [ ] 临期任务查询正确（一周内到期）
- [ ] 临期任务按到期日期正序排序
- [ ] 全部任务按到期日期倒序排序
- [ ] 不能跳过早期任务提交
- [ ] 提交成功后任务状态变为已完成
- [ ] 已完成任务不在列表中显示

---

## 7. API调用示例代码

### 7.1 React + Axios 示例

```javascript
import axios from 'axios';

const API_BASE = '/api/app/lims2';

// 1. 接收留样样品
export const receiveRetentionSamples = async (sampleIds, storageLocation) => {
  return axios.post(`${API_BASE}/sample-receive/retention/batch-receive`, {
    sampleIds,
    storageLocation
  });
};

// 2. 查询留样样品列表
export const getRetentionSampleList = async (params) => {
  return axios.post(`${API_BASE}/retention-sample-manage/page`, params);
};

// 3. 领用样品
export const collectSample = async (sampleId, data) => {
  return axios.post(
    `${API_BASE}/retention-sample-manage/${sampleId}/collect`,
    data
  );
};

// 4. 查询观察任务
export const getObservationTasks = async (params) => {
  return axios.post(`${API_BASE}/retention-observation/task/page`, params);
};

// 5. 提交观察结果
export const submitObservation = async (taskId, data) => {
  return axios.post(
    `${API_BASE}/retention-observation/task/${taskId}/submit`,
    data
  );
};
```

### 7.2 使用示例

```javascript
// 接收样品
const handleReceive = async (selectedSamples, location) => {
  try {
    const sampleIds = selectedSamples.map(s => s.id);
    await receiveRetentionSamples(sampleIds, location);
    message.success('接收成功，已自动生成观察任务');
    refreshList();
  } catch (error) {
    message.error(error.message);
  }
};

// 领用样品
const handleCollect = async (sampleId, formData) => {
  try {
    await collectSample(sampleId, {
      collectQuantity: formData.quantity,
      unitId: formData.unitId,
      collectReason: formData.reason,
      collectorId: currentUser.id,
      collectorName: currentUser.name
    });
    message.success('领用成功');
    refreshList();
  } catch (error) {
    if (error.code === 81000001) {
      message.error('领用数量不能大于当前样品数量');
    }
  }
};

// 查询临期任务
const loadUpcomingTasks = async () => {
  const { data } = await getObservationTasks({
    pageNum: 1,
    pageSize: 10,
    queryType: 'upcoming'
  });
  setTaskList(data.list);
  setTotal(data.total);
};
```

---

## 8. 常见问题 FAQ

**Q1: 为什么接收样品时必须填写储存位置？**

A: 留样样品需要长期保存，储存位置是后续管理和观察的重要信息。

**Q2: 接收后会自动生成多少个观察任务？**

A: 根据留样期限自动计算。例如：
- 留样期限3年 → 生成3个观察任务（每年1个）
- 留样期限2年 → 生成2个观察任务

**Q3: 为什么观察任务不能跳过？**

A: 为了确保观察记录的连续性和完整性，必须按年度顺序完成。

**Q4: quantity 和 currentQuantity 有什么区别？**

A:
- `quantity`: 样品初始数量，永远不变
- `currentQuantity`: 当前可用数量，领用后会减少

建议在界面上显示：当前数量 450 / 初始数量 500

**Q5: 如何判断是否为临期任务？**

A: 使用 `queryType: "upcoming"` 查询，系统会自动返回一周内到期的任务。

**Q6: 样品的留样期限是如何确定的？**

A: 样品的留样期限来源于检验单（请验单）：
- 创建留样检验单时，填写"有效期至"字段（retention_expiry_date）
- 取样确认时，系统自动生成样品，样品的留样期限 = 检验单有效期至 + 1年
- 例如：检验单有效期至 = 2028-06-01，则样品留样期限 = 2029-06-01
- 前端无需手动输入样品的留样期限，这个字段会自动计算并填充

**Q7: 留样期限延期后，观察任务会自动生成吗？**

A: 会自动生成新的观察任务：
- 如果延期后的年数增加，系统会自动生成额外的年度观察任务
- 例如：原留样期限3年（已有3个观察任务），延期到5年，系统会自动生成第4年和第5年的观察任务
- 前端延期成功后，可以提示用户："延期成功，已自动生成N个新的观察任务"
- 已存在的观察任务不会被修改

---

## 9. 联系方式

如有疑问，请联系：
- **后端开发**: LIMS后端团队
- **文档维护**: yigaohui
- **最后更新**: 2026-02-10

详细接口文档：[RETENTION_SAMPLE_API_DOCUMENTATION.md](./RETENTION_SAMPLE_API_DOCUMENTATION.md)
