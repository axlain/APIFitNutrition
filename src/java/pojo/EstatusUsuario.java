package pojo;

public class EstatusUsuario {
    private Integer idEstatus;
    private String nombre;

    public EstatusUsuario() {
    }

    public EstatusUsuario(Integer idEstatus, String nombre) {
        this.idEstatus = idEstatus;
        this.nombre = nombre;
    }
    
    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
