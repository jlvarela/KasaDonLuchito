/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Disparador;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class DisparadorFacade extends AbstractFacade<Disparador> implements DisparadorFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DisparadorFacade() {
        super(Disparador.class);
    }
    
    @Override
    public void eliminarDisparador(Disparador t) {
        //disparadorExecutor.eliminarDisparador(t);
        remove(t);
    }
    
}
