package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Dieta;
import utilidades.Constantes;

public class DietaImp {

    public static List<Dieta> obtenerTodas() {
        List<Dieta> dietas = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                dietas = conexionBD.selectList("dieta.obtener-todas");
            } finally {
                conexionBD.close();
            }
        }

        return dietas;
    }

    public static List<Dieta> buscar(String filtro) {
        List<Dieta> dietas = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (filtro == null) {
                    filtro = "";
                }

                dietas = conexionBD.selectList("dieta.buscar", filtro);
            } finally {
                conexionBD.close();
            }
        }

        return dietas;
    }

    public static Dieta obtenerPorId(Integer idDieta) {
        Dieta dieta = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                dieta = conexionBD.selectOne("dieta.obtener-por-id", idDieta);
            } finally {
                conexionBD.close();
            }
        }

        return dieta;
    }
    
    public static Dieta obtenerDetalle(Integer idDieta) {
        Dieta dieta = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                dieta = conexionBD.selectOne("dieta.obtener-por-id", idDieta);

                if (dieta != null) {
                    dieta.setAlimentos(
                            conexionBD.selectList(
                                    "dietaAlimento.obtener-por-dieta",
                                    idDieta
                            )
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                conexionBD.close();
            }
        }

        return dieta;
    }

    public static Respuesta registrar(Dieta dieta) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (dieta == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se recibió la información de la dieta.");
                    return respuesta;
                }

                if (dieta.getNombre() == null || dieta.getNombre().trim().isEmpty()) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El nombre de la dieta es obligatorio.");
                    return respuesta;
                }

                if (dieta.getIdMedico() == null || dieta.getIdMedico() <= 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("Debe seleccionar un médico para la dieta.");
                    return respuesta;
                }

                conexionBD.insert("dieta.registrar", dieta);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Dieta registrada correctamente.");
                respuesta.setIdGenerado(dieta.getIdDieta());

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar la dieta: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    public static Respuesta editar(Dieta dieta) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (dieta == null) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se recibió la información de la dieta.");
                    return respuesta;
                }

                if (dieta.getIdDieta() == null || dieta.getIdDieta() <= 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se recibió el identificador de la dieta a editar.");
                    return respuesta;
                }

                if (dieta.getNombre() == null || dieta.getNombre().trim().isEmpty()) {
                    respuesta.setError(true);
                    respuesta.setMensaje("El nombre de la dieta es obligatorio.");
                    return respuesta;
                }

                boolean dietaEnUso = verificarInmutabilidad(dieta.getIdDieta(), conexionBD);

                if (dietaEnUso) {
                    respuesta.setError(true);
                    respuesta.setMensaje("Esta dieta ya fue asignada a un paciente y no puede ser modificada");
                    return respuesta;
                }

                conexionBD.update("dieta.editar", dieta);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Dieta actualizada correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar la dieta: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }
    
    public static Respuesta eliminar(Integer idDieta) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {

                Integer existeDieta = conexionBD.selectOne(
                        "dieta.verificar-existe",
                        idDieta
                );

                if (existeDieta == null || existeDieta == 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No existe una dieta con ese identificador.");
                    return respuesta;
                }

                Integer dietaAsignada = conexionBD.selectOne(
                        "dieta.verificar-asignada",
                        idDieta
                );

                if (dietaAsignada != null && dietaAsignada > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se puede eliminar la dieta porque ya fue asignada.");
                    return respuesta;
                }

                Integer dietaUsada = conexionBD.selectOne(
                         "dieta.verificar-dieta-en-uso",
                        idDieta
                );

                if (dietaUsada != null && dietaUsada > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("No se puede eliminar la dieta porque ya fue usada en una consulta.");
                    return respuesta;
                }

                conexionBD.delete("dieta.eliminar-alimentos", idDieta);
                conexionBD.delete("dieta.eliminar", idDieta);

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Dieta eliminada correctamente.");

            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al eliminar dieta: " + e.getMessage());

            } finally {
                conexionBD.close();
            }

        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    private static boolean verificarInmutabilidad(Integer idDieta, SqlSession conexionBD) {
        Integer totalUsos = conexionBD.selectOne(
                "dieta.verificar-dieta-en-uso",
                idDieta
        );

        return totalUsos != null && totalUsos > 0;
    }
}