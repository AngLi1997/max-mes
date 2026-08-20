# PDF生成功能依赖说明

为了支持HTML转PDF功能，需要在 `bmos-lims2-server/pom.xml` 中添加以下Maven依赖：

## 必需依赖

### 1. Flying Saucer (xhtmlrenderer) - HTML转PDF核心库
```xml
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.1.22</version>
</dependency>
```

### 2. OpenPDF - PDF生成库（iText的开源版本）
```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version>
</dependency>
```

### 3. iText Asian Font Support - 中文字体支持（可选但推荐）
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-asian</artifactId>
    <version>5.2.0</version>
</dependency>
```

## 完整的依赖配置示例

在 `bmos-lims2-server/pom.xml` 的 `<dependencies>` 节点中添加：

```xml
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

## 版本说明

- **flying-saucer-pdf 9.1.22**: 最新稳定版本，支持Java 8+
- **openpdf 1.3.30**: 最新版本，兼容性好，无许可证问题
- **itext-asian 5.2.0**: 支持中文字体渲染

## 功能特点

1. **HTML转PDF**: 支持将HTML内容直接转换为PDF
2. **CSS样式支持**: 支持大部分CSS样式属性
3. **中文字体**: 正确显示中文内容
4. **表格支持**: 完美支持HTML表格转换
5. **开源免费**: 无商业许可证限制

## 使用示例

```java
// 使用PdfGeneratorUtil工具类
byte[] pdfBytes = PdfGeneratorUtil.convertHtmlToPdf(htmlContent);
```

## 注意事项

1. HTML必须是格式良好的XHTML格式
2. CSS样式建议使用内联或内部样式表
3. 图片路径需要是绝对路径或base64编码
4. 不支持JavaScript和复杂的CSS3特性

## 替代方案

如果不想使用Flying Saucer，也可以考虑以下替代方案：

### 方案一：iText 7 + pdfHTML
```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>html2pdf</artifactId>
    <version>4.0.5</version>
</dependency>
```

### 方案二：wkhtmltopdf (需要系统安装wkhtmltopdf)
```xml
<dependency>
    <groupId>com.github.jhonnymertz</groupId>
    <artifactId>java-wkhtmltopdf-wrapper</artifactId>
    <version>1.1.12-RELEASE</version>
</dependency>
```

推荐使用Flying Saucer + OpenPDF方案，因为它轻量、稳定且无需外部依赖。