package ies.sequeros.com.dam.pmdm.administrador.modelo

interface IProductoRepositorio {
    suspend fun getAll(): List<Producto>
    suspend fun getById(id: String): Producto?
    suspend fun getByCategoria(categoriaId: String): List<Producto>
    suspend fun add(producto: Producto)
    suspend fun update(producto: Producto): Boolean
    suspend fun remove(producto: Producto): Boolean
    suspend fun remove(id: String): Boolean
}