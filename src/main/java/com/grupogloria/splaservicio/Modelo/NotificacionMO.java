package com.grupogloria.splaservicio.Modelo;

import java.util.List;

public class NotificacionMO
{
    private String NombreArchivo;
    private List<ArchivoMO> ListaArchivos;
    private String De;
    private String Para;
    private String Asunto;
    private String Entidad;
    private String FechaInicial;
    private String FechaFinal;

    public String getNombreArchivo() {
        return NombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        NombreArchivo = nombreArchivo;
    }

    public List<ArchivoMO> getListaArchivos() {
        return ListaArchivos;
    }

    public void setListaArchivos(List<ArchivoMO> listaArchivos) {
        ListaArchivos = listaArchivos;
    }

    public String getDe() {
        return De;
    }

    public void setDe(String de) {
        this.De = de;
    }

    public String getPara() {
        return Para;
    }

    public void setPara(String para) {
        Para = para;
    }

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String asunto) {
        Asunto = asunto;
    }
    
    public String getEntidad() {
        return Entidad;
    }

    public void setEntidad(String entidad) {
        Entidad = entidad;
    }

    public String getFechaInicial() {
        return FechaInicial;
    }

    public void setFechaInicial(String fechaInicial) {
        FechaInicial = fechaInicial;
    }

    public String getFechaFinal() {
        return FechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        FechaFinal = fechaFinal;
    }
}
