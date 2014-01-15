/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Timer;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

/**
 *
 * @author victor
 */
@Singleton
@Startup
public class TimerExecutor implements TimerExecutorLocal {
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    @EJB
    private TimerFacadeLocal timerFacade;
    @Resource
    TimerService servicioTemporizador;
    
    private List<Timer> timers;
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public TimerExecutor() {
        timers = new LinkedList<Timer>();
    }
    
    @PostConstruct
    public void init() {
        List<Timer> timersTemp = timerFacade.findAll();
        
        //Se crean los scheduler por cada timer
        for(Timer t : timersTemp) {
            agregarTimer(t);
        }
    
    
    }
    
    @PreDestroy
    public void eliminarTimers() {
        Collection<javax.ejb.Timer> temporizadores = servicioTemporizador.getTimers();
        for(javax.ejb.Timer t : temporizadores) {
            t.cancel();
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Todos los timers eliminados");
    }
    
    private String getDaysOfWeek(Timer t) {
        StringBuilder dias = new StringBuilder();
        boolean primero = false;
        if (t.isRepetirLunes()) {
            dias.append("1");
            primero = true;
        }
        if (t.isRepetirMartes()) {
            if (primero)
                dias.append(",");
            dias.append("2");
            primero = true;
        }
        if (t.isRepetirMiercoles()) {
            if (primero)
                dias.append(",");
            dias.append("3");
            primero = true;
        }
        if (t.isRepetirJueves()) {
            if (primero)
                dias.append(",");
            dias.append("4");
            primero = true;
        }
        if (t.isRepetirViernes()) {
            if (primero)
                dias.append(",");
            dias.append("5");
            primero = true;
        }
        if (t.isRepetirSabado()) {
            if (primero)
                dias.append(",");
            dias.append("6");
            primero = true;
        }
        if (t.isRepetirDomingo()) {
            if (primero)
                dias.append(",");
            dias.append("7");
        }
        return dias.toString().trim();
    }
    
    @Override
    public void agregarTimer(Timer t) {
        
        timers.add(t);
        
        
        Calendar horaCalendar = Calendar.getInstance();
        horaCalendar.setTime(t.getHora());
        int minuto = horaCalendar.get(Calendar.MINUTE);
        int hora = horaCalendar.get(Calendar.HOUR_OF_DAY);

        //Se crea el scheduler

        ScheduleExpression schedExpr = new ScheduleExpression();
        schedExpr.minute(minuto);
        schedExpr.hour(hora);
        
        //Entonces es un timer único, sin repetición por días
        boolean ejecucionUnica = false;
        String diasStr = getDaysOfWeek(t);
        if (diasStr.isEmpty()) {
            ejecucionUnica = true;
        }
        if (ejecucionUnica) {
            servicioTemporizador.createSingleActionTimer(t.getHora(), new TimerConfig(t.getId().toString(), true));
            
        }
        else {
            schedExpr.dayOfWeek(diasStr);
            servicioTemporizador.createCalendarTimer(schedExpr, new TimerConfig(t.getId().toString(), true));
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Timer agregado con id: {0}", t.getId());
    }
    
    @Override
    public void modificarTimer(Timer t) {
        eliminarTimer(t);
        
        agregarTimer(t);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Timer modificado con id: {0}", t.getId());
    }
    
    @Override
    public void eliminarTimer(Timer t) {
        timers.remove(t);
        boolean encontrado = false;
        //Se elimina el scheduler existente
        Collection<javax.ejb.Timer> temporizadores = servicioTemporizador.getTimers();
        for(javax.ejb.Timer tim : temporizadores) {
            if (((String)tim.getInfo()).equals(t.getId().toString())) {
                tim.cancel();
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Temporizador cancelado para timer con id: {0}", t.getId());
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Timer no ha podido ser eliminado, no se encontró temporizador asociado con id: {0}", t.getId());
        }
        
    }
    
    @Timeout
    public void ejecutartimerMask(javax.ejb.Timer timer) {
        Timer t = null;
        String idTimer = (String)(timer.getInfo());
        for (Timer tTemp : timers) {
            if (tTemp.getId().toString().equals(idTimer)) {
                t = tTemp;
                break;
            }
        }
        if (t != null) {
            ejecutarTimer(t);
        }
        else {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al ejecutar el timer, no existe Timer asociado al temporizador");
        }
    }
    
    
    private void ejecutarTimer(Timer t) {
        if (getDaysOfWeek(t).isEmpty()) {
            t.setActivo(false); //Se desactiva luego de ejecutarse
            timerFacade.edit(t);
        }
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Ejecutando timer con id: {0}", t.getId());
        try {
            if ((t.getDispositivoQueAcciona() != null) && (t.getValorAccionDispositivo() != null)) {
                dispositivoFacade.accion(t.getDispositivoQueAcciona().getId(), t.getValorAccionDispositivo());
            }
            else if (t.getEscenaQueAcciona() != null) {
                escenaFacade.accionar(t.getEscenaQueAcciona().getId().intValue());
            }
            else {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al ejecutar el timer, no existe acción asociada a una escena o dispositivo ");
            }
        }
        catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error al ejecutar el timer: {0}", e.getMessage());
            
        }
    }

}
