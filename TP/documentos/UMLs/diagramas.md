# Diagramas del modelo de dominio

Diagramas en Mermaid generados a partir del código (sin modificarlo). Se excluyen vistas, controladores, `Tripulante` y sus decoradores.

## 1. UML: Comandable, Nave, Asistente y Misión

```mermaid
classDiagram
    direction TB

    class ElementoDelUniverso {
        <<interface>>
        +interactuar(Colisionable) void
    }
    class Colisionable {
        <<interface>>
        +recibirDanio(int) void
        +recibirAyuda(int, int) void
        +enfrentadoPorExplorador() void
        +enfrentadoPorCarguero() void
        +enfrentadoPorAtaque() void
    }
    class FuenteDeRecursos {
        <<interface>>
        +getCombustible() int
        +getEnergia() int
        +consumirCombustible(int) void
        +consumirEnergia(int) void
        +cargarCombustible(int) void
        +cargarEnergia(int) void
    }
    class Mantenible {
        <<interface>>
        +getDesgaste() int
        +incrementaDesgaste(int) void
        +requiereMantenimiento() boolean
        +realizarMantenimiento() void
    }
    class Comandable {
        <<interface>>
        +setBitacora(Bitacora) void
        +consultarMotorDisponible() boolean
        +saltar(Tablero, ArrayList~Posicion~) void
        +saltarWarp(Tablero, ArrayList~Posicion~) void
        +reacondicionamientoTotal() void
        +reiniciarMotor() void
        +insertarTripulante(Tripulante) void
        +getTripulacion() List~Tripulante~
    }

    ElementoDelUniverso <|-- Colisionable
    Colisionable <|-- Comandable
    FuenteDeRecursos <|-- Comandable
    Mantenible <|-- Comandable

    class Nave {
        <<abstract>>
        -int combustible
        -int energia
        -int desgaste
        #MotorWarp motorWarp
        #Bitacora bitacoraNave
        +Nave(int, int)
        #perderCombate(String) void
        #ganarCombate(String) void
        #pactarPaz(String) void
    }
    class Explorador
    class Carguero
    class Ataque

    Comandable <|.. Nave
    Nave <|-- Explorador
    Nave <|-- Carguero
    Nave <|-- Ataque

    class MotorWarp {
        -MotorState estado
        -FuenteDeRecursos recursos
        -Mantenible estructuraNave
        +saltar(Tablero, ArrayList~Posicion~) void
        +saltarWarp(Tablero, ArrayList~Posicion~) void
        +reiniciar() void
        +consultaMotorDisponible() boolean
    }
    Nave *-- MotorWarp : crea MotorWarp(this, this)
    MotorWarp ..> FuenteDeRecursos : consume energia/combustible
    MotorWarp ..> Mantenible : incrementa desgaste

    class Asistente {
        -Comandable nave
        -Bitacora bitacora
        +Asistente(Nave)
        +movimiento(Tablero, ArrayList~Posicion~) void
        +inspeccionarPosicionActual(Tablero) void
        +despegue(int, int, int) void
        +transferirRecursos(int, int) void
        +reacondicionarNave() void
        +reiniciarMotor() void
        +realizarMantenimiento() void
        +registrarEvento(String, String) void
    }
    Asistente o-- "1" Comandable : nave (depende solo de la interfaz)
    Asistente *-- Bitacora
    Nave --> Bitacora : bitacoraNave
    Asistente ..> ElementoDelUniverso : interactuar(nave)

    class Mision {
        <<abstract>>
        #int combustibleBase
        #int energiaBase
        #int desgasteBase
        #Asistente ac
        #Posicion objetivo
        #ArrayList~Posicion~ ruta
        +setObservador(ObservadorMision) void
        +realizarMision() void
        #preparar() void
        #definirObjetivo() Posicion
        #ejecutar() void
        #evaluarResultado()* void
        #getNombre()* String
        #cerrar() void
    }
    class Mision_01
    class Mision_02
    class Mision_03
    class ObservadorMision {
        <<interface>>
        +rutaPlanificada(List~Posicion~) void
        +interaccionDetectada(String, String) void
    }
    class Tablero
    class Destino

    Mision <|-- Mision_01
    Mision <|-- Mision_02
    Mision <|-- Mision_03
    Mision --> "1" Asistente : ac
    Mision --> ObservadorMision
    Mision --> Tablero
    Mision --> Destino
```

