package pojo;

import java.util.List;

public class Dieta {

    private Integer idDieta;
    private String nombre;
    private Double totalCalorias;
    private String observaciones;
    private Boolean asignada;

    private Integer idMedico;
    private Medico medico;

    private List<DietaAlimento> alimentos;

    public Dieta() {
    }

    public Dieta(Integer idDieta, String nombre, Double totalCalorias,
            String observaciones, Boolean asignada,
            Integer idMedico, Medico medico,
            List<DietaAlimento> alimentos) {

        this.idDieta = idDieta;
        this.nombre = nombre;
        this.totalCalorias = totalCalorias;
        this.observaciones = observaciones;
        this.asignada = asignada;
        this.idMedico = idMedico;
        this.medico = medico;
        this.alimentos = alimentos;
    }

    public Integer getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(Integer idDieta) {
        this.idDieta = idDieta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getTotalCalorias() {
        return totalCalorias;
    }

    public void setTotalCalorias(Double totalCalorias) {
        this.totalCalorias = totalCalorias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getAsignada() {
        return asignada;
    }

    public void setAsignada(Boolean asignada) {
        this.asignada = asignada;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public List<DietaAlimento> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<DietaAlimento> alimentos) {
        this.alimentos = alimentos;
    }
}