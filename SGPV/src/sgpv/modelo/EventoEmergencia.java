package sgpv.modelo;

import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.utils.StringUtils;

/**
 * Evento de respuesta a una emergencia (incendio, inundación, etc.).
 * Solo acepta voluntarios con la habilidad requerida y prioridad ALTA o CATASTROFE.
 */
public class EventoEmergencia extends Evento {

    private String habilidadRequerida;

    public EventoEmergencia(String idEvento, String nombre, String lugar, String comuna,
            String fecha, int cupos, Prioridad prioridad, String habilidadRequerida) {
        super(idEvento, nombre, lugar, comuna, fecha, cupos, prioridad);
        validarPrioridad(prioridad);
        validarHabilidad(habilidadRequerida);
        this.habilidadRequerida = habilidadRequerida.trim();
    }

    public String getHabilidadRequerida() {
        return habilidadRequerida;
    }

    public void setHabilidadRequerida(String habilidadRequerida) {
        validarHabilidad(habilidadRequerida);
        this.habilidadRequerida = habilidadRequerida.trim();
    }

    @Override
    public void asignarVoluntario(Voluntario voluntario)
            throws CupoLlenoException, VoluntarioNoDisponibleException {
        if (voluntario != null && !voluntario.tieneHabilidad(habilidadRequerida)) {
            throw new VoluntarioNoDisponibleException(voluntario.getNombre()
                    + " no tiene la habilidad requerida para esta emergencia: " + habilidadRequerida);
        }
        super.asignarVoluntario(voluntario);
    }

    @Override
    public void setPrioridad(Prioridad prioridad) {
        validarPrioridad(prioridad);
        super.setPrioridad(prioridad);
    }

    @Override
    public String getHabilidadAsociada() {
        return habilidadRequerida;
    }

    @Override
    public String getTipo() {
        return "EMERGENCIA";
    }

    @Override
    public String toString() {
        return super.toString() + " [Emergencia, requiere: " + habilidadRequerida + "]";
    }

    private static void validarPrioridad(Prioridad prioridad) {
        if (prioridad != Prioridad.ALTA && prioridad != Prioridad.CATASTROFE) {
            throw new IllegalArgumentException(
                    "Un evento de emergencia solo puede tener prioridad ALTA o CATASTROFE");
        }
    }

    private static void validarHabilidad(String habilidad) {
        if (StringUtils.isBlank(habilidad)) {
            throw new IllegalArgumentException(
                    "Un evento de emergencia debe indicar la habilidad requerida");
        }
    }
}
