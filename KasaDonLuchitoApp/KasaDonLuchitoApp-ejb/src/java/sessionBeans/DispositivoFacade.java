/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import entities.Dispositivo;
import entities.TipoDispositivo;
import java.util.LinkedList;
import java.util.List;
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
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private TipoDispositivoFacadeLocal tipoDispositivoFacade;
    @EJB
    private ArduinoFacadeLocal arduinoFacade;
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
    public List<Dispositivo> findbyUserNameLogged(String username) {
        Query q;
        if (usuarioFacade.isAdministrador(username)) {
            q = this.em.createNamedQuery("Dispositivo.findAll");
        }
        else {
            q = this.em.createNamedQuery("Dispositivo.findbyUserNameLogged");
            q.setParameter("username", username);
        }
        try {
            List<Dispositivo> res = (List<Dispositivo>)q.getResultList();
            
            if (res == null) {
                //System.out.println("No era admin, res es null");
                return new LinkedList<Dispositivo>();
            }
            //System.out.println("No era admin, res es de largo: "+res.size());
            return res;
        }
        catch (NoResultException nre) {
            return new LinkedList<Dispositivo>();
        }
    }
    
    @Override
    public void agregarDispositivo(String nombre, Integer idInterno, 
            Integer id_tipo_dispositivo, Integer id_arduino, 
            List<Integer> lista_pines, List<Integer> lista_configs) throws Exception {
        Arduino a = arduinoFacade.find(id_arduino);
        TipoDispositivo td = tipoDispositivoFacade.find(id_tipo_dispositivo);
        Dispositivo d = new Dispositivo();
        d.setNombre(nombre);
        d.setIdInterno(idInterno);
        d.setArduino(a);
        a.getDispositivos().add(d);
        d.setTipo(td);
        d.setConfiguraciones(lista_configs);
        d.setPines(lista_pines);
        d.setValor(0);
        
        create(d);
        conectionArduino.consultar(d);
    }
    
    @Override
    public List<Integer> getValoresDispositivo(Integer id) {
        Dispositivo disp;
        if (id == null)
            return new LinkedList<Integer>();
        disp = find(id);
        return disp.getValoresPosibles();
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
    
    @Override
    public Dispositivo findByIdInterno(Integer idInterno) {
        Dispositivo disp;
        if (idInterno == null)
            return null;
        Query q = this.em.createNamedQuery("Dispositivo.findByIdInterno");
        q.setParameter("idInterno", idInterno);
        try {
            disp = (Dispositivo)q.getSingleResult();
        }
        catch (NoResultException nre) {
            return null;
        }
        return disp;
    }
}
