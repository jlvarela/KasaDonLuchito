/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Timer;
import java.util.Date;
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

    public void crearTimer(String nombre, Date hora, List<String> dias, boolean accionaEscena, Integer idDispSeleccionado, Integer valorAccionDispositivo, Integer idEscenaSeleccionada) throws Exception;
    
}
