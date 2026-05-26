package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.DietaAlimento;

public class DietaAlimentoImp {

    public static List<DietaAlimento> obtenerPorDieta(Integer idDieta) {
        List<DietaAlimento> alimentos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                alimentos = conexionBD.selectList("dietaAlimento.obtener-por-dieta", idDieta);
            } finally {
                conexionBD.close();
            }
        }

        return alimentos;
    }

    public static DietaAlimento obtenerPorId(Integer idDietaAlimento) {
        DietaAlimento dietaAlimento = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                dietaAlimento = conexionBD.selectOne("dietaAlimento.obtener-por-id", idDietaAlimento);
            } finally {
                conexionBD.close();
            }
        }

        return dietaAlimento;
    }

    public static Respuesta registrar(DietaAlimento dietaAlimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                Respuesta validacion = validar(dietaAlimento);

                if (validacion.isError()) {
                    conexionBD.rollback();
                    return validacion;
                }

                if (verificarDietaEnUso(dietaAlimento.getIdDieta(), conexionBD)) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("Esta dieta ya fue asignada a un paciente y no puede ser modificada");
                    return respuesta;
                }

                conexionBD.insert("dietaAlimento.registrar", dietaAlimento);

                recalcularCaloriasDieta(dietaAlimento.getIdDieta(), conexionBD);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento agregado a la dieta correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al agregar el alimento a la dieta: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    public static Respuesta editar(DietaAlimento dietaAlimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (dietaAlimento == null || dietaAlimento.getIdDietaAlimento() == null || dietaAlimento.getIdDietaAlimento() <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se recibió el alimento de la dieta a editar.");
                    return respuesta;
                }

                Respuesta validacion = validar(dietaAlimento);

                if (validacion.isError()) {
                    conexionBD.rollback();
                    return validacion;
                }

                if (verificarDietaEnUso(dietaAlimento.getIdDieta(), conexionBD)) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("Esta dieta ya fue asignada a un paciente y no puede ser modificada");
                    return respuesta;
                }

                conexionBD.update("dietaAlimento.editar", dietaAlimento);

                recalcularCaloriasDieta(dietaAlimento.getIdDieta(), conexionBD);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento de la dieta actualizado correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar el alimento de la dieta: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    public static Respuesta quitarAlimentoDeDieta(Integer idDietaAlimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (idDietaAlimento == null || idDietaAlimento <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("El id de la relación dieta-alimento es obligatorio.");
                    return respuesta;
                }

                Integer idDieta = conexionBD.selectOne("dietaAlimento.obtener-id-dieta", idDietaAlimento);

                if (idDieta == null || idDieta <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró la relación alimento-dieta.");
                    return respuesta;
                }

                if (verificarDietaEnUso(idDieta, conexionBD)) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("Esta dieta ya fue asignada a un paciente y no puede ser modificada");
                    return respuesta;
                }

                conexionBD.delete("dietaAlimento.eliminar", idDietaAlimento);

                recalcularCaloriasDieta(idDieta, conexionBD);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento quitado de la dieta correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al quitar el alimento de la dieta: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    private static Respuesta validar(DietaAlimento dietaAlimento) {
        Respuesta respuesta = new Respuesta();

        if (dietaAlimento == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió información del alimento.");
            return respuesta;
        }

        if (dietaAlimento.getIdDieta() == null || dietaAlimento.getIdDieta() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("Debe seleccionar una dieta.");
            return respuesta;
        }

        if (dietaAlimento.getIdAlimento() == null || dietaAlimento.getIdAlimento() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("Debe seleccionar un alimento.");
            return respuesta;
        }

        if (dietaAlimento.getIdSegmentoDia() == null || dietaAlimento.getIdSegmentoDia() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("Debe seleccionar un segmento.");
            return respuesta;
        }

        if (dietaAlimento.getCantidad() == null || dietaAlimento.getCantidad() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("La cantidad debe ser mayor a cero.");
            return respuesta;
        }

        respuesta.setError(false);
        respuesta.setMensaje("Validación correcta.");
        return respuesta;
    }

    private static boolean verificarDietaEnUso(Integer idDieta, SqlSession conexionBD) {
        Integer total = conexionBD.selectOne("dietaAlimento.verificar-dieta-en-uso", idDieta);
        return total != null && total > 0;
    }

    private static void recalcularCaloriasDieta(Integer idDieta, SqlSession conexionBD) {
        conexionBD.update("dietaAlimento.recalcular-calorias-dieta", idDieta);
    }
}