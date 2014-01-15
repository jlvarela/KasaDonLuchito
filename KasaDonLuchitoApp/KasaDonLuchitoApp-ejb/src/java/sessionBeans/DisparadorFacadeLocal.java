/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Disparador;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface DisparadorFacadeLocal {

    void create(Disparador disparador);

    void edit(Disparador disparador);

    void remove(Disparador disparador);

    Disparador find(Object id);

    List<Disparador> findAll();

    List<Disparador> findRange(int[] range);

    int count();

    public void eliminarDisparador(Disparador t);
    
}
