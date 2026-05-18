package pojo;

public class SegmentoDia {
    private Integer idSegmento;
    private String nombre;

    public SegmentoDia() {
    }

    public SegmentoDia(Integer idSegmento, String nombre) {
        this.idSegmento = idSegmento;
        this.nombre = nombre;
    }

    public Integer getIdSegmento() {
        return idSegmento;
    }

    public void setIdSegmento(Integer idSegmento) {
        this.idSegmento = idSegmento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
