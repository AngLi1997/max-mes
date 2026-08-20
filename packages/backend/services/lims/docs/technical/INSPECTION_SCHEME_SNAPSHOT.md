# 检验方案快照功能设计文档

## 📋 **概述**

您的观点非常正确！在请验阶段确实应该保存检验方案的快照数据。这个设计解决了检验过程中方案变更带来的数据一致性问题，确保一次检验全程基于同一套标准执行。

## 🎯 **为什么需要快照？**

### **1. 业务连续性保障**
```
请验阶段        样品登记        样品接收        样品领取        检验完成
    ↓              ↓              ↓              ↓              ↓
选择方案V1.0 ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━→ 基于V1.0执行

如果没有快照：
选择方案V1.0 → 方案升级到V1.1 → 样品登记基于V1.1 → 数据不一致 ❌

有了快照：
选择方案V1.0 → 保存V1.0快照 → 后续流程基于快照 → 数据一致 ✅
```

### **2. 监管合规要求**
- **审计追溯**：能够证明检验全程使用同一套标准
- **数据完整性**：快照数据是重要的合规证据
- **版本控制**：明确记录每次检验基于哪个版本的方案

### **3. 数据依赖关系**
- **样品登记**：需要知道具体的检验项目列表
- **样品接收**：需要基于分析项配置进行验收
- **数据录入**：需要基于数据点配置收集信息
- **报告生成**：需要基于快照的配置生成报告

## 🗃️ **数据库设计**

### **表结构层次关系**
```
lm_inspection_order (检验单)
    ↓
lm_inspection_order_item (检验项目快照)
    ↓  
lm_inspection_order_parameter (分析项快照)
    ↓
lm_inspection_order_data_point (数据点快照)
```

### **1. 检验单检验项目快照表 (`lm_inspection_order_item`)**
```sql
-- 保存请验时选择的检验项目配置
CREATE TABLE lm_inspection_order_item (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    original_detail_id BIGINT,                -- 原始方案明细ID（追溯用）
    material_id BIGINT NOT NULL,              -- 检品ID
    material_code VARCHAR(100),               -- 检品编码（快照）
    material_name VARCHAR(200),               -- 检品名称（快照）
    package_id BIGINT NOT NULL,               -- 实验包ID
    package_name VARCHAR(200),                -- 实验包名称（快照）
    sampling_amount DECIMAL(10,2),            -- 取样量
    sampling_unit VARCHAR(20),                -- 取样单位
    sampling_unit_name VARCHAR(50),           -- 取样单位名称（快照）
    sort INT DEFAULT 0                        -- 排序
);
```

### **2. 检验单分析项快照表 (`lm_inspection_order_parameter`)**
```sql
-- 保存每个检验项目下的分析项配置
CREATE TABLE lm_inspection_order_parameter (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    order_item_id BIGINT NOT NULL,            -- 检验项目ID
    original_parameter_id BIGINT,            -- 原始分析项配置ID（追溯用）
    parameter_id BIGINT NOT NULL,             -- 分析项ID
    parameter_code VARCHAR(100),              -- 分析项编码（快照）
    parameter_name VARCHAR(200),              -- 分析项名称（快照）
    parameter_type VARCHAR(50),               -- 分析项类型（快照）
    test_method VARCHAR(500),                 -- 检验方法（快照）
    standard_rule TEXT,                       -- 标准规定
    is_reportable TINYINT(1),                 -- 是否报告项
    is_executable TINYINT(1),                 -- 是否可执行
    sort INT DEFAULT 0                        -- 排序
);
```

### **3. 检验单数据点快照表 (`lm_inspection_order_data_point`)**
```sql
-- 保存每个分析项下的数据点配置
CREATE TABLE lm_inspection_order_data_point (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    order_parameter_id BIGINT NOT NULL,      -- 分析项ID
    original_data_point_id BIGINT,            -- 原始数据点配置ID（追溯用）
    point_code VARCHAR(100),                  -- 数据点编码（快照）
    point_name VARCHAR(200) NOT NULL,         -- 数据点名称
    point_type VARCHAR(20) NOT NULL,          -- 数据点类型
    data_type VARCHAR(50),                    -- 数据类型（快照）
    unit VARCHAR(20),                         -- 单位  
    unit_name VARCHAR(50),                    -- 单位名称（快照）
    decimal_places INT,                       -- 小数位数
    min_value DECIMAL(20,6),                  -- 最小值
    max_value DECIMAL(20,6),                  -- 最大值
    default_value VARCHAR(500),               -- 默认值
    trend_line_config TEXT,                   -- 趋势线配置(JSON)
    options TEXT,                             -- 选项配置(JSON)
    is_required TINYINT(1),                   -- 是否必填
    is_report_display TINYINT(1),             -- 是否报告显示
    sort INT DEFAULT 0                        -- 排序
);
```

## 🏗️ **架构设计**

