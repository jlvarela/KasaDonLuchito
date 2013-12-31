/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoDispositivo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface TipoDispositivoFacadeLocal {

    void create(TipoDispositivo tipoDispositivo);

    void edit(TipoDispositivo tipoDispositivo);

    void remove(TipoDispositivo tipoDispositivo);

    TipoDispositivo find(Object id);

    List<TipoDispositivo> findAll();

    List<TipoDispositivo> findRange(int[] range);

    int count();
    
}
