package pojo;

public class Administrador {
    private Integer idAdministrador;
    private String numeroPersonal;
    private String contrasena;
    
    private Integer idUsuario; 
    private Usuario usuario;

    public Administrador() {
    }

    public Administrador(Integer idAdministrador, String numeroPersonal, String contrasena, Integer idUsuario, Usuario usuario) {
        this.idAdministrador = idAdministrador;
        this.numeroPersonal = numeroPersonal;
        this.contrasena = contrasena;
        this.idUsuario = idUsuario;
        this.usuario = usuario;
    }

    public Integer getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(Integer idAdministrador) {
        this.idAdministrador = idAdministrador;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public void setNumeroPersonal(String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
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
