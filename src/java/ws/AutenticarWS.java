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

        return AutenticarImp.loginAdministrador(numeroPersonal, contrasena);
    }

    @POST
    @Path("medico")
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticar loginMedico(
            @FormParam("numero_personal") String numeroPersonal,
            @FormParam("contrasena") String contrasena) {

        return AutenticarImp.loginMedico(numeroPersonal, contrasena);
    }

    @POST
    @Path("paciente")
    @Produces(MediaType.APPLICATION_JSON)
    public RSAutenticar loginPaciente(
            @FormParam("correo") String correo,
            @FormParam("codigo_acceso") String codigoAcceso) {

        return AutenticarImp.loginPaciente(correo, codigoAcceso);
    }
}