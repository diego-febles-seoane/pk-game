-- Eliminar la tabla usuario si existe
DROP TABLE IF EXISTS usuario;

-- Crear tabla de usuarios
CREATE TABLE usuario (
    email TEXT PRIMARY KEY,
    nombre_usuario TEXT NOT NULL UNIQUE,
    contrasenia TEXT NOT NULL,
    victorias_totales INTEGER DEFAULT 0,
    derrotas_totales INTEGER DEFAULT 0,
    mayor_racha INTEGER DEFAULT 0,
    racha_actual INTEGER DEFAULT 0,
    derrotas_consecutivas INTEGER DEFAULT 0,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insertar usuarios de ejemplo
INSERT INTO usuario (email, nombre_usuario, contrasenia, victorias_totales, derrotas_totales, mayor_racha) 
VALUES 
    ('admin@pkgame.com', 'admin', 'admin123', 10, 5, 8),
    ('demo@pkgame.com', 'demo', 'demo123', 3, 2, 3),
    ('test@pkgame.com', 'test', 'test123', 0, 0, 0);

-- Crear índices para optimizar consultas
CREATE INDEX idx_nombre_usuario ON usuario(nombre_usuario);
CREATE INDEX idx_email ON usuario(email);

-- Consultar todos los usuarios
SELECT * FROM usuario;
