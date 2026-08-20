package com.bmos.lims2.common.util;

import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;

/**
 * HTML转图片工具类（使用wkhtmltoimage）
 */
@Slf4j
public class HtmlToImageUtil {
    
    /**
     * wkhtmltoimage可执行文件路径
     */
    private static final String WKHTMLTOIMAGE_PATH = "C:/Program Files/wkhtmltopdf/bin/wkhtmltoimage.exe";  // Windows路径
    // private static final String WKHTMLTOIMAGE_PATH = "/usr/local/bin/wkhtmltoimage";  // Linux路径

    /**
     * 将HTML文件转换为JPG图片
     *
     * @param htmlFile HTML文件路径
     * @param jpgFile 输出JPG文件路径
     * @param width 图片宽度（像素）
     */
    public static void convertToJpg(String htmlFile, String jpgFile, int width) {
        try {
            // 构建命令
            ProcessBuilder builder = new ProcessBuilder(
                WKHTMLTOIMAGE_PATH,
                "--quality", "90",
                "--width", String.valueOf(width),
                "--format", "jpg",
                htmlFile,
                jpgFile
            );

            // 执行命令
            Process process = builder.start();
            
            // 等待进程完成
            int exitCode = process.waitFor();
            
            // 检查执行结果
            if (exitCode != 0) {
                throw new RuntimeException("wkhtmltoimage process failed with exit code: " + exitCode);
            }
            
        } catch (IOException | InterruptedException e) {
            log.error("Convert HTML to JPG failed", e);
            throw new RuntimeException("Convert HTML to JPG failed", e);
        }
    }

    /**
     * 将HTML文件转换为JPG图片（带更多选项）
     *
     * @param htmlFile HTML文件路径
     * @param jpgFile 输出JPG文件路径
     * @param width 图片宽度（像素）
     * @param quality 图片质量（0-100）
     * @param zoom 缩放比例（1.0表示100%）
     */
    public static void convertToJpg(String htmlFile, String jpgFile, int width, int quality, double zoom) {
        try {
            // 构建命令
            ProcessBuilder builder = new ProcessBuilder(
                WKHTMLTOIMAGE_PATH,
                "--quality", String.valueOf(quality),
                "--width", String.valueOf(width),
                "--format", "jpg",
                "--zoom", String.valueOf(zoom),
                "--disable-smart-width",  // 禁用智能宽度
                "--enable-local-file-access",  // 允许访问本地文件
                htmlFile,
                jpgFile
            );

            // 执行命令
            Process process = builder.start();
            
            // 等待进程完成
            int exitCode = process.waitFor();
            
            // 检查执行结果
            if (exitCode != 0) {
                throw new RuntimeException("wkhtmltoimage process failed with exit code: " + exitCode);
            }
            
        } catch (IOException | InterruptedException e) {
            log.error("Convert HTML to JPG failed", e);
            throw new RuntimeException("Convert HTML to JPG failed", e);
        }
    }
} 