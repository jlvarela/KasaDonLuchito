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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author victor
 */
@Entity
@NamedQueries( {
    @NamedQuery(name="Arduino.portInUse", query="SELECT u.puertoCOM FROM Arduino u WHERE u.puertoCOM = :port"),
})
public class Arduino implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String puertoCOM;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @OneToMany(mappedBy = "arduino", cascade = CascadeType.REMOVE)
    private List<Dispositivo> dispositivos;

    public Arduino() {
        dispositivos = new LinkedList<Dispositivo>();
    }

    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<Dispositivo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public String getPuertoCOM() {
        return puertoCOM;
    }

    public void setPuertoCOM(String puertoCOM) {
        this.puertoCOM = puertoCOM;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof Arduino)) {
            return false;
        }
        Arduino other = (Arduino) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Arduino[ id=" + id + " ]";
    }
    
}
