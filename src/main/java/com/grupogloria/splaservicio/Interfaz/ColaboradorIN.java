package com.grupogloria.splaservicio.Interfaz;

import com.grupogloria.splaservicio.Modelo.ColaboradorMO;
import com.grupogloria.splaservicio.Modelo.ObjetoColaboradorMO;

public interface ColaboradorIN
{
    public ObjetoColaboradorMO CrearColaborador(ColaboradorMO colaboradorMO) throws Exception;
    public ObjetoColaboradorMO ActivarColaborador(String idInternoCliente) throws Exception;
    public ObjetoColaboradorMO EditarColaborador(ColaboradorMO colaboradorMO) throws Exception;
    public ObjetoColaboradorMO InactivarColaborador(String idInternoCliente) throws Exception;
}