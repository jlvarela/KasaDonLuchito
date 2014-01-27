/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans.SerialInterface;

import entities.Dispositivo;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface ConectionArduinoLocal {

    public void accionar(Dispositivo disp, int valor);

    public int consultar(Dispositivo disp);

    public void eliminar(Dispositivo disp);
    
}
