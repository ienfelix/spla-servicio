package com.grupogloria.splaservicio.Repositorio;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.grupogloria.splaservicio.Comun.Conexion;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Interfaz.ColaboradorIN;
import com.grupogloria.splaservicio.Modelo.ColaboradorMO;
import com.grupogloria.splaservicio.Modelo.ObjetoColaboradorMO;

public class ColaboradorRE implements ColaboradorIN
{
    private Conexion _conexion = null;
    private Connection _con = null;
    private CallableStatement _cst = null;
    private ResultSet _rst = null;
    private Log _log = null;

    public ColaboradorRE() throws Exception
    {
       _conexion = new Conexion();
       _log = new Log(ColaboradorRE.class.getName(), Constante.ENTIDAD_COLABORADOR);
    }

    public ObjetoColaboradorMO CrearColaborador(ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
           _con =_conexion.ObtenerConexion();
           _cst =_con.prepareCall("{call " + Constante.SP_CREAR_COLABORADOR + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
           _cst.setString(Constante._1, colaboradorMO.getIdInternoCliente());
           _cst.setString(Constante._2, colaboradorMO.getDenominacion());
           _cst.setString(Constante._3, colaboradorMO.getDocumento());
           _cst.setInt(Constante._4, colaboradorMO.getIdTipoDocumento());
           _cst.setInt(Constante._5, colaboradorMO.getIdTipoEntidad());
           _cst.setInt(Constante._6, colaboradorMO.getIdCategoria());
           _cst.setInt(Constante._7, colaboradorMO.getIdEmpresa());
           _cst.setString(Constante._8, colaboradorMO.getDireccion());
           _cst.setString(Constante._9, colaboradorMO.getTelefono());
           _cst.setString(Constante._10, colaboradorMO.getEmail());
           _cst.setString(Constante._11, colaboradorMO.getDescripcionTipoDocumento());
            if (colaboradorMO.getIdPaisDocumento() != null)
            {
               _cst.setInt(Constante._12, colaboradorMO.getIdPaisDocumento());
            }
            else
            {
               _cst.setNull(Constante._12, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._13, colaboradorMO.getDescripcionPaisDocumento());
           _cst.setString(Constante._14, colaboradorMO.getDescripcionTipoEntidad());
           _cst.setString(Constante._15, colaboradorMO.getNombreCliente());
           _cst.setString(Constante._16, colaboradorMO.getPrimerNombre());
           _cst.setString(Constante._17, colaboradorMO.getSegundoNombre());
           _cst.setString(Constante._18, colaboradorMO.getPrimerApellido());
           _cst.setString(Constante._19, colaboradorMO.getSegundoApellido());
           _cst.setString(Constante._20, colaboradorMO.getContacto());
            if (colaboradorMO.getIdPaisResidencia() != null)
            {
               _cst.setInt(Constante._21, colaboradorMO.getIdPaisResidencia());
            }
            else
            {
               _cst.setNull(Constante._21, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._22, colaboradorMO.getDescripcionPaisResidencia());
            if (colaboradorMO.getIdPaisOrigen() != null)
            {
               _cst.setInt(Constante._23, colaboradorMO.getIdPaisOrigen());
            }
            else
            {
               _cst.setNull(Constante._23, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._24, colaboradorMO.getDescripcionPaisOrigen());
           _cst.setString(Constante._25, colaboradorMO.getObservaciones());
           _cst.setString(Constante._26, colaboradorMO.getDescripcionCategoria());
           _cst.setString(Constante._27, colaboradorMO.getCargoPEP());
            if (colaboradorMO.getPEP() != null)
            {
               _cst.setBoolean(Constante._28, colaboradorMO.getPEP());
            }
            else
            {
               _cst.setNull(Constante._28, java.sql.Types.BIT);
            }
            if (colaboradorMO.getIdEstadoCivil() != null)
            {
               _cst.setInt(Constante._29, colaboradorMO.getIdEstadoCivil());
            }
            else
            {
               _cst.setNull(Constante._29, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._30, colaboradorMO.getDescripcionEstadoCivil());
           _cst.setString(Constante._31, colaboradorMO.getNaturalezaDeRelacion());
           _cst.setString(Constante._32, colaboradorMO.getProfesion());
           _cst.setString(Constante._33, colaboradorMO.getOrigenDeFondos());
           _cst.setString(Constante._34, colaboradorMO.getFechaNacimiento());
           _cst.setString(Constante._35, colaboradorMO.getLugarNacimiento());
            if (colaboradorMO.getIncluidoEnMatching() != null)
            {
               _cst.setBoolean(Constante._36, colaboradorMO.getIncluidoEnMatching());
            }
            else
            {
               _cst.setNull(Constante._36, java.sql.Types.BIT);
            }
           _cst.setString(Constante._37, colaboradorMO.getDescripcionEmpresa());
            if (colaboradorMO.getIncluidoEnMatchingIntegrantes() != null)
            {
               _cst.setBoolean(Constante._38, colaboradorMO.getIncluidoEnMatchingIntegrantes());
            }
            else
            {
               _cst.setNull(Constante._38, java.sql.Types.BIT);
            }
            if (colaboradorMO.getIdRiesgo() != null)
            {
               _cst.setInt(Constante._39, colaboradorMO.getIdRiesgo());
            }
            else
            {
               _cst.setNull(Constante._39, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._40, colaboradorMO.getDescripcionRiesgo());
           _cst.setString(Constante._41, Constante.USUARIO_CREACION);
           _cst.executeQuery();
           _rst =_cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoColaboradorMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoColaboradorMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO ActivarColaborador(String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
           _con =_conexion.ObtenerConexion();
           _cst =_con.prepareCall("{call " + Constante.SP_ACTIVAR_COLABORADOR + "(?, ?)}");
           _cst.setString(Constante._1, idInternoCliente);
           _cst.setString(Constante._2, Constante.USUARIO_EDICION);
           _cst.executeQuery();
           _rst =_cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoColaboradorMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoColaboradorMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO EditarColaborador(ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
           _con =_conexion.ObtenerConexion();
           _cst =_con.prepareCall("{call " + Constante.SP_EDITAR_COLABORADOR + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
           _cst.setString(Constante._1, colaboradorMO.getIdInternoCliente());
           _cst.setString(Constante._2, colaboradorMO.getDenominacion());
           _cst.setString(Constante._3, colaboradorMO.getDocumento());
            if (colaboradorMO.getIdTipoDocumento() != null)
            {
               _cst.setInt(Constante._4, colaboradorMO.getIdTipoDocumento());
            }
            else
            {
               _cst.setNull(Constante._4, java.sql.Types.INTEGER);
            }
            if (colaboradorMO.getIdTipoEntidad() != null)
            {
               _cst.setInt(Constante._5, colaboradorMO.getIdTipoEntidad());
            }
            else
            {
               _cst.setNull(Constante._5, java.sql.Types.INTEGER);
            }
            if (colaboradorMO.getIdCategoria() != null)
            {
               _cst.setInt(Constante._6, colaboradorMO.getIdCategoria());
            }
            else
            {
               _cst.setNull(Constante._6, java.sql.Types.INTEGER);
            }
            if (colaboradorMO.getIdEmpresa() != null)
            {
               _cst.setInt(Constante._7, colaboradorMO.getIdEmpresa());
            }
            else
            {
               _cst.setNull(Constante._7, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._8, colaboradorMO.getDireccion());
           _cst.setString(Constante._9, colaboradorMO.getTelefono());
           _cst.setString(Constante._10, colaboradorMO.getEmail());
           _cst.setString(Constante._11, colaboradorMO.getDescripcionTipoDocumento());
            if (colaboradorMO.getIdPaisDocumento() != null)
            {
               _cst.setInt(Constante._12, colaboradorMO.getIdPaisDocumento());
            }
            else
            {
               _cst.setNull(Constante._12, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._13, colaboradorMO.getDescripcionPaisDocumento());
           _cst.setString(Constante._14, colaboradorMO.getDescripcionTipoDocumento());
           _cst.setString(Constante._15, colaboradorMO.getNombreCliente());
           _cst.setString(Constante._16, colaboradorMO.getPrimerNombre());
           _cst.setString(Constante._17, colaboradorMO.getSegundoNombre());
           _cst.setString(Constante._18, colaboradorMO.getPrimerApellido());
           _cst.setString(Constante._19, colaboradorMO.getSegundoApellido());
           _cst.setString(Constante._20, colaboradorMO.getContacto());
            if (colaboradorMO.getIdPaisResidencia() != null)
            {
               _cst.setInt(Constante._21, colaboradorMO.getIdPaisResidencia());
            }
            else
            {
               _cst.setNull(Constante._21, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._22, colaboradorMO.getDescripcionPaisResidencia());
            if (colaboradorMO.getIdPaisOrigen() != null)
            {
               _cst.setInt(Constante._23, colaboradorMO.getIdPaisOrigen());
            }
            else
            {
               _cst.setNull(Constante._23, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._24, colaboradorMO.getDescripcionPaisOrigen());
           _cst.setString(Constante._25, colaboradorMO.getObservaciones());
           _cst.setString(Constante._26, colaboradorMO.getDescripcionCategoria());
           _cst.setString(Constante._27, colaboradorMO.getCargoPEP());
            if (colaboradorMO.getPEP() != null)
            {
               _cst.setBoolean(Constante._28, colaboradorMO.getPEP());
            }
            else
            {
               _cst.setNull(Constante._28, java.sql.Types.BIT);
            }
            if (colaboradorMO.getIdEstadoCivil() != null)
            {
               _cst.setInt(Constante._29, colaboradorMO.getIdEstadoCivil());
            }
            else
            {
               _cst.setNull(Constante._29, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._30, colaboradorMO.getDescripcionEstadoCivil());
           _cst.setString(Constante._31, colaboradorMO.getNaturalezaDeRelacion());
           _cst.setString(Constante._32, colaboradorMO.getProfesion());
           _cst.setString(Constante._33, colaboradorMO.getOrigenDeFondos());
           _cst.setString(Constante._34, colaboradorMO.getFechaNacimiento());
           _cst.setString(Constante._35, colaboradorMO.getLugarNacimiento());
            if (colaboradorMO.getIncluidoEnMatching() != null)
            {
               _cst.setBoolean(Constante._36, colaboradorMO.getIncluidoEnMatching());
            }
            else
            {
               _cst.setNull(Constante._36, java.sql.Types.BIT);
            }
           _cst.setString(Constante._37, colaboradorMO.getDescripcionEmpresa());
            if (colaboradorMO.getIncluidoEnMatchingIntegrantes() != null)
            {
               _cst.setBoolean(Constante._38, colaboradorMO.getIncluidoEnMatchingIntegrantes());
            }
            else
            {
               _cst.setNull(Constante._38, java.sql.Types.BIT);
            }
            if (colaboradorMO.getIdRiesgo() != null)
            {
               _cst.setInt(Constante._39, colaboradorMO.getIdRiesgo());
            }
            else
            {
               _cst.setNull(Constante._39, java.sql.Types.INTEGER);
            }
           _cst.setString(Constante._40, colaboradorMO.getDescripcionRiesgo());
           _cst.setString(Constante._41, Constante.USUARIO_EDICION);
           _cst.executeQuery();
           _rst =_cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoColaboradorMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoColaboradorMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoColaboradorMO;
    }

    public ObjetoColaboradorMO InactivarColaborador(String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
           _con =_conexion.ObtenerConexion();
           _cst =_con.prepareCall("{call " + Constante.SP_INACTIVAR_COLABORADOR + "(?, ?)}");
           _cst.setString(Constante._1, idInternoCliente);
           _cst.setString(Constante._2, Constante.USUARIO_EDICION);
           _cst.executeQuery();
           _rst =_cst.getResultSet();

            if (_rst.isBeforeFirst())
            {
                if (_rst.next())
                {
                    objetoColaboradorMO.setCodigo(_rst.getInt(Constante.CODIGO));
                    objetoColaboradorMO.setMensaje(_rst.getString(Constante.MENSAJE));
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
        return objetoColaboradorMO;
    }
}
