package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Medico;
import pojo.Usuario;
import utilidades.Constantes;

public class MedicoImp {

    public static List<Medico> obtenerTodos() {
        List<Medico> medicos = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                medicos = conexionBD.selectList(
                        "medico.obtener-activos"
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return medicos;
    }
    
    public static Medico obtenerPorId(Integer idMedico) {
        Medico medico = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                medico = conexionBD.selectOne("medico.obtener-por-id", idMedico);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return medico;
    }

    public static Respuesta registrar(Medico medico) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // 1. Registrar domicilio primero
                conexionBD.insert("domicilio.registrar", medico.getUsuario().getDomicilio());
                Integer idDomicilio = conexionBD.selectOne("domicilio.obtener-id-ultimo");
                medico.getUsuario().setIdDomicilio(idDomicilio);

                // 2. Verificar correo
                Integer existeCorreo = conexionBD.selectOne(
                        "usuario.verificar-correo",
                        medico.getUsuario().getCorreo()
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
                            "MEDICO",
                            medico.getUsuario().getNombre(),
                            medico.getUsuario().getApellidoPaterno(),
                            medico.getUsuario().getApellidoMaterno()
                    );
                    Integer existeNumero = conexionBD.selectOne(
                            "medico.verificar-numero-personal",
                            numeroPersonal
                    );
                    if (existeNumero == 0) {
                        numeroValido = true;
                    }
                }
                medico.setNumeroPersonal(numeroPersonal);

                // 4. Registrar usuario
                conexionBD.insert("usuario.registrar", medico.getUsuario());
                Integer idUsuario = conexionBD.selectOne(
                        "usuario.obtener-id-por-correo",
                        medico.getUsuario().getCorreo()
                );
                medico.setIdUsuario(idUsuario);

                // 5. Registrar médico
                conexionBD.insert("medico.registrar", medico);

                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Médico registrado correctamente. Número personal: " + numeroPersonal);

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar médico: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editar(Medico medico) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                // Obtener idUsuario asociado al médico
                Integer idUsuario = conexionBD.selectOne(
                        "medico.obtener-id-usuario",
                        medico.getIdMedico()
                );

                medico.setIdUsuario(idUsuario);
                medico.getUsuario().setIdUsuario(idUsuario);

                // Editar usuario
                conexionBD.update("usuario.editar", medico.getUsuario());

                // Editar médico
                conexionBD.update("medico.editar", medico);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Médico editado correctamente.");

            } catch (Exception e) {

                conexionBD.rollback();

                respuesta.setError(true);
                respuesta.setMensaje("Error al editar médico: " + e.getMessage());

            } finally {

                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static List<Medico> buscar(String filtro) {

        List<Medico> medicos = null;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                medicos = conexionBD.selectList(
                        "medico.buscar",
                        filtro
                );

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                conexionBD.close();
            }
        }

        return medicos;
    }

    public static Respuesta darBaja(Integer idMedico) {

        Respuesta respuesta = new Respuesta();

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {

            try {

                conexionBD.update(
                        "medico.dar-baja",
                        idMedico
                );

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Médico dado de baja correctamente.");

            } catch (Exception e) {

                conexionBD.rollback();

                respuesta.setError(true);
                respuesta.setMensaje("Error al dar de baja: " + e.getMessage());

            } finally {

                conexionBD.close();
            }

        } else {

            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }
    
    public static Respuesta cambiarContrasena(Integer idMedico, String contrasenaActual, String nuevaContrasena) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (idMedico == null || idMedico <= 0 || contrasenaActual == null || contrasenaActual.isEmpty()
                || nuevaContrasena == null || nuevaContrasena.isEmpty()) {
            respuesta.setMensaje("Datos inválidos");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                HashMap<String, Object> parametrosVerificacion = new HashMap<>();
                parametrosVerificacion.put("idMedico", idMedico);
                parametrosVerificacion.put("contrasenaActual", contrasenaActual);

                int existe = conexionBD.selectOne("medico.verificar-contrasena", parametrosVerificacion);

                if (existe == 0) {
                    respuesta.setMensaje("La contraseña actual es incorrecta.");
                } else {
                    HashMap<String, Object> parametrosUpdate = new HashMap<>();
                    parametrosUpdate.put("idMedico", idMedico);
                    parametrosUpdate.put("nuevaContrasena", nuevaContrasena);

                    conexionBD.update("medico.cambiar-contrasena", parametrosUpdate);
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