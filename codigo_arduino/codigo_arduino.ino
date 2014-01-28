#include <Ultrasonic.h>


#define MAX_BODY 126
#define TAM_HEADER 2
#define TAM_BODY headerMsg[1]
#define TIEMPO_MAXIMO_REBOOT_MSG 6000

//Tipos de mensajes aceptados desde el computador
#define TIPO_MSG_MAPEO_PINES '1'
#define TIPO_MSG_CONSULTA_DATOS '2'
#define TIPO_MSG_ACCION_DISPOSITIVO '3'
#define TIPO_MSG_ELIMINAR_DISPOSITIVO '7'
//Tipos de mensajes enviados al computador
#define TIPO_MSG_CONFIG_REALIZADA '4'
#define TIPO_MSG_ACCION_REALIZADA '5'
#define TIPO_MSG_DATOS_SENSOR '6'

#define TIPO_MSG_DEBUG '0'

//Cambiar estas constantes si se compila para arduino distinto a UNO
#define CANTIDAD_PINES_DIGITALES 13
#define CANTIDAD_PINES_ANALOGOS 6
unsigned char PINES_PWM[CANTIDAD_PINES_ANALOGOS] = {3, 5, 6, 9, 10, 11};


//Defino los tipos de dispositivos sensores (En un rango de 1 a 50)
#define SENSOR_BTN_DIGITAL 1
#define SENSOR_ANALOG_GENERICO 2
#define SENSOR_ANALOG_GENERICO_DIGITALIZADOR 3
#define SENSOR_PROXIMIDAD_ULTRASONIDO 4

//Defino los tipos de dispositivos actuadores (En un rango de 51 a 100)
#define ACTUADOR_ON_OFF 51
#define ACTUADOR_DIMMER 52

#define MAX_PINES_POR_DISPOSITIVO 3
#define MAX_CONFIGS_POR_DISPOSITIVO 3
#define MAX_CANTIDAD_DISPOSITIVOS 20

#define RANGO_SENSIBILIDAD 1

struct Dispositivo {
  unsigned char id; //Es lo mismo que el index del arreglo en que se encuentre
  unsigned char tipo_dispositivo;
  unsigned char pinesUsados[MAX_PINES_POR_DISPOSITIVO];
  unsigned int configuraciones[MAX_CONFIGS_POR_DISPOSITIVO];
  unsigned char valor;
  unsigned long ultimoMuestreo;
};

Dispositivo dispositivos[MAX_CANTIDAD_DISPOSITIVOS]; //Arreglo con todos los dispositivos
Ultrasonic ultraTemp;

unsigned char inByte;
unsigned char headerMsg[TAM_HEADER];
unsigned char bodyMsg[MAX_BODY+1];
unsigned char indexHeader;
unsigned char indexMsg;
boolean headerReceived;
boolean bodyReceived;
boolean validHeader;
boolean msgComplete;
boolean recepcionComenzada;
unsigned long previousMillis = 0;
unsigned long currentMillisBeforeMuestreo = 0;
unsigned long currentMillis = 0;

void setup()
{
  //wea = 0;
  delay(10);
  inicializarDispositivos();
  // start serial port at 115200 bps and wait for port to open:
  Serial.begin(115200);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }
  rebootVarsMsg();
}

