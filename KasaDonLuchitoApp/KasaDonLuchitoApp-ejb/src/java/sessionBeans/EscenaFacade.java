/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.AccionEscena;
import entities.Dispositivo;
import entities.Escena;
import entities.Usuario;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public void crearEscena(String nombre, Map<Integer, Integer> accionesdispositivos, List<Integer> idUsuariosPermitidos) throws Exception {
        Escena escena = new Escena();
        escena.setNombre(nombre);
        
        Dispositivo dispTemp;
        AccionEscena accionTemp;
        Set<Integer> keys = accionesdispositivos.keySet();
        for(Integer idDisp : keys) {
            dispTemp = dispositivoFacade.find(idDisp);
            accionTemp = new AccionEscena();
            accionTemp.setDispositivo(dispTemp);
            accionTemp.setAccion(accionesdispositivos.get(idDisp));
            accionTemp.setEscena(escena);
            
            
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
        
        getEntityManager().persist(escena);
        
    }
}
