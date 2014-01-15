KasaDonLuchito
==============

Proyecto para la asignatura Automatización de Proyectos del Departamento de Ingeniería en Informática de la Universidad de Santiago de Chile. Consta de la automatización de acciones cotidianas dentro de la casa. Comúnmente se relaciona con el término Domótica.
Como tecnología de hardware se hace uso de placas Arduino.
Como software se hace uso de Glassfish server 3+, JSF, java EE.
Actualmente posee las funcionalidades de:
-Actuar sobre dispositivos.
-Configurar dispositivos conectados al Arduino de forma dinámica en tiempo de 
ejecución.
-Envío de información de sensores desde Arduino a la aplicación java EE.
-Escenas: Conjunto de acciones realizadas a la vez.
-Timers: Acción de cierto dispositivo o cierta escena en cierta hora y día de la semana.


Falta por implementar:
-Logo
-Mostrar el estado de la conexión del o los arduino
-Mantenedor de tipos de dispositivos de nivel de usuario
-Cambio de configuraciones del sistema relacionadas con el logo, fondo de pantalla y nombre de la app.
-Disparadores: Método configurable por el usuario para realizar ciertas acciones al cumplirse ciertas condiciones en los dispositivos (sean sensores y/o actuadores).
-Gráficos y medición del estado de los dispositivos en historial.
