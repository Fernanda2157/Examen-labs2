# 1. Resolución del Examen Práctico

## 5.1 Comprensión del Negocio (2.0 pts)

### Descripción General
El sistema gestiona las Novedades (incidentes) que ocurren en las máquinas de los laboratorios de cómputo de la Universidad Central del Ecuador. Su objetivo es canalizar automáticamente las incidencias reportadas por los estudiantes hacia el personal técnico especializado (administradores) según el área temática, priorizando el nivel de urgencia y manteniendo la trazabilidad desde su apertura hasta su solución.

### Actores del Sistema
* **Estudiante:** Usuario con credenciales universitarias que detecta una falla física, lógica o de conectividad en una máquina y la reporta generando un ticket.
* **Administrador (Técnico de Laboratorio):** Personal especializado asignado a un área específica (hardware, software, redes) encargado de revisar la bandeja de incidencias asignadas y registrarlas como resueltas.

### Reglas de Negocio Identificadas (Extraídas del Código)
1. **Autorización de Registro:** Solo los usuarios con rol estudiante pueden registrar novedades.
2. **Estado del Laboratorio:** No se aceptan novedades en laboratorios que estén en estado inactivo.
3. **Existencia de Máquina:** La máquina reportada debe pertenecer físicamente a la lista de máquinas habilitadas del laboratorio.
4. **Validación del Tipo de Incidente:** Los tipos válidos son exclusivamente: `hardware`, `software`, `red`, `perifericos` u `otro`.
5. **Longitud de Descripción:** La descripción del problema debe tener una longitud mínima de 10 caracteres.
6. **No Duplicidad de Incidentes:** No se permite abrir una nueva novedad para una máquina si esta ya tiene una novedad en estado `ABIERTA`.
7. **Ruteo Automático de Administradores:**
   * `hardware` / `perifericos` -> Administrador del área hardware.
   * `software` / `otro` -> Administrador del área software.
   * `red` -> Administrador del área redes.
8. **Cálculo Automático de Prioridad:**
   * Incidentes de `red` o `hardware` -> Prioridad **ALTA**.
   * Incidentes de `software` -> Prioridad **MEDIA**.
   * Otros incidentes -> Prioridad **BAJA**.
9. **Formato Correlativo del Ticket:** El código del ticket sigue el patrón `NOV-YYYY-XXXX` (ej. `NOV-2026-0001`).
10. **Notificación Directa:** Todo registro exitoso debe desencadenar un correo de notificación al administrador asignado.
11. **Autorización de Cierre:** Una novedad solo puede ser cerrada por el administrador asignado a esa área/ticket.
12. **Validación de Solución:** El texto explicativo de la solución debe tener al menos 5 caracteres y la novedad pasa a estado `CERRADA`.

---

## 5.2 Lenguaje Ubicuo (1.0 pt)

| Término | Definición |
| :--- | :--- |
| **Novedad** | Incidente o falla reportada por un estudiante respecto a una máquina dentro de un laboratorio. |
| **Ticket** | Código alfanumérico único (`NOV-YYYY-XXXX`) que identifica formalmente una novedad dentro de la universidad. |
| **Laboratorio** | Espacio físico equipado con computadores, ubicado en un edificio y piso determinado, en estado activo o inactivo. |
| **Máquina** | Estación de trabajo/computador individual identificado por un número correlativo dentro de un laboratorio. |
| **Tipo de Incidente** | Categorización del problema (`hardware`, `software`, `red`, `perifericos`, `otro`). |
| **Prioridad** | Nivel de urgencia asignado automáticamente (`ALTA`, `MEDIA`, `BAJA`) según la categoría técnica del daño. |
| **Administrador** | Personal encasillado en un área de soporte (`hardware`, `software`, `redes`) encargado de la resolución. |
| **Estado de Novedad** | Situación del ticket durante su ciclo de vida (`ABIERTA` o `CERRADA`). |

---

## 5.3 Bounded Contexts y Context Map (2.5 pts)

Identificamos 3 Bounded Contexts (Contextos Delimitados) principales:

1. **Contexto de Gestión de Novedades (Incident Management Context):** *Core Domain*. Se encarga del registro, asignación de responsable, cálculo de prioridad, validación de reglas de apertura/cierre y ciclo de vida de la novedad.
2. **Contexto de Infraestructura de Laboratorios (Lab Infrastructure Context):** *Supporting Domain*. Encargado del catálogo de laboratorios, ubicación física, estado de los laboratorios y verificación de existencia de máquinas.
3. **Contexto de Notificaciones (Notification Context):** *Generic Subdomain*. Encargado del armado y envío de alertas/correos hacia los administradores.

