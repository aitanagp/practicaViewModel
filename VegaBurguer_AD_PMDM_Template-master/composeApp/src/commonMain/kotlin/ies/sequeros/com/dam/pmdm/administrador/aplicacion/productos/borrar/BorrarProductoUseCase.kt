package ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.borrar

import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class BorrarProductoUseCase(
    private val repositorio: IProductoRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(id: String) {
        val producto = repositorio.getById(id) ?: return

        // Borrar de BBDD
        repositorio.remove(id)

        // Borrar imagen física (asumiendo ruta /productos/ + nombre)
        // Nota: Ajusta esto según cómo tu almacenDatos espere la ruta para borrar
        // Si necesita ruta completa:
        val rutaCompleta = almacenDatos.getAppDataDir() + "/productos/" + producto.imgPath
        almacenDatos.remove(rutaCompleta)
    }
}