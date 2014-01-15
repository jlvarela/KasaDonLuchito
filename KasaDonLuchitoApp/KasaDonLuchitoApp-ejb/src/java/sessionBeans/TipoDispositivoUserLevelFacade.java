/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoDispositivoUserLevel;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class TipoDispositivoUserLevelFacade extends AbstractFacade<TipoDispositivoUserLevel> implements TipoDispositivoUserLevelFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDispositivoUserLevelFacade() {
        super(TipoDispositivoUserLevel.class);
    }
    
}
