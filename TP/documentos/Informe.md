INTRODUCCION Y OBJETIVOS

El presente informe documenta el diseño, la arquitectura y las decisiones de implementación del sistema de simulación espacial en Java. El objetivo central es modelar una nave espacial interestelar capaz de gestionar recursos críticos (combustible y energía), soportar desgaste operativo, controlar saltos hiperespaciales mediante un motor de curvatura, registrar eventos en una bitácora centralizada y administrar una tripulación diversa con diferentes rangos y orígenes planetarios.

ARQUITECTURA GENERAL Y PAQUETES

Estructurado en cinco paquetes principales:

1. Nave: Clase abstracta con susespecializaciones (Ataque, Carguero, Explorador), la fábrica NaveFactory y las excepciones asociadas a recursos y mantenimiento.
2. Motor Warp: Implementa el ciclo de vida del motor de curvatura mediante el patrón State (MotorState, MotorWarp, DisponibleState, EnWarpState, EnfriandoState) y sus excepciones de transición.
3. Tripulación: Modela los cargos dentro de la nave (Capitan, Teniente, Alferez, Consejero), su especie/origen mediante el patrón Decorator (DecoratorTrip, Terricola, Marciano, Vulcano) y el control de antigüedad.
4. Asistente de Comando: Subsistema de telemetría y auditoría compuesto por Asistente, Bitacora y Evento.
5. Misiones: Define el contrato base para los requerimientos de combustible, energía y desgaste necesarios para ejecutar trayectos espaciales.

PATRONES DE DISEÑO APLICADOS

1. PATRON STATE

   Busca controlar de forma segura las transiciones del motor Warp sin dispersar banderas booleanas ni estructuras condicionales anidadas en la nave. La clase MotorState(interfaz) encapsula las transiciones que estan permitidas y cuales representan un error.

2. PATRON DECORATOR

   Un tripulante posee un rol jerárquico y un origen biológico/planetario. La herencia simple generaría una explosión de clases combinatorias. Para ello tripulante acctua como un componente abstracto y de este se definen los roles concretos y con un decorador definir los orgines para los objetos que se creen    

3. PATRON FACTORY

   Desacoplar a los clientes que solicitan naves de las clases concretas e instanciaciones directas. La clase NaveFactory centraliza la creación, instanciando la subclase adecuada con una cadena asociada al tipo de nave.