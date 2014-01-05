/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Escena;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import otros.CommonFunctions;
import pojos.EscenaPojo;
import sessionBeans.EscenaFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorEscenasVerListadoMB")
@RequestScoped
public class MantenedorEscenasVerListadoMB {
    @EJB
    private EscenaFacadeLocal escenaFacade;
    
    @Inject
    private MantenedorGenericoConversation mantEscenaConv;

    private List<EscenaPojo> lista;
    private List<EscenaPojo> listaBusqueda;
    
    
    @PostConstruct
    public void init() {
        List<Escena> listaTemp = escenaFacade.findbyUserNameLogged(CommonFunctions.getUsuarioLogueado());
        //List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        EscenaPojo elemPojo;
        lista = new LinkedList<EscenaPojo>();
        listaBusqueda = new LinkedList<EscenaPojo>();
        for(Escena t : listaTemp) {
            elemPojo = new EscenaPojo();
            elemPojo.setId(t.getId());
            elemPojo.setNombre(t.getNombre());
            elemPojo.setCantidadAcciones(t.getAccionesEscena().size());
            lista.add(elemPojo);
            listaBusqueda.add(elemPojo);
        }
    }
    
    public void accionar(Integer idEscena) {
        try {
            escenaFacade.accionar(idEscena);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha accionado la escena",
                    "Se ha accionado correctamente la escena");
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
    }
    
    public void editar(Integer num) {
        Escena toEdit = escenaFacade.find(num);
        if (toEdit != null) {
            this.mantEscenaConv.beginConversation();
            this.mantEscenaConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantEscenaConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/editarEscena.xhtml?cid=".concat(this.mantEscenaConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantEscenaConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Escena toEdit = escenaFacade.find(num);
            if (toEdit != null) {
                escenaFacade.remove(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado la escena",
                        "Se ha eliminado correctamente la escena");
            }
            else {
                //MOSTRAR ERROR
                this.mantEscenaConv.limpiarDatos();
                CommonFunctions.goToPage("/faces/users/verEscenas.xhtml?faces-redirect=true");
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantEscenaConv.limpiarDatos();
        CommonFunctions.goToPage("/faces/users/verEscenas.xhtml?faces-redirect=true");
    }
    
    
    /**
     * Creates a new instance of MantenedorEscenasVerListadoMB
     */
    public MantenedorEscenasVerListadoMB() {
    }

    public List<EscenaPojo> getLista() {
        return lista;
    }

    public void setLista(List<EscenaPojo> lista) {
        this.lista = lista;
    }

    public List<EscenaPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<EscenaPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }
    
}
