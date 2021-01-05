package com.grupogloria.splaservicio.Negocio;

import com.grupogloria.splaservicio.Interfaz.TokenIN;
import com.grupogloria.splaservicio.Modelo.ObjetoTokenMO;
import com.grupogloria.splaservicio.Modelo.TokenMO;
import com.grupogloria.splaservicio.Repositorio.TokenRE;

public class TokenNE implements TokenIN
{
    private TokenRE tokenRE;

    public TokenNE() throws Exception
    {
        tokenRE = new TokenRE();
    }

    public ObjetoTokenMO ObtenerToken() throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            objetoTokenMO = tokenRE.ObtenerToken();
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoTokenMO;
    }
    
    public ObjetoTokenMO RegistrarToken(TokenMO tokenMO) throws Exception
    {
        ObjetoTokenMO objetoTokenMO = new ObjetoTokenMO();
        try
        {
            objetoTokenMO = tokenRE.RegistrarToken(tokenMO);
        }
        catch (Exception e)
        {
            throw e;
        }
        return objetoTokenMO;
    }
}
