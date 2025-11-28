package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar

data class ActualizarProductoCommand(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagePath: String,
    val categoriaId: String,
    val activo: Boolean
)
