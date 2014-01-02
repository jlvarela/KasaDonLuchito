/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Configuracion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface ConfiguracionFacadeLocal {

    void create(Configuracion configuracion);

    void edit(Configuracion configuracion);

    void remove(Configuracion configuracion);

    Configuracion find(Object id);

    List<Configuracion> findAll();

    List<Configuracion> findRange(int[] range);

    int count();

    public String findByName(String nombre);
    
}
