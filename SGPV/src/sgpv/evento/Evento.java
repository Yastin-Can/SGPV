package sgpv.evento;

import java.util.ArrayList;
import java.util.List;
import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.modelo.Prioridad;
import sgpv.voluntario.Voluntario;

/** Representa una actividad perteneciente a un programa de voluntariado. */
public class Evento {
    private String idEvento;
    private String nombre;
    private String lugar;
    private String comuna;
    private String fecha;
    private int cupos;
    private Prioridad prioridad;
    private ArrayList<Voluntario> voluntariosAsignados;

    public Evento(String idEvento, String nombre, String lugar, String fecha,
            int cupos, String prioridad) {
        this(idEvento, nombre, lugar, "", fecha, cupos,
                Prioridad.valueOf(prioridad.trim().toUpperCase()));
    }

    public Evento(String idEvento, String nombre, String lugar, String fecha,
            int cupos, Prioridad prioridad) {
        this(idEvento, nombre, lugar, "", fecha, cupos, prioridad);
    }

    public Evento(String idEvento, String nombre, String lugar, String comuna,
            String fecha, int cupos, String prioridad) {
        this(idEvento, nombre, lugar, comuna, fecha, cupos,
                Prioridad.valueOf(prioridad.trim().toUpperCase()));
    }

    public Evento(String idEvento, String nombre, String lugar, String comuna,
            String fecha, int cupos, Prioridad prioridad) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.lugar = lugar;
        this.comuna = comuna;
        this.fecha = fecha;
        this.cupos = cupos;
        this.prioridad = prioridad;
        this.voluntariosAsignados = new ArrayList<>();
    }

    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }


    public String getLugar() {
        return lugar;
    }


    public int getCupos() {
        return cupos;
    }
    
    public String getFecha() {
        return fecha;
    }
    
    public String getComuna() {
        return comuna;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }
    
    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public ArrayList<Voluntario> getVoluntariosAsignados() {
        return voluntariosAsignados;
    }

    public void setVoluntariosAsignados(List<Voluntario> voluntariosAsignados) {
        this.voluntariosAsignados = voluntariosAsignados == null
                ? new ArrayList<>()
                : new ArrayList<>(voluntariosAsignados);
    }

    public void asignarVoluntario(Voluntario voluntario)
            throws CupoLlenoException, VoluntarioNoDisponibleException {
        if (voluntariosAsignados.contains(voluntario)) {
            return;
        }
        if (voluntariosAsignados.size() >= cupos) {
            throw new CupoLlenoException("No quedan cupos disponibles en el evento");
        }
        if (voluntario == null || !voluntario.isDisponible()) {
            throw new VoluntarioNoDisponibleException(
                    "El voluntario no se encuentra disponible");
        }
        voluntariosAsignados.add(voluntario);
        voluntario.setDisponible(false);
    }

    public void asignarVoluntario(List<Voluntario> voluntarios)
            throws CupoLlenoException, VoluntarioNoDisponibleException {
        if (voluntarios == null) {
            return;
        }
        for (Voluntario voluntario : voluntarios) {
            asignarVoluntario(voluntario);
        }
    }
    
    public Voluntario buscarVoluntario(String rut) {
        if (rut == null) return null;
        
        for(int i = 0; i < voluntariosAsignados.size(); i++) {
            Voluntario vol = (Voluntario) voluntariosAsignados.get(i);
            if(vol.getRut().equals(rut)) {
                return vol;
            }
        }
        
        return null;
    }
    
    
    public boolean modificarVoluntario(String rut, String nombre,
            String comuna, boolean disponible) {
        Voluntario vol = (Voluntario) buscarVoluntario(rut);
        if (vol == null) return false;

        vol.setNombre(nombre);
        vol.setComuna(comuna);
        vol.setDisponible(disponible);
        return true;
    }
    
    public boolean eliminarVoluntario(String rut) {
        Voluntario vol = (Voluntario) buscarVoluntario(rut);
        if (vol == null) return false;

        boolean eliminado = voluntariosAsignados.remove(vol);
        if (eliminado) {
            vol.setDisponible(true);
        }
        return eliminado;
    }
    
    public void mostrarVoluntarios() {
        if(voluntariosAsignados == null) return;
        if(voluntariosAsignados.size() == 0) return;
        
        for(int i = 0; i < voluntariosAsignados.size(); i++) {
            Voluntario vol = (Voluntario) voluntariosAsignados.get(i);
            System.out.println("V" + (i + 1) + " rut: " + vol.getRut() + ", nombre: " + vol.getNombre() + ", comuna: " + vol.getComuna());
        }
    }
    
    @Override
    public String toString() {
        return "Evento{" + "id='" + idEvento + '\''
                + ", nombre='" + nombre + '\''
                + ", lugar='" + lugar + '\''
                + ", comuna='" + comuna + '\''
                + ", fecha='" + fecha + '\''
                + ", cupos=" + cupos
                + ", prioridad=" + prioridad
                + ", voluntariosAsignados=" + voluntariosAsignados.size() + '}';
    }
}
