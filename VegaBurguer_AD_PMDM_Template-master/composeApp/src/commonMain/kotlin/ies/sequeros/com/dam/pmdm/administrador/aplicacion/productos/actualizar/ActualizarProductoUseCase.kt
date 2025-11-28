package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ActualizarProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: ActualizarProductoCommand): ProductoDTO {
        val productoActual = repositorio.getById(command.id)
            ?: throw IllegalArgumentException("Producto no encontrado")

        var nombreImagen = productoActual.imgPath
        val rutaBase = almacenDatos.getAppDataDir() + "/productos/"
        val rutaActualCompleta = if (productoActual.imgPath.isNotEmpty()) rutaBase + productoActual.imgPath else ""

        // Si la ruta ha cambiado y no está vacía, guardamos la nueva imagen
        if (command.imagePath != rutaActualCompleta && command.imagePath.isNotEmpty()) {
            nombreImagen = almacenDatos.copy(command.imagePath, command.id, "/productos/")
            // Borrar la imagen antigua
        }

        val productoActualizado = productoActual.copy(
            nombre = command.nombre,
            descripcion = command.descripcion,
            precio = command.precio,
            imgPath = nombreImagen,
            categoriaId = command.categoriaId,
            activo = command.activo
        )

        repositorio.update(productoActualizado)

        return productoActualizado.toDTO(rutaBase)
    }
}