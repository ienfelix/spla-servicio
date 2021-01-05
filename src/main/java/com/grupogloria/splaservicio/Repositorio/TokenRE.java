package com.grupogloria.splaservicio.Repositorio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.grupogloria.splaservicio.Modelo.*;
import com.grupogloria.splaservicio.Comun.Conexion;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Interfaz.TokenIN;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class TokenRE implements TokenIN
{
    private Conexion _conexion = null;
    private Connection _con = null;
    private CallableStatement _cst = null;
    private ResultSet _rst = null;
    private Logger _logger = (Logger) LoggerFactory.getLogger(TokenRE.class);

    public TokenRE()
    {
        _conexion = new Conexion();
    }

    public ObjetoTokenMO ObtenerToken() throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_OBTENER_TOKEN + "}");
            _cst.execute();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    TokenMO tokenMO = new TokenMO();
                    tokenMO.setToken(_rst.getString(Constante.TOKEN));
                    tokenMO.setFecha(_rst.getString(Constante.FECHA));
                    objetoTokenMO.setTokenMO(tokenMO);
                }

                objetoTokenMO.setCodigo(Constante.CODIGO_OK);
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
            _rst.close();
            _cst.close();
            _con.close();
        }
        return objetoTokenMO;
    }
    
    public ObjetoTokenMO RegistrarToken(TokenMO tokenMO) throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_REGISTRAR_TOKEN + "(?, ?)}");
            _cst.setString(1, tokenMO.getToken());
            _cst.setString(2, tokenMO.getFecha());
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoTokenMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoTokenMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
            _rst.close();
            _cst.close();
            _con.close();
        }
        return objetoTokenMO;
    }
}
