package ies.sequeros.com.dam.pmdm.administrador.infraestructura

import ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias.BBDDRepositorioCategoriasJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class BBDDCategoriaRepository(
    private val bbddRepositorioCategoriasJava: BBDDRepositorioCategoriasJava
) : ICategoriaRepositorio {

    override suspend fun add(item: Categoria) {
        if (item.id.isEmpty()) {
            item.id = UUID.randomUUID().toString()
        }
        bbddRepositorioCategoriasJava.add(item)
    }

    override suspend fun remove(item: Categoria): Boolean {
        bbddRepositorioCategoriasJava.remove(item.id)
        return true
    }

    override suspend fun remove(id: String): Boolean {
        bbddRepositorioCategoriasJava.remove(id)
        return true
    }

    override suspend fun update(item: Categoria): Boolean {
        bbddRepositorioCategoriasJava.update(item)
        return true
    }

    override suspend fun getAll(): List<Categoria> {
        return bbddRepositorioCategoriasJava.getAll()
    }

    override suspend fun findByName(name: String): Categoria? = withContext(Dispatchers.IO) {
        // Filtramos en Kotlin porque Java no tiene el método findByName
        return@withContext bbddRepositorioCategoriasJava.getAll().find { it.nombre == name }
    }

    override suspend fun getById(id: String): Categoria? { // <--- TIPO CATEGORIA?
        return bbddRepositorioCategoriasJava.getById(id)
    }
}