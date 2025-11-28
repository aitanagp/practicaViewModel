package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

data class PedidoFormState(
    val cliente: String = "",
    val estado: String = "PENDIENTE", // PENDIENTE, ENTREGADO, CANCELADO
    val total: String = "0.0", // String para visualización
    val fecha: String = "",

    // Errores
    val clienteError: String? = null,

    val isValid: Boolean = false
)