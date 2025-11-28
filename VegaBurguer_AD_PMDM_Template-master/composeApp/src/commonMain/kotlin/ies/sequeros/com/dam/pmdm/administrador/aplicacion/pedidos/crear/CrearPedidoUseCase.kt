package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import ies.sequeros.com.dam.pmdm.generateUUID // Importa tu generador de UUID

class CrearPedidoUseCase(
    private val repositorio: IPedidoRepositorio
) {
    suspend fun invoke(command: CrearPedidoCommand): PedidoDTO {
        val idPedido = generateUUID()

        val lineasPedido = command.lineas.map { lineaCmd ->
            LineaPedido(
                id = generateUUID(),
                productoId = lineaCmd.productoId,
                cantidad = lineaCmd.cantidad,
                precioUnitario = lineaCmd.precioUnitario
            )
        }

        val nuevoPedido = Pedido(
            id = idPedido,
            cliente = command.cliente,
            fecha = command.fecha,
            estado = command.estado,
            total = command.total,
            lineas = lineasPedido
        )

        repositorio.add(nuevoPedido)
        return nuevoPedido.toDTO()
    }
}