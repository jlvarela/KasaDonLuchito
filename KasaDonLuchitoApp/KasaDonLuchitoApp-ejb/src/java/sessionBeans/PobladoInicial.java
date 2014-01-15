/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import entities.Configuracion;
import entities.Dispositivo;
import entities.Timer;
import entities.TipoDispositivo;
import entities.TipoDispositivoUserLevel;
import entities.TipoUsuario;
import entities.Usuario;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class PobladoInicial implements PobladoInicialLocal {
    @EJB
    private TimerExecutorLocal timerExecutor;
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;
    
    private List<TipoUsuario> tiposDeUsuarios;
    private List<TipoDispositivo> tiposDeDispositivosHardware;
    private List<TipoDispositivoUserLevel> tiposDeDispositivosUserLevel;
    private List<Dispositivo> dispositivos;

    @Override
    public void poblarDBTesting() {
        poblarDBProduction();
    }
    
    @Override
    public void poblarDBProduction() {
        genererarTiposDispositivosHardware();
        generarTiposDispositivosUserLevel();
        generarTiposUsuarios();
        
        Arduino arduino = new Arduino();
        arduino.setNombre("Default");
        arduino.setPuertoCOM("COM9");
        
        Dispositivo d1 = new Dispositivo();
        d1.setArduino(arduino);
        arduino.getDispositivos().add(d1);
        d1.setNombre("Luz pin13");
        d1.setIdInterno(1);
        d1.setTipo(tiposDeDispositivosUserLevel.get(4));
        d1.setValorSW(0);
        d1.getPines().add(new Integer(13));
        
        Dispositivo d2 = new Dispositivo();
        d2.setArduino(arduino);
        arduino.getDispositivos().add(d2);
        d2.setNombre("Luz dimmer pin9");
        d2.setIdInterno(2);
        d2.setTipo(tiposDeDispositivosUserLevel.get(5));
        d2.setValorSW(0);
        d2.getPines().add(new Integer(9));
        
        dispositivos = new LinkedList<Dispositivo>();
        dispositivos.add(d1);
        dispositivos.add(d2);
        
        Usuario user = new Usuario();
        user.setUsername("admin");
        user.setTipoUsuario(tiposDeUsuarios.get(0));
        user.setPassword(getMd5("admin"));
        
        Timer t = new Timer();
        t.setNombre("timer de prueba");
        t.setActivo(true);
        t.setDispositivoQueAcciona(d1);
        t.setEscenaQueAcciona(null);
        t.setValorAccionDispositivo(1);
        t.setUsuarioCreador(user);
        Date hora = Calendar.getInstance().getTime();
        hora.setMinutes(hora.getMinutes()+1);
        t.setHora(hora);
        t.setRepetirLunes(true);
        t.setRepetirMartes(true);
        t.setRepetirMiercoles(true);
        t.setRepetirJueves(true);
        t.setRepetirViernes(true);
        t.setRepetirSabado(true);
        t.setRepetirDomingo(true);
        
        persist(user);
        persist(arduino);
        persist(d1);
        persist(d2);
        persist(t);
        em.flush();
        em.refresh(t);
        timerExecutor.agregarTimer(t);
        
        //Tirando configuraciones básicas
        Configuracion config = new Configuracion("fondo_app", "fondo3.jpg");
        persist(config);
        
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Realizado poblado inicial");
    }
    
    public String getMd5(String password) {
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("UTF-8"));

            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            password = bigInt.toString(16);
        }
        catch (Exception e) {
            //System.out.println("No se pudo convertir a MD5 la password");
        }
        return password;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<TipoDispositivo> genererarTiposDispositivosHardware() {
        tiposDeDispositivosHardware = new LinkedList<TipoDispositivo>();
        TipoDispositivo td = new TipoDispositivo();
        td.setId(1);
        td.setNombre("Sensor on/off digital");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(2);
        td.setNombre("Sensor análogo genérico");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setRangoValores(true);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(3);
        td.setNombre("Sensor análogo digitalizado on/off");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(4);
        td.setNombre("Sensor proximidad ultrasonido HR-S04");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setRangoValores(true);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(51);
        td.setNombre("Actuador on/off");
        td.setSensor(false);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(52);
        td.setNombre("Actuador dimmer");
        td.setSensor(false);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setRangoValores(true);
        tiposDeDispositivosHardware.add(td);
        persist(td);
        
        return tiposDeDispositivosHardware;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<TipoDispositivoUserLevel> generarTiposDispositivosUserLevel() {
        tiposDeDispositivosUserLevel = new LinkedList<TipoDispositivoUserLevel>();
        TipoDispositivoUserLevel td = new TipoDispositivoUserLevel();
        td.setNombre("Sensor on/off digital");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(0));
        td.setUnidad("");
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        td = new TipoDispositivoUserLevel();
        td.setNombre("Sensor de temperatura");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(1));
        td.getValoresPosibles().add(10);
        td.getValoresPosibles().add(50);
        td.setUnidad("°C");
        td.setRangoValores(true);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        td = new TipoDispositivoUserLevel();
        td.setNombre("Sensor de proximidad infrarojo");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(2)); //Sensor análogo digitalizado
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setUnidad("");
        td.setRangoValores(false);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        td = new TipoDispositivoUserLevel();
        td.setNombre("Sensor proximidad ultrasonido HR-S04");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(3));
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setUnidad("CM");
        td.setRangoValores(true);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        td = new TipoDispositivoUserLevel();
        td.setNombre("Actuador on/off");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(4));
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setUnidad("");
        td.setRangoValores(false);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        td = new TipoDispositivoUserLevel();
        td.setNombre("Actuador dimmer");
        td.setTipoDispositivo(tiposDeDispositivosHardware.get(5));
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(10);
        td.setUnidad("");
        td.setRangoValores(true);
        tiposDeDispositivosUserLevel.add(td);
        persist(td);
        
        return tiposDeDispositivosUserLevel;
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<TipoUsuario> generarTiposUsuarios() {
        tiposDeUsuarios = new LinkedList<TipoUsuario>();
        TipoUsuario tu = new TipoUsuario();
        tu.setNombreTipo("Administrador");
        tiposDeUsuarios.add(tu);
        persist(tu);
        
        tu = new TipoUsuario();
        tu.setNombreTipo("Usuario");
        tiposDeUsuarios.add(tu);
        persist(tu);
        
        return tiposDeUsuarios;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public void persist(Object object) {
        em.persist(object);
    }

}
