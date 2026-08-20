package com.bmos.lims2.server.inspect.order.utils;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * PDF生成工具类
 * 使用 Flying Saucer (xhtmlrenderer) + OpenPDF 实现HTML转PDF
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Slf4j
public class PdfGeneratorUtil {

    /**
     * 将HTML内容转换为PDF字节数组
     * 
     * @param htmlContent HTML内容
     * @return PDF字节数组
     * @throws Exception 转换异常
     */
    public static byte[] convertHtmlToPdf(String htmlContent) throws Exception {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            // 确保HTML格式正确
            String wellFormedHtml = ensureWellFormedHtml(htmlContent);
            
            // 创建ITextRenderer
            ITextRenderer renderer = new ITextRenderer();
            
            // 解析HTML文档
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(wellFormedHtml.getBytes(StandardCharsets.UTF_8)));
            
            // 设置文档
            renderer.setDocument(doc, null);
            
            // 布局文档
            renderer.layout();
            
            // 创建PDF
            renderer.createPDF(outputStream);
            
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("HTML转PDF失败: {}", e.getMessage(), e);
            throw new Exception("HTML转PDF转换失败: " + e.getMessage(), e);
        }
    }

    /**
     * 确保HTML格式正确，添加必要的DOCTYPE和meta标签
     * 
     * @param htmlContent 原始HTML内容
     * @return 格式化后的HTML内容
     */
    private static String ensureWellFormedHtml(String htmlContent) {
        // 如果HTML已经包含DOCTYPE，直接返回
        if (htmlContent.trim().toLowerCase().startsWith("<!doctype")) {
            return htmlContent;
        }
        
        // 如果HTML已经包含html标签，添加DOCTYPE
        if (htmlContent.trim().toLowerCase().startsWith("<html")) {
            return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                   "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + htmlContent;
        }
        
        // 完整包装HTML
        StringBuilder wellFormed = new StringBuilder();
        wellFormed.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" ");
        wellFormed.append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        wellFormed.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        wellFormed.append("<head>\n");
        wellFormed.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n");
        wellFormed.append("<title>请验单</title>\n");
        wellFormed.append("<style type=\"text/css\">\n");
        wellFormed.append("body { font-family: SimSun, serif; font-size: 12px; }\n");
        wellFormed.append("table { border-collapse: collapse; width: 100%; }\n");
        wellFormed.append("table, th, td { border: 1px solid black; }\n");
        wellFormed.append("th, td { padding: 5px; text-align: left; }\n");
        wellFormed.append("h1 { text-align: center; }\n");
        wellFormed.append("</style>\n");
        wellFormed.append("</head>\n");
        wellFormed.append("<body>\n");
        wellFormed.append(htmlContent);
        wellFormed.append("\n</body>\n");
        wellFormed.append("</html>");
        
        return wellFormed.toString();
    }

    /**
     * 生成默认的检验单HTML模板
     * 
     * @param orderNo 检验单号
     * @param materialName 检品名称
     * @param batchNo 批次号
     * @param createTime 创建时间
     * @return HTML内容
     */
    public static String generateDefaultTemplate(String orderNo, String materialName, 
                                                String batchNo, String createTime) {
        StringBuilder html = new StringBuilder();
        
        html.append("<h1>请验单</h1>");
        html.append("<table>");
        html.append("<tr><td><strong>检验单号:</strong></td><td>").append(orderNo != null ? orderNo : "").append("</td></tr>");
        html.append("<tr><td><strong>检品名称:</strong></td><td>").append(materialName != null ? materialName : "").append("</td></tr>");
        html.append("<tr><td><strong>批次号:</strong></td><td>").append(batchNo != null ? batchNo : "").append("</td></tr>");
        html.append("<tr><td><strong>创建时间:</strong></td><td>").append(createTime != null ? createTime : "").append("</td></tr>");
        html.append("</table>");
        
        return html.toString();
    }
}