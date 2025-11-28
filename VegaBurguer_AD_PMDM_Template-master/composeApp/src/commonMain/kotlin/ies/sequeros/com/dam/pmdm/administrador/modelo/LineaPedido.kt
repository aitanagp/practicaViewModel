package ies.sequeros.com.dam.pmdm.administrador.modelo
import kotlinx.serialization.Serializable

@Serializable
data class LineaPedido(
    var id: String,
    val productoId: String,
    val cantidad: Int,
    val precioUnitario: Double
)