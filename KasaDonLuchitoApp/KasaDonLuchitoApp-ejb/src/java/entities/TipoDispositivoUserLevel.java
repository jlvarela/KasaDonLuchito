/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author victor
 */
@Entity
public class TipoDispositivoUserLevel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private TipoDispositivo tipoDispositivo;
    
    private String unidad;
    
    @Column(nullable = false)
    private List<Integer> valoresPosibles;
    
    @Column(nullable = false)
    private boolean rangoValores;

    public TipoDispositivoUserLevel() {
        valoresPosibles = new ArrayList<Integer>();
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

    public TipoDispositivo getTipoDispositivo() {
        return tipoDispositivo;
    }

    public void setTipoDispositivo(TipoDispositivo tipoDispositivo) {
        this.tipoDispositivo = tipoDispositivo;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
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
        if (!(object instanceof TipoDispositivoUserLevel)) {
            return false;
        }
        TipoDispositivoUserLevel other = (TipoDispositivoUserLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TipoDispositivoUserLevel[ id=" + id + " ]";
    }
    
}
