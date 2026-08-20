# 留样功能 - 系统内置检验项目机制补充说明

## 问题
留样检验项目不应该在前端的检验项目列表、下拉等地方显示。

## 解决方案

### 1. 数据库层面
给 `lm_inspect_item` 表添加 `is_system` 字段，用于标识系统内置项目。

```sql
ALTER TABLE lm_inspect_item
    ADD COLUMN is_system TINYINT(1) DEFAULT 0
    COMMENT '是否为系统内置项目（0-否，1-是，系统内置项目不在前端列表显示）';
```

### 2. 实体层面
- `InspectItem` 实体类添加 `isSystem` 字段
- `InspectItemDTO` 添加 `isSystem` 字段

### 3. 查询层面
在 `InspectItemMapper.xml` 的查询中添加过滤条件：

```xml
AND (ii.is_system IS NULL OR ii.is_system = 0)
```

影响的查询：
- `selectByParam` - 分页查询
- `selectIdsByParam` - ID列表查询

**不影响的查询：**
- `selectDetailsByIds` - 根据ID查询详情（允许查询系统内置项目）

### 4. 数据初始化
在迁移脚本中创建留样检验项目时，设置 `is_system = 1`：

```sql
INSERT INTO lm_inspect_item (..., is_system, ...)
VALUES (2000000000000000001, ..., 1, ...);
```

## 前端影响

### 自动过滤机制
- 前端调用检验项目列表接口时，后端自动过滤系统内置项目
- 前端无需修改现有代码，列表中不会显示留样检验项目
- 前端无需关心过滤逻辑

### 使用方式
前端使用留样检验项目时，**不应从列表中查询**，而应使用固定ID：

```javascript
const RETENTION_INSPECT_ITEM = {
  id: 2000000000000000001,
  code: 'SAMPLE_RETENTION',
  name: '留样'
};
```

### 推荐实现
```javascript
// 勾选留样时，自动添加留样取样信息
function onRetentionRequiredChange(checked) {
  if (checked) {
    formData.samplingList.push({
      inspectItemId: 2000000000000000001,  // 使用固定ID
      inspectItemName: '留样',              // 固定名称
      plannedQuantity: '',
      unitId: null,
      sampleCount: 1
    });
  } else {
    // 取消留样时，移除留样检验项目
    formData.samplingList = formData.samplingList.filter(
      item => item.inspectItemId !== 2000000000000000001
    );
  }
}
```

## 修改文件清单

### 数据库
1. `V1.1.1_0.0.37__add_retention_fields_to_inspection_order.sql` - 迁移脚本

### 实体类
2. `InspectItem.java` - 添加 isSystem 字段
3. `InspectItemDTO.java` - 添加 isSystem 字段

### Mapper
4. `InspectItemMapper.xml` - 添加查询过滤条件

### 文档
5. `RETENTION_FEATURE_FRONTEND_API_CHANGES.md` - 更新前端API对接文档

## 验证方法

### 后端验证
```sql
-- 查询留样检验项目
SELECT * FROM lm_inspect_item WHERE code = 'SAMPLE_RETENTION';
-- 应该返回一条记录，is_system = 1

-- 模拟前端查询（应过滤掉留样检验项目）
SELECT * FROM lm_inspect_item
WHERE is_deleted = 0
  AND (is_system IS NULL OR is_system = 0);
-- 结果中不应包含留样检验项目
```

### 前端验证
1. 打开检验项目列表页面，确认列表中没有"留样"项目
2. 打开检验项目下拉选择，确认下拉中没有"留样"选项
3. 在请验单页面勾选"是否留样"，确认自动添加了留样取样信息
4. 取样信息表格中应显示"留样"项目（由前端硬编码显示）

## 总结

通过 `is_system` 字段机制，实现了系统内置检验项目的自动过滤：
- ✅ 后端自动过滤，前端无需关心
- ✅ 不影响后端根据ID查询详情的功能
- ✅ 前端使用固定ID，不依赖列表查询
- ✅ 保持了API的向后兼容性

## 日期
2026-02-06
