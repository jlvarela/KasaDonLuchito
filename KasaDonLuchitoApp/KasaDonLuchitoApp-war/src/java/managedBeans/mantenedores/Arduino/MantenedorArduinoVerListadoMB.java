/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores.Arduino;

import managedBeans.mantenedores.MantenedorGenericoConversation;
import entities.Arduino;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import otros.CommonFunctions;
import pojos.ArduinoPojo;
import sessionBeans.ArduinoFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorArduinoVerListadoMB")
@RequestScoped
public class MantenedorArduinoVerListadoMB {
    @EJB
    private ArduinoFacadeLocal arduinoFacade;
    
    @Inject
    private MantenedorGenericoConversation mantArduinoConv;
    
    private List<ArduinoPojo> lista;
    private List<ArduinoPojo> listaBusqueda;
    
    @PostConstruct
    public void init() {
        List<Arduino> listaTemp = arduinoFacade.findAll();
        ArduinoPojo pojoTemp;
        lista = new LinkedList<ArduinoPojo>();
        listaBusqueda = new LinkedList<ArduinoPojo>();
        for(Arduino t : listaTemp) {
            pojoTemp = new ArduinoPojo(t.getId(), t.getNombre(), t.getPuertoCOM(), t.getDispositivos().size());
            lista.add(pojoTemp);
            listaBusqueda.add(pojoTemp);
        }
    }
    
    public void editar(Integer num) {
        Arduino toEdit = arduinoFacade.find(num);
        if (toEdit != null) {
            this.mantArduinoConv.beginConversation();
            this.mantArduinoConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantArduinoConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/admin/editarArduino.xhtml?cid=".concat(this.mantArduinoConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantArduinoConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Arduino toEdit = arduinoFacade.find(num);
            if (toEdit != null) {
                arduinoFacade.remove(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado el Arduino",
                        "Se ha eliminado correctamente el Arduino");
            }
            else {
                //MOSTRAR ERROR
                this.mantArduinoConv.limpiarDatos();
                CommonFunctions.goToIndex();
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantArduinoConv.limpiarDatos();
        CommonFunctions.goToIndex();
    }
    
    /**
     * Creates a new instance of MantenedorArduinoVerListadoMB
     */
    public MantenedorArduinoVerListadoMB() {
    }

    public List<ArduinoPojo> getLista() {
        return lista;
    }

    public List<ArduinoPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<ArduinoPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }
    
}