void loop()
{
  currentMillisBeforeMuestreo = millis();
  leerSensores();
  currentMillis = millis();
  
  //Se realiza la acción que quedó en el body del mensaje
  if (msgComplete) {
    realizarAccion();
    rebootVarsMsg();
    delay(1);
  }
  
  
  if (Serial.available() > 0) {
    //Se intenta recibir un header
    if (!headerReceived) {
      inByte = Serial.read();
      recepcionComenzada = true;
      previousMillis = currentMillis;
      
      headerMsg[indexHeader++] = inByte;
      if (indexHeader >= TAM_HEADER) {
        validHeader = validateHeader(headerMsg);
        headerReceived = true;
      }
    }
    //Se intenta recibir el cuerpo del mensaje
    else if (!bodyReceived) {
      inByte = Serial.read();
      recepcionComenzada = true;
      previousMillis = currentMillis;
      
      if (validHeader) {
        bodyMsg[indexMsg++] = inByte;
        if (indexMsg >= TAM_BODY) {
          bodyMsg[indexMsg] = 0; //Caracter terminador de string
          bodyReceived = true;
          msgComplete = true;
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
        enviarMsgDebug("Reiniciando buffer de msg por timeout");
      }
      
      rebootVarsMsg();
    }
  }
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

boolean validateHeader(unsigned char *header) {
  //Se valida que el tipo de mensaje es correcto
  if (((header[0] >= '0') && (header[0] <= '3')) || (header[0] == '7')) {
    //Se comprueba el tamaño del body que sigue
    if (header[1] <= MAX_BODY) {
      return true;
    }
  }
  Serial.print(header[0]);
  enviarMsgDebug("Mensaje invalido");
  return false;
}

boolean esEntradaPWM(unsigned char num) {
  unsigned char i;
  for (i = 0; i < CANTIDAD_PINES_ANALOGOS; i++) {
    if (PINES_PWM[i] == num) {
      return true;
    }
  }
  return false;
}


void realizarAccion() {
  switch (headerMsg[0]) {
    case TIPO_MSG_DEBUG:
      enviarMsgDebug("Llego mensaje de debug");
      break;
    case TIPO_MSG_MAPEO_PINES:
      configurarDispositivo();
      break;
    case TIPO_MSG_CONSULTA_DATOS:
      consultarDispositivo();
      break;
    
    case TIPO_MSG_ACCION_DISPOSITIVO:
      actuarDispositivo();
      break;
    case TIPO_MSG_ELIMINAR_DISPOSITIVO:
      eliminarDispositivo();
      break;
  }
}

void resetDispositivo(struct Dispositivo *disp) {
  unsigned char tipoDispositivo = disp->tipo_dispositivo;
  switch (tipoDispositivo) {
    case ACTUADOR_ON_OFF:
      digitalWrite(disp->pinesUsados[0], 0); //Apago todo
      delay(2);
      break;
      
    case ACTUADOR_DIMMER:
      digitalWrite(disp->pinesUsados[0], 0); //Apago todo
      delay(2);
      break;
    
    pinMode(disp->pinesUsados[0], INPUT); //High impedance state (default)
  }
  
}

void eliminarDispositivo() {
  unsigned char idInterno = bodyMsg[0];
  //enviarMsgDebug("Eliminando dispositivo");
  if (idInterno >= MAX_CANTIDAD_DISPOSITIVOS) {
    enviarMsgDebug("Error de config: id invalido, fuera de rango");
    return;
  }
  resetDispositivo(&dispositivos[idInterno]);
  memset(&dispositivos[idInterno], 0, 1*sizeof(Dispositivo));
  enviarMsgDebug("Dispositivo eliminado");
}

void configurarDispositivo() {
  unsigned char idInterno = bodyMsg[0];
  unsigned char numPin;
  unsigned char cantidadAttr;
  unsigned char atributoTemp;
  unsigned char tipoDispositivo = bodyMsg[1];
  //enviarMsgDebug("configurando dispositivo");
  
  if (idInterno >= MAX_CANTIDAD_DISPOSITIVOS) {
    enviarMsgDebug("Error de config: id invalido, fuera de rango");
    return;
  }
  //Identifico que tipo de dispositivo se desea configurar
  switch (tipoDispositivo) { //En esta posición del bodyMsg está el tipo de dispositivo
    case SENSOR_BTN_DIGITAL:
        numPin = bodyMsg[2]; //Este tipo de dispositivo tiene un único pin
        cantidadAttr = bodyMsg[3];
        if (cantidadAttr != 0) {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        if (numPin >= CANTIDAD_PINES_DIGITALES) {
          enviarMsgDebug("Error de config: numero de pin invalido");
          return;
        }
        
        //Se configura en modo input
        pinMode(numPin, INPUT);
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin}, {10}, 0, 0};
        break;
        
        
        
    case SENSOR_ANALOG_GENERICO:
        numPin = bodyMsg[2]; //Este tipo de dispositivo tiene un único pin
        cantidadAttr = bodyMsg[3];
        if (cantidadAttr != 0) {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        if (numPin >= CANTIDAD_PINES_DIGITALES) {
          enviarMsgDebug("Error de config: numero de pin invalido");
          return;
        }
        
        //Por defecto estos son y siempre serán inputs
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin}, {100}, 0, 0};
        break;
        
        
        
    case SENSOR_ANALOG_GENERICO_DIGITALIZADOR:
        numPin = bodyMsg[2]; //Este tipo de dispositivo tiene un único pin
        cantidadAttr = bodyMsg[3];
        if (cantidadAttr == 0) {
          atributoTemp = 512;
        }
        else if (cantidadAttr == 1) {
          atributoTemp = bodyMsg[4]; //Umbral
        }
        else {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        if (numPin >= CANTIDAD_PINES_DIGITALES) {
          enviarMsgDebug("Error de config: numero de pin invalido");
          return;
        }
        
        //Por defecto estos son y siempre serán inputs
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin}, {atributoTemp, 20}, 0, 0};
        //analogRead(numPin);
        break;
        
        
        
    case SENSOR_PROXIMIDAD_ULTRASONIDO:
        numPin = bodyMsg[2];
        cantidadAttr = bodyMsg[4];
        if (cantidadAttr != 0) {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        if (numPin > CANTIDAD_PINES_DIGITALES) { //Pin de trigger
          enviarMsgDebug("Error de config: numero de primer pin invalido");
          return;
        }
        if (bodyMsg[3] > CANTIDAD_PINES_DIGITALES) { //Pin de echo
          enviarMsgDebug("Error de config: numero de segundo pin invalido");
          return;
        }
        if (bodyMsg[3] == numPin) {
          enviarMsgDebug("Error de config: los pines de trigger y echo no pueden ser iguales");
          return;
        }
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin, bodyMsg[3]}, {TIEMPO_MAXIMO_REBOOT_MSG, 1000}, 0, 0};
        ultraTemp.init(dispositivos[idInterno].pinesUsados[0], dispositivos[idInterno].pinesUsados[1], dispositivos[idInterno].configuraciones[0]);
        break;
    
    
    
    case ACTUADOR_ON_OFF:
        numPin = bodyMsg[2]; //Este tipo de dispositivo tiene un único pin
        cantidadAttr = bodyMsg[3];
        if (cantidadAttr != 0) {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        //numPin = 13; //Para hacer debug
        if (numPin > CANTIDAD_PINES_DIGITALES) {
          enviarMsgDebug("Error de config: numero de pin invalido");
          return;
        }
        
        //Se configura en modo output
        pinMode(numPin, OUTPUT);
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin}, {}, 0, 0};
        digitalWrite(numPin, 0);
        break; 
    
    
    
    case ACTUADOR_DIMMER:
        numPin = bodyMsg[2]; //Este tipo de dispositivo tiene un único pin
        cantidadAttr = bodyMsg[3];
        if (cantidadAttr != 0) {
          enviarMsgDebug("Error de config: cantidad de atributos invalida");
          return;
        }
        //numPin = 13; //Para hacer debug
        if (numPin > CANTIDAD_PINES_DIGITALES) {
          enviarMsgDebug("Error de config: numero de pin invalido");
          return;
        }
        if (!esEntradaPWM(numPin)) {
          enviarMsgDebug("Error de config: numero de pin no es PWM");
          return;
        }
        
        //Se configura en modo input
        pinMode(numPin, OUTPUT);
        dispositivos[idInterno] = (Dispositivo){idInterno, tipoDispositivo, {numPin}, {}, 0, 0};
        analogWrite(numPin, 0);
        break;
    
    
    
    default:
      enviarMsgDebug("Error de config: Tipo de dispositivo desconocido");
      return;
  }
  
  Serial.write(TIPO_MSG_CONFIG_REALIZADA);
  Serial.write(1);
  Serial.write(idInterno);
  Serial.flush();
  
}


