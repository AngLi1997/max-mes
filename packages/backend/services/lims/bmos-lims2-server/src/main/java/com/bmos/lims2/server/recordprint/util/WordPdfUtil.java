package com.bmos.lims2.server.recordprint.util;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.VerticalAlignment;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description: Word/PDF 构建工具（页眉/页脚/页码、合并、加水印）
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
public final class WordPdfUtil {

    private WordPdfUtil() {}

    /**
     * 将PDF字节写入临时文件，返回文件路径
     * @param pdfBytes pdf内容
     * @param prefix 前缀
     * @return 临时文件路径
     */
    public static String writePdfToTempFile(byte[] pdfBytes, String prefix) {
        try {
            java.io.File tmp = java.io.File.createTempFile(prefix, ".pdf");
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tmp)) {
                fos.write(pdfBytes);
            }
            tmp.deleteOnExit();
            return tmp.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("写入临时PDF文件失败", e);
        }
    }

    /**
     * 通过HTML构建Word文档
     * @param html html
     * @return Document
     */
    public static Document buildDocFromHtml(String html) {
        try {
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            return new Document(new java.io.ByteArrayInputStream(bytes));
        } catch (Exception e) {
            throw new RuntimeException("根据HTML构建Word失败", e);
        }
    }

    /**
     * 设置页眉/页脚/页码
     * @param document 文档
     * @param headerTitle 页眉标题
     */
    public static void applyHeaderFooterAndPageNumbers(Document document, String headerTitle) {
        try {
            for (Section section : document.getSections()) {
                // Header
                HeaderFooter header = new HeaderFooter(document, HeaderFooterType.HEADER_PRIMARY);
                section.getHeadersFooters().add(header);
                DocumentBuilder builder = new DocumentBuilder(document);
                builder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);
                builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
                builder.write(headerTitle == null ? "" : headerTitle);

                // Footer with page number: "Page x of y"
                HeaderFooter footer = new HeaderFooter(document, HeaderFooterType.FOOTER_PRIMARY);
                section.getHeadersFooters().add(footer);
                builder.moveToHeaderFooter(HeaderFooterType.FOOTER_PRIMARY);
                builder.getParagraphFormat().setAlignment(ParagraphAlignment.RIGHT);
                builder.write("第 ");
                builder.insertField("PAGE", "");
                builder.write(" 页 / 共 ");
                builder.insertField("NUMPAGES", "");
                builder.write(" 页");
            }
        } catch (Exception e) {
            throw new RuntimeException("设置页眉/页脚/页码失败", e);
        }
    }

    /**
     * 为文档添加对角线水印
     * @param document 文档
     * @param watermarkText 水印文字
     */
    public static void addDiagonalWatermark(Document document, String watermarkText) {
        try {
            Shape watermark = new Shape(document, ShapeType.TEXT_PLAIN_TEXT);
            watermark.getTextPath().setText(watermarkText);
            watermark.getTextPath().setFontFamily("宋体");
            watermark.setWidth(400);
            watermark.setHeight(100);
            watermark.setRotation(-40);
            watermark.getFill().setColor(new Color(180, 180, 180, 80));
            watermark.setStrokeColor(new Color(180, 180, 180, 80));
            watermark.setRelativeHorizontalPosition(com.aspose.words.RelativeHorizontalPosition.PAGE);
            watermark.setRelativeVerticalPosition(com.aspose.words.RelativeVerticalPosition.PAGE);
            watermark.setWrapType(com.aspose.words.WrapType.NONE);
            watermark.setVerticalAlignment(VerticalAlignment.CENTER);
            watermark.setHorizontalAlignment(com.aspose.words.HorizontalAlignment.CENTER);

            // 必须将 Shape 放到段落中再加入到 HeaderFooter，不能直接 append Shape 到 HeaderFooter
            com.aspose.words.Paragraph watermarkPara = new com.aspose.words.Paragraph(document);
            watermarkPara.appendChild(watermark);

            for (Section sect : document.getSections()) {
                HeaderFooter header = sect.getHeadersFooters().getByHeaderFooterType(HeaderFooterType.HEADER_PRIMARY);
                if (header == null) {
                    header = new HeaderFooter(document, HeaderFooterType.HEADER_PRIMARY);
                    sect.getHeadersFooters().add(header);
                }
                // 将段落克隆后添加到页眉
                header.appendChild(watermarkPara.deepClone(true));
            }
        } catch (Exception e) {
            throw new RuntimeException("添加水印失败", e);
        }
    }

    /**
     * 合并多个文档
     * @param docs 文档列表
     * @return 合并后的文档
     */
    public static Document mergeDocuments(List<Document> docs) {
        try {
            if (docs == null || docs.isEmpty()) {
                return new Document();
            }
            Document base = docs.get(0);
            for (int i = 1; i < docs.size(); i++) {
                Document next = docs.get(i);
                base.appendDocument(next, com.aspose.words.ImportFormatMode.KEEP_SOURCE_FORMATTING);
            }
            return base;
        } catch (Exception e) {
            throw new RuntimeException("合并文档失败", e);
        }
    }

    /**
     * 将文档导出为PDF字节数组
     * @param document 文档
     * @return PDF bytes
     */
    public static byte[] toPdfBytes(Document document) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            document.save(bos, SaveFormat.PDF);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("导出PDF失败", e);
        }
    }
}


