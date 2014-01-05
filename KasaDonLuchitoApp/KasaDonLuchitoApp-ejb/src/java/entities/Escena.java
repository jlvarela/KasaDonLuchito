/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author victor
 */
@Entity
@NamedQueries( {
    @NamedQuery(name="Escena.findAll", query="SELECT e FROM Escena e"),
    @NamedQuery(name="Escena.findbyUserNameLogged", query="SELECT e FROM Escena e JOIN e.usuariosPermitidos u WHERE u.username = :username")
})
public class Escena implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    
    
    @OneToMany(mappedBy = "escena", cascade = CascadeType.REMOVE)
    private List<AccionEscena> accionesEscena;
    
    @ManyToMany
    private List<Usuario> usuariosPermitidos;

    public Escena() {
        usuariosPermitidos = new LinkedList<Usuario>();
        accionesEscena = new LinkedList<AccionEscena>();
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

    public List<AccionEscena> getAccionesEscena() {
        return accionesEscena;
    }

    public void setAccionesEscena(List<AccionEscena> accionesEscena) {
        this.accionesEscena = accionesEscena;
    }

    public List<Usuario> getUsuariosPermitidos() {
        return usuariosPermitidos;
    }

    public void setUsuariosPermitidos(List<Usuario> usuariosPermitidos) {
        this.usuariosPermitidos = usuariosPermitidos;
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
        if (!(object instanceof Escena)) {
            return false;
        }
        Escena other = (Escena) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Escena[ id=" + id + " ]";
    }
    
}
