package sgpv.ui;

import java.util.List;

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

    /** Pide un texto libre. Devuelve "" (no null) si el usuario no ingresa nada. */
    String pedirTexto(String etiqueta);

    /** Pide un número entero dentro de un rango, reintentando hasta obtener uno válido. */
    int pedirEntero(String etiqueta, int min, int max);

    /** Pide una respuesta sí/no. */
    boolean pedirBooleano(String etiqueta);
}
