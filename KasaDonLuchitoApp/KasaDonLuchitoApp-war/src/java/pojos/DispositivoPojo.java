/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author victor
 */
public class DispositivoPojo {
    private Integer id;
    
    private Integer idInterno;
    
    private String nombre;
    
    private List<Integer> valoresPosibles;
    
    private boolean actuador;
    
    private int valor;

    public DispositivoPojo() {
        valoresPosibles = new LinkedList<Integer>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdInterno() {
        return idInterno;
    }

    public void setIdInterno(Integer idInterno) {
        this.idInterno = idInterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Integer> getValoresPosibles() {
        return valoresPosibles;
    }

    public void setValoresPosibles(List<Integer> valoresPosibles) {
        this.valoresPosibles = valoresPosibles;
    }

    public boolean isActuador() {
        return actuador;
    }

    public void setActuador(boolean actuador) {
        this.actuador = actuador;
    }
    
    public boolean isSensor() {
        return !actuador;
    }
    
    public void setSensor(boolean sensor) {
        this.actuador = !sensor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
    
    public Integer getMinimoValorPosible() {
        Integer res = null;
        for(Integer i : valoresPosibles) {
            if (res == null) {
                res = i;
            }
            else if (i < res){
                res = i;
            }
        }
        if (res == null) {
                res = -1;
            }
        return res;
    }
    
    public Integer getMaximoValorPosible() {
        Integer res = null;
        for(Integer i : valoresPosibles) {
            if (res == null) {
                res = i;
            }
            else if (i > res){
                res = i;
            }
        }
        if (res == null) {
            res = -1;
        }
        return res;
    }
    
}
