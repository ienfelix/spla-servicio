package com.grupogloria.splaservicio.Comun;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoTokenMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Negocio.TokenNE;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.Logger;

public class Util
{
    private TokenNE tokenNE;
    private RestTemplate restTemplate;
    private CumploMO cumploMO;
    private Logger _logger = (Logger) LoggerFactory.getLogger(Util.class);

    public Util() throws Exception
    {
        restTemplate = new RestTemplate();
        tokenNE = new TokenNE();
        cumploMO = ObtenerCumplo();
    }
    
    public TokenMO ValidarToken() throws Exception
    {
        TokenMO tokenMO = null;
        try
        {
            ObjetoTokenMO objetoTokenMO = tokenNE.ObtenerToken();

            if (objetoTokenMO.getCodigo() == Constante.CODIGO_OK)
            {
                tokenMO = objetoTokenMO.getTokenMO();
                _logger.info(String.format(Constante.MENSAJE_TOKEN_VIGENTE, tokenMO.getToken(), tokenMO.getFecha()));
            }
            else if (objetoTokenMO.getCodigo() != Constante.CODIGO_OK)
            {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                var enlace = cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_LOGIN;
                HttpEntity<CumploMO> httpEntity = new HttpEntity<CumploMO>(cumploMO, httpHeaders);
                ResponseEntity<String> responseEntity = restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
                objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                CumploMO respuesta = objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Integer cumploCodigo = respuesta.getStatus();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                var fecha = new Date();
                _logger.info(String.format(Constante.MENSAJE_TOKEN_DESCARGADO, respuesta.getDescripcion(), simpleDateFormat.format(fecha)));

                if (cumploCodigo == Constante.CUMPLO_CODIGO_OK)
                {
                    tokenMO = new TokenMO();
                    tokenMO.setToken(respuesta.getDescripcion());
                    tokenMO.setFecha(simpleDateFormat.format(fecha));
                    tokenNE.RegistrarToken(tokenMO);
                    _logger.info(String.format(Constante.MENSAJE_TOKEN_REGISTRADO, tokenMO.getToken(), tokenMO.getFecha()));
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return tokenMO;
    }

    public CumploMO ObtenerCumplo()
    {
        CumploMO cumploMO = new CumploMO();
        try
        {
            cumploMO = new CumploMO();
            cumploMO.setUrl(Constante.CUMPLO_URL);
            cumploMO.setUsername(Constante.CUMPLO_USERNAME);
            cumploMO.setPassword(Constante.CUMPLO_PASSWORD);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return cumploMO;
    }

    public Boolean IsNullOrEmpty(String valor)
    {
        Boolean isNullOrEmpty = false;
        try
        {
            isNullOrEmpty = valor == null || valor.length() == Constante._0;
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return isNullOrEmpty;
    }

    public Boolean IsNullOrEmpty(Integer valor)
    {
        Boolean isNullOrEmpty = false;
        try
        {
            isNullOrEmpty = valor == null || valor < Constante._0;
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return isNullOrEmpty;
    }
}
