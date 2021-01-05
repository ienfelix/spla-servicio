package com.grupogloria.splaservicio.Modelo;

import com.grupogloria.splaservicio.Comun.Constante;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = { "com.grupogloria.*" })
@ConfigurationProperties(prefix = Constante.DATABASE_PREFIX)
@PropertySource("classpath:application.properties")
public class ConexionMO
{
    @Value("${database.Url}")
    private String Url;
    private String Username;
    private String Password;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}