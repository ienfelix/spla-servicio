package com.grupogloria.splaservicio.Comun;

import java.sql.Connection;

import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import ch.qos.logback.classic.Logger;

public class Conexion
{
    private DriverManagerDataSource driverManagerDataSource;
    private Logger _logger = (Logger) LoggerFactory.getLogger(Conexion.class);

    public Conexion()
    {
        try
        {
            driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(Constante.DATABASE_DRIVER);
            driverManagerDataSource.setUrl(Constante.DATABASE_URL);
            driverManagerDataSource.setUsername(Constante.DATABASE_USERNAME);
            driverManagerDataSource.setPassword(Constante.DATABASE_PASSWORD);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
    }
    
    public Connection ObtenerConexion() throws Exception
    {
        Connection con = null;
        try
        {
            con = driverManagerDataSource.getConnection();
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return con;
    }
}
