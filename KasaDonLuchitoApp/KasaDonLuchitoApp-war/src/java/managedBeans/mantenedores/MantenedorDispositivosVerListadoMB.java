/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Dispositivo;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import managedBeans.ConfiguracionesMB;
import org.primefaces.event.SlideEndEvent;
import otros.CommonFunctions;
import pojos.DispositivoPojo;
import sessionBeans.DispositivoFacadeLocal;

/**
 *
 * @author victor
 */
@ManagedBean
@Named(value = "mantenedorDispositivosVerListadoMB")
@RequestScoped
public class MantenedorDispositivosVerListadoMB {
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    @Inject
    private MantenedorGenericoConversation mantDispositivoConv;
    
    @Inject
    private ConfiguracionesMB configMB;
    
    private List<DispositivoPojo> lista;
    private List<DispositivoPojo> listaBusqueda;
    
    @PostConstruct
    public void init() {
        List<Dispositivo> listaTemp = dispositivoFacade.findbyUserNameLogged(CommonFunctions.getUsuarioLogueado());
        //List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        DispositivoPojo dispPojoTemp;
        lista = new LinkedList<DispositivoPojo>();
        listaBusqueda = new LinkedList<DispositivoPojo>();
        for(Dispositivo t : listaTemp) {
            dispPojoTemp = new DispositivoPojo();
            dispPojoTemp.setId(t.getId());
            dispPojoTemp.setIdInterno(t.getIdInterno());
            dispPojoTemp.setNombre(t.getNombre());
            for(Integer valorPosible : t.getValoresPosibles()) {
                dispPojoTemp.getValoresPosibles().add(valorPosible);
            }
            dispPojoTemp.setActuador(t.isActuador());
            dispPojoTemp.setValor(t.getValor());
            lista.add(dispPojoTemp);
            listaBusqueda.add(dispPojoTemp);
        }
    }
    
    public void editar(Integer num) {
        Dispositivo toEdit = dispositivoFacade.find(num);
        if (toEdit != null) {
            this.mantDispositivoConv.beginConversation();
            this.mantDispositivoConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantDispositivoConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/admin/editarDispositivo.xhtml?cid=".concat(this.mantDispositivoConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantDispositivoConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Dispositivo toEdit = dispositivoFacade.find(num);
            if (toEdit != null) {
                dispositivoFacade.remove(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado el dispositivo",
                        "Se ha eliminado correctamente el dispositivo");
            }
            else {
                //MOSTRAR ERROR
                this.mantDispositivoConv.limpiarDatos();
                CommonFunctions.goToIndex();
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantDispositivoConv.limpiarDatos();
        CommonFunctions.goToIndex();
    }
    
    /**
     * Creates a new instance of MantenedorDispositivosVerListadoMB
     */
    public MantenedorDispositivosVerListadoMB() {
    }
    
    public void verificarValor(Integer id) {
        //Extrañamente funciona aunque haga nada, creo que es porque inicia el init
    }
    
    public int getIntervaloActualizacion() {
        int resInt = 3;
        String res = configMB.getConfiguracion("tiempo_actualizacion_sensores");
        if (res == null) 
            return resInt;
        else {
            try {
                resInt = Integer.parseInt(res);
            }
            catch (NumberFormatException nfe) {
                Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error de parseo de la config \"tiempo_actualizacion_sensores\", usando valor por default");
            }
            return resInt;
        }
    }
    
    public void cambioDispositivo(SlideEndEvent e) {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = 
        fc.getExternalContext().getRequestParameterMap();
        String data =  params.get("id");
        int id;
        try {
            id = Integer.parseInt(data);
        }
        catch (NumberFormatException nfe) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Error de parseo del id al hacer ajax");
            return;
        }
        int valor = e.getValue();
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Id dispositivo: {0} con el valor: {1}", new Object[]{data, valor});
        dispositivoFacade.accion(id, valor);
    }

    public List<DispositivoPojo> getLista() {
        return lista;
    }
    
    public List<DispositivoPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<DispositivoPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }

    public DispositivoFacadeLocal getDispositivoFacade() {
        return dispositivoFacade;
    }

    public void setDispositivoFacade(DispositivoFacadeLocal dispositivoFacade) {
        this.dispositivoFacade = dispositivoFacade;
    }
    
}
