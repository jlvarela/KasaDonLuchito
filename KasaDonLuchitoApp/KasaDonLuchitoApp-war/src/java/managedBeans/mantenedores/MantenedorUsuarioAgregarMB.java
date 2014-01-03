/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Dispositivo;
import entities.Escena;
import entities.TipoUsuario;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import org.primefaces.model.DualListModel;
import otros.CommonFunctions;
import pojos.DispositivoPojo;
import pojos.SelectElemPojo;
import sessionBeans.DispositivoFacadeLocal;
import sessionBeans.EscenaFacadeLocal;
import sessionBeans.TipoUsuarioFacadeLocal;
import sessionBeans.UsuarioFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorUsuarioAgregarMB")
@RequestScoped
public class MantenedorUsuarioAgregarMB {
    @EJB
    private TipoUsuarioFacadeLocal tipoUsuarioFacade;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    private String nombre;
    
    private String nombreRol;
    
    private List<SelectElemPojo> roles;
    
    private DualListModel<SelectElemPojo> dispositivosDualModel;
    
    private DualListModel<SelectElemPojo> escenasDualModel;

    @PostConstruct
    public void init() {
        List<SelectElemPojo> sourceDispositivos, targetDispositivos;
        List<SelectElemPojo> sourceEscenas, targetEscenas;
        
        List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        SelectElemPojo dispPojoTemp;
        sourceDispositivos = new LinkedList<SelectElemPojo>();
        targetDispositivos = new LinkedList<SelectElemPojo>();
        for(Dispositivo t : listaTemp) {
            dispPojoTemp = new SelectElemPojo();
            dispPojoTemp.setId(t.getId().toString());
            dispPojoTemp.setLabel(t.getNombre());
            sourceDispositivos.add(dispPojoTemp);
        }
        
        List<Escena> listaTemp2 = escenaFacade.findAll();
        sourceEscenas = new LinkedList<SelectElemPojo>();
        targetEscenas = new LinkedList<SelectElemPojo>();
        for(Escena t : listaTemp2) {
            dispPojoTemp = new SelectElemPojo();
            dispPojoTemp.setId(t.getId().toString());
            dispPojoTemp.setLabel(t.getNombre());
            sourceEscenas.add(dispPojoTemp);
        }
        
        dispositivosDualModel = new DualListModel(sourceDispositivos, targetDispositivos);
        escenasDualModel = new DualListModel(sourceEscenas, targetEscenas);
        
        roles = new LinkedList<SelectElemPojo>();
        List<TipoUsuario> listaTemp3 = tipoUsuarioFacade.findAll();
        SelectElemPojo tipoTemp;
        for(TipoUsuario t : listaTemp3) {
            tipoTemp = new SelectElemPojo();
            tipoTemp.setId(t.getNombreTipo());
            tipoTemp.setLabel(t.getNombreTipo());
            roles.add(tipoTemp);
        }
    }
    
    public void agregarUsuario() {
        List<Integer> idDispositivos = new LinkedList();
        for(SelectElemPojo s : dispositivosDualModel.getTarget()) {
            idDispositivos.add(Integer.parseInt(s.getId()));
        }
        List<Integer> idEscenas = new LinkedList();
        for(SelectElemPojo s : escenasDualModel.getTarget()) {
            idEscenas.add(Integer.parseInt(s.getId()));
        }
        try {
            usuarioFacade.crearUsuario(nombre, idDispositivos, idEscenas, nombreRol);
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
    public MantenedorUsuarioAgregarMB() {
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

    public DualListModel<SelectElemPojo> getEscenasDualModel() {
        return escenasDualModel;
    }

    public void setEscenasDualModel(DualListModel<SelectElemPojo> escenasDualModel) {
        this.escenasDualModel = escenasDualModel;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public List<SelectElemPojo> getRoles() {
        return roles;
    }

    public void setRoles(List<SelectElemPojo> roles) {
        this.roles = roles;
    }
    
}
