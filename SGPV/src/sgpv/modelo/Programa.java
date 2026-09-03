package sgpv;
import java.util.HashMap;
import java.util.Map;
import sgpv.evento.Evento;

/**
 *
 * @author Benja
 */

public class Programa{
    private String idPrograma;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private String organizacion;
    private Map<String, Evento> eventos;
    
    public Programa(String idPrograma, String titulo, String descripcion, String fechaInicio, String organizacion) {
        this.idPrograma = idPrograma;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.organizacion = organizacion;
        this.eventos = new HashMap<>();
    }

    // Recibe un objeto "Evento" ya instanciado
    public boolean agregarEvento(Evento evento) {
        if (evento == null || evento.getIdEvento() == null || eventos.containsKey(evento.getIdEvento())) {
            return false;
        }
        eventos.put(evento.getIdEvento(), evento);
        return true;
    }

    // Recibe los datos sueltos y crea el Evento internamente, esto cumple con ser una sobrecarga
    public boolean agregarEvento(String idEvento, String nombre, String lugar, String fecha, int cupos, String prioridad) {
        if (idEvento == null) {
            return false;
        }
        Evento nuevo = new Evento(idEvento, nombre, lugar, fecha, cupos, prioridad);
        return agregarEvento(nuevo);
    }
}
