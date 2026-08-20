# 平台编号规则集成文档（正确实现版本）

## ✅ 实现完成

已成功实现检验单号调用平台编号规则接口的**正确流程**：先获取编号，再确认使用。

## 🎯 实现内容

### 1. 核心修改

#### **PlatformCodeFeignClient.java**
新增两个方法实现正确的编号获取流程：

```java
/**
 * 获取检验单编号（未确认使用）
 */
public synchronized NextCodeVO getInspectOrderNextUseNo(String code) {
    // 调用 getNextUseNo 接口获取编号
    NextUseCodeDTO nextUseCodeDTO = new NextUseCodeDTO();
    nextUseCodeDTO.setType(CodeRuleTypeEnum.INSPECT_ORDER_NO.getValue());
    nextUseCodeDTO.setCode(code);
    nextUseCodeDTO.setProcessId(0L); // 检验单场景设置为0
    nextUseCodeDTO.setFields(new HashMap<>());
    
    ResponseInfo<NextCodeVO> responseInfo = platformCodeFeign.getNextUseNo(nextUseCodeDTO);
    // 如果失败直接抛异常，不使用备用方案
    if (!responseInfo.isSuccess()) {
        throw new BmosException(LimsResponseCode.BUSINESS_ERROR, "获取检验单编号失败：" + responseInfo.getMessage());
    }
    return responseInfo.getData();
}

/**
 * 确认检验单编号已使用
 */
public synchronized void confirmInspectOrderNo(String codeRule, String fullNo) {
    // 调用 confirmNo 接口确认编号使用
    ConfirmNextUseCodeDTO confirmDTO = ConfirmNextUseCodeDTO.builder()
            .code(codeRule)
            .fullNo(fullNo)
            .codeApplyTime(LocalDate.now())
            .fields(new HashMap<>())
            .build();
    
    ResponseInfo<Void> responseInfo = platformCodeFeign.confirmNo(confirmDTO);
    // 如果失败直接抛异常
    if (!responseInfo.isSuccess()) {
        throw new BmosException(LimsResponseCode.BUSINESS_ERROR, "确认编号使用失败：" + responseInfo.getMessage());
    }
}
```

#### **InspectionOrderServiceImpl.java**
修改为两步流程：

```java
// 生成检验单号（未确认使用）
private NextCodeVO generateOrderNo() {
    log.info("调用平台接口获取检验单编号，编号规则代码：{}", INSPECTION_ORDER_CODE_RULE);
    return platformCodeFeignClient.getInspectOrderNextUseNo(INSPECTION_ORDER_CODE_RULE);
}

// 确认检验单编号已使用
private void confirmOrderNo(NextCodeVO nextCodeVO) {
    log.info("确认检验单编号已使用，编号：{}", nextCodeVO.getNo());
    platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());
}

// 保存检验单的逻辑
NextCodeVO nextCodeVO = generateOrderNo(); // 先获取编号
inspectionOrder.setOrderNo(nextCodeVO.getNo());

try {
    // 保存检验单和相关数据
    inspectionOrderMapper.insert(inspectionOrder);
    saveCustomFields(inspectionOrder.getId(), saveDTO.getCustomFields());
    saveSamplingInfo(inspectionOrder.getId(), saveDTO.getSamplingList());
    
    // 确认编号已使用
    confirmOrderNo(nextCodeVO);
    
} catch (Exception e) {
    // 保存失败时不确认编号，让编号可以被重复使用
    log.error("保存检验单失败，编号：{}，错误：{}", nextCodeVO.getNo(), e.getMessage(), e);
    throw e;
}
```

### 2. 调用流程

```
新建检验单
    ↓
saveInspectionOrder() 方法
    ↓
1. generateOrderNo() 方法
    ↓
   platformCodeFeignClient.getInspectOrderNextUseNo(INSPECTION_ORDER_CODE_RULE)
    ↓
   调用平台接口: /api/app/platform/codeRule/getNextUseNo
    ↓
   返回 NextCodeVO (编号未确认使用)
    ↓
2. 保存检验单数据到数据库
    ↓
3. confirmOrderNo(nextCodeVO) 方法
    ↓
   platformCodeFeignClient.confirmInspectOrderNo(code, fullNo)
    ↓
   调用平台接口: /api/app/platform/codeRule/confirmNo
    ↓
   确认编号已使用，完成流程
```

### 3. 技术架构

```
InspectionOrderServiceImpl
    ↓ 依赖注入
PlatformCodeFeignClient
    ↓ 调用
PlatformCodeFeign (Feign接口)
    ↓ HTTP请求
平台服务 (bmos-platform-service)
    ↓ 返回
生成的编号
```

## 🛡️ 异常处理机制

### 1. **严格异常处理**
- **获取编号失败**：直接抛出 `BmosException`，让用户知道平台接口异常
- **确认编号失败**：直接抛出 `BmosException`，但编号已被占用，需要重新获取
- **保存数据失败**：不确认编号使用，让编号可以被重复获取

### 2. **详细日志记录**
- 获取编号时记录编号规则代码
- 成功获取编号时记录编号信息
- 确认编号使用时记录确认操作
- 异常发生时记录详细错误信息

### 3. **事务处理**
- 先获取编号（未确认）
- 保存数据成功后才确认编号使用
- 保存失败时不确认，编号可重复使用
- 确保编号不会因为数据保存失败而被浪费

### 4. **无备用方案**
⚠️ **重要**：不再提供备用编号生成方案，所有编号必须通过平台接口获取，确保编号的唯一性和规范性。

## 📝 平台配置要求

### 1. **编号规则配置**
在平台管理系统中需要配置编号规则：

