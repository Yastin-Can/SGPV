package sgpv.modelo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import sgpv.modelo.Habilidad;


/** Representa a una persona voluntaria y sus habilidades. */
public class Voluntario {
    
    private String rut;
    private String nombre;
    private String comuna;
    private boolean disponible;
    private ArrayList<Habilidad> habilidades = new ArrayList<>();

    public Voluntario(String nombre, String rut, String comuna, boolean disponible) {
        this.nombre = nombre;
        setRut(rut);
        this.comuna = comuna;
        this.disponible = disponible;
    }

    public Voluntario(String nombre, String rut, boolean disponible, List<Habilidad> habilidades) {
        this(nombre, rut, "", disponible, habilidades);
    }

    public Voluntario(String nombre, String rut, String comuna, boolean disponible, List<Habilidad> habilidades) {
        this(nombre, rut, comuna, disponible);
        if (habilidades != null) {
            this.habilidades.addAll(habilidades);
        }
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getRut() {
        return rut;
    }
    
    public boolean getDisp() {
        return disponible;
    }
    
    public String getComuna() {
        return comuna;
    }

    public boolean isDisponible() {
        return disponible;
    }

    /** Nombre alternativo requerido por las clases que consumen este modelo. */
    public boolean isDisponibilidad() {
        return disponible;
    }
    
    public ArrayList<Habilidad> getHabilidades() {
        return habilidades;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setRut(String rut) {
        if (!tieneRutValido(rut)) {
            throw new IllegalArgumentException("El rut debe tener el formato 11111111-1");
        }

        this.rut = rut;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public void setHabilidades(ArrayList<Habilidad> habilidades) {
        this.habilidades = habilidades == null
                ? new ArrayList<>()
                : new ArrayList<>(habilidades);
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
    
    public boolean agregarHabilidad(Habilidad habilidad) {
        if (habilidad == null || habilidad.getNombre() == null
                || habilidad.getNombre().trim().isEmpty()
                || tieneHabilidad(habilidad.getNombre())) {
            return false;
        }
        return habilidades.add(habilidad);
    }
    

    public boolean agregarHabilidad(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        return agregarHabilidad(new Habilidad(nombre.trim()));
    }

    public boolean tieneHabilidad(String nombre) {
        if (nombre == null) {
            return false;
        }
        for (Habilidad habilidad : habilidades) {
            if (habilidad.getNombre() != null
                    && habilidad.getNombre().equalsIgnoreCase(nombre.trim())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean eliminarHabilidad(Habilidad habilidad) {
        if (habilidad == null) return false;
        
        return eliminarHabilidad(habilidad.getNombre());
    }
    
    public boolean eliminarHabilidad(String nombreHabilidad) {
        if (nombreHabilidad == null) return false;

        String nombreBuscado = nombreHabilidad.trim();
        
        for(int i = 0; i < habilidades.size(); i++) {
            Habilidad habilidadActual = (Habilidad) habilidades.get(i);
            if(habilidadActual.getNombre() != null) {
                if(habilidadActual.getNombre().equalsIgnoreCase(nombreBuscado)) {
                    habilidades.remove(i);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean modificarHabilidad(String nombreActual, String nombreNuevo, String nuevaDescripcion) {
        if (nombreActual == null || nombreNuevo == null
                || nombreNuevo.trim().isEmpty()) {
            return false;
        }

        for (int i = 0; i < habilidades.size(); i++) {
            Habilidad habilidadActual = (Habilidad) habilidades.get(i);
            if (habilidadActual.getNombre().equalsIgnoreCase(nombreActual.trim())) {
                if (!nombreActual.equalsIgnoreCase(nombreNuevo.trim())
                        && tieneHabilidad(nombreNuevo)) {
                    return false;
                }
                habilidadActual.setNombre(nombreNuevo);
                habilidadActual.setDescripcion(nuevaDescripcion);
                return true;
            }
        }
        return false;
    }
    
    public void mostrarHabilidades() {
        if (habilidades.isEmpty()) {
            System.out.println("Sin habilidades registradas");
            return;
        }
        for (int i = 0; i < habilidades.size(); i++) {
            Habilidad habilidad = (Habilidad) habilidades.get(i);
            System.out.println("H" + (i + 1) + ": " + habilidad);
        }
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Voluntario)) {
            return false;
        }

        Voluntario otroVoluntario = (Voluntario) objeto;
        String rutActual = normalizarRut(rut);
        String rutComparado = normalizarRut(otroVoluntario.rut);
        return !rutActual.isEmpty() && rutActual.equals(rutComparado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizarRut(rut));
    }

    private static String normalizarRut(String rut) {
        if (rut == null) {
            return "";
        }
        return rut.replace(".", "")
                .replace("-", "")
                .trim()
                .toUpperCase();
    }
    
    public static boolean tieneRutValido(String rut) {
        return rut != null && rut.matches("[0-9]{7,8}-[0-9K]");
    }

    @Override
    public String toString() {
        return "Voluntario{" + "nombre='" + nombre + '\''
                + ", rut='" + rut + '\''
                + ", comuna='" + comuna + '\''
                + ", disponible=" + disponible
                + ", habilidades=" + habilidades + '}';
    }
}
