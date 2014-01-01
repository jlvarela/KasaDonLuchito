/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import otros.CommonFunctions;
import sessionBeans.PobladoInicialLocal;

/**
 *
 * @author victor
 */
@Named(value = "pobladoInicialMB")
@RequestScoped
public class pobladoInicialMB {
    @EJB
    PobladoInicialLocal pobladoInicial;// = lookupPobladoInicialLocal();

    /**
     * Creates a new instance of pobladoInicialMB
     */
    public pobladoInicialMB() {
    }
    
    public void poblarModoTesting() {
        try {
            pobladoInicial.poblarDBTesting();
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha poblado la base de datos en modo testing",
                    "Se ha poblado la base de datos y está todo listo para poder ejecutar");
            CommonFunctions.goToPage("/faces/index.xhtml?faces-redirect=true");
        } 
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "Error: La configuración inicial ya se había realizado con anterioridad",
                    "Ha ocurrido un error ya que el sistema ya ha sido configurado anteriormente");
            CommonFunctions.goToPage("/faces/index.xhtml?faces-redirect=true");
        }
    }
    
    public void poblarModoProduction() {
        try {
            pobladoInicial.poblarDBProduction();
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha creado la config inicial. username: admin, password: admin",
                    "Se ha poblado la base de datos y está todo listo para poder ejecutar");
            CommonFunctions.goToPage("/faces/index.xhtml?faces-redirect=true");
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "Error: La configuración inicial ya se había realizado con anterioridad",
                    "Ha ocurrido un error ya que el sistema ya ha sido configurado anteriormente");
            CommonFunctions.goToPage("/faces/index.xhtml?faces-redirect=true");
        }
    }
}
