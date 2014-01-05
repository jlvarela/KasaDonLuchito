/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Dispositivo;
import entities.Usuario;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DualListModel;
import otros.CommonFunctions;
import pojos.AccionPojo;
import pojos.SelectElemPojo;
import sessionBeans.DispositivoFacadeLocal;
import sessionBeans.EscenaFacadeLocal;
import sessionBeans.UsuarioFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorEscenaAgregarMB")
@ViewScoped
public class MantenedorEscenaAgregarMB implements Serializable{
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    private String nombre;
    
    private List<SelectElemPojo> dispositivos;
    
    private List<SelectElemPojo> valores;
    
    private DualListModel<SelectElemPojo> usuariosDualModel;
    
    private Integer idDispSeleccionado;
    
    private Integer valorAccionesDispositivo;
    
    private List<AccionPojo> accionesEscena;

    @PostConstruct
    public void init() {
        if (accionesEscena == null) {
            accionesEscena = new LinkedList<AccionPojo>();
            System.out.println("Creando la lista de acciones de la escena");
        }
        List<SelectElemPojo> sourceDispositivos;
        List<SelectElemPojo> sourceUsuarios, targetUsuarios;
        
        List<Dispositivo> listaTemp = dispositivoFacade.findOnlyActuatorsByUserNameLogged(CommonFunctions.getUsuarioLogueado());
        SelectElemPojo elemPojoTemp;
        sourceDispositivos = new LinkedList<SelectElemPojo>();
        boolean agregar;
        for(Dispositivo t : listaTemp) {
            //Se evita agregar los dispositivos que ya se tienen acciones agregadas
            agregar = true;
            for(AccionPojo accionTemp : getAccionesEscena()) {
                if (accionTemp.getIdDispositivo().intValue() == t.getId().intValue()) {
                    agregar = false;
                    break;
                }
            }
            if (agregar) {
                elemPojoTemp = new SelectElemPojo();
                elemPojoTemp.setId(t.getId().toString());
                elemPojoTemp.setLabel(t.getNombre());
                sourceDispositivos.add(elemPojoTemp);
            }
            
        }
        
        List<Usuario> listaTemp2 = usuarioFacade.findOnlyUsers();
        sourceUsuarios = new LinkedList<SelectElemPojo>();
        targetUsuarios = new LinkedList<SelectElemPojo>();
        for(Usuario t : listaTemp2) {
            elemPojoTemp = new SelectElemPojo();
            elemPojoTemp.setId(t.getId().toString());
            elemPojoTemp.setLabel(t.getUsername());
            sourceUsuarios.add(elemPojoTemp);
        }
        
        dispositivos = sourceDispositivos;
        usuariosDualModel = new DualListModel(sourceUsuarios, targetUsuarios);
        
    }
    
