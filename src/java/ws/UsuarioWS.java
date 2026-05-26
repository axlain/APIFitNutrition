package ws;

import dominio.UsuarioImp;
import dto.Respuesta;
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
    
    @Path("guardar-foto/{idUsuario}")
    @PUT
    // Dependiendo de cómo envíes la foto desde el cliente, puede ser útil agregar un @Consumes.
    // Si la envías como binario puro, puedes usar @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta subirFoto(@PathParam("idUsuario") Integer idUsuario, byte[] foto) {
        
        // Se agregó la validación 'foto != null' para evitar un NullPointerException en foto.length
        if (idUsuario != null && idUsuario > 0 && foto != null && foto.length > 0) {
            return UsuarioImp.subirFoto(idUsuario, foto);
        }
        
        throw new BadRequestException("ID de usuario inválido o archivo de foto vacío.");
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