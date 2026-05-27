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
            throw new BadRequestException("El id de la dieta es inválido.");
        }

        return DietaAlimentoImp.obtenerPorDieta(idDieta);
    }

    @GET
    @Path("obtener-por-id/{idDietaAlimento}")
    @Produces(MediaType.APPLICATION_JSON)
    public DietaAlimento obtenerPorId(@PathParam("idDietaAlimento") Integer idDietaAlimento) {
        if (idDietaAlimento == null || idDietaAlimento <= 0) {
            throw new BadRequestException("El id de la relación dieta-alimento es inválido.");
        }

        return DietaAlimentoImp.obtenerPorId(idDietaAlimento);
    }

    @POST
    @Path("registrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String jsonDietaAlimento) {
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonDietaAlimento == null || jsonDietaAlimento.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            DietaAlimento dietaAlimento = gson.fromJson(jsonDietaAlimento, DietaAlimento.class);

            if (dietaAlimento == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la relación dieta-alimento es obligatoria.");
                return respuesta;
            }

            if (dietaAlimento.getIdDieta() == null || dietaAlimento.getIdDieta() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La dieta es obligatoria.");
                return respuesta;
            }

            if (dietaAlimento.getIdAlimento() == null || dietaAlimento.getIdAlimento() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El alimento es obligatorio.");
                return respuesta;
            }

            if (dietaAlimento.getIdSegmentoDia() == null || dietaAlimento.getIdSegmentoDia() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El segmento del día es obligatorio.");
                return respuesta;
            }

            if (dietaAlimento.getCantidad() == null || dietaAlimento.getCantidad() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La cantidad debe ser mayor a 0.");
                return respuesta;
            }

            return DietaAlimentoImp.registrar(dietaAlimento);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar alimento en dieta: " + e.getMessage());
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonDietaAlimento) {
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonDietaAlimento == null || jsonDietaAlimento.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            DietaAlimento dietaAlimento = gson.fromJson(jsonDietaAlimento, DietaAlimento.class);

            if (dietaAlimento == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la relación dieta-alimento es obligatoria.");
                return respuesta;
            }

            if (dietaAlimento.getIdDietaAlimento() == null || dietaAlimento.getIdDietaAlimento() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador de la relación dieta-alimento es obligatorio.");
                return respuesta;
            }

            if (dietaAlimento.getIdDieta() == null || dietaAlimento.getIdDieta() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La dieta es obligatoria.");
                return respuesta;
            }

            if (dietaAlimento.getIdAlimento() == null || dietaAlimento.getIdAlimento() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El alimento es obligatorio.");
                return respuesta;
            }

            if (dietaAlimento.getIdSegmentoDia() == null || dietaAlimento.getIdSegmentoDia() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El segmento del día es obligatorio.");
                return respuesta;
            }

            if (dietaAlimento.getCantidad() == null || dietaAlimento.getCantidad() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La cantidad debe ser mayor a 0.");
                return respuesta;
            }

            return DietaAlimentoImp.editar(dietaAlimento);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar alimento en dieta: " + e.getMessage());
        }
    }

    @DELETE
    @Path("quitar-alimento/{idDietaAlimento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta quitarAlimentoDeDieta(@PathParam("idDietaAlimento") Integer idDietaAlimento) {
        if (idDietaAlimento == null || idDietaAlimento <= 0) {
            throw new BadRequestException("El id de la relación dieta-alimento es inválido.");
        }

        return DietaAlimentoImp.quitarAlimentoDeDieta(idDietaAlimento);
    }
}