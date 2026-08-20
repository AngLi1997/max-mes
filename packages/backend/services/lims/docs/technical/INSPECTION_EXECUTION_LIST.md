# 检验单执行项目清单功能设计文档

## 📋 **概述**

**✅ 正确的业务模型理解：**
检验项目清单应该由后端根据检验方案版本ID自动查询生成，并且必须按照正确的业务层级关系进行查询。

**🎯 正确的业务层级关系：**
```
检验方案版本 (InspectionSchemeVersion)
  └── 实验包配置 (InspectionSchemeDetail: materialId + packageId)  
      └── 检验项目 (InspectPackageItem: 实验包包含哪些检验项目)
          └── 分析项 (InspectItemParameter: 检验项目包含哪些分析项)
              └── 数据点 (InspectParameterDataPoint: 分析项包含哪些数据点)
```

**❌ 之前的错误理解：**
- 检验方案详情直接包含分析项配置，跳过了检验项目层级
- 执行清单生成逻辑没有按照正确的层级关系

**✅ 修正后的优势：**
- ✅ **业务模型正确** - 严格按照 实验包→检验项目→分析项→数据点 的层级关系
- ✅ **数据一致性保障** - 直接从相应的关联表查询，避免数据错误
- ✅ **减少网络传输** - 前端只需传递版本ID，不需要传递大量项目数据  
- ✅ **业务逻辑集中** - 项目选择逻辑统一在后端处理
- ✅ **维护性更好** - 配置变更只需要修改后端逻辑

## 🎯 **功能定位**

### **不是完整快照，而是执行清单**
```
传统理解（我之前的误解）:
复制所有配置数据 → 大量冗余数据 → 存储空间浪费

正确理解（您的观点）:
选择执行项目 → 存储项目清单 → 后续流程基于清单执行
```

### **核心价值**
1. **明确执行范围** - 请验时确认本次检验要做哪些项目
2. **数据基础稳定** - 后续流程基于固定的执行清单
3. **避免重复查询** - 不需要每次都查检验方案配置
4. **业务逻辑清晰** - 每个检验单都有明确的工作清单

## 🗃️ **数据库设计**

### **清单表结构（非快照模式）**

#### **1. 检验单检验项目清单表**
```sql
CREATE TABLE lm_inspection_order_item (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    inspect_item_id BIGINT NOT NULL,          -- 检验项目ID（引用）
    inspect_item_name VARCHAR(200),           -- 检验项目名称（冗余字段，便于查询）
    inspect_item_code VARCHAR(100),           -- 检验项目编码（冗余字段）
    sort INT DEFAULT 0,                       -- 排序
    remark VARCHAR(500),                      -- 备注
    UNIQUE KEY uk_order_inspect_item (inspection_order_id, inspect_item_id)
);
```

#### **2. 检验单分析项清单表**
```sql
CREATE TABLE lm_inspection_order_parameter (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    order_item_id BIGINT NOT NULL,            -- 检验项目清单ID
    parameter_id BIGINT NOT NULL,             -- 分析项ID（引用）
    parameter_name VARCHAR(200),              -- 分析项名称（冗余字段）
    parameter_code VARCHAR(100),              -- 分析项编码（冗余字段）
    sort INT DEFAULT 0,                       -- 排序
    remark VARCHAR(500),                      -- 备注
    UNIQUE KEY uk_order_parameter (inspection_order_id, order_item_id, parameter_id)
);

-- 注意：实体类使用InspectionOrderParameter命名以保持与InspectParameter一致
```

#### **3. 检验单数据点清单表**
```sql
CREATE TABLE lm_inspection_order_data_point (
    id BIGINT PRIMARY KEY,
    inspection_order_id BIGINT NOT NULL,      -- 检验单ID
    order_parameter_id BIGINT NOT NULL,      -- 分析项清单ID
    data_point_id BIGINT NOT NULL,            -- 数据点ID（引用）
    data_point_name VARCHAR(200),             -- 数据点名称（冗余字段）
    data_point_code VARCHAR(100),             -- 数据点编码（冗余字段）
    sort INT DEFAULT 0,                       -- 排序
    remark VARCHAR(500),                      -- 备注
    UNIQUE KEY uk_order_data_point (inspection_order_id, order_parameter_id, data_point_id)
);
```

## 🎨 **前端交互设计**

### **请验界面流程（修正后）**
```
1. 选择检品和检验方案版本
    ↓
2. 填写请验单基本信息（批次号、生产日期等）
    ↓  
3. 填写自定义字段（如果有模板）
    ↓
4. 填写取样信息
    ↓
5. 提交请验单（只传递基础信息 + 方案版本ID）
    ↓
6. 后端自动根据方案版本ID生成执行清单
    ↓
7. 完成请验单创建
```

