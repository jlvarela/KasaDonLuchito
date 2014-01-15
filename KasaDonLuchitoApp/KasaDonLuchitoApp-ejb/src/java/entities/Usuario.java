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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author victor
 */
@Entity
@NamedQueries( {
    @NamedQuery(name="Usuario.isFromType", query="SELECT count(u) FROM Usuario u WHERE u.tipoUsuario.nombreTipo = :tipoUsuario AND u.username = :username"),
    @NamedQuery(name="Usuario.findOnlyType", query="SELECT u FROM Usuario u WHERE u.tipoUsuario.nombreTipo = :tipoUsuario"),
        @NamedQuery(name="Usuario.findByUsername", query="SELECT u FROM Usuario u WHERE u.username = :username")
})
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private TipoUsuario tipoUsuario;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
    private List<PermisoDispositivo> permisoDispositivos;
    
    @ManyToMany(mappedBy = "usuariosPermitidos")
    private List<Escena> escenasPermitidas;

    public Usuario() {
        permisoDispositivos = new LinkedList<PermisoDispositivo>();
        escenasPermitidas = new LinkedList<Escena>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PermisoDispositivo> getPermisoDispositivos() {
        return permisoDispositivos;
    }

    public void setPermisoDispositivos(List<PermisoDispositivo> permisoDispositivos) {
        this.permisoDispositivos = permisoDispositivos;
    }

    public List<Escena> getEscenasPermitidas() {
        return escenasPermitidas;
    }

    public void setEscenasPermitidas(List<Escena> escenasPermitidas) {
        this.escenasPermitidas = escenasPermitidas;
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
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Usuario[ id=" + id + " ]";
    }
    
}
