package com.grupogloria.splaservicio.Interfaz;

import com.grupogloria.splaservicio.Modelo.ObjetoProveedorMO;
import com.grupogloria.splaservicio.Modelo.ProveedorMO;

public interface ProveedorIN
{
    public ObjetoProveedorMO CrearProveedor(ProveedorMO ProveedorMO) throws Exception;
    public ObjetoProveedorMO ActivarProveedor(String idInternoProveedor) throws Exception;
    public ObjetoProveedorMO EditarProveedor(ProveedorMO ProveedorMO) throws Exception;
    public ObjetoProveedorMO InactivarProveedor(String idInternoProveedor) throws Exception;
}
