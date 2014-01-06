/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Escena;
import entities.Timer;
import java.text.DateFormat;
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
import pojos.TimerPojo;
import sessionBeans.TimerFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorTimersVerListadoMB")
@RequestScoped
public class MantenedorTimersVerListadoMB {
    @EJB
    private TimerFacadeLocal timerFacade;
    
    @Inject
    private MantenedorGenericoConversation mantEscenaConv;

    private List<TimerPojo> lista;
    private List<TimerPojo> listaBusqueda;
    
    
    @PostConstruct
    public void init() {
        List<Timer> listaTemp = timerFacade.findAll();
        //List<Dispositivo> listaTemp = dispositivoFacade.findAll();
        TimerPojo elemPojo;
        lista = new LinkedList<TimerPojo>();
        listaBusqueda = new LinkedList<TimerPojo>();
        String usernameActual = CommonFunctions.getUsuarioLogueado();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for(Timer t : listaTemp) {
            elemPojo = new TimerPojo();
            elemPojo.setId(t.getId());
            elemPojo.setNombre(t.getNombre());
            elemPojo.setActivado(t.isActivo());
            if (t.getDispositivoQueAcciona() != null) {
                elemPojo.setAccionARealizar("Poner dispositivo \""+t.getDispositivoQueAcciona().getNombre()+"\" en \""+t.getValorAccionDispositivo()+"\"");
            }
            if (t.getEscenaQueAcciona()!= null) {
                elemPojo.setAccionARealizar("Accionar escena \""+t.getEscenaQueAcciona().getNombre()+"\"");
            }
            
            elemPojo.setHora(sdf.format(t.getHora()));
            StringBuilder strBuild = new StringBuilder();
            boolean primeroListo = false;
            if (t.isRepetirLunes()) {
                strBuild.append("Lun");
                primeroListo = true;
            }
            if (t.isRepetirMartes()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Mar");
                primeroListo = true;
            }
            if (t.isRepetirMiercoles()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Mie");
                primeroListo = true;
            }
            if (t.isRepetirJueves()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Jue");
                primeroListo = true;
            }
            if (t.isRepetirViernes()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Vie");
                primeroListo = true;
            }
            if (t.isRepetirSabado()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Sab");
                primeroListo = true;
            }
            if (t.isRepetirDomingo()) {
                if (primeroListo)
                    strBuild.append("-");
                strBuild.append("Dom");
            }
                        
            elemPojo.setCuandoRepetir(strBuild.toString());
            
            elemPojo.setEsCreador(false);
            //Si es el usuario creador, puede editarla y eliminarla
            if (usernameActual.equals(t.getUsuarioCreador().getUsername())) {
                elemPojo.setEsCreador(true);
            }
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
        Timer toEdit = timerFacade.find(num);
        if (toEdit != null) {
            this.mantEscenaConv.beginConversation();
            this.mantEscenaConv.setState(MantenedorGenericoConversation.VER);
            this.mantEscenaConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/detallesTimer.xhtml?cid=".concat(this.mantEscenaConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantEscenaConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void editar(Integer num) {
        Timer toEdit = timerFacade.find(num);
        if (toEdit != null) {
            this.mantEscenaConv.beginConversation();
            this.mantEscenaConv.setState(MantenedorGenericoConversation.EDITAR);
            this.mantEscenaConv.setIdToEdit(num);
            CommonFunctions.goToPage("/faces/users/editarTimer.xhtml?cid=".concat(this.mantEscenaConv.getConversation().getId()));
        }
        else {
            //MOSTRAR ERROR
            this.mantEscenaConv.limpiarDatos();
            CommonFunctions.goToIndex();
        }
    }
    
    public void eliminar(Integer num) {
        try {
            Timer toEdit = timerFacade.find(num);
            if (toEdit != null) {
                timerFacade.remove(toEdit);
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                        "Se ha eliminado el timer",
                        "Se ha eliminado correctamente el timer");
            }
            else {
                //MOSTRAR ERROR
                this.mantEscenaConv.limpiarDatos();
                CommonFunctions.goToPage("/faces/users/verTimers.xhtml?faces-redirect=true");
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    e.getMessage(),
                    e.getMessage());
        }
        this.mantEscenaConv.limpiarDatos();
        CommonFunctions.goToPage("/faces/users/verTimers.xhtml?faces-redirect=true");
    }
    
    
    /**
     * Creates a new instance of MantenedorEscenasVerListadoMB
     */
    public MantenedorTimersVerListadoMB() {
    }

    public List<TimerPojo> getLista() {
        return lista;
    }

    public void setLista(List<TimerPojo> lista) {
        this.lista = lista;
    }

    public List<TimerPojo> getListaBusqueda() {
        return listaBusqueda;
    }

    public void setListaBusqueda(List<TimerPojo> listaBusqueda) {
        this.listaBusqueda = listaBusqueda;
    }
    
}
