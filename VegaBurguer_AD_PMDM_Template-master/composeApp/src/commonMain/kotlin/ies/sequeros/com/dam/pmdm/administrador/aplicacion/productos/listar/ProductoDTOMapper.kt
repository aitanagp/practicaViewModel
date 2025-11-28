package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

fun Producto.toDTO(pathBase: String = "") = ProductoDTO(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imagePath = if (imgPath.isNotEmpty()) pathBase + imgPath else "",
    categoriaId = categoriaId,
    activo = activo
)

fun ProductoDTO.toProducto() = Producto(
    id = id,
    nombre = nombre,
    descripcion = descripcion,
    precio = precio,
    imgPath = imagePath,
    categoriaId = categoriaId,
    activo = activo
)