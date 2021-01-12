package com.grupogloria.splaservicio.Repositorio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.grupogloria.splaservicio.Comun.Conexion;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Interfaz.NotificacionIN;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;

public class NotificacionRE implements NotificacionIN
{
    private Conexion _conexion = null;
    private Connection _con = null;
    private CallableStatement _cst = null;
    private ResultSet _rst = null;
    private Log _log = null;

    public NotificacionRE() throws Exception
    {
        _conexion = new Conexion();
        _log = new Log(NotificacionRE.class.getName(), "");
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
            _log.error(e);
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
