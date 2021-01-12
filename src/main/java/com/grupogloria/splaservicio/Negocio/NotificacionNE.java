package com.grupogloria.splaservicio.Negocio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.NotificacionIN;
import com.grupogloria.splaservicio.Modelo.NotificacionMO;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;
import com.grupogloria.splaservicio.Repositorio.NotificacionRE;

public class NotificacionNE implements NotificacionIN
{
    private NotificacionRE _notificacionRE = null;
    private ObjectMapper _objectMapper = null;
    private Log _log = null;

    public NotificacionNE() throws Exception
    {
        _notificacionRE = new NotificacionRE();
        _objectMapper = Util.GetObjectMapper();
		_log = new Log(NotificacionRE.class.getName(), "");
    }

    public ObjetoNotificacionMO EnviarNotificacion(NotificacionMO notificacionMO) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            String parametros = _objectMapper.writeValueAsString(notificacionMO);
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));
            
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
            _log.error(e);
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
            _log.error(e);
            throw e;
        }
        return objetoNotificacionMO;
    }
}
