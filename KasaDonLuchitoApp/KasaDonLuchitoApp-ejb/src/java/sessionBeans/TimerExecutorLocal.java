/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Timer;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface TimerExecutorLocal {

    public void agregarTimer(Timer t);

    public void modificarTimer(Timer t);

    public void eliminarTimer(Timer t);
    
}
