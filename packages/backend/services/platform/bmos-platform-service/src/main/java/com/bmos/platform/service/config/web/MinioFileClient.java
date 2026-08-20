package com.bmos.platform.service.config.web;

import com.bmos.common.exception.BmosException;
import com.bmos.platform.facade.system.user.constants.PlatformMinioBucket;
import com.bmos.platform.common.exception.PlatformResponseCode;
import io.minio.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;

/**
 * minio客户端
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/13 17:28
 */
@Component
public class MinioFileClient {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinioProperties minioProperties;

    /**
     * 上传文件
     *
     * @param bucket 桶
     * @param file   文件
     * @return 文件地址
     */
    public String uploadFile(PlatformMinioBucket bucket, File file) throws Exception {
//        UploadObjectArgs objectArgs =
//                UploadObjectArgs.builder()
//                        .bucket(minioProperties.getBuckets().getArchive())
//                        .object(filePath+"/"+file.getName())
//                        .filename(file.getAbsolutePath())
//                        .build();
//        ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(objectArgs);
        return "";
    }

    /**
     * 上传文件
     *
     * @param bucket     桶
     * @param file       文件
     * @param uploadPath 上传路径
     * @return 文件地址
     */
    public String uploadFile(PlatformMinioBucket bucket, File file, String uploadPath) throws Exception {
        UploadObjectArgs objectArgs =
                UploadObjectArgs.builder()
                        .bucket(getBucketName(bucket))
                        .object(uploadPath)
                        .filename(file.getAbsolutePath())
                        .build();
        ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(objectArgs);
        return objectWriteResponse.object();
    }

    public String getBucketName(PlatformMinioBucket bucket) {
        switch (bucket) {
            case USER_SIGN:
                return minioProperties.getBuckets().getSign();
        }
        throw new IllegalArgumentException("请传入正确的minio桶参数");
    }

    /**
     * 文件删除
     */
    public void delete(PlatformMinioBucket bucket, String path) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(getBucketName(bucket))
                .object(path)
                .build());
    }

    /**
     * 文件下载
     *
     * @param path
     * @param response
     * @throws Exception
     */
    public void download(PlatformMinioBucket bucket, String path, HttpServletResponse response) throws Exception {
        try (OutputStream outputStream = response.getOutputStream()) {
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(getBucketName(bucket))
                    .object(path)
                    .build());
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = object.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // 刷新输出流
            outputStream.flush();
            // 关闭流
            object.close();
            //IoUtil.copy(object, response.getOutputStream());
        } catch (Exception e) {
            throw new BmosException(PlatformResponseCode.MINIO_DOWNLOAD_FILE_FAIL);
        }

    }
}
