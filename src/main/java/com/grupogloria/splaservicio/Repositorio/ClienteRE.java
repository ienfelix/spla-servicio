package com.grupogloria.splaservicio.Repositorio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.grupogloria.splaservicio.Comun.Conexion;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Interfaz.ClienteIN;
import com.grupogloria.splaservicio.Modelo.ClienteMO;
import com.grupogloria.splaservicio.Modelo.ObjetoClienteMO;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class ClienteRE implements ClienteIN
{
    private Conexion _conexion = null;
    private Connection _con = null;
    private CallableStatement _cst = null;
    private ResultSet _rst = null;
    private Logger _logger = (Logger) LoggerFactory.getLogger(ClienteRE.class);

    public ClienteRE()
    {
        _conexion = new Conexion();
    }

    public ObjetoClienteMO CrearCliente(ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_CREAR_CLIENTE + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            _cst.setString(Constante._1, clienteMO.getIdInternoCliente());
            _cst.setString(Constante._2, clienteMO.getDenominacion());
            _cst.setString(Constante._3, clienteMO.getDocumento());
            _cst.setInt(Constante._4, clienteMO.getIdTipoDocumento());
            _cst.setInt(Constante._5, clienteMO.getIdTipoEntidad());
            _cst.setInt(Constante._6, clienteMO.getIdCategoria());
            _cst.setInt(Constante._7, clienteMO.getIdEmpresa());
            _cst.setString(Constante._8, clienteMO.getDireccion());
            _cst.setString(Constante._9, clienteMO.getTelefono());
            _cst.setString(Constante._10, clienteMO.getEmail());
            _cst.setString(Constante._11, clienteMO.getDescripcionTipoDocumento());
            if (clienteMO.getIdPaisDocumento() != null)
            {
                _cst.setInt(Constante._12, clienteMO.getIdPaisDocumento());
            }
            else
            {
                _cst.setNull(Constante._12, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._13, clienteMO.getDescripcionPaisDocumento());
            _cst.setString(Constante._14, clienteMO.getDescripcionTipoEntidad());
            _cst.setString(Constante._15, clienteMO.getNombreCliente());
            _cst.setString(Constante._16, clienteMO.getPrimerNombre());
            _cst.setString(Constante._17, clienteMO.getSegundoNombre());
            _cst.setString(Constante._18, clienteMO.getPrimerApellido());
            _cst.setString(Constante._19, clienteMO.getSegundoApellido());
            _cst.setString(Constante._20, clienteMO.getContacto());
            if (clienteMO.getIdPaisResidencia() != null)
            {
                _cst.setInt(Constante._21, clienteMO.getIdPaisResidencia());
            }
            else
            {
                _cst.setNull(Constante._21, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._22, clienteMO.getDescripcionPaisResidencia());
            if (clienteMO.getIdPaisOrigen() != null)
            {
                _cst.setInt(Constante._23, clienteMO.getIdPaisOrigen());
            }
            else
            {
                _cst.setNull(Constante._23, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._24, clienteMO.getDescripcionPaisOrigen());
            _cst.setString(Constante._25, clienteMO.getObservaciones());
            _cst.setString(Constante._26, clienteMO.getDescripcionCategoria());
            _cst.setString(Constante._27, clienteMO.getCargoPEP());
            if (clienteMO.getPEP() != null)
            {
                _cst.setBoolean(Constante._28, clienteMO.getPEP());
            }
            else
            {
                _cst.setNull(Constante._28, java.sql.Types.BIT);
            }
            if (clienteMO.getIdEstadoCivil() != null)
            {
                _cst.setInt(Constante._29, clienteMO.getIdEstadoCivil());
            }
            else
            {
                _cst.setNull(Constante._29, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._30, clienteMO.getDescripcionEstadoCivil());
            _cst.setString(Constante._31, clienteMO.getNaturalezaDeRelacion());
            _cst.setString(Constante._32, clienteMO.getProfesion());
            _cst.setString(Constante._33, clienteMO.getOrigenDeFondos());
            _cst.setString(Constante._34, clienteMO.getFechaNacimiento());
            _cst.setString(Constante._35, clienteMO.getLugarNacimiento());
            if (clienteMO.getIncluidoEnMatching() != null)
            {
                _cst.setBoolean(Constante._36, clienteMO.getIncluidoEnMatching());
            }
            else
            {
                _cst.setNull(Constante._36, java.sql.Types.BIT);
            }
            _cst.setString(Constante._37, clienteMO.getDescripcionEmpresa());
            if (clienteMO.getIncluidoEnMatchingIntegrantes() != null)
            {
                _cst.setBoolean(Constante._38, clienteMO.getIncluidoEnMatchingIntegrantes());
            }
            else
            {
                _cst.setNull(Constante._38, java.sql.Types.BIT);
            }
            if (clienteMO.getIdRiesgo() != null)
            {
                _cst.setInt(Constante._39, clienteMO.getIdRiesgo());
            }
            else
            {
                _cst.setNull(Constante._39, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._40, clienteMO.getDescripcionRiesgo());
            _cst.setString(Constante._41, Constante.USUARIO_CREACION);
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoClienteMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoClienteMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoClienteMO;
    }

    public ObjetoClienteMO ActivarCliente(String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_ACTIVAR_CLIENTE + "(?, ?)}");
            _cst.setString(Constante._1, idInternoCliente);
            _cst.setString(Constante._2, Constante.USUARIO_EDICION);
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoClienteMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoClienteMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoClienteMO;
    }

    public ObjetoClienteMO EditarCliente(ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_EDITAR_CLIENTE + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            _cst.setString(Constante._1, clienteMO.getIdInternoCliente());
            _cst.setString(Constante._2, clienteMO.getDenominacion());
            _cst.setString(Constante._3, clienteMO.getDocumento());
            if (clienteMO.getIdTipoDocumento() != null)
            {
                _cst.setInt(Constante._4, clienteMO.getIdTipoDocumento());
            }
            else
            {
                _cst.setNull(Constante._4, java.sql.Types.INTEGER);
            }
            if (clienteMO.getIdTipoEntidad() != null)
            {
                _cst.setInt(Constante._5, clienteMO.getIdTipoEntidad());
            }
            else
            {
                _cst.setNull(Constante._5, java.sql.Types.INTEGER);
            }
            if (clienteMO.getIdCategoria() != null)
            {
                _cst.setInt(Constante._6, clienteMO.getIdCategoria());
            }
            else
            {
                _cst.setNull(Constante._6, java.sql.Types.INTEGER);
            }
            if (clienteMO.getIdEmpresa() != null)
            {
                _cst.setInt(Constante._7, clienteMO.getIdEmpresa());
            }
            else
            {
                _cst.setNull(Constante._7, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._8, clienteMO.getDireccion());
            _cst.setString(Constante._9, clienteMO.getTelefono());
            _cst.setString(Constante._10, clienteMO.getEmail());
            _cst.setString(Constante._11, clienteMO.getDescripcionTipoDocumento());
            if (clienteMO.getIdPaisDocumento() != null)
            {
                _cst.setInt(Constante._12, clienteMO.getIdPaisDocumento());
            }
            else
            {
                _cst.setNull(Constante._12, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._13, clienteMO.getDescripcionPaisDocumento());
            _cst.setString(Constante._14, clienteMO.getDescripcionTipoDocumento());
            _cst.setString(Constante._15, clienteMO.getNombreCliente());
            _cst.setString(Constante._16, clienteMO.getPrimerNombre());
            _cst.setString(Constante._17, clienteMO.getSegundoNombre());
            _cst.setString(Constante._18, clienteMO.getPrimerApellido());
            _cst.setString(Constante._19, clienteMO.getSegundoApellido());
            _cst.setString(Constante._20, clienteMO.getContacto());
            if (clienteMO.getIdPaisResidencia() != null)
            {
                _cst.setInt(Constante._21, clienteMO.getIdPaisResidencia());
            }
            else
            {
                _cst.setNull(Constante._21, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._22, clienteMO.getDescripcionPaisResidencia());
            if (clienteMO.getIdPaisOrigen() != null)
            {
                _cst.setInt(Constante._23, clienteMO.getIdPaisOrigen());
            }
            else
            {
                _cst.setNull(Constante._23, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._24, clienteMO.getDescripcionPaisOrigen());
            _cst.setString(Constante._25, clienteMO.getObservaciones());
            _cst.setString(Constante._26, clienteMO.getDescripcionCategoria());
            _cst.setString(Constante._27, clienteMO.getCargoPEP());
            if (clienteMO.getPEP() != null)
            {
                _cst.setBoolean(Constante._28, clienteMO.getPEP());
            }
            else
            {
                _cst.setNull(Constante._28, java.sql.Types.BIT);
            }
            if (clienteMO.getIdEstadoCivil() != null)
            {
                _cst.setInt(Constante._29, clienteMO.getIdEstadoCivil());
            }
            else
            {
                _cst.setNull(Constante._29, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._30, clienteMO.getDescripcionEstadoCivil());
            _cst.setString(Constante._31, clienteMO.getNaturalezaDeRelacion());
            _cst.setString(Constante._32, clienteMO.getProfesion());
            _cst.setString(Constante._33, clienteMO.getOrigenDeFondos());
            _cst.setString(Constante._34, clienteMO.getFechaNacimiento());
            _cst.setString(Constante._35, clienteMO.getLugarNacimiento());
            if (clienteMO.getIncluidoEnMatching() != null)
            {
                _cst.setBoolean(Constante._36, clienteMO.getIncluidoEnMatching());
            }
            else
            {
                _cst.setNull(Constante._36, java.sql.Types.BIT);
            }
            _cst.setString(Constante._37, clienteMO.getDescripcionEmpresa());
            if (clienteMO.getIncluidoEnMatchingIntegrantes() != null)
            {
                _cst.setBoolean(Constante._38, clienteMO.getIncluidoEnMatchingIntegrantes());
            }
            else
            {
                _cst.setNull(Constante._38, java.sql.Types.BIT);
            }
            if (clienteMO.getIdRiesgo() != null)
            {
                _cst.setInt(Constante._39, clienteMO.getIdRiesgo());
            }
            else
            {
                _cst.setNull(Constante._39, java.sql.Types.INTEGER);
            }
            _cst.setString(Constante._40, clienteMO.getDescripcionRiesgo());
            _cst.setString(Constante._41, Constante.USUARIO_EDICION);
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoClienteMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoClienteMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoClienteMO;
    }

    public ObjetoClienteMO InactivarCliente(String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            _con = _conexion.ObtenerConexion();
            _cst = _con.prepareCall("{call " + Constante.SP_INACTIVAR_CLIENTE + "(?, ?)}");
            _cst.setString(Constante._1, idInternoCliente);
            _cst.setString(Constante._2, Constante.USUARIO_EDICION);
            _cst.executeQuery();
            _rst = _cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoClienteMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoClienteMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoClienteMO;
    }
}
