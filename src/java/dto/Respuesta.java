package dto;

public class Respuesta {
    private boolean error;
    private String mensaje;
    private Integer idGenerado;

    public Respuesta() {
    }
    
    public Respuesta(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getIdGenerado() {
        return idGenerado;
    }

    public void setIdGenerado(Integer idGenerado) {
        this.idGenerado = idGenerado;
    }

}
