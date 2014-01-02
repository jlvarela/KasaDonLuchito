/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import ConexionSerial.ConectorSerial;
import entities.Arduino;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    @Override
    public List<String> getPuertosDisponibles() {
        ConectorSerial c = new ConectorSerial();
        List<String> listaRes = c.searchForPorts();
        //System.out.println("Cantidad de puertos encontrados: "+listaRes.size());
        return listaRes;
    }
    
    @Override
    public void agregarArduino(String nombre, String port) throws Exception {
        if (puertoCOMAsignado(port)) {
            throw new Exception("Puerto ".concat(port).concat(" ya está en uso por otro Arduino"));
        }
        Arduino ardu = new Arduino();
        ardu.setNombre(nombre);
        ardu.setPuertoCOM(port);
        
        create(ardu);
    }
    
    public boolean puertoCOMAsignado(String port) {
        Query q = this.em.createNamedQuery("Arduino.portInUse");
        q.setParameter("port", port);
        List<String> res = (List<String>)q.getResultList();
        if (res.size() > 0) {
            return true;
        }
        return false;
    }
}
