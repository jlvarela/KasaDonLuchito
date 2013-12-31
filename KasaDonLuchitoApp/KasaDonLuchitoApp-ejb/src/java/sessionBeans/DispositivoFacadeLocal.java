/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Dispositivo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface DispositivoFacadeLocal {

    void create(Dispositivo dispositivo);

    void edit(Dispositivo dispositivo);

    void remove(Dispositivo dispositivo);

    Dispositivo find(Object id);

    List<Dispositivo> findAll();

    List<Dispositivo> findRange(int[] range);

    int count();
    
}
