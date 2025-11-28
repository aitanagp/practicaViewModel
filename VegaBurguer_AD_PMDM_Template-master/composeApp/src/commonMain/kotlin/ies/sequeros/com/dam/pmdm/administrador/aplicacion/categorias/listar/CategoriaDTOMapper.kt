package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria

// Convierte de Modelo -> DTO (para mostrar en UI)
fun Categoria.toDTO(path: String = "") = CategoriaDTO(
    id = id,
    nombre = nombre,
    imagePath = path + imgpath, // Concatenamos la ruta base si se pasa
    activa = activa
)

// Convierte de DTO -> Modelo (para guardar en BBDD)
fun CategoriaDTO.toCategoria() = Categoria(
    id = id,
    nombre = nombre,
    imgpath = imagePath,
    activa = activa
)