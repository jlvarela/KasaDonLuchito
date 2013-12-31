/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Arduino;
import entities.Dispositivo;
import entities.TipoDispositivo;
import entities.TipoUsuario;
import entities.Usuario;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victor
 */
@Stateless
public class PobladoInicial implements PobladoInicialLocal {
    @PersistenceContext(unitName = "KasaDonLuchitoApp-ejbPU")
    private EntityManager em;
    
    private List<TipoUsuario> tiposDeUsuarios;
    private List<TipoDispositivo> tiposDeDispositivos;
    private List<Dispositivo> dispositivos;

    @Override
    public void poblarDBTesting() {
        poblarDBProduction();
    }
    
    @Override
    public void poblarDBProduction() {
        genererarTiposDispositivos();
        generarTiposUsuarios();
        
        Arduino arduino = new Arduino();
        arduino.setNombre("Default");
        arduino.setPuertoCOM("COM9");
        
        Dispositivo d1 = new Dispositivo();
        d1.setArduino(arduino);
        arduino.getDispositivos().add(d1);
        d1.setNombre("Luz pin13");
        d1.setIdInterno(1);
        d1.setValor(0);
        d1.setTipo(tiposDeDispositivos.get(4));
        d1.getPines().add(new Integer(13));
        
        Usuario user = new Usuario();
        user.setUsername("admin");
        user.setTipoUsuario(tiposDeUsuarios.get(0));
        user.setPassword(getMd5("admin"));
        
        persist(user);
        persist(arduino);
        persist(d1);
        
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
    
    public List<TipoDispositivo> genererarTiposDispositivos() {
        tiposDeDispositivos = new LinkedList<TipoDispositivo>();
        TipoDispositivo td = new TipoDispositivo();
        td.setId(1);
        td.setNombre("Sensor on/off digital");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivos.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(2);
        td.setNombre("Sensor análogo genérico");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setRangoValores(true);
        tiposDeDispositivos.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(3);
        td.setNombre("Sensor análogo digitalizado on/off");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivos.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(4);
        td.setNombre("Sensor proximidad ultrasonido HR-S04");
        td.setSensor(true);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(300);
        td.setRangoValores(true);
        tiposDeDispositivos.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(51);
        td.setNombre("Actuador on/off");
        td.setSensor(false);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(1);
        td.setRangoValores(false);
        tiposDeDispositivos.add(td);
        persist(td);
        
        td = new TipoDispositivo();
        td.setId(52);
        td.setNombre("Actuador dimmer");
        td.setSensor(false);
        td.getValoresPosibles().add(0);
        td.getValoresPosibles().add(255);
        td.setRangoValores(true);
        tiposDeDispositivos.add(td);
        persist(td);
        
        return tiposDeDispositivos;
    }
    
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
