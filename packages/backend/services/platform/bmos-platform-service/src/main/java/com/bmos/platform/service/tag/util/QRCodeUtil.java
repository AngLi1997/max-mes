package com.bmos.platform.service.tag.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * 二维码生成器
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/26 17:03
 */
public class QRCodeUtil {

    /**
     * 生成二维码base64字符串
     *
     * @param content 内容
     * @param width   宽
     * @param height  高
     * @return base64字符串
     */
    public static String crateQRCodeBase64(String content, BarcodeFormat barcodeFormat, int width, int height) {
        String resultImage;
        if (!StrUtil.isEmpty(content)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            @SuppressWarnings("rawtypes")
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {

                Writer writer;
                if (BarcodeFormat.CODE_128 == barcodeFormat) {
                    writer = new com.google.zxing.oned.Code128Writer();
                }else {
                    writer = new QRCodeWriter();
                }
                BitMatrix bitMatrix = writer.encode(content, barcodeFormat, width, height, hints);
                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                resultImage = "data:image/png;base64," + Base64.encode(os.toByteArray());
                return resultImage;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
