package dominio;

import dto.Respuesta;
import java.util.List;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Domicilio;
import utilidades.Constantes;

public class DomicilioImp {

    // =========================================
    // REGISTRAR DOMICILIO
    // =========================================
    public static Respuesta registrar(Domicilio domicilio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.insert("domicilio.registrar", domicilio);
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Domicilio registrado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al registrar domicilio: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // =========================================
    // EDITAR DOMICILIO
    // =========================================
    public static Respuesta editar(Domicilio domicilio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.update("domicilio.editar", domicilio);
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Domicilio editado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al editar domicilio: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // =========================================
    // ELIMINAR DOMICILIO
    // =========================================
    public static Respuesta eliminar(Integer idDomicilio) {
        Respuesta respuesta = new Respuesta();
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                conexionBD.delete("domicilio.eliminar", idDomicilio);
                conexionBD.commit();
                respuesta.setError(false);
                respuesta.setMensaje("Domicilio eliminado correctamente.");
            } catch (Exception e) {
                conexionBD.rollback();
                respuesta.setError(true);
                respuesta.setMensaje("Error al eliminar domicilio: " + e.getMessage());
            } finally {
                conexionBD.close();
            }
        } else {
            respuesta.setError(true);
            respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
        }

        return respuesta;
    }

    // =========================================
    // OBTENER DOMICILIO POR ID
    // =========================================
    public static Domicilio obtenerPorId(Integer idDomicilio) {
        Domicilio domicilio = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                domicilio = conexionBD.selectOne("domicilio.obtener-por-id", idDomicilio);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return domicilio;
    }

    // =========================================
    // OBTENER TODOS LOS DOMICILIOS
    // =========================================
    public static List<Domicilio> obtenerTodos() {
        List<Domicilio> domicilios = null;
        SqlSession conexionBD = MyBatisUtil.getSession();

        if (conexionBD != null) {
            try {
                domicilios = conexionBD.selectList("domicilio.obtener-todos");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conexionBD.close();
            }
        }

        return domicilios;
    }
}