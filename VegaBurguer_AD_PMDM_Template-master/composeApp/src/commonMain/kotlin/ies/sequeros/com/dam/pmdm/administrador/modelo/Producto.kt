package ies.sequeros.com.dam.pmdm.administrador.modelo
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imgPath: String,
    val categoriaId: String, // ID de la categoría a la que pertenece
    val activo: Boolean
)