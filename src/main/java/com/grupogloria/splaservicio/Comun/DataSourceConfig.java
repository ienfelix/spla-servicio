package com.grupogloria.splaservicio.Comun;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig
{
    @Bean
    public DataSource getDataSource()
    {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(Constante.DATABASE_DRIVER);
        driverManagerDataSource.setUrl(Constante.DATABASE_URL);
        driverManagerDataSource.setUsername(Constante.DATABASE_USERNAME);
        driverManagerDataSource.setPassword(Constante.DATABASE_PASSWORD);
        return driverManagerDataSource;
    }
}
