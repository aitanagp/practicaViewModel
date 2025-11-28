package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaDTO(
    val id: String,
    val nombre: String,
    val imagePath: String,
    val activa: Boolean
)
