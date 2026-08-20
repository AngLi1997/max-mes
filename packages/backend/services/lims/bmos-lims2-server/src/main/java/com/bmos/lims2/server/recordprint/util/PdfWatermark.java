package com.bmos.lims2.server.recordprint.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

import java.io.File;
import java.io.OutputStream;

/**
 * @Description: PDF水印工具（参考随机分布水印写法）
 * @Author: yigaohui
 * @Date: 2025/11/25 11:00
 */
public final class PdfWatermark {

    private PdfWatermark() {}

    public static void addRandomWatermarks(String inputPdfPath,
                                           OutputStream outputStream,
                                           String watermarkText,
                                           String fontTtfPath,
                                           int rows,
                                           int cols) {
        try (PDDocument document = PDDocument.load(new File(inputPdfPath))) {
            PDType0Font font = PDType0Font.load(document, new File(fontTtfPath));

            for (PDPage page : document.getPages()) {
                PDRectangle mediaBox = page.getMediaBox();
                float pageWidth = mediaBox.getWidth();
                float pageHeight = mediaBox.getHeight();

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true, true)) {

                    // 半透明设置
                    PDExtendedGraphicsState graphicsState = new PDExtendedGraphicsState();
                    graphicsState.setNonStrokingAlphaConstant(0.15f);
                    graphicsState.setAlphaSourceFlag(true);
                    contentStream.setGraphicsStateParameters(graphicsState);

                    contentStream.beginText();
                    contentStream.setFont(font, 28);

                    float xGap = pageWidth / (cols + 1);
                    float yGap = pageHeight / (rows + 1);

                    for (int r = 1; r <= rows; r++) {
                        for (int c = 1; c <= cols; c++) {
                            float x = c * xGap;
                            float y = r * yGap;
                            // 旋转一定角度
                            contentStream.setTextMatrix(org.apache.pdfbox.util.Matrix.getRotateInstance(Math.toRadians(30), x, y));
                            contentStream.showText(watermarkText);
                        }
                    }
                    contentStream.endText();
                }
            }
            document.save(outputStream);
        } catch (Exception e) {
            throw new RuntimeException("PDF添加水印失败", e);
        }
    }
}


