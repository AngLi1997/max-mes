package com.bmos.platform.service.config.web;

import cn.hutool.core.util.ReflectUtil;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO
 */
@Getter
@Setter
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
@Slf4j
public class MinioConfig {
    @Bean
    @ConditionalOnMissingBean
    public MinioProperties minioProperties() {
        return new MinioProperties();
    }

    @Bean
    public MinioClient minioClient() {
        MinioProperties miniioProperties = minioProperties();
        // 创建 MinioClient 客户端
        MinioClient minioClient = MinioClient.builder()
                .endpoint(miniioProperties.getEndpoint())
                .credentials(miniioProperties.getAccessKey(), miniioProperties.getSecretKey())
                .build();
        if (miniioProperties.getBuckets() != null) {
            this.createBucket(minioClient, miniioProperties.getBuckets());
        }
        return minioClient;
    }

    private void createBucket(MinioClient minioClient, MinioProperties.MinioBucketsProperties buckets) {
        // 反射获取buckets 所有属性，每个属性都是一个桶名
        Object[] fieldsValue = ReflectUtil.getFieldsValue(buckets);
        if (fieldsValue == null) {
            return;
        }
        try {
            for (Object o : fieldsValue) {
                if (o instanceof String) {
                    String bucket = (String) o;
                    if (bucket.isEmpty()) {
                        continue;
                    }
                    if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                        log.info("createBucket {} success!", bucket);
                    }
                }
            }
        } catch (Exception e) {
            log.error("minio 桶创建失败");
            throw new RuntimeException("minio桶创建失败", e);
        }
    }
}
