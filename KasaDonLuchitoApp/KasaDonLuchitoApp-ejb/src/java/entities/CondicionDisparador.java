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
import javax.persistence.Transient;

/**
 *
 * @author victor
 */
@Entity
public class CondicionDisparador implements Serializable {
    public static final String IGUAL_QUE = "=";
    public static final String MENOR_QUE = "<";
    public static final String MAYOR_QUE = ">";
    public static final String MENOR_O_IGUAL_QUE = "<=";
    public static final String MAYOR_O_IGUAL_QUE = ">=";
    public static final String DISTINTO_QUE = "!=";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private int valorOld;
    
    private int valorNew;
    
    private String comparador; //ENUMERACION
    
    private String comparadorOld;
    
    @Transient
    private boolean cumplida; //Indica si la condición actualmente se está cumpliendo o no, así evita comprobarla cada vez

    @ManyToOne
    @JoinColumn(nullable = false)
    private Disparador disparador;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Dispositivo dispositivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Disparador getDisparador() {
        return disparador;
    }

    public void setDisparador(Disparador disparador) {
        this.disparador = disparador;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }
    public int getValorOld() {
        return valorOld;
    }

    public boolean isCumplida() {
        return cumplida;
    }

    public void setCumplida(boolean cumplida) {
        this.cumplida = cumplida;
    }

    public void setValorOld(int valorOld) {
        this.valorOld = valorOld;
    }

    public int getValorNew() {
        return valorNew;
    }

    public void setValorNew(int valorNew) {
        this.valorNew = valorNew;
    }

    public String getComparador() {
        return comparador;
    }

    public void setComparador(String comparador) {
        this.comparador = comparador;
    }

    public String getComparadorOld() {
        return comparadorOld;
    }

    public void setComparadorOld(String comparadorOld) {
        this.comparadorOld = comparadorOld;
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
        if (!(object instanceof CondicionDisparador)) {
            return false;
        }
        CondicionDisparador other = (CondicionDisparador) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.CondicionesDisparador[ id=" + id + " ]";
    }
    
}
