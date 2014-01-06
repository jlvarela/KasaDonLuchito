/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Dispositivo;
import entities.Escena;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import otros.CommonFunctions;
import pojos.SelectElemPojo;
import sessionBeans.DispositivoFacadeLocal;
import sessionBeans.EscenaFacadeLocal;
import sessionBeans.TimerFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorTimerAgregarMB")
@ViewScoped
public class MantenedorTimerAgregarMB implements Serializable {
    @EJB
    private TimerFacadeLocal timerFacade;
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    private String nombre;
    
    private Date hora;
    
    private List<String> dias;
    
    private boolean accionaEscena;
    
    private Integer idDispSeleccionado;
    
    private Integer valorAccionDispositivo;
    
    private List<SelectElemPojo> dispositivos;
    
    private List<SelectElemPojo> valores;
    
    private Integer idEscenaSeleccionada;
    
    private List<SelectElemPojo> escenas;
    
    @PostConstruct
    public void init() {
        
        List<Dispositivo> listaTemp = dispositivoFacade.findOnlyActuatorsByUserNameLogged(CommonFunctions.getUsuarioLogueado());
        SelectElemPojo elemPojoTemp;
        for(Dispositivo t : listaTemp) {
            elemPojoTemp = new SelectElemPojo(t.getId().toString(), t.getNombre());
            dispositivos.add(elemPojoTemp);
        }
        
        List<Escena> listaTemp2 = escenaFacade.findAll();
        for(Escena t : listaTemp2) {
            elemPojoTemp = new SelectElemPojo(t.getId().toString(), t.getNombre());
            escenas.add(elemPojoTemp);
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
    
    public void queAccionaCambiado() {
        System.out.println("Se ha presionado el botón 'accionaEscena' "+accionaEscena);
    }
    
    public void agregarEscena() {
        /*
        List<Integer> idUsuarios = new LinkedList();
        for(SelectElemPojo s : usuariosDualModel.getTarget()) {
            idUsuarios.add(Integer.parseInt(s.getId()));
        }
        if (!CommonFunctions.isUserInRole("Administrador")) {
            Integer idUser = usuarioFacade.findByUsername(CommonFunctions.getUsuarioLogueado()).getId();
            idUsuarios.add(idUser);
        }
        */
        try {
            timerFacade.crearTimer(nombre, hora, dias, accionaEscena, idDispSeleccionado, valorAccionDispositivo, idEscenaSeleccionada);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado un timer",
                    "Se ha creado el timer \"".concat(nombre).concat("\""));
            CommonFunctions.goToPage("/faces/users/verTimers.xhtml?faces-redirect=true");
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR, 
                    e.getMessage(), 
                    e.getMessage());
            CommonFunctions.goToPage("/faces/users/admin/agregarTimer.xhtml?faces-redirect=true");
        }
    }

    public void volverToLista() {
        CommonFunctions.goToPage("/faces/users/verTimers.xhtml?faces-redirect=true");
    }
    
    /**
     * Creates a new instance of MantenedorTimerAgregarMB
     */
    public MantenedorTimerAgregarMB() {
        valores = new LinkedList<SelectElemPojo>();
        dispositivos = new LinkedList<SelectElemPojo>();
        escenas = new LinkedList<SelectElemPojo>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public List<String> getDias() {
        return dias;
    }

    public void setDias(List<String> dias) {
        this.dias = dias;
    }

    public boolean isAccionaEscena() {
        return accionaEscena;
    }

    public void setAccionaEscena(boolean accionaEscena) {
        this.accionaEscena = accionaEscena;
    }

    public Integer getIdDispSeleccionado() {
        return idDispSeleccionado;
    }

    public void setIdDispSeleccionado(Integer idDispSeleccionado) {
        this.idDispSeleccionado = idDispSeleccionado;
    }

    public Integer getValorAccionDispositivo() {
        return valorAccionDispositivo;
    }

    public void setValorAccionDispositivo(Integer valorAccionDispositivo) {
        this.valorAccionDispositivo = valorAccionDispositivo;
    }

    public List<SelectElemPojo> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(List<SelectElemPojo> dispositivos) {
        this.dispositivos = dispositivos;
    }

    public List<SelectElemPojo> getValores() {
        return valores;
    }

    public void setValores(List<SelectElemPojo> valores) {
        this.valores = valores;
    }

    public Integer getIdEscenaSeleccionada() {
        return idEscenaSeleccionada;
    }

    public void setIdEscenaSeleccionada(Integer idEscenaSeleccionada) {
        this.idEscenaSeleccionada = idEscenaSeleccionada;
    }

    public List<SelectElemPojo> getEscenas() {
        return escenas;
    }

    public void setEscenas(List<SelectElemPojo> escenas) {
        this.escenas = escenas;
    }
    
}
