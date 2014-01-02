/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Configuracion;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author victor
 */
@Stateless
public class ConfiguracionFacade extends AbstractFacade<Configuracion> implements ConfiguracionFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;
    
    @Override
    public String findByName(String nombre) {
        String res;
        if (nombre == null)
            return null;
        Query q = this.em.createNamedQuery("Configuracion.findByNombre");
        q.setParameter("nombre", nombre);
        try {
            res = (String)q.getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
        return res;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ConfiguracionFacade() {
        super(Configuracion.class);
    }
    
}
