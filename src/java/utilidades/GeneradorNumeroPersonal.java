package utilidades;

import java.util.Random;

public class GeneradorNumeroPersonal {

    public static String generarNumeroPersonal(
            String rol,
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno
    ) {

        String prefijo = "FN";

        // Abreviatura del rol
        String rolAbreviado = "";
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

        // Obtiene las primeras 2 letras del nombre
        String primerasNombre = nombre.substring(0, Math.min(2, nombre.length())).toUpperCase();

        // Primera letra del apellido paterno
        String inicialPaterno = apellidoPaterno.substring(0, 1).toUpperCase();

        // Primera letra del apellido materno
        String inicialMaterno = apellidoMaterno.substring(0, 1).toUpperCase();

        // 3 números aleatorios
        Random random = new Random();
        int numeros = 100 + random.nextInt(900); // 100 a 999

        // Concatenar todo
        return String.format("%s-%s-%s%s%s-%03d", 
                prefijo, rolAbreviado, primerasNombre, inicialPaterno, inicialMaterno, numeros);
    }

}