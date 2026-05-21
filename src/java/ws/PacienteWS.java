package ws;

import com.google.gson.Gson;
import dominio.PacienteImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pojo.Paciente;

@Path("paciente")
public class PacienteWS {

    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> obtenerTodos() {
        return PacienteImp.obtenerTodos();
    }

    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Paciente paciente = gson.fromJson(json, Paciente.class);
            return PacienteImp.registrar(paciente);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Path("editar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();
        try {
            Paciente paciente = gson.fromJson(json, Paciente.class);
            return PacienteImp.editar(paciente);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Path("dar-baja/{idPaciente}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBaja(@PathParam("idPaciente") Integer idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException("ID de paciente inválido");
        }
        return PacienteImp.darBaja(idPaciente);
    }

    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paciente> buscar(@QueryParam("filtro") String filtro) {
        if (filtro == null) filtro = "";
        return PacienteImp.buscar(filtro);
    }
    
    @Path("actualizar-codigo-acceso/{idPaciente}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta actualizarCodigoAcceso(@PathParam("idPaciente") Integer idPaciente) {
        if (idPaciente == null || idPaciente <= 0) {
            throw new BadRequestException("ID de paciente inválido");
        }
        return PacienteImp.actualizarCodigoAcceso(idPaciente);
    }
}