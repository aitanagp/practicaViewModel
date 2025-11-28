package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

data class ActivarProductoCommand(val id: String, val activo: Boolean)

class ActivarProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: ActivarProductoCommand): ProductoDTO {
        val producto = repositorio.getById(command.id)
            ?: throw IllegalArgumentException("Producto no encontrado")

        val actualizado = producto.copy(activo = command.activo)
        repositorio.update(actualizado)

        return actualizado.toDTO(almacenDatos.getAppDataDir() + "/productos/")
    }
}