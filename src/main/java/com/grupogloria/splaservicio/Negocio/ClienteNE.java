package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ClienteIN;
import com.grupogloria.splaservicio.Modelo.ClienteMO;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoClienteMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ClienteRE;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.Logger;

public class ClienteNE implements ClienteIN
{
    private ClienteRE _clienteRE;
    private RestTemplate _restTemplate;
    private Util _util;
    private TokenMO _tokenMO = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Logger _logger = (Logger) LoggerFactory.getLogger(ClienteNE.class);

    public ClienteNE() throws Exception
    {
        _clienteRE = new ClienteRE();
        _restTemplate = new RestTemplate();
        _util = new Util();
        _tokenMO = _util.ValidarToken();
        _cumploMO = _util.ObtenerCumplo();
        _httpHeaders = new HttpHeaders();
        _httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        _httpHeaders.set(Constante.AUTHORIZATION, _tokenMO.getToken());
        _objectMapper = new ObjectMapper();
        _objectMapper.setSerializationInclusion(Include.NON_NULL);
        _objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        _objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _objectMapper.setVisibility(_objectMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public ObjetoClienteMO CrearCliente(ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(clienteMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarCliente(clienteMO);
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(mensajeValidacion);
            _logger.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoClienteMO = _clienteRE.CrearCliente(clienteMO);
                _logger.info(objetoClienteMO.getMensaje());

                if (objetoClienteMO.getCodigo() == Constante.CODIGO_OK)
                {
                    mensaje = objetoClienteMO.getMensaje();
                    String json = _objectMapper.writeValueAsString(clienteMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                    objetoClienteMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoClienteMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        mensaje += Constante.DELIMITADOR_BARRA + respuestaMO.getDetalle();
                        objetoClienteMO = ActivarCliente(clienteMO.getIdInternoCliente());
                        _logger.info(objetoClienteMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                        objetoClienteMO.setMensaje(mensaje);
                    }
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoClienteMO;
    }

    public ObjetoClienteMO ActivarCliente(String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            objetoClienteMO = _clienteRE.ActivarCliente(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoClienteMO;
    }
    
    public ObjetoClienteMO EditarCliente(ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(clienteMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(clienteMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(clienteMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                mensaje = cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion();
                objetoClienteMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoClienteMO = _clienteRE.EditarCliente(clienteMO);
                    _logger.info(objetoClienteMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                    objetoClienteMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoClienteMO;
    }

    public ObjetoClienteMO AnularCliente(String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            String mensaje = "";
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = _util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                HttpEntity<String> httpEntity = new HttpEntity<String>(_httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.CODIGO_INTERNO_CLIENTE + Constante.DELIMITADOR_IGUAL + idInternoCliente;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.DELETE, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                if (cumploExito)
                {
                    mensaje += Constante.DELIMITADOR_BARRA + respuestaMO.getDescripcion();
                    objetoClienteMO = InactivarCliente(idInternoCliente);
                    _logger.info(objetoClienteMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                    objetoClienteMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoClienteMO;
    }

    public ObjetoClienteMO InactivarCliente(String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            objetoClienteMO = _clienteRE.InactivarCliente(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoClienteMO;
    }
    
    public String ValidarCliente(ClienteMO clienteMO)
    {
        String mensaje = "";
        try
        {
            if (clienteMO == null)
            {
                mensaje += String.format(Constante.MENSAJE_ENTIDAD_VACIA, clienteMO);
            }
            else
            {
                String idInternoCliente = clienteMO.getIdInternoCliente();
                String denominacion = clienteMO.getDenominacion();
                String documento = clienteMO.getDocumento();
                Integer idTipoDocumento = clienteMO.getIdTipoDocumento();
                Integer idTipoEntidad = clienteMO.getIdTipoEntidad();
                Integer idCategoria = clienteMO.getIdCategoria();
                Integer idEmpresa = clienteMO.getIdEmpresa();

                if (_util.IsNullOrEmpty(idInternoCliente))
                {
                    mensaje += Constante.MENSAJE_ID_INTERNO_CLIENTE;
                }
                if (_util.IsNullOrEmpty(denominacion))
                {
                    mensaje += Constante.MENSAJE_DENOMINACION;
                }
                if (_util.IsNullOrEmpty(documento))
                {
                    mensaje += Constante.MENSAJE_DOCUMENTO;
                }
                if (_util.IsNullOrEmpty(idTipoDocumento))
                {
                    mensaje += Constante.MENSAJE_ID_TIPO_DOCUMENTO;
                }
                if (_util.IsNullOrEmpty(idTipoEntidad))
                {
                    mensaje += Constante.MENSAJE_ID_TIPO_ENTIDAD;
                }
                if (_util.IsNullOrEmpty(idCategoria))
                {
                    mensaje += Constante.MENSAJE_ID_CATEGORIA;
                }
                if (_util.IsNullOrEmpty(idEmpresa))
                {
                    mensaje += Constante.MENSAJE_ID_EMPRESA;
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return mensaje;
    }
}
