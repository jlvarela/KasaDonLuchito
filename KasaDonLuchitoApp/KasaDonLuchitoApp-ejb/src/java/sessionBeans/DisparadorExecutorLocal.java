/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Disparador;
import entities.Dispositivo;
import javax.ejb.Local;

/**
 *
 * @author victor
 */
@Local
public interface DisparadorExecutorLocal {
    public void agregarDisparador(Disparador d);

    public void modificarDisparador(Disparador d);

    public void eliminarDisparador(Disparador d);

    public void comprobarCondicionesDisparadores(Dispositivo dispositivoCambiado);
}