### **1. 实体类设计**
```java
// 检验项目快照实体
@Entity
@TableName("lm_inspection_order_item")
public class InspectionOrderItem extends BaseDO {
    private Long inspectionOrderId;
    private Long originalDetailId;
    private Long materialId;
    private String materialCode;
    private String materialName;
    // ... 其他字段
}

// 分析项快照实体  
@Entity
@TableName("lm_inspection_order_parameter")
public class InspectionOrderParameter extends BaseDO {
    private Long inspectionOrderId;
    private Long orderItemId;
    private Long originalParameterId;
    private Long parameterId;
    private String parameterCode;
    // ... 其他字段
}

// 数据点快照实体
@Entity
@TableName("lm_inspection_order_data_point")  
public class InspectionOrderDataPoint extends BaseDO {
    private Long inspectionOrderId;
    private Long orderAnalyzeItemId;
    private Long originalDataPointId;
    private String pointCode;
    private String pointName;
    // ... 其他字段
}
```

### **2. Mapper接口设计**
```java
@Mapper
public interface InspectionOrderItemMapper extends BaseMapperX<InspectionOrderItem> {
    List<InspectionOrderItem> selectByInspectionOrderId(Long inspectionOrderId);
    int deleteByInspectionOrderId(Long inspectionOrderId);
    int batchInsert(List<InspectionOrderItem> items);
}

@Mapper  
public interface InspectionOrderAnalyzeItemMapper extends BaseMapperX<InspectionOrderAnalyzeItem> {
    List<InspectionOrderAnalyzeItem> selectByInspectionOrderId(Long inspectionOrderId);
    List<InspectionOrderAnalyzeItem> selectByOrderItemId(Long orderItemId);
    int batchInsert(List<InspectionOrderAnalyzeItem> inspectionParameters);
}

@Mapper
public interface InspectionOrderDataPointMapper extends BaseMapperX<InspectionOrderDataPoint> {
    List<InspectionOrderDataPoint> selectByInspectionOrderId(Long inspectionOrderId);
    List<InspectionOrderDataPoint> selectByOrderAnalyzeItemId(Long orderAnalyzeItemId);
    int batchInsert(List<InspectionOrderDataPoint> dataPoints);
}
```

## ⚙️ **实现逻辑**

### **1. 快照保存流程**
```java
@Transactional(rollbackFor = Exception.class)
public Long saveInspectionOrder(InspectionOrderSaveDTO saveDTO) {
    // ... 其他保存逻辑
    
    // 保存检验方案快照数据（新增功能）
    saveInspectionSchemeSnapshot(inspectionOrder.getId(), 
                                saveDTO.getSchemeId(), 
                                saveDTO.getSchemeVersionId());
    
    // ... 确认编号等后续逻辑
}

private void saveInspectionSchemeSnapshot(Long inspectionOrderId, 
                                        Long schemeId, 
                                        Long schemeVersionId) {
    // 1. 根据方案版本ID查询检验项目配置
    List<InspectionSchemeDetail> schemeDetails = 
        inspectionSchemeService.getDetailsByVersionId(schemeVersionId);
    
    // 2. 转换并保存检验项目快照
    List<InspectionOrderItem> snapshotItems = 
        convertToSnapshotItems(inspectionOrderId, schemeDetails);
    inspectionOrderItemMapper.batchInsert(snapshotItems);
    
    // 3. 保存分析项快照
    for (InspectionOrderItem item : snapshotItems) {
        List<InspectionSchemeAnalyzeItem> inspectionParameters = 
            inspectionSchemeService.getAnalyzeItemsByDetailId(item.getOriginalDetailId());
        List<InspectionOrderAnalyzeItem> snapshotAnalyzeItems = 
            convertToSnapshotAnalyzeItems(inspectionOrderId, item.getId(), inspectionParameters);
        inspectionOrderAnalyzeItemMapper.batchInsert(snapshotAnalyzeItems);
        
        // 4. 保存数据点快照
        for (InspectionOrderAnalyzeItem analyzeItem : snapshotAnalyzeItems) {
            List<InspectionSchemeDataPoint> dataPoints = 
                inspectionSchemeService.getDataPointsByAnalyzeItemId(analyzeItem.getOriginalAnalyzeItemId());
            List<InspectionOrderDataPoint> snapshotDataPoints = 
                convertToSnapshotDataPoints(inspectionOrderId, analyzeItem.getId(), dataPoints);
            inspectionOrderDataPointMapper.batchInsert(snapshotDataPoints);
        }
    }
}
```

