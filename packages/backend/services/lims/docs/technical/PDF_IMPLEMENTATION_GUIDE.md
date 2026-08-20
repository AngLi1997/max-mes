# PDF生成功能实现指南

## ✅ 已完成的工作

### 1. 添加Maven依赖
已在 `bmos-lims2-server/pom.xml` 中添加以下依赖：

```xml
<!-- Lombok依赖 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
    <scope>provided</scope>
</dependency>

<!-- PDF生成相关依赖 -->
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.1.22</version>
</dependency>
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-asian</artifactId>
    <version>5.2.0</version>
</dependency>
```

### 2. 创建PDF工具类
创建了 `PdfGeneratorUtil` 工具类，位于：
`bmos-lims2-server/src/main/java/com/bmos/lims2/server/inspect/order/utils/PdfGeneratorUtil.java`

核心功能：
- ✅ HTML转PDF转换
- ✅ 自动格式化XHTML
- ✅ 中文字体支持
- ✅ CSS样式支持
- ✅ 异常处理

### 3. 更新Service层
更新了 `InspectionOrderServiceImpl` 中的 `generateInspectionOrderPdf` 方法：

```java
// 修复前（错误）
return HtmlToImageUtil.convertHtmlToPdf(htmlContent); // ❌ 方法不存在

// 修复后（正确）
return PdfGeneratorUtil.convertHtmlToPdf(htmlContent); // ✅ 完整实现
```

### 4. 增强HTML内容生成
`generateHtmlContent` 方法现在包含：
- ✅ 检验单基本信息（单号、检品、批次等）
- ✅ 自定义字段动态显示
- ✅ 取样计划表格
- ✅ 备注信息
- ✅ 专业表格样式

## 🚀 如何使用

### 方法1: 直接调用Service方法
```java
@Autowired
private InspectionOrderService inspectionOrderService;

public void generatePdf(Long orderId) {
    try {
        byte[] pdfBytes = inspectionOrderService.generateInspectionOrderPdf(orderId);
        // 保存到文件或返回给前端
        Files.write(Paths.get("inspection_order_" + orderId + ".pdf"), pdfBytes);
    } catch (Exception e) {
        log.error("PDF生成失败", e);
    }
}
```

### 方法2: 通过Controller API
```java
@GetMapping("/pdf/{id}")
@ApiOperation("生成检验单PDF")
public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
    byte[] pdfData = inspectionOrderService.generateInspectionOrderPdf(id);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "inspection_order_" + id + ".pdf");
    
    return ResponseEntity.ok().headers(headers).body(pdfData);
}
```

### 方法3: 直接使用工具类
```java
import com.bmos.lims2.server.inspect.order.utils.PdfGeneratorUtil;

public void customPdfGeneration() {
    String htmlContent = "<h1>测试PDF</h1><p>这是一个测试PDF文档</p>";
    
    try {
        byte[] pdfBytes = PdfGeneratorUtil.convertHtmlToPdf(htmlContent);
        // 处理PDF字节数组
    } catch (Exception e) {
        log.error("PDF生成失败", e);
    }
}
```

## 🎯 PDF内容示例

生成的PDF将包含以下内容：

```
                        检验单
    ┌─────────────────────────────────────────────┐
    │ 检验单号：    IO202501270001                 │
    │ 检品名称：    原料药A                        │
    │ 检品编码：    MAT001                        │
    │ 批次号：      BATCH20250127                 │
    │ 生产日期：    2025-01-27                    │
    │ 检验方案：    原料药检验方案V1.0              │
    │ 方案版本：    1.0                           │
    │ 单据状态：    待确认                         │
    │ 创建时间：    2025-01-27 15:30:00           │
    │ 创建人：      admin                         │
    │                                            │
    │ 自定义字段：                                │
    │ 供应商：      供应商A                        │
    │ 合格证号：    CERT001                       │
    └─────────────────────────────────────────────┘

                       取样计划
    ┌──────────┬────────┬──────┬──────┬────────┬────────┐
    │ 检验项目 │计划取样量│ 单位 │取样份数│ 取样方式│ 取样地点│
    ├──────────┼────────┼──────┼──────┼────────┼────────┤
    │ 外观检验 │   100   │  g   │  3   │ 随机取样│ 仓库A  │
    │ 含量检验 │   50    │  g   │  2   │ 均匀取样│ 仓库A  │
    └──────────┴────────┴──────┴──────┴────────┴────────┘

                         备注
    此批次原料需要特殊处理，请注意保存条件。
```

## 🔧 项目集成步骤

1. **刷新依赖**
   - 在IDE中刷新Maven项目
   - 或运行 `mvn clean compile`

2. **验证依赖**
   - 确保 `org.xhtmlrenderer.pdf.ITextRenderer` 类可以正常导入
   - 确保 `lombok` 注解正常工作

3. **测试功能**
   ```java
   // 简单测试
   @Test
   public void testPdfGeneration() {
       String html = PdfGeneratorUtil.generateDefaultTemplate(
           "TEST001", "测试检品", "BATCH001", "2025-01-27"
       );
       
       byte[] pdf = PdfGeneratorUtil.convertHtmlToPdf(html);
       
       assertNotNull(pdf);
       assertTrue(pdf.length > 0);
   }
   ```

## ⚠️ 注意事项

1. **HTML格式要求**
   - 必须是格式良好的XHTML
   - 自动处理DOCTYPE和meta标签

2. **中文字体支持**
   - 已配置SimSun字体支持中文显示
   - CSS中使用 `font-family: SimSun, serif;`

3. **样式限制**
   - 不支持JavaScript
   - 复杂CSS3特性支持有限
   - 建议使用内联或内部样式表

4. **性能考虑**
   - 大文档生成可能较慢
   - 建议异步处理大批量PDF生成

## 🐛 故障排除

### 依赖问题
如果出现 `ITextRenderer cannot be resolved` 错误：
1. 确认Maven依赖已正确添加
2. 刷新IDE项目
3. Clean并重新编译项目

### 字体问题
如果中文显示异常：
1. 确认 `itext-asian` 依赖已添加
2. 检查CSS字体设置
3. 可能需要在系统中安装中文字体

### 内存问题
如果生成大PDF时内存不足：
1. 增加JVM内存参数
2. 优化HTML内容大小
3. 考虑分页处理

## 🎉 完成！

PDF生成功能现已完全实现，支持：
- ✅ HTML转PDF转换
- ✅ 中文内容显示
- ✅ 专业表格布局
- ✅ 自定义字段展示
- ✅ 完整的检验单信息
- ✅ 错误处理和日志记录

现在您可以生成专业、美观的检验单PDF文档了！