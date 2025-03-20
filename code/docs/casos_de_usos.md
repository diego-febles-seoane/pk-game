
## Caso de Usos 

### Actores

   - **Administrador**
     - Descripción: Usuario con permisos completos para gestionar la aplicación y moderar contenidos.

   - **Usuario Registrado**
     - Descripción: Usuario que puede crear y modificar su perfil, así como interactuar con contenidos.

   - **Usuario sin Registrar**
     - Descripción: Usuario sin registro que puede visualizar algunos contenidos de la aplicación.

  
### Caracteristicas de caso de uso

---

### **Actor: Usuario Registrado**  

| **Actor** | Usuario Registrado |
|---|---|
| **Descripción** | _Es un usuario que tiene una cuenta en la plataforma y puede acceder a diversas funciones._ |
| **Características** | _Puede autenticarse, modificar su perfil y participar en actividades._ |
| **Relaciones** | _Iniciar sesión, editar perfil, eliminar cuenta, participar en combates, mandar sugerencia, mandar email._ |
| **Referencias** | _Ajuste de cuenta, acceder a su configuración, recuperar contraseña, buscar contrincante, cargar equipo._ |
| **Notas** | _Debe tener credenciales válidas para acceder a la plataforma._ |
| **Autor** | _Diego Febles Seoane_ |
| **Fecha** | _20/03/2025_ |

---

### **Actor: Usuario sin Registrar**  

| **Actor** | Usuario sin Registrar |
|---|---|
| **Descripción** | _Es un visitante que aún no ha creado una cuenta._ |
| **Características** | _Puede realizar acciones limitadas antes de registrarse._ |
| **Relaciones** | _Registrarse, visitar perfiles de usuario, participar en combates de tutorial._ |
| **Referencias** | _Mandar mensaje._ |
| **Notas** | _Debe registrarse para acceder a más funciones._ |
| **Autor** | _Diego Febles Seoane_ |
| **Fecha** | _20/03/2025_ |

---

### **Actor: Administrador**  

| **Actor** | Administrador |
|---|---|
| **Descripción** | _Es un usuario con permisos avanzados para gestionar la plataforma._ |
| **Características** | _Puede administrar usuarios, modificar configuraciones y gestionar contenido._ |
| **Relaciones** | _Administrar usuarios, visualizar sugerencias, revisar solicitudes de soporte, modificar configuraciones de la aplicación, generar reportes de actividad, acceder al panel de administración._ |
| **Referencias** | _Añadir contenido a usuarios._ |
| **Notas** | _Debe tener permisos de administrador._ |
| **Autor** | _Diego Febles Seoane_ |
| **Fecha** | _20/03/2025_ |

---

---

### **Caso de Uso: Iniciar Sesión**

| **Caso de Uso** | Iniciar Sesión |
|-----------------|----------------|
| **Descripción**  | _Permite a un usuario autenticarse en la plataforma._ |
| **Actores**  | _Usuario Registrado_ |
| **Precondiciones** | _El usuario debe estar registrado y tener credenciales válidas._ |
| **Flujo Principal**  |  
1. El usuario accede a la pantalla de inicio de sesión.  
2. Ingresa su correo electrónico y contraseña.  
3. El sistema verifica las credenciales.  
4. Si son correctas, el usuario es redirigido a su perfil. |
| **Flujo Alternativo**  |  
- Si las credenciales son incorrectas, se muestra un mensaje de error. |

---

### **Caso de Uso: Editar Perfil**

| **Caso de Uso** | Editar Perfil |
|-----------------|---------------|
| **Descripción**  | _Permite a un usuario modificar su información personal._ |
| **Actores**  | _Usuario Registrado_ |
| **Precondiciones** | _El usuario debe haber iniciado sesión._ |
| **Flujo Principal**  |  
1. El usuario accede a la configuración de su perfil.  
2. Modifica los datos deseados (nombre, foto, etc.).  
3. Guarda los cambios y el sistema los actualiza. |
| **Flujo Alternativo**  |  
- Si hay errores en los datos ingresados, se muestra un mensaje de validación. |

---

### **Caso de Uso: Eliminar Cuenta**

| **Caso de Uso** | Eliminar Cuenta |
|-----------------|-----------------|
| **Descripción**  | _Permite a un usuario registrado eliminar su cuenta de manera permanente._ |
| **Actores**  | _Usuario Registrado_ |
| **Precondiciones** | _El usuario debe haber iniciado sesión y confirmar la eliminación._ |
| **Flujo Principal**  |  
1. El usuario accede a la opción de "Eliminar cuenta".  
2. Se solicita confirmación de la acción.  
3. Si el usuario confirma, la cuenta y todos sus datos son eliminados.  
4. Se muestra un mensaje de confirmación. |
| **Flujo Alternativo**  |  
- Si el usuario cancela la acción, la cuenta no se elimina. |

---

### **Caso de Uso: Participar en Combates**

