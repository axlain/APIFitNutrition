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
    
    public static class Cambio {
        public Integer idMedico;
        public String contrasenaActual;
        public String nuevaContrasena;
    }

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
        Respuesta respuesta = new Respuesta();

        try {
            Medico medico = gson.fromJson(json, Medico.class);

            if (medico == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del médico es obligatoria.");
                return respuesta;
            }

            if (medico.getUsuario() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del usuario es obligatoria.");
                return respuesta;
            }

            if (medico.getUsuario().getNombre() == null || medico.getUsuario().getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre es obligatorio.");
                return respuesta;
            }

            if (medico.getUsuario().getApellidoPaterno() == null || medico.getUsuario().getApellidoPaterno().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El apellido paterno es obligatorio.");
                return respuesta;
            }

            if (medico.getUsuario().getCorreo() == null || medico.getUsuario().getCorreo().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El correo es obligatorio.");
                return respuesta;
            }

            if (medico.getCedulaProfesional() == null || medico.getCedulaProfesional().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La cédula profesional es obligatoria.");
                return respuesta;
            }

            if (medico.getCedulaProfesional().trim().length() > 30) {
                respuesta.setError(true);
                respuesta.setMensaje("La cédula profesional no puede exceder 30 caracteres.");
                return respuesta;
            }

            if (medico.getContrasena() == null || medico.getContrasena().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña es obligatoria.");
                return respuesta;
            }

            if (medico.getContrasena().trim().length() > 20) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña no puede exceder 20 caracteres.");
                return respuesta;
            }

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
        Respuesta respuesta = new Respuesta();

        try {
            Medico medico = gson.fromJson(json, Medico.class);

            if (medico == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del médico es obligatoria.");
                return respuesta;
            }

            if (medico.getIdMedico() == null || medico.getIdMedico() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador del médico es obligatorio.");
                return respuesta;
            }

            if (medico.getUsuario() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del usuario es obligatoria.");
                return respuesta;
            }

            if (medico.getUsuario().getNombre() == null || medico.getUsuario().getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre es obligatorio.");
                return respuesta;
            }

            if (medico.getUsuario().getApellidoPaterno() == null || medico.getUsuario().getApellidoPaterno().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El apellido paterno es obligatorio.");
                return respuesta;
            }

            if (medico.getUsuario().getCorreo() == null || medico.getUsuario().getCorreo().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El correo es obligatorio.");
                return respuesta;
            }

            if (medico.getCedulaProfesional() == null || medico.getCedulaProfesional().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La cédula profesional es obligatoria.");
                return respuesta;
            }

            if (medico.getCedulaProfesional().trim().length() > 30) {
                respuesta.setError(true);
                respuesta.setMensaje("La cédula profesional no puede exceder 30 caracteres.");
                return respuesta;
            }

            return MedicoImp.editar(medico);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Path("dar-baja/{idMedico}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta darBaja(@PathParam("idMedico") Integer idMedico) {
        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException("El id del médico es inválido.");
        }

        return MedicoImp.darBaja(idMedico);
    }

    @Path("buscar")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Medico> buscar(@QueryParam("filtro") String filtro) {
        if (filtro == null) {
            filtro = "";
        }

        return MedicoImp.buscar(filtro);
    }

    @Path("cambiar-contrasena")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cambiarContrasena(String json) {
        Gson gson = new Gson();
        Respuesta respuesta = new Respuesta();

        try {
            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Cambio cambio = gson.fromJson(json, Cambio.class);

            if (cambio == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información para cambiar la contraseña es obligatoria.");
                return respuesta;
            }

            if (cambio.idMedico == null || cambio.idMedico <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador del médico es obligatorio.");
                return respuesta;
            }

            if (cambio.contrasenaActual == null || cambio.contrasenaActual.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña actual es obligatoria.");
                return respuesta;
            }

            if (cambio.nuevaContrasena == null || cambio.nuevaContrasena.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La nueva contraseña es obligatoria.");
                return respuesta;
            }

            if (cambio.nuevaContrasena.trim().length() > 20) {
                respuesta.setError(true);
                respuesta.setMensaje("La nueva contraseña no puede exceder 20 caracteres.");
                return respuesta;
            }

            return MedicoImp.cambiarContrasena(
                    cambio.idMedico,
                    cambio.contrasenaActual.trim(),
                    cambio.nuevaContrasena.trim()
            );

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    
    @Path("obtener-por-id/{idMedico}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Medico obtenerPorId(@PathParam("idMedico") Integer idMedico) {
        if (idMedico == null || idMedico <= 0) {
            throw new BadRequestException("El id del médico es inválido.");
        }

        return MedicoImp.obtenerPorId(idMedico);
    }
}