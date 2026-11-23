-- Esquema de persistencia para el módulo de Control de Acceso (Entrega 2)
-- Motor: SQLite embebido (driver org.xerial:sqlite-jdbc)
-- Alcance: persiste únicamente credenciales de operadores (no se persisten naves, tripulantes ni misiones).

CREATE TABLE IF NOT EXISTS usuarios (
    username TEXT PRIMARY KEY,
    password TEXT NOT NULL
);