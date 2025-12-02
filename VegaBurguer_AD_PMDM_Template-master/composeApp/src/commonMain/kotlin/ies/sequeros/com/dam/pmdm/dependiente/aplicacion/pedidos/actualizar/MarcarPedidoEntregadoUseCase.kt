package ies.sequeros.com.dam.pmdm.dependiente.aplicacion.pedidos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio

class MarcarPedidoEntregadoUseCase(
    private val repositorio: IPedidoRepositorio
) {
    suspend fun invoke(id: String) {
        val pedido = repositorio.getById(id) ?: throw Exception("Pedido no encontrado")
        // Actualizamos estado
        val entregado = pedido.copy(estado = "ENTREGADO")
        // Guardamos
        repositorio.add(entregado)
    }
}