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
import javax.faces.application.FacesMessage;
import org.primefaces.model.DualListModel;
import otros.CommonFunctions;
import pojos.SelectElemPojo;
import sessionBeans.DispositivoFacadeLocal;
import sessionBeans.EscenaFacadeLocal;
import sessionBeans.UsuarioFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorEscenaAgregarMB")
@RequestScoped
public class MantenedorEscenaAgregarMB {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    private String nombre;
    
    private DualListModel<SelectElemPojo> dispositivosDualModel;
    
    private DualListModel<SelectElemPojo> usuariosDualModel;

    @PostConstruct
    public void init() {
        List<SelectElemPojo> sourceDispositivos, targetDispositivos;
        List<SelectElemPojo> sourceUsuarios, targetUsuarios;
        
        List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        SelectElemPojo elemPojoTemp;
        sourceDispositivos = new LinkedList<SelectElemPojo>();
        targetDispositivos = new LinkedList<SelectElemPojo>();
        for(Dispositivo t : listaTemp) {
            elemPojoTemp = new SelectElemPojo();
            elemPojoTemp.setId(t.getId().toString());
            elemPojoTemp.setLabel(t.getNombre());
            sourceDispositivos.add(elemPojoTemp);
        }
        
        List<Usuario> listaTemp2 = usuarioFacade.findAll();
        sourceUsuarios = new LinkedList<SelectElemPojo>();
        targetUsuarios = new LinkedList<SelectElemPojo>();
        for(Usuario t : listaTemp2) {
            elemPojoTemp = new SelectElemPojo();
            elemPojoTemp.setId(t.getId().toString());
            elemPojoTemp.setLabel(t.getUsername());
            sourceUsuarios.add(elemPojoTemp);
        }
        
        dispositivosDualModel = new DualListModel(sourceDispositivos, targetDispositivos);
        usuariosDualModel = new DualListModel(sourceUsuarios, targetUsuarios);
        
    }
    
    public void agregarEscena() {
        List<Integer> idDispositivos = new LinkedList();
        for(SelectElemPojo s : dispositivosDualModel.getTarget()) {
            idDispositivos.add(Integer.parseInt(s.getId()));
        }
        List<Integer> idUsuarios = new LinkedList();
        for(SelectElemPojo s : usuariosDualModel.getTarget()) {
            idUsuarios.add(Integer.parseInt(s.getId()));
        }
        try {
            //escenaFacade.crearEscena(nombre, accionesdispositivos, idUsuarios);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado un nuevo administrador",
                    "Se ha creado el usuario \"".concat(nombre).concat("\" su contraseña es igual a su username"));
            CommonFunctions.goToPage("/faces/users/admin/verUsuarios.xhtml?faces-redirect=true");
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR, 
                    e.getMessage(), 
                    e.getMessage());
            CommonFunctions.goToPage("/faces/users/admin/agregarUsuario.xhtml?faces-redirect=true");
        }
    }
    
    public void volverToLista() {
        CommonFunctions.goToPage("/faces/users/admin/verUsuarios.xhtml?faces-redirect=true");
    }
    
    /**
     * Creates a new instance of MantenedorUsuarioAgregarMB
     */
    public MantenedorEscenaAgregarMB() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DualListModel<SelectElemPojo> getDispositivosDualModel() {
        return dispositivosDualModel;
    }

    public void setDispositivosDualModel(DualListModel<SelectElemPojo> dispositivosDualModel) {
        this.dispositivosDualModel = dispositivosDualModel;
    }

    public DualListModel<SelectElemPojo> getUsuariosDualModel() {
        return usuariosDualModel;
    }

    public void setUsuariosDualModel(DualListModel<SelectElemPojo> usuariosDualModel) {
        this.usuariosDualModel = usuariosDualModel;
    }

}
