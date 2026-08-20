package com.bmos.file.docx.util;

import cn.hutool.core.io.FileUtil;
import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * @author yigaohui
 * @date 2024/6/7
 **/
@Slf4j
public final class WordUtil {

    private static final String TEMP_FOLDER = File.separator + "data" + File.separator + "temp" + File.separator;

    /**
     * 转换html字符串到pdf文件
     *
     * @param html html字符串
     * @return pdf 文件
     */
    public static File convertHtml2docx(String path, String fileName, String html) {
//        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        try {
//            license.setLicense(is);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        if (!FileUtil.file(path).exists()) {
            FileUtil.mkdir(path);
        }
        File file = FileUtil.file(path, fileName);
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            HtmlLoadOptions htmlLoadOptions = new HtmlLoadOptions();
            Document document = new Document(inputStream, htmlLoadOptions);
            document.save(fileOutputStream, SaveFormat.DOCX);
        } catch (Exception e) {
            log.error("【{}】保存pdf文件失败", file.getAbsolutePath());
        }
        return file;
    }

    public static void saveToPDF(File word, File pdf) throws Exception {
        // 加载文档
        Document document = new Document(word.getAbsolutePath());

        // 优化文档设置
        optimizeDocumentSettings(document);

        // 优化表格样式（重点处理边框问题）
        optimizeTableStyles(document);

        // 配置PDF保存选项
        PdfSaveOptions options = configurePdfSaveOptions();

        // 保存为PDF
        document.save(pdf.getAbsolutePath(), options);
    }

    /**
     * 优化文档设置
     */
    private static void optimizeDocumentSettings(Document doc) {
        // 设置兼容性选项，确保与Word 2016+兼容
        doc.getCompatibilityOptions().optimizeFor(MsWordVersion.WORD_2016);

        // 设置布局选项
        LayoutOptions layoutOptions = doc.getLayoutOptions();
        layoutOptions.setKeepOriginalFontMetrics(true);
    }

    /**
     * 优化文档中所有表格的样式，重点解决边框丢失问题
     */
    private static void optimizeTableStyles(Document doc) throws Exception {
        NodeCollection<Table> tables = doc.getChildNodes(NodeType.TABLE, true);

        for (Table table : tables) {
            // 1. 设置表格基本属性
            optimizeTableBasicProperties(table);

            // 2. 强化表格边框设置
            reinforceTableBorders(table);

            // 3. 处理跨页设置
            handleTablePageBreaking(table);

            // 4. 优化每一行
            for (Row row : table.getRows()) {
                optimizeRowFormat(row);

                // 5. 优化每个单元格
                for (Cell cell : row.getCells()) {
                    optimizeCellFormat(cell);
                }
            }
        }
    }

    /**
     * 设置表格基本属性
     */
    private static void optimizeTableBasicProperties(Table table) throws Exception {
        // 禁用表格自动适应，保持原始宽度
        table.setAllowAutoFit(false);

        // 设置表格样式选项
        table.setStyleOptions(TableStyleOptions.FIRST_ROW |
                TableStyleOptions.LAST_ROW |
                TableStyleOptions.FIRST_COLUMN |
                TableStyleOptions.LAST_COLUMN);

        // 确保表格对齐方式
        table.setAlignment(TableAlignment.LEFT);
    }

    /**
     * 强化表格边框设置 - 这是解决边框丢失的关键
     */
    private static void reinforceTableBorders(Table table) throws Exception {
        // 获取表格的边框设置
        BorderCollection borders = table.getFirstRow().getRowFormat().getBorders();

        // 如果表格有边框，强化边框设置
        if (hasBorders(borders)) {
            // 设置表格级别的边框
            table.setBorders(LineStyle.SINGLE, 0.5, Color.BLACK);

            // 确保内部边框也显示
            table.setBorder(BorderType.HORIZONTAL, LineStyle.SINGLE, 0.5, Color.BLACK, true);
            table.setBorder(BorderType.VERTICAL, LineStyle.SINGLE, 0.5, Color.BLACK, true);
        }
    }

    /**
     * 检查是否有边框
     */
    private static boolean hasBorders(BorderCollection borders) {
        for (Border border : borders) {
            if (border.getLineStyle() != LineStyle.NONE && border.getLineWidth() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理表格跨页设置
     */
    private static void handleTablePageBreaking(Table table) {
        // 设置表头重复
        if (table.getFirstRow() != null) {
            table.getFirstRow().getRowFormat().setHeadingFormat(true);
        }

        // 防止表格在页面间断开（对于小表格）
        if (table.getRows().getCount() <= 10) { // 只对小表格应用
            for (Row row : table.getRows()) {
                row.getRowFormat().setAllowBreakAcrossPages(false);
            }
        }
    }

    /**
     * 优化行格式
     */
    private static void optimizeRowFormat(Row row) {
        RowFormat rowFormat = row.getRowFormat();

        // 保持行高设置
        if (rowFormat.getHeightRule() == HeightRule.AUTO) {
            rowFormat.setHeightRule(HeightRule.AT_LEAST);
            if (rowFormat.getHeight() < 12.0) {
                rowFormat.setHeight(12.0); // 最小行高
            }
        }

        // 强化行边框
        BorderCollection rowBorders = rowFormat.getBorders();
        for (Border border : rowBorders) {
            if (border.getLineStyle() != LineStyle.NONE) {
                // 确保边框颜色和宽度
                if (border.getColor().equals(Color.WHITE) || border.getColor().getAlpha() == 0) {
                    border.setColor(Color.BLACK);
                }
                if (border.getLineWidth() < 0.25) {
                    border.setLineWidth(0.5);
                }
            }
        }
    }

    /**
     * 优化单元格格式 - 重点处理边框
     */
    private static void optimizeCellFormat(Cell cell) {
        CellFormat cellFormat = cell.getCellFormat();

        // 1. 强化单元格边框
        reinforceCellBorders(cell);

        // 2. 保持单元格合并设置
        preserveCellMerging(cellFormat);

        // 3. 优化单元格内容
        optimizeCellContent(cell);

        // 4. 设置单元格内边距（防止内容贴边）
        cellFormat.setTopPadding(2.0);
        cellFormat.setBottomPadding(2.0);
        cellFormat.setLeftPadding(3.0);
        cellFormat.setRightPadding(3.0);
    }

    /**
     * 强化单元格边框 - 核心方法
     */
    private static void reinforceCellBorders(Cell cell) {
        BorderCollection cellBorders = cell.getCellFormat().getBorders();

        for (Border border : cellBorders) {
            if (border.getLineStyle() != LineStyle.NONE) {
                // 确保边框可见性
                if (border.getColor().equals(Color.WHITE) ||
                        border.getColor().getAlpha() == 0 ||
                        border.getLineWidth() < 0.1) {

                    border.setColor(Color.BLACK);
                    border.setLineWidth(0.5);
                    border.setLineStyle(LineStyle.SINGLE);
                }
            }
        }
    }

    /**
     * 保持单元格合并设置
     */
    private static void preserveCellMerging(CellFormat cellFormat) {
        if (cellFormat.getHorizontalMerge() != CellMerge.NONE) {
            cellFormat.setHorizontalMerge(cellFormat.getHorizontalMerge());
        }
        if (cellFormat.getVerticalMerge() != CellMerge.NONE) {
            cellFormat.setVerticalMerge(cellFormat.getVerticalMerge());
        }
    }

    /**
     * 优化单元格内容
     */
    private static void optimizeCellContent(Cell cell) {
        for (Paragraph para : cell.getParagraphs()) {
            // 保持段落格式
            ParagraphFormat paraFormat = para.getParagraphFormat();
            paraFormat.setAlignment(paraFormat.getAlignment());
        }

        // 保持垂直对齐
        cell.getCellFormat().setVerticalAlignment(cell.getCellFormat().getVerticalAlignment());
    }

    /**
     * 配置PDF保存选项，优化转换质量
     */
    private static PdfSaveOptions configurePdfSaveOptions() {
        PdfSaveOptions options = new PdfSaveOptions();

        // 1. 高质量渲染
        options.setUseHighQualityRendering(true);

        // 2. 图像质量设置
        options.setJpegQuality(100);

        // 3. 字体设置
//        options.setEmbedFullFonts(true);
        options.setUseCoreFonts(true); // 改为false，使用嵌入字体

        // 4. 保留表单字段
        options.setPreserveFormFields(true);

        // 5. 优化PDF结构
        options.setOptimizeOutput(true);

        // 6. 设置PDF合规性（有助于边框渲染）
        options.setCompliance(PdfCompliance.PDF_17);

        // 7. 颜色空间设置（确保边框颜色正确）
        options.setColorMode(ColorMode.NORMAL);

        // 8. 关键：设置页面模式，有助于表格跨页处理
        options.setPageMode(PdfPageMode.USE_NONE);

        return options;
    }
}