| **Caso de Uso** | Participar en Combates |
|-----------------|------------------------|
| **Descripción**  | _Permite a un usuario competir contra otros jugadores._ |
| **Actores**  | _Usuario Registrado, Usuario sin Registrar (solo tutorial)_ |
| **Precondiciones** | _El usuario debe haber seleccionado su equipo de combate._ |
| **Flujo Principal**  |  
1. El usuario accede a la opción de combate.  
2. Busca o es emparejado con un contrincante.  
3. El combate inicia y se registran los resultados.  
4. Se muestran los resultados al finalizar la partida. |

---

### **Caso de Uso: Mandar Sugerencia**

| **Caso de Uso** | Mandar Sugerencia |
|-----------------|-------------------|
| **Descripción**  | _Permite a un usuario enviar una sugerencia al equipo de administración._ |
| **Actores**  | _Usuario Registrado_ |
| **Precondiciones** | _Debe existir un formulario de sugerencias disponible._ |
| **Flujo Principal**  |  
1. El usuario accede al formulario de sugerencias.  
2. Redacta su mensaje.  
3. Envía la sugerencia y el sistema la almacena para revisión. |

---

### **Caso de Uso: Mandar Email**

| **Caso de Uso** | Mandar Email |
|-----------------|--------------|
| **Descripción**  | _Permite a un usuario enviar un correo electrónico desde la plataforma._ |
| **Actores**  | _Usuario Registrado_ |
| **Precondiciones** | _El usuario debe haber iniciado sesión._ |
| **Flujo Principal**  |  
1. El usuario accede a la opción de correo.  
2. Redacta un mensaje.  
3. Envía el email. |

---

### **Caso de Uso: Administrar Usuarios**

| **Caso de Uso** | Administrar Usuarios |
|-----------------|----------------------|
| **Descripción**  | _Permite a un administrador gestionar cuentas de usuarios._ |
| **Actores**  | _Administrador_ |
| **Precondiciones** | _El usuario debe ser administrador y haber iniciado sesión._ |
| **Flujo Principal**  |  
1. El administrador accede a la sección de gestión de usuarios.  
2. Puede ver la lista de usuarios.  
3. Realiza acciones como eliminar, modificar o suspender cuentas. |

---

### **Caso de Uso: Revisar Solicitudes de Soporte**

| **Caso de Uso** | Revisar Solicitudes de Soporte |
|-----------------|-------------------------------|
| **Descripción**  | _Permite a un administrador atender problemas reportados por usuarios._ |
| **Actores**  | _Administrador_ |
| **Precondiciones** | _Debe haber solicitudes de soporte activas._ |
| **Flujo Principal**  |  
1. El administrador accede al panel de soporte.  
2. Revisa las solicitudes enviadas por los usuarios.  
3. Puede responderlas o tomar medidas para resolver el problema. |

---

### **Caso de Uso: Modificar Configuraciones de la Aplicación**

| **Caso de Uso** | Modificar Configuraciones de la Aplicación |
|-----------------|--------------------------------------------|
| **Descripción**  | _Permite a un administrador cambiar ajustes generales de la plataforma._ |
| **Actores**  | _Administrador_ |
| **Precondiciones** | _El usuario debe ser administrador y haber iniciado sesión._ |
| **Flujo Principal**  |  
1. El administrador accede a la sección de configuración.  
2. Realiza cambios en los parámetros del sistema.  
3. Guarda los cambios y se aplican de inmediato. |

---

### **Caso de Uso: Generar Reportes de Actividad**

| **Caso de Uso** | Generar Reportes de Actividad |
|-----------------|-------------------------------|
| **Descripción**  | _Permite a un administrador crear informes sobre la actividad de la plataforma._ |
| **Actores**  | _Administrador_ |
| **Precondiciones** | _Debe existir actividad registrada en el sistema._ |
| **Flujo Principal**  |  
1. El administrador accede a la sección de reportes.  
2. Selecciona los filtros y parámetros deseados.  
3. Genera el informe y lo descarga o visualiza. |

---

### **Caso de Uso: Acceder al Panel de Administración**

| **Caso de Uso** | Acceder al Panel de Administración |
|-----------------|-----------------------------------|
| **Descripción**  | _Permite a un administrador entrar en la interfaz de gestión avanzada._ |
| **Actores**  | _Administrador_ |
| **Precondiciones** | _Debe tener permisos administrativos._ |
| **Flujo Principal**  |  
1. El administrador inicia sesión.  
2. Accede a la interfaz de administración.  
3. Puede gestionar la plataforma desde ahí. |

---

### **Caso de Uso: Enviar Correo**

| **Caso de Uso** | Enviar Correo |
|-----------------|--------------|
| **Descripción**  | _Permite al sistema enviar correos electrónicos a los usuarios._ |
| **Actores**  | _Sistema_ |
| **Precondiciones** | _Debe existir una razón válida para el envío de correo (ej. recuperación de contraseña)._ |
| **Flujo Principal**  |  
1. Se genera el contenido del correo.  
2. Se envía a la dirección de email del usuario.  
3. El usuario recibe la notificación en su bandeja de entrada. |

--- 
     