void consultarDispositivo() {
  unsigned char idInterno;
  unsigned char tipoDispositivo;
  idInterno = bodyMsg[0];
  if (idInterno >= MAX_CANTIDAD_DISPOSITIVOS) {
    enviarMsgDebug("Error de actuacion: id invalido, fuera de rango");
    return;
  }
  //enviarMsgDebug("consultando dispositivo");
  
  tipoDispositivo = dispositivos[idInterno].tipo_dispositivo;
  switch (tipoDispositivo) { //En esta posición del bodyMsg está el tipo de dispositivo
    case SENSOR_BTN_DIGITAL:
      dispositivos[idInterno].valor = digitalRead(dispositivos[idInterno].pinesUsados[0]);
      break;
      
      
  
    case SENSOR_ANALOG_GENERICO:
      dispositivos[idInterno].valor = analogRead(dispositivos[idInterno].pinesUsados[0])/4;
      break;
      
      
    
    case SENSOR_ANALOG_GENERICO_DIGITALIZADOR:
      dispositivos[idInterno].valor = analogRead(dispositivos[idInterno].pinesUsados[0]);
      if (dispositivos[idInterno].valor < dispositivos[idInterno].configuraciones[0]) { //Comparo con el umbral
        dispositivos[idInterno].valor = 0;
      }
      else {
        dispositivos[idInterno].valor = 1;
      }
      break;
          
          
      
    case SENSOR_PROXIMIDAD_ULTRASONIDO:
      delay(10);
      ultraTemp.init(dispositivos[idInterno].pinesUsados[0], dispositivos[idInterno].pinesUsados[1], dispositivos[idInterno].configuraciones[0]);
      dispositivos[idInterno].valor = ultraTemp.Ranging(CM);
      break;
      
      
  
    case ACTUADOR_ON_OFF:
      digitalWrite(dispositivos[idInterno].pinesUsados[0], dispositivos[idInterno].valor);
      break;
      
      
  
    case ACTUADOR_DIMMER:
      analogWrite(dispositivos[idInterno].pinesUsados[0], dispositivos[idInterno].valor);
      break;
  
    default:
      enviarMsgDebug("Error al consultar: tipo de dispositivo desconocido");
      return;
    
  }
  Serial.write(TIPO_MSG_DATOS_SENSOR);
  Serial.write(2);
  Serial.write(idInterno);
  Serial.write(dispositivos[idInterno].valor);
  Serial.flush();
}

