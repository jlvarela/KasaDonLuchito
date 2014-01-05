/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import entities.AccionEscena;
import entities.Escena;
import entities.Usuario;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.inject.Inject;
import otros.CommonFunctions;
import pojos.AccionPojo;
import pojos.EscenaPojo;
import pojos.UsuarioPojo;
import sessionBeans.EscenaFacadeLocal;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorEscenaDetallesMB")
@RequestScoped
public class MantenedorEscenaDetallesMB implements Serializable {
    @EJB
    private EscenaFacadeLocal escenaFacade;
    
    @Inject
    private MantenedorGenericoConversation mantEscenasConv;
    
    private String nombre;
    
    private List<UsuarioPojo> usuariosPemitidos;
    
    private List<AccionPojo> accionesEscena;

    @PostConstruct
    public void init() {
        Integer idEscena = mantEscenasConv.getIdToEdit();
        Escena escena = escenaFacade.find(idEscena);
        if (escena == null) {
            volverToLista();
            return;
        }
        
        nombre = escena.getNombre();
        
        List<Usuario> users = escena.getUsuariosPermitidos();
        UsuarioPojo userPojo;
        for(Usuario u : users) {
            userPojo = new UsuarioPojo();
            userPojo.setId(u.getId());
            userPojo.setLabel(u.getUsername());
            userPojo.setRol(u.getTipoUsuario().getNombreTipo());
            usuariosPemitidos.add(userPojo);
        }
        
        AccionPojo accionPojo;
        for(AccionEscena accionEscena : escena.getAccionesEscena()) {
            accionPojo = new AccionPojo();
            accionPojo.setIdDispositivo(accionEscena.getDispositivo().getId());
            accionPojo.setNombreDispositivo(accionEscena.getDispositivo().getNombre());
            accionPojo.setValor(accionEscena.getAccion());
            accionesEscena.add(accionPojo);
        }
    }
    
    public void volverToLista() {
        mantEscenasConv.endConversation();
        mantEscenasConv.limpiarDatos();
        CommonFunctions.goToPage("/faces/users/verEscenas.xhtml?faces-redirect=true");
    }
    
    /**
     * Creates a new instance of MantenedorUsuarioAgregarMB
     */
    public MantenedorEscenaDetallesMB() {
        usuariosPemitidos = new LinkedList<UsuarioPojo>();
        accionesEscena = new LinkedList<AccionPojo>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<UsuarioPojo> getUsuariosPemitidos() {
        return usuariosPemitidos;
    }

    public void setUsuariosPemitidos(List<UsuarioPojo> usuariosPemitidos) {
        this.usuariosPemitidos = usuariosPemitidos;
    }

    public List<AccionPojo> getAccionesEscena() {
        return accionesEscena;
    }

    public void setAccionesEscena(List<AccionPojo> accionesEscena) {
        this.accionesEscena = accionesEscena;
    }

}
