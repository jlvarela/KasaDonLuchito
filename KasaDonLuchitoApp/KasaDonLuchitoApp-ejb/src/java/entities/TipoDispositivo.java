/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author victor
 */
@Entity
public class TipoDispositivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer id; //ENUMERACION
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false)
    private boolean actuador;
    
    @Column(nullable = false)
    private List<Integer> valoresPosibles;
    
    @Column(nullable = false)
    private boolean rangoValores;

    public TipoDispositivo() {
        valoresPosibles = new LinkedList<Integer>();
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

    public List<Integer> getValoresPosibles() {
        return valoresPosibles;
    }

    public void setValoresPosibles(List<Integer> valoresPosibles) {
        this.valoresPosibles = valoresPosibles;
    }

    public boolean isRangoValores() {
        return rangoValores;
    }

    public void setRangoValores(boolean rangoValores) {
        this.rangoValores = rangoValores;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDispositivo)) {
            return false;
        }
        TipoDispositivo other = (TipoDispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TipoDispositivo[ id=" + id + " ]";
    }
    
}
