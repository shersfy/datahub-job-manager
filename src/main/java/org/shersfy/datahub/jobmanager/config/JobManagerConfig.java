package org.shersfy.datahub.jobmanager.config;

import javax.sql.DataSource;

import org.shersfy.datahub.jobmanager.i18n.I18nMessages;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobManagerConfig {
    
    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource() {
        DataSource ds = DataSourceBuilder.create().build();
        return ds;
    }
    
    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "i18n")
    public I18nMessages i18n() {
        I18nMessages i18n = new I18nMessages();
        return i18n;
    }

}
