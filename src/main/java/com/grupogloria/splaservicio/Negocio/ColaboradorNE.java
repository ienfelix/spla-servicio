package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.ColaboradorIN;
import com.grupogloria.splaservicio.Modelo.ColaboradorMO;
import com.grupogloria.splaservicio.Modelo.CumploMO;
import com.grupogloria.splaservicio.Modelo.ObjetoColaboradorMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.ColaboradorRE;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ColaboradorNE implements ColaboradorIN
{
    private ColaboradorRE _colaboradorRE = null;
    private RestTemplate _restTemplate;
    private TokenNE _tokenNE = null;
    private CumploMO _cumploMO = null;
    private HttpHeaders _httpHeaders = null;
    private ObjectMapper _objectMapper = null;
    private Log _log = null;

    public ColaboradorNE() throws Exception
    {
        _colaboradorRE = new ColaboradorRE();
        _restTemplate = new RestTemplate();
        _tokenNE = new TokenNE();
        _cumploMO = Util.ObtenerCumplo();
        TokenMO tokenMO = _tokenNE.ValidarToken();
        _httpHeaders = Util.GetHeaders(tokenMO);
        _objectMapper = Util.GetObjectMapper();
        _log = new Log(ColaboradorNE.class.getName(), Constante.ENTIDAD_COLABORADOR);
    }

    public ObjetoColaboradorMO CrearColaborador(ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            String mensaje = "";
            String parametros = _objectMapper.writeValueAsString(colaboradorMO);
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            String mensajeValidacion = ValidarColaborador(colaboradorMO);
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(mensajeValidacion);
            _log.info(mensajeValidacion);

            if (!isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(mensajeValidacion);
            }
            else
            {
                objetoColaboradorMO = _colaboradorRE.CrearColaborador(colaboradorMO);
                _log.info(objetoColaboradorMO.getMensaje());

                if (objetoColaboradorMO.getCodigo() == Constante.CODIGO_OK)
                {
                    String json = _objectMapper.writeValueAsString(colaboradorMO);
                    HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                    var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.FALSE;
                    ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                    CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                    Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                    mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                    _log.info(mensaje);
                    mensaje = objetoColaboradorMO.getMensaje() + Constante.DELIMITADOR_BARRA + mensaje;
                    objetoColaboradorMO.setCodigo(cumploExito ? Constante.CODIGO_OK : Constante.CODIGO_NO_OK);
                    objetoColaboradorMO.setMensaje(mensaje);

                    if (cumploExito)
                    {
                        parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                        _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                        objetoColaboradorMO = ActivarColaborador(colaboradorMO.getIdInternoCliente());
                        _log.info(objetoColaboradorMO.getMensaje());
                        mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                        objetoColaboradorMO.setMensaje(mensaje);
                    }
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            Boolean isNullOrEmpty = Util.IsNullOrEmpty(colaboradorMO.getIdInternoCliente());

            if (isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
            }
            else
            {
                String json = _objectMapper.writeValueAsString(colaboradorMO);
                HttpEntity<String> httpEntity = new HttpEntity<String>(json, _httpHeaders);
                var enlace = _cumploMO.getUrl() + "/" + _cumploMO.getApi() + "/" + _cumploMO.getEntidad() + Constante.DELIMITADOR_PREGUNTA + Constante.MODIFICAR + Constante.DELIMITADOR_IGUAL + Constante.TRUE;
                ResponseEntity<String> responseEntity = _restTemplate.exchange(enlace, HttpMethod.POST, httpEntity, String.class);
                CumploMO respuestaMO = _objectMapper.readValue(responseEntity.getBody(), CumploMO.class);
                Boolean cumploExito = respuestaMO.getExito() == null ? false : respuestaMO.getExito();
                mensaje = respuestaMO.getDetalle() == null ? respuestaMO.getDescripcion() : respuestaMO.getDetalle();
                _log.info(mensaje);
                objetoColaboradorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    parametros = _objectMapper.writeValueAsString(respuestaMO.getCliente());
                    _log.info(String.format(Constante.PARAMETROS_SALIDA, parametros));
                    objetoColaboradorMO = _colaboradorRE.EditarColaborador(colaboradorMO);
                    _log.info(objetoColaboradorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                    objetoColaboradorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.info(String.format(Constante.PARAMETROS_INGRESO, idInternoCliente));

            Boolean isNullOrEmpty = Util.IsNullOrEmpty(idInternoCliente);

            if (isNullOrEmpty)
            {
                objetoColaboradorMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(Constante.MENSAJE_ID_INTERNO_CLIENTE);
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
                objetoColaboradorMO.setCodigo(cumploExito ? Constante.CODIGO_NO_OK : Constante.CODIGO_NO_OK);
                objetoColaboradorMO.setMensaje(mensaje);

                if (cumploExito)
                {
                    objetoColaboradorMO = InactivarColaborador(idInternoCliente);
                    _log.info(objetoColaboradorMO.getMensaje());
                    mensaje += Constante.DELIMITADOR_BARRA + objetoColaboradorMO.getMensaje();
                    objetoColaboradorMO.setMensaje(mensaje);
                }
            }
        }
        catch (Exception e)
        {
            _log.error(e);
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
            _log.error(e);
            throw e;
        }
        return objetoColaboradorMO;
    }
    
    public String ValidarColaborador(ColaboradorMO colaboradorMO) throws Exception
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
