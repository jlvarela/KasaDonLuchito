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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 *
 * @author victor
 */
@Entity
@NamedQueries( {
    @NamedQuery(name="Dispositivo.findById", query="SELECT u FROM Dispositivo u WHERE u.id = :id"),
    @NamedQuery(name="Dispositivo.findByIdInterno", query="SELECT u FROM Dispositivo u WHERE u.idInterno = :idInterno")
})
public class Dispositivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private Integer idInterno;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Arduino arduino;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private TipoDispositivo tipo;
    
    private int valor;
    
    @Transient
    private boolean configurado;
    
    private List<Integer> pines;
    
    private List<Integer> configuraciones;

    public Dispositivo() {
        pines = new LinkedList<Integer>();
        configuraciones = new LinkedList<Integer>();
        configurado = false;
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

    public Arduino getArduino() {
        return arduino;
    }

    public void setArduino(Arduino arduino) {
        this.arduino = arduino;
    }

    public TipoDispositivo getTipo() {
        return tipo;
    }

    public void setTipo(TipoDispositivo tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public List<Integer> getPines() {
        return pines;
    }

    public void setPines(List<Integer> pines) {
        this.pines = pines;
    }

    public List<Integer> getConfiguraciones() {
        return configuraciones;
    }

    public void setConfiguraciones(List<Integer> configuraciones) {
        this.configuraciones = configuraciones;
    }
    
    public boolean isActuador() {
        return this.tipo.isActuador();
    }
    
    public List<Integer> getValoresPosibles() {
        if (this.tipo.isRangoValores()) {
            LinkedList<Integer> res = new LinkedList<Integer>();
            Integer primero, ultimo;
            if (this.tipo.getValoresPosibles().isEmpty()) {
                return res;
            }
            primero = this.tipo.getValoresPosibles().get(0);
            ultimo = this.tipo.getValoresPosibles().get(this.tipo.getValoresPosibles().size()-1);
            for(int i = primero; i < ultimo; i++) {
                res.add(new Integer(i));
            }
            return res;
        }
        else {
            return this.tipo.getValoresPosibles();
        }
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
        if (!(object instanceof Dispositivo)) {
            return false;
        }
        Dispositivo other = (Dispositivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Dispositivo[ id=" + id + " ]";
    }

    public boolean isConfigurado() {
        return configurado;
    }

    public void setConfigurado(boolean configurado) {
        this.configurado = configurado;
    }
    
}
