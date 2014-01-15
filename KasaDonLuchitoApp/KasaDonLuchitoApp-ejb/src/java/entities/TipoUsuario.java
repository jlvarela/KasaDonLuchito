/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author victor
 */
@Entity
public class TipoUsuario implements Serializable {
    public static final String ADMINISTRADOR = "Administrador";
    public static final String USUARIO = "Usuario";
    @Id
    private String nombreTipo; //ENUMERACION

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreTipo != null ? nombreTipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoUsuario)) {
            return false;
        }
        TipoUsuario other = (TipoUsuario) object;
        if ((this.nombreTipo == null && other.nombreTipo != null) || (this.nombreTipo != null && !this.nombreTipo.equals(other.nombreTipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TipoUsuario[ id=" + nombreTipo + " ]";
    }
    
}
