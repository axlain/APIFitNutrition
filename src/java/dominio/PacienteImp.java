package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Paciente;
import utilidades.Constantes;

public class PacienteImp {

    public static List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                // Puedes cambiar el id del mapper si prefieres que traiga solo activos
                pacientes = conexionBD.selectList("paciente.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return pacientes;
    }

    public static Respuesta registrar(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // 1. Registrar domicilio primero (si aplica)
                if (paciente.getUsuario().getDomicilio() != null) {
                    conexionBD.insert("domicilio.registrar", paciente.getUsuario().getDomicilio());
                    Integer idDomicilio = conexionBD.selectOne("domicilio.obtener-id-ultimo");
                    paciente.getUsuario().setIdDomicilio(idDomicilio);
                }

                // 2. Verificar correo
                Integer existeCorreo = conexionBD.selectOne(
                        "usuario.verificar-correo",
                        paciente.getUsuario().getCorreo()
                );
                if (existeCorreo != null && existeCorreo > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo ya se encuentra registrado.");
                    return respuesta;
                }

                // 3. Generar código de acceso de 4 dígitos único
                String codigoAcceso = generarCodigoUnico(conexionBD);
                paciente.setCodigoAcceso(codigoAcceso);

                // 4. Registrar usuario
                conexionBD.insert("usuario.registrar", paciente.getUsuario());
                Integer idUsuario = conexionBD.selectOne(
                        "usuario.obtener-id-por-correo",
                        paciente.getUsuario().getCorreo()
                );
                paciente.setIdUsuario(idUsuario);

                // 5. Registrar paciente (se asume que paciente.getIdMedico() ya viene asignado desde la UI)
                conexionBD.insert("paciente.registrar", paciente);

                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Paciente registrado correctamente. Código de acceso: " + codigoAcceso);

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar paciente: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static Respuesta editar(Paciente paciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Editar los datos generales del usuario asociado
                conexionBD.update("usuario.editar", paciente.getUsuario());

                // Editar paciente (basado en el mapper anterior, solo actualiza idMedico)
                conexionBD.update("paciente.editar", paciente);

                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Paciente editado correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar paciente: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    public static List<Paciente> buscar(String filtro) {
        List<Paciente> pacientes = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                pacientes = conexionBD.selectList("paciente.buscar", filtro);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return pacientes;
    }

    public static Respuesta darBaja(Integer idPaciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("paciente.dar-baja", idPaciente);
                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Paciente dado de baja correctamente.");

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

    public static Respuesta actualizarCodigoAcceso(Integer idPaciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // 1. Generar nuevo código de acceso único
                String nuevoCodigo = generarCodigoUnico(conexionBD);

                // 2. Preparar parámetros para el mapper
                HashMap<String, Object> parametrosUpdate = new HashMap<>();
                parametrosUpdate.put("idPaciente", idPaciente);
                parametrosUpdate.put("nuevoCodigo", nuevoCodigo);

                // 3. Ejecutar actualización
                conexionBD.update("paciente.actualizar-codigo-acceso", parametrosUpdate);
                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Código de acceso actualizado correctamente: " + nuevoCodigo);

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al actualizar el código de acceso: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // ==========================================
    // MÉTODO AUXILIAR PRIVADO
    // ==========================================
    private static String generarCodigoUnico(SqlSession conexionBD) {
        boolean esUnico = false;
        String codigoGenerado = "";
        Random random = new Random();

        while (!esUnico) {
            // Genera un número entre 0 y 9999 y lo formatea con ceros a la izquierda si es necesario
            int numeroAleatorio = random.nextInt(10000);
            codigoGenerado = String.format("%04d", numeroAleatorio);

            Integer existeCodigo = conexionBD.selectOne("paciente.verificar-codigo-acceso", codigoGenerado);
            
            if (existeCodigo == null || existeCodigo == 0) {
                esUnico = true;
            }
        }
        
        return codigoGenerado;
    }
}

