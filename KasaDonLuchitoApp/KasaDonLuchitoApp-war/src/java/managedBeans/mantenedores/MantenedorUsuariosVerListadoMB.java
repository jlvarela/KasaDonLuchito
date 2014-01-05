/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Usuario;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import otros.CommonFunctions;
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
    
    @Inject
    private MantenedorGenericoConversation mantUsuariosConv;

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
    
    public void editar(Integer num) {
        Usuario toEdit = usuarioFacade.find(num);
        if (toEdit != null) {
            this.mantUsuariosConv.beginConversation();
            this.mantUsuariosConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantUsuariosConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/admin/editarUsuario.xhtml?cid=".concat(this.mantUsuariosConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantUsuariosConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Usuario toEdit = usuarioFacade.find(num);
            if (toEdit != null) {
                usuarioFacade.remove(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado el usuario",
                        "Se ha eliminado correctamente el usuario");
            }
            else {
                //MOSTRAR ERROR
                this.mantUsuariosConv.limpiarDatos();
                CommonFunctions.goToPage("/faces/users/admin/verUsuarios.xhtml?faces-redirect=true");
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantUsuariosConv.limpiarDatos();
        CommonFunctions.goToIndex();
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