Notas:
- `Colisionable` extiende `ElementoDelUniverso`, por eso una `Nave` (vía `Comandable`) también es un elemento con `interactuar(...)`. Eso habilita el *double dispatch* entre un elemento y la nave.
- `Asistente` recibe una `Nave` en el constructor, pero la guarda como `Comandable`.
- `Mision` nunca toca la nave: todo pasa por `Asistente`.

## 2. Diagrama de estados del MotorWarp

Patrón State: `MotorWarp` delega en un `MotorState` (`DisponibleState`, `PreparandoState`, `EjecutandoSaltoWarpState`, `EnfriandoState`). El evento `saltar` / `saltarWarp` es el que dispara la mayoría de las transiciones.

```mermaid
stateDiagram-v2
    [*] --> Disponible : new MotorWarp

    Disponible --> Preparando : saltar / saltarWarp<br/>(prepararWarp + salto normal de 1 casillero)

    Preparando --> EjecutandoSaltoWarp : saltar / saltarWarp<br/>[energia > 5] iniciarWarp + salto normal
    Preparando --> Preparando : saltar / saltarWarp<br/>[energia <= 5] ignicion fallida + salto normal

    EjecutandoSaltoWarp --> EjecutandoSaltoWarp : saltar<br/>(salto normal de 1 casillero)
    EjecutandoSaltoWarp --> Enfriando : saltarWarp [energia > 5]<br/>consume 5 de energia, avanza hasta 2 casilleros
    EjecutandoSaltoWarp --> Preparando : saltarWarp [energia <= 5]<br/>salto abortado

    Enfriando --> Disponible : saltar / saltarWarp<br/>enfriarWarp: desgaste +3 (la nave no se mueve)

    Preparando --> Disponible : reiniciar (fin de mision)
    EjecutandoSaltoWarp --> Disponible : reiniciar (fin de mision)
    Enfriando --> Disponible : reiniciar<br/>completa enfriamiento, desgaste +3
    Enfriando --> Enfriando : reiniciar<br/>[desgaste excedido] MantenimientoException

    note right of Disponible
        motorDisponible() = true
        solo en este estado
    end note
```

Operaciones directas inválidas (lanzan excepción, no cambian el estado):

| Estado | `prepararWarp` | `iniciarWarp` | `ejecutandoWarp` | `enfriarWarp` |
|---|---|---|---|---|
| Disponible | → Preparando | `TransicionInvalida` | `TransicionInvalida` | `TransicionRedundante` |
| Preparando | `TransicionRedundante` | → EjecutandoSaltoWarp (si hay energía) | `TransicionInvalida` | `TransicionInvalida` |
| EjecutandoSaltoWarp | `TransicionInvalida` | `TransicionRedundante` | → Enfriando / Preparando | `TransicionInvalida` |
| Enfriando | `TransicionInvalida` | `TransicionInvalida` | `TransicionRedundante` | → Disponible |

## 3. Flujo de la misión → Asistente → Nave (vía Comandable)

### 3.1 Flujo general (`Mision.realizarMision`, método template)

