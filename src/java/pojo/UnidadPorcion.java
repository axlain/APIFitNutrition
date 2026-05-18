package pojo;

public class UnidadPorcion {
    private Integer idUnidadPorcion;
    private String nombre;

    public UnidadPorcion() {
    }

    public UnidadPorcion(Integer idUnidadPorcion, String nombre) {
        this.idUnidadPorcion = idUnidadPorcion;
        this.nombre = nombre;
    }

    public Integer getIdUnidadPorcion() {
        return idUnidadPorcion;
    }

    public void setIdUnidadPorcion(Integer idUnidadPorcion) {
        this.idUnidadPorcion = idUnidadPorcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
