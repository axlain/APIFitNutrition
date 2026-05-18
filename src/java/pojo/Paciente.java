package pojo;

public class Paciente {
    private Integer idPaciente;
    private String codigoAcceso;
    
    private Integer idUsuario; 
    private Usuario usuario;
    private Integer idMedico; 
    private Medico medico; 

    public Paciente() {
    }

    public Paciente(Integer idPaciente, String codigoAcceso, Integer idUsuario, Usuario usuario, Integer idMedico, Medico medico) {
        this.idPaciente = idPaciente;
        this.codigoAcceso = codigoAcceso;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.idMedico = idMedico;
        this.medico = medico;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getCodigoAcceso() {
        return codigoAcceso;
    }

    public void setCodigoAcceso(String codigoAcceso) {
        this.codigoAcceso = codigoAcceso;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
