package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear

data class CrearCategoriaCommand(
    val id: String,
    val nombre: String,
    val imagePath: String,
    val activa: Boolean
)
