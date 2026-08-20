package com.bmos.lims2.server.report.service.impl;

import com.bmos.lims2.server.report.service.StorageService;
import io.minio.MinioClient;
import io.minio.GetObjectArgs;
import io.minio.PutObjectArgs;
import io.minio.UploadObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class MinioStorageServiceImpl implements StorageService {

    private final MinioClient minioClient;

    @Override
    public void upload(String bucket, String objectKey, MultipartFile file) {
        try {
            File tempFile = File.createTempFile(objectKey, null);
            file.transferTo(tempFile);
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .contentType(file.getContentType())
                            .filename(tempFile.getAbsolutePath())
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public java.io.InputStream getObject(String bucket, String objectKey) {
        try {
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(objectKey).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void upload(String bucket, String objectKey, java.io.InputStream inputStream, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .contentType(contentType)
                            .stream(inputStream, size, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


