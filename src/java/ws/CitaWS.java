package ws;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dominio.CitaImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Cita;

@Path("cita")
public class CitaWS {

    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Cita cita = gson.fromJson(json, Cita.class);
            return CitaImp.registrar(cita);
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Formato JSON invalido: " + e.getMessage());
        }
    }

    @Path("reagendar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta reagendar(String json) {
        Gson gson = new Gson();
        try {
            Cita cita = gson.fromJson(json, Cita.class);
            return CitaImp.reagendar(cita);
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Formato JSON invalido: " + e.getMessage());
        }
    }

    @Path("cancelar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelar(String json) {
        Gson gson = new Gson();
        try {
            Cita cita = gson.fromJson(json, Cita.class);
            return CitaImp.cancelar(cita);
        } catch (JsonSyntaxException e) {
            throw new BadRequestException("Formato JSON invalido: " + e.getMessage());
        }
    }

    @Path("obtener-por-medico")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerPorMedico(@QueryParam("idMedico") Integer idMedico,
                                       @QueryParam("fecha") String fecha) {
        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException("El id del medico es obligatorio.");
        }
        if (fecha == null || fecha.trim().isEmpty()) {
            throw new BadRequestException("La fecha es obligatoria.");
        }
        return CitaImp.obtenerPorMedico(idMedico, fecha);
    }

    @Path("obtener-vigentes-paciente/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerVigentesPaciente(@PathParam("idPaciente") Integer idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException("El id del paciente es obligatorio.");
        }
        return CitaImp.obtenerVigentesPaciente(idPaciente);
    }
}