void actuarDispositivo() {
  unsigned char idInterno;
  unsigned char tipoDispositivo;
  unsigned char accionTemp;
  idInterno = bodyMsg[0];
  if (idInterno >= MAX_CANTIDAD_DISPOSITIVOS) {
    enviarMsgDebug("Error de actuacion: id invalido, fuera de rango");
    return;
  }
  
  tipoDispositivo = dispositivos[idInterno].tipo_dispositivo;
  //enviarMsgDebug("actuando dispositivo");
  switch (tipoDispositivo) { //En esta posición del bodyMsg está el tipo de dispositivo
    case ACTUADOR_ON_OFF:
        accionTemp = bodyMsg[1];
        
        if (accionTemp == 0) {
          dispositivos[idInterno].valor = 0;
          digitalWrite(dispositivos[idInterno].pinesUsados[0], LOW);
        }
        else if (accionTemp == 1) { 
          dispositivos[idInterno].valor = 1;
          digitalWrite(dispositivos[idInterno].pinesUsados[0], HIGH);
        }
        else {
          enviarMsgDebug("Error de actuacion: accion invalida, fuera de rango");
          return;
        }
        break; 
    
    
    
    case ACTUADOR_DIMMER:
        accionTemp = bodyMsg[1];
        dispositivos[idInterno].valor = accionTemp;
        analogWrite(dispositivos[idInterno].pinesUsados[0], accionTemp);
        break;
    
    
    
    default:
      enviarMsgDebug("Error de actuacion: Tipo de dispositivo de actuacion desconocido");
      return;
  }
  Serial.write(TIPO_MSG_ACCION_REALIZADA);
  Serial.write(1);
  Serial.write(idInterno);
  Serial.flush();
}


