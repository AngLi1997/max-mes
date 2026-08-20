package com.bmos.platform.service.tag.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.bmos.platform.service.tag.dto.TagInstanceField;
import com.bmos.platform.service.tag.enums.PrinterDpi;
import com.google.zxing.BarcodeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/26 16:34
 */
public class TagUtil {

    private static final Logger log = LoggerFactory.getLogger(TagUtil.class);

    private static final String PLUGIN_NAME = "html-to-image-plugin";
    private static final String PLUGIN_CLASS_NAME = "com.liang.HtmlToImageUtil";
    private static final String PLUGIN_CLASS_METHOD_NAME = "convertToImage";

    /**
     * 渲染实例二维码
     *
     * @param htmlTemplate  html模板
     * @param barcodeFormat
     * @param barCode       示例二维码数据
     * @param width         标签宽度
     * @param height        标签高度
     * @return
     */
    public static String renderHtmlBarCodeOnly(String htmlTemplate, BarcodeFormat barcodeFormat, String barCode, int width, int height) {
        Document document = Jsoup.parse(htmlTemplate);
        Element tag = document.getElementById("tag");
        if (tag != null) {
            System.out.println("开始渲染标签宽高...");
            String style = tag.attr("style");
            if (StrUtil.isNotBlank(style) && !style.endsWith(";")) {
                style += ";";
            }
            style += "width: " + width * PrinterDpi.DPI_203.getDpiPoint() + "px; height: " + height * PrinterDpi.DPI_203.getDpiPoint() + "px;";
            tag.attr("style", style);
        }
        // 处理二维码
        String base64 = QRCodeUtil.crateQRCodeBase64(barCode, barcodeFormat == null ? BarcodeFormat.QR_CODE : barcodeFormat, 200, 200);
        Element qrCodeImg = document.getElementById("qrCode");
        if (qrCodeImg != null && base64 != null) {
            qrCodeImg.attr("src", base64);
        }
        return document.toString();
    }


    /**
     * 渲染html模板
     *
     * @param htmlTemplate html模板
     * @param params       渲染参数
     * @param fieldMap     模板和数据接口字段映射
     * @param width        标签宽度
     * @param height       标签高度
     * @param dpi          dpi
     * @return
     */
    public static File renderHtmlFile(String htmlTemplate,BarcodeFormat barcodeFormat, JSONObject params, Map<String, TagInstanceField> fieldMap, String qrCodeField, String qrCodePrefix, int width, int height, PrinterDpi dpi) {
        Document document = Jsoup.parse(htmlTemplate);
        Element tag = document.getElementById("tag");
        if (tag != null) {
            System.out.println("开始渲染标签宽高...");
            String style = tag.attr("style");
            if (StrUtil.isNotBlank(style) && !style.endsWith(";")) {
                style += ";";
            }
            style += "width: " + width * dpi.getDpiPoint() + "px; height: " + height * dpi.getDpiPoint() + "px;";
            tag.attr("style", style);
        }
        System.out.println("开始渲染标签内容...");
        fieldMap.forEach((k, v) -> {
            Element element = document.getElementById(k);
            if (element != null) {
                String label = v.getLabel();
                String str = params.getStr(v.getDataSourceField());
                if (StrUtil.isNotBlank(str)) {
                    element.text((label +" "+ str.trim()));
                }
            }
        });

        String qrCode = params.getStr(qrCodeField);
        String base64 = QRCodeUtil.crateQRCodeBase64(StrUtil.isBlank(qrCode) ? "" : qrCodePrefix + qrCode, barcodeFormat==null?BarcodeFormat.QR_CODE:barcodeFormat,200, 200);
        Element qrCodeImg = document.getElementById("qrCode");
        if (qrCodeImg != null && base64 != null) {
            qrCodeImg.attr("src", base64);
        }
        // 一维码需要将内容也放到特定区域
        if (barcodeFormat==BarcodeFormat.CODE_128){
            Element qrCodeText = document.getElementById("qrCodeString");
            if (qrCodeText != null && StrUtil.isNotBlank(qrCode)) {
                qrCodeText.text(qrCode);
            }
        }
        System.out.println("正在生成html文件...");
        try {
            File file = File.createTempFile(UUID.randomUUID().toString(), ".html");
            document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            FileUtil.writeUtf8String(document.toString(), file);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * F
     * 查看预览图片
     *
     * @param inputHtml 输入html
     * @param width
     * @param height
     * @param dpi
     * @return
     */
    public static File getPreviewPicture(File inputHtml, int width, int height, PrinterDpi dpi) {
        try {
            // 由于antlr4版本冲突 使用插件的形式引入进来
//            Class<?> aClass = PluginHelper.loadClass(PLUGIN_NAME, PLUGIN_CLASS_NAME);
//            Method convertToImage = aClass.getDeclaredMethod(PLUGIN_CLASS_METHOD_NAME, File.class, int.class, int.class, String.class);
//            return  (File)convertToImage.invoke(null, inputHtml, width * dpi.getDpiPoint(), height * dpi.getDpiPoint(), ".jpg");
            return HtmlToImageConverter.convertHtmlToLabel(inputHtml, dpi,width, height);
        }catch (Exception e){
            log.error("生成预览图片失败", e);
            return null;
        }
    }
}
