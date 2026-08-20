# 请验留样功能实现总结

## 功能概述

为请验功能增加留样支持,允许用户在请验时选择是否进行留样,并设置留样有效期。

## 需求说明

1. **选择留样**: 请验时允许选择是否进行留样
2. **有效期至**: 如果选择留样,则"有效期至"字段必填
3. **留样时长**: 留样时长为有效期至后一年
4. **留样取样**: 如果请验时选择了留样,则取样信息中必须包含留样的取样信息
5. **留样检验项目**: 留样为一个特殊的检验项目,在后端使用code="SAMPLE_RETENTION"表示

## 实现方案

### 1. 数据库变更

**表**: `lm_inspection_order`

新增字段:
- `retention_required` TINYINT(1): 是否需要留样 (0-否, 1-是), 默认值为0
- `retention_expiry_date` DATE: 有效期至 (留样时必填)

**表**: `lm_inspect_item`

插入留样检验项目数据:
- ID: 2000000000000000001 (固定雪花ID)
- code: "SAMPLE_RETENTION"
- name: "留样"
- is_system: 1 (标识为系统内置项目，不在前端列表显示)
- remark: "特殊检验项目:用于标识样品需要进行留样保存。此为系统内置项目，不在前端列表显示"

**系统内置项目机制**:

为了防止留样检验项目在前端列表中显示，引入了系统内置项目机制：

1. 给 `lm_inspect_item` 表添加 `is_system` 字段（TINYINT(1)，默认0）
2. 留样检验项目设置 `is_system = 1`
3. 在检验项目查询的 Mapper XML 中添加过滤条件：`AND (ii.is_system IS NULL OR ii.is_system = 0)`
4. 前端调用检验项目列表接口时，自动过滤掉系统内置项目

**迁移脚本**: `V1.1.1_0.0.37__add_retention_fields_to_inspection_order.sql`

### 3. 系统内置项目机制

为了确保留样检验项目不在前端列表中显示，引入了系统内置项目机制。

#### 3.1 数据库层

给 `lm_inspect_item` 表添加 `is_system` 字段：

```sql
ALTER TABLE lm_inspect_item
    ADD COLUMN is_system TINYINT(1) DEFAULT 0
    COMMENT '是否为系统内置项目（0-否，1-是，系统内置项目不在前端列表显示）';
```

#### 3.2 实体层

在 `InspectItem` 和 `InspectItemDTO` 中添加 `isSystem` 字段：

```java
/**
 * 是否为系统内置项目（0-否，1-是）
 * 系统内置项目不在前端列表中显示
 */
private Boolean isSystem;
```

#### 3.3 查询层

在 `InspectItemMapper.xml` 的查询中添加过滤条件，自动过滤系统内置项目：

```xml
<!-- selectByParam 查询 -->
<where>
    ...
    AND ii.is_deleted = 0
    AND (ii.is_system IS NULL OR ii.is_system = 0)
</where>

<!-- selectIdsByParam 查询 -->
<where>
    ...
    AND ii.is_deleted = 0
    AND (ii.is_system IS NULL OR ii.is_system = 0)
</where>
```

**影响的查询：**
- 检验项目分页查询（前端列表）
- 检验项目ID列表查询
- 检验项目下拉查询

**不影响的查询：**
- `selectDetailsByIds` - 根据ID查询详情（允许查询系统内置项目）

#### 3.4 前端影响

- 前端调用检验项目列表/下拉接口时，不会返回留样检验项目
- 前端无需修改现有代码，自动过滤
- 前端使用留样检验项目时，应使用固定ID（`2000000000000000001`），不应从列表中查询

### 4. 错误码定义

#### LimsResponseCode 新增错误码
`bmos-lims2-common/src/main/java/com/bmos/lims2/common/i18n/LimsResponseCode.java`

新增留样相关错误码:
```java
ResponseItem RETENTION_EXPIRY_DATE_REQUIRED = ResponseItem.from(83_18_02, "选择留样时，有效期至不能为空", "bmosLims");
ResponseItem RETENTION_SAMPLING_REQUIRED = ResponseItem.from(83_18_03, "选择留样时，必须添加留样的取样信息", "bmosLims");
ResponseItem RETENTION_INSPECT_ITEM_REQUIRED = ResponseItem.from(83_18_04, "选择留样时，取样信息中必须包含留样检验项目", "bmosLims");
ResponseItem RETENTION_INSPECT_ITEM_NOT_CONFIGURED = ResponseItem.from(83_18_05, "系统未配置留样检验项目，请联系管理员", "bmosLims");
```

### 5. 实体类修改

#### InspectionOrder 实体类
`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/entity/InspectionOrder.java`

新增字段:
```java
private Boolean retentionRequired;  // 是否需要留样
private LocalDate retentionExpiryDate;  // 有效期至
```

#### InspectionOrderDTO
`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/dto/InspectionOrderDTO.java`

新增字段:
```java
@ApiModelProperty("是否需要留样")
private Boolean retentionRequired;

@ApiModelProperty("有效期至(留样时必填)")
private LocalDate retentionExpiryDate;
```

