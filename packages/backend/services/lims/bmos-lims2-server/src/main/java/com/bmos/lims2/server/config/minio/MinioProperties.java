package com.bmos.lims2.server.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

        private String reportTemplate;

        private String reportVersion;

        private String methodTemplate;

        private String operateRuleSop;

        private String record;

        private String sign;

        private String platformRecord;
    }
}


