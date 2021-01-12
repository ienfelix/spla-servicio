package com.grupogloria.splaservicio.Modelo;

public class CumploMO
{
    private String Url;
    private String Username;
    private String Password;
    private String Api;
    private String Login;
    private String Entidad;
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

    public String getApi() {
        return Api;
    }

    public void setApi(String api) {
        Api = api;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getEntidad() {
        return Entidad;
    }

    public void setEntidad(String entidad) {
        Entidad = entidad;
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
