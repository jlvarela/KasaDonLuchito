/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import sessionBeans.ConfiguracionFacadeLocal;

/**
 *
 * @author victor
 */
@ManagedBean
@Named(value = "configuracionesMB")
@RequestScoped
public class ConfiguracionesMB {
    @EJB
    private ConfiguracionFacadeLocal configuracionFacade;
    
    public String getConfiguracion(String nombre) {
        String res = configuracionFacade.findByName(nombre);
        return res;
    }
    
    public String getFondoPantalla() {
        String res = configuracionFacade.findByName("fondo_app");
        if (res == null) {
            res = "Fondo2.jpg";
        }
        return res;
    }
    
    public String getLogo() {
        String res = configuracionFacade.findByName("logo_app");
        if (res == null) {
            res = "logo.jpg";
        }
        return res;
    }
    
    public String getIcon() {
        String res = configuracionFacade.findByName("icono_app");
        if (res == null) {
            res = "icon.png";
        }
        return res;
    }

    /**
     * Creates a new instance of ConfiguracionesMB
     */
    public ConfiguracionesMB() {
    }
}
