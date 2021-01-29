package com.grupogloria.splaservicio.Negocio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupogloria.splaservicio.Comun.Constante;
import com.grupogloria.splaservicio.Comun.Log;
import com.grupogloria.splaservicio.Comun.Util;
import com.grupogloria.splaservicio.Interfaz.NotificacionIN;
import com.grupogloria.splaservicio.Modelo.ArchivoMO;
import com.grupogloria.splaservicio.Modelo.NotificacionMO;
import com.grupogloria.splaservicio.Modelo.ObjetoNotificacionMO;
import com.grupogloria.splaservicio.Repositorio.NotificacionRE;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class NotificacionNE implements NotificacionIN {
    private NotificacionRE _notificacionRE = null;
    private ObjectMapper _objectMapper = null;
    private JavaMailSender _mailSender = null;
    private Log _log = null;

    public NotificacionNE() throws Exception {
        _notificacionRE = new NotificacionRE();
        _objectMapper = Util.GetObjectMapper();
        _log = new Log(NotificacionNE.class.getName(), "");
        _mailSender = Util.ObtenerMailSender();
    }

    public ObjetoNotificacionMO EnviarNotificacion(NotificacionMO notificacionMO) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        InputStream inputStream = null, inputStreamTemp = null;
        try
        {
            String parametros = _objectMapper.writeValueAsString(notificacionMO);
            _log.info(String.format(Constante.PARAMETROS_INGRESO, parametros));

            if (notificacionMO == null)
            {
                objetoNotificacionMO.setCodigo(Constante.CODIGO_NO_OK);
                objetoNotificacionMO.setMensaje(String.format(Constante.MENSAJE_ENTIDAD_VACIA, notificacionMO));
            }
            else
            {
                ClassPathResource classPathResource = new ClassPathResource(Constante.APPLICATION_PROPERTIES);
                inputStreamTemp = classPathResource.getInputStream();
                File tempFile = File.createTempFile(Constante.APPLICATION_PROPERTIES, null);
                FileUtils.copyInputStreamToFile(inputStreamTemp, tempFile);
                inputStream = new FileInputStream(tempFile);
                tempFile.delete();
                Properties properties = new Properties();
                properties.load(inputStream);
                String rutaBitacora = Util.IsNullOrEmpty(properties.getProperty(Constante.API_RUTA_BITACORA)) ? "" : properties.getProperty(Constante.API_RUTA_BITACORA);
                String nombreBitacora = Util.IsNullOrEmpty(properties.getProperty(Constante.API_NOMBRE_BITACORA)) ? "" : properties.getProperty(Constante.API_NOMBRE_BITACORA);
                properties.clear();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(new Date());
                String nombreServicio = notificacionMO.getEntidad() + Constante.DELIMITADOR_GUION_BAJO + nombreBitacora + Constante.DELIMITADOR_PUNTO + Constante.EXTENSION_TXT;
                String nombreConsola = nombreServicio.replace(Constante.SERVICIO, Constante.CONSOLA);
                String rutaServicio = rutaBitacora + Constante.DELIMITADOR_BARRA_OBLICUA + date + Constante.DELIMITADOR_GUION_BAJO + nombreServicio;
                String rutaConsola = rutaServicio.replace(Constante.SERVICIO, Constante.CONSOLA);
                String body = GetMessageBody(notificacionMO);
                String utf8 = StandardCharsets.UTF_8.name();
                InternetAddress[] internetAddress = InternetAddress.parse(notificacionMO.getPara());
                MimeMessage mimeMessage = _mailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, utf8);
                mimeMessageHelper.setFrom(notificacionMO.getDe());
                mimeMessageHelper.setTo(internetAddress);
                mimeMessageHelper.setSubject(notificacionMO.getAsunto());
                mimeMessageHelper.setText(body, true);
                File fileServicio = FileUtils.getFile(rutaServicio);
                File fileConsola = FileUtils.getFile(rutaConsola);
                mimeMessageHelper.addAttachment(nombreServicio, fileServicio);
                mimeMessageHelper.addAttachment(nombreConsola, fileConsola);
                _mailSender.send(mimeMessage);
                objetoNotificacionMO.setCodigo(Constante.CODIGO_OK);
                objetoNotificacionMO.setMensaje(String.format(Constante.MENSAJE_NOTIFICACION, Constante.MENSAJE_SI, notificacionMO.getPara()));
            }
        }
        catch (Exception e)
        {
            objetoNotificacionMO.setCodigo(Constante.CODIGO_NO_OK);
            objetoNotificacionMO.setMensaje(String.format(Constante.MENSAJE_NOTIFICACION, Constante.MENSAJE_SI, e.getMessage()));
            _log.error(e);
            throw e;
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
            if (inputStreamTemp != null)
            {
                inputStreamTemp.close();
            }
        }
		return objetoNotificacionMO;
    }
    
    private String GetMessageBody(NotificacionMO notificacionMO)
    {
        String cuerpo = "";
        try
        {
            if (notificacionMO != null && notificacionMO.getListaArchivos() != null && notificacionMO.getListaArchivos().size() > Constante._0)
            {
                cuerpo = "<html lang='en'>";
                cuerpo += "<head>";
                cuerpo += "<meta charset='utf-8'>";
                cuerpo += "<title>" + notificacionMO.getAsunto() + "</title>";
                cuerpo += "</head>";
                cuerpo += "<body>";
                cuerpo += "<table>";
                cuerpo += "<caption>Tipo de entidad procesada [" + notificacionMO.getEntidad() + "].</caption>";
                cuerpo += "<thead>";
                cuerpo += "<tr><th colspan='2'>El archivo comprimido procesado es [" + notificacionMO.getNombreArchivo() + "].</th></tr>";
                cuerpo += "</thead>";
                cuerpo += "<tbody>";

                for (int i = Constante._0; i < notificacionMO.getListaArchivos().size(); i++)
                {
                    ArchivoMO archivoMO = notificacionMO.getListaArchivos().get(i);
                    cuerpo += "<tr><td>Archivo Interno:</td><td>Resultado:</td></tr>";
                    cuerpo += "<tr>";
                    cuerpo += "<td>" + archivoMO.getNombreArchivo() + "</td>";
                    cuerpo += "<td>" + archivoMO.getMensaje() + "</td>";
                    cuerpo += "</tr>";
                    cuerpo += "<tr><td colspan='2' style='border-bottom: 1px solid black'></td></tr>";
                }

                cuerpo += "</tbody>";
                cuerpo += "</table>";
                cuerpo += "</body>";
                cuerpo += "</html>";
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return cuerpo;
    }

    public ObjetoNotificacionMO EnviarNotificacion(String json, String destinatario) throws Exception
    {
        ObjetoNotificacionMO objetoNotificacionMO = new ObjetoNotificacionMO();
        try
        {
            objetoNotificacionMO = _notificacionRE.EnviarNotificacion(json, destinatario);
        }
        catch (Exception e)
        {
            _log.error(e);
            throw e;
        }
        return objetoNotificacionMO;
    }
}
