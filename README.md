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
-Capa de abstracción entre dispositivos desde el punto de vista de Arduino y desde el punto de vista del usuario, esto incluye conversión de rangos de datos leídos, unidades de medición, nombre de dispositivo a nivel de usuario (Se pretende implementar mediante una herencia de la clase "tipo_dispositivo").
-Disparadores: Método configurable por el usuario para realizar ciertas acciones al cumplirse ciertas condiciones en los dispositivos (sean sensores y/o actuadores).
-Cambio de contraseña y nombre de usuario.
-Logo
-Cambio de configuraciones del sistema relacionadas con el logo, fondo de pantalla y nombre de la app.
-Gráficos y medición del estado de los dispositivos en historial.