### **界面示例（修正后）**
```
┌─ 请验单创建 ─────────────────────────────────────┐
│                                                   │
│ 检品信息: [原料药A]  方案版本: [V1.0] ✓           │
│                                                   │
│ 基本信息:                                         │
│ 批次号: [2025010101]                              │
│ 生产日期: [2025-01-01]                            │
│ 备注: [常规检验]                                   │
│                                                   │
│ 自定义字段:                                        │
│ 供应商: [ABC制药]                                 │
│ 检验原因: [入库检验]                               │
│                                                   │
│ 取样信息:                                         │
│ 取样地点: [仓库A-1]  取样量: [100g]               │
│ 取样人员: [张三]                                   │
│                                                   │
│ [创建请验单] ← 只传递这些基础信息                   │
│                                                   │
│ ↓ 后端自动处理 ↓                                  │
│ • 根据方案版本ID查询所有配置                       │
│ • 自动生成执行清单保存到数据库                     │
│ • 无需前端选择具体项目                             │
│                                                   │
└─────────────────────────────────────────────────┘
```

## ⚙️ **技术实现**

### **1. 请求数据结构（简化后）**
```java
// 请验单保存DTO - 不再包含项目选择信息
public class InspectionOrderSaveDTO {
    private Long materialId;              // 检品ID
    private Long schemeVersionId;         // 检验方案版本ID ← 核心字段
    private String batchNo;               // 批次号
    private LocalDateTime productionDate; // 生产日期
    private Long templateId;              // 模板ID
    private List<CustomFieldValueDTO> customFields; // 自定义字段
    private String remark;                // 备注
    private List<InspectionSamplingSaveDTO> samplingList; // 取样信息
    // 移除了 inspectionItems 字段！
}
```

### **2. 保存逻辑（修正后 - 按照正确的业务层级）**
```java
/**
 * 根据检验方案版本ID自动生成执行清单
 * 按照正确的业务层级：检验方案版本 → 实验包配置 → 检验项目 → 分析项 → 数据点
 */
private void saveInspectionItemsList(Long inspectionOrderId, Long schemeVersionId) {
    // 1. 查询检验方案详情（实验包配置）
    List<InspectionSchemeDetailDTO> schemeDetails = 
        inspectionSchemeDetailService.listInspectionSchemeDetails(schemeVersionId);
    
    // 2. 遍历实验包配置
    for (InspectionSchemeDetailDTO schemeDetail : schemeDetails) {
        
        // 3. 根据实验包ID查询检验项目列表
        List<InspectPackageItem> packageItems = 
            inspectPackageItemMapper.selectByPackageId(schemeDetail.getPackageId());
        
        // 提取检验项目ID列表并批量查询详情
        List<Long> inspectItemIds = packageItems.stream()
            .map(InspectPackageItem::getInspectItemId).collect(Collectors.toList());
        List<InspectItemDTO> inspectItems = inspectItemService.selectByIdList(inspectItemIds);
        
        // 4. 遍历检验项目
        for (InspectItemDTO inspectItem : inspectItems) {
            
            // 4.1. 保存检验项目清单（使用真实的检验项目信息）
            InspectionOrderItem orderItem = new InspectionOrderItem();
            orderItem.setInspectionOrderId(inspectionOrderId);
            orderItem.setInspectItemId(inspectItem.getId());
            orderItem.setInspectItemName(inspectItem.getName());
            orderItem.setInspectItemCode(inspectItem.getCode());
            inspectionOrderItemMapper.insert(orderItem);
            
            // 5. 根据检验项目ID查询分析项列表
            List<InspectItemParameter> itemParameters = 
                inspectItemParameterMapper.selectByInspectId(inspectItem.getId());
            
            // 提取分析项ID列表并批量查询详情
            List<Long> parameterIds = itemParameters.stream()
                .map(InspectItemParameter::getInspectParameterId).collect(Collectors.toList());
            List<InspectParameterDTO> parameters = 
                inspectParameterService.selectByIdList(parameterIds);
            
            // 6. 遍历分析项
            for (InspectParameterDTO parameter : parameters) {
                
                // 6.1. 保存分析项清单（使用真实的分析项信息）
                InspectionOrderAnalyzeItem orderAnalyzeItem = new InspectionOrderAnalyzeItem();
                orderAnalyzeItem.setInspectionOrderId(inspectionOrderId);
                orderAnalyzeItem.setOrderItemId(orderItem.getId());
                orderAnalyzeItem.setParameterId(parameter.getId());
                orderAnalyzeItem.setParameterName(parameter.getName());
                orderAnalyzeItem.setParameterCode(parameter.getCode());
                inspectionOrderAnalyzeItemMapper.insert(orderAnalyzeItem);
                
                // 7. 根据分析项ID查询数据点列表
                List<InspectParameterDataPoint> dataPoints = 
                    inspectParameterDataPointMapper.selectByParameterId(parameter.getId());
                
                // 7.1. 保存数据点清单（使用真实的数据点信息）
                for (InspectParameterDataPoint dataPoint : dataPoints) {
                    InspectionOrderDataPoint orderDataPoint = new InspectionOrderDataPoint();
                    orderDataPoint.setInspectionOrderId(inspectionOrderId);
                    orderDataPoint.setOrderAnalyzeItemId(orderAnalyzeItem.getId());
                    orderDataPoint.setDataPointId(dataPoint.getId());
                    orderDataPoint.setDataPointName(dataPoint.getName());
                    orderDataPoint.setDataPointCode(dataPoint.getCode());
                    inspectionOrderDataPointMapper.insert(orderDataPoint);
                }
            }
        }
    }
}
```

