package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarCategoriaUseCase(private val repositorio: ICategoriaRepositorio?,
                                 private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: ActualizarCategoriaCommand, ): CategoriaDTO {
        val item: Categoria?= repositorio?.getById(command.id)

        if (item==null) {
            throw IllegalArgumentException("La Categoría no esta registrado.")
        }
        //se pasa a dto para tener el path
        var itemDTO: CategoriaDTO =item.toDTO(almacenDatos.getAppDataDir()+"/categorias/")
        val newCategoria = item.copy(
            activa = command.activa
        )
        repositorio.update(newCategoria)
        //se devuelve con el path correcto
        return newCategoria.toDTO(almacenDatos.getAppDataDir()+"/categorias/")
    }
}