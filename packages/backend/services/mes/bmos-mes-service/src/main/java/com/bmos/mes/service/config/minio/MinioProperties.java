package com.bmos.mes.service.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private MinioBucketsProperties buckets;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "minio.buckets")
    public static class MinioBucketsProperties{

        /**
         * 归档 bucket
         */
        private String archive;

        /**
         * 批记录 bucket
         */
        private String record;

        /**
         * sop bucket
         */
        private String sop;

        /**
         * 生产附件 bucket
         */
        private String product;

        /**
         * 批记录模板 bucket
         */
        private String batchRecordTemplate;

        /**
         * 批签发模板 bucket
         */
        private String lotRelease;

        /**
         * 签名 bucket
         */
        private String sign;
    }
}
