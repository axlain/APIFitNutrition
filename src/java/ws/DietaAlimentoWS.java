package ws;

import com.google.gson.Gson;
import dominio.DietaAlimentoImp;
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
import javax.ws.rs.core.MediaType;
import pojo.DietaAlimento;

@Path("dieta-alimento")
public class DietaAlimentoWS {

    private final Gson gson = new Gson();

    @GET
    @Path("obtener-por-dieta/{idDieta}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DietaAlimento> obtenerPorDieta(@PathParam("idDieta") Integer idDieta) {
        if (idDieta == null || idDieta <= 0) {
            throw new BadRequestException("El id de la dieta es obligatorio.");
        }

        return DietaAlimentoImp.obtenerPorDieta(idDieta);
    }

    @GET
    @Path("obtener-por-id/{idDietaAlimento}")
    @Produces(MediaType.APPLICATION_JSON)
    public DietaAlimento obtenerPorId(@PathParam("idDietaAlimento") Integer idDietaAlimento) {
        if (idDietaAlimento == null || idDietaAlimento <= 0) {
            throw new BadRequestException("El id de la relación dieta-alimento es obligatorio.");
        }

        return DietaAlimentoImp.obtenerPorId(idDietaAlimento);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String jsonDietaAlimento) {
        try {
            if (jsonDietaAlimento == null || jsonDietaAlimento.trim().isEmpty()) {
                throw new BadRequestException("El JSON es obligatorio.");
            }

            DietaAlimento dietaAlimento = gson.fromJson(jsonDietaAlimento, DietaAlimento.class);

            if (dietaAlimento == null) {
                throw new BadRequestException("No se pudo interpretar la información.");
            }

            return DietaAlimentoImp.registrar(dietaAlimento);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar alimento en dieta.");
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonDietaAlimento) {
        try {
            if (jsonDietaAlimento == null || jsonDietaAlimento.trim().isEmpty()) {
                throw new BadRequestException("El JSON es obligatorio.");
            }

            DietaAlimento dietaAlimento = gson.fromJson(jsonDietaAlimento, DietaAlimento.class);

            if (dietaAlimento == null
                    || dietaAlimento.getIdDietaAlimento() == null
                    || dietaAlimento.getIdDietaAlimento() <= 0) {
                throw new BadRequestException("Debe enviar una relación dieta-alimento válida.");
            }

            return DietaAlimentoImp.editar(dietaAlimento);

        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar alimento en dieta.");
        }
    }

    @DELETE
    @Path("quitar-alimento/{idDietaAlimento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta quitarAlimentoDeDieta(@PathParam("idDietaAlimento") Integer idDietaAlimento) {
        if (idDietaAlimento == null || idDietaAlimento <= 0) {
            throw new BadRequestException("El id de la relación dieta-alimento es obligatorio.");
        }

        return DietaAlimentoImp.quitarAlimentoDeDieta(idDietaAlimento);
    }
}