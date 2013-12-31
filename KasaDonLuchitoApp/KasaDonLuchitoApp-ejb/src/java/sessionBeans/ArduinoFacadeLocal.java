/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface ArduinoFacadeLocal {

    void create(Arduino arduino);

    void edit(Arduino arduino);

    void remove(Arduino arduino);

    Arduino find(Object id);

    List<Arduino> findAll();

    List<Arduino> findRange(int[] range);

    int count();
    
}
