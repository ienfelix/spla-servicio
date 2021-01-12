package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ProveedorIN;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoProveedorMO;
import com.grupogloria.splaservicio.Modelo.ProveedorMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ProveedorRE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ProveedorNE implements ProveedorIN
{
    private ProveedorRE _proveedorRE = null;
    private RestTemplate _restTemplate;
    private TokenNE _tokenNE = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Log _log = null;

    public ProveedorNE() throws Exception
    {
        _proveedorRE = new ProveedorRE();
        _restTemplate = new RestTemplate();
        _tokenNE = new TokenNE();
        _cumploMO = Util.ObtenerCumplo();
        TokenMO tokenMO = _tokenNE.ValidarToken();
        _httpHeaders = Util.GetHeaders(tokenMO);
        _objectMapper = Util.GetObjectMapper();
        _log = new Log(ProveedorNE.class.getName(), Constante.ENTIDAD_PROVEEDOR);
    }

    public ObjetoProveedorMO CrearProveedor(ProveedorMO proveedorMO) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(proveedorMO);
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarProveedor(proveedorMO);
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(mensajeValidacion);
            _log.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoProveedorMO = _proveedorRE.CrearProveedor(proveedorMO);
                _log.info(objetoProveedorMO.getMensaje());

                if (objetoProveedorMO.getCodigo() == Constante.CODIGO_OK)
                {
                    String json = _objectMapper.writeValueAsString(proveedorMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                    _log.info(mensaje);
                    mensaje = objetoProveedorMO.getMensaje() + Constante.DELIMITADOR_BARRA + mensaje;
                    objetoProveedorMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoProveedorMO.setMensaje(mensaje);

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        objetoProveedorMO = ActivarProveedor(proveedorMO.getIdInternoCliente());
                        _log.info(objetoProveedorMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                        objetoProveedorMO.setMensaje(mensaje);
                    }
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(proveedorMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(proveedorMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                _log.info(mensaje);
                objetoProveedorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoProveedorMO = _proveedorRE.EditarProveedor(proveedorMO);
                    _log.info(objetoProveedorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                    objetoProveedorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = Util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoProveedorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                HttpEntity<String> httpEntity = new HttpEntity<String>(_httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.CODIGO_INTERNO_CLIENTE + Constante.DELIMITADOR_IGUAL + idInternoCliente;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.DELETE, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                _log.info(mensaje);
                objetoProveedorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoProveedorMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    objetoProveedorMO = InactivarProveedor(idInternoCliente);
                    _log.info(objetoProveedorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoProveedorMO.getMensaje();
                    objetoProveedorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
            throw e;
        }
        return objetoProveedorMO;
    }
    
    public String ValidarProveedor(ProveedorMO proveedorMO) throws Exception
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

                if (Util.IsNullOrEmpty(idInternoCliente))
                {
                    mensaje += Constante.MENSAJE_ID_INTERNO_CLIENTE;
                }
                if (Util.IsNullOrEmpty(denominacion))
                {
                    mensaje += Constante.MENSAJE_DENOMINACION;
                }
                if (Util.IsNullOrEmpty(documento))
                {
                    mensaje += Constante.MENSAJE_DOCUMENTO;
                }
                if (Util.IsNullOrEmpty(idTipoDocumento))
                {
                    mensaje += Constante.MENSAJE_ID_TIPO_DOCUMENTO;
                }
                if (Util.IsNullOrEmpty(idTipoEntidad))
                {
                    mensaje += Constante.MENSAJE_ID_TIPO_ENTIDAD;
                }
                if (Util.IsNullOrEmpty(idCategoria))
                {
                    mensaje += Constante.MENSAJE_ID_CATEGORIA;
                }
                if (Util.IsNullOrEmpty(idEmpresa))
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
