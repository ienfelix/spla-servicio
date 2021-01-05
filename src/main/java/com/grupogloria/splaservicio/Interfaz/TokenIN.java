package com.grupogloria.splaservicio.Interfaz;

import com.grupogloria.splaservicio.Modelo.ObjetoTokenMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;

public interface TokenIN
{
    public ObjetoTokenMO ObtenerToken() throws Exception;
    public ObjetoTokenMO RegistrarToken(TokenMO tokenMO) throws Exception;
}