### Context Map
* **Gestión de Novedades** actúa como *Upstream (Customer)* respecto a **Infraestructura de Laboratorios** *(Downstream/Supplier)* para consultar la disponibilidad y existencia física de equipos.
* **Gestión de Novedades** actúa como *Upstream* respecto a **Notificaciones** *(Downstream)* publicando el evento de dominio `NovedadRegistradaEvent`.
  +------------------------------------+          +--------------------------------------+
| Contexto Infraestructura de Labs   | <------- | Contexto Gestión de Novedades (Core) |
| (Valida Labs y Máquinas)           |  (ACL)   | (Reglas, Tickets y Estados)          |
+------------------------------------+          +--------------------------------------+
|
| (Event: NovedadRegistrada)
v
+--------------------------------------+
| Contexto de Notificaciones           |
| (Notificaciones por email)           |
+--------------------------------------+


---

## 5.4 Modelado Táctico DDD (2.5 pts)

### Elementos del Dominio

* **Agregado Novedad (Raíz del Agregado):**
  * **Entidad Raíz:** `Novedad` (Identificada por `NovedadId` o `CodigoTicket`).
  * **Value Objects:** `CodigoTicket`, `Descripcion`, `Prioridad`, `EstadoNovedad`, `TipoIncidente`, `Solucion`.
* **Entidades externas o agregados secundarios:**
  * `Usuario` (con sub-tipos o atributos `Estudiante` y `Administrador`).
  * `Laboratorio` (con su lista de números de máquinas y estado activo/inactivo).
* **Value Objects vs. Entidades (Justificación):**
  * `Maquina` / `CodigoTicket` / `Prioridad` son **Value Objects**: No tienen identidad propia fuera de su contexto. Un número de máquina 5 dentro de un laboratorio no existe por sí solo sin el contexto del laboratorio. `CodigoTicket` es inmutable.
  * `Novedad` y `Laboratorio` son **Entidades**: Tienen ciclo de vida, cambian de estado con el tiempo y poseen una identidad única que trasciende sus atributos.
* **Servicios de Dominio:**
  * `RuteoNovedadService`: Aplica la regla de negocio para decidir qué administrador recibe la novedad según el `TipoIncidente`.
* **Repositorios (Interfaces):**
  * `NovedadRepository`: Para persistir y consultar las novedades.
  * `LaboratorioRepository`: Para verificar validez de laboratorios.
  * `UsuarioRepository`: Para buscar estudiantes y administradores.
* **Eventos de Dominio:**
  * `NovedadRegistradaEvent`: Emitido cuando una novedad es creada con éxito. Escuchado por el servicio de notificaciones.
  * `NovedadCerradaEvent`: Emitido cuando el administrador resuelve la incidencia.

---

## 5.5 Elección de Arquitectura (1.5 pts)

Elegimos la **Arquitectura por Capas (Layered Architecture)**.

### Justificación
* **Separación de Responsabilidades:** El sistema se organiza en capas concéntricas/secuenciales con responsabilidades claramente delimitadas:
  * **Presentación (`presentation`):** Maneja la interacción con el usuario mediante la consola (`SimuladorNovedades`) sin contener reglas de negocio.
  * **Aplicación (`application`):** Orquesta los casos de uso del sistema (`NovedadService`) coordinando el flujo entre la presentación y el dominio.
  * **Dominio (`domain`):** Almacena el núcleo del negocio (entidades, objetos de valor y reglas de validación como el cálculo de prioridades y control de estados) de forma aislada.
  * **Infraestructura / Persistencia (`infraestructure.persitence`):** Implementa el almacenamiento físico y acceso a datos (`InMemoryUserRepository`, `InMemoryNovedadRepository`), desacoplado mediante interfaces del dominio.
* **Inversión de Dependencias y Control de Acoplamiento:** La capa de aplicación interactúa con el dominio y con abstracciones (interfaces de repositorios), mientras que las implementaciones concretas residen en infraestructura. Esto evita que la lógica de negocio dependa directamente de detalles de persistencia o frameworks externos.
* **Mantenibilidad y Escalabilidad:** Permite actualizar o reemplazar componentes de capas ex
