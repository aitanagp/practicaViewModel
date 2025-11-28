package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarCategoriasUseCase(
    private val repositorio: ICategoriaRepositorio?,
    private val almacenDatos: AlmacenDatos
) {

    suspend operator fun invoke(): List<CategoriaDTO> {
        // Si el repositorio es nulo, devolvemos lista vacía
        if (repositorio == null) return emptyList()

        // Obtenemos las categorías del repositorio
        val categorias = repositorio.getAll()

        // Las mapeamos a DTOs
        return categorias.map { categoria ->
            val rutaBase = if (categoria.imgpath.isEmpty()) "" else almacenDatos.getAppDataDir() + "/categorias/"
            categoria.toDTO(rutaBase)
        }
    }
}