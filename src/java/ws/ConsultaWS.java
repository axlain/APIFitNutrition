package ws;

import com.google.gson.Gson;
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
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pojo.Consulta;

@Path("consulta")
public class ConsultaWS {

    @GET
    @Path("obtener-todas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerTodas() {
        return ConsultaImp.obtenerTodas();
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonInput) {
        if (jsonInput == null || jsonInput.isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": true, \"mensaje\": \"El cuerpo de la solicitud no puede estar vacío.\"}")
                            .build()
            );
        }
        try {
            Gson gson = new Gson();
            Consulta consulta = gson.fromJson(jsonInput, Consulta.class);
            return ConsultaImp.editar(consulta);
        } catch (Exception e) {
            Respuesta resFail = new Respuesta();
            resFail.setError(true);
            resFail.setMensaje("Error al procesar la solicitud JSON: " + e.getMessage());
            return resFail;
        }
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String jsonInput) {
        if (jsonInput == null || jsonInput.isEmpty()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": true, \"mensaje\": \"El cuerpo de la solicitud no puede estar vacío.\"}")
                            .build()
            );
        }

        try {
            Gson gson = new Gson();
            Consulta consulta = gson.fromJson(jsonInput, Consulta.class);

            if (consulta.getIdPaciente() == null || consulta.getIdMedico() == null
                    || consulta.getPeso() == null || consulta.getEstatura() == null || consulta.getTalla() == null) {

                Respuesta resError = new Respuesta();
                resError.setError(true);
                resError.setMensaje("Faltan parámetros obligatorios (idPaciente, idMedico, peso, estatura o talla).");
                return resError;
            }

            return ConsultaImp.registrarConsulta(consulta);

        } catch (Exception e) {
            Respuesta resFail = new Respuesta();
            resFail.setError(true);
            resFail.setMensaje("Error al procesar la solicitud JSON: " + e.getMessage());
            return resFail;
        }
    }

    @GET
    @Path("historial/{idPaciente}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Consulta> obtenerHistorialPaciente(@PathParam("idPaciente") Integer idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": true, \"mensaje\": \"El idPaciente proporcionado no es válido.\"}")
                            .build()
            );
        }

        return ConsultaImp.obtenerHistorialPaciente(idPaciente);
    }

    @GET
    @Path("obtener/{idConsulta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDetalleConsulta(@PathParam("idConsulta") Integer idConsulta) {
        if (idConsulta == null || idConsulta <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": true, \"mensaje\": \"El idConsulta proporcionado no es válido.\"}")
                    .build();
        }

        Consulta consulta = ConsultaImp.obtenerDetalleConsulta(idConsulta);

        if (consulta != null) {
            return Response.status(Response.Status.OK).entity(consulta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": true, \"mensaje\": \"La consulta especificada no existe.\"}")
                    .build();
        }
    }
}
