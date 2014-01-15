/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Disparador;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import otros.CommonFunctions;
import pojos.DisparadorPojo;
import sessionBeans.DisparadorFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorDisparadoresVerListadoMB")
@RequestScoped
public class MantenedorDisparadoresVerListadoMB {
    @EJB
    private DisparadorFacadeLocal disparadorFacade;
    
    @Inject
    private MantenedorGenericoConversation mantDisparadorConv;

    private List<DisparadorPojo> lista;
    private List<DisparadorPojo> listaBusqueda;
    
    
    @PostConstruct
    public void init() {
        List<Disparador> listaTemp = disparadorFacade.findAll();
        //List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        DisparadorPojo elemPojo;
        lista = new LinkedList<DisparadorPojo>();
        listaBusqueda = new LinkedList<DisparadorPojo>();
        String usernameActual = CommonFunctions.getUsuarioLogueado();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for(Disparador t : listaTemp) {
            elemPojo = new DisparadorPojo();
            elemPojo.setId(t.getId());
            elemPojo.setNombre(t.getNombre());
            elemPojo.setActivado(t.isActivo());
            if (t.getDispositivoQueAcciona() != null) {
                elemPojo.setAccionARealizar("Poner dispositivo \""+t.getDispositivoQueAcciona().getNombre()+"\" en \""+t.getValorAccionDispositivo()+"\"");
            }
            if (t.getEscenaQueAcciona()!= null) {
                elemPojo.setAccionARealizar("Accionar escena \""+t.getEscenaQueAcciona().getNombre()+"\"");
            }
            
            StringBuilder strBuild = new StringBuilder();
            
            /*
            elemPojo.setEsCreador(false);
            
            //Si es el usuario creador, puede editarla y eliminarla
            if (usernameActual.equals(t.getUsuarioCreador().getUsername())) {
                elemPojo.setEsCreador(true);
            }
            */
            //O si es administrador, puede hacer lo que sea
            if (CommonFunctions.isUserInRole("Administrador")) {
                elemPojo.setEsCreador(true);
            }
            //elemPojo.setCantidadAcciones(t.getAccionesEscena().size());
            lista.add(elemPojo);
            listaBusqueda.add(elemPojo);
        }
    }
    
    public void detalles(Integer num) {
        Disparador toEdit = disparadorFacade.find(num);
        if (toEdit != null) {
            this.mantDisparadorConv.beginConversation();
            this.mantDisparadorConv.setState(MantenedorGenericoConversation.VER);
            this.mantDisparadorConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/detallesDisparador.xhtml?cid=".concat(this.mantDisparadorConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantDisparadorConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void editar(Integer num) {
        Disparador toEdit = disparadorFacade.find(num);
        if (toEdit != null) {
            this.mantDisparadorConv.beginConversation();
            this.mantDisparadorConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantDisparadorConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/admin/editarDisparador.xhtml?cid=".concat(this.mantDisparadorConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantDisparadorConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Disparador toEdit = disparadorFacade.find(num);
            if (toEdit != null) {
                disparadorFacade.eliminarDisparador(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado el disparador",
                        "Se ha eliminado correctamente el disparador");
            }
            else {
                //MOSTRAR ERROR
                this.mantDisparadorConv.limpiarDatos();
                CommonFunctions.goToPage("/faces/users/verDisparadores.xhtml?faces-redirect=true");
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantDisparadorConv.limpiarDatos();
        CommonFunctions.goToPage("/faces/users/verDisparadores.xhtml?faces-redirect=true");
    }
    
    
    /**
     * Creates a new instance of MantenedorEscenasVerListadoMB
     */
    public MantenedorDisparadoresVerListadoMB() {
    }

    public List<DisparadorPojo> getLista() {
        return lista;
    }

    public void setLista(List<DisparadorPojo> lista) {
        this.lista = lista;
    }

    public List<DisparadorPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<DisparadorPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }
    
}
