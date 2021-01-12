package com.grupogloria.splaservicio.Comun;

import java.sql.Connection;

import com.grupogloria.splaservicio.Modelo.ConexionMO;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Conexion
{
    private DriverManagerDataSource driverManagerDataSource;

    public Conexion() throws Exception
    {
        try
        {
            ConexionMO conexionMO = Util.ObtenerConexion();
            driverManagerDataSource = new DriverManagerDataSource();
            driverManagerDataSource.setDriverClassName(conexionMO.getDriver());
            driverManagerDataSource.setUrl(conexionMO.getUrl());
            driverManagerDataSource.setUsername(conexionMO.getUsername());
            driverManagerDataSource.setPassword(conexionMO.getPassword());
        }
        catch (Exception e)
        {
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
            throw e;
        }
        return con;
    }
}
