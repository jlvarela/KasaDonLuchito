/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import otros.CommonFunctions;

/**
 *
 * @author victor
 */
@Named(value = "login")
@RequestScoped
public class Login {
    @Inject private AutenticadorMB autenticador;
    private String username;
    private String password;
    private String originalURL;
    
    /**
     * Creates a new instance of Login
     */
    public Login() {
    }
    
    @PostConstruct
    public void init() {
        originalURL = (String)FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        //Le quito el contextPath
        //System.out.println("URL Original: "+originalURL);
        if (originalURL == null) {
            originalURL = autenticador.getOriginalURL();
            if (originalURL == null) {
                originalURL = "/faces/users/manipularDispositivos.xhtml";
            }
        }
        else {
            String contextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
            originalURL = originalURL.substring(contextPath.length());
            autenticador.setOriginalURL(originalURL);
        }
        
        //Hace que sea redireccionado en caso de ya estar logueado
        if (autenticador.verificaSiEstaLogueado()) {
            CommonFunctions.goToPage(originalURL);
        }
    }
    
    public void login() {
        if(autenticador.login(username, password)) {
            CommonFunctions.goToPage(originalURL);
        }
        
    }
    
    public void indexLoggued() {
        CommonFunctions.goToIndex();
    }
    
    public void goToIndex() {
        CommonFunctions.goToIndex();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
}
