package config;

import filter.DruidSqlLogFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {
    /**
     * SQL 日志格式化
     * @return DruidSqlLogFilter
     */
    @Bean
    public DruidSqlLogFilter sqlLogFilter() {
        return new DruidSqlLogFilter();
    }
}
