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
import pojo.UnidadPorcion;

@Path("alimento")
public class AlimentoWS {

    @Path("obtener-activos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Alimento> obtenerActivos() {
        return AlimentoImp.obtenerActivos();
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
        Respuesta respuesta = new Respuesta();

        try {
            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Alimento alimento = gson.fromJson(json, Alimento.class);

            if (alimento == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del alimento es obligatoria.");
                return respuesta;
            }

            if (alimento.getNombre() == null || alimento.getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre del alimento es obligatorio.");
                return respuesta;
            }

            if (alimento.getNombre().trim().length() > 100) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre del alimento no puede exceder 100 caracteres.");
                return respuesta;
            }

            if (alimento.getPorcion() == null || alimento.getPorcion().doubleValue() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La porción debe ser mayor a 0.");
                return respuesta;
            }

            if (alimento.getCaloriasPorPorcion() == null || alimento.getCaloriasPorPorcion().doubleValue() < 0) {
                respuesta.setError(true);
                respuesta.setMensaje("Las calorías por porción no pueden ser negativas.");
                return respuesta;
            }

            if (alimento.getIdUnidadPorcion() == null || alimento.getIdUnidadPorcion() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La unidad de porción es obligatoria.");
                return respuesta;
            }

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
        Respuesta respuesta = new Respuesta();

        try {
            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Alimento alimento = gson.fromJson(json, Alimento.class);

            if (alimento == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del alimento es obligatoria.");
                return respuesta;
            }

            if (alimento.getIdAlimento() == null || alimento.getIdAlimento() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador del alimento es obligatorio.");
                return respuesta;
            }

            if (alimento.getNombre() == null || alimento.getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre del alimento es obligatorio.");
                return respuesta;
            }

            if (alimento.getNombre().trim().length() > 100) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre del alimento no puede exceder 100 caracteres.");
                return respuesta;
            }

            if (alimento.getPorcion() == null || alimento.getPorcion().doubleValue() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La porción debe ser mayor a 0.");
                return respuesta;
            }

            if (alimento.getCaloriasPorPorcion() == null || alimento.getCaloriasPorPorcion().doubleValue() < 0) {
                respuesta.setError(true);
                respuesta.setMensaje("Las calorías por porción no pueden ser negativas.");
                return respuesta;
            }

            if (alimento.getIdUnidadPorcion() == null || alimento.getIdUnidadPorcion() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("La unidad de porción es obligatoria.");
                return respuesta;
            }

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
    
    @GET
    @Path("unidades")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UnidadPorcion> obtenerUnidades() {
        return AlimentoImp.obtenerUnidades();
    }
}