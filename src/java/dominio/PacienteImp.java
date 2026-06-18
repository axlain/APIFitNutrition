package dominio;

import dto.Respuesta;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Domicilio;
import pojo.Paciente;
import utilidades.Constantes;

public class PacienteImp {

    public static List<Paciente> obtenerTodos() {
        List<Paciente> pacientes = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                pacientes = conexionBD.selectList("paciente.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return pacientes;
    }

    public static List<Paciente> obtenerRecientes(int limite) {
        List<Paciente> pacientes = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                pacientes = conexionBD.selectList("paciente.obtener-recientes", limite);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return pacientes;
    }

    public static List<Paciente> obtenerRecientesMedico(int idMedico, int limite) {
        List<Paciente> pacientes = null;
        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                HashMap<String, Object> parametros = new HashMap<>();
                parametros.put("idMedico", idMedico);
                parametros.put("limite", limite);
                pacientes = conexionBD.selectList("paciente.obtener-recientes-medico", parametros);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return pacientes;
    }

    public static Paciente obtenerPorId(Integer idPaciente) {

        Paciente paciente = null;

        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                paciente = conexionBD.selectOne(
                        "paciente.obtener-por-id",
                        idPaciente
                );

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                conexionBD.close();
            }
        }

        return paciente;
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
                // Verificar que el correo no este usado por otro usuario (unicidad al editar)
                HashMap<String, Object> parametrosCorreo = new HashMap<>();
                parametrosCorreo.put("correo", paciente.getUsuario().getCorreo());
                parametrosCorreo.put("idUsuario", paciente.getUsuario().getIdUsuario());
                Integer existeCorreo = conexionBD.selectOne(
                        "usuario.verificar-correo-otro-usuario",
                        parametrosCorreo
                );
                if (existeCorreo != null && existeCorreo > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El correo ya está registrado en otro usuario.");
                    return respuesta;
                }

                // Manejar domicilio: actualizar si ya existe, insertar si es nuevo
                Domicilio domicilio = paciente.getUsuario().getDomicilio();
                if (domicilio != null) {
                    if (domicilio.getIdDomicilio() != null) {
                        conexionBD.update("domicilio.editar", domicilio);
                    } else if (tieneDatosDomicilio(domicilio)) {
                        conexionBD.insert("domicilio.registrar", domicilio);
                        Integer idDomicilio = conexionBD.selectOne("domicilio.obtener-id-ultimo");
                        paciente.getUsuario().setIdDomicilio(idDomicilio);
                    }
                }

                // Editar los datos generales del usuario asociado
                conexionBD.update("usuario.editar", paciente.getUsuario());

                // Editar paciente (solo actualiza idMedico)
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

                int filasAfectadas = conexionBD.update(
                        "paciente.dar-baja",
                        idPaciente
                );

                if (filasAfectadas > 0) {

                    // Cancelar las citas activas a futuro del paciente dado de baja.
                    conexionBD.update(
                            "cita.cancelar-por-paciente",
                            idPaciente
                    );

                    conexionBD.commit();

                    respuesta.setError(false);
                    respuesta.setMensaje("Paciente dado de baja correctamente.");

                } else {

                    conexionBD.rollback();

                    respuesta.setError(true);
                    respuesta.setMensaje("No existe un paciente con ese identificador.");
                }

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

    public static Respuesta actualizarCodigoAcceso(Integer idPaciente, String codigoActual, String nuevoCodigo) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                HashMap<String, Object> parametrosValidacion = new HashMap<>();
                parametrosValidacion.put("idPaciente", idPaciente);
                parametrosValidacion.put("codigoActual", codigoActual);

                Integer codigoCorrecto = conexionBD.selectOne(
                        "paciente.verificar-codigo-actual",
                        parametrosValidacion
                );

                if (codigoCorrecto == null || codigoCorrecto == 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El código actual no es correcto.");
                    return respuesta;
                }

                HashMap<String, Object> parametrosCodigo = new HashMap<>();
                parametrosCodigo.put("nuevoCodigo", nuevoCodigo);
                parametrosCodigo.put("idPaciente", idPaciente);

                Integer existeCodigo = conexionBD.selectOne(
                        "paciente.verificar-codigo-acceso-otro-paciente",
                        parametrosCodigo
                );

                if (existeCodigo != null && existeCodigo > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El nuevo código ya está asignado a otro paciente.");
                    return respuesta;
                }

                HashMap<String, Object> parametrosUpdate = new HashMap<>();
                parametrosUpdate.put("idPaciente", idPaciente);
                parametrosUpdate.put("nuevoCodigo", nuevoCodigo);

                int filasAfectadas = conexionBD.update(
                        "paciente.actualizar-codigo-acceso",
                        parametrosUpdate
                );

                if (filasAfectadas > 0) {
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Código de acceso actualizado correctamente.");
                } else {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No existe un paciente con ese identificador.");
                }

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
    // MÉTODOS AUXILIARES PRIVADOS
    // ==========================================
    private static boolean tieneDatosDomicilio(Domicilio domicilio) {
        return (domicilio.getCalle() != null && !domicilio.getCalle().trim().isEmpty())
                || (domicilio.getColonia() != null && !domicilio.getColonia().trim().isEmpty())
                || (domicilio.getMunicipio() != null && !domicilio.getMunicipio().trim().isEmpty())
                || (domicilio.getEstado() != null && !domicilio.getEstado().trim().isEmpty());
    }

    public static Respuesta generarCodigoAcceso(Integer idPaciente) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                String nuevoCodigo = generarCodigoUnico(conexionBD);

                HashMap<String, Object> parametros = new HashMap<>();
                parametros.put("idPaciente", idPaciente);
                parametros.put("nuevoCodigo", nuevoCodigo);

                int filasAfectadas = conexionBD.update("paciente.actualizar-codigo-acceso", parametros);

                if (filasAfectadas > 0) {
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Código de acceso generado correctamente: " + nuevoCodigo);
                } else {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No existe un paciente con ese identificador.");
                }

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al generar el código de acceso: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

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