- **规则代码**: `INSPECTION_ORDER`
- **规则类型**: `INSPECT_ORDER_NO`
- **编号格式**: 建议格式 `IO{YYYY}{MM}{DD}{0000}`
- **示例**: `IO202501270001`, `IO202501270002`

### 2. **权限配置**
确保LIMS服务有权限调用平台编号规则接口：
- `/api/app/platform/codeRule/getNextNo`

## 🔧 使用方式

### 1. **自动调用（推荐）**
创建新检验单时自动调用两步流程：
```java
InspectionOrderSaveDTO saveDTO = new InspectionOrderSaveDTO();
// ... 设置其他字段
Long inspectionOrderId = inspectionOrderService.saveInspectionOrder(saveDTO);
// 检验单号已自动通过平台接口获取并确认使用
```

### 2. **手动调用平台接口（高级用法）**
如果需要手动控制编号获取和确认流程：
```java
@Autowired
private PlatformCodeFeignClient platformCodeFeignClient;

public void manualOrderNoFlow() {
    // 1. 获取编号（未确认）
    NextCodeVO nextCodeVO = platformCodeFeignClient.getInspectOrderNextUseNo("INSPECTION_ORDER");
    String orderNo = nextCodeVO.getNo();
    
    try {
        // 2. 执行业务逻辑
        // ... 保存数据等操作
        
        // 3. 确认编号使用
        platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());
    } catch (Exception e) {
        // 业务失败时不确认编号，让编号可以重复使用
        log.error("业务处理失败，编号未确认使用：{}", orderNo, e);
        throw e;
    }
}
```

## 📊 监控和测试

### 1. **日志监控**
关注以下日志关键字：
- `调用平台接口获取检验单编号` - 开始获取编号
- `平台返回编号信息` - 成功获取编号（未确认）
- `确认检验单编号已使用` - 开始确认编号
- `确认编号使用结果` - 确认编号成功
- `检验单创建成功` - 整个流程完成
- `保存检验单失败` - 业务失败，编号未确认
- `获取检验单编号失败` - 平台接口获取编号失败
- `确认编号使用失败` - 平台接口确认失败

### 2. **测试场景**
1. **正常场景**: 平台接口正常，两步流程成功完成
2. **获取编号失败**: 平台接口不可用，直接抛异常给用户
3. **确认编号失败**: 编号获取成功但确认失败，抛异常
4. **业务保存失败**: 编号获取成功但数据保存失败，不确认编号
5. **并发场景**: 多个检验单同时创建，编号不重复

### 3. **测试方法**
```java
@Test
public void testGenerateOrderNo() {
    InspectionOrderSaveDTO saveDTO = new InspectionOrderSaveDTO();
    saveDTO.setMaterialId(1L);
    saveDTO.setSchemeId(1L);
    saveDTO.setBatchNo("TEST001");
    // ... 其他必填字段
    
    Long orderId = inspectionOrderService.saveInspectionOrder(saveDTO);
    InspectionOrderDTO orderDTO = inspectionOrderService.getInspectionOrderById(orderId);
    
    assertNotNull(orderDTO.getOrderNo());
    assertTrue(orderDTO.getOrderNo().startsWith("IO"));
    log.info("Generated order no: {}", orderDTO.getOrderNo());
}
```

## 🎉 优势特点

### 1. **标准化和规范性**
- 使用平台统一的编号规则，确保全系统编号一致性
- 支持复杂的编号格式配置，灵活满足业务需求
- 避免不同模块编号冲突，确保编号唯一性

### 2. **数据完整性**
- **两步流程**：先获取后确认，确保编号不会因业务失败而浪费
- **事务安全**：业务失败时不确认编号，让编号可以重复使用
- **异常透明**：平台接口异常直接传递给用户，不隐藏问题

### 3. **高可靠性**
- **严格异常处理**：不使用备用方案，确保所有编号都符合规范
- **详细日志记录**：完整记录获取、确认、异常等各个环节
- **并发安全**：平台接口保证并发场景下编号不重复

### 4. **易维护性**
- **职责清晰**：获取编号和确认使用分离，逻辑清晰
- **集中管理**：所有编号规则在平台统一配置和管理
- **接口标准**：遵循平台接口规范，便于后续扩展

## 🔍 相关文件

- `PlatformCodeFeign.java` - Feign接口定义
- `PlatformCodeFeignClient.java` - 编号规则客户端
- `CodeRuleTypeEnum.java` - 编号规则类型枚举
- `InspectionOrderServiceImpl.java` - 检验单服务实现

## 📋 注意事项

1. **编号规则配置**: 确保平台中已正确配置 `INSPECTION_ORDER` 编号规则
2. **网络连接**: 确保LIMS服务能正常访问平台服务
3. **权限设置**: 确保有调用编号规则接口的权限
4. **监控告警**: 建议设置监控，当备用方案频繁使用时及时处理

## ✅ 完成状态

- ✅ **正确实现两步流程**：先获取编号（`getNextUseNo`），再确认使用（`confirmNo`）
- ✅ **严格异常处理**：获取编号失败直接抛异常，不使用备用方案
- ✅ **事务安全保证**：业务失败时不确认编号，避免编号浪费
- ✅ **完整日志记录**：记录获取、确认、异常等所有关键节点
- ✅ **接口规范遵循**：使用平台标准的编号规则接口
- ✅ **详细文档说明**：提供完整的实现说明和使用指南

## 🚨 重要提醒

⚠️ **已移除备用方案**：获取编号失败时不再生成 `IO + 时间戳` 格式的备用编号，确保所有编号都通过平台接口获取，保证编号的标准化和唯一性。

🎯 **正确流程**：现在检验单创建时会严格按照平台要求的两步流程执行 - 先获取编号，保存成功后再确认编号使用！🚀