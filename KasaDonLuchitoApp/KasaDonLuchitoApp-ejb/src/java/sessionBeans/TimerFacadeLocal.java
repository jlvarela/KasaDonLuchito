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
    
    public static final int REPETIR_LUNES = 1;
    public static final int REPETIR_MARTES = 2;
    public static final int REPETIR_MIERCOLES = 3;
    public static final int REPETIR_JUEVES = 4;
    public static final int REPETIR_VIERNES = 5;
    public static final int REPETIR_SABADO = 6;
    public static final int REPETIR_DOMINGO = 7;

    void create(Timer timer);

    void edit(Timer timer);

    void remove(Timer timer);

    Timer find(Object id);

    List<Timer> findAll();

    List<Timer> findRange(int[] range);

    int count();

    public void crearTimer(String nombre, Date hora, List<Integer> dias,
            boolean accionaEscena, Integer idDispSeleccionado, Integer valorAccionDispositivo, 
            Integer idEscenaSeleccionada, String usernameCreador) throws Exception;

    public void eliminarTimer(Timer t);
    
}
