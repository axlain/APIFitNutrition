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
        Respuesta respuesta = validarDatosConsulta(consulta);
        if (respuesta.isError()) {
            return respuesta;
        }

        consulta.setImc(calcularImc(consulta.getPeso(), consulta.getEstatura()));

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (consulta.getIdDieta() != null && consulta.getIdDieta() > 0) {
                    conexionBD.update("dieta.marcar-asignada", consulta.getIdDieta());
                }

                int filasAfectadas = conexionBD.insert("consulta.registrar", consulta);

                if (filasAfectadas > 0) {
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
        Respuesta respuesta = validarDatosConsulta(consulta);
        if (respuesta.isError()) {
            return respuesta;
        }

        if (consulta.getIdConsulta() == null || consulta.getIdConsulta() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El ID de la consulta es obligatorio para editar.");
            return respuesta;
        }

        consulta.setImc(calcularImc(consulta.getPeso(), consulta.getEstatura()));

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
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

    public static Consulta buscarConsulta(Integer idConsulta) {
        Consulta consulta = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                consulta = conexionBD.selectOne("consulta.buscar", idConsulta);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return consulta;
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
    
    public static List<Consulta> obtenerHistorialMedico(Integer idMedico) {
        List<Consulta> historial = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                historial = conexionBD.selectList("consulta.obtener-historial-medico", idMedico);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return historial;
    }

    public static List<Consulta> obtenerTodas() {
        List<Consulta> lista = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                lista = conexionBD.selectList("consulta.obtener-todas");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return lista;
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

    private static Respuesta validarDatosConsulta(Consulta consulta) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(false);

        if (consulta == null) {
            respuesta.setError(true);
            respuesta.setMensaje("Los datos de la consulta son obligatorios.");
            return respuesta;
        }

        if (consulta.getFecha() == null || consulta.getFecha().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La fecha de la consulta es obligatoria.");
            return respuesta;
        }

        if (consulta.getHora() == null || consulta.getHora().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La hora de la consulta es obligatoria.");
            return respuesta;
        }

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

        if (consulta.getTalla() == null || consulta.getTalla().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("La talla es obligatoria.");
            return respuesta;
        }

        if (consulta.getIdPaciente() == null || consulta.getIdPaciente() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El paciente es obligatorio.");
            return respuesta;
        }

        if (consulta.getIdMedico() == null || consulta.getIdMedico() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("El médico es obligatorio.");
            return respuesta;
        }

        return respuesta;
    }
}