/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionSerial;

import entities.Dispositivo;
import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import sessionBeans.DisparadorExecutorLocal;
import sessionBeans.DispositivoFacadeLocal;

/**
 *
 * @author victor
 */
public class ConectorSerial implements SerialPortEventListener {
    DisparadorExecutorLocal disparadorExecutor = lookupDisparadorExecutorLocal();
    DispositivoFacadeLocal dispositivoFacade = lookupDispositivoFacadeLocal();
    private Integer idArduino;
    private Enumeration ports = null;
    //map the port names to CommPortIdentifiers
    private HashMap portMap = new HashMap();

    //this is the object that contains the opened port
    private CommPortIdentifier selectedPortIdentifier = null;
    private SerialPort serialPort = null;

    //input and output streams for sending and receiving data
    public InputStream input = null;
    public OutputStream output = null;

    //just a boolean flag that i use for enabling
    //and disabling buttons depending on whether the program
    //is connected to a serial port or not
    private boolean connected = false;

    //the timeout value for connecting with the port
    final static int TIMEOUT = 2000;
    
    //Tipos de mensajes enviados al arduino
    public final static char TIPO_MSG_DEBUG = '0';
    public final static char TIPO_MSG_MAPEO_PINES = '1';
    public final static char TIPO_MSG_CONSULTA_DATOS = '2';
    public final static char TIPO_MSG_ACCION_DISPOSITIVO = '3';
    public final static char TIPO_MSG_ELIMINAR_DISPOSITIVO = '7';
    //Tipos de mensajes recibidos desde el arduino
    public final static char TIPO_MSG_CONFIG_REALIZADA = '4';
    public final static char TIPO_MSG_ACCION_REALIZADA = '5';
    public final static char TIPO_MSG_DATOS_SENSOR = '6';
    
    public final static int TAM_HEADER = 2;
    public final static int MAX_BODY = 126;
    public final static int TIEMPO_MAXIMO_REBOOT_MSG = 6000;
    public final static int MAX_CANTIDAD_DISPOSITIVOS = 20; //CAMBIARLO SI SE USA OTRO MODELO DE ARDUINO
    
    byte[] headerMsg = new byte[TAM_HEADER];
    byte[] bodyMsg = new byte[MAX_BODY+1];
    int indexHeader;
    int indexMsg;
    boolean headerReceived;
    boolean bodyReceived;
    boolean validHeader;
    boolean msgComplete;
    boolean recepcionComenzada;
    int tam_body_defined_in_header;
    long previousMillis = 0;
    long currentMillis = 0;
    
    Dispositivo[] dispositivosManejados;
    
    public void initListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean connect(String selectedPort)
    {
        selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

        CommPort commPort = null;

        try
        {
            //the method below returns an object of type CommPort
            System.out.println("Intentando abrir puerto");
            if (selectedPortIdentifier == null) {
                throw new Exception("El arduino no está conectado");
            }
            commPort = selectedPortIdentifier.open("TigerControlPanel", TIMEOUT);
            System.out.println("Puerto abierto");
            //the CommPort object can be casted to a SerialPort object
            serialPort = (SerialPort)commPort;
            serialPort.setSerialPortParams(115200,
            SerialPort.DATABITS_8,
            SerialPort.STOPBITS_1,
            SerialPort.PARITY_NONE);
            connected = true;
            try {
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
                Logger.getLogger(ConectorSerial.class.getName()).log(Level.INFO, null, "Arduino Conectado en "+selectedPort);
            } catch (IOException ex) {
                Logger.getLogger(ConectorSerial.class.getName()).log(Level.SEVERE, null, "Error al abrir un puerto serial en "+selectedPort);
            }
            initListener();
            return true;
        }
        catch (PortInUseException e)
        {
            System.out.println("Puerto en uso. "+ e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error al abrir puerto " + selectedPort+ ". Exception: "+ e.getMessage());
            
        }
        connected = false;
        return false;
    }
    
    public boolean sendMessage(byte[] datosRes)
    {
        //return value for whether opening the streams is successful or not
        boolean successful = false;

        try {
            
            Thread.sleep(50);
            //System.out.println("Escribiendo :"+datosRes.toString());
            output.write(datosRes);
            output.flush();
            successful = true;
            return successful;
        }
        catch (IOException e) {
            return successful;
        } catch (InterruptedException ex) {
            System.out.println("Error  "+ ex.getMessage());
            return successful;
        }
    }
    
    @Override
    public void serialEvent(SerialPortEvent evt) {
        byte inByte = 0;
        currentMillis = Calendar.getInstance().getTimeInMillis();
        


        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            //Se intenta recibir un header
            if (!headerReceived) {
                try {
                    inByte = (byte)input.read();
                } catch (IOException ex) {
                    Logger.getLogger(ConectorSerial.class.getName()).log(Level.SEVERE, null, ex);
                }
                recepcionComenzada = true;
                previousMillis = currentMillis;

                headerMsg[indexHeader++] = inByte;
                if (indexHeader >= TAM_HEADER) {
                    validHeader = validateHeader(headerMsg);
                    tam_body_defined_in_header = (int)headerMsg[1];
                    headerReceived = true;
                }
            }
            //Se intenta recibir el cuerpo del mensaje
            else if (!bodyReceived) {
                try {
                    inByte = (byte)input.read();
                } catch (IOException ex) {
                    Logger.getLogger(ConectorSerial.class.getName()).log(Level.SEVERE, null, ex);
                }
                recepcionComenzada = true;
                previousMillis = currentMillis;

                if (validHeader) {
                    bodyMsg[indexMsg++] = inByte;
                    if (indexMsg >= tam_body_defined_in_header) {
                        bodyMsg[indexMsg] = 0; //Caracter terminador de string
                        bodyReceived = true;
                        msgComplete = true;
                        llegoMensaje();
                        rebootVarsMsg();
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ConectorSerial.class.getName()).log(Level.SEVERE, null, ex);
                        }
                            
                    }
                }
            }

