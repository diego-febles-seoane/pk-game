-- Eliminar la tabla palabra si existe
DROP TABLE IF EXISTS usuario;


-- Crear tabla de usuarios
CREATE TABLE usuario (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_usuario TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    contrasenia TEXT NOT NULL,
    victorias_totales INTEGER DEFAULT 0,
    derrotas_totales INTEGER DEFAULT 0,
    mayor_racha INTEGER DEFAULT 0,
    racha_actual INTEGER DEFAULT 0,
    derrotas_consecutivas INTEGER DEFAULT 0
);

-- Insertar usuarios de ejemplo
INSERT INTO usuario (nombre_usuario, email, contrasenia) VALUES
    ('1', '1@gmail.com', '1', 1);
