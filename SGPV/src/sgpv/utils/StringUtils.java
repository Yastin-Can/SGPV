package sgpv.utils;

/**
 * Clase de utilidad para operaciones relacionadas con cadenas de texto.
 * @author Benja
 */

public final class StringUtils{
    private StringUtils() {}
    /**
     * Verifica si un texto es nulo, está vacío o contiene únicamente espacios en blanco.
     * 
     * @_param str la cadena de texto a evaluar
     * @return true si el texto es nulo, vacio o tiene solo espacios, false en caso contrario.
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
