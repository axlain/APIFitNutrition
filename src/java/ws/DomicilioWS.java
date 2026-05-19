package ws;

import com.google.gson.Gson;
import dominio.DomicilioImp;
import dto.Respuesta;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Domicilio;

@Path("domicilio")
public class DomicilioWS {

    // =========================================
    // REGISTRAR DOMICILIO
    // =========================================
    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Domicilio domicilio = gson.fromJson(json, Domicilio.class);
            return DomicilioImp.registrar(domicilio);
        } catch (Exception e) {
            Respuesta r = new Respuesta();
            r.setError(true);
            r.setMensaje("Error al registrar domicilio: " + e.getMessage());
            return r;
        }
    }
}