void leerSensores() {
  unsigned char idInterno;
  unsigned char valorTemp;
  unsigned char tipoDispositivo;
  
  for (idInterno = 0; idInterno < MAX_CANTIDAD_DISPOSITIVOS; idInterno++) {
    tipoDispositivo = dispositivos[idInterno].tipo_dispositivo;
    
    if (tipoDispositivo != 0) { //Está asignado si vale distinto de 0
      
      switch (tipoDispositivo) { //En esta posición del bodyMsg está el tipo de dispositivo
        case SENSOR_BTN_DIGITAL:
          if ((currentMillisBeforeMuestreo - dispositivos[idInterno].ultimoMuestreo) > dispositivos[idInterno].configuraciones[0]) {
            
            delay(2); //Espera para recuperar el conversor Análogo-digital
            valorTemp = digitalRead(dispositivos[idInterno].pinesUsados[0]);
            if (valorTemp != dispositivos[idInterno].valor) {
              dispositivos[idInterno].valor = valorTemp;
              enviarInfoSensor(idInterno, valorTemp);
            }
            dispositivos[idInterno].ultimoMuestreo = currentMillisBeforeMuestreo;
          }
          break;
          
          
      
        case SENSOR_ANALOG_GENERICO:
          if ((currentMillisBeforeMuestreo - dispositivos[idInterno].ultimoMuestreo) > dispositivos[idInterno].configuraciones[0]) {
            delay(2); //Espera para recuperar el conversor Análogo-digital
            valorTemp = analogRead(dispositivos[idInterno].pinesUsados[0]) / 4;//Valor máximo 255
            //valorTemp = valorTemp/4;
            if (valorTemp < dispositivos[idInterno].valor) {
              if (dispositivos[idInterno].valor - valorTemp > RANGO_SENSIBILIDAD ) {
                dispositivos[idInterno].valor = valorTemp;
                enviarInfoSensor(idInterno, valorTemp);
              }
            }
            else {
              if (valorTemp - dispositivos[idInterno].valor > RANGO_SENSIBILIDAD ) {
                dispositivos[idInterno].valor = valorTemp;
                enviarInfoSensor(idInterno, valorTemp);
              }
            }
            dispositivos[idInterno].ultimoMuestreo = currentMillisBeforeMuestreo;
          }
          break;
          
          
        
        case SENSOR_ANALOG_GENERICO_DIGITALIZADOR:
          if ((currentMillisBeforeMuestreo - dispositivos[idInterno].ultimoMuestreo) > dispositivos[idInterno].configuraciones[1]) {
            delay(2); //Espera para recuperar el conversor Análogo-digital
            valorTemp = (unsigned int)analogRead(dispositivos[idInterno].pinesUsados[0]);
            if (valorTemp < dispositivos[idInterno].configuraciones[0]) { //Comparo con el umbral
              valorTemp = 0;
            }
            else {
              valorTemp = 1;
            }
            if (valorTemp != dispositivos[idInterno].valor) {
              dispositivos[idInterno].valor = valorTemp;
              enviarInfoSensor(idInterno, valorTemp);
            }
            dispositivos[idInterno].ultimoMuestreo = currentMillisBeforeMuestreo;
          }
          break;
              
              
          
        case SENSOR_PROXIMIDAD_ULTRASONIDO:
        
          if ((currentMillisBeforeMuestreo - dispositivos[idInterno].ultimoMuestreo) > dispositivos[idInterno].configuraciones[1]) {
            delay(2); //Espera para recuperar el conversor Análogo-digital
            ultraTemp.init(dispositivos[idInterno].pinesUsados[0], dispositivos[idInterno].pinesUsados[1], dispositivos[idInterno].configuraciones[0]);
            valorTemp = ultraTemp.Ranging(CM);
            if (valorTemp != dispositivos[idInterno].valor) {
              dispositivos[idInterno].valor = valorTemp;
              
              enviarInfoSensor(idInterno, valorTemp);
            }
            dispositivos[idInterno].ultimoMuestreo = currentMillisBeforeMuestreo;
          }
          break;
      }
    }
  }
}

void enviarInfoSensor(unsigned char idInterno, unsigned char valorTemp) {
  Serial.write('6');
  Serial.write(2);
  Serial.write(idInterno);
  Serial.write(valorTemp);
  Serial.flush();
  
}

void inicializarDispositivos() {
  unsigned char i;
  for (i = 0; i < MAX_CANTIDAD_DISPOSITIVOS; i++) {
    memset(&(dispositivos[i]), sizeof(Dispositivo), (byte)0);
  }
  
  
  //DEBE SACARSE INFORMACIÓN DE LA EPROM
  
  
}


void enviarMsgDebug(const char mensaje[]) {
  unsigned char i;
  unsigned char largo;
  largo = strlen(mensaje);
  
  Serial.write('0');
  Serial.write(largo);
  Serial.write(mensaje);
  Serial.flush();
  
}
