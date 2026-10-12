INTRODUCCION Y OBJETIVOS

El presente informe documenta el diseño, la arquitectura y las decisiones de implementación del sistema de simulación espacial en Java. El objetivo central es modelar una nave espacial interestelar capaz de gestionar recursos críticos (combustible y energía), soportar desgaste operativo, controlar saltos hiperespaciales mediante un Motor Warp, registrar eventos en una bitácora independiente por instancia (mediante inyección de dependencias) y administrar una tripulación diversa con diferentes rangos y orígenes planetarios.

ARQUITECTURA GENERAL Y PAQUETES

Estructurado en cinco paquetes principales:

Nave: Clase abstracta con sus especializaciones (Ataque, Carguero, Explorador), la fábrica NaveFactory y las excepciones asociadas a recursos y mantenimiento. Ahora la Nave recibe su Bitácora por inyección y la propaga hacia sus componentes internos.

Motor Warp: Implementa el ciclo de vida del Motor Warp mediante el patrón State (MotorState, MotorWarp, DisponibleState, EnWarpState, EnfriandoState) y sus excepciones de transición.

Tripulación: Modela los cargos dentro de la nave (Capitan, Teniente, Alferez, Consejero), su especie/origen mediante el patrón Decorator (DecoratorTrip, Terricola, Marciano, Vulcano) y el control de antigüedad.

Asistente de Comando: Subsistema de telemetría y auditoría compuesto por Asistente, Bitacora y Evento. El Asistente actúa como el orquestador principal: instancia su propia Bitácora evitando estados globales y coordina las referencias con la Nave.

Misiones: Define el contrato base para los requerimientos de combustible, energía y desgaste necesarios para ejecutar trayectos espaciales, comunicándose con el Asistente para la validación de recursos y el registro de bitácora.

PATRONES DE DISEÑO APLICADOS

PATRON STATE

Busca controlar de forma segura las transiciones del motor Warp sin dispersar banderas booleanas ni estructuras condicionales anidadas en la nave. La clase MotorState(interfaz) encapsula las transiciones que estan permitidas y cuales representan un error.

PATRON DECORATOR

Un tripulante posee un rol jerárquico y un origen biológico/planetario. La herencia simple generaría una explosión de clases combinatorias. Para ello tripulante acctua como un componente abstracto y de este se definen los roles concretos y con un decorador definir los orgines para los objetos que se creen.

PATRON FACTORY

Desacoplar a los clientes que solicitan naves de las clases concretas e instanciaciones directas. La clase NaveFactory centraliza la creación, instanciando la subclase adecuada con una cadena asociada al tipo de nave. 

PATRÓN TEMPLATE METHOD

Aplicado en la clase Mision mediante el método realizarMision(). Este define el esqueleto secuencial e inalterable del algoritmo (preparar, ejecutar, evaluarResultado, cerrar, informe), delegando la implementación concreta de los pasos intermedios a cada subclase de misión específica y centralizando la captura de excepciones y registros en la bitácora.]