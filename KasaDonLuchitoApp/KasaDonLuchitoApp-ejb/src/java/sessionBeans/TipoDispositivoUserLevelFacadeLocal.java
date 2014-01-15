/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoDispositivoUserLevel;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface TipoDispositivoUserLevelFacadeLocal {

    void create(TipoDispositivoUserLevel tipoDispositivo);

    void edit(TipoDispositivoUserLevel tipoDispositivo);

    void remove(TipoDispositivoUserLevel tipoDispositivo);

    TipoDispositivoUserLevel find(Object id);

    List<TipoDispositivoUserLevel> findAll();

    List<TipoDispositivoUserLevel> findRange(int[] range);

    int count();
    
}
