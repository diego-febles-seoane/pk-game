-- Insertar movimientos de Bulbasaur (si no existen)
INSERT OR IGNORE INTO movimientos (nombre, potencia, precision, tipo) VALUES
('Placaje', 40, 100, 'Normal'),
('Látigo Cepa', 45, 100, 'Planta'),
('Gruñido', 0, 100, 'Normal'),
('Hoja Afilada', 55, 95, 'Planta');

-- Insertar estadísticas de Bulbasaur
INSERT INTO estadisticas (ataque, defensa, vida, velocidad) VALUES
(49, 49, 45, 45);

-- Insertar Bulbasaur
INSERT INTO pokemon (nombre, tipo1, tipo2, estadisticas_id) VALUES
('Bulbasaur', 'Planta', 'Veneno', (SELECT last_insert_rowid()));

-- Obtener el ID de Bulbasaur
-- Asignar movimientos a Bulbasaur
INSERT INTO pokemon_movimientos (pokemon_id, movimiento_id) VALUES
((SELECT id FROM pokemon WHERE nombre = 'Bulbasaur'), (SELECT id FROM movimientos WHERE nombre = 'Placaje')),
((SELECT id FROM pokemon WHERE nombre = 'Bulbasaur'), (SELECT id FROM movimientos WHERE nombre = 'Látigo Cepa')),
((SELECT id FROM pokemon WHERE nombre = 'Bulbasaur'), (SELECT id FROM movimientos WHERE nombre = 'Gruñido')),
((SELECT id FROM pokemon WHERE nombre = 'Bulbasaur'), (SELECT id FROM movimientos WHERE nombre = 'Hoja Afilada'));

-- Verificar que Bulbasaur se insertó correctamente
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
WHERE p.nombre = 'Bulbasaur'
GROUP BY p.id, p.nombre, p.tipo1, p.tipo2, e.ataque, e.defensa, e.vida, e.velocidad;

