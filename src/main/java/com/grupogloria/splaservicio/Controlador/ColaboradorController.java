package com.grupogloria.splaservicio.Controlador;

import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Modelo.ColaboradorMO;
import com.grupogloria.splaservicio.Modelo.ObjetoColaboradorMO;
import com.grupogloria.splaservicio.Negocio.ColaboradorNE;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constante.COLABORADOR_CONTROLADOR)
public class ColaboradorController
{
    private ColaboradorNE _colaboradorNE = null;

    public ColaboradorController() throws Exception
    {
        _colaboradorNE = new ColaboradorNE();
    }

    @GetMapping(produces = Constante.APPLICATION_JSON)
    public String Get()
    {
        return String.format(Constante.SERVICIO_ACTIVO, Constante.COLABORADOR_CONTROLADOR);
    }

    @PostMapping(path = Constante.CREAR_COLABORADOR, produces = Constante.APPLICATION_JSON)
    public ObjetoColaboradorMO CrearColaborador(@RequestBody ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            objetoColaboradorMO = _colaboradorNE.CrearColaborador(colaboradorMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoColaboradorMO;
    }

    @PutMapping(path = Constante.EDITAR_COLABORADOR, produces = Constante.APPLICATION_JSON)
    public ObjetoColaboradorMO EditarColaborador(@RequestBody ColaboradorMO colaboradorMO) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            objetoColaboradorMO = _colaboradorNE.EditarColaborador(colaboradorMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoColaboradorMO;
    }

    @DeleteMapping(path = Constante.ANULAR_COLABORADOR, produces = Constante.APPLICATION_JSON)
    public ObjetoColaboradorMO AnularColaborador(@RequestParam String idInternoCliente) throws Exception
    {
        ObjetoColaboradorMO objetoColaboradorMO = new ObjetoColaboradorMO();
        try
        {
            objetoColaboradorMO = _colaboradorNE.AnularColaborador(idInternoCliente);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoColaboradorMO;
    }
}
