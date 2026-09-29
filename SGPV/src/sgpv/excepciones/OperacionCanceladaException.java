package sgpv.excepciones;

/**
 * Excepcion que se lanza cuando el usuario cancela una operacion a mitad de camino.
 */
public class OperacionCanceladaException extends RuntimeException {

    public OperacionCanceladaException() {
        super("Operación cancelada.");
    }
}
