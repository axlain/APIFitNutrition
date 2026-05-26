package ws;

import dominio.AutenticarImp;
import dto.RSAutenticar;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("autenticar")
public class AutenticarWS {

    @POST
    @Path("administrador")
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticar loginAdministrador(
            @FormParam("numero_personal") String numeroPersonal,
            @FormParam("contrasena") String contrasena) {

        RSAutenticar respuesta = new RSAutenticar();

        // Validar campos vacíos
        if (numeroPersonal == null || numeroPersonal.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El número personal es obligatorio.");
            return respuesta;
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La contraseña es obligatoria.");
            return respuesta;
        }

        numeroPersonal = numeroPersonal.trim();

        // Validar longitud número personal
        if (numeroPersonal.length() > 9) {
            respuesta.setError(true);
            respuesta.setMensaje("El número personal no puede exceder 9 caracteres.");
            return respuesta;
        }

        return AutenticarImp.loginAdministrador(numeroPersonal, contrasena);
    }

    @POST
    @Path("medico")
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticar loginMedico(
            @FormParam("numero_personal") String numeroPersonal,
            @FormParam("contrasena") String contrasena) {

        RSAutenticar respuesta = new RSAutenticar();

        // Validar campos vacíos
        if (numeroPersonal == null || numeroPersonal.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El número personal es obligatorio.");
            return respuesta;
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La contraseña es obligatoria.");
            return respuesta;
        }

        numeroPersonal = numeroPersonal.trim();

        // Validar longitud número personal
        if (numeroPersonal.length() > 9) {
            respuesta.setError(true);
            respuesta.setMensaje("El número personal no puede exceder 9 caracteres.");
            return respuesta;
        }

        return AutenticarImp.loginMedico(numeroPersonal, contrasena);
    }

    @POST
    @Path("paciente")
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticar loginPaciente(
            @FormParam("correo") String correo,
            @FormParam("codigo_acceso") String codigoAcceso) {

        RSAutenticar respuesta = new RSAutenticar();

        // Validar correo
        if (correo == null || correo.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El correo es obligatorio.");
            return respuesta;
        }

        // Validar código acceso
        if (codigoAcceso == null || codigoAcceso.trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El código de acceso es obligatorio.");
            return respuesta;
        }

        codigoAcceso = codigoAcceso.trim();

        // Validar longitud
        if (codigoAcceso.length() > 4) {
            respuesta.setError(true);
            respuesta.setMensaje("El código de acceso no puede exceder 4 caracteres.");
            return respuesta;
        }

        // Validar que sean solo números
        if (!codigoAcceso.matches("\\d{4}")) {
            respuesta.setError(true);
            respuesta.setMensaje("El código de acceso debe contener exactamente 4 números.");
            return respuesta;
        }

        return AutenticarImp.loginPaciente(correo, codigoAcceso);
    }
}