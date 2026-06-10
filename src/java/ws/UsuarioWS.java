package ws;

import com.google.gson.Gson;
import dominio.UsuarioImp;
import dto.Respuesta;
import java.util.Base64;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.Usuario; // Faltaba importar la clase Usuario

@Path("usuario")
public class UsuarioWS {

    public static class FotoRequest {
        public String fotoBase64;
    }

    @Path("guardar-foto/{idUsuario}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta subirFoto(@PathParam("idUsuario") Integer idUsuario, String json) {
        if (idUsuario == null || idUsuario <= 0) {
            throw new BadRequestException("ID de usuario inválido.");
        }

        if (json == null || json.trim().isEmpty()) {
            throw new BadRequestException("La fotografía en Base64 es obligatoria.");
        }

        Gson gson = new Gson();
        FotoRequest datos = gson.fromJson(json, FotoRequest.class);

        if (datos == null || datos.fotoBase64 == null || datos.fotoBase64.trim().isEmpty()) {
            throw new BadRequestException("La fotografía en Base64 es obligatoria.");
        }

        byte[] foto;
        try {
            foto = Base64.getDecoder().decode(datos.fotoBase64.trim());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("La fotografía no tiene un formato Base64 válido.");
        }

        if (foto.length == 0) {
            throw new BadRequestException("ID de usuario inválido o archivo de foto vacío.");
        }

        return UsuarioImp.subirFoto(idUsuario, foto);
    }

    @Path("obtener-foto/{idUsuario}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario obtenerFoto(@PathParam("idUsuario") Integer idUsuario) {
        if (idUsuario != null && idUsuario > 0) {
            // Corregido: Llamaba a ColaboradorImp en lugar de UsuarioImp
            return UsuarioImp.obtenerFoto(idUsuario); 
        }
        
        throw new BadRequestException("ID de usuario inválido.");
    }
}