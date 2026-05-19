package dominio;

import dto.Respuesta;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Usuario;
import utilidades.Constantes;

public class UsuarioImp {

    public static Respuesta guardarFoto(int idUsuario, byte[] foto) {
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);

        try (SqlSession conexionBD = MyBatisUtil.getSession()) {

            // Verificar si el usuario existe
            if (!existeUsuario(conexionBD, idUsuario)) {
                respuesta.setMensaje("El usuario no existe");
                return respuesta;
            }

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setFotografia(foto);

            int filasAfectadas = conexionBD.update("usuario.guardar-foto", usuario);
            conexionBD.commit();

            if (filasAfectadas > 0) {
                respuesta.setError(false);
                respuesta.setMensaje("La fotografía del usuario ha sido guardada exitosamente");
            } else {
                respuesta.setMensaje("No se logró guardar la fotografía");
            }

        } catch (Exception e) {
            respuesta.setMensaje("Error al guardar la foto: " + e.getMessage());
        }

        return respuesta;
    }

    public static Usuario obtenerFoto(int idUsuario) {
        Usuario usuario = null;

        try (SqlSession conexionBD = MyBatisUtil.getSession()) {

            if (!existeUsuario(conexionBD, idUsuario)) {
                return null;
            }

            usuario = conexionBD.selectOne("usuario.obtener-foto", idUsuario);

            if (usuario != null && usuario.getFotoBase64() != null) {
                String fotoLimpia = usuario.getFotoBase64().replaceAll("\\s+", "");
                usuario.setFotoBase64(fotoLimpia);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // MÉTODO AUXILIAR 
    private static boolean existeUsuario(SqlSession conexionBD, int idUsuario) {
        try {
            Integer existe = conexionBD.selectOne("usuario.verificar-existe", idUsuario);
            return existe != null && existe > 0;
        } catch (Exception e) {
            return false;
        }
    }
}