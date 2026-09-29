package sgpv.modelo;

import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.utils.StringUtils;

/**
 * Evento de capacitación donde se enseña una habilidad.
 * No acepta voluntarios que ya tengan esa habilidad.
 */
public class EventoCapacitacion extends Evento {

    private String habilidadQueEnsena;

    public EventoCapacitacion(String idEvento, String nombre, String lugar, String comuna,
            String fecha, int cupos, Prioridad prioridad, String habilidadQueEnsena) {
        super(idEvento, nombre, lugar, comuna, fecha, cupos, prioridad);
        validarHabilidad(habilidadQueEnsena);
        this.habilidadQueEnsena = habilidadQueEnsena.trim();
    }

    public String getHabilidadQueEnsena() {
        return habilidadQueEnsena;
    }

    public void setHabilidadQueEnsena(String habilidadQueEnsena) {
        validarHabilidad(habilidadQueEnsena);
        this.habilidadQueEnsena = habilidadQueEnsena.trim();
    }

    @Override
    public void asignarVoluntario(Voluntario voluntario)
            throws CupoLlenoException, VoluntarioNoDisponibleException {
        if (voluntario != null && voluntario.tieneHabilidad(habilidadQueEnsena)) {
            throw new VoluntarioNoDisponibleException(voluntario.getNombre()
                    + " ya tiene la habilidad que enseña esta capacitación: " + habilidadQueEnsena);
        }
        super.asignarVoluntario(voluntario);
    }

    @Override
    public String getHabilidadAsociada() {
        return habilidadQueEnsena;
    }

    @Override
    public String getTipo() {
        return "CAPACITACION";
    }

    @Override
    public String toString() {
        return super.toString() + " [Capacitación, enseña: " + habilidadQueEnsena + "]";
    }

    private static void validarHabilidad(String habilidad) {
        if (StringUtils.isBlank(habilidad)) {
            throw new IllegalArgumentException(
                    "Una capacitación debe indicar la habilidad que enseña");
        }
    }
}
