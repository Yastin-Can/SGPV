import java.util.ArrayList;

public class PersonaVoluntaria {
    private String nombre;
    private int id;
    private boolean  disponibilidad;

    private ArrayList<Skill> skills = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public boolean  getDisponibilidad() {
        return disponibilidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDisponibilidad(boolean  disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }
}
