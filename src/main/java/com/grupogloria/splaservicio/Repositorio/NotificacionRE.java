package com.grupogloria.splaservicio.Repositorio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.grupogloria.splaservicio.Comun.Conexion;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Interfaz.NotificacionIN;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class NotificacionRE implements NotificacionIN
{
    private Conexion _conexion = null;
    private Connection _con = null;
    private CallableStatement _cst = null;
    private ResultSet _rst = null;
    private static final Logger _logger = (Logger) LoggerFactory.getLogger(NotificacionRE.class);

    public NotificacionRE()
    {
        _conexion = new Conexion();
    }

    public ObjetoNotificacionMO EnviarNotificacion (String json, String destinatario) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_ENVIAR_NOTIFICACION + "(?, ?, ?)}");
            _cst.setString(Constante._1, json);
            _cst.setString(Constante._2, destinatario);
            _cst.setString(Constante._3, Constante.USUARIO_CREACION);
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                while (_rst.next())
                {
                    objetoNotificacionMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoNotificacionMO.setMensaje(_rst.getString(Constante.MENSAJE));
                }
            }
        }
        catch (Exception e)
        {
            var stack = e.getStackTrace()[Constante._0];
            _logger.error(String.format(Constante.ERROR, stack.getClassName(), stack.getMethodName(), stack.getLineNumber(), e.getMessage()));
            throw e;
        }
        finally
        {
            _cst.close();
            _rst.close();
            _con.close();
        }
        return objetoNotificacionMO;
    }
}
