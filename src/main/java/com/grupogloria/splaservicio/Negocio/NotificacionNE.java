package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Interfaz.NotificacionIN;
import com.grupogloria.splaservicio.Modelo.NotificacionMO;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;
import com.grupogloria.splaservicio.Repositorio.NotificacionRE;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class NotificacionNE implements NotificacionIN
{
    private NotificacionRE _notificacionRE = null;
    private ObjectMapper _objectMapper = null;
    private static final Logger _logger = (Logger) LoggerFactory.getLogger(NotificacionNE.class);

    public NotificacionNE()
    {
        _notificacionRE = new NotificacionRE();
        _objectMapper = new ObjectMapper();
        _objectMapper.setSerializationInclusion(Include.NON_NULL);
        _objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        _objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        _objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _objectMapper.setVisibility(_objectMapper.getSerializationConfig().getDefaultVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY).withGetterVisibility(JsonAutoDetect.Visibility.NONE).withSetterVisibility(JsonAutoDetect.Visibility.NONE).withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
    }

    public ObjetoNotificacionMO EnviarNotificacion(NotificacionMO notificacionMO) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            String parametros = _objectMapper.writeValueAsString(notificacionMO);
            _logger.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            
            if (notificacionMO == null)
            {
                objetoNotificacionMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoNotificacionMO.setMensaje(String.format(Constante.MENSAJE_ENTIDAD_VACIA, notificacionMO));
            }
            else
            {
                String json = _objectMapper.writeValueAsString(notificacionMO);
                objetoNotificacionMO = EnviarNotificacion(json, notificacionMO.getDestinatario());
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoNotificacionMO;
    }

    public ObjetoNotificacionMO EnviarNotificacion(String json, String destinatario) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            objetoNotificacionMO = _notificacionRE.EnviarNotificacion(json, destinatario);
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        return objetoNotificacionMO;
    }
}
