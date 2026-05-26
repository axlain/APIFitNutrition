package utilidades;

import java.util.Random;

public class GeneradorNumeroPersonal {

    public static String generarNumeroPersonal(
            String rol,
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno
    ) {

        // Abreviatura del rol
        String rolAbreviado;

        switch (rol.toUpperCase()) {
            case "MEDICO":
                rolAbreviado = "MED";
                break;

            case "ADMINISTRADOR":
                rolAbreviado = "ADM";
                break;

            case "PACIENTE":
                rolAbreviado = "PAC";
                break;

            default:
                rolAbreviado = "UNK";
        }

        // Primera letra del nombre
        String inicialNombre = nombre.substring(0, 1).toUpperCase();

        // Primera letra apellido paterno
        String inicialPaterno = apellidoPaterno.substring(0, 1).toUpperCase();

        // Primera letra apellido materno
        String inicialMaterno = apellidoMaterno.substring(0, 1).toUpperCase();

        // 2 números aleatorios
        Random random = new Random();
        int numeros = 10 + random.nextInt(90);

        return String.format(
                "%s%s%s%s%02d",
                rolAbreviado,
                inicialNombre,
                inicialPaterno,
                inicialMaterno,
                numeros
        );
    }
}