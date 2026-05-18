package dto;

import pojo.Administrador;
import pojo.Medico;
import pojo.Paciente;

public class RSAutenticar{
    
    private boolean error;
    private String mensaje;
    private String tipoUsuario;

    private Administrador administrador;
    private Medico medico;
    private Paciente paciente;

    public RSAutenticar() {
    }

    public RSAutenticar(boolean error, String mensaje, String tipoUsuario, Administrador administrador, Medico medico, Paciente paciente) {
        this.error = error;
        this.mensaje = mensaje;
        this.tipoUsuario = tipoUsuario;
        this.administrador = administrador;
        this.medico = medico;
        this.paciente = paciente;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}