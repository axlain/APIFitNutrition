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

        // Inicial de cada parte, segura ante valores vacios o nulos (p. ej. apellido materno opcional)
        String inicialNombre = inicial(nombre);
        String inicialPaterno = inicial(apellidoPaterno);
        String inicialMaterno = inicial(apellidoMaterno);

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

    private static String inicial(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "X";
        }
        return texto.trim().substring(0, 1).toUpperCase();
    }
}