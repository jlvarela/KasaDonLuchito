/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author victor
 */
@Entity
public class Timer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false)
    private boolean activo;
    
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date hora;
    
    @Column(nullable = false)
    private boolean repetirLunes;
    @Column(nullable = false)
    private boolean repetirMartes;
    @Column(nullable = false)
    private boolean repetirMiercoles;
    @Column(nullable = false)
    private boolean repetirJueves;
    @Column(nullable = false)
    private boolean repetirViernes;
    @Column(nullable = false)
    private boolean repetirSabado;
    @Column(nullable = false)
    private boolean repetirDomingo;
    
    @ManyToOne
    private Dispositivo dispositivoQueAcciona;
    
    @ManyToOne
    private Escena escenaQueAcciona;
    
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

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public boolean isRepetirLunes() {
        return repetirLunes;
    }

    public void setRepetirLunes(boolean repetirLunes) {
        this.repetirLunes = repetirLunes;
    }

    public boolean isRepetirMartes() {
        return repetirMartes;
    }

    public void setRepetirMartes(boolean repetirMartes) {
        this.repetirMartes = repetirMartes;
    }

    public boolean isRepetirMiercoles() {
        return repetirMiercoles;
    }

    public void setRepetirMiercoles(boolean repetirMiercoles) {
        this.repetirMiercoles = repetirMiercoles;
    }

    public boolean isRepetirJueves() {
        return repetirJueves;
    }

    public void setRepetirJueves(boolean repetirJueves) {
        this.repetirJueves = repetirJueves;
    }

    public boolean isRepetirViernes() {
        return repetirViernes;
    }

    public void setRepetirViernes(boolean repetirViernes) {
        this.repetirViernes = repetirViernes;
    }

    public boolean isRepetirSabado() {
        return repetirSabado;
    }

    public void setRepetirSabado(boolean repetirSabado) {
        this.repetirSabado = repetirSabado;
    }

    public boolean isRepetirDomingo() {
        return repetirDomingo;
    }

    public void setRepetirDomingo(boolean repetirDomingo) {
        this.repetirDomingo = repetirDomingo;
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
        if (!(object instanceof Timer)) {
            return false;
        }
        Timer other = (Timer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Timers[ id=" + id + " ]";
    }
    
}
