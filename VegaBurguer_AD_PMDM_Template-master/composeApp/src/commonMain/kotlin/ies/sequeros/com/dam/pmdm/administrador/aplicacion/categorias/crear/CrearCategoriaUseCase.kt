package ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.Categoria
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.generateUUID

class CrearCategoriaUseCase(
    private val repositorio: ICategoriaRepositorio?,
    private val almacenDatos: AlmacenDatos
) {

    suspend fun invoke(createCategoriaCommand: CrearCategoriaCommand): CategoriaDTO {
        // 1. Validar si ya existe
        if (repositorio?.findByName(createCategoriaCommand.nombre) != null) {
            throw IllegalArgumentException("El nombre de la categoría ya está registrado.")
        }

        // 2. Generar ID
        val id = generateUUID()

        // 3. Almacenar la imagen físicamente (copiar a la carpeta /categorias/)
        // Se asume que createCategoriaCommand.imagePath trae la ruta temporal o seleccionada
        val imageName = almacenDatos.copy(createCategoriaCommand.imagePath, id, "/categorias/")

        // 4. Crear el objeto de Dominio
        val item = Categoria(
            id = id,
            nombre = createCategoriaCommand.nombre,
            imgpath = imageName, // Guardamos solo el nombre del fichero devuelto por copy
            activa = createCategoriaCommand.activa
        )

        // 5. Guardar en el repositorio (BBDD)
        repositorio?.add(item)

        // 6. Devolver el DTO mapeado con la ruta absoluta para que la UI lo pinte
        return item.toDTO(almacenDatos.getAppDataDir() + "/categorias/")
    }
}