package sgpv;
import java.util.ArrayList;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author justin
 */
public class Voluntario {
    
    private String nombre;
    private String rut;
    private boolean disponible;
    
    private ArrayList<Habilidad> habilidades = new ArrayList<>();
    
    public String getNombre() {
        return nombre;
    }
    
    public String getRut() {
        return rut;
    }
    
    public boolean getDisp() {
        return disponible;
    }
    
    public ArrayList<Habilidad> getHabilidades() {
        return habilidades;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setRut(String rut) {
        this.rut = rut;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public void setHabilidades(ArrayList<Habilidad> habilidades) {
        this.habilidades = habilidades;
    }
}
