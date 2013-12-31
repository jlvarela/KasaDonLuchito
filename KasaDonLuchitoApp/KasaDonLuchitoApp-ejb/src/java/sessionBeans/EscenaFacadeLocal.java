/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Escena;
import java.util.List;
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
    
}
