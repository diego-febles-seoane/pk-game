-- Eliminar las tablas si existen
DROP TABLE IF EXISTS pokemon_movimientos;
DROP TABLE IF EXISTS pokemon;
DROP TABLE IF EXISTS movimientos;
DROP TABLE IF EXISTS estadisticas;

-- Crear tabla de estadísticas
CREATE TABLE estadisticas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    ataque INTEGER NOT NULL,
    defensa INTEGER NOT NULL,
    vida INTEGER NOT NULL,
    velocidad INTEGER NOT NULL
);

-- Crear tabla de movimientos
CREATE TABLE movimientos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    potencia INTEGER NOT NULL,
    precision INTEGER NOT NULL,
    tipo TEXT NOT NULL
);

-- Crear tabla de pokemon
CREATE TABLE pokemon (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    tipo1 TEXT NOT NULL,
    tipo2 TEXT,
    estadisticas_id INTEGER NOT NULL,
    FOREIGN KEY (estadisticas_id) REFERENCES estadisticas(id)
);

-- Crear tabla de relación pokemon-movimientos (máximo 4 movimientos por pokemon)
CREATE TABLE pokemon_movimientos (
    pokemon_id INTEGER NOT NULL,
    movimiento_id INTEGER NOT NULL,
    PRIMARY KEY (pokemon_id, movimiento_id),
    FOREIGN KEY (pokemon_id) REFERENCES pokemon(id),
    FOREIGN KEY (movimiento_id) REFERENCES movimientos(id)
);

-- Insertar movimientos de Charmander
INSERT INTO movimientos (nombre, potencia, precision, tipo) VALUES
('Arañazo', 40, 100, 'Normal'),
('Ascuas', 40, 100, 'Fuego'),
('Gruñido', 0, 100, 'Normal'),
('Furia Dragón', 40, 100, 'Dragón');

-- Insertar estadísticas de Charmander
INSERT INTO estadisticas (ataque, defensa, vida, velocidad) VALUES
(52, 43, 39, 65);

-- Insertar Charmander
INSERT INTO pokemon (nombre, tipo1, tipo2, estadisticas_id) VALUES
('Charmander', 'Fuego', NULL, 1);

-- Asignar movimientos a Charmander
INSERT INTO pokemon_movimientos (pokemon_id, movimiento_id) VALUES
(1, 1), -- Arañazo
(1, 2), -- Ascuas
(1, 3), -- Gruñido
(1, 4); -- Furia Dragón

-- Consultar Charmander con sus estadísticas y movimientos
SELECT 
    p.id,
    p.nombre,
    p.tipo1,
    p.tipo2,
    e.ataque,
    e.defensa,
    e.vida,
    e.velocidad,
    GROUP_CONCAT(m.nombre || ' (' || m.potencia || '/' || m.precision || '/' || m.tipo || ')') as movimientos
FROM pokemon p
JOIN estadisticas e ON p.estadisticas_id = e.id
JOIN pokemon_movimientos pm ON p.id = pm.pokemon_id
JOIN movimientos m ON pm.movimiento_id = m.id
WHERE p.nombre = 'Charmander'
GROUP BY p.id, p.nombre, p.tipo1, p.tipo2, e.ataque, e.defensa, e.vida, e.velocidad;

