package com.bmos.mes.service.config.sharding;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/12 11:42
 */
@Configuration
@Data
public class ShardingJdbcConfiguration {

    @Value("${db.host}")
    private String dbHost;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${db.schema}")
    private String dbSchema;

    private static final String SHARDING_JDBC_JDBC_URL = "SHARDING_JDBC_JDBC_URL";
    private static final String SHARDING_JDBC_USERNAME = "SHARDING_JDBC_USERNAME";
    private static final String SHARDING_JDBC_PASSWORD = "SHARDING_JDBC_PASSWORD";

    @Bean
    @FlywayDataSource
    public DataSource shardingDatasource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
        config.setJdbcUrl("jdbc:shardingsphere:classpath:sharding.yaml?placeholder-type=system_props");
        try {
            System.setProperty(SHARDING_JDBC_JDBC_URL, "jdbc:mysql://" + dbHost + "/" + dbSchema + "?useUnicode=true&characterEncoding=UTF8&autoReconnect=true&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8&rewriteBatchedStatements=true");
            System.setProperty(SHARDING_JDBC_USERNAME, dbUsername);
            System.setProperty(SHARDING_JDBC_PASSWORD, dbPassword);
            return new HikariDataSource(config);
        } finally {
            System.clearProperty(SHARDING_JDBC_JDBC_URL);
            System.clearProperty(SHARDING_JDBC_USERNAME);
            System.clearProperty(SHARDING_JDBC_PASSWORD);
        }
    }
}
