package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ColaboradorIN;
import com.grupogloria.splaservicio.Modelo.ColaboradorMO;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoColaboradorMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ColaboradorRE;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.Logger;

public class ColaboradorNE implements ColaboradorIN
{
    private ColaboradorRE _colaboradorRE;
    private RestTemplate _restTemplate;
    private Util _util;
    private TokenMO _tokenMO = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Logger _logger = (Logger) LoggerFactory.getLogger(ColaboradorNE.class);

    public ColaboradorNE() throws Exception
    {
        _colaboradorRE = new ColaboradorRE();
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

    public ObjetoColaboradorMO CrearColaborador(ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(colaboradorMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarColaborador(colaboradorMO);
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(mensajeValidacion);
            _logger.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoColaboradorMO = _colaboradorRE.CrearColaborador(colaboradorMO);
                _logger.info(objetoColaboradorMO.getMensaje());

                if (objetoColaboradorMO.getCodigo() == Constante.CODIGO_OK)
                {
                    mensaje = objetoColaboradorMO.getMensaje();
                    String json = _objectMapper.writeValueAsString(colaboradorMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                    objetoColaboradorMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoColaboradorMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        mensaje += Constante.DELIMITADOR_BARRA + respuestaMO.getDetalle();
                        objetoColaboradorMO = ActivarColaborador(colaboradorMO.getIdInternoCliente());
                        _logger.info(objetoColaboradorMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                        objetoColaboradorMO.setMensaje(mensaje);
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
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO ActivarColaborador(String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            objetoColaboradorMO = _colaboradorRE.ActivarColaborador(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoColaboradorMO;
    }
    
    public ObjetoColaboradorMO EditarColaborador(ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(colaboradorMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(colaboradorMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(colaboradorMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                mensaje = cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion();
                objetoColaboradorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoColaboradorMO = _colaboradorRE.EditarColaborador(colaboradorMO);
                    _logger.info(objetoColaboradorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                    objetoColaboradorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO AnularColaborador(String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            String mensaje = "";
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = _util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
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
                    objetoColaboradorMO = InactivarColaborador(idInternoCliente);
                    _logger.info(objetoColaboradorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                    objetoColaboradorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO InactivarColaborador(String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            objetoColaboradorMO = _colaboradorRE.InactivarColaborador(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoColaboradorMO;
    }
    
    public String ValidarColaborador(ColaboradorMO colaboradorMO)
    {
        String mensaje = "";
        try
        {
            if (colaboradorMO == null)
            {
                mensaje += String.format(Constante.MENSAJE_ENTIDAD_VACIA, colaboradorMO);
            }
            else
            {
                String idInternoCliente = colaboradorMO.getIdInternoCliente();
                String denominacion = colaboradorMO.getDenominacion();
                String documento = colaboradorMO.getDocumento();
                Integer idTipoDocumento = colaboradorMO.getIdTipoDocumento();
                Integer idTipoEntidad = colaboradorMO.getIdTipoEntidad();
                Integer idCategoria = colaboradorMO.getIdCategoria();
                Integer idEmpresa = colaboradorMO.getIdEmpresa();

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
