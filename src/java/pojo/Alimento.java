package pojo;

public class Alimento {
    private Integer idAlimento;
    private String nombre;
    private Double porcion;
    private Double caloriasPorPorcion;
    
    private Integer idUnidadPorcion;
    private UnidadPorcion unidadPorcion;

    public Alimento() {
    }

    public Alimento(Integer idAlimento, String nombre, Double porcion, Double caloriasPorPorcion, Integer idUnidadPorcion, UnidadPorcion unidadPorcion) {
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.porcion = porcion;
        this.caloriasPorPorcion = caloriasPorPorcion;
        this.idUnidadPorcion = idUnidadPorcion;
        this.unidadPorcion = unidadPorcion;
    }

    public Integer getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(Integer idAlimento) {
        this.idAlimento = idAlimento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPorcion() {
        return porcion;
    }

    public void setPorcion(Double porcion) {
        this.porcion = porcion;
    }

    public Double getCaloriasPorPorcion() {
        return caloriasPorPorcion;
    }

    public void setCaloriasPorPorcion(Double caloriasPorPorcion) {
        this.caloriasPorPorcion = caloriasPorPorcion;
    }

    public Integer getIdUnidadPorcion() {
        return idUnidadPorcion;
    }

    public void setIdUnidadPorcion(Integer idUnidadPorcion) {
        this.idUnidadPorcion = idUnidadPorcion;
    }

    public UnidadPorcion getUnidadPorcion() {
        return unidadPorcion;
    }

    public void setUnidadPorcion(UnidadPorcion unidadPorcion) {
        this.unidadPorcion = unidadPorcion;
    }
    
    
}
