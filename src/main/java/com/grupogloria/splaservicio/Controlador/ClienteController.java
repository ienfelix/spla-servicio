package com.grupogloria.splaservicio.Controlador;

import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Modelo.ClienteMO;
import com.grupogloria.splaservicio.Modelo.ObjetoClienteMO;
import com.grupogloria.splaservicio.Negocio.ClienteNE;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constante.CLIENTE_CONTROLADOR)
public class ClienteController
{
    private ClienteNE _clienteNE = null;

    public ClienteController() throws Exception
    {
        _clienteNE = new ClienteNE();
    }

    @GetMapping(produces = Constante.APPLICATION_JSON)
    public String Get()
    {
        return String.format(Constante.SERVICIO_ACTIVO, Constante.CLIENTE_CONTROLADOR);
    }

    @PostMapping(path = Constante.CREAR_CLIENTE, produces = Constante.APPLICATION_JSON)
    public ObjetoClienteMO CrearCliente(@RequestBody ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            objetoClienteMO = _clienteNE.CrearCliente(clienteMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoClienteMO;
    }

    @PutMapping(path = Constante.EDITAR_CLIENTE, produces = Constante.APPLICATION_JSON)
    public ObjetoClienteMO EditarCliente(@RequestBody ClienteMO clienteMO) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            objetoClienteMO = _clienteNE.EditarCliente(clienteMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoClienteMO;
    }

    @DeleteMapping(path = Constante.ANULAR_CLIENTE, produces = Constante.APPLICATION_JSON)
    public ObjetoClienteMO AnularCliente(@RequestParam String idInternoCliente) throws Exception
    {
        ObjetoClienteMO objetoClienteMO = new ObjetoClienteMO();
        try
        {
            objetoClienteMO = _clienteNE.AnularCliente(idInternoCliente);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoClienteMO;
    }
}
