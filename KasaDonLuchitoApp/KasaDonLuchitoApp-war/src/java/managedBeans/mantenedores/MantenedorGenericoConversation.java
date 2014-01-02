/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.mantenedores;

import javax.inject.Named;
import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import managedBeans.AbstractConversation;

/**
 *
 * @author victor
 */
@Named(value = "mantenedorPuntoLimpioConversation")
@ConversationScoped
public class MantenedorGenericoConversation extends AbstractConversation implements Serializable {
    
    public static final int AGREGAR = 1;
    public static final int EDITAR = 2;
    public static final int VER = 3;
    
    private Integer idToEdit;
    private int state;
    
    /**
     * Creates a new instance of MantenedorGenericoConversation
     */
    public MantenedorGenericoConversation() {
    }
    
    public void limpiarDatos() {
    }
    
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Integer getIdToEdit() {
        return idToEdit;
    }

    public void setIdToEdit(Integer idToEdit) {
        this.idToEdit = idToEdit;
    }
    
}
