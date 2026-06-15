package dominio;

import dto.Respuesta;
import modelo.mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import pojo.Usuario;
import utilidades.Constantes;

public class UsuarioImp {

    public static Respuesta subirFoto(int idUsuario, byte[] foto){
        Respuesta respuesta = new Respuesta();
        respuesta.setError(true);
        SqlSession conexionBD = MyBatisUtil.getSession();
        
        if(conexionBD != null){
            try{
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(idUsuario);
                usuario.setFotografia(foto);
                
                int filasAfectadas = conexionBD.update("usuario.guardar-foto", usuario);
                conexionBD.commit();
                
                if(filasAfectadas > 0){
                    respuesta.setError(false);
                    respuesta.setMensaje("La fotografía del usuario(a) ha sido guardada exitosamente");
                } else {
                    respuesta.setMensaje("Lo sentimos, la fotografía no se logró guardar");
                }
            } catch (Exception e){
               respuesta.setMensaje(e.getMessage());
            } finally {
                // Siempre cerramos la conexión en el bloque finally
                conexionBD.close();
            }
       } else {
           respuesta.setMensaje(Constantes.MSJ_ERROR_BD);
       }
       return respuesta;
    }

    public static Usuario obtenerFoto(int idUsuario){
       Usuario usuario = null;
       SqlSession conexionBD = MyBatisUtil.getSession();

       if(conexionBD != null){
           try{
               // 1. Usamos tu método auxiliar correctamente
               if (!existeUsuario(conexionBD, idUsuario)) {
                   return null; 
               }

               // 2. Corregimos "colaborador" por "usuario"
               usuario = conexionBD.selectOne("usuario.obtener-foto", idUsuario);

               // 3. Verificamos las propiedades del usuario
               if(usuario != null && usuario.getFotoBase64() != null){
                   String fotoLimpia = usuario.getFotoBase64().replaceAll("\\s+", "");
                   usuario.setFotoBase64(fotoLimpia);
               }

           } catch (Exception e){
               e.printStackTrace();
           } finally {
               // Siempre cerramos la conexión
               conexionBD.close();
           }
      } 

      return usuario;
   }
    
    public static Respuesta correoDisponible(String correo, Integer idUsuario) {
        Respuesta respuesta = new Respuesta();

        try {
            MyBatisUtil myBatisUtil = new MyBatisUtil();
            SqlSession conexion = myBatisUtil.getSession();

            if (conexion != null) {
                Integer existe = conexion.selectOne(
                        "usuario.correo-disponible",
                        new java.util.HashMap<String, Object>() {{
                            put("correo", correo);
                            put("idUsuario", idUsuario);
                        }}
                );

                if (existe != null && existe > 0) {
                    respuesta.setError(true);
                    respuesta.setMensaje("Ya existe otro usuario registrado con ese correo.");
                } else {
                    respuesta.setError(false);
                    respuesta.setMensaje("Correo disponible.");
                }

                conexion.close();
            } else {
                respuesta.setError(true);
                respuesta.setMensaje("No hay conexión con la base de datos.");
            }

        } catch (Exception e) {
            respuesta.setError(true);
            respuesta.setMensaje("Error al validar correo: " + e.getMessage());
        }

        return respuesta;
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