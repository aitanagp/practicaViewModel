package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio?,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(id: String) {
        val categoria = repositorio?.getById(id)

        if (categoria == null) {
            throw IllegalArgumentException("El id no está registrado.")
        }

        // Obtenemos el DTO para saber la ruta de la imagen y borrarla
        // Asegúrate de usar la misma carpeta que en Crear (/categorias/)
        val categoriaDTO = categoria.toDTO(almacenDatos.getAppDataDir() + "/categorias/")

        // 1. Borrar de la base de datos
        repositorio?.remove(id)

        // 2. Borrar la imagen física
        // (Asegúrate de que tu clase AlmacenDatos tenga el método remove implementado)
        almacenDatos.remove(categoriaDTO.imagePath)
    }
}