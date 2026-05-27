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

    // ==========================================
    //           REGISTRAR CONSULTA
    // ==========================================
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

            return ConsultaImp.registrarConsulta(consulta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar consulta: " + e.getMessage());
        }
    }

    // ==========================================
    //           EDITAR CONSULTA
    // ==========================================
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

            if (consulta == null || consulta.getIdConsulta() == null || consulta.getIdConsulta() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El ID de la consulta es obligatorio para editar.");
                return respuesta;
            }

            if (consulta.getTalla() != null && consulta.getTalla().trim().length() > 20) {
                respuesta.setError(true);
                respuesta.setMensaje("La talla no puede exceder 20 caracteres.");
                return respuesta;
            }

            return ConsultaImp.editarConsulta(consulta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar consulta: " + e.getMessage());
        }
    }

    // ==========================================
    //           CANCELAR CITA ASOCIADA
    // ==========================================
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

            // Parseamos manualmente el JSON para extraer los parámetros de la cita
            JsonObject json = JsonParser.parseString(jsonInput).getAsJsonObject();
            
            Integer idCita = (json.has("idCita") && !json.get("idCita").isJsonNull()) 
                             ? json.get("idCita").getAsInt() : null;
                             
            String motivoCancelacion = (json.has("motivoCancelacion") && !json.get("motivoCancelacion").isJsonNull()) 
                                       ? json.get("motivoCancelacion").getAsString() : null;

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

    // ==========================================
    //           BUSCAR CONSULTAS
    // ==========================================
    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> buscarConsultas(
            @QueryParam("idConsulta") Integer idConsulta,
            @QueryParam("idPaciente") Integer idPaciente,
            @QueryParam("idMedico") Integer idMedico,
            @QueryParam("fecha") String fecha
    ) {
        // Los parámetros son opcionales, MyBatis se encarga de ignorar los nulos en el XML
        return ConsultaImp.buscarConsultas(idConsulta, idPaciente, idMedico, fecha);
    }

    // ==========================================
    //        HISTORIAL POR PACIENTE
    // ==========================================
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

    // ==========================================
    //        OBTENER DETALLE CONSULTA
    // ==========================================
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
}