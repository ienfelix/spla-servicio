package com.grupogloria.splaservicio.Comun;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Modelo.ConexionMO;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class Util
{
    public Util() throws Exception
    {
    }
    
    public static CumploMO ObtenerCumplo() throws Exception
    {
        CumploMO cumploMO = new CumploMO();
        InputStream inputStream = null, inputStreamTemp = null;
        try
        {
            ClassPathResource classPathResource = new ClassPathResource(Constante.APPLICATION_PROPERTIES);
            inputStreamTemp = classPathResource.getInputStream();
            File tempFile = File.createTempFile(Constante.APPLICATION_PROPERTIES, null);
            FileUtils.copyInputStreamToFile(inputStreamTemp, tempFile);
            inputStream = new FileInputStream(tempFile);
            tempFile.delete();
            Properties properties = new Properties();
            properties.load(inputStream);
            String url = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_URL)) ? "" : properties.getProperty(Constante.CUMPLO_URL);
            String username = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_USERNAME)) ? "" : properties.getProperty(Constante.CUMPLO_USERNAME);
            String password = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_PASSWORD)) ? "" : properties.getProperty(Constante.CUMPLO_PASSWORD);
            String api = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_API)) ? "" : properties.getProperty(Constante.CUMPLO_API);
            String login = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_LOGIN)) ? "" : properties.getProperty(Constante.CUMPLO_LOGIN);
            String entidad = Util.IsNullOrEmpty(properties.getProperty(Constante.CUMPLO_ENTIDAD)) ? "" : properties.getProperty(Constante.CUMPLO_ENTIDAD);
            cumploMO.setUrl(url);
            cumploMO.setUsername(username);
            cumploMO.setPassword(password);
            cumploMO.setApi(api);
            cumploMO.setLogin(login);
            cumploMO.setEntidad(entidad);
            properties.clear();
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
            if (inputStreamTemp != null)
            {
                inputStreamTemp.close();
            }
        }
        return cumploMO;
    }

    public static ConexionMO ObtenerConexion() throws Exception
    {
        ConexionMO conexionMO = new ConexionMO();
        InputStream inputStream = null, inputStreamTemp = null;
        try
        {
            ClassPathResource classPathResource = new ClassPathResource(Constante.APPLICATION_PROPERTIES);
            inputStreamTemp = classPathResource.getInputStream();
            File tempFile = File.createTempFile(Constante.APPLICATION_PROPERTIES, null);
            FileUtils.copyInputStreamToFile(inputStreamTemp, tempFile);
            inputStream = new FileInputStream(tempFile);
            tempFile.delete();
            Properties properties = new Properties();
            properties.load(inputStream);
            String driver = Util.IsNullOrEmpty(properties.getProperty(Constante.SQLSERVER_DRIVER)) ? "" : properties.getProperty(Constante.SQLSERVER_DRIVER);
            String url = Util.IsNullOrEmpty(properties.getProperty(Constante.SQLSERVER_URL)) ? "" : properties.getProperty(Constante.SQLSERVER_URL);
            String username = Util.IsNullOrEmpty(properties.getProperty(Constante.SQLSERVER_USERNAME)) ? "" : properties.getProperty(Constante.SQLSERVER_USERNAME);
            String password = Util.IsNullOrEmpty(properties.getProperty(Constante.SQLSERVER_PASSWORD)) ? "" : properties.getProperty(Constante.SQLSERVER_PASSWORD);
            conexionMO.setDriver(driver);
            conexionMO.setUrl(url);
            conexionMO.setUsername(username);
            conexionMO.setPassword(password);
            properties.clear();
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
            if (inputStreamTemp != null)
            {
                inputStreamTemp.close();
            }
        }
        return conexionMO;
    }

    public static Boolean IsNullOrEmpty(String valor) throws Exception
    {
        Boolean isNullOrEmpty = false;
        try
        {
            isNullOrEmpty = valor == null || valor.length() == Constante._0;
        }
        catch (Exception e)
        {
            throw e;
        }
        return isNullOrEmpty;
    }

    public static Boolean IsNullOrEmpty(Integer valor) throws Exception
    {
        Boolean isNullOrEmpty = false;
        try
        {
            isNullOrEmpty = valor == null || valor < Constante._0;
        }
        catch (Exception e)
        {
            throw e;
        }
        return isNullOrEmpty;
    }

    public static HttpHeaders GetHeaders(TokenMO tokenMO) throws Exception
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        try
        {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set(Constante.AUTHORIZATION, tokenMO.getToken());
        }
        catch (Exception e)
        {
            throw e;
        }
        return httpHeaders;
    }

    public static ObjectMapper GetObjectMapper() throws Exception
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try
        {
            objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        }
        catch (Exception e)
        {
            throw e;
        }
        return objectMapper;
    }
}
