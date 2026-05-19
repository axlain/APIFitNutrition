package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Administrador;
import pojo.Medico;
import pojo.Usuario;
import utilidades.Constantes;

public class AdministradorImp {
    
    public static List<Administrador> obtenerTodos() {
        List<Administrador> administradores = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                administradores = conexionBD.selectList(
                        "administrador.obtener-todos"
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return administradores;
    }
    
    public static Respuesta registrar(Administrador admin) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // 1. Registrar domicilio primero
                conexionBD.insert("domicilio.registrar", admin.getUsuario().getDomicilio());
                Integer idDomicilio = conexionBD.selectOne("domicilio.obtener-id-ultimo");
                admin.getUsuario().setIdDomicilio(idDomicilio);

                // 2. Verificar correo
                Integer existeCorreo = conexionBD.selectOne(
                        "usuario.verificar-correo",
                        admin.getUsuario().getCorreo()
                );
                if (existeCorreo > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo ya se encuentra registrado.");
                    return respuesta;
                }

                // 3. Generar número personal único
                boolean numeroValido = false;
                String numeroPersonal = "";
                while (!numeroValido) {
                    numeroPersonal = utilidades.GeneradorNumeroPersonal.generarNumeroPersonal(
                            "ADMINISTRADOR",
                            admin.getUsuario().getNombre(),
                            admin.getUsuario().getApellidoPaterno(),
                            admin.getUsuario().getApellidoMaterno()
                    );
                    Integer existeNumero = conexionBD.selectOne(
                            "administrador.verificar-numero-personal",
                            numeroPersonal
                    );
                    if (existeNumero == 0) {
                        numeroValido = true;
                    }
                }
                admin.setNumeroPersonal(numeroPersonal);

                // 4. Registrar usuario
                conexionBD.insert("usuario.registrar", admin.getUsuario());
                Integer idUsuario = conexionBD.selectOne(
                        "usuario.obtener-id-por-correo",
                        admin.getUsuario().getCorreo()
                );
                admin.setIdUsuario(idUsuario);

                // 5. Registrar administrador
                conexionBD.insert("administrador.registrar", admin);

                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Administrador registrado correctamente. Número personal: " + numeroPersonal);

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar administrador: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editar(Administrador admin) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Solo se editan los datos del usuario
                conexionBD.update("usuario.editar", admin.getUsuario());

                // Administrador no se edita numeroPersonal ni contrasena
                conexionBD.update("administrador.editar", admin);

                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Administrador editado correctamente (sin modificar contraseña ni número personal).");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar administrador: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta cambiarContrasena(Integer idAdmin, String contrasenaActual, String nuevaContrasena) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (idAdmin == null || idAdmin <= 0 || contrasenaActual == null || contrasenaActual.isEmpty()
                || nuevaContrasena == null || nuevaContrasena.isEmpty()) {
            respuesta.setMensaje("Datos inválidos");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                HashMap<String, Object> parametrosVerificacion = new HashMap<>();
                parametrosVerificacion.put("idAdministrador", idAdmin);
                parametrosVerificacion.put("contrasenaActual", contrasenaActual);

                // Verificar la contraseña actual
                Integer existe = conexionBD.selectOne("administrador.verificar-contrasena", parametrosVerificacion);

                if (existe == 0) {
                    respuesta.setMensaje("La contraseña actual es incorrecta.");
                } else {
                    HashMap<String, Object> parametrosUpdate = new HashMap<>();
                    parametrosUpdate.put("idAdministrador", idAdmin);
                    parametrosUpdate.put("nuevaContrasena", nuevaContrasena);

                    conexionBD.update("administrador.cambiar-contrasena", parametrosUpdate);
                    conexionBD.commit();

                    respuesta.setError(false);
                    respuesta.setMensaje("Contraseña actualizada correctamente.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setMensaje("Error al actualizar la contraseña: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
}