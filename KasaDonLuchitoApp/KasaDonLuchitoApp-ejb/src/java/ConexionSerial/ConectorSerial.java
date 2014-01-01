/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionSerial;

import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author victor
 */
public class ConectorSerial implements SerialPortEventListener {
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

    //some ascii values for for certain things
    final static int SPACE_ASCII = 32;
    final static int DASH_ASCII = 45;
    final static int NEW_LINE_ASCII = 10;
    
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
                System.out.println("SelectedPortIdentifier is null " + selectedPort);
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
            System.out.println("Puerto configurado");
            try {
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
            } catch (IOException ex) {
                Logger.getLogger(ConectorSerial.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
            }
            System.out.println("Arduino Conectado en "+selectedPort);
            initListener();
            return true;
        }
        catch (PortInUseException e)
        {
            System.out.println("Puerto en uso. "+ e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error al abrir puerto" + selectedPort+ ". "+ e.getMessage());
            e.printStackTrace();
        }
        connected = false;
        return false;
    }
    
    public boolean sendMessage(byte[] datosRes)
    {
        //return value for whether opening the streams is successful or not
        boolean successful = false;

        try {
            
            Thread.sleep(100);
            System.out.println("Escribiendo :"+datosRes.toString());
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
        if (evt.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                System.out.println("Error al esperar");
            }
            try
            {
                int cuantos;
                char tipoMsg;
                if (input.available() > 0) {
                    tipoMsg = (char)input.read();
                    if (tipoMsg == '0') {
                        System.out.print("Llegó mensaje de debug: ");
                    }
                    if (tipoMsg == '4') {
                        System.out.print("Llegó mensaje de confirmación de configuración: ");
                    }
                    if (tipoMsg == '5') {
                        System.out.print("Llegó mensaje de confirmación de acción: ");
                    }
                    if (tipoMsg == '6') {
                        System.out.print("Llegó mensaje de infoDispositivo: ");
                    }
                    cuantos = (int)input.read();
                    byte[] datosSalida = new byte[cuantos];
                    
                    int leidos = input.read(datosSalida, 0, cuantos);
                    while (leidos < cuantos) {
                        Thread.sleep(10);
                        datosSalida[leidos++] = (byte)input.read();
                    }
                    if (tipoMsg == '0') {
                        StringBuilder strBuild = new StringBuilder();
                        for (int i = 0; i < datosSalida.length; i++) {
                            strBuild.append((char)datosSalida[i]);
                        }
                        System.out.println(strBuild.toString());
                    }
                    else if (tipoMsg == '4'){
                        System.out.print("Confirmación config ID:"+(int)datosSalida[0]);
                    }
                    else if (tipoMsg == '5'){
                        System.out.print("Confirmación acción ID:"+(int)datosSalida[0]);
                    }
                    else if (tipoMsg == '6'){
                        System.out.print("ID:"+(int)datosSalida[0]+" valor:"+(int)datosSalida[1]);
                    }
                    else {
                        System.out.print("Tipo de mensaje desconocido: ");
                        for (int i = 0; i < datosSalida.length; i++) {
                            System.out.print((char)datosSalida[i]);
                        }
                    }
                    
                    System.out.println();
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: "+e.getMessage());
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
                //window.cboxPorts.addItem(curPort.getName());
                System.out.println("Agregando port: "+curPort.getName());
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
        searchForPorts();
    }
    
    
    
}
