# Informe de Diseño y Arquitectura — Software Embarcado de Nave Interestelar

## 1. Introducción y Objetivos

El presente informe documenta la arquitectura, los patrones de diseño y las decisiones de implementación del sistema de simulación espacial desarrollado incrementalmente en Java (JDK 21 + Maven) para las Entregas 1 y 2 de Programación C.

- **Entrega 1 (Núcleo OO):** modelado de naves, gestión de combustible, energía, desgaste y mantenimiento, propulsión por Motor Warp, tripulación con liquidación de haberes, asistente de comando, bitácora independiente por nave y ciclo base de misiones.
- **Entrega 2 (Sistema integrado):** evolución del núcleo dentro de un universo dinámico por turnos (`Tablero` y `Destino`), resolución de interacciones mediante Double Dispatch, interfaz gráfica en Swing bajo MVC + Observer, persistencia de credenciales con DAO/DTO sobre JDBC (SQLite) y módulo concurrente Productor-Consumidor con monitores.

---

## 2. Arquitectura General y Paquetes (Estructura Maven)

El proyecto adopta la estructura estándar de Maven (`TP/src/main/java/src/...`) con `pom.xml` gestionando la compilación y el driver `sqlite-jdbc`.

| Paquete | Clases principales | Responsabilidad |
| :--- | :--- | :--- |
| `src.nave` | `Nave`, `Ataque`, `Carguero`, `Explorador`, `NaveFactory`, `Comandable`, `FuenteDeRecursos`, `Mantenible` | Modelo de naves, invariantes de recursos, mantenimiento, combate Double Dispatch y creación desacoplada. |
| `src.MotorWarp` | `MotorWarp`, `MotorState`, `DisponibleState`, `PreparandoState`, `EjecutandoSaltoWarpState`, `EnfriandoState` | Ciclo de vida del Motor Warp mediante patrón State, costos de salto y transiciones válidas/inválidas. |
| `src.Tripulacion` | `Tripulante`, `Capitan`, `Teniente`, `Alferez`, `Consejero`, `DecoratorTrip`, `Terricola`, `Vulcano`, `Marciano`, `TripulanteFactory` | Cargos jerárquicos, decoración por origen planetario y cálculo polimórfico de haberes. |
| `src.AC` | `SistemaEstelar`, `Asistente`, `Bitacora`, `Evento` | Fachada de aplicación (`SistemaEstelar`), coordinación por nave (`Asistente`) y bitácora sincronizada por instancia. |
| `src.Misiones` | `Mision`, `Mision_01`, `Mision_02`, `Mision_03`, `MisionFactory`, `ObservadorMision` | Ciclo de vida Template Method de las tres misiones, navegación por turnos, cálculo de ruta en L e informe final. |
| `src.ElementosDelUniverso` | `ElementoDelUniverso`, `Colisionable`, `Asteroide`, `EstacionDeCarga`, `ColisionException` | Elementos espaciales del universo dinámico y sus efectos sobre la nave. |
| `src.destinoytablero` | `Tablero`, `Destino`, `Posicion`, `Observable`, `Observador` | Módulo provisto por la cátedra para grilla espacial, radares, turnos del entorno y notificaciones. |
| `src.vista` | `IVistaPrincipal`, `VentanaPrincipal`, `PanelTablero`, `PopupColision`, `DialogoInformeMision`, `IVistaLogin`, `VentanaLogin` | Capa de presentación en Swing, desacoplada del modelo y apta para proyección en aula. |
| `src.controlador` | `ControladorPrincipal`, `ControladorLogin` | Mediadores MVC que escuchan gestos de usuario (`ActionListener`) y coordinan operaciones con el modelo. |
| `src.Usuario` | `UsuarioDTO`, `UsuarioDAO`, `UsuarioDAOJDBC`, `ConexionBD`, `UsuarioService`, `UsuarioExcepciones` | Capa de acceso a datos JDBC (SQLite) y servicio de negocio para registro y autenticación de operadores. |
| `src.concurrencia.*` | `PlataformaDeCarga`, `GeneradoraDeEnergia`, `NaveConsumidora`, `Bateria`, `EventoVisorDTO`, `VentanaVisorUniverso`, `ControladorVisor` | Módulo autónomo del Visor del Universo Paralelo bajo Productor-Consumidor, MVC y Observer. |
| `src.main` | `MainAplicacion` | Punto de entrada que ensambla acceso autenticado y, tras validar credenciales, inicia el sistema principal. |

---

## 3. Patrones de Diseño Aplicados

### 3.1 Patrones del Núcleo (Entrega 1, evolucionados en Entrega 2)

- **State (`src.MotorWarp`):** encapsula las fases del Motor Warp (`DisponibleState`, `PreparandoState`, `EjecutandoSaltoWarpState`, `EnfriandoState`) detrás de `MotorState`. Elimina condicionales centralizados y `instanceof`, garantiza el cobro de combustible en salto normal (2), energía en salto Warp (5) y desgaste en enfriamiento (3), y resuelve el reinicio al cierre de misión mediante `reiniciarWarp()` en cada estado.
- **Decorator (`src.Tripulacion`):** evita la explosión combinatoria entre 4 cargos y 3 orígenes. `DecoratorTrip` envuelve a `Tripulante`, delega `getIdent()`, `getAntiguedad()`, `setAntiguedad()` y `esCapitan()` atravesando múltiples capas, y compone los subsidios mensuales (`Terricola` +20, `Vulcano` +30, `Marciano` +18).
- **Factory (`NaveFactory`, `TripulanteFactory`, `MisionFactory`):** centraliza la instanciación de naves, tripulantes decorados y misiones. Ningún cliente ni controlador instancia clases concretas ni depende de ellas.
- **Template Method (`src.Misiones`):** `Mision.realizarMision()` es `public final` y fija la secuencia `preparar() → ejecutar() → evaluarResultado() → cerrar()`. Centraliza validaciones, despegue, ciclo de turnos con `Thread.sleep`, condiciones de vida, captura de `MisionException` y armado del informe en `String`. Las subclases solo definen su nombre (`getNombre()`), su evaluación (`evaluarResultado()`) y, en `Mision_03`, el hook `definirObjetivo()`.