```mermaid
flowchart TD
    A([realizarMision]) --> P[preparar]
    P --> P1{"Recursos suficientes,<br/>sin mantenimiento pendiente<br/>y motor disponible?<br/>(vía Asistente)"}
    P1 -- no --> F["PrepMisionException<br/>(MisionException)"]
    P1 -- si --> P2["definirObjetivo<br/>y calcular ruta en L"]
    P2 --> P3{"Nave ya en<br/>el objetivo?"}
    P3 -- si --> F
    P3 -- no --> P4["notificar rutaPlanificada<br/>al ObservadorMision"]
    P4 --> E["ejecutar:<br/>ac.despegue(comb, energia, desgaste)"]
    E --> E1{"Recursos / desgaste<br/>suficientes?"}
    E1 -- no --> F
    E1 -- si --> C

    subgraph C [cicloDeTravesia]
        direction TB
        L{"Llego al<br/>objetivo?"} -- no --> M{"Turnos agotados?"}
        M -- si --> X["MisionException"]
        M -- no --> T1["turnoDeLaNave:<br/>inspeccionarPosicion,<br/>ac.movimiento,<br/>inspeccionarPosicion"]
        T1 --> V["verificarCondicionDeVida<br/>(desgaste, energia, combustible)"]
        V --> W1[esperarTurno]
        W1 --> L2{"Llego al<br/>objetivo?"}
        L2 -- no --> U["turnoDelUniverso:<br/>destino.turnoDestino"]
        U --> W2[esperarTurno]
        W2 --> L
        L2 -- si --> L
    end

    L -- si --> R["evaluarResultado<br/>(propio de cada Mision_0X)"]
    R --> OK["exitosa = true"]
    X --> F
    C -. ColisionException / MotorException /<br/>RecursosInsuficientes / Mantenimiento .-> F
    F --> FF["exitosa = false<br/>registrar 'Mision fallida'"]
    OK --> CL
    FF --> CL["cerrar:<br/>ac.reiniciarMotor,<br/>armar informe final,<br/>registrar 'Mision cerrada'"]
    CL --> Z([fin])
```

### 3.2 Comunicación Mision → Asistente → Comandable (turno de la nave)

```mermaid
sequenceDiagram
    autonumber
    participant M as Mision
    participant A as Asistente
    participant T as Tablero
    participant C as Comandable (Nave)
    participant MW as MotorWarp
    participant S as MotorState actual
    participant EU as ElementoDelUniverso

    Note over M,C: preparar()
    M->>A: getCombustibleNave / getEnergiaNave
    A->>C: getCombustible / getEnergia
    M->>A: consultaReqMantenimiento / consultaMotorDisp
    A->>C: requiereMantenimiento / consultarMotorDisponible
    C->>MW: consultaMotorDisponible
    MW->>S: motorDisponible

    Note over M,C: ejecutar()
    M->>A: despegue(combustible, energia, desgaste)
    A->>C: consumirCombustible / consumirEnergia / incrementaDesgaste

    loop cada turno de la nave
        M->>T: inspeccionarPosicion()
        T-->>M: elemento (o null)
        opt elemento nuevo
            M->>A: registrarEvento("Encuentro")
            M->>A: inspeccionarPosicionActual(tablero)
            A->>T: inspeccionarPosicion()
            A->>EU: interactuar(nave)
            EU->>C: double dispatch: recibirDanio / recibirAyuda /<br/>enfrentadoPorX (Colisionable)
        end

        M->>A: movimiento(tablero, ruta)
        A->>T: observarAlrededores(), getPosicionNave()
        alt siguiente casillero vacio
            A->>C: saltar(tablero, ruta)
            C->>MW: saltar
            MW->>S: saltar
        else siguiente casillero ocupado
            A->>C: saltarWarp(tablero, ruta)
            C->>MW: saltarWarp
            MW->>S: saltarWarp
        end
        S->>MW: saltoNormal / mover (cambia de estado con setEstado)
        MW->>T: moverNave(posicion)

        M->>A: getDesgasteNave / getEnergiaNave / getCombustibleNave
        A->>C: getDesgaste / getEnergia / getCombustible
    end

    M->>M: evaluarResultado() (propio de cada mision)
    Note over M,C: cerrar()
    M->>A: reiniciarMotor()
    A->>C: reiniciarMotor()
    C->>MW: reiniciar()
    MW->>S: reiniciarWarp() (vuelve a Disponible)
```

Notas:
- Las excepciones (`ColisionException`, `MotorException`, `RecursosInsuficientesException`, `MantenimientoException`) se traducen a `MisionException` en `turnoDeLaNave` y `ejecutar`, y llevan al cierre con misión fallida.
- `Asistente` decide entre `saltar` y `saltarWarp` según el radar (`observarAlrededores`): casillero siguiente vacío → `saltar`; ocupado → `saltarWarp`. La política de transiciones queda en los estados del motor (ver diagrama 2).
