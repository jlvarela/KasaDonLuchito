/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans.SerialInterface;

import ConexionSerial.ConectorSerial;
import entities.Arduino;
import entities.Dispositivo;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import sessionBeans.ArduinoFacadeLocal;
import sessionBeans.DisparadorExecutorLocal;

/**
 *
 * @author victor
 */
@Singleton
@Startup
public class ConectionArduino implements ConectionArduinoLocal {
    @EJB
    private DisparadorExecutorLocal disparadorExecutor;
    @EJB
    private ArduinoFacadeLocal arduinoFacade;
    
    private List<ConectorSerial> conexionesArduinos;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ConectionArduino() {
        conexionesArduinos = new LinkedList<ConectorSerial>();
    }
    
    @PostConstruct 
    public void init() { 
       Logger.getLogger(getClass().getName()).log(Level.INFO, "Iniciando conector serial");
       List<Arduino> arduinos = arduinoFacade.findAll();
       for(Arduino a : arduinos) {
           conectarArduino(a.getPuertoCOM(), a.getId(), a.getDispositivos());
       }
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
    public void eliminar(Dispositivo disp) {
        ConectorSerial conexionArduino;
        conexionArduino = getConexionArduino(disp.getArduino().getId());
        
        if (conexionArduino != null) {
            if (conexionArduino.isConnected()) {
                byte idDispInterno = (byte)disp.getIdInterno().intValue();
                byte[] datosRes = {ConectorSerial.TIPO_MSG_ELIMINAR_DISPOSITIVO, 1, idDispInterno};
                System.out.println("Enviando mensaje de eliminación de dispositivo");
                conexionArduino.sendMessage(datosRes);
            }
            else {
                conexionesArduinos.remove(conexionArduino);
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible enviar la orden al arduino");
            }
            conexionArduino.getDispositivosManejados()[disp.getIdInterno()] = null; //Se elimina del array en memoria
        }
    }
    
    private ConectorSerial conectarArduino(String puertoCom, Integer idArduino, List<Dispositivo> dispositivosDelArduino) {
        ConectorSerial conexionArduino;
        conexionArduino = new ConectorSerial();
        List<String> puertosDisp = conexionArduino.searchForPorts();
        for(String puerto : puertosDisp) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Puerto serial disponible: {0}", puerto);
        }
        if (!conexionArduino.connect(puertoCom)) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en {0}", puertoCom);
            /*
            if (!puertosDisp.isEmpty()) {
                boolean conectado = false;
                
                //POR AHORA NO SE INTENTA CONECTAR A OTRO PUERTO COM PARA EVITAR CONECTAR 2 VECES EL MISMO ARDUINO
                for(String puerto : puertosDisp) {
                    if(conexionArduino.connect(puerto)) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Temporalmente se ha conectado en {0}", puerto);
                        conexionArduino.setIdArduino(idArduino);
                        configurarDispositivos(dispositivosDelArduino, conexionArduino);
                        conexionesArduinos.add(conexionArduino);
                        conectado = true;
                        break;
                    }
                }
                
                if (!conectado) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "No ha sido posible conectar el arduino en ningún puerto serial");
                }
             }
             */
        }
        else {
            conexionArduino.setIdArduino(idArduino);
            configurarDispositivos(dispositivosDelArduino, conexionArduino);
            conexionesArduinos.add(conexionArduino);
        }
        return conexionArduino;
    }
    
    /*
     * Actualiza los valores new y old de los dispositivos
     */
    @Override
    public void oneStepTimeValorDispositivos() {
        for(ConectorSerial con : conexionesArduinos) {
            for (Dispositivo d : con.getDispositivosManejados()) {
                if (d != null) {
                    d.setValorHWOld(d.getValorHW());
                }
            }
        }
    }
    
    @Override
    public Dispositivo buscarDispositivoById(int idDisp) {
        for(ConectorSerial con : conexionesArduinos) {
            for (Dispositivo d : con.getDispositivosManejados()) {
                if (d != null) {
                    if (d.getId().intValue() == idDisp) {
                        return d;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Dispositivo buscarDispositivoByIdInterno(int idDispInterno) {
        for(ConectorSerial con : conexionesArduinos) {
            for (Dispositivo d : con.getDispositivosManejados()) {
                if (d != null) {
                    if (d.getIdInterno().intValue() == idDispInterno) {
                        return d;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public void accionar(int idDisp, int valor) {
        Dispositivo disp;
        disp = buscarDispositivoById(idDisp);
        if (disp == null) {
            return;
        }
        disp.setValorHW(valor);
        
        ConectorSerial conexionArduino;
        conexionArduino = getConexionArduino(disp.getArduino().getId());
        
        if (conexionArduino == null) {
            conexionArduino = conectarArduino(disp.getArduino().getPuertoCOM(), disp.getArduino().getId(), disp.getArduino().getDispositivos());
        }
        
        if (conexionArduino.isConnected()) {
            //List<Integer> listaPines = disp.getPines();
            byte idDispInterno = (byte)disp.getIdInterno().intValue();
            
            byte[] datosRes = {ConectorSerial.TIPO_MSG_ACCION_DISPOSITIVO, 2/*largomsg*/, idDispInterno,(byte)valor};
            //byte[] datosRes = {'3', 2, 1, 1};
            conexionArduino.sendMessage(datosRes);
            
            Dispositivo dispositivo = conexionArduino.modificarValorDispositivo(idDisp, valor);
            if (dispositivo != null)
                disparadorExecutor.comprobarCondicionesDisparadores(dispositivo); //Se avisa al ejecutor de disparadores
            
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
            conexionArduino = conectarArduino(disp.getArduino().getPuertoCOM(), disp.getArduino().getId(), disp.getArduino().getDispositivos());
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
            Dispositivo index = conexionArduino.getDispositivosManejados()[disp.getIdInterno()];
            if (index != null) {
                conexionArduino.getDispositivosManejados()[disp.getIdInterno()].setConfigurado(true);
            }
            else {
                disp.setConfigurado(true);
                conexionArduino.getDispositivosManejados()[disp.getIdInterno()] = disp;
            }
        }
    }
    
    public void configurarDispositivo(Dispositivo disp, ConectorSerial conexionArduino) {
            byte idDispInterno = (byte)disp.getIdInterno().intValue();
            byte idTipo = (byte)disp.getTipo().getTipoDispositivo().getId().intValue();
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