### 3.2 Patrones Incorporados en Entrega 2

- **Fachada (`SistemaEstelar`):** actúa como modelo de aplicación. Encapsula la flota, las fábricas, el `Tablero` compartido, el `Destino` único y `posicionesFlota` (estacionamiento de cada nave al alternar nave activa sin duplicar la posición dentro de `Nave`).
- **MVC (`src.vista` + `src.controlador`):** separación estricta en tres circuitos (Login, Comando Principal y Visor Concurrente). El modelo desconoce Swing y no imprime en consola; las vistas exponen datos planos y constantes `CMD_*`; los controladores coordinan gestos, lanzan las misiones en hilos secundarios para no congelar el EDT y traducen excepciones de dominio a alertas visuales.
- **Observer:** se aplica en tres niveles complementarios:
  1. `Tablero` (`Observable`) notifica a `VentanaPrincipal` (repintado de `PanelTablero`) y a `ControladorPrincipal` (actualización de telemetría, bitácora real y cartel de turno `NAVE`/`UNIVERSO`).
  2. `ObservadorMision` (interfaz Java pura) notifica al controlador la ruta en L planificada y el resultado efectivo de cada encuentro (`PopupColision`).
  3. `PlataformaDeCarga` notifica instantáneas inmutables (`EventoVisorDTO`) a `VentanaVisorUniverso`.
- **Double Dispatch (`src.ElementosDelUniverso` + `src.nave`):** resuelve las batallas entre naves sin `instanceof` mediante dos despachos polimórficos (`elemento.interactuar(nave)` → `nave.enfrentadoPorAtaque/Carguero/Explorador()`), implementando la tabla piedra-papel-tijera (`Ataque > Explorador > Carguero > Ataque`) y registrando victorias, pactos de paz o derrotas (`ColisionException`).
- **DAO + DTO + Servicio (`src.Usuario`):**
  - `UsuarioDTO` (inmutable, `Serializable`) transporta únicamente `username` y `password`.
  - `UsuarioDAO` / `UsuarioDAOJDBC` encapsulan consultas SQL parametrizadas (`PreparedStatement`) abriendo y cerrando `Connection`, `PreparedStatement` y `ResultSet` dentro de cada método mediante `try-with-resources`.
  - `UsuarioService` recibe `UsuarioDAO` por inyección de constructor y concentra las reglas de negocio (unicidad de usuario y validación de credenciales), mientras `VentanaLogin` valida el formato de los campos.
- **Productor-Consumidor con Monitores (`src.concurrencia`):** `PlataformaDeCarga` sincroniza hilos `GeneradoraDeEnergia` y `NaveConsumidora` (implementando `Runnable`) mediante métodos `synchronized`, condiciones de guarda en bucles `while` (para evitar despertares espurios y carreras), `wait()` y `notifyAll()`. Los hilos se crean dinámicamente en ejecución y se marcan como *daemon*.

---

## 4. Diseño por Contrato y Manejo de Errores

Se adopta el criterio de la cátedra diferenciando fallos de programación de reglas del dominio:

- **Precondiciones e invariantes (`assert` con `-ea`):** validan parámetros no nulos, rangos válidos (cantidades positivas en recursos, dimensiones, índices alineados en `SistemaEstelar`, capacidad de `PlataformaDeCarga`) y postcondiciones tras mutaciones de estado.
- **Reglas de negocio (excepciones propias):** `RecursosInsuficientesException`, `ExcesoRecursoException`, `MantenimientoException`, `MotorException` (`TransicionInvalidaException`, `TransicionRedundanteException`), `ColisionException`, `MisionException` (`PrepMisionException`), `AntiguedadNegativa` y `UsuarioExcepciones`.

---

## 5. Decisiones de Diseño Relevantes

1. **Base de datos SQLite embebida:** se eligió `sqlite-jdbc` gestionado por Maven en lugar de un servidor MySQL externo para garantizar ejecución reproducible en cualquier equipo de evaluación sin configuración previa de servicios.
2. **Bitácora como fuente de verdad sincronizada:** `Bitacora` protege sus lecturas y escrituras con `synchronized` devolviendo instantáneas inmodificables; `ControladorPrincipal` mantiene un índice de sincronización independiente por nave (`IdentityHashMap`) para reflejar todos los eventos reales (saltos, daño por asteroides, recargas efectivas, combates y mantenimiento) sin duplicados.
3. **Ejecución de misiones fuera del hilo de Swing:** `ControladorPrincipal` ejecuta `Mision.realizarMision()` en un hilo secundario *daemon* debido a las pausas `Thread.sleep(600)` entre medios turnos, y `VentanaPrincipal` canaliza todas las actualizaciones gráficas por `SwingUtilities.invokeLater(...)`.
4. **Posición por nave en `SistemaEstelar`:** la posición no se almacena en `Nave` para evitar doble fuente de verdad respecto de `Tablero` y conservar cohesión (las naves enemigas también heredan de `Nave`).