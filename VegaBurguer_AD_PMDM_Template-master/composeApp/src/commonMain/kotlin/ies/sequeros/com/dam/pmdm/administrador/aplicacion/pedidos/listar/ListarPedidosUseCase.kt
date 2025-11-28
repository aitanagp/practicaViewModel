package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio

class ListarPedidosUseCase(
    private val repositorio: IPedidoRepositorio
) {
    suspend fun invoke(): List<PedidoDTO> {
        val pedidos = repositorio.getAll()
        return pedidos.map { it.toDTO() }
    }
}