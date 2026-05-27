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
            throw new BadRequestException("El id de la dieta es inválido.");
        }

        return DietaImp.obtenerPorId(idDieta);
    }
    
    @GET
    @Path("detalle/{idDieta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Dieta obtenerDetalle(@PathParam("idDieta") Integer idDieta) {

        if (idDieta == null || idDieta <= 0) {
            throw new BadRequestException("El id de la dieta es inválido.");
        }

        return DietaImp.obtenerDetalle(idDieta);
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
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonDieta == null || jsonDieta.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Dieta dieta = gson.fromJson(jsonDieta, Dieta.class);

            if (dieta == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la dieta es obligatoria.");
                return respuesta;
            }

            if (dieta.getNombre() == null || dieta.getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre de la dieta es obligatorio.");
                return respuesta;
            }

            if (dieta.getNombre().trim().length() > 100) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre de la dieta no puede exceder 100 caracteres.");
                return respuesta;
            }

            if (dieta.getObservaciones() != null && dieta.getObservaciones().trim().length() > 255) {
                respuesta.setError(true);
                respuesta.setMensaje("Las observaciones no pueden exceder 255 caracteres.");
                return respuesta;
            }

            if (dieta.getIdMedico() == null || dieta.getIdMedico() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El médico responsable es obligatorio.");
                return respuesta;
            }

            return DietaImp.registrar(dieta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para registrar dieta: " + e.getMessage());
        }
    }

    @PUT
    @Path("editar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String jsonDieta) {
        Respuesta respuesta = new Respuesta();

        try {
            if (jsonDieta == null || jsonDieta.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Dieta dieta = gson.fromJson(jsonDieta, Dieta.class);

            if (dieta == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información de la dieta es obligatoria.");
                return respuesta;
            }

            if (dieta.getIdDieta() == null || dieta.getIdDieta() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador de la dieta es obligatorio.");
                return respuesta;
            }

            if (dieta.getNombre() == null || dieta.getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre de la dieta es obligatorio.");
                return respuesta;
            }

            if (dieta.getNombre().trim().length() > 100) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre de la dieta no puede exceder 100 caracteres.");
                return respuesta;
            }

            if (dieta.getObservaciones() != null && dieta.getObservaciones().trim().length() > 255) {
                respuesta.setError(true);
                respuesta.setMensaje("Las observaciones no pueden exceder 255 caracteres.");
                return respuesta;
            }

            if (dieta.getIdMedico() == null || dieta.getIdMedico() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El médico responsable es obligatorio.");
                return respuesta;
            }

            return DietaImp.editar(dieta);

        } catch (Exception e) {
            throw new BadRequestException("JSON inválido para editar dieta: " + e.getMessage());
        }
    }
    
    @DELETE
    @Path("eliminar/{idDieta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta eliminar(@PathParam("idDieta") Integer idDieta) {

        if (idDieta == null || idDieta <= 0) {
            throw new BadRequestException("El id de la dieta es inválido.");
        }

        return DietaImp.eliminar(idDieta);
    }
}