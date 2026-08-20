package com.bmos.mes.service.dataset.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 图片下载工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/22 10:55
 */
public class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    private static final String LOG_PREFIX = "[ImageUtil]";

    public static byte[] downloadImage(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)){
            log.info("{}图片地址为空 取消下载", LOG_PREFIX);
            return new byte[0];
        }
        if (!StringUtils.startsWith(imageUrl, "http://") && !StringUtils.startsWith(imageUrl, "https://")){
            log.info("{}图片地址[{}]格式不正确 取消下载", imageUrl, LOG_PREFIX);
            return new byte[0];
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error(LOG_PREFIX + "图片下载失败", e);
            return new byte[0];
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 多线程下载图片
     *
     * @param urls urls
     * @return 图片map url -> 图片byte[]
     */
    public static @NotNull Map<String, byte[]> downloadAllPictures(Collection<String> urls) {
        Map<String, byte[]> imageMap = new HashMap<>();
        Map<String, CompletableFuture<byte[]>> futures = new HashMap<>();
        for (String imageUrl : urls) {
            CompletableFuture<byte[]> completableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return ImageUtil.downloadImage(imageUrl);
                } catch (Exception e) {
                    return new byte[0];
                }
            });
            futures.put(imageUrl, completableFuture);
        }
        CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0])).join();
        for (Map.Entry<String, CompletableFuture<byte[]>> entry : futures.entrySet()) {
            try {
                byte[] bytes = entry.getValue().get();
                if (bytes != null) {
                    imageMap.put(entry.getKey(), bytes);
                }
            } catch (Exception e) {
                log.error(LOG_PREFIX + "图片下载失败", e);
            }
        }
        return imageMap;
    }

    /**
     * 图片根据宽高自适应
     * @param bufferedImage
     * @param documentWidth
     * @param documentHeight
     * @return
     */
    public static Dimension getDimensionAuto(BufferedImage bufferedImage, double documentWidth, double documentHeight){
        if (documentWidth < 0 || documentHeight < 0){
            return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        }
        // 获取图像的宽度和高度
        double imageWidth = bufferedImage.getWidth();
        double imageHeight = bufferedImage.getHeight();

        // 计算图像的宽高比
        double imageAspectRatio = imageWidth / imageHeight;

        // 计算文档的宽高比
        double documentAspectRatio = documentWidth / documentHeight;

        // 用于存储等比例缩放后的宽高
        double finalWidth, finalHeight;

        // 如果图像的宽高比大于文档的宽高比，则按照宽度缩放，否则按照高度缩放
        if (imageAspectRatio > documentAspectRatio) {
            // 按照文档宽度缩放
            finalWidth = documentWidth;
            finalHeight = documentWidth / imageAspectRatio;
        } else {
            // 按照文档高度缩放
            finalHeight = documentHeight;
            finalWidth = documentHeight * imageAspectRatio;
        }
        // 返回一个 Dimension 对象，包含等比例缩放后的宽度和高度
        return new Dimension((int) finalWidth, (int) finalHeight);
    }
}