#### InspectionOrderSaveDTO
`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/dto/InspectionOrderSaveDTO.java`

新增字段:
```java
@ApiModelProperty("是否需要留样")
private Boolean retentionRequired;

@ApiModelProperty("有效期至(留样时必填)")
private LocalDate retentionExpiryDate;
```

#### InspectionOrderSaveVO (Web层请求VO)
`bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/order/vo/req/InspectionOrderSaveVO.java`

新增字段:
```java
@ApiModelProperty("是否需要留样")
private Boolean retentionRequired;

@ApiModelProperty("有效期至(留样时必填)")
@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate retentionExpiryDate;
```

#### InspectionOrderRespVO (Web层响应VO)
`bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/order/vo/resp/InspectionOrderRespVO.java`

新增字段:
```java
@ApiModelProperty("是否需要留样")
private Boolean retentionRequired;

@ApiModelProperty("有效期至(留样时必填)")
private LocalDate retentionExpiryDate;
```

### 6. Mapper映射

由于InspectionOrderMapper.xml中使用了`io.*`查询所有字段,新增字段会自动被MyBatis映射,无需修改XML文件。

### 7. 业务逻辑实现

#### InspectionOrderServiceImpl
`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/service/impl/InspectionOrderServiceImpl.java`

**新增常量**:
```java
private static final String SAMPLE_RETENTION_CODE = "SAMPLE_RETENTION";
```

**新增依赖注入**:
```java
@Autowired
private InspectItemMapper inspectItemMapper;
```

**修改保存方法**:

在`saveInspectionOrder()`方法中:
1. 调用`validateRetention()`验证留样参数
2. 更新时支持更新留样字段

**新增验证方法**:

```java
private void validateRetention(InspectionOrderSaveDTO saveDTO) {
    // 如果不需要留样,则不验证
    if (saveDTO.getRetentionRequired() == null || !saveDTO.getRetentionRequired()) {
        return;
    }

    // 需要留样时,有效期至必填
    if (saveDTO.getRetentionExpiryDate() == null) {
        throw new BmosException(LimsResponseCode.RETENTION_EXPIRY_DATE_REQUIRED);
    }

    // 需要留样时,取样信息中必须包含留样检验项目
    if (CollUtil.isEmpty(saveDTO.getSamplingList())) {
        throw new BmosException(LimsResponseCode.RETENTION_SAMPLING_REQUIRED);
    }

    // 获取留样检验项目ID
    Long retentionItemId = getRetentionInspectItemId();
    if (retentionItemId == null) {
        throw new BmosException(LimsResponseCode.RETENTION_INSPECT_ITEM_NOT_CONFIGURED);
    }

    // 验证取样信息中是否包含留样检验项目
    boolean hasRetentionSampling = saveDTO.getSamplingList().stream()
            .anyMatch(sampling -> retentionItemId.equals(sampling.getInspectItemId()));

    if (!hasRetentionSampling) {
        throw new BmosException(LimsResponseCode.RETENTION_INSPECT_ITEM_REQUIRED);
    }
}
```

**新增辅助方法**:

```java
private Long getRetentionInspectItemId() {
    LambdaQueryWrapper<InspectItem> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(InspectItem::getCode, SAMPLE_RETENTION_CODE);
    InspectItem retentionItem = inspectItemMapper.selectOne(queryWrapper);
    return retentionItem != null ? retentionItem.getId() : null;
}
```

## 验证规则

### 保存时验证

1. **retentionRequired = true** 时:
   - `retentionExpiryDate` 必须有值,否则抛出错误码: `83_18_02` (RETENTION_EXPIRY_DATE_REQUIRED)
   - `samplingList` 不能为空,否则抛出错误码: `83_18_03` (RETENTION_SAMPLING_REQUIRED)
   - `samplingList` 中必须包含至少一条 `inspectItemId` 等于留样检验项目ID的记录,否则抛出错误码: `83_18_04` (RETENTION_INSPECT_ITEM_REQUIRED)
   - 系统必须已配置留样检验项目(code=SAMPLE_RETENTION),否则抛出错误码: `83_18_05` (RETENTION_INSPECT_ITEM_NOT_CONFIGURED)

2. **retentionRequired = false** 或 **null** 时:
   - 不进行任何验证

### 错误码说明

| 错误码 | 常量名 | 错误消息 | 触发条件 |
|--------|--------|----------|----------|
| 83_18_02 | RETENTION_EXPIRY_DATE_REQUIRED | 选择留样时，有效期至不能为空 | 选择留样但未填写有效期至 |
| 83_18_03 | RETENTION_SAMPLING_REQUIRED | 选择留样时，必须添加留样的取样信息 | 选择留样但取样信息为空 |
| 83_18_04 | RETENTION_INSPECT_ITEM_REQUIRED | 选择留样时，取样信息中必须包含留样检验项目 | 选择留样但取样信息中不包含留样检验项目 |
| 83_18_05 | RETENTION_INSPECT_ITEM_NOT_CONFIGURED | 系统未配置留样检验项目，请联系管理员 | 系统中不存在code为SAMPLE_RETENTION的检验项目 |

