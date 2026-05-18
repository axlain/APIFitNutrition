package dominio;

import dto.RSAutenticar;
import java.util.HashMap;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Administrador;
import pojo.Medico;
import pojo.Paciente;
import utilidades.Constantes;

public class AutenticarImp {

    public static RSAutenticar loginAdministrador(String numeroPersonal, String contrasena) {

        RSAutenticar respuesta = new RSAutenticar();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("numero_personal", numeroPersonal);
                parametros.put("contrasena", contrasena);

                Administrador administrador = conexionBD.selectOne(
                        "autentificar.administrador",
                        parametros
                );

                if (administrador != null) {

                    respuesta.setError(false);
                    respuesta.setTipoUsuario("Administrador");

                    respuesta.setMensaje(
                            "Bienvenido: "
                            + administrador.getUsuario().getNombre()
                            + " "
                            + administrador.getUsuario().getApellidoPaterno()
                    );

                    respuesta.setAdministrador(administrador);

                } else {

                    respuesta.setError(true);
                    respuesta.setMensaje("Número personal o contraseña incorrectos.");
                }

            } catch (Exception e) {

                respuesta.setError(true);
                respuesta.setMensaje("Error al autentificar: " + e.getMessage());

            } finally {
                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static RSAutenticar loginMedico(String numeroPersonal, String contrasena) {

        RSAutenticar respuesta = new RSAutenticar();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("numero_personal", numeroPersonal);
                parametros.put("contrasena", contrasena);

                Medico medico = conexionBD.selectOne(
                        "autentificar.medico",
                        parametros
                );

                if (medico != null) {

                    respuesta.setError(false);
                    respuesta.setTipoUsuario("Medico");

                    respuesta.setMensaje(
                            "Bienvenido Dr(a). "
                            + medico.getUsuario().getNombre()
                            + " "
                            + medico.getUsuario().getApellidoPaterno()
                    );

                    respuesta.setMedico(medico);

                } else {

                    respuesta.setError(true);
                    respuesta.setMensaje("Número personal o contraseña incorrectos.");
                }

            } catch (Exception e) {

                respuesta.setError(true);
                respuesta.setMensaje("Error al autentificar: " + e.getMessage());

            } finally {
                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static RSAutenticar loginPaciente(String correo, String codigoAcceso) {

        RSAutenticar respuesta = new RSAutenticar();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("correo", correo);
                parametros.put("codigo_acceso", codigoAcceso);

                Paciente paciente = conexionBD.selectOne(
                        "autentificar.paciente",
                        parametros
                );

                if (paciente != null) {

                    respuesta.setError(false);
                    respuesta.setTipoUsuario("Paciente");

                    respuesta.setMensaje(
                            "Bienvenido: "
                            + paciente.getUsuario().getNombre()
                            + " "
                            + paciente.getUsuario().getApellidoPaterno()
                    );

                    respuesta.setPaciente(paciente);

                } else {

                    respuesta.setError(true);
                    respuesta.setMensaje("Correo o código de acceso incorrectos.");
                }

            } catch (Exception e) {

                respuesta.setError(true);
                respuesta.setMensaje("Error al autentificar: " + e.getMessage());

            } finally {
                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

}