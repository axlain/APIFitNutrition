package pojo;

public class Cita {
    private Integer idCita;
    private String fecha;
    private String hora;
    private String observaciones;
    private String motivoCancelacion;

    // Relaciones
    private Integer idEstadoCita;
    private EstadoCita estadoCita;
    
    private Integer idPaciente;
    private Paciente paciente;
    
    private Integer idMedico;
    private Medico medico;

    public Cita() {
    }

    public Cita(Integer idCita, String fecha, String hora, String observaciones, String motivoCancelacion, Integer idEstadoCita, EstadoCita estadoCita, Integer idPaciente, Paciente paciente, Integer idMedico, Medico medico) {
        this.idCita = idCita;
        this.fecha = fecha;
        this.hora = hora;
        this.observaciones = observaciones;
        this.motivoCancelacion = motivoCancelacion;
        this.idEstadoCita = idEstadoCita;
        this.estadoCita = estadoCita;
        this.idPaciente = idPaciente;
        this.paciente = paciente;
        this.idMedico = idMedico;
        this.medico = medico;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public Integer getIdEstadoCita() {
        return idEstadoCita;
    }

    public void setIdEstadoCita(Integer idEstadoCita) {
        this.idEstadoCita = idEstadoCita;
    }

    public EstadoCita getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(EstadoCita estadoCita) {
        this.estadoCita = estadoCita;
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

    
    
    
}
