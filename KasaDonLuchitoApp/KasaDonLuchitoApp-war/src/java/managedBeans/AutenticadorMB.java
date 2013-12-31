/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import otros.CommonFunctions;
//import com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm;

/**
 *
 * @author victor
 */
@Named(value = "autenticadorMB")
@SessionScoped
public class AutenticadorMB implements Serializable {
    private String originalURL;
    
    
    public boolean verificaSiEstaLogueado() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (request.getRemoteUser() != null) {
            return true;
        }
        return false;
    }
    
    public boolean verificarSiLoginAntiguo(String username) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (request.getRemoteUser() != null) {
            if (request.getRemoteUser().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean login(String username, String password) {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            if (!verificaSiEstaLogueado()) {
                request.login(username, password);
            }
            return true;
        }
        catch (Exception e) {
            System.out.println("MENSAJE DE EXCEPCIÓN EN LOGIN: "+e.getMessage());
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Nombre de usuario o contraseña incorrectos", ""));
        }
        return false;
    }

    public void logout() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        this.originalURL = null;
        CommonFunctions.goToPage("/faces/index.xhtml");
    }
    
    public void indexLoggued() {
        CommonFunctions.goToPage("/faces/users/manipularDispositivos.xhtml");
    }
    
    public AutenticadorMB() {
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }
    
}