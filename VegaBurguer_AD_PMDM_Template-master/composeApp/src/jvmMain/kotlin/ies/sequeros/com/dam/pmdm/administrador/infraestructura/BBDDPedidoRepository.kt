package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos.BBDDRepositorioPedidosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Pedido
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BBDDPedidoRepository(
    private val repoJava: BBDDRepositorioPedidosJava
) : IPedidoRepositorio {

    override suspend fun getAll(): List<Pedido> = withContext(Dispatchers.IO) {
        return@withContext repoJava.getAll()
    }

    override suspend fun getById(id: String): Pedido? = withContext(Dispatchers.IO) {
        return@withContext repoJava.getAll().find { it.id == id }
    }

    override suspend fun add(pedido: Pedido) = withContext(Dispatchers.IO) {
        // 1. Generar UUID para el Pedido si no tiene
        var pedidoAGuardar = pedido
        if (pedido.id.isEmpty()) {
            pedidoAGuardar = pedido.copy(id = UUID.randomUUID().toString())
        }

        // 2. Generar UUIDs para las Líneas de pedido si no tienen
        val lineasConId = pedidoAGuardar.lineas.map { linea ->
            if (linea.id.isEmpty()) {
                linea.copy(id = UUID.randomUUID().toString())
            } else {
                linea
            }
        }
        // Actualizamos el pedido con las líneas que ya tienen ID
        pedidoAGuardar = pedidoAGuardar.copy(lineas = lineasConId)

        // 3. Llamamos al repositorio de Java para guardar
        repoJava.add(pedidoAGuardar)
    }
}