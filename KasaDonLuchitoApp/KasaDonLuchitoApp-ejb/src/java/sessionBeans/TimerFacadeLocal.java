/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Timer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface TimerFacadeLocal {

    void create(Timer timer);

    void edit(Timer timer);

    void remove(Timer timer);

    Timer find(Object id);

    List<Timer> findAll();

    List<Timer> findRange(int[] range);

    int count();
    
}