    public void eliminarAccion(Integer idDisp) {
        System.out.println("Eliminando acción para el dispositivo con id="+idDisp);
        if (idDisp == null) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "Ha ocurrido un error al eliminar la acción",
                    "No ha sido posible eliminar la acción, vuelva a intentarlo más adelante");
            return;
        }
        //Se elimina la acción de la tabla de acciones
        int idSelecInt = idDisp.intValue();
        Iterator<AccionPojo> it = accionesEscena.iterator();
        AccionPojo elem = null;
        while (it.hasNext()) {
            elem = it.next(); // must be called before you can call i.remove()
            // Do something
            if (elem.getIdDispositivo().intValue() == idSelecInt) {
                it.remove();
                break;
            }
        }
        
        //Se agrega el dispositivo ahora disponible a la lista de dispositivos
        if (elem != null) {
            dispositivos.add(new SelectElemPojo(elem.getIdDispositivo().toString(), elem.getNombreDispositivo()));
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha eliminado una acción",
                    "Se ha la acción de la escena que se está creando");
        }
        else {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "Ha ocurrido un error al eliminar la acción",
                    "No ha sido posible eliminar la acción, vuelva a intentarlo más adelante");
        }
        
        System.out.println("acción eliminada para elem");
        
        
    }
    
    public void agregarAccion() {
        if ((idDispSeleccionado != null) && (valorAccionesDispositivo != null)) {
            Dispositivo d = dispositivoFacade.find(idDispSeleccionado);
            AccionPojo accionTemp = new AccionPojo();
            accionTemp.setIdDispositivo(idDispSeleccionado);
            accionTemp.setNombreDispositivo(d.getNombre());
            accionTemp.setValor(valorAccionesDispositivo);
            getAccionesEscena().add(accionTemp);
            //System.out.println("Se ha agregado una acción");
            //System.out.println("Y ahora revisando accionesEscena de largo: "+accionesEscena.size());
            
            int idSelecInt = idDispSeleccionado.intValue();
            Iterator<SelectElemPojo> it = dispositivos.iterator();
            SelectElemPojo elem;
            while (it.hasNext()) {
                elem = it.next(); // must be called before you can call i.remove()
                // Do something
                if (Integer.parseInt(elem.getId()) == idSelecInt) {
                    it.remove();
                    break;
                }
            }
            
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha agregado una acción",
                    "Se ha creado una nueva acción para la escena");
        }
        else {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "No ha seleccionado un dispositivo o valor para este",
                    "No se ha podido agregar la acción porque no ha seleccionado un dispositivo o valor para este");
        }
    }
    
    public void agregarEscena() {
        List<Integer> idUsuarios = new LinkedList();
        for(SelectElemPojo s : usuariosDualModel.getTarget()) {
            idUsuarios.add(Integer.parseInt(s.getId()));
        }
        if (!CommonFunctions.isUserInRole("Administrador")) {
            Integer idUser = usuarioFacade.findByUsername(CommonFunctions.getUsuarioLogueado()).getId();
            idUsuarios.add(idUser);
        }
        try {
            Map<Integer, Integer> mapaDispValor = new HashMap<Integer, Integer>();
            for (AccionPojo a : getAccionesEscena()) {
                mapaDispValor.put(a.getIdDispositivo(), a.getValor());
            }
            escenaFacade.crearEscena(nombre, mapaDispValor, idUsuarios);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado una escena",
                    "Se ha creado la escena \"".concat(nombre).concat("\""));
            CommonFunctions.goToPage("/faces/users/verEscenas.xhtml?faces-redirect=true");
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR, 
                    e.getMessage(), 
                    e.getMessage());
            CommonFunctions.goToPage("/faces/users/agregarEscena.xhtml?faces-redirect=true");
        }
    }
    
    public void cargarValores() {
        Integer idDisp = idDispSeleccionado;
        List<Integer> listaValores = dispositivoFacade.getValoresDispositivo(idDisp);
        SelectElemPojo elemTemp;
        valores.clear();
        for(Integer valor : listaValores) {
            elemTemp = new SelectElemPojo(valor.toString(), valor.toString());
            valores.add(elemTemp);
        }
    }
    
    public void volverToLista() {
        CommonFunctions.goToPage("/faces/users/verEscenas.xhtml?faces-redirect=true");
    }
    
    /**
     * Creates a new instance of MantenedorUsuarioAgregarMB
     */
    public MantenedorEscenaAgregarMB() {
        valores = new LinkedList<SelectElemPojo>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<SelectElemPojo> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<SelectElemPojo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public DualListModel<SelectElemPojo> getUsuariosDualModel() {
        return usuariosDualModel;
    }

    public void setUsuariosDualModel(DualListModel<SelectElemPojo> usuariosDualModel) {
        this.usuariosDualModel = usuariosDualModel;
    }

    public Integer getIdDispSeleccionado() {
        return idDispSeleccionado;
    }

    public void setIdDispSeleccionado(Integer idDispSeleccionado) {
        this.idDispSeleccionado = idDispSeleccionado;
    }

    public Integer getValorAccionesDispositivo() {
        return valorAccionesDispositivo;
    }

    public void setValorAccionesDispositivo(Integer valorAccionesDispositivo) {
        this.valorAccionesDispositivo = valorAccionesDispositivo;
    }

    public List<SelectElemPojo> getValores() {
        return valores;
    }

    public void setValores(List<SelectElemPojo> valores) {
        this.valores = valores;
    }

    public List<AccionPojo> getAccionesEscena() {
        return accionesEscena;
    }

    public void setAccionesEscena(List<AccionPojo> accionesEscena) {
        this.accionesEscena = accionesEscena;
    }

}
