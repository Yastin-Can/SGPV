package sgpv.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import sgpv.modelo.Voluntario;

/**
 * Presentacion y entrada de datos, no diferencia entre consola y ventana, para que sirva como un mismo flujo.
 */
public interface Vista {

    void mostrarTitulo(String titulo);

    void mostrarMensaje(String mensaje);

    void mostrarError(String mensaje);

    /** Muestra una lista de líneas ya con el formato (una por elemento). */
    void mostrarLista(List<String> lineas);

    /**
     * Presenta las opciones dadas y devuelve el índice (0 en adelante) de
     * la elegida, o -1 si el usuario elige volver/salir/cancelar.
     */
    int pedirOpcion(List<String> opciones);


    String pedirTexto(String etiqueta);

    /** Pide un numero entero dentro de un rango, reintentando hasta obtener uno valido. */
    int pedirEntero(String etiqueta, int min, int max);


    boolean pedirBooleano(String etiqueta);

    default String pedirFecha(String etiqueta) {
        while (true) {
            String fecha = pedirTexto(etiqueta + " (formato DD-MM-AAAA)");
            if (Vista.esFechaValida(fecha)) {
                return fecha;
            }
            mostrarError("Fecha inválida. Debe tener el formato DD-MM-AAAA (ejemplo: 15-03-2026).");
        }
    }

    /** Valida que el texto tenga exactamente el formato DD-MM-AAAA y sea una fecha real. */
    static boolean esFechaValida(String texto) {
        if (texto == null || !texto.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }
        try {
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(texto, formato);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    default String pedirRut(String etiqueta) {
        while (true) {
            String rut = pedirTexto(etiqueta + " (formato 11111111-1)");
            if (Voluntario.tieneRutValido(rut)) {
                return rut;
            }
            mostrarError("RUT inválido. Debe tener el formato 11111111-1 (7 u 8 dígitos, guion, dígito verificador o K).");
        }
    }
}
