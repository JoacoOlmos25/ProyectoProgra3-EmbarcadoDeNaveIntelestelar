# Registro de Uso de Inteligencia Artificial

Registro de usos relevantes de herramientas de IA conforme a la sección 8 de las Reglas Operativas del TPG (Programación C — 2026).

---

## Entrega 1

### 1. Formato de contratos y Javadoc en Tripulación
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Definir una estructura formal de contratos (invariantes de clase, precondiciones y postcondiciones) en formato Javadoc a partir de un esquema inicial del equipo.
- **Componente o documento afectado:** Clases del paquete `src.Tripulacion`.
- **Resultado aprovechado:** Separación entre invariantes de clase y contratos por método con fórmulas explícitas en formato Javadoc.
- **Revisión o modificación realizada por el equipo:** Se adaptaron los contratos a los atributos reales y reglas de remuneración de cada cargo.
- **Forma en que se verificó el resultado:** Revisión conjunta de Javadoc y ejecución de pruebas de liquidación de haberes.

### 2. Resolución de conflictos en Git
- **Herramienta utilizada:** Claude.
- **Propósito de la consulta:** Asistencia para resolver conflictos de merge entre ramas.
- **Componente o documento afectado:** Historial Git del repositorio.
- **Resultado aprovechado:** Referencia de comandos básicos de Git (no aportó una solución resolutiva directa; el conflicto se resolvió manualmente).
- **Revisión o modificación realizada por el equipo:** Resolución manual de los archivos en conflicto y verificación de compilación.
- **Forma en que se verificó el resultado:** `git status`, `git log` y compilación limpia del proyecto.

### 3. Manejo de marcas temporales (`LocalDateTime`) e inyección de `Bitacora`
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Consultar el formato de `LocalDateTime.now()` y posteriormente corregir el problema de acoplamiento generado por métodos estáticos en `Bitacora` que mezclaban registros entre múltiples naves.
- **Componente o documento afectado:** `Bitacora.java`, `Evento.java`, `Asistente.java`, `Nave.java`.
- **Resultado aprovechado:** Formalización de inyección de dependencias por instancia para mantener una bitácora independiente por cada nave/asistente.
- **Revisión o modificación realizada por el equipo:** Se evaluaron los puntos afectados, se eliminó el estado estático y se inyectó la instancia de `Bitacora` desde `Asistente` hacia `Nave` y `MotorWarp`.
- **Forma en que se verificó el resultado:** Ejecución con varias naves comprobando que cada una mantiene su propia bitácora sin mezclar eventos.

### 4. Bosquejo del informe y generación inicial de diagramas UML (PlantUML)
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Armar un borrador inicial del informe de estructura y generar código `.puml` cuando los plugins del IDE no generaron diagramas claros.
- **Componente o documento afectado:** `TP/documentos/Informe.md` y archivos `.puml` iniciales en `TP/documentos/UMLs/`.
- **Resultado aprovechado:** Estructura base del informe y sintaxis PlantUML por paquete.
- **Revisión o modificación realizada por el equipo:** Edición manual del informe y contraste de relaciones UML con el código de Entrega 1.
- **Forma en que se verificó el resultado:** Renderizado de los diagramas `.png` y revisión visual clase por clase.

---

## Entrega 2

### 5. Diseño e implementación del Visor del Universo Paralelo (Concurrencia + MVC + Observer)
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Diseñar el módulo Productor-Consumidor con monitores (`synchronized`, bucles `while`, `wait()`, `notifyAll()`), `Runnable`, DTOs inmutables para notificaciones seguras hacia Swing y panel gráfico en tiempo real.
- **Componente o documento afectado:** Paquete `src.concurrencia.*` (`PlataformaDeCarga`, `GeneradoraDeEnergia`, `NaveConsumidora`, `Bateria`, `EventoVisorDTO`, `EstadoEntidadDTO`, `VentanaVisorUniverso`, `PanelAnimacionVisor`, `ControladorVisor`).
- **Resultado aprovechado:** Esquema de estados visuales, monitor con exclusión mutua e instantáneas inmutables (`EventoVisorDTO`) para evitar `ConcurrentModificationException` en Swing.
- **Revisión o modificación realizada por el equipo:** Adaptación a los paquetes del repositorio, marcado de hilos como *daemon*, cierre controlado en `finally` y agregado de contratos y `assert`.
- **Forma en que se verificó el resultado:** Creación dinámica de generadoras y naves en ejecución verificando bloqueos por plataforma llena/vacía, contadores en vivo y detención limpia.

### 6. Interfaz gráfica principal, Fachada y Controlador MVC
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Diseñar la interfaz principal en Swing apta para proyección, separar responsabilidades de negocio en `SistemaEstelar` y `TripulanteFactory`, y conectar el refresco por turno mediante `Observador`.
- **Componente o documento afectado:** `src.vista.*`, `ControladorPrincipal.java`, `SistemaEstelar.java`, `TripulanteFactory.java`.
- **Resultado aprovechado:** Layout de alto contraste, `PanelTablero` con rotación de nave y trazo de ruta, `PopupColision`, `DialogoInformeMision` y ejecución de misiones en hilo secundario.
- **Revisión o modificación realizada por el equipo:** Ajustes de distribución visual, limpieza de firmas y acoplamiento con las misiones reales.
- **Forma en que se verificó el resultado:** Pruebas interactivas alternando múltiples naves, cargando tripulación y ejecutando M-01, M-02 y M-03 en pantalla completa.