### **3. 查询使用**
```java
// 后续流程基于执行清单而不是实时查询方案配置
public List<InspectionOrderItem> getExecutionItems(Long inspectionOrderId) {
    return inspectionOrderItemMapper.selectByInspectionOrderId(inspectionOrderId);
}

public List<InspectionOrderAnalyzeItem> getExecutionAnalyzeItems(Long inspectionOrderId) {
    return inspectionOrderAnalyzeItemMapper.selectByInspectionOrderId(inspectionOrderId);
}

public List<InspectionOrderDataPoint> getExecutionDataPoints(Long inspectionOrderId) {
    return inspectionOrderDataPointMapper.selectByInspectionOrderId(inspectionOrderId);
}
```

## 📊 **使用场景**

### **1. 样品登记阶段**
```java
// 获取本次检验需要登记的检验项目（基于执行清单）
List<InspectionOrderItem> executionItems = 
    inspectionOrderItemMapper.selectByInspectionOrderId(inspectionOrderId);

// 只为选中的检验项目创建样品登记记录
for (InspectionOrderItem item : executionItems) {
    createSampleRegistration(inspectionOrderId, item.getInspectItemId());
}
```

### **2. 样品接收阶段**
```java
// 获取本次检验要执行的分析项（基于执行清单）
List<InspectionOrderAnalyzeItem> executionAnalyzeItems = 
    inspectionOrderAnalyzeItemMapper.selectByInspectionOrderId(inspectionOrderId);

// 验证样品是否满足所选分析项的要求
for (InspectionOrderAnalyzeItem analyzeItem : executionAnalyzeItems) {
    validateSampleForAnalyzeItem(sample, analyzeItem.getParameterId());
}
```

### **3. 数据录入阶段**
```java
// 获取数据录入界面需要的数据点（基于执行清单）
List<InspectionOrderDataPoint> executionDataPoints = 
    inspectionOrderDataPointMapper.selectByOrderAnalyzeItemId(analyzeItemId);

// 根据执行清单生成数据录入界面（只显示选中的数据点）
for (InspectionOrderDataPoint dataPoint : executionDataPoints) {
    createInputField(dataPoint.getDataPointId());
}
```

## 🎉 **优势特点**

### **1. 业务逻辑清晰**
- ✅ **执行范围明确** - 每次检验都有清晰的工作清单
- ✅ **用户主导选择** - 用户在请验时主动选择要执行的项目
- ✅ **避免不必要工作** - 只执行选中的项目，提高效率

### **2. 数据存储优化**
- ✅ **存储空间节省** - 只存储选中的项目ID和必要的冗余字段
- ✅ **查询性能优良** - 直接查询执行清单，无需复杂关联
- ✅ **引用关系清晰** - 保持与原始配置的引用关系

### **3. 系统架构简化**
- ✅ **后续流程简单** - 基于固定清单执行，逻辑清晰
- ✅ **配置变更隔离** - 不受原始方案配置变更影响
- ✅ **扩展性良好** - 可以灵活增加清单相关功能

### **4. 用户体验优化**
- ✅ **选择灵活** - 用户根据实际需要选择执行项目
- ✅ **界面清晰** - 分层选择，逻辑清楚
- ✅ **工作高效** - 避免不必要的检验项目

## 🔄 **实现方式对比**

| 方面 | ❌ 前端传递方式 | ✅ 后端查询方式 |
|------|----------|-------------|
| **数据一致性** | 可能存在前端数据错误 | 直接从配置查询，保证一致性 |
| **网络传输** | 需要传递大量项目数据 | 只传递版本ID |
| **业务逻辑** | 前后端都有选择逻辑 | 逻辑集中在后端 |
| **维护复杂度** | 前后端都需要维护 | 只需要维护后端逻辑 |
| **数据安全** | 前端可能传递错误数据 | 后端控制数据准确性 |
| **配置变更影响** | 前后端都需要同步更新 | 只需要更新后端逻辑 |

### **新的工作流程**
```
前端：用户选择方案版本 → 填写基础信息 → 提交
                 ↓
后端：接收版本ID → 查询方案配置 → 生成执行清单 → 保存
                 ↓
结果：检验单创建完成，包含完整的执行项目清单
```

## ✅ **总结**

您的观点非常正确！这个设计完美解决了：

1. **🎯 执行范围明确化** - 请验时明确本次检验的工作范围
2. **📋 数据基础稳定化** - 后续流程基于固定的执行清单
3. **⚡ 系统性能优化** - 避免复杂的实时配置查询
4. **🛠️ 业务逻辑简化** - 后续流程逻辑更加清晰

这不是"完整快照"，而是"执行清单"的概念，既保证了数据的稳定性，又提供了选择的灵活性，是LIMS系统设计中的优秀实践！🚀