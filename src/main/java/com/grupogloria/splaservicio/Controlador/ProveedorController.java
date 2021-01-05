package com.grupogloria.splaservicio.Controlador;

import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Modelo.ObjetoProveedorMO;
import com.grupogloria.splaservicio.Modelo.ProveedorMO;
import com.grupogloria.splaservicio.Negocio.ProveedorNE;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constante.PROVEEDOR_CONTROLADOR)
public class ProveedorController
{
    private ProveedorNE _proveedorNE = null;

    public ProveedorController() throws Exception
    {
        _proveedorNE = new ProveedorNE();
    }

    @GetMapping(produces = Constante.APPLICATION_JSON)
    public String Get()
    {
        return String.format(Constante.SERVICIO_ACTIVO, Constante.PROVEEDOR_CONTROLADOR);
    }

    @PostMapping(path = Constante.CREAR_PROVEEDOR, produces = Constante.APPLICATION_JSON)
    public ObjetoProveedorMO CrearProveedor(@RequestBody ProveedorMO proveedorMO) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            objetoProveedorMO = _proveedorNE.CrearProveedor(proveedorMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoProveedorMO;
    }

    @PutMapping(path = Constante.EDITAR_PROVEEDOR, produces = Constante.APPLICATION_JSON)
    public ObjetoProveedorMO EditarProveedor(@RequestBody ProveedorMO proveedorMO) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            objetoProveedorMO = _proveedorNE.EditarProveedor(proveedorMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoProveedorMO;
    }

    @DeleteMapping(path = Constante.ANULAR_PROVEEDOR, produces = Constante.APPLICATION_JSON)
    public ObjetoProveedorMO anularProveedorMO(@RequestParam String idInternoCliente) throws Exception
    {
        ObjetoProveedorMO objetoProveedorMO = new ObjetoProveedorMO();
        try
        {
            objetoProveedorMO = _proveedorNE.AnularProveedor(idInternoCliente);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoProveedorMO;
    }
}
