/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.AccionEscena;
import entities.Dispositivo;
import entities.Escena;
import entities.Usuario;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author victor
 */
@Stateless
public class EscenaFacade extends AbstractFacade<Escena> implements EscenaFacadeLocal {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EscenaFacade() {
        super(Escena.class);
    }
    
    @Override
    public void accionar(Integer id) throws Exception {
        //System.out.println("Accionando escena con id: "+id);
        if (id == null) {
            throw new Exception("No ha especificado la escena a accionar");
        }
        Escena escena = find(id);
        if (escena == null) {
            throw new Exception("No se ha encontrado la escena para accionar");
        }
        
        for (AccionEscena d : escena.getAccionesEscena()) {
            //System.out.println("Accionando dispositivo con id: "+d.getId()+" y acción: "+d.getAccion());
            dispositivoFacade.accion(d.getDispositivo().getId(), d.getAccion());
        }
    }
    
    @Override
    public List<Escena> findbyUserNameLogged(String username) {
        Query q;
        if (usuarioFacade.isAdministrador(username)) {
            q = this.em.createNamedQuery("Escena.findAll");
        }
        else {
            q = this.em.createNamedQuery("Escena.findbyUserNameLogged");
            q.setParameter("username", username);
        }
        try {
            List<Escena> res = (List<Escena>)q.getResultList();
            
            if (res == null) {
                //System.out.println("No era admin, res es null");
                return new LinkedList<Escena>();
            }
            //System.out.println("No era admin, res es de largo: "+res.size());
            return res;
        }
        catch (NoResultException nre) {
            return new LinkedList<Escena>();
        }
    }
    
    @Override
    public void crearEscena(String nombre, Map<Integer, Integer> accionesdispositivos, List<Integer> idUsuariosPermitidos, String usernameCreador) throws Exception {
        if (nombre == null) {
            throw new Exception("La escena debe tener un nombre");
        }
        else if (nombre.trim().equals("")) {
            throw new Exception("La escena debe tener un nombre");
        }
        else if (accionesdispositivos.isEmpty()) {
            throw new Exception("La escena no tiene acciones");
        }
        else if (usernameCreador == null) {
            throw new Exception("La escena debe ser creada por un usuario, no ha especificado uno");
        }
        Usuario user = usuarioFacade.findByUsername(usernameCreador);
        if (user == null) {
            throw new Exception("La escena debe ser creada por un usuario válido, no se ha encontrado el usuario");
        }
        
        Escena escena = new Escena();
        escena.setNombre(nombre);
        escena.setUsuarioCreador(user);
        getEntityManager().persist(escena);
        
        Dispositivo dispTemp;
        AccionEscena accionTemp;
        Set<Integer> keys = accionesdispositivos.keySet();
        for(Integer idDisp : keys) {
            dispTemp = dispositivoFacade.find(idDisp);
            accionTemp = new AccionEscena();
            accionTemp.setDispositivo(dispTemp);
            accionTemp.setAccion(accionesdispositivos.get(idDisp));
            accionTemp.setEscena(escena);
            
            getEntityManager().persist(accionTemp);
            escena.getAccionesEscena().add(accionTemp);
        }
        
        Usuario userTemp;
        for(Integer idUser : idUsuariosPermitidos) {
            userTemp = usuarioFacade.find(idUser);
            if (userTemp == null) {
                throw new Exception("No se ha encontrado usuario");
            }
            userTemp.getEscenasPermitidas().add(escena);
            getEntityManager().merge(userTemp);
            escena.getUsuariosPermitidos().add(userTemp);
        }
        
        getEntityManager().merge(escena);
        
    }
}
