package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.Producto
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID // Importa tu función generadora

class CrearProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(command: CrearProductoCommand): ProductoDTO {
        val id = generateUUID()

        // Guardamos la imagen en la carpeta /productos/
        val imageName = almacenDatos.copy(command.imagePath, id, "/productos/")

        val nuevoProducto = Producto(
            id = id,
            nombre = command.nombre,
            descripcion = command.descripcion,
            precio = command.precio,
            imgPath = imageName,
            categoriaId = command.categoriaId,
            activo = command.activo
        )

        repositorio.add(nuevoProducto)

        return nuevoProducto.toDTO(almacenDatos.getAppDataDir() + "/productos/")
    }
}