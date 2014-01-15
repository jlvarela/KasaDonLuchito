/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.Usuario;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import managedBeans.AutenticadorMB;
import otros.CommonFunctions;
import sessionBeans.UsuarioFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorPerfilUsuarioMB")
@RequestScoped
public class MantenedorPerfilUsuarioMB {
    @EJB
    private UsuarioFacadeLocal usuarioFacade;

    @Inject
    private AutenticadorMB autenticadorMB;
    
    private String username;
    private String password;
    private String passwordNew;
    private String passwordNew2;
    
    @PostConstruct
    public void init() {
        Usuario u = usuarioFacade.findByUsername(CommonFunctions.getUsuarioLogueado());
        if (u == null) {
            autenticadorMB.logout();
            return;
        }
        username = u.getUsername();
        
    }
    
    public void actualizarDatos() {
        try {
            String oldUsername = CommonFunctions.getUsuarioLogueado();
            if (!passwordNew.equals(passwordNew2)) {
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR,
                    "Las contraseñas no coinciden",
                    "Error: Las contraseñas no coinciden");
                return;
            }
            boolean cambioUsername = usuarioFacade.changeUsernameAndPassword(oldUsername, username, password, passwordNew);
            if (cambioUsername) {
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se han cambiado sus datos y cerrado sesión",
                    "Vuelva a iniciar sesión nuevamente con sus nuevos datos");
                autenticadorMB.logout();
            }
            else {
                CommonFunctions.viewMessage(FacesMessage.SEVERITY_INFO,
                    "Se ha cambiado su contraseña",
                    "Se ha cambiado su contraseña");
                this.volverToLista();
            }
        }
        catch (Exception e) {
            CommonFunctions.viewMessage(FacesMessage.SEVERITY_ERROR, 
                e.getMessage(), 
                e.getMessage());

        }
    }
    
    public void volverToLista() {
        CommonFunctions.goToPage("/faces/users/manipularDispositivos.xhtml?faces-redirect=true");
    }
    
    /**
     * Creates a new instance of MantenedorPerfilUsuarioMB
     */
    public MantenedorPerfilUsuarioMB() {
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

    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    public String getPasswordNew2() {
        return passwordNew2;
    }

    public void setPasswordNew2(String passwordNew2) {
        this.passwordNew2 = passwordNew2;
    }
    
}
