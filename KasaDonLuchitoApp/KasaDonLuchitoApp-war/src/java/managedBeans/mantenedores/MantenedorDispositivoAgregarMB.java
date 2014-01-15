/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Arduino;
import entities.TipoDispositivo;
import entities.TipoDispositivoUserLevel;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import otros.CommonFunctions;
import pojos.SelectElemPojo;
import sessionBeans.ArduinoFacadeLocal;
import sessionBeans.DispositivoFacadeLocal;
import sessionBeans.TipoDispositivoFacadeLocal;
import sessionBeans.TipoDispositivoUserLevelFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorDispositivoAgregarMB")
@RequestScoped
public class MantenedorDispositivoAgregarMB {
    @EJB
    private TipoDispositivoUserLevelFacadeLocal tipoDispositivoUserLevelFacade;
    @EJB
    private ArduinoFacadeLocal arduinoFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    
    //@Inject
    //private MantenedorGenericoConversation mantArduinoConv;
    
    private String nombre;
    
    private Integer idInterno;
    
    private Integer id_tipo_dispositivo;
    
    private Integer id_arduino;
    
    private String pines;
    
    private String configs;
    
    private List<SelectElemPojo> arduinos;
    
    private List<SelectElemPojo> tipos_dispositivos;
    
    @PostConstruct
    public void init() {
        List<Arduino> listaArduinos = arduinoFacade.findAll();
        List<TipoDispositivoUserLevel> listaTiposDispositivos = tipoDispositivoUserLevelFacade.findAll();
        
        SelectElemPojo elemTemp;
        arduinos = new LinkedList<SelectElemPojo>();
        for(Arduino a : listaArduinos) {
            elemTemp = new SelectElemPojo(a.getId().toString(), a.getNombre());
            arduinos.add(elemTemp);
        }
        
        tipos_dispositivos = new LinkedList<SelectElemPojo>();
        for (TipoDispositivoUserLevel td : listaTiposDispositivos) {
            elemTemp = new SelectElemPojo(td.getId().toString(), td.getNombre());
            tipos_dispositivos.add(elemTemp);
        }
        //List<Arduino> listaArduinos = dispositivoFacade.getPuertosDisponibles();
        
    }
    
    public void agregarDispositivo() {
        try {
            List<Integer> lista_pines = new LinkedList<Integer>();
            List<Integer> lista_configs = new LinkedList<Integer>();
            
            Scanner in = new Scanner(pines);
            while(in.hasNextInt()) {
                lista_pines.add(new Integer(in.nextInt()));
            }
            
            in = new Scanner(configs);
            while(in.hasNextInt()) {
                lista_configs.add(new Integer(in.nextInt()));
            }
            
            //System.out.println("Se parsearon "+ lista_pines.size()+" pines y "+lista_configs.size()+" configs");
            
            dispositivoFacade.agregarDispositivo(nombre, idInterno, id_tipo_dispositivo, id_arduino, lista_pines, lista_configs);
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado un nuevo dispositivo",
                    "Se ha creado el dispositivo \"".concat(nombre).concat("\""));
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
        CommonFunctions.goToPage("/faces/users/manipularDispositivos.xhtml?faces-redirect=true");
    }
    
    
    /**
     * Creates a new instance of mantenedorArduinoVerListado
     */
    public MantenedorDispositivoAgregarMB() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdInterno() {
        return idInterno;
    }

    public void setIdInterno(Integer idInterno) {
        this.idInterno = idInterno;
    }

    public Integer getId_tipo_dispositivo() {
        return id_tipo_dispositivo;
    }

    public void setId_tipo_dispositivo(Integer id_tipo_dispositivo) {
        this.id_tipo_dispositivo = id_tipo_dispositivo;
    }

    public Integer getId_arduino() {
        return id_arduino;
    }

    public void setId_arduino(Integer id_arduino) {
        this.id_arduino = id_arduino;
    }

    public String getPines() {
        return pines;
    }

    public void setPines(String pines) {
        this.pines = pines;
    }

    public String getConfigs() {
        return configs;
    }

    public void setConfigs(String configs) {
        this.configs = configs;
    }

    public List<SelectElemPojo> getArduinos() {
        return arduinos;
    }

    public void setArduinos(List<SelectElemPojo> arduinos) {
        this.arduinos = arduinos;
    }

    public List<SelectElemPojo> getTipos_dispositivos() {
        return tipos_dispositivos;
    }

    public void setTipos_dispositivos(List<SelectElemPojo> tipos_dispositivos) {
        this.tipos_dispositivos = tipos_dispositivos;
    }
    
}
