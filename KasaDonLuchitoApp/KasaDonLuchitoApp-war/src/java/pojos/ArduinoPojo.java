/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author victor
 */
public class ArduinoPojo {
    Integer id;
    String nombre;
    String puertoCOM;
    int cantidadDispositivos;

    public ArduinoPojo(Integer id, String nombre, String puertoCOM, int cantidadDispositivos) {
        this.id = id;
        this.nombre = nombre;
        this.puertoCOM = puertoCOM;
        this.cantidadDispositivos = cantidadDispositivos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuertoCOM() {
        return puertoCOM;
    }

    public void setPuertoCOM(String puertoCOM) {
        this.puertoCOM = puertoCOM;
    }

    public int getCantidadDispositivos() {
        return cantidadDispositivos;
    }

    public void setCantidadDispositivos(int cantidadDispositivos) {
        this.cantidadDispositivos = cantidadDispositivos;
    }
    
}
