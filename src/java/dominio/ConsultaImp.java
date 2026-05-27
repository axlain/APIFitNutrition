package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Consulta;

public class ConsultaImp {

    public static Respuesta registrarConsulta(Consulta consulta) {
        Respuesta respuesta = validarDatosMedicas(consulta);
        if (respuesta.isError()) {
            return respuesta;
        }

        // Aplicar regla de negocio: Calcular IMC
        consulta.setImc(calcularImc(consulta.getPeso(), consulta.getEstatura()));

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                // Regla: Marcar dieta como asignada
                if (consulta.getIdDieta() != null && consulta.getIdDieta() > 0) {
                    conexionBD.update("dieta.marcar-asignada", consulta.getIdDieta());
                }

                int filasAfectadas = conexionBD.insert("consulta.registrar", consulta);

                if (filasAfectadas > 0) {
                    // Regla: Si proviene de una cita, actualizar estatus a "Asistida" (5)
                    if (consulta.getIdCita() != null && consulta.getIdCita() > 0) {
                        conexionBD.update("consulta.marcar-cita-asistida", consulta.getIdCita());
                    }

                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta nutricional registrada correctamente.");
                } else {
                    conexionBD.rollback();
                    respuesta.setMensaje("Hubo un error al registrar la consulta.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setMensaje("Error interno del servidor: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje("Error al conectar con la base de datos.");
        }
        return respuesta;
    }

    public static Respuesta editarConsulta(Consulta consulta) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (consulta.getIdConsulta() == null || consulta.getIdConsulta() <= 0) {
            respuesta.setMensaje("El ID de la consulta es obligatorio para editar.");
            return respuesta;
        }

        // Regla: Recalcular IMC si se actualizan las medidas
        if (consulta.getPeso() != null && consulta.getEstatura() != null) {
            consulta.setImc(calcularImc(consulta.getPeso(), consulta.getEstatura()));
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                // Regla: Si se asigna una nueva dieta, marcarla
                if (consulta.getIdDieta() != null && consulta.getIdDieta() > 0) {
                    conexionBD.update("dieta.marcar-asignada", consulta.getIdDieta());
                }

                int filasAfectadas = conexionBD.update("consulta.editar", consulta);

                if (filasAfectadas > 0) {
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("Consulta actualizada correctamente.");
                } else {
                    conexionBD.rollback();
                    respuesta.setMensaje("No se encontró la consulta o no hubo cambios.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setMensaje("Error interno del servidor al editar: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje("Error al conectar con la base de datos.");
        }
        return respuesta;
    }

    public static Respuesta cancelarCitaAsociada(Integer idCita, String motivoCancelacion) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (idCita == null || idCita <= 0) {
            respuesta.setMensaje("El ID de la cita es obligatorio para cancelarla.");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("idCita", idCita);
                parametros.put("motivoCancelacion", motivoCancelacion);

                // Actualiza estatus a 3 (Cancelada) y guarda el motivo
                int filasAfectadas = conexionBD.update("consulta.cancelar-cita", parametros);

                if (filasAfectadas > 0) {
                    conexionBD.commit();
                    respuesta.setError(false);
                    respuesta.setMensaje("La cita ha sido marcada como cancelada.");
                } else {
                    conexionBD.rollback();
                    respuesta.setMensaje("No se encontró la cita o ya estaba cancelada.");
                }
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setMensaje("Error al cancelar la cita: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setMensaje("Error al conectar con la base de datos.");
        }
        return respuesta;
    }

    public static List<Consulta> buscarConsultas(Integer idConsulta, Integer idPaciente, Integer idMedico, String fecha) {
        List<Consulta> lista = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                // Preparamos los parámetros de búsqueda dinámica
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("idConsulta", idConsulta);
                parametros.put("idPaciente", idPaciente);
                parametros.put("idMedico", idMedico);
                parametros.put("fecha", fecha);

                lista = conexionBD.selectList("consulta.buscar", parametros);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return lista;
    }

    public static List<Consulta> obtenerHistorialPaciente(Integer idPaciente) {
        List<Consulta> historial = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                historial = conexionBD.selectList("consulta.obtener-historial-paciente", idPaciente);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return historial;
    }

    public static Consulta obtenerDetalleConsulta(Integer idConsulta) {
        Consulta detalle = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                detalle = conexionBD.selectOne("consulta.obtener-detalle", idConsulta);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }
        return detalle;
    }
    
    private static Double calcularImc(Double peso, Double estatura) {
        if (peso != null && estatura != null && estatura > 0) {
            double imc = peso / Math.pow(estatura, 2);
            return Math.round(imc * 100.0) / 100.0;
        }
        return null;
    }

    /**
     * Valida que los datos obligatorios para medidas estén presentes.
     */
    private static Respuesta validarDatosMedicas(Consulta consulta) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(false);

        if (consulta.getPeso() == null || consulta.getPeso() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El peso es obligatorio y debe ser mayor a 0.");
            return respuesta;
        }
        if (consulta.getEstatura() == null || consulta.getEstatura() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("La estatura es obligatoria y debe ser mayor a 0.");
            return respuesta;
        }
        return respuesta;
    }
}
