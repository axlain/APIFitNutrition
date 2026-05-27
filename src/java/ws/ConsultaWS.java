package ws;

import com.google.gson.Gson;
import dominio.ConsultaImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
                respuesta.setMensaje("El peso debe ser mayor a 0.");
                return respuesta;
            }

            if (consulta.getEstatura() == null || consulta.getEstatura() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La estatura debe ser mayor a 0.");
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

            throw new BadRequestException(
                    "JSON inválido para registrar consulta: "
                    + e.getMessage()
            );
        }
    }

    @GET
    @Path("historial/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerHistorialPaciente(
            @PathParam("idPaciente") Integer idPaciente
    ) {

        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException(
                    "El id del paciente es inválido."
            );
        }

        return ConsultaImp.obtenerHistorialPaciente(idPaciente);
    }

    @GET
    @Path("obtener/{idConsulta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Consulta obtenerDetalleConsulta(
            @PathParam("idConsulta") Integer idConsulta
    ) {

        if (idConsulta == null || idConsulta <= 0) {
            throw new BadRequestException(
                    "El id de la consulta es inválido."
            );
        }

        return ConsultaImp.obtenerDetalleConsulta(idConsulta);
    }
}