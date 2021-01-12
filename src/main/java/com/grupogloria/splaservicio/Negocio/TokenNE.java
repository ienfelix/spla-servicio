package com.grupogloria.splaservicio.Negocio;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.TokenIN;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoTokenMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.TokenRE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class TokenNE implements TokenIN
{
    private TokenRE tokenRE;
    private RestTemplate _restTemplate = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Log _log = null;

    public TokenNE() throws Exception
    {
        tokenRE = new TokenRE();
        _restTemplate = new RestTemplate();
        _cumploMO = Util.ObtenerCumplo();
        _httpHeaders = new HttpHeaders();
        _httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        _objectMapper = Util.GetObjectMapper();
        _log = new Log(TokenNE.class.getName(), "");
    }

    public TokenMO ValidarToken() throws Exception
    {
        TokenMO tokenMO = null;
        try
        {
            ObjetoTokenMO objetoTokenMO = ObtenerToken();

            if (objetoTokenMO.getCodigo() == Constante.CODIGO_OK)
            {
                tokenMO = objetoTokenMO.getTokenMO();
                _log.info(String.format(Constante.MENSAJE_TOKEN_VIGENTE, tokenMO.getToken(), tokenMO.getFecha()));
            }
            else if (objetoTokenMO.getCodigo() != Constante.CODIGO_OK)
            {
                var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getLogin();
                HttpEntity<CumploMO> httpEntity = new HttpEntity<CumploMO>(_cumploMO, _httpHeaders);
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuesta = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Integer cumploCodigo = respuesta.getStatus();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                var fecha = new Date();
                _log.info(String.format(Constante.MENSAJE_TOKEN_DESCARGADO, respuesta.getDescripcion(), simpleDateFormat.format(fecha)));

                if (cumploCodigo == Constante.CUMPLO_CODIGO_OK)
                {
                    tokenMO = new TokenMO();
                    tokenMO.setToken(respuesta.getDescripcion());
                    tokenMO.setFecha(simpleDateFormat.format(fecha));
                    objetoTokenMO = RegistrarToken(tokenMO);
                    _log.info(objetoTokenMO.getMensaje());
                    _log.info(String.format(Constante.MENSAJE_TOKEN_REGISTRADO, tokenMO.getToken(), tokenMO.getFecha()));
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
            throw e;
        }
        return tokenMO;
    }

    public ObjetoTokenMO ObtenerToken() throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            objetoTokenMO = tokenRE.ObtenerToken();
        }
        catch (Exception e)
        {
            _log.error(e);
            throw e;
        }
        return objetoTokenMO;
    }
    
    public ObjetoTokenMO RegistrarToken(TokenMO tokenMO) throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            objetoTokenMO = tokenRE.RegistrarToken(tokenMO);
        }
        catch (Exception e)
        {
            _log.error(e);
            throw e;
        }
        return objetoTokenMO;
    }
}
