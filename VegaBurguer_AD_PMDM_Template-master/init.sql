-- init.sql
-- 1. Crear la Base de Datos
CREATE DATABASE IF NOT EXISTS vegaburguer;
USE vegaburguer;

-- 2. Tabla de Categorías
CREATE TABLE IF NOT EXISTS Categorias (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    imgPath VARCHAR(255),
    activa BOOLEAN DEFAULT TRUE
);

-- 3. Tabla de Productos (Relacionada con Categorías)
CREATE TABLE IF NOT EXISTS Productos (
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    imgPath VARCHAR(255),
    categoria_id VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (categoria_id) REFERENCES Categorias(id)
);

-- 4. Tabla de Dependientes (Adaptada a tu modelo Kotlin)
DROP TABLE IF EXISTS Dependientes;
CREATE TABLE Dependientes (
    id VARCHAR(50) PRIMARY KEY,       -- ID como String
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    image_path VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    is_admin BOOLEAN DEFAULT FALSE    -- Campo para roles
);

-- 5. Tabla de Pedidos
CREATE TABLE IF NOT EXISTS Pedidos (
    id VARCHAR(50) PRIMARY KEY,
    cliente VARCHAR(100),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    total DECIMAL(10, 2)
);

-- 6. Tabla de Líneas de Pedido (Detalle)
CREATE TABLE IF NOT EXISTS LineasPedido (
    id VARCHAR(50) PRIMARY KEY,
    pedido_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES Pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES Productos(id)
);

-- DATOS DE PRUEBA --
INSERT INTO Categorias (nombre, imgPath) VALUES ('Hamburguesas', 'burger.png'), ('Bebidas', 'soda.png');

-- Usuario(Admin)
INSERT INTO Dependientes (id, name, email, password, image_path, enabled, is_admin)
VALUES ('1', 'Admin', 'admin@vegaburguer.com', '1234', 'default.png', TRUE, TRUE);