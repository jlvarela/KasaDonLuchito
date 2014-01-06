/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Dispositivo;
import entities.Escena;
import entities.Timer;
import entities.Usuario;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class TimerFacade extends AbstractFacade<Timer> implements TimerFacadeLocal {
    @EJB
    private TimerExecutorLocal timerExecutor;
    @EJB
    private UsuarioFacadeLocal usuarioFacade;
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TimerFacade() {
        super(Timer.class);
    }
    
    @Override
    public void eliminarTimer(Timer t) {
        timerExecutor.eliminarTimer(t);
        remove(t);
    }
    
    @Override
    public void crearTimer(String nombre, Date hora, List<Integer> dias, 
            boolean accionaEscena, Integer idDispSeleccionado, Integer valorAccionDispositivo, 
            Integer idEscenaSeleccionada, String usernameCreador) throws Exception {
        if ((accionaEscena) && (idEscenaSeleccionada == null)) {
            throw new Exception("No ha seleccionado una escena para accionar");
        }
        if ((!accionaEscena) && (idDispSeleccionado == null)) {
            throw new Exception("No ha seleccionado un dispositivo para accionar");
        }
        if ((!accionaEscena) && (valorAccionDispositivo == null)) {
            throw new Exception("No ha seleccionado un valor que darle al dispositivo a accionar");
        }
        if (usernameCreador == null) {
            throw new Exception("El timer debe ser creado por un usuario, no ha especificado uno");
        }
        Usuario user = usuarioFacade.findByUsername(usernameCreador);
        if (user == null) {
            throw new Exception("El timer debe ser creado por un usuario válido, no se ha encontrado el usuario");
        }
        
        Timer t = new Timer();
        t.setNombre(nombre);
        t.setActivo(true);
        if (accionaEscena) {
            Escena escenaAccionada = escenaFacade.find(idEscenaSeleccionada);
            t.setEscenaQueAcciona(escenaAccionada);
            t.setValorAccionDispositivo(null);
            t.setDispositivoQueAcciona(null);
        }
        else {
            Dispositivo d = dispositivoFacade.find(idDispSeleccionado);
            t.setValorAccionDispositivo(valorAccionDispositivo);
            t.setDispositivoQueAcciona(d);
            t.setEscenaQueAcciona(null);
        }
        t.setHora(hora);
        t.setUsuarioCreador(user);
        
        //Se setea en que días que ejecutará el timer
        if (dias.contains(REPETIR_LUNES)) {
            t.setRepetirLunes(true);
        }
        if (dias.contains(REPETIR_MARTES)) {
            t.setRepetirMartes(true);
        }
        if (dias.contains(REPETIR_MIERCOLES)) {
            t.setRepetirMiercoles(true);
        }
        if (dias.contains(REPETIR_JUEVES)) {
            t.setRepetirJueves(true);
        }
        if (dias.contains(REPETIR_VIERNES)) {
            t.setRepetirViernes(true);
        }
        if (dias.contains(REPETIR_SABADO)) {
            t.setRepetirSabado(true);
        }
        if (dias.contains(REPETIR_DOMINGO)) {
            t.setRepetirDomingo(true);
        }
        
        create(t);
        em.flush();
        em.refresh(t);
        
        //Avisar al managed beans que ejecuta los timers
        timerExecutor.agregarTimer(t);
        
    }
    
}