            //Se ha recibido tanto el header como el body
            else {
                rebootVarsMsg();
            }

        }
        //No hay bytes por leer
        else {
            if (currentMillis < previousMillis) {
                previousMillis = 0; //Se desbordó
            }
            if(currentMillis - previousMillis > TIEMPO_MAXIMO_REBOOT_MSG) {
                previousMillis = currentMillis;
                if (recepcionComenzada) {
                    System.out.println("Reiniciando buffer de msg por timeout");
                }

                rebootVarsMsg();
            }
        }
    }
    
    
    public List<String> searchForPorts()
    {
        List<String> listaPuertos = new LinkedList<String>();
        ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier curPort = (CommPortIdentifier)ports.nextElement();

            //get only serial ports
            if (curPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                //System.out.println("Agregando port: "+curPort.getName());
                portMap.put(curPort.getName(), curPort);
                listaPuertos.add(curPort.getName());
            }
        }
        return listaPuertos;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Integer getIdArduino() {
        return idArduino;
    }

    public void setIdArduino(Integer idArduino) {
        this.idArduino = idArduino;
    }

    public ConectorSerial() {
        dispositivosManejados = new Dispositivo[ConectorSerial.MAX_CANTIDAD_DISPOSITIVOS];
    }
    
    public void close() throws Exception{
        serialPort.removeEventListener();
        serialPort.close();
        input.close();
        output.close();
    }
    
    
    void rebootVarsMsg() {
        headerReceived = false;
        bodyReceived = false;
        indexHeader = 0;
        indexMsg = 0;
        msgComplete = false;
        validHeader = false;
        recepcionComenzada = false;
    }

    boolean validateHeader(byte[] header) {
        //Se valida que el tipo de mensaje es correcto
        if (((header[0] >= '4') && (header[0] <= '6')) || (header[0] <= '0')) {
            //Se comprueba el tamaño del body que sigue
            if (header[1] <= MAX_BODY) {
                return true;
            }
        }
        System.out.println("Llegó mensaje con header inválido");
        return false;
    }
    
    
    public void llegoMensaje() {
        int largo = headerMsg[1];
        if (headerMsg[0] == TIPO_MSG_DEBUG) {
            StringBuilder strBuild = new StringBuilder();
            for(int i = 0; i < largo; i++) {
                strBuild.append((char)bodyMsg[i]);
            }
            System.out.println("Ha llegado el mensaje de debug: "+strBuild.toString());
        }
        else if (headerMsg[0] == TIPO_MSG_CONFIG_REALIZADA) {
            int idConfirmadoConfiguracion = (int)bodyMsg[0];
            System.out.println("Ha llegado un mensaje de confirmación de configuración al dispositivo con id: "+idConfirmadoConfiguracion);
        }
        else if (headerMsg[0] == TIPO_MSG_ACCION_REALIZADA) {
            int idConfirmadoActuacion = (int)bodyMsg[0];
            System.out.println("Ha llegado un mensaje de confirmación de acción al dispositivo con id: "+idConfirmadoActuacion);
        }
        else if (headerMsg[0] == TIPO_MSG_DATOS_SENSOR) {
            int idSensor = (int)bodyMsg[0];
            int valorRecibido = (int)bodyMsg[1];
            if (valorRecibido < 0) {
                valorRecibido = valorRecibido+255;
            }
            //LO CORRECTO ES ENVIARLO A UNA COLA O MESSAGE DRIVEN BEAN
            Dispositivo dispositivo = modificarValorDispositivo(idSensor, valorRecibido);
            if (dispositivo != null)
                disparadorExecutor.comprobarCondicionesDisparadores(dispositivo); //Se avisa al ejecutor de disparadores
            
            /*
            Dispositivo d = dispositivoFacade.findByIdInterno(idSensor);
            d.setValorHW(valorRecibido);
            dispositivoFacade.edit(d);
            */
            System.out.println("Ha llegado un mensaje de datos del dispositivo con id: "+idSensor + " con valor: "+valorRecibido);
        }
        else {
            System.out.println("Ha llegado un mensaje desconocido con header" + (char)headerMsg[0]);
            StringBuilder strBuild = new StringBuilder();
            for(int i = 0; i < largo; i++) {
                strBuild.append((char)bodyMsg[i]);
            }
            System.out.println("Se ha intentado interpretar como: "+strBuild.toString());
        }
        
    }
    
    public Dispositivo modificarValorDispositivo(int idSensor, int valorRecibido) {
        for(Dispositivo d : dispositivosManejados) {
            if (d != null) {
                if (d.getIdInterno().intValue() == idSensor) {
                    d.setValorHW(valorRecibido);
                    return d;
                }
            }
        }
        return null;
    }

    public Dispositivo[] getDispositivosManejados() {
        return dispositivosManejados;
    }

    private DispositivoFacadeLocal lookupDispositivoFacadeLocal() {
        try {
            Context c = new InitialContext();
            return (DispositivoFacadeLocal) c.lookup("java:global/KasaDonLuchitoApp/KasaDonLuchitoApp-ejb/DispositivoFacade!sessionBeans.DispositivoFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private DisparadorExecutorLocal lookupDisparadorExecutorLocal() {
        try {
            Context c = new InitialContext();
            return (DisparadorExecutorLocal) c.lookup("java:global/KasaDonLuchitoApp/KasaDonLuchitoApp-ejb/DisparadorExecutor!sessionBeans.DisparadorExecutorLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
