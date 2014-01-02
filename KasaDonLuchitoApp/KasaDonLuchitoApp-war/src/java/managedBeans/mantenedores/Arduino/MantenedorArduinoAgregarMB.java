/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores.Arduino;

import managedBeans.mantenedores.MantenedorGenericoConversation;
import entities.Arduino;
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
@Named(value = "mantenedorArduinoAgregarMB")
@RequestScoped
public class MantenedorArduinoAgregarMB {
    @EJB
    private ArduinoFacadeLocal arduinoFacade;
    
    @Inject
    private MantenedorGenericoConversation mantArduinoConv;
    
    private String nombre;
    
    private String puerto_seleccionado;
    
    private List<String> listaPuertos;
    
    @PostConstruct
    public void init() {
        listaPuertos = arduinoFacade.getPuertosDisponibles();
        
    }
    
    public void agregarArduino() {
        try {
            arduinoFacade.agregarArduino(nombre, puerto_seleccionado);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado un nuevo arduino",
                    "Se ha creado el arduino \"".concat(nombre).concat("\""));
            volverToLista();
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR, 
                    e.getMessage(), 
                    e.getMessage());
            CommonFunctions.goToPage("/faces/users/admin/agregarArduino.xhtml?faces-redirect=true");
        }
    }
    
    public void volverToLista() {
        CommonFunctions.goToPage("/faces/users/admin/verArduinos.xhtml?faces-redirect=true");
    }
    
    
    /**
     * Creates a new instance of mantenedorArduinoVerListado
     */
    public MantenedorArduinoAgregarMB() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuerto_seleccionado() {
        return puerto_seleccionado;
    }

    public void setPuerto_seleccionado(String puerto_seleccionado) {
        this.puerto_seleccionado = puerto_seleccionado;
    }

    public List<String> getListaPuertos() {
        return listaPuertos;
    }

    public void setListaPuertos(List<String> listaPuertos) {
        this.listaPuertos = listaPuertos;
    }
    
}
