/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Dispositivo;
import entities.Usuario;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import pojos.DispositivoPojo;
import pojos.SelectElemPojo;
import pojos.UsuarioPojo;
import sessionBeans.UsuarioFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorUsuariosVerListadoMB")
@RequestScoped
public class MantenedorUsuariosVerListadoMB {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    private List<UsuarioPojo> lista;
    private List<UsuarioPojo> listaBusqueda;
    
    @PostConstruct
    public void init() {
        List<Usuario> listaTemp = usuarioFacade.findAll();
        UsuarioPojo userTemp;
        lista = new LinkedList<UsuarioPojo>();
        listaBusqueda = new LinkedList<UsuarioPojo>();
        for(Usuario t : listaTemp) {
            userTemp = new UsuarioPojo();
            userTemp.setId(t.getId());
            userTemp.setLabel(t.getUsername());
            userTemp.setRol(t.getTipoUsuario().getNombreTipo());
            lista.add(userTemp);
            listaBusqueda.add(userTemp);
        }
    }
    
    /**
     * Creates a new instance of MantenedorUsuariosVerListadoMB
     */
    public MantenedorUsuariosVerListadoMB() {
    }
    
    public List<UsuarioPojo> getLista() {
        return lista;
    }

    public void setLista(List<UsuarioPojo> lista) {
        this.lista = lista;
    }

    public List<UsuarioPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<UsuarioPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }
}
