/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author victor
 */
public class TimerPojo {
    Integer id;
    String nombre;
    String accionARealizar;
    String hora;
    String cuandoRepetir;
    boolean activado;
    boolean esCreador;

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

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCuandoRepetir() {
        return cuandoRepetir;
    }

    public void setCuandoRepetir(String cuandoRepetir) {
        this.cuandoRepetir = cuandoRepetir;
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
