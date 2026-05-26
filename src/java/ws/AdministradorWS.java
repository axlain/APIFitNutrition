package ws;

import com.google.gson.Gson;
import dominio.AdministradorImp;
import dto.Respuesta;
import java.util.List;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Administrador;

@Path("administrador")
public class AdministradorWS {

    public static class Cambio {
        public Integer idAdministrador;
        public String contrasenaActual;
        public String nuevaContrasena;
    }

    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Administrador> obtenerTodos() {
        return AdministradorImp.obtenerTodos();
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

            Administrador admin = gson.fromJson(json, Administrador.class);

            if (admin == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del administrador es obligatoria.");
                return respuesta;
            }

            if (admin.getUsuario() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del usuario es obligatoria.");
                return respuesta;
            }

            if (admin.getUsuario().getNombre() == null || admin.getUsuario().getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre es obligatorio.");
                return respuesta;
            }

            if (admin.getUsuario().getApellidoPaterno() == null || admin.getUsuario().getApellidoPaterno().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El apellido paterno es obligatorio.");
                return respuesta;
            }

            if (admin.getUsuario().getCorreo() == null || admin.getUsuario().getCorreo().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El correo es obligatorio.");
                return respuesta;
            }

            if (admin.getContrasena() == null || admin.getContrasena().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña es obligatoria.");
                return respuesta;
            }

            if (admin.getContrasena().trim().length() > 20) {
                respuesta.setError(true);
                respuesta.setMensaje("La contraseña no puede exceder 20 caracteres.");
                return respuesta;
            }

            return AdministradorImp.registrar(admin);

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
            if (json == null || json.trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El cuerpo de la petición es obligatorio.");
                return respuesta;
            }

            Administrador admin = gson.fromJson(json, Administrador.class);

            if (admin == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del administrador es obligatoria.");
                return respuesta;
            }

            if (admin.getIdAdministrador() == null || admin.getIdAdministrador() <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador del administrador es obligatorio.");
                return respuesta;
            }

            if (admin.getUsuario() == null) {
                respuesta.setError(true);
                respuesta.setMensaje("La información del usuario es obligatoria.");
                return respuesta;
            }

            if (admin.getUsuario().getNombre() == null || admin.getUsuario().getNombre().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El nombre es obligatorio.");
                return respuesta;
            }

            if (admin.getUsuario().getApellidoPaterno() == null || admin.getUsuario().getApellidoPaterno().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El apellido paterno es obligatorio.");
                return respuesta;
            }

            if (admin.getUsuario().getCorreo() == null || admin.getUsuario().getCorreo().trim().isEmpty()) {
                respuesta.setError(true);
                respuesta.setMensaje("El correo es obligatorio.");
                return respuesta;
            }

            return AdministradorImp.editar(admin);

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
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

            if (cambio.idAdministrador == null || cambio.idAdministrador <= 0) {
                respuesta.setError(true);
                respuesta.setMensaje("El identificador del administrador es obligatorio.");
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

            return AdministradorImp.cambiarContrasena(
                    cambio.idAdministrador,
                    cambio.contrasenaActual.trim(),
                    cambio.nuevaContrasena.trim()
            );

        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}