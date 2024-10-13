USE prueba;

-- Desactivar temporalmente las comprobaciones de campos no nulos y foreign keys
SET SQL_MODE = '';
SET FOREIGN_KEY_CHECKS = 0;

-- Crear usuarios
INSERT INTO usuario (id, primerNombre, apellido, tipo, valor, activo) VALUES
(100, 'Juan', 'Perez', 'DNI', '12345678', true),
(200, 'Maria', 'Gonzalez', 'DNI', '23456789', true),
(300, 'Carlos', 'Rodriguez', 'DNI', '34567890', true),
(400, 'Ana', 'Martinez', 'DNI', '45678901', true);

-- Crear tarjetas
INSERT INTO tarjeta (id, idRecipiente) VALUES
(UUID_TO_BIN(UUID()), 100),
(UUID_TO_BIN(UUID()), 200),
(UUID_TO_BIN(UUID()), 300),
(UUID_TO_BIN(UUID()), 400);

-- Crear heladeras
INSERT INTO heladera (id, nombre, barrio, capacidadEnViandas, fechaInstalacion, idColaborador, latitud, longitud) VALUES
(100, 'Heladera A', 'Palermo', 100, NOW(), 100, 1, 1),
(200, 'Heladera B', 'Recoleta', 100, NOW(), 200, 2, 2),
(300, 'Heladera C', 'San Telmo', 100, NOW(), 300, 3, 3);

-- Crear solicitudes de apertura
INSERT INTO solicitudAperturaPorConsumicion (id, idTarjeta, idHeladera, fechaUsada, fechaCreacion, fechaVencimiento, idVianda) VALUES
(100, (SELECT id FROM tarjeta LIMIT 1),          100, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 1),
(200, (SELECT id FROM tarjeta LIMIT 1 OFFSET 1), 100, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 2),
(300, (SELECT id FROM tarjeta LIMIT 1 OFFSET 2), 200, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 3),
(400, (SELECT id FROM tarjeta LIMIT 1 OFFSET 3), 300, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 4),
(500, (SELECT id FROM tarjeta LIMIT 1),          200, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 5),
(600, (SELECT id FROM tarjeta LIMIT 1 OFFSET 1), 300, NOW(), NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 6);

-- Restaurar el modo SQL normal
SET SQL_MODE = DEFAULT;
SET FOREIGN_KEY_CHECKS = 1;
