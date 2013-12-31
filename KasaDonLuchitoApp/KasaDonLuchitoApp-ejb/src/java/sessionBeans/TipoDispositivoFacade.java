/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoDispositivo;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class TipoDispositivoFacade extends AbstractFacade<TipoDispositivo> implements TipoDispositivoFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoDispositivoFacade() {
        super(TipoDispositivo.class);
    }
    
}
