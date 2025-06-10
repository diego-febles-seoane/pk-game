-- Insertar movimientos de Squirtle (si no existen)
INSERT OR IGNORE INTO movimientos (nombre, potencia, precision, tipo) VALUES
('Placaje', 40, 100, 'Normal'),
('Pistola Agua', 40, 100, 'Agua'),
('Refugio', 0, 100, 'Agua'),
('Burbuja', 40, 100, 'Agua');

-- Insertar estadísticas de Squirtle
INSERT INTO estadisticas (ataque, defensa, vida, velocidad) VALUES
(48, 65, 44, 43);

-- Insertar Squirtle
INSERT INTO pokemon (nombre, tipo1, tipo2, estadisticas_id) VALUES
('Squirtle', 'Agua', NULL, (SELECT last_insert_rowid()));

-- Asignar movimientos a Squirtle
INSERT INTO pokemon_movimientos (pokemon_id, movimiento_id) VALUES
((SELECT id FROM pokemon WHERE nombre = 'Squirtle'), (SELECT id FROM movimientos WHERE nombre = 'Placaje')),
((SELECT id FROM pokemon WHERE nombre = 'Squirtle'), (SELECT id FROM movimientos WHERE nombre = 'Pistola Agua')),
((SELECT id FROM pokemon WHERE nombre = 'Squirtle'), (SELECT id FROM movimientos WHERE nombre = 'Refugio')),
((SELECT id FROM pokemon WHERE nombre = 'Squirtle'), (SELECT id FROM movimientos WHERE nombre = 'Burbuja'));

-- Verificar que Squirtle se insertó correctamente
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
WHERE p.nombre = 'Squirtle'
GROUP BY p.id, p.nombre, p.tipo1, p.tipo2, e.ataque, e.defensa, e.vida, e.velocidad;

