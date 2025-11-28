package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos.BBDDRepositorioProductosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto

class BBDDProductoRepository(
    private val bbddRepositorioProductosJava: BBDDRepositorioProductosJava
) : IProductoRepositorio {

    override suspend fun add(producto: Producto) {
        bbddRepositorioProductosJava.add(producto)
    }

    override suspend fun remove(producto: Producto): Boolean {
        bbddRepositorioProductosJava.remove(producto.id)
        return true
    }

    override suspend fun remove(id: String): Boolean {
        bbddRepositorioProductosJava.remove(id)
        return true
    }

    override suspend fun update(producto: Producto): Boolean {
        bbddRepositorioProductosJava.update(producto)
        return true
    }
    override suspend fun getAll(): List<Producto> {
        return bbddRepositorioProductosJava.all
    }

    override suspend fun getById(id: String): Producto? {
        return bbddRepositorioProductosJava.getById(id)
    }

    override suspend fun getByCategoria(categoriaId: String): List<Producto> {
        return bbddRepositorioProductosJava.getByCategoria(categoriaId)
    }



}