### 7. Integración de Misiones reales, Motor Warp, Double Dispatch y migración a Maven estándar
- **Herramienta utilizada:** Claude / Gemini.
- **Propósito de la consulta:** Integrar el ciclo Template Method en `Mision` con `Tablero` y `Destino`, aplicar Double Dispatch en batallas entre naves, resolver el reinicio del `MotorWarp` sin `instanceof` y reestructurar el proyecto al estándar Maven (`src/main/java`).
- **Componente o documento afectado:** `TP/pom.xml`, `src.Misiones.*`, `src.MotorWarp.*`, `src.nave.*`, `src.ElementosDelUniverso.*`, `DecisionesEtapa1y2.md`.
- **Resultado aprovechado:** `MisionFactory`, `ObservadorMision`, `reiniciarWarp()` polimórfico en `MotorState`, tabla de combates por Double Dispatch y guía paso a paso de vinculación Maven en IntelliJ.
- **Revisión o modificación realizada por el equipo:** Verificación de costos y reglas frente al enunciado, ajuste de configuración de módulos en IntelliJ y pruebas de compilación en `target/classes`.
- **Forma en que se verificó el resultado:** Compilación completa con Maven (`clean` + `compile` → `BUILD SUCCESS`) y ejecución de las tres misiones verificando informes, recursos y batallas.

### 8. Sincronización completa de Bitácora real y detalle efectivo en encuentros
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Diagnosticar por qué los choques con `Asteroide` y las recargas en `EstacionDeCarga` no quedaban registrados en la pestaña Bitácora ni mostraban los montos efectivos en el popup.
- **Componente o documento afectado:** `Nave.java`, `Bitacora.java`, `Mision.java`, `ControladorPrincipal.java`.
- **Resultado aprovechado:** Registro de daño/asistencia en `Nave`, protección `synchronized` con copias inmodificables en `Bitacora`, captura del resultado efectivo tras interactuar en `Mision` y sincronización completa por nave (`IdentityHashMap`) en `ControladorPrincipal`.
- **Revisión o modificación realizada por el equipo:** Revisión de los mensajes generados, corrección de una línea duplicada en el informe final de `Mision.cerrar()` y ajuste de la duración del popup.
- **Forma en que se verificó el resultado:** Ejecución de misiones con múltiples naves comprobando en pantalla y en la pestaña Bitácora los registros exactos de asteroides, estaciones de carga, saltos y combates.

### 9. Persistencia DAO/DTO + JDBC (SQLite) y auditoría integral de contratos y `assert`
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Revisar la implementación de persistencia de credenciales en SQLite, separar responsabilidades entre Vista, Controlador, Servicio y DAO, y auditar contratos Javadoc, invariantes y `assert` en todos los paquetes del proyecto.
- **Componente o documento afectado:** `src.Usuario.*` (`UsuarioService`, `UsuarioDAO`, `UsuarioDAOJDBC`, `UsuarioDTO`, `ConexionBD`, `UsuarioExcepciones`), `VentanaLogin.java`, `ControladorLogin.java`, `MainAplicacion.java`, `src.MotorWarp.*`, `src.Tripulacion.*`, `src.nave.*`, `src.Misiones.*`, `src.AC.*`, `src.concurrencia.*`.
- **Resultado aprovechado:** Creación de `UsuarioService` (inyectando `UsuarioDAO`), retorno de `UsuarioDTO` en consultas del DAO con `PreparedStatement` y `try-with-resources`, validación de formato en `VentanaLogin`, corrección de bugs menores (`EjecutandoSaltoWarpState`, `DecoratorTrip.setAntiguedad`, `TIPOS_NAVES_ENEMIGAS` en `MainAplicacion`) y estandarización de invariantes/`assert`.
- **Revisión o modificación realizada por el equipo:** Integración por bloques paquete por paquete, verificación de visibilidad *package-private* en `MotorWarp` y comprobación de estilos visuales de `VentanaLogin`.
- **Forma en que se verificó el resultado:** Compilación con Maven (`clean` + `compile`) y ejecución de `MainAplicacion` con `-ea` activo probando login/registro (campos vacíos, usuario duplicado, clave inválida, persistencia tras reinicio), las tres misiones y el visor concurrente.

### 10. Actualización de diagramas UML y documentación final de Entrega 2
- **Herramienta utilizada:** Gemini.
- **Propósito de la consulta:** Auditar la consistencia entre los diagramas `.puml` heredados de Entrega 1 y el código definitivo de Entrega 2, y actualizar la documentación del repositorio (`README.md`, `Informe.md`, `../database`, `.gitignore`).
- **Componente o documento afectado:** `TP/documentos/UMLs/*.puml` (incluyendo los nuevos `PersistenciaUml.puml` y `ConcurrenciaUml.puml`), `README.md`, `TP/documentos/Informe.md`, `../database`, `.gitignore`, `TP/documentos/uso-ia.md`.
- **Resultado aprovechado:** Actualización de todos los diagramas PlantUML reflejando interfaces (`Comandable`, `ObservadorMision`, `UsuarioDAO`, `IVistaLogin`, `IVistaVisor`), patrones de Entrega 2 y documentación reproducible de compilación y ejecución.
- **Revisión o modificación realizada por el equipo:** Contraste con la estructura real del repositorio (`estructura_actual.txt`), regeneración de los `.png` y limpieza de archivos temporales antes del etiquetado.
- **Forma en que se verificó el resultado:** Revisión de consistencia entre cada diagrama y su paquete correspondiente, validación de rutas en el `README.md` y verificación final del árbol de archivos en Git.