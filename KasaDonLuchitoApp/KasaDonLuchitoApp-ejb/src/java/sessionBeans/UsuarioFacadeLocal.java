/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Usuario;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface UsuarioFacadeLocal {

    void create(Usuario usuario);

    void edit(Usuario usuario);

    void remove(Usuario usuario);

    Usuario find(Object id);

    List<Usuario> findAll();

    List<Usuario> findRange(int[] range);

    int count();

    public void crearUsuario(String username, List<Integer> idDispositivosPermitidos, List<Integer> idEscenasPermitidas, String nombreRol) throws Exception;

    public List<Usuario> findOnlyUsers();

    public boolean isAdministrador(String username);

    public Usuario findByUsername(String username);

    public boolean changeUsernameAndPassword(String oldUsername, String username, String password, String passwordNew) throws Exception;
    
}
