package com.grupogloria.splaservicio.Interfaz;

import com.grupogloria.splaservicio.Modelo.ClienteMO;
import com.grupogloria.splaservicio.Modelo.ObjetoClienteMO;

public interface ClienteIN
{
    public ObjetoClienteMO CrearCliente(ClienteMO clienteMO) throws Exception;
    public ObjetoClienteMO ActivarCliente(String idInternoCliente) throws Exception;
    public ObjetoClienteMO EditarCliente(ClienteMO clienteMO) throws Exception;
    public ObjetoClienteMO InactivarCliente(String idInternoCliente) throws Exception;
}
