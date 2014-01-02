/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author victor
 */
public class SelectElemPojo {
    private String label;
    private String id;

    public SelectElemPojo() {
    }
    
    public SelectElemPojo(String id, String label) {
        this.label = label;
        this.id = id;
    }
    
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
