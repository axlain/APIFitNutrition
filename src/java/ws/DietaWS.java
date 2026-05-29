package ws;

import com.google.gson.Gson;
import dominio.DietaImp;
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
import pojo.Dieta;

@Path("dieta")
public class DietaWS {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-todas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dieta> obtenerTodas() {
        return DietaImp.obtenerTodas();
    }

    @GET
    @Path("obtener-por-id/{idDieta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dieta obtenerPorId(@PathParam("idDieta") Integer idDieta) {
        if (idDieta == null || idDieta <= 0) {
            throw new BadRequestException("El id de la dieta es obligatorio.");
        }

        return DietaImp.obtenerPorId(idDieta);
    }

    @GET
    @Path("buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Dieta> buscar(@QueryParam("filtro") String filtro) {
        if (filtro == null) {
            filtro = "";
        }

        return DietaImp.buscar(filtro);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String jsonDieta) {
        try {
            if (jsonDieta == null || jsonDieta.trim().isEmpty()) {
                throw new BadRequestException("El JSON de la dieta es obligatorio.");
            }

            Dieta dieta = gson.fromJson(jsonDieta, Dieta.class);

            if (dieta == null) {
                throw new BadRequestException("No se pudo interpretar la información de la dieta.");
            }

            return DietaImp.registrar(dieta);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar dieta.");
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonDieta) {
        try {
            if (jsonDieta == null || jsonDieta.trim().isEmpty()) {
                throw new BadRequestException("El JSON de la dieta es obligatorio.");
            }

            Dieta dieta = gson.fromJson(jsonDieta, Dieta.class);

            if (dieta == null || dieta.getIdDieta() == null || dieta.getIdDieta() <= 0) {
                throw new BadRequestException("Debe enviar una dieta válida con idDieta.");
            }

            return DietaImp.editar(dieta);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar dieta.");
        }
    }

    @DELETE
    @Path("eliminar/{idDieta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idDieta") Integer idDieta) {
        if (idDieta == null || idDieta <= 0) {
            throw new BadRequestException("El id de la dieta es obligatorio para eliminarla.");
        }

        return DietaImp.eliminar(idDieta);
    }
}