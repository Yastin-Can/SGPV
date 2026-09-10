package sgpv.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sgpv.evento.Evento;
import sgpv.utils.StringUtils;

/**
 *
 * @author Benja
 */
public class Programa {
    private String idPrograma;
    private String titulo;
    private String descripcion;
    private String fechaInicio;
    private Map<String, Evento> eventos;

    public Programa(String idPrograma, String titulo, String descripcion, String fechaInicio){
        if(StringUtils.isBlank(idPrograma)){
            throw new IllegalArgumentException("El idPrograma no puede ser nulo o vacío.");
        }
        if(StringUtils.isBlank(titulo)){
            throw new IllegalArgumentException("El título del programa no puede ser nulo o vacío.");
        }
        
        this.idPrograma = idPrograma;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.eventos = new HashMap<>();
    }

    // CRUD sobre la colección de eventos

    // Recibe un objeto "Evento" ya instanciado
    public boolean agregarEvento(Evento evento){
        if(evento == null || evento.getIdEvento() == null || eventos.containsKey(evento.getIdEvento())){
            return false;
        }
        eventos.put(evento.getIdEvento(), evento);
        return true;
    }

    // Recibe los datos sueltos y crea el Evento internamente, esto cumple con ser una sobrecarga.
    // El constructor de Evento valida sus datos y puede lanzar IllegalArgumentException;
    // la capturamos acá para mantener el contrato del método (SIA-12: manejo vía try-catch).
    public boolean agregarEvento(String idEvento, String nombre, String lugar, String fecha, int cupos, String prioridad){
        if(idEvento == null || eventos.containsKey(idEvento)){
            return false;
        }
        try{
            Evento nuevo = new Evento(idEvento, nombre, lugar, fecha, cupos, prioridad);
            return agregarEvento(nuevo);
        }catch(IllegalArgumentException e){
            System.err.println("No se pudo crear el evento \"" + idEvento + "\": " + e.getMessage());
            return false;
        }
    }

    // Elimina un evento del programa por id. Devuelve true si existía y fue removido.
    public boolean eliminarEvento(String idEvento){
        if(idEvento == null) return false;
        return eventos.remove(idEvento) != null;
    }

    // Obtiene un evento puntual por id, sin necesidad de copiar todo el mapa
    public Evento getEvento(String idEvento){
        if(idEvento == null) return null;
        return eventos.get(idEvento);
    }

    public Map<String, Evento> getEventos(){
        return new HashMap<>(eventos); // copia defensiva, para no exponer el mapa real
    }

    public int getCantidadEventos(){
        return eventos.size();
    }

    // Funcionalidad propia de negocio
    // Filtrado de eventos por criterio, distinto de CRUD/reportes

    // Devuelve el subconjunto de eventos que todavía tienen cupos disponibles
    public List<Evento> listarEventosConCuposDisponibles(){
        List<Evento> resultado = new ArrayList<>();
        for(Evento e : eventos.values()){
            if(e.getCuposDisponibles() > 0){
                resultado.add(e);
            }
        }
        return resultado;
    }

    // Devuelve el subconjunto de eventos que coinciden con la prioridad indicada
    public List<Evento> buscarEventosPorPrioridad(Prioridad prioridad){
        List<Evento> resultado = new ArrayList<>();
        if(prioridad == null) return resultado;
        for(Evento e : eventos.values()){
            if(e.getPrioridad() == prioridad){
                resultado.add(e);
            }
        }
        return resultado;
    }

    // Getters y Setters

    public String getIdPrograma(){
        return idPrograma;
    }

    public void setIdPrograma(String idPrograma){
        this.idPrograma = idPrograma;
    }

    public String getTitulo(){
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public String getFechaInicio(){
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio){
        this.fechaInicio = fechaInicio;
    }

    // equals / hashCode basados en idPrograma

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Programa programa = (Programa) o;
        return Objects.equals(idPrograma, programa.idPrograma);
    }

    @Override
    public int hashCode(){
        return Objects.hash(idPrograma);
    }

    @Override
    public String toString(){
        return "[" + idPrograma + "] " + titulo + " | Inicio: " + fechaInicio +
               " | Eventos: " + eventos.size();
    }
}
