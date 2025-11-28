package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

data class CrearPedidoCommand(
    val cliente: String,
    val fecha: String, // O LocalDateTime si usas kotlinx-datetime
    val estado: String = "PENDIENTE",
    val total: Double,
    val lineas: List<CrearLineaPedidoCommand>
)

data class CrearLineaPedidoCommand(
    val productoId: String,
    val cantidad: Int,
    val precioUnitario: Double
)