package com.bmos.lims2.server.inspect.order.utils;

import com.aspose.words.*;
import com.bmos.lims2.server.inspect.order.dto.InspectionOrderDTO;
import com.bmos.lims2.server.inspect.order.dto.InspectionSamplingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: 请验单 Aspose Words 生成器，将数据渲染为与设计图一致的版式并导出为PDF
 * @Author: yigaohui
 * @Date: 2025/09/17 10:30
 */
public class AsposeInspectionOrderPdfBuilder {

    private static final Logger log = LoggerFactory.getLogger(AsposeInspectionOrderPdfBuilder.class); 

    private static final String FONT_FALLBACK = "SimSun"; // 宋体，确保中文显示
    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    /**
     * 生成请验单 PDF
     * @param dto 检验单数据
     * @return PDF字节数组
     */
    public static byte[] buildPdf(InspectionOrderDTO dto) {
        try {
//            ensureLicense();

            Document doc = new Document();
            DocumentBuilder builder = new DocumentBuilder(doc);

            // 页面与字体基础设置
            PageSetup ps = builder.getPageSetup();
            ps.setPaperSize(PaperSize.A4);
            ps.setLeftMargin(36);   // 0.5 inch
            ps.setRightMargin(36);
            ps.setTopMargin(36);
            ps.setBottomMargin(36);

            builder.getFont().setName(FONT_FALLBACK);
            builder.getFont().setSize(12);

            // 标题
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
            builder.getFont().setBold(true);
            builder.getFont().setSize(16);
            builder.writeln("请验单");

            // 空行
            builder.getFont().setBold(false);
            builder.getFont().setSize(12);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.writeln("");

            // 四列表格：标签/值/标签/值
            Table table = builder.startTable();
            table.setAllowAutoFit(false);

            double tableWidth = doc.getFirstSection().getPageSetup().getPageWidth()
                    - ps.getLeftMargin() - ps.getRightMargin();
            // 4列宽度：标签列 18% + 值列 32% + 标签列 18% + 值列 32%
            double[] widths = new double[]{tableWidth * 0.18, tableWidth * 0.32, tableWidth * 0.18, tableWidth * 0.32};

            // 表格公共样式
            builder.getCellFormat().clearFormatting();
            builder.getCellFormat().getBorders().setLineStyle(LineStyle.SINGLE);
            builder.getCellFormat().getBorders().setColor(java.awt.Color.BLACK);
            builder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);

            // 1 行：检品信息 / materialName / 检品规格 / materialSpec
            writeRow(builder, widths,
                    new String[]{"检品信息", nz(dto.getMaterialName()), "检品规格", nz(dto.getMaterialSpec())});

            // 2 行：所属分类 / (留空或由前端补充) / 单位 / unitName
            writeRow(builder, widths,
                    new String[]{"所属分类", "", "单位", nz(dto.getUnitName())});

            // 3 行：批号 / batchNo / 检验单号 / orderNo
            writeRow(builder, widths,
                    new String[]{"批号", nz(dto.getBatchNo()), "检验单号", nz(dto.getOrderNo())});

            // 4 行：请验人 / requestUserName / 请验时间 / requestTime
            String reqTime = dto.getRequestTime() == null ? "" : DATE_FMT.format(dto.getRequestTime());
            writeRow(builder, widths,
                    new String[]{"请验人", nz(dto.getRequestUserName()), "请验时间", reqTime});

            // 5 行：分析项（左侧为标签，右侧合并三列为值）
            builder.insertCell();
            builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(widths[0]));
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.write("分析项");

            builder.insertCell();
            builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(widths[1] + widths[2] + widths[3]));
            builder.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.write(joinAnalyzeItems(dto.getSamplingList()));

            // 占位合并单元格（与上方单元格合并）
            builder.insertCell();
            builder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
            builder.insertCell();
            builder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
            builder.endRow();

            // 6 行：备注（左侧为标签，右侧合并三列为值）
            builder.insertCell();
            builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(widths[0]));
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.write("备注");

            builder.insertCell();
            builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(widths[1] + widths[2] + widths[3]));
            builder.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.write(nz(dto.getRemark()));

            // 占位合并
            builder.insertCell();
            builder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
            builder.insertCell();
            builder.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
            builder.endRow();

            builder.endTable();

            // 导出 PDF
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                PdfSaveOptions saveOptions = new PdfSaveOptions();
                saveOptions.setCompliance(PdfCompliance.PDF_A_1_B); // 提高兼容性
                doc.save(out, saveOptions);
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error("请验单 PDF 生成失败", e);
            throw new RuntimeException("请验单 PDF 生成失败: " + e.getMessage(), e);
        }
    }

    private static void writeRow(DocumentBuilder builder, double[] widths, String[] texts) throws Exception {
        for (int i = 0; i < 4; i++) {
            builder.insertCell();
            builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(widths[i]));
            builder.getParagraphFormat().setAlignment(i % 2 == 0 ? ParagraphAlignment.LEFT : ParagraphAlignment.LEFT);
            builder.write(texts[i] == null ? "" : texts[i]);
            if (i == 3) {
                builder.endRow();
            }
        }
    }

    private static String joinAnalyzeItems(List<InspectionSamplingDTO> samplingList) {
        if (samplingList == null || samplingList.isEmpty()) {
            return "";
        }
        // 去重并保持顺序
        Set<String> items = new LinkedHashSet<>();
        for (InspectionSamplingDTO s : samplingList) {
            if (s != null && s.getInspectItemName() != null) {
                items.add(s.getInspectItemName());
            }
        }
        return items.stream().collect(Collectors.joining("、"));
    }

    private static String nz(String s) { return s == null ? "" : s; }

    private static void ensureLicense() {
        try {
            InputStream is = AsposeInspectionOrderPdfBuilder.class.getClassLoader().getResourceAsStream("license.xml");
            if (is != null) {
                License license = new License();
                license.setLicense(is);
            }
        } catch (Exception e) {
            // 忽略license设置失败，Aspose会出现水印，但不阻断功能
            log.warn("Aspose license 设置失败: {}", e.getMessage());
        }
    }
}


