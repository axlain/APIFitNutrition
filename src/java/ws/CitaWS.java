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
        Respuesta respuesta = new Respuesta();

        try {

            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Cita cita = gson.fromJson(json, Cita.class);

            if (cita == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la cita es obligatoria.");
                return respuesta;
            }

            if (cita.getIdPaciente() == null || cita.getIdPaciente() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El paciente es obligatorio.");
                return respuesta;
            }

            if (cita.getIdMedico() == null || cita.getIdMedico() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El médico es obligatorio.");
                return respuesta;
            }

            if (cita.getFecha() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La fecha de la cita es obligatoria.");
                return respuesta;
            }

            if (cita.getHora() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La hora de la cita es obligatoria.");
                return respuesta;
            }

            return CitaImp.registrar(cita);

        } catch (JsonSyntaxException e) {

            throw new BadRequestException(
                    "Formato JSON inválido: " + e.getMessage()
            );
        }
    }

    @Path("reagendar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta reagendar(String json) {

        Gson gson = new Gson();
        Respuesta respuesta = new Respuesta();

        try {

            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Cita cita = gson.fromJson(json, Cita.class);

            if (cita == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la cita es obligatoria.");
                return respuesta;
            }

            if (cita.getIdCita() == null || cita.getIdCita() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador de la cita es obligatorio.");
                return respuesta;
            }

            if (cita.getFecha() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La nueva fecha es obligatoria.");
                return respuesta;
            }

            if (cita.getHora() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La nueva hora es obligatoria.");
                return respuesta;
            }

            return CitaImp.reagendar(cita);

        } catch (JsonSyntaxException e) {

            throw new BadRequestException(
                    "Formato JSON inválido: " + e.getMessage()
            );
        }
    }

    @Path("cancelar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelar(String json) {

        Gson gson = new Gson();
        Respuesta respuesta = new Respuesta();

        try {

            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Cita cita = gson.fromJson(json, Cita.class);

            if (cita == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la cita es obligatoria.");
                return respuesta;
            }

            if (cita.getIdCita() == null || cita.getIdCita() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador de la cita es obligatorio.");
                return respuesta;
            }

            return CitaImp.cancelar(cita);

        } catch (JsonSyntaxException e) {

            throw new BadRequestException(
                    "Formato JSON inválido: " + e.getMessage()
            );
        }
    }

    @Path("obtener-por-medico")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerPorMedico(
            @QueryParam("idMedico") Integer idMedico,
            @QueryParam("fecha") String fecha
    ) {

        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException(
                    "El id del médico es obligatorio."
            );
        }

        if (fecha == null || fecha.trim().isEmpty()) {
            throw new BadRequestException(
                    "La fecha es obligatoria."
            );
        }

        return CitaImp.obtenerPorMedico(idMedico, fecha);
    }

    @Path("obtener-vigentes-paciente/{idPaciente}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerVigentesPaciente(
            @PathParam("idPaciente") Integer idPaciente
    ) {

        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException(
                    "El id del paciente es obligatorio."
            );
        }

        return CitaImp.obtenerVigentesPaciente(idPaciente);
    }

    @Path("obtener-todas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerTodas() {
        return CitaImp.obtenerTodas();
    }

    @Path("obtener-por-fecha/{fecha}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Cita> obtenerPorFecha(@PathParam("fecha") String fecha) {

        if (fecha == null || fecha.trim().isEmpty()) {
            throw new BadRequestException("La fecha es obligatoria.");
        }

        return CitaImp.obtenerPorFecha(fecha);
    }

    @Path("horas-disponibles")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> obtenerHorasDisponibles(
            @QueryParam("idMedico") Integer idMedico,
            @QueryParam("fecha") String fecha,
            @QueryParam("idCita") Integer idCita
    ) {

        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException(
                    "El id del médico es obligatorio."
            );
        }

        if (fecha == null || fecha.trim().isEmpty()) {
            throw new BadRequestException(
                    "La fecha es obligatoria."
            );
        }

        return CitaImp.obtenerHorasDisponibles(idMedico, fecha, idCita);
    }
}