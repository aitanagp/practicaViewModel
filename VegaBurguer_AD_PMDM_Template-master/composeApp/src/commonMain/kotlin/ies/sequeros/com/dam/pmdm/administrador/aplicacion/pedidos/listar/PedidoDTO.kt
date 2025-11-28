package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import kotlinx.serialization.Serializable

@Serializable
data class PedidoDTO(
    val id: String,
    val cliente: String,
    val fecha: String,
    val estado: String,
    val total: Double,
    val lineas: List<LineaPedidoDTO> = emptyList()
)

@Serializable
data class LineaPedidoDTO(
    val id: String,
    val productoId: String,
    val cantidad: Int,
    val precioUnitario: Double
)
