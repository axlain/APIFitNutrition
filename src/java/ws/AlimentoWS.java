package ws;

import com.google.gson.Gson;
import dominio.AlimentoImp;
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
import pojo.Alimento;

@Path("alimento")
public class AlimentoWS {

    @Path("obtener-activos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alimento> obtenerActivos() {
        return AlimentoImp.obtenerActivos();
    }

    @Path("unidades")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<pojo.UnidadPorcion> obtenerUnidades() {
        return AlimentoImp.obtenerUnidades();
    }

    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alimento> buscar(@QueryParam("filtro") String filtro) {
        if (filtro == null) {
            filtro = "";
        }

        return AlimentoImp.buscar(filtro);
    }

    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();

        try {
            Alimento alimento = gson.fromJson(json, Alimento.class);
            return AlimentoImp.registrar(alimento);
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar alimento: " + e.getMessage());
        }
    }

    @Path("editar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();

        try {
            Alimento alimento = gson.fromJson(json, Alimento.class);
            return AlimentoImp.editar(alimento);
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar alimento: " + e.getMessage());
        }
    }

    @Path("dar-baja/{idAlimento}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBaja(@PathParam("idAlimento") Integer idAlimento) {
        if (idAlimento == null || idAlimento <= 0) {
            throw new BadRequestException("ID de alimento inválido.");
        }

        return AlimentoImp.darBaja(idAlimento);
    }
}