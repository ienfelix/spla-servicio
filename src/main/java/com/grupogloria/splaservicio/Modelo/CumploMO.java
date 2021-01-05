package com.grupogloria.splaservicio.Modelo;

import com.grupogloria.splaservicio.Comun.Constante;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Constante.CUMPLO_PREFIX)
public class CumploMO
{
    private String Url;
    private String Username;
    private String Password;
    public Integer Status;
    public String Descripcion;
    public Boolean Exito;
    public String Detalle;
    public ClienteMO Cliente;
    
    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
    
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Boolean getExito() {
        return Exito;
    }

    public void setExito(Boolean exito) {
        Exito = exito;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }

    public ClienteMO getCliente() {
        return Cliente;
    }

    public void setCliente(ClienteMO cliente) {
        Cliente = cliente;
    }
}
