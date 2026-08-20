package com.bmos.lims2.server.report.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * 上传文件至对象存储
     * @param bucket bucket
     * @param objectKey 对象键
     * @param file 文件
     */
    void upload(String bucket, String objectKey, MultipartFile file);

    /**
     * 获取对象存储文件流
     * @param bucket bucket
     * @param objectKey 对象键
     * @return 输入流
     */
    java.io.InputStream getObject(String bucket, String objectKey);

    /**
     * 上传输入流
     */
    void upload(String bucket, String objectKey, java.io.InputStream inputStream, long size, String contentType);
}


