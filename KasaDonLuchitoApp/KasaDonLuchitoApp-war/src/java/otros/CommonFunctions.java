
package otros;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victor
 */
public class CommonFunctions {
    /**
     * Pasale una url incluyendo /faces/ y te redirecciona
     * @param pagina 
     */
    public static void goToPage(String pagina) {
       ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
       try {
           externalContext.redirect(externalContext.getRequestContextPath() + pagina);
       }
       catch (IOException e) {
           System.out.println(e.getMessage());
       }
    }
    
    public static void goToIndex() {
        goToPage("/faces/index.xhtml");
    }
    
    public static String getUsuarioLogueado() {
        HttpServletRequest request = (HttpServletRequest) 
                (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        return request.getRemoteUser();
    }
    
    public static boolean isUserInRole(String nombreRol) {
        HttpServletRequest request = (HttpServletRequest) 
                (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        return request.isUserInRole(nombreRol);
    }
    
    public static boolean isGetMethod() {
        HttpServletRequest request = (HttpServletRequest) 
                (FacesContext.getCurrentInstance().getExternalContext().getRequest());
        if (request.getMethod().equals("GET")) {
            return true;
        }
        return false;
    }
    
    public static void viewMessage(FacesMessage.Severity sev, String titMsg, String msj) {
        FacesMessage msg = new FacesMessage(sev, titMsg, msj);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
}
