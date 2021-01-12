package com.grupogloria.splaservicio.Modelo;

public class ConexionMO
{
    private String Driver;
    private String Url;
    private String Username;
    private String Password;

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        this.Driver = driver;
    }
    
    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        this.Url = url;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}