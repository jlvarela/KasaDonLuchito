/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import entities.Dispositivo;
import entities.TipoDispositivo;
import entities.TipoDispositivoUserLevel;
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
    private TipoDispositivoUserLevelFacadeLocal tipoDispositivoUserLevelFacade;
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
    public List<Dispositivo> findOnlyActuatorsByUserNameLogged(String username) {
        Query q;
        if (usuarioFacade.isAdministrador(username)) {
            q = this.em.createNamedQuery("Dispositivo.findAllActuators");
        }
        else {
            q = this.em.createNamedQuery("Dispositivo.findOnlyActuatorsByUserNameLogged");
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
    public List<Dispositivo> findByUserNameLogged(String username) {
        Query q;
        if (usuarioFacade.isAdministrador(username)) {
            q = this.em.createNamedQuery("Dispositivo.findAll");
        }
        else {
            q = this.em.createNamedQuery("Dispositivo.findByUserNameLogged");
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
            Integer id_tipo_dispositivo_userLevel, Integer id_arduino, 
            List<Integer> lista_pines, List<Integer> lista_configs) throws Exception {
        Arduino a = arduinoFacade.find(id_arduino);
        TipoDispositivoUserLevel td = tipoDispositivoUserLevelFacade.find(id_tipo_dispositivo_userLevel);
        Dispositivo d = new Dispositivo();
        d.setNombre(nombre);
        d.setIdInterno(idInterno);
        d.setArduino(a);
        a.getDispositivos().add(d);
        d.setTipo(td);
        d.setConfiguraciones(lista_configs);
        d.setPines(lista_pines);
        d.setValorSW(0);
        
        create(d);
        conectionArduino.consultar(d);
    }
    
    @Override
    public List<Integer> getValoresDispositivoHW(Integer id) {
        Dispositivo disp;
        if (id == null)
            return new LinkedList<Integer>();
        disp = find(id);
        return disp.getValoresPosiblesHW();
    }
    
    @Override
    public List<Integer> getValoresDispositivoSW(Integer id) {
        Dispositivo disp;
        if (id == null)
            return new LinkedList<Integer>();
        disp = find(id);
        return disp.getValoresPosiblesSW();
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
        actualizarDB(disp, valor);
        conectionArduino.accionar(disp, disp.getValorHW());
        
    }
    
    private void actualizarDB(Dispositivo disp, int valor) {
        disp.setValorSW(valor);
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
