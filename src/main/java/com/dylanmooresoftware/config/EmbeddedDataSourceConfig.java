package com.dylanmooresoftware.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Configuration
@EnableTransactionManagement
class EmbeddedDataSourceConfig implements TransactionManagementConfigurer {
  @Value("${dataSource.driverClassName}")
  private String driver;
  @Value("${dataSource.url}")
  private String url;
  @Value("${dataSource.username}")
  private String username;
  @Value("${dataSource.password}")
  private String password;

  @Bean
  public JdbcTemplate configureJdbcTemplate() {
    return new JdbcTemplate(configureDataSource());
  }
  
  @Bean
  public DataSource configureDataSource() {
    final EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.HSQL)
        .addScript("customers.sql").build();

    return embeddedDatabase;
  }
  
  @Bean
  public PlatformTransactionManager configureTxManager() {
    return new DataSourceTransactionManager(configureDataSource());
  }

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager() {
    return configureTxManager();
  }

}
