package pojo;

public class Medico {
    private Integer idMedico;
    private String numeroPersonal;
    private String cedulaProfesional;
    private String contrasena;
    
    private Integer idUsuario; 
    private Usuario usuario; 

    public Medico() {
    }

    public Medico(Integer idMedico, String numeroPersonal, String cedulaProfesional, String contrasena, Integer idUsuario, Usuario usuario) {
        this.idMedico = idMedico;
        this.numeroPersonal = numeroPersonal;
        this.cedulaProfesional = cedulaProfesional;
        this.contrasena = contrasena;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
    }

    public Integer getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Integer idMedico) {
        this.idMedico = idMedico;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getCedulaProfesional() {
        return cedulaProfesional;
    }

    public void setCedulaProfesional(String cedulaProfesional) {
        this.cedulaProfesional = cedulaProfesional;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
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
    
    
}
