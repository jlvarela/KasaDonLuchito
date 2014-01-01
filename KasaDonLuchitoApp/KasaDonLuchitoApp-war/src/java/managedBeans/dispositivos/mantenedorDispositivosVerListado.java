/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.dispositivos;

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
import javax.faces.context.FacesContext;
import org.primefaces.event.SlideEndEvent;
import pojos.DispositivoPojo;
import sessionBeans.DispositivoFacadeLocal;

/**
 *
 * @author victor
 */
@ManagedBean
@Named(value = "mantenedorDispositivosVerListado")
@RequestScoped
public class mantenedorDispositivosVerListado {
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    private List<DispositivoPojo> lista;
    private List<DispositivoPojo> listaBusqueda;
    
    @PostConstruct
    public void init() {
        List<Dispositivo> listaTemp = dispositivoFacade.findAll();
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
    
    /**
     * Creates a new instance of mantenedorDispositivosVerListado
     */
    public mantenedorDispositivosVerListado() {
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
