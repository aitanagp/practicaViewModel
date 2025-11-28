package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar

data class ActualizarCategoriaCommand(
    val id: String,
    val nombre: String,
    val imagePath: String,
    val activa: Boolean
)
