package com.grupogloria.splaservicio.Modelo;

public class ObjetoTokenMO {
    private int Codigo;
    private String Mensaje;
    private TokenMO TokenMO;

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public TokenMO getTokenMO() {
        return TokenMO;
    }

    public void setTokenMO(TokenMO tokenMO) {
        TokenMO = tokenMO;
    }
}
