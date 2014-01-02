/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans.SerialInterface;

import ConexionSerial.ConectorSerial;
import entities.Dispositivo;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author victor
 */
@Singleton
@Startup
public class ConectionArduino implements ConectionArduinoLocal {
    private List<ConectorSerial> conexionesArduinos;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ConectionArduino() {
        conexionesArduinos = new LinkedList<ConectorSerial>();
    }
    
    @PostConstruct 
    public void init() { 
       Logger.getLogger(getClass().getName()).log(Level.INFO, "Iniciando conector serial");
    }
    
    @PreDestroy 
    public void cleanup() {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Cerrando conector serial para todos los arduino");
        for(ConectorSerial c : conexionesArduinos) {
            try {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Cerrando el puerto serial");
                c.close();
            }
            catch (Exception e) {
                Logger.getLogger(getClass().getName()).log(Level.INFO, "Error al cerrar el puerto serial");
            }
        }
    }
    
    @Override
    public void accionar(Dispositivo disp, int valor) {
        ConectorSerial conexionArduino;
        conexionArduino = getConexionArduino(disp.getArduino().getId());
        
        if (conexionArduino == null) {
            conexionArduino = new ConectorSerial();
            List<String> puertosDisp = conexionArduino.searchForPorts();
            for(String puerto : puertosDisp) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Puerto serial disponible: {0}", puerto);
            }
            if (!conexionArduino.connect(disp.getArduino().getPuertoCOM())) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en {0}", disp.getArduino().getPuertoCOM());
                if (!puertosDisp.isEmpty()) {
                    boolean conectado = false;
                    for(String puerto : puertosDisp) {
                        if(conexionArduino.connect(puerto)) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Temporalmente se ha conectado en {0}", puerto);
                            conexionArduino.setIdArduino(disp.getArduino().getId());
                            configurarDispositivos(disp.getArduino().getDispositivos(), conexionArduino);
                            conexionesArduinos.add(conexionArduino);
                            conectado = true;
                            break;
                        }
                    }
                    if (!conectado) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en ningún puerto serial");
                    }
                 }
                
            }
            else {
                conexionArduino.setIdArduino(disp.getArduino().getId());
                configurarDispositivos(disp.getArduino().getDispositivos(), conexionArduino);
                conexionesArduinos.add(conexionArduino);
            }
        }
        
        if (conexionArduino.isConnected()) {
            //List<Integer> listaPines = disp.getPines();
            byte idDispInterno = (byte)disp.getIdInterno().intValue();
            
            byte[] datosRes = {'3', 2, idDispInterno,(byte)valor};
            //byte[] datosRes = {'3', 2, 1, 1};
            conexionArduino.sendMessage(datosRes);
        }
        else {
            conexionesArduinos.remove(conexionArduino);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible enviar la orden al arduino");
        }
        
    }
    
    @Override
    public int consultar(Dispositivo disp) {
        ConectorSerial conexionArduino;
        conexionArduino = getConexionArduino(disp.getArduino().getId());
        
        if (conexionArduino == null) {
            conexionArduino = new ConectorSerial();
            List<String> puertosDisp = conexionArduino.searchForPorts();
            for(String puerto : puertosDisp) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Puerto serial disponible: {0}", puerto);
            }
            if (!conexionArduino.connect(disp.getArduino().getPuertoCOM())) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en {0}", disp.getArduino().getPuertoCOM());
                if (!puertosDisp.isEmpty()) {
                    boolean conectado = false;
                    for(String puerto : puertosDisp) {
                        if(conexionArduino.connect(puerto)) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Temporalmente se ha conectado en {0}", puerto);
                            conexionArduino.setIdArduino(disp.getArduino().getId());
                            configurarDispositivos(disp.getArduino().getDispositivos(), conexionArduino);
                            conexionesArduinos.add(conexionArduino);
                            conectado = true;
                            break;
                        }
                    }
                    if (!conectado) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en ningún puerto serial");
                    }
                 }
                
            }
            else {
                conexionArduino.setIdArduino(disp.getArduino().getId());
                configurarDispositivos(disp.getArduino().getDispositivos(), conexionArduino);
                conexionesArduinos.add(conexionArduino);
            }
        }
        
        if (conexionArduino.isConnected()) {
            //List<Integer> listaPines = disp.getPines();
            if (!disp.isConfigurado()) {
                configurarDispositivo(disp, conexionArduino);
            }
            byte idDispInterno = (byte)disp.getIdInterno().intValue();
            
            byte[] datosRes = {ConectorSerial.TIPO_MSG_CONSULTA_DATOS, 1, idDispInterno};
            //byte[] datosRes = {'3', 2, 1, 1};
            conexionArduino.sendMessage(datosRes);
        }
        else {
            conexionesArduinos.remove(conexionArduino);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible enviar la orden al arduino");
        }
        return 0;
    }
    
    public void configurarDispositivos(List<Dispositivo> listaDispositivos, ConectorSerial conexionArduino) {
        System.out.println("Configurando "+ listaDispositivos.size() + " dispositivos");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConectionArduino.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Dispositivo disp : listaDispositivos) {
            configurarDispositivo(disp, conexionArduino);
            int index = conexionArduino.getDispositivosManejados().indexOf(disp);
            if (index >= 0) {
                conexionArduino.getDispositivosManejados().get(index).setConfigurado(true);
            }
            else {
                disp.setConfigurado(true);
                conexionArduino.getDispositivosManejados().add(disp);
            }
        }
    }
    
    public void configurarDispositivo(Dispositivo disp, ConectorSerial conexionArduino) {
            byte idDispInterno = (byte)disp.getIdInterno().intValue();
            byte idTipo = (byte)disp.getTipo().getId().byteValue();
            byte pin1 = (byte)disp.getPines().get(0).intValue(); //ARREGLAR PARA WEAS DE MÁS PINES
            System.out.println("Configurando idDisp: "+idDispInterno + " idTipo: "+idTipo+ " pin: " + pin1);
            
            byte[] datosConfig = new byte[1+1+1+1+disp.getPines().size()+1+disp.getConfiguraciones().size()];
            int i = 0;
            datosConfig[i++] = ConectorSerial.TIPO_MSG_MAPEO_PINES;
            datosConfig[i++] = (byte)(datosConfig.length-2);
            datosConfig[i++] = idDispInterno;
            datosConfig[i++] = idTipo;
            for (Integer pin : disp.getPines()) {
                datosConfig[i++] = (byte)pin.intValue();
            }
            datosConfig[i++] = (byte)disp.getConfiguraciones().size();
            for (Integer config : disp.getConfiguraciones()) {
                datosConfig[i++] = (byte)config.intValue();
            }
            
            //byte[] datosConfig = {ConectorSerial.TIPO_MSG_MAPEO_PINES, (byte)(3+disp.getPines().size()), idDispInterno, idTipo, pin1, 0};
            //byte[] datosConfig = {'1', 4, 1, 51, 13, 0};
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConectionArduino.class.getName()).log(Level.SEVERE, null, ex);
            }
            conexionArduino.sendMessage(datosConfig);
    }
    
    public ConectorSerial getConexionArduino(Integer idArduino) {
        ConectorSerial res = null;
        for(ConectorSerial c : conexionesArduinos ) {
            if (c.getIdArduino().intValue() == idArduino.intValue())
                return c;
        }
        return res;
    }
}
