package sgpv.excepciones;

/**
 * Excepcion que se lanza cuando se intenta registrar un voluntario o participante
 * en un evento que ya ha alcanzado su límite máximo de cupos disponibles.
 * @author Benja
 */
public class CupoLlenoException extends Exception {
    /**
     * Construye una nueva excepcion de voluntario no disponible con un mensaje especificado.
     * 
     * @param mensaje el mensaje describe la causa del error
     */
    public CupoLlenoException(String mensaje) {
        super(mensaje);
    }
}