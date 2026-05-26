package dominio;

import dto.Respuesta;
import java.util.ArrayList;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Alimento;

public class AlimentoImp {

    public static List<Alimento> obtenerActivos() {
        List<Alimento> alimentos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                alimentos = conexionBD.selectList("alimento.obtener-activos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return alimentos;
    }

    public static List<Alimento> buscar(String filtro) {
        List<Alimento> alimentos = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (filtro == null) {
                    filtro = "";
                }

                alimentos = conexionBD.selectList("alimento.buscar", filtro);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return alimentos;
    }

    public static Respuesta registrar(Alimento alimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                Respuesta validacion = validarAlimento(alimento, false);

                if (validacion.isError()) {
                    conexionBD.rollback();
                    return validacion;
                }

                conexionBD.insert("alimento.registrar", alimento);
                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento registrado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar el alimento: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    public static Respuesta editar(Alimento alimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                Respuesta validacion = validarAlimento(alimento, true);

                if (validacion.isError()) {
                    conexionBD.rollback();
                    return validacion;
                }

                boolean cambioCalorias = verificarCambioCalorias(alimento, conexionBD);

                int filasAfectadas = conexionBD.update("alimento.editar", alimento);

                if (filasAfectadas <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró el alimento a editar.");
                    return respuesta;
                }

                if (cambioCalorias) {
                    actualizarDietasAfectadas(alimento.getIdAlimento(), conexionBD);
                }

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento actualizado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar el alimento: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    public static Respuesta darBaja(Integer idAlimento) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                if (idAlimento == null || idAlimento <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("El id del alimento es obligatorio.");
                    return respuesta;
                }

                if (verificarAlimentoEnDieta(idAlimento, conexionBD)) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se puede eliminar el alimento porque ya está asignado a una dieta.");
                    return respuesta;
                }

                int filasAfectadas = conexionBD.delete("alimento.dar-baja", idAlimento);

                if (filasAfectadas <= 0) {
                    conexionBD.rollback();
                    respuesta.setError(true);
                    respuesta.setMensaje("No se encontró el alimento a eliminar.");
                    return respuesta;
                }

                conexionBD.commit();

                respuesta.setError(false);
                respuesta.setMensaje("Alimento eliminado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al eliminar el alimento: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje("No hay conexión con la base de datos.");
        }

        return respuesta;
    }

    private static boolean verificarCambioCalorias(Alimento alimentoNuevo, SqlSession conexionBD) {
        Alimento alimentoOriginal = conexionBD.selectOne(
                "alimento.obtener-por-id",
                alimentoNuevo.getIdAlimento()
        );

        if (alimentoOriginal == null) {
            throw new IllegalArgumentException("No se encontró el alimento original.");
        }

        Double caloriasOriginales = alimentoOriginal.getCaloriasPorPorcion();
        Double caloriasNuevas = alimentoNuevo.getCaloriasPorPorcion();

        if (caloriasOriginales == null && caloriasNuevas == null) {
            return false;
        }

        if (caloriasOriginales == null || caloriasNuevas == null) {
            return true;
        }

        return Double.compare(caloriasOriginales, caloriasNuevas) != 0;
    }

    private static void actualizarDietasAfectadas(Integer idAlimento, SqlSession conexionBD) {
        conexionBD.update("dieta.recalcular-calorias-por-alimento", idAlimento);
    }

    private static boolean verificarAlimentoEnDieta(Integer idAlimento, SqlSession conexionBD) {
        Integer total = conexionBD.selectOne("alimento.verificar-en-dieta", idAlimento);
        return total != null && total > 0;
    }

    private static Respuesta validarAlimento(Alimento alimento, boolean validarId) {
        Respuesta respuesta = new Respuesta();

        if (alimento == null) {
            respuesta.setError(true);
            respuesta.setMensaje("No se recibió la información del alimento.");
            return respuesta;
        }

        if (validarId && (alimento.getIdAlimento() == null || alimento.getIdAlimento() <= 0)) {
            respuesta.setError(true);
            respuesta.setMensaje("El id del alimento es obligatorio.");
            return respuesta;
        }

        if (alimento.getNombre() == null || alimento.getNombre().trim().isEmpty()) {
            respuesta.setError(true);
            respuesta.setMensaje("El nombre del alimento es obligatorio.");
            return respuesta;
        }

        if (alimento.getPorcion() == null || alimento.getPorcion() <= 0) {
            respuesta.setError(true);
            respuesta.setMensaje("La porción debe ser mayor a cero.");
            return respuesta;
        }

        if (alimento.getCaloriasPorPorcion() == null || alimento.getCaloriasPorPorcion() < 0) {
            respuesta.setError(true);
            respuesta.setMensaje("Las calorías no pueden ser negativas.");
            return respuesta;
        }

        if (!validarId && (alimento.getIdUnidadPorcion() == null || alimento.getIdUnidadPorcion() <= 0)) {
            respuesta.setError(true);
            respuesta.setMensaje("Debe seleccionar una unidad de porción.");
            return respuesta;
        }

        respuesta.setError(false);
        respuesta.setMensaje("Validación correcta.");
        return respuesta;
    }
}