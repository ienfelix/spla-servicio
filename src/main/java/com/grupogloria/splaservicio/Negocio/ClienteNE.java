package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ClienteIN;
import com.grupogloria.splaservicio.Modelo.ClienteMO;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoClienteMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ClienteRE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClienteNE implements ClienteIN
{
    private ClienteRE _clienteRE = null;
    private RestTemplate _restTemplate;
    private TokenNE _tokenNE = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Log _log = null;

    public ClienteNE() throws Exception
    {
        _clienteRE = new ClienteRE();
        _restTemplate = new RestTemplate();
        _tokenNE = new TokenNE();
        _cumploMO = Util.ObtenerCumplo();
        TokenMO tokenMO = _tokenNE.ValidarToken();
        _httpHeaders = Util.GetHeaders(tokenMO);
        _objectMapper = Util.GetObjectMapper();
        _log = new Log(ClienteNE.class.getName(), Constante.ENTIDAD_CLIENTE);
    }

    public ObjetoClienteMO CrearCliente(ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(clienteMO);
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarCliente(clienteMO);
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(mensajeValidacion);
            _log.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoClienteMO = _clienteRE.CrearCliente(clienteMO);
                _log.info(objetoClienteMO.getMensaje());

                if (objetoClienteMO.getCodigo() == Constante.CODIGO_OK)
                {
                    String json = _objectMapper.writeValueAsString(clienteMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                    _log.info(mensaje);
                    mensaje = objetoClienteMO.getMensaje() + Constante.DELIMITADOR_BARRA + mensaje;
                    objetoClienteMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoClienteMO.setMensaje(mensaje);

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        objetoClienteMO = ActivarCliente(clienteMO.getIdInternoCliente());
                        _log.info(objetoClienteMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                        objetoClienteMO.setMensaje(mensaje);
                    }
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(clienteMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(clienteMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                _log.info(mensaje);
                objetoClienteMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoClienteMO = _clienteRE.EditarCliente(clienteMO);
                    _log.info(objetoClienteMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                    objetoClienteMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = Util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoClienteMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
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
                objetoClienteMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoClienteMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    objetoClienteMO = InactivarCliente(idInternoCliente);
                    _log.info(objetoClienteMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoClienteMO.getMensaje();
                    objetoClienteMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
            throw e;
        }
        return objetoClienteMO;
    }
    
    public String ValidarCliente(ClienteMO clienteMO) throws Exception
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
