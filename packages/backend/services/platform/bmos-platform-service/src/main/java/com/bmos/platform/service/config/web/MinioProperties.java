package com.bmos.platform.service.config.web;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String bucket;

    private MinioBucketsProperties buckets;

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "minio.buckets")
    public static class MinioBucketsProperties{

        /**
         * 签名bucket
         */
        private String sign;
    }
}
