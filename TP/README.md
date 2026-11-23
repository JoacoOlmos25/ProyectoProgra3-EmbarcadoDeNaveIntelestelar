# Embarcado de Nave Interestelar — TPG Programación C (2026)

Sistema en Java (JDK 21 + Maven + Swing + JDBC) que modela el software embarcado de una nave interestelar tripulada operando en un universo dinámico por turnos, con control de acceso persistente, arquitectura MVC + Observer y módulo de concurrencia Productor-Consumidor.

---

## Estructura del repositorio

```text
ProyectoProgra3-EmbarcadoDeNaveIntelestelar/
├── README.md
├── .gitignore
└── TP/
    ├── pom.xml                              # Proyecto Maven (artifactId: embarcado-nave-interestelar)
    ├── documentos/
    │   ├── Informe.md                       # Informe de arquitectura, patrones y decisiones (E1 + E2)
    │   ├── README_DestinoYTablero.md        # Documentación del módulo provisto por la cátedra
    │   ├── uso-ia.md                        # Registro formal de uso de IA
    │   ├── database/
    │   │   └── schema.sql                   # Esquema SQLite de la tabla usuarios
    │   └── UMLs/                            # Diagramas de clases, estados y secuencia (.puml, .png, diagramas.md)
    └── src/main/java/src/                   # Código fuente (paquete raíz: src)
        ├── AC/                              # Fachada SistemaEstelar, Asistente, Bitacora, Evento
        ├── ElementosDelUniverso/            # ElementoDelUniverso, Colisionable, Asteroide, EstacionDeCarga
        ├── Misiones/                        # Template Method (Mision, Mision_01/02/03), MisionFactory, ObservadorMision
        ├── MotorWarp/                       # Patrón State (MotorWarp, MotorState y sus 4 estados)
        ├── Tripulacion/                     # Decorator (Tripulante, cargos, orígenes) y TripulanteFactory
        ├── Usuario/                         # Persistencia DAO/DTO + JDBC SQLite (ConexionBD, UsuarioService)
        ├── concurrencia/                    # Productor-Consumidor con monitores (MVC + Observer)
        ├── controlador/                     # ControladorPrincipal, ControladorLogin
        ├── destinoytablero/                 # Módulo de universo dinámico provisto por la cátedra
        ├── main/                            # MainAplicacion (punto de entrada único)
        ├── nave/                            # Nave, Ataque, Carguero, Explorador, NaveFactory, Comandable
        └── vista/                           # Vistas Swing desacopladas (IVistaPrincipal, IVistaLogin, etc.)

1. Requisitos de ejecución
JDK 21 (OpenJDK u Oracle JDK 21).

IntelliJ IDEA (recomendado, incluye Maven integrado) o Apache Maven 3.9+ instalado en el PATH del sistema.

Conexión a internet en la primera compilación para que Maven descargue la dependencia org.xerial:sqlite-jdbc declarada en TP/pom.xml.

No se requiere instalar ningún servidor de base de datos externo: se utiliza SQLite embebido.

2. Comandos Maven y configuración paso a paso
Abrir la carpeta raíz del repositorio (ProyectoProgra3-EmbarcadoDeNaveIntelestelar) en IntelliJ IDEA.

En el panel Project, hacer clic derecho sobre TP/pom.xml → Add as Maven Project (o desde la pestaña lateral derecha Maven apretar + y seleccionar TP/pom.xml). Debe aparecer el proyecto embarcado-nave-interestelar.

Ir a File → Project Structure → Project y verificar que el SDK seleccionado sea 21.

Ir a File → Project Structure → Modules y verificar que haya un único módulo (embarcado-nave-interestelar) y que, en la pestaña Sources, solo src/main/java esté marcado en azul (Sources).

Abrir la pestaña lateral derecha Maven (ícono m) → embarcado-nave-interestelar → Lifecycle:

Hacer doble clic en clean.

Hacer doble clic en compile.

La consola de Build debe finalizar con BUILD SUCCESS y generar los compilados en TP/target/classes/.

3. Forma de iniciar el sistema
El punto de entrada único es src.main.MainAplicacion. Debe ejecutarse con la opción de JVM -ea (Enable Assertions) para activar en tiempo de ejecución todos los contratos implementados con assert.

Desde IntelliJ IDEA
Abrir TP/src/main/java/src/main/MainAplicacion.java.

Ejecutar una primera vez desde el triángulo verde Run al lado del método main para generar la configuración de ejecución.

Ir a Run → Edit Configurations... y seleccionar MainAplicacion:

Hacer clic en Modify options (arriba a la derecha) → marcar Add VM options.

En el nuevo campo VM options (encima de Program arguments), escribir: -ea.

Presionar Apply y OK.

Ejecutar nuevamente MainAplicacion.

Desde terminal (con Maven en el PATH)
cd TP
mvn clean compile
mvn exec:exec -Dexec.executable="java" -Dexec.args="-ea -cp %classpath src.main.MainAplicacion"

4. Forma de ejecutar las verificaciones
Al iniciar MainAplicacion se abre primero el módulo de acceso y, tras autenticar un operador, el Centro de Comando Estelar:

    A. Control de Acceso y Persistencia (DAO/DTO + JDBC SQLite + UsuarioService):

    Intentar ingresar o registrar con campos vacíos: VentanaLogin rechaza el formato sin consultar la base de datos.

    En la pestaña Registrar Usuario, dar de alta un operador nuevo y volver a intentar el mismo nombre (verifica rechazo de usuario duplicado en UsuarioService).

    En Iniciar Sesión, ingresar una contraseña incorrecta (verifica rechazo de credenciales y limpieza de clave) y luego las credenciales válidas para abrir la ventana principal.

    Cerrar y volver a abrir la aplicación e iniciar sesión con el usuario registrado para comprobar la persistencia en usuarios.db.

    B. Flota (Factory) y Tripulación (Decorator):

    Crear naves Explorador, Carguero y Ataque desde el panel superior y alternar entre ellas en Nave en Uso (cada nave conserva recursos, tripulación, bitácora y última posición propios).

    Asignar tripulantes combinando los 4 cargos (Capitan, Teniente, Alferez, Consejero) con los 3 orígenes (Terricola, Vulcano, Marciano), verificando la liquidación mensual de haberes en la pestaña Tripulación y el rechazo ante identidades repetidas o segundo capitán.

    C. Misiones (Template Method), Motor Warp (State), Universo Dinámico (Observer) y Batallas (Double Dispatch):

    Seleccionar e iniciar M-01 (Intercepción y Asistencia), M-02 (Recolección) y M-03 (Retorno a Punto Seguro con reacondicionamiento final 100 / 100 / 0%).

    Verificar en tiempo real: ruta planificada en L, rotación de la nave, alternancia del indicador de turno (NAVE / UNIVERSO), actualización de las 4 tarjetas de telemetría, popups de encuentro con detalle efectivo (Asteroide, EstacionDeCarga y combates entre naves), registro temporal completo en la pestaña Bitácora e informe final en Informes.

    D. Visor del Universo Paralelo (Concurrencia — Productor-Consumidor con Monitores):

    Presionar Abrir Visor del Universo Paralelo en la parte inferior de la ventana principal.

    Agregar dinámicamente Generadoras de Energía y Naves Consumidoras en tiempo de ejecución, verificando los estados visuales (trabajando, solicitando, esperando en wait(), operando bajo synchronized), los contadores en vivo y el log de eventos.
5. Configuración local necesaria

Base de datos automática: al primer intento de registro o inicio de sesión, ConexionBD.java crea automáticamente el archivo SQLite usuarios.db y la tabla usuarios(username, password) (esquema documentado en TP/documentos/database/schema.sql).

No se requiere instalar servicios externos ni configurar variables de entorno o contraseñas de motor de base de datos.