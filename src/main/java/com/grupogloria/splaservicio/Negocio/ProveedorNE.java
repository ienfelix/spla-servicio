package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ProveedorIN;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoProveedorMO;
import com.grupogloria.splaservicio.Modelo.ProveedorMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ProveedorRE;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.classic.Logger;

public class ProveedorNE implements ProveedorIN
{
    private ProveedorRE _proveedorRE;
    private RestTemplate _restTemplate;
    private Util _util;
    private TokenMO _tokenMO = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Logger _logger = (Logger) LoggerFactory.getLogger(ProveedorNE.class);

    public ProveedorNE() throws Exception
    {
        _proveedorRE = new ProveedorRE();
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

    public ObjetoProveedorMO CrearProveedor(ProveedorMO proveedorMO) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(proveedorMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarProveedor(proveedorMO);
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(mensajeValidacion);
            _logger.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoProveedorMO = _proveedorRE.CrearProveedor(proveedorMO);
                _logger.info(objetoProveedorMO.getMensaje());

                if (objetoProveedorMO.getCodigo() == Constante.CODIGO_OK)
                {
                    mensaje = objetoProveedorMO.getMensaje();
                    String json = _objectMapper.writeValueAsString(proveedorMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                    objetoProveedorMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoProveedorMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        mensaje += Constante.DELIMITADOR_BARRA + respuestaMO.getDetalle();
                        objetoProveedorMO = ActivarProveedor(proveedorMO.getIdInternoCliente());
                        _logger.info(objetoProveedorMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                        objetoProveedorMO.setMensaje(mensaje);
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
        return objetoProveedorMO;
    }

    public ObjetoProveedorMO ActivarProveedor(String idInternoCliente) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            objetoProveedorMO = _proveedorRE.ActivarProveedor(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoProveedorMO;
    }
    
    public ObjetoProveedorMO EditarProveedor(ProveedorMO proveedorMO) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(proveedorMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = _util.IsNullOrEmpty(proveedorMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(proveedorMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + Constante.CUMPLO_API + "/" + Constante.CUMPLO_CLIENTE + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                _logger.info(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());
                mensaje = cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion();
                objetoProveedorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(cumploExito ? respuestaMO.getDetalle() : respuestaMO.getDescripcion());

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _logger.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoProveedorMO = _proveedorRE.EditarProveedor(proveedorMO);
                    _logger.info(objetoProveedorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                    objetoProveedorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoProveedorMO;
    }

    public ObjetoProveedorMO AnularProveedor(String idInternoCliente) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            String mensaje = "";
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = _util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
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
                    objetoProveedorMO = InactivarProveedor(idInternoCliente);
                    _logger.info(objetoProveedorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                    objetoProveedorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoProveedorMO;
    }

    public ObjetoProveedorMO InactivarProveedor(String idInternoCliente) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            objetoProveedorMO = _proveedorRE.InactivarProveedor(idInternoCliente);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoProveedorMO;
    }
    
    public String ValidarProveedor(ProveedorMO proveedorMO)
    {
        String mensaje = "";
        try
        {
            if (proveedorMO == null)
            {
                mensaje += String.format(Constante.MENSAJE_ENTIDAD_VACIA, proveedorMO);
            }
            else
            {
                String idInternoCliente = proveedorMO.getIdInternoCliente();
                String denominacion = proveedorMO.getDenominacion();
                String documento = proveedorMO.getDocumento();
                Integer idTipoDocumento = proveedorMO.getIdTipoDocumento();
                Integer idTipoEntidad = proveedorMO.getIdTipoEntidad();
                Integer idCategoria = proveedorMO.getIdCategoria();
                Integer idEmpresa = proveedorMO.getIdEmpresa();

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
