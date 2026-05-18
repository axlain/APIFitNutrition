package pojo;

public class EstadoCita {
    private Integer idEstadoCita;
    private String nombre;

    public EstadoCita() {
    }

    public EstadoCita(Integer idEstadoCita, String nombre) {
        this.idEstadoCita = idEstadoCita;
        this.nombre = nombre;
    }

    public Integer getIdEstadoCita() {
        return idEstadoCita;
    }

    public void setIdEstadoCita(Integer idEstadoCita) {
        this.idEstadoCita = idEstadoCita;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
