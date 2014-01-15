/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author victor
 */
public class DisparadorPojo {
    Integer id;
    String nombre;
    String accionARealizar;
    boolean activado;
    boolean esCreador;
    int cantCondiciones;

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

    public String getAccionARealizar() {
        return accionARealizar;
    }

    public void setAccionARealizar(String accionARealizar) {
        this.accionARealizar = accionARealizar;
    }

    public int getCantCondiciones() {
        return cantCondiciones;
    }

    public void setCantCondiciones(int cantCondiciones) {
        this.cantCondiciones = cantCondiciones;
    }

    public boolean isActivado() {
        return activado;
    }

    public void setActivado(boolean activado) {
        this.activado = activado;
    }

    public boolean isEsCreador() {
        return esCreador;
    }

    public void setEsCreador(boolean esCreador) {
        this.esCreador = esCreador;
    }
    
}
