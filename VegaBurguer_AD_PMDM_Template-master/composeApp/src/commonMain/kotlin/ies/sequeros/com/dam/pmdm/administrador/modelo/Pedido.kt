package ies.sequeros.com.dam.pmdm.administrador.modelo

import kotlinx.serialization.Serializable

@Serializable
data class Pedido(
    var id: String,
    val cliente: String,
    val fecha: String, // Usamos String para simplificar la fecha en BBDD
    val estado: String,
    val total: Double,
    val lineas: List<LineaPedido> = emptyList() // Lista de productos del pedido
)