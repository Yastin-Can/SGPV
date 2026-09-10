package sgpv.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sgpv.modelo.Evento;
import sgpv.excepciones.CupoLlenoException;
import sgpv.excepciones.VoluntarioNoDisponibleException;
import sgpv.modelo.Habilidad;
import sgpv.utils.StringUtils;
import sgpv.modelo.Voluntario;

/**
 * Clase q administra la colección principal de programas y eventos, con el registro global de voluntarios
 *
 */
public class GestorVoluntariado {

    private Map<String, Programa> programas;
    private Map<String, Voluntario> voluntariosRegistrados;

    public GestorVoluntariado() {
        this.programas = new HashMap<>();
        this.voluntariosRegistrados = new HashMap<>();
    }

    // CRUD: Programas 

    public boolean agregarPrograma(Programa programa) {
        if (programa == null || programa.getIdPrograma() == null
                || programas.containsKey(programa.getIdPrograma())) {
            return false;
        }
        programas.put(programa.getIdPrograma(), programa);
        return true;
    }

    public boolean agregarPrograma(String idPrograma, String titulo, String descripcion, String fechaInicio) {
        if (StringUtils.isBlank(idPrograma) || programas.containsKey(idPrograma)) {
            return false;
        }
        try {
            Programa nuevo = new Programa(idPrograma, titulo, descripcion, fechaInicio);
            return agregarPrograma(nuevo);
        } catch (IllegalArgumentException e) {
            System.err.println("No se pudo crear el programa \"" + idPrograma + "\": " + e.getMessage());
            return false;
        }
    }

    public boolean editarPrograma(String idPrograma, String nuevoTitulo, String nuevaDescripcion, String nuevaFechaInicio) {
        Programa p = programas.get(idPrograma);
        if (p == null) return false;
        if (!StringUtils.isBlank(nuevoTitulo)) p.setTitulo(nuevoTitulo);
        p.setDescripcion(nuevaDescripcion);
        p.setFechaInicio(nuevaFechaInicio);
        return true;
    }

    public boolean eliminarPrograma(String idPrograma) {
        if (idPrograma == null) return false;
        return programas.remove(idPrograma) != null;
    }

    public Programa buscarPrograma(String idPrograma) {
        if (idPrograma == null) return null;
        return programas.get(idPrograma);
    }

    public List<Programa> listarProgramas() {
        return new ArrayList<>(programas.values());
    }

    public int getCantidadProgramas() {
        return programas.size();
    }

    //  CRUD: Registro de voluntarios 

    public boolean registrarVoluntario(Voluntario voluntario) {
        if (voluntario == null || voluntario.getRut() == null
                || voluntariosRegistrados.containsKey(voluntario.getRut())) {
            return false;
        }
        voluntariosRegistrados.put(voluntario.getRut(), voluntario);
        return true;
    }

    public boolean registrarVoluntario(String nombre, String rut, String comuna, boolean disponible) {
        if (StringUtils.isBlank(rut) || voluntariosRegistrados.containsKey(rut)) {
            return false;
        }
        Voluntario nuevo = new Voluntario(nombre, rut, comuna, disponible);
        return registrarVoluntario(nuevo);
    }

    public boolean editarVoluntario(String rut, String nuevoNombre, String nuevaComuna, boolean disponible) {
        Voluntario v = voluntariosRegistrados.get(rut);
        if (v == null) return false;
        v.setNombre(nuevoNombre);
        v.setComuna(nuevaComuna);
        v.setDisponible(disponible);
        return true;
    }

    public boolean eliminarVoluntario(String rut) {
        if (rut == null) return false;
        return voluntariosRegistrados.remove(rut) != null;
    }

    public Voluntario buscarVoluntario(String rut) {
        if (rut == null) return null;
        return voluntariosRegistrados.get(rut);
    }

    public List<Voluntario> listarVoluntarios() {
        return new ArrayList<>(voluntariosRegistrados.values());
    }

    public int getCantidadVoluntarios() {
        return voluntariosRegistrados.size();
    }


    /**
     * Busca, entre los voluntarios registrados, los q están disponibles
     * y poseen la habilidad indicada.
     *
     */
    public List<Voluntario> listarVoluntariosDisponiblesPorHabilidad(String nombreHabilidad) {
        List<Voluntario> resultado = new ArrayList<>();
        if (StringUtils.isBlank(nombreHabilidad)) return resultado;

        for (Voluntario v : voluntariosRegistrados.values()) {
            if (v.isDisponible() && v.tieneHabilidad(nombreHabilidad)) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    /**
     * Ante un evento (catastrofe) toma del registro global el subconjunto de
     * voluntarios disponibles que posean la habilidad requerida y los asigna
     * automáticamente al evento hasta llenar sus cupos disponibles.
     *
     *
     * Devuelve la lista de voluntarios q fueron asignados
     */
    public List<Voluntario> asignarVoluntariosDisponiblesAEvento(String idPrograma, String idEvento,
            String nombreHabilidad) throws CupoLlenoException, VoluntarioNoDisponibleException {

        List<Voluntario> asignados = new ArrayList<>();

        Programa programa = buscarPrograma(idPrograma);
        if (programa == null) return asignados;

        Evento evento = programa.getEvento(idEvento);
        if (evento == null) return asignados;

        List<Voluntario> candidatos = listarVoluntariosDisponiblesPorHabilidad(nombreHabilidad);

        for (Voluntario candidato : candidatos) {
            if (evento.getCuposDisponibles() <= 0) {
                break;
            }
            evento.asignarVoluntario(candidato);
            asignados.add(candidato);
        }
        return asignados;
    }

    @Override
    public String toString() {
        return "GestorVoluntariado{programas=" + programas.size()
                + ", voluntariosRegistrados=" + voluntariosRegistrados.size() + '}';
    }
}
