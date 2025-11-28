package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

data class CrearProductoCommand(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagePath: String,
    val categoriaId: String,
    val activo: Boolean = true
)
