package ws;

import com.google.gson.Gson;
import dominio.MedicoImp;
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
import pojo.Medico;

@Path("medico")
public class MedicoWS {
    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> obtenerTodos() {
        return MedicoImp.obtenerTodos();
    }

    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Medico medico = gson.fromJson(json, Medico.class);
            return MedicoImp.registrar(medico);
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
            Medico medico = gson.fromJson(json, Medico.class);
            return MedicoImp.editar(medico);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Path("dar-baja/{idMedico}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBaja(@PathParam("idMedico") Integer idMedico) {
        if (idMedico == null || idMedico <= 0) throw new BadRequestException();
        return MedicoImp.darBaja(idMedico);
    }

    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> buscar(@QueryParam("filtro") String filtro) {
        if (filtro == null) filtro = "";
        return MedicoImp.buscar(filtro);
    }
}