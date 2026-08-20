package com.bmos.platform.service.tag.util;

import com.bmos.platform.service.tag.enums.PrinterDpi;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.UUID;

/**
 * HTML转图片工具类，支持精确物理尺寸输出
 *
 * @author yigaohui
 * @date 2025/7/22 11:26
 */
@Slf4j
public class HtmlToImageConverter {

    /**
     * 将HTML转换为图片
     *
     * @param htmlContent HTML内容
     * @param width       输出图片宽度（像素）
     * @param height      输出图片高度（像素）
     * @param outputPath  输出图片路径（.jpg格式）
     */
    public static void convertHtmlToImage(String htmlContent, int dpi,int width, int height, String outputPath) {
        try {
            // 创建一个临时HTML文件
            File tempHtmlFile = createTempHtmlFile(htmlContent);

            // 使用Java2DRenderer渲染HTML
            Java2DRenderer renderer = new Java2DRenderer(tempHtmlFile, width, height);

            // 设置渲染质量
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setDPI(dpi); // 设置DPI为打印机的DPI
            sharedContext.setDotsPerPixel(1);

            // 渲染图片
            BufferedImage image = renderer.getImage();

            // 创建最终图片
            BufferedImage finalImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = finalImage.createGraphics();

            // 设置渲染质量
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // 设置白色背景
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);

            // 绘制图片
            g2d.drawImage(image, 0, 0, width, height, null);
            g2d.dispose();

            // 保存高质量图片
            saveHighQualityImage(finalImage, outputPath);

            // 清理临时文件
            tempHtmlFile.delete();

        } catch (Exception e) {
            throw new RuntimeException("Failed to convert HTML to image", e);
        }
    }

    /**
     * 创建临时HTML文件
     */
    private static File createTempHtmlFile(String htmlContent) throws IOException {
        File tempFile = File.createTempFile("html2image", ".html");
        tempFile.deleteOnExit();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8)) {
            writer.write(htmlContent);
        }

        return tempFile;
    }

    /**
     * 使用高质量设置保存JPEG图片
     */
    private static void saveHighQualityImage(BufferedImage image, String outputPath) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("No JPEG image writers found");
        }

        ImageWriter writer = writers.next();
        try (ImageOutputStream output = ImageIO.createImageOutputStream(new File(outputPath))) {
            writer.setOutput(output);

            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionQuality(1.0f); // 最高质量

            writer.write(null, new IIOImage(image, null, null), params);
        } finally {
            writer.dispose();
        }
    }

    /**
     * 读取文件内容为字符串
     */
    private static String readFileContent(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    /**
     * 将HTML转换为标签图片（适配指定DPI）
     *
     * @param htmlFile   HTML内容
     * @param dpi        目标DPI
     */
    public static File convertHtmlToLabel(File htmlFile, PrinterDpi dpi, int width, int height) throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
        String htmlContent = readFileContent(htmlFile);
        int widthPx = width * dpi.getDpiPoint();  // 80mm
        int heightPx = height * dpi.getDpiPoint(); // 100mm

        convertHtmlToImage(htmlContent, dpi.getDpi(),widthPx, heightPx, file.getAbsolutePath());
        return file;
    }

//    public static void main(String[] args) {
//        try {
//            File htmlFile = new File("D:\\test\\test.html");
//            // 目标物理尺寸：80mm x 100mm
//            // HTML像素尺寸：640px x 800px
//            File file = convertHtmlToLabel(htmlFile, PrinterDpi.DPI_203, 80, 100);
//            log.info("Image generated successfully: {}", file.getAbsolutePath());
//        } catch (Exception e) {
//            log.error("Failed to convert HTML to image", e);
//        }
//    }
}