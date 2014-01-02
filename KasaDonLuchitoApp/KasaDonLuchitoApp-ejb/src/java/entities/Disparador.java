/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author victor
 */
@Entity
public class Disparador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    
    @Column(nullable = false)
    private boolean activo;
    
    @OneToMany(mappedBy = "disparador", cascade = CascadeType.REMOVE)
    private List<CondicionDisparador> condiciones;
    
    @ManyToOne
    private Dispositivo dispositivoQueAcciona;
    
    @ManyToOne
    private Escena escenaQueAcciona;

    public Disparador() {
        condiciones = new LinkedList<CondicionDisparador>();
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<CondicionDisparador> getCondiciones() {
        return condiciones;
    }

    public void setCondiciones(List<CondicionDisparador> condiciones) {
        this.condiciones = condiciones;
    }

    public Dispositivo getDispositivoQueAcciona() {
        return dispositivoQueAcciona;
    }

    public void setDispositivoQueAcciona(Dispositivo dispositivoQueAcciona) {
        this.dispositivoQueAcciona = dispositivoQueAcciona;
    }

    public Escena getEscenaQueAcciona() {
        return escenaQueAcciona;
    }

    public void setEscenaQueAcciona(Escena escenaQueAcciona) {
        this.escenaQueAcciona = escenaQueAcciona;
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
        if (!(object instanceof Disparador)) {
            return false;
        }
        Disparador other = (Disparador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Disparador[ id=" + id + " ]";
    }
    
}
