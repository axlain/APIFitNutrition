package ws;

import com.google.gson.Gson;
import dominio.AdministradorImp;
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
import pojo.Administrador;

@Path("administrador")
public class AdministradorWS {
    // =========================================
    // OBTENER TODOS LOS ADMINISTRADORES
    // =========================================
    @Path("obtener-todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Administrador> obtenerTodos() {
        return AdministradorImp.obtenerTodos();
    }

    // =========================================
    // REGISTRAR ADMINISTRADOR
    // =========================================
    @Path("registrar")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta registrar(String json) {
        Gson gson = new Gson();
        try {
            Administrador admin = gson.fromJson(json, Administrador.class);
            return AdministradorImp.registrar(admin);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // =========================================
    // EDITAR ADMINISTRADOR (sin cambiar contraseña)
    // =========================================
    @Path("editar")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta editar(String json) {
        Gson gson = new Gson();
        try {
            Administrador admin = gson.fromJson(json, Administrador.class);
            return AdministradorImp.editar(admin);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    // =========================================
    // CAMBIAR CONTRASEÑA DE ADMINISTRADOR
    // =========================================
    @Path("cambiar-contrasena")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta cambiarContrasena(String json) {
        Gson gson = new Gson();
        try {
            class Cambio {
                public Integer idAdministrador;
                public String contrasenaActual;
                public String nuevaContrasena;
            }
            Cambio cambio = gson.fromJson(json, Cambio.class);
            return AdministradorImp.cambiarContrasena(
                    cambio.idAdministrador,
                    cambio.contrasenaActual,
                    cambio.nuevaContrasena
            );
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}