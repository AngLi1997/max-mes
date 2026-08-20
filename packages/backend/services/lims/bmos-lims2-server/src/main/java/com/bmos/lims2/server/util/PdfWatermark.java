package com.bmos.lims2.server.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class PdfWatermark {

    /**
     * 为pdf添加水印
     * @param inputPdfPath
     * @param outputStream
     * @param watermarkText: 水印内容
     * @param ttfPath: 字体路径
     * @throws IOException
     */
    public static void addRandomWatermarks(String inputPdfPath, OutputStream outputStream, String watermarkText, String ttfPath, int rows, int cols) throws IOException {
        // 加载输入 PDF 文档
        PDDocument document = PDDocument.load(new File(inputPdfPath));
        PDType0Font font = PDType0Font.load(document, new File(ttfPath));
        float opacity = 0.4f; // 水印透明度
        Color color = Color.LIGHT_GRAY;
        for (PDPage page : document.getPages()) {
            PDRectangle mediaBox = page.getMediaBox();
            float pageWidth = mediaBox.getWidth();
            float pageHeight = mediaBox.getHeight();

            try (PDPageContentStream cs = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                // 设置透明度和颜色
                PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
                gs.setNonStrokingAlphaConstant(opacity);
                cs.setGraphicsStateParameters(gs);
                cs.setNonStrokingColor(color);

                // 计算网格尺寸
                float gridWidth = pageWidth / cols;
                float gridHeight = pageHeight / rows;

                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        // 计算当前网格中心坐标
                        float x = col * gridWidth + gridWidth / 2;
                        float y = pageHeight - (row * gridHeight + gridHeight / 2);

                        // 自动计算字体大小（网格宽高中的较小值的70%）
                        float fontSize = Math.min(gridWidth, gridHeight) * 0.2f;
                        fontSize = Math.max(fontSize, 4); // 最小字号4pt

                        // 计算文本尺寸
                        float textWidth = font.getStringWidth(watermarkText) / 1000 * fontSize;
                        float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

                        // 设置水印变换矩阵
                        cs.saveGraphicsState();
                        Matrix matrix = Matrix.getTranslateInstance(x, y);
                        matrix.concatenate(Matrix.getRotateInstance(Math.toRadians(30), x, y));
                        cs.transform(matrix);
                        // 绘制水印文本
                        cs.beginText();
                        cs.setFont(font, fontSize);
                        cs.newLineAtOffset(-textWidth / 2, -textHeight / 2); // 文本居中
                        cs.showText(watermarkText);
                        cs.endText();
                        cs.restoreGraphicsState();
                    }
                }
            }
        }

        // 保存并关闭 PDF 文档
        document.save(outputStream);
        document.close();
    }

}
