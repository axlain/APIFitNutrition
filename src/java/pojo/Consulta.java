package pojo;

public class Consulta {
    private Integer idConsulta;
    private String fechaHora;
    private Double peso;
    private Double estatura;
    private String talla;
    private Double imc;
    private String observaciones;

    // Relaciones
    private Integer idPaciente;
    private Paciente paciente;
    private Integer idMedico;
    private Medico medico;
    private Integer idDieta;
    private Dieta dieta; 
    private Integer idCita; 
           
    public Consulta() {
    }

    public Consulta(Integer idConsulta, String fechaHora, Double peso, Double estatura, String talla, Double imc, String observaciones, Integer idPaciente, Paciente paciente, Integer idMedico, Medico medico, Integer idDieta, Dieta dieta, Integer idCita) {
        this.idConsulta = idConsulta;
        this.fechaHora = fechaHora;
        this.peso = peso;
        this.estatura = estatura;
        this.talla = talla;
        this.imc = imc;
        this.observaciones = observaciones;
        this.idPaciente = idPaciente;
        this.paciente = paciente;
        this.idMedico = idMedico;
        this.medico = medico;
        this.idDieta = idDieta;
        this.dieta = dieta;
        this.idCita = idCita;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getEstatura() {
        return estatura;
    }

    public void setEstatura(Double estatura) {
        this.estatura = estatura;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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

    public Integer getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(Integer idDieta) {
        this.idDieta = idDieta;
    }

    public Dieta getDieta() {
        return dieta;
    }

    public void setDieta(Dieta dieta) {
        this.dieta = dieta;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    
}