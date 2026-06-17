package ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dominio.ConsultaImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Consulta;

@Path("consulta")
public class ConsultaWS {

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String jsonInput) {
        Gson gson = new Gson();
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonInput == null || jsonInput.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Consulta consulta = gson.fromJson(jsonInput, Consulta.class);

            if (consulta == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la consulta es obligatoria.");
                return respuesta;
            }

            Respuesta validacion = validarConsultaCompleta(consulta);
            if (validacion.isError()) {
                return validacion;
            }

            return ConsultaImp.registrarConsulta(consulta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar consulta: " + e.getMessage());
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonInput) {
        Gson gson = new Gson();
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonInput == null || jsonInput.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Consulta consulta = gson.fromJson(jsonInput, Consulta.class);

            if (consulta == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la consulta es obligatoria.");
                return respuesta;
            }

            if (consulta.getIdConsulta() == null || consulta.getIdConsulta() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El ID de la consulta es obligatorio para editar.");
                return respuesta;
            }

            Respuesta validacion = validarConsultaCompleta(consulta);
            if (validacion.isError()) {
                return validacion;
            }

            return ConsultaImp.editarConsulta(consulta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar consulta: " + e.getMessage());
        }
    }

    @PUT
    @Path("cancelar-cita")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cancelarCita(String jsonInput) {
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonInput == null || jsonInput.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            JsonObject json = JsonParser.parseString(jsonInput).getAsJsonObject();

            Integer idCita = null;
            String motivoCancelacion = null;

            if (json.has("idCita") && !json.get("idCita").isJsonNull()) {
                idCita = json.get("idCita").getAsInt();
            }

            if (json.has("motivoCancelacion") && !json.get("motivoCancelacion").isJsonNull()) {
                motivoCancelacion = json.get("motivoCancelacion").getAsString();
            }

            if (idCita == null || idCita <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El ID de la cita es obligatorio.");
                return respuesta;
            }

            return ConsultaImp.cancelarCitaAsociada(idCita, motivoCancelacion);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para cancelar cita: " + e.getMessage());
        }
    }

    @GET
    @Path("obtener-todas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerTodas() {
        return ConsultaImp.obtenerTodas();
    }

    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Consulta buscarConsulta(
            @QueryParam("idConsulta") Integer idConsulta
    ) {
        if (idConsulta == null || idConsulta <= 0) {
            throw new BadRequestException("El id de la consulta es inválido.");
        }

        return ConsultaImp.buscarConsulta(idConsulta);
    }

    @GET
    @Path("historial/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerHistorialPaciente(
            @PathParam("idPaciente") Integer idPaciente
    ) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException("El id del paciente es inválido.");
        }

        return ConsultaImp.obtenerHistorialPaciente(idPaciente);
    }
    
    @GET
    @Path("historial-medico/{idMedico}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerHistorialMedico(
            @PathParam("idMedico") Integer idMedico
    ) {
        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException("El id del médico es inválido.");
        }

        return ConsultaImp.obtenerHistorialMedico(idMedico);
    }

    @GET
    @Path("obtener/{idConsulta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Consulta obtenerDetalleConsulta(
            @PathParam("idConsulta") Integer idConsulta
    ) {
        if (idConsulta == null || idConsulta <= 0) {
            throw new BadRequestException("El id de la consulta es inválido.");
        }

        return ConsultaImp.obtenerDetalleConsulta(idConsulta);
    }

    private Respuesta validarConsultaCompleta(Consulta consulta) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(false);

        if (consulta.getFecha() == null || consulta.getFecha().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La fecha de la consulta es obligatoria.");
            return respuesta;
        }

        if (consulta.getHora() == null || consulta.getHora().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La hora de la consulta es obligatoria.");
            return respuesta;
        }

        if (consulta.getIdPaciente() == null || consulta.getIdPaciente() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El paciente es obligatorio.");
            return respuesta;
        }

        if (consulta.getIdMedico() == null || consulta.getIdMedico() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El médico es obligatorio.");
            return respuesta;
        }

        if (consulta.getPeso() == null || consulta.getPeso() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El peso es obligatorio y debe ser mayor a 0.");
            return respuesta;
        }

        if (consulta.getEstatura() == null || consulta.getEstatura() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("La estatura es obligatoria y debe ser mayor a 0.");
            return respuesta;
        }

        if (consulta.getTalla() == null || consulta.getTalla().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La talla es obligatoria.");
            return respuesta;
        }

        if (consulta.getTalla().trim().length() > 20) {
            respuesta.setError(true);
            respuesta.setMensaje("La talla no puede exceder 20 caracteres.");
            return respuesta;
        }

        return respuesta;
    }
}