package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Consulta;

public class ConsultaImp {

    public static Respuesta registrarConsulta(Consulta consulta) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        if (consulta.getPeso() != null && consulta.getEstatura() != null && consulta.getEstatura() > 0) {
            double imc = consulta.getPeso() / Math.pow(consulta.getEstatura(), 2);
            imc = Math.round(imc * 100.0) / 100.0;
            consulta.setImc(imc);
        } else {
            respuesta.setMensaje("El peso y la estatura son obligatorios y mayores a 0 para calcular el IMC.");
            return respuesta;
        }

        SqlSession conexionBD = MyBatisUtil.getSession();
        if (conexionBD != null) {
            try {
                if (consulta.getIdDieta() != null && consulta.getIdDieta() > 0) {
                    conexionBD.update("dieta.marcar-asignada", consulta.getIdDieta());
                }

                int filasAfectadas = conexionBD.insert("consulta.registrar", consulta);

                if (filasAfectadas > 0) {
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
}
