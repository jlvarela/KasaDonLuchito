/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.CondicionDisparador;
import entities.Disparador;
import entities.Dispositivo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import sessionBeans.SerialInterface.ConectionArduinoLocal;

/**
 *
 * @author victor
 */
@Singleton
@Startup
public class DisparadorExecutor implements DisparadorExecutorLocal {
    @EJB
    private EscenaFacadeLocal escenaFacade;
    @EJB
    private DispositivoFacadeLocal dispositivoFacade;
    @EJB
    private DisparadorFacadeLocal disparadorFacade;
    @EJB
    private ConectionArduinoLocal conectionArduino;
    
    private List<Disparador> listaDisparadores;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public DisparadorExecutor() {
        
    }
    
    @PostConstruct
    public void init() {
        listaDisparadores = disparadorFacade.findAll();
    }

    @Override
    public void comprobarCondicionesDisparadores(Dispositivo dispositivoCambiado) {
        System.out.println("Ha ocurrido un cambio en el dispositivo con id: "+dispositivoCambiado.getId() + " su valorNewHW y SW es: "+dispositivoCambiado.getValorHW()+ " "+dispositivoCambiado.getValorSW());
        int valorOld, valorNew;
        valorOld = dispositivoCambiado.getValorSWOld();
        valorNew = dispositivoCambiado.getValorSW();
        boolean todasCumplidas;
        Dispositivo dtemp;
        
        //Compruebo condiciones
        for(Disparador disparador : listaDisparadores) {
            System.out.println("Comprobando disparador con id: "+disparador.getId());
            todasCumplidas = true;
            disparador.setActuarAhora(false);
            for(CondicionDisparador cond : disparador.getCondiciones()) {
                System.out.println("Comprobando condición con id: "+cond.getId());
                dtemp = conectionArduino.buscarDispositivoById(cond.getDispositivo().getId().intValue());
                valorNew = dtemp.getValorSW();
                valorOld = dtemp.getValorSWOld();
                cond.setCumplida(seCumpleCondicion(cond, valorOld, valorNew));
                
                if (!cond.isCumplida()) {
                    todasCumplidas = false;
                }
            }
            if (todasCumplidas) {
                System.out.println("Se cumplen todas las condiciones, marcando para actuar...");
                disparador.setActuarAhora(true);
            }
            else {
                System.out.println("No se cumplieron las condiciones");
            }
        }
        
        //Actualizar valores de dispositivos
        conectionArduino.oneStepTimeValorDispositivos();
        
        //Realizo acciones
        for(Disparador disparador : listaDisparadores) {
            if (disparador.isActuarAhora()) {
                disparador.setActuarAhora(false);
                System.out.println("Actuando lo que se marcó");
                hacerAccion(disparador);
            }
        }
        
        
    }
    
    private boolean seCumpleCondicion(CondicionDisparador cond, int valorOld, int valorAct) {
        String comp_op = cond.getComparador();
        String comp_op_old = cond.getComparadorOld();
        
        //Podrían ser int
        float valorActCondicion = cond.getValorNew();
        float valorOldCondicion = cond.getValorOld();
        
        System.out.println("Comprobando condición comp: "+comp_op + " comp_old: "+comp_op_old + " valorOldCond: "+ valorOldCondicion + " valorActCond: "+ valorActCondicion +" valorOld: "+valorOld+ " valorAct: "+valorAct);
        
        if (comp_op.equals(CondicionDisparador.IGUAL_QUE)) { //entonces el valor actual debe ser igual al valor del dispositivo
		if (valorActCondicion == valorAct) {
			
			if (comp_op_old.equals(CondicionDisparador.IGUAL_QUE)) {
				if (valorOld == valorOldCondicion) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MENOR_QUE)) {
				if (valorOldCondicion < valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MAYOR_QUE)) {
				if (valorOldCondicion > valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.DISTINTO_QUE)) {
				if (valorOld != valorOldCondicion) {
					return true;
				}
			}
		}
	}
	else if (comp_op.equals(CondicionDisparador.MAYOR_QUE)) { //entonces el valor actual debe ser mayor al valor del zenit
		if (valorActCondicion > valorAct) {
			if (comp_op_old.equals(CondicionDisparador.IGUAL_QUE)) {
				if (valorOld == valorOldCondicion) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MENOR_QUE)) {
				if (valorOldCondicion < valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MAYOR_QUE)) {
				if (valorOldCondicion > valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.DISTINTO_QUE)) {
				if (valorOld != valorOldCondicion) {
					return true;
				}
			}
		}
		
	}
	else if (comp_op.equals(CondicionDisparador.MENOR_QUE)) { //entonces el valor actual debe ser menor al valor del zenit
		if (valorActCondicion < valorAct) {
			if (comp_op_old.equals(CondicionDisparador.IGUAL_QUE)) {
				if (valorOld == valorOldCondicion) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MENOR_QUE)) {
				if (valorOldCondicion < valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MAYOR_QUE)) {
				if (valorOldCondicion > valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.DISTINTO_QUE)) {
				if (valorOld != valorOldCondicion) {
					return true;
				}
			}
		}
		
	}
	else if (comp_op.equals(CondicionDisparador.DISTINTO_QUE)) { //entonces el valor actual debe ser menor al valor del zenit
		if (valorActCondicion != valorAct) {
			if (comp_op_old.equals(CondicionDisparador.IGUAL_QUE)) {
				if (valorOld == valorOldCondicion) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MENOR_QUE)) {
				if (valorOldCondicion < valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.MAYOR_QUE)) {
				if (valorOldCondicion > valorOld) {
					return true;
				}
			}
			else if (comp_op_old.equals(CondicionDisparador.DISTINTO_QUE)) {
				if (valorOld != valorOldCondicion) {
					return true;
				}
			}
		}
		
	}
        
        return false;
    }
    
    private void hacerAccion(Disparador d) {
        try {
            if (d.getEscenaQueAcciona() != null) {
                escenaFacade.accionar(d.getEscenaQueAcciona().getId());
            }
            else if (d.getDispositivoQueAcciona() != null) {
                dispositivoFacade.accion(d.getDispositivoQueAcciona().getId(), d.getValorAccionDispositivo());
            }
            else {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Disparador no ha podido ser ejecutado, no acción ya que todo vale null");
            }
        }
        catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Disparador no ha podido ser ejecutado, excepción al realizar la acción");
        }
        
    }
    
    @Override
    public void eliminarDisparador(Disparador d) {
        boolean encontrado = listaDisparadores.remove(d);
        //Se elimina el scheduler existente
        if (!encontrado) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Disparador no ha podido ser eliminado, no se encontró disparador con id: {0}", d.getId());
        }
        
    }
    
    @Override
    public void modificarDisparador(Disparador d) {
        eliminarDisparador(d);
        
        agregarDisparador(d);
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Disparador modificado con id: {0}", d.getId());
    }

    @Override
    public void agregarDisparador(Disparador d) {
        if (!listaDisparadores.contains(d)) {
            listaDisparadores.add(d);
        }
        else {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Disparador que se intentaba agregar ya existía con id: {0}", d.getId());
        }
    
    }
}