### 确认请验单时

当请验单确认时,如果包含留样检验项目的取样信息,会自动生成留样样品。留样样品的生命周期由后续的样品管理功能控制。

## 前端对接说明

### 请求参数 (InspectionOrderSaveVO)

```json
{
  "materialId": 123,
  "schemeVersionId": 456,
  "retentionRequired": true,  // 是否留样
  "retentionExpiryDate": "2026-12-31",  // 有效期至（日期格式）
  "samplingList": [
    {
      "inspectItemId": 2000000000000000001,  // 留样检验项目ID
      "plannedQuantity": "10",
      "unitId": 1,
      "sampleCount": 3
    }
  ]
}
```

### 响应数据 (InspectionOrderRespVO)

```json
{
  "id": 789,
  "orderNo": "QY202602060001",
  "retentionRequired": true,
  "retentionExpiryDate": "2026-12-31",
  "samplingList": [...]
}
```

## 修改文件清单

### 错误码定义
1. `bmos-lims2-common/src/main/java/com/bmos/lims2/common/i18n/LimsResponseCode.java`

### 检验项目相关（系统内置项目机制）
2. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/item/entity/InspectItem.java`
3. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/item/dto/InspectItemDTO.java`
4. `bmos-lims2-server/src/main/resources/mapper/inspection/item/InspectItemMapper.xml`

### 请验单相关
5. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/entity/InspectionOrder.java`
6. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/dto/InspectionOrderDTO.java`
7. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/dto/InspectionOrderSaveDTO.java`
8. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/order/vo/req/InspectionOrderSaveVO.java`
9. `bmos-lims2-web/src/main/java/com/bmos/lims2/web/inspect/order/vo/resp/InspectionOrderRespVO.java`
10. `bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/service/impl/InspectionOrderServiceImpl.java`

### 数据库脚本
11. `bmos-lims2-web/src/main/resources/init/db/V1.1.1_0.0.37__add_retention_fields_to_inspection_order.sql`

### 文档
12. `RETENTION_FEATURE_FRONTEND_API_CHANGES.md` - 前端API对接文档
13. `RETENTION_SYSTEM_ITEM_MECHANISM.md` - 系统内置项目机制说明

## 编译验证

项目已通过编译验证,无语法错误。

## 注意事项

1. **留样时长计算**: 留样时长为"有效期至+1年",这个逻辑应在样品管理模块中实现,本次修改仅存储"有效期至"字段。

2. **留样检验项目**: 系统需要预先配置code为"SAMPLE_RETENTION"的检验项目,迁移脚本会自动创建。

3. **数据迁移**: 执行迁移脚本时会为现有检验单添加默认值 `retention_required=0`。

4. **前端展示**: 前端需要:
   - 添加"是否留样"复选框
   - 添加"有效期至"日期选择器(当选择留样时显示并必填, 格式: yyyy-MM-dd)
   - 在取样信息中允许选择留样检验项目

5. **API兼容性**: 新增字段对现有API完全向后兼容,不传递留样字段时系统会使用默认值。

## 测试建议

1. **正向测试**:
   - 创建请验单,选择留样,填写有效期至,添加留样取样信息 ✓
   - 创建请验单,不选择留样 ✓
   - 编辑已有请验单,修改留样信息 ✓

2. **异常测试**:
   - 选择留样但不填写有效期至 → 应报错
   - 选择留样但取样信息中不包含留样检验项目 → 应报错
   - 选择留样但取样信息为空 → 应报错

3. **集成测试**:
   - 确认包含留样的请验单,验证样品是否正确生成
   - 查询请验单详情,验证留样字段是否正确返回

## 后续工作

1. 样品管理模块需要实现留样样品的特殊处理逻辑
2. 留样时长(有效期至+1年)的计算和管理
3. 留样样品的过期提醒功能
4. 留样样品的处理和销毁流程

## 版本历史

### v1.1 - 2026-02-06
- **变更**: 将有效期至字段类型从 `LocalDateTime` 改为 `LocalDate`
- **影响范围**:
  - InspectionOrder.java - 字段类型和JsonFormat注解
  - InspectionOrderDTO.java - 字段类型
  - InspectionOrderSaveDTO.java - 字段类型
  - InspectionOrderSaveVO.java - 字段类型和JsonFormat注解
  - InspectionOrderRespVO.java - 字段类型
  - 数据库迁移脚本 - 字段类型从 DATETIME 改为 DATE
  - 前端API文档 - 日期格式从 "yyyy-MM-dd HH:mm:ss" 改为 "yyyy-MM-dd"
- **原因**: 有效期至只需要日期部分,不需要时间部分

### v1.0 - 2026-02-06
- 初始实现留样功能
- 新增留样相关字段
- 实现系统内置项目机制
- 添加留样验证逻辑
