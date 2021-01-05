package com.grupogloria.splaservicio.Interfaz;

import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;

public interface NotificacionIN
{
    public ObjetoNotificacionMO EnviarNotificacion(String json, String destinatario) throws Exception;
}
