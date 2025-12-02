package ies.sequeros.com.dam.pmdm.dependiente.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio

class ListarPedidosPendientesUseCase(
    private val repositorio: IPedidoRepositorio
) {
    suspend fun invoke(): List<PedidoDTO> {
        val todos = repositorio.getAll()
        // Filtramos solo los que están PENDIENTES
        return todos.filter { it.estado == "PENDIENTE" }.map { it.toDTO() }
    }
}