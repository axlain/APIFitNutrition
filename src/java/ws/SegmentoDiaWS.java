package ws;

import dominio.SegmentoDiaImp;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pojo.SegmentoDia;

@Path("segmento-dia")
public class SegmentoDiaWS {

    @GET
    @Path("obtener-todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SegmentoDia> obtenerTodos() {
        return SegmentoDiaImp.obtenerTodos();
    }
}
