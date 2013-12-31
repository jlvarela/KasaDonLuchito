/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Timer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class TimerFacade extends AbstractFacade<Timer> implements TimerFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TimerFacade() {
        super(Timer.class);
    }
    
}
