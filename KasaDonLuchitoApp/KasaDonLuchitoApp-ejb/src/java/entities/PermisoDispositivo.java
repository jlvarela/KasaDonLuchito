/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
public class PermisoDispositivo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private boolean visualiza;
    
    private boolean modifica;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Dispositivo dispositivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isVisualiza() {
        return visualiza;
    }

    public void setVisualiza(boolean visualiza) {
        this.visualiza = visualiza;
    }

    public boolean isModifica() {
        return modifica;
    }

    public void setModifica(boolean modifica) {
        this.modifica = modifica;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
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
        if (!(object instanceof PermisoDispositivo)) {
            return false;
        }
        PermisoDispositivo other = (PermisoDispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PermisoDispositivo[ id=" + id + " ]";
    }
    
}
