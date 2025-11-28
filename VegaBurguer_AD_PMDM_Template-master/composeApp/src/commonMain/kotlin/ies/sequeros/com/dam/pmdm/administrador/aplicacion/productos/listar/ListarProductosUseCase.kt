package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class ListarProductosUseCase(
    private val repositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(): List<ProductoDTO> {
        val productos = repositorio.getAll()
        return productos.map {
            it.toDTO(almacenDatos.getAppDataDir() + "/productos/")
        }
    }
}