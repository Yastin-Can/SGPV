package sgpv.habilidad;

/** Representa una habilidad que puede poseer un voluntario. */
public class Habilidad {
    private String nombre;
    private String descripcion;

    public Habilidad() {
        this("", "");
    }

    public Habilidad(String nombre) {
        this(nombre, "");
    }

    public Habilidad(String nombre, String descripcion) {
        setNombre(nombre);
        setDescripcion(descripcion);
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre == null ? "" : nombre.trim();
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }
    
    @Override
    public String toString() {
        return descripcion.isEmpty() ? nombre : nombre + " (" + descripcion + ")";
    }
}
