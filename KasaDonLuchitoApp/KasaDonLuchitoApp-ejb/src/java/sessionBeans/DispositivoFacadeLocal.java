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

    public void accion(Integer id, int valor);

    public void agregarDispositivo(String nombre, Integer idInterno, Integer id_tipo_dispositivo, Integer id_arduino, List<Integer> lista_pines, List<Integer> lista_configs) throws Exception;

    public Dispositivo findByIdInterno(Integer idInterno);

    public List<Dispositivo> findOnlyActuatorsByUserNameLogged(String username);

    public List<Dispositivo> findByUserNameLogged(String username);

    public List<Integer> getValoresDispositivoSW(Integer id);

    public List<Integer> getValoresDispositivoHW(Integer id);
    
}