### **2. 快照查询使用**
```java
// 后续功能使用快照数据而不是实时方案数据
public List<InspectionItemDTO> getInspectionItemsByOrderId(Long inspectionOrderId) {
    // 使用快照数据
    List<InspectionOrderItem> snapshotItems = 
        inspectionOrderItemMapper.selectByInspectionOrderId(inspectionOrderId);
    return convertToInspectionItemDTOs(snapshotItems);
}

public List<AnalyzeItemDTO> getAnalyzeItemsByOrderId(Long inspectionOrderId) {
    // 使用快照数据
    List<InspectionOrderAnalyzeItem> snapshotAnalyzeItems = 
        inspectionOrderAnalyzeItemMapper.selectByInspectionOrderId(inspectionOrderId);
    return convertToAnalyzeItemDTOs(snapshotAnalyzeItems);
}
```

## 📊 **使用场景**

### **1. 样品登记阶段**
```java
// 获取本次检验需要登记的检验项目（基于快照）
List<InspectionOrderItem> inspectionItems = 
    inspectionOrderItemMapper.selectByInspectionOrderId(inspectionOrderId);

// 为每个检验项目创建样品登记记录
for (InspectionOrderItem item : inspectionItems) {
    SampleRegistration registration = new SampleRegistration();
    registration.setInspectionOrderId(inspectionOrderId);
    registration.setOrderItemId(item.getId());
    registration.setMaterialId(item.getMaterialId());
    registration.setMaterialName(item.getMaterialName()); // 使用快照数据
    // ... 其他字段
}
```

### **2. 样品接收阶段**
```java
// 获取本次检验的分析项配置（基于快照）
List<InspectionOrderAnalyzeItem> inspectionParameters = 
    inspectionOrderAnalyzeItemMapper.selectByInspectionOrderId(inspectionOrderId);

// 验证样品是否满足分析项要求
for (InspectionOrderAnalyzeItem analyzeItem : inspectionParameters) {
    validateSampleForAnalyzeItem(sample, analyzeItem);
}
```

### **3. 数据录入阶段**
```java
// 获取数据录入界面需要的数据点配置（基于快照）
List<InspectionOrderDataPoint> dataPoints = 
    inspectionOrderDataPointMapper.selectByOrderAnalyzeItemId(analyzeItemId);

// 根据快照配置生成数据录入界面
for (InspectionOrderDataPoint dataPoint : dataPoints) {
    FormField field = new FormField();
    field.setPointName(dataPoint.getPointName());
    field.setPointType(dataPoint.getPointType());
    field.setRequired(dataPoint.getIsRequired());
    field.setUnit(dataPoint.getUnitName()); // 使用快照的单位名称
    // ... 其他配置
}
```

## 🎉 **优势特点**

### **1. 数据一致性保障**
- ✅ **版本锁定**：一次检验全程基于同一个方案版本
- ✅ **配置稳定**：不受后续方案变更影响
- ✅ **追溯完整**：每个快照都记录原始配置ID

### **2. 业务流程优化**
- ✅ **查询高效**：直接查询快照表，无需关联多张方案表
- ✅ **逻辑简化**：后续功能只需关注快照数据
- ✅ **性能提升**：避免复杂的多表关联查询

### **3. 合规审计支持**
- ✅ **审计友好**：能够完整还原检验执行时的标准
- ✅ **证据完整**：快照数据是重要的合规证据
- ✅ **版本管理**：明确记录每次检验的方案版本

### **4. 系统可维护性**
- ✅ **职责清晰**：快照管理与方案管理分离
- ✅ **扩展容易**：可以根据需要增加快照字段
- ✅ **测试友好**：快照数据便于单元测试

## ⚠️ **注意事项**

### **1. 存储空间**
- 快照会增加存储空间占用
- 建议定期清理已完成检验的历史快照数据

### **2. 数据同步**
- 快照数据是静态的，不会自动更新
- 如需更新，必须重新生成快照

### **3. 实现依赖**
- 需要完善的检验方案查询服务
- 需要实现快照数据转换逻辑

## 📋 **TODO: 完善实现**

当前实现是框架代码，需要根据实际的检验方案服务完善：

1. **补充检验方案查询服务**
   - `inspectionSchemeService.getDetailsByVersionId()`
   - `inspectionSchemeService.getAnalyzeItemsByDetailId()`
   - `inspectionSchemeService.getDataPointsByAnalyzeItemId()`

2. **实现数据转换方法**
   - `convertToSnapshotItems()`
   - `convertToSnapshotAnalyzeItems()`
   - `convertToSnapshotDataPoints()`

3. **添加Mapper XML实现**
   - 批量插入的SQL实现
   - 复杂查询的SQL实现

4. **完善VO层支持**
   - 添加快照相关的VO对象
   - 更新前端接口

## ✅ **结论**

您的快照建议非常正确且重要！这个设计完美解决了检验过程中的数据一致性问题，是LIMS系统设计中的最佳实践。快照机制确保了：

- 🎯 **业务连续性** - 检验全程基于同一标准
- 🛡️ **数据完整性** - 不受方案变更影响  
- 📋 **合规要求** - 满足审计追溯需求
- ⚡ **性能优化** - 提供高效的数据查询

这个功能为后续的样品登记、样品接收、样品领取等流程提供了稳定可靠的数据基础！🚀