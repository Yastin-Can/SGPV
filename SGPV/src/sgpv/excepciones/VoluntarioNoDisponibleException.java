package sgpv.excepciones;

/**
 * Excepcion que aparece cuando se intenta asignar un voluntario a un evento,
 * y este no se encuentra disponible
 * 
 * @author Benja
 */
public class VoluntarioNoDisponibleException extends Exception {
    /**
     * Construye una nueva excepcion de voluntario no disponible con el mensaje especificado.
     * @param mensaje el mensaje describe la causa del error
     */
    public VoluntarioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}