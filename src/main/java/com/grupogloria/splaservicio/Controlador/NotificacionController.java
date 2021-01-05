package com.grupogloria.splaservicio.Controlador;

import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Modelo.NotificacionMO;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;
import com.grupogloria.splaservicio.Negocio.NotificacionNE;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constante.NOTIFICACION_CONTROLADOR)
public class NotificacionController
{
    private NotificacionNE _notificacionNE = null;

    public NotificacionController() throws Exception
    {
        _notificacionNE = new NotificacionNE();
    }

    @GetMapping(produces = Constante.APPLICATION_JSON)
    public String Get()
    {
        return String.format(Constante.SERVICIO_ACTIVO, Constante.NOTIFICACION_CONTROLADOR);
    }

    @PostMapping(path = Constante.ENVIAR_NOTIFICACION, produces = Constante.APPLICATION_JSON)
    public ObjetoNotificacionMO EnviarNotificacion(@RequestBody NotificacionMO notificacionMO) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            objetoNotificacionMO = _notificacionNE.EnviarNotificacion(notificacionMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoNotificacionMO;
    }
}
