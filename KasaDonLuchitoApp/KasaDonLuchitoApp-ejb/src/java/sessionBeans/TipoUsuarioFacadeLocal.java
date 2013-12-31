/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.TipoUsuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface TipoUsuarioFacadeLocal {

    void create(TipoUsuario tipoUsuario);

    void edit(TipoUsuario tipoUsuario);

    void remove(TipoUsuario tipoUsuario);

    TipoUsuario find(Object id);

    List<TipoUsuario> findAll();

    List<TipoUsuario> findRange(int[] range);

    int count();
    
}
