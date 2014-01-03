/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Dispositivo;
import entities.Escena;
import entities.PermisoDispositivo;
import entities.TipoUsuario;
import entities.Usuario;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioFacade;
    
    
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    @Override
    public void crearUsuario(String username, List<Integer> idDispositivosPermitidos, List<Integer> idEscenasPermitidas, String nombreRol) throws Exception {
        Usuario user = new Usuario();
        TipoUsuario tu = tipoUsuarioFacade.find(nombreRol);
        if (tu == null) {
            throw new Exception("No existe el tipo de usuario especificado, haga un poblado inicial");
        }
        user.setUsername(username);
        user.setTipoUsuario(tu);
        user.setPassword(getMd5(username));
        
        
        PermisoDispositivo permisoTemp;
        Dispositivo dispTemp;
        for(Integer idDisp : idDispositivosPermitidos) {
            dispTemp = dispositivoFacade.find(idDisp);
            if (dispTemp == null) {
                throw new Exception("No se ha encontrado dispositivo");
            }
            permisoTemp = new PermisoDispositivo();
            permisoTemp.setModifica(true);
            permisoTemp.setVisualiza(true);
            permisoTemp.setUsuario(user);
            permisoTemp.setDispositivo(dispTemp);
            getEntityManager().persist(permisoTemp);
            user.getPermisoDispositivos().add(permisoTemp);
        }
        
        Escena escenaTemp;
        for(Integer idDisp : idEscenasPermitidas) {
            escenaTemp = escenaFacade.find(idDisp);
            if (escenaTemp == null) {
                throw new Exception("No se ha encontrado escena");
            }
            escenaTemp.getUsuariosPermitidos().add(user);
            getEntityManager().merge(escenaTemp);
            user.getEscenasPermitidas().add(escenaTemp);
        }
        
        getEntityManager().persist(user);
        
    }
    
    public String getMd5(String password) {
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("UTF-8"));

            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            password = bigInt.toString(16);
        }
        catch (Exception e) {
            //System.out.println("No se pudo convertir a MD5 la password");
        }
        return password;
    }
    
}
