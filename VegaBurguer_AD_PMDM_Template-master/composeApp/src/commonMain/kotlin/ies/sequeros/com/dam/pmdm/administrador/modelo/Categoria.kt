package ies.sequeros.com.dam.pmdm.administrador.modelo
import kotlinx.serialization.Serializable
@Serializable
data class Categoria(
    var id: String,
    val nombre: String,
    val imgpath: String,
    val activa: Boolean
)
