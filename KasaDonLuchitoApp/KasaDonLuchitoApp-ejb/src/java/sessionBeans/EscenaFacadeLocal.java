/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Escena;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface EscenaFacadeLocal {

    void create(Escena escena);

    void edit(Escena escena);

    void remove(Escena escena);

    Escena find(Object id);

    List<Escena> findAll();

    List<Escena> findRange(int[] range);

    int count();

    public void crearEscena(String nombre, Map<Integer, Integer> accionesdispositivos, 
            List<Integer> idUsuariosPermitidos, String usernameCreador) throws Exception;

    public List<Escena> findbyUserNameLogged(String username);

    public void accionar(Integer id) throws Exception;
    
}
