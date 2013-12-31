/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class TipoUsuarioFacade extends AbstractFacade<TipoUsuario> implements TipoUsuarioFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoUsuarioFacade() {
        super(TipoUsuario.class);
    }
    
}
