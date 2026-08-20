package com.bmos.lims2.server.config.minio;

import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * minio客户端
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/13 17:28
 */
@Component
@Slf4j
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
    public String uploadFile(MinioBucket bucket, File file) throws Exception {
        UploadObjectArgs objectArgs =
                UploadObjectArgs.builder()
                        .bucket(getBucketName(bucket))
                        .object(file.getName())
                        .filename(file.getAbsolutePath())
                        .build();
        ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(objectArgs);
        return objectWriteResponse.object();
    }

    /**
     * 上传文件
     *
     * @param bucket     桶
     * @param file       文件
     * @param uploadPath 上传路径
     * @return 文件地址
     */
    public String uploadFile(MinioBucket bucket, File file, String uploadPath) throws Exception {
        UploadObjectArgs objectArgs =
                UploadObjectArgs.builder()
                        .bucket(getBucketName(bucket))
                        .object(uploadPath)
                        .filename(file.getAbsolutePath())
                        .build();
        ObjectWriteResponse objectWriteResponse = minioClient.uploadObject(objectArgs);
        return objectWriteResponse.object();
    }

    public String getBucketName(MinioBucket bucket) {
        switch (bucket) {
            case METHOD_TEMPLATE_BUCKET:
                return minioProperties.getBuckets().getMethodTemplate();
            case OPERATE_RULE_SOP:
                return minioProperties.getBuckets().getOperateRuleSop();
            case ELN_RECORD:
                return minioProperties.getBuckets().getRecord();

        }
        throw new IllegalArgumentException("请传入正确的minio桶参数");
    }

    /**
     * 文件删除
     */
    public void delete(MinioBucket bucket, String path) throws Exception {
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
    public void download(MinioBucket bucket, String path, HttpServletResponse response) throws Exception {
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
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.FILE_DOWNLOAD_FAILED);
        }
    }

    public void downLoadFile(MinioBucket bucket, String path, OutputStream outputStream) throws Exception {
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.FILE_DOWNLOAD_FAILED);
        }
    }

    public String downLoadFileToTemp(MinioBucket bucket, String path, String suffix) throws Exception {
        try {
            GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(getBucketName(bucket))
                    .object(path)
                    .build());
            // 创建临时文件
            Path tempFile = Files.createTempFile("temp_pdf", suffix);
            FileOutputStream fos = new FileOutputStream(tempFile.toFile());
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = object.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            // 刷新输出流
            fos.flush();
            return tempFile.toAbsolutePath().toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new BmosException(LimsResponseCode.FILE_DOWNLOAD_FAILED);
        }
    }

    /**
     * 删除同种的文件
     * @param minioBucket： {@link MinioBucket}
     * @param fileName
     */
    public void removeFile(MinioBucket minioBucket, String fileName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(getBucketName(minioBucket))
                .object(fileName)
                .build());
    }

    /**
     * 批量删除桶中的文件
     * @param minioBucket
     * @param fileNames
     * @throws Exception
     * @return List 未被删除的文件
     */
    public Set<String> removeFiles(MinioBucket minioBucket, Collection<String> fileNames){
        Set<String> notDeleteList = new HashSet<>();
        for (String fileName : fileNames) {
            try{
                removeFile(minioBucket, fileName);
            } catch (Exception e){
                log.error("删除文件失败, path={}", fileName, e);
                notDeleteList.add(fileName);
            }
        }
        return notDeleteList;
    }
}
