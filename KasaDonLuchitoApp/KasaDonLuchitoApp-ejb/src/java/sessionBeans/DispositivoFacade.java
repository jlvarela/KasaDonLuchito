/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Dispositivo;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sessionBeans.SerialInterface.ConectionArduinoLocal;

/**
 *
 * @author victor
 */
@Stateless
public class DispositivoFacade extends AbstractFacade<Dispositivo> implements DispositivoFacadeLocal {
    @EJB
    private ConectionArduinoLocal conectionArduino;
    
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DispositivoFacade() {
        super(Dispositivo.class);
    }
    
    @Override
    public void accion(Integer id, int valor) {
        Dispositivo disp;
        if (id == null)
            return;
        Query q = this.em.createNamedQuery("Dispositivo.findById");
        q.setParameter("id", id);
        try {
            disp = (Dispositivo)q.getSingleResult();
        }
        catch (NoResultException nre) {
            return;
        }
        conectionArduino.accionar(disp, valor);
        actualizarDB(disp, valor);
    }
    
    private void actualizarDB(Dispositivo disp, int valor) {
        disp.setValor(valor);
        edit(disp);
    }
    
}
