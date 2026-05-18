package utilidades;

public class GeneradorNumeroPersonal {

    public static String generarNumeroPersonal(
            String rol,
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno
    ) {

        // Obtiene las primeras 2 letras del nombre
        String primerasNombre = nombre
                .substring(0, 2)
                .toUpperCase();

        // Obtiene la primera letra del apellido paterno
        String inicialPaterno = apellidoPaterno
                .substring(0, 1)
                .toUpperCase();

        // Obtiene la primera letra del apellido materno
        String inicialMaterno = apellidoMaterno
                .substring(0, 1)
                .toUpperCase();

        // Retorna únicamente las letras solicitadas (Ejemplo: AXVR)
        return primerasNombre + inicialPaterno + inicialMaterno;
    }

}