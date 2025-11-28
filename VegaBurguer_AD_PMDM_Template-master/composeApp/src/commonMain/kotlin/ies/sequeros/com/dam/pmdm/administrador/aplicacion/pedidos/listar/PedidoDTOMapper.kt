package ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.LineaPedido
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido

fun Pedido.toDTO() = PedidoDTO(
    id = id,
    cliente = cliente,
    fecha = fecha,
    estado = estado,
    total = total,
    lineas = lineas.map { it.toDTO() }
)

fun LineaPedido.toDTO() = LineaPedidoDTO(
    id = id,
    productoId = productoId,
    cantidad = cantidad,
    precioUnitario = precioUnitario
)

fun PedidoDTO.toPedido() = Pedido(
    id = id,
    cliente = cliente,
    fecha = fecha,
    estado = estado,
    total = total,
    lineas = lineas.map { it.toLineaPedido() }
)

fun LineaPedidoDTO.toLineaPedido() = LineaPedido(
    id = id,
    productoId = productoId,
    cantidad = cantidad,
    precioUnitario = precioUnitario
)