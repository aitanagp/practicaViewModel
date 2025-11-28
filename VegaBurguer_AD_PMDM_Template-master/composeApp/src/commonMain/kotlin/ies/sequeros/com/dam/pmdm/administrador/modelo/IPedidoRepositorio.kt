package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IPedidoRepositorio {
    suspend fun getAll(): List<Pedido>
    suspend fun getById(id: String): Pedido?
    suspend fun add(pedido: Pedido)
}