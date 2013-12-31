/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class ArduinoFacade extends AbstractFacade<Arduino> implements ArduinoFacadeLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ArduinoFacade() {
        super(Arduino.class);
    }
    
}
