package ies.sequeros.com.dam.pmdm.dependiente.aplicacion.login

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.toDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos

class LoginDependienteUseCase(
    private val repositorio: IDependienteRepositorio,
    private val almacenDatos: AlmacenDatos
) {
    suspend fun invoke(usuario: String, clave: String): DependienteDTO {
        // 1. Buscar usuario por nombre (login)
        val dependiente = repositorio.findByName(usuario)
            ?: throw Exception("Usuario no encontrado")

        // 2. Comprobar contraseña
        if (dependiente.password != clave) {
            throw Exception("Contraseña incorrecta")
        }

        // 3. Comprobar si está activo
        if (!dependiente.enabled) {
            throw Exception("Usuario desactivado. Contacte con el administrador.")
        }

        // 4. Devolver DTO si todo es correcto
        return dependiente.toDTO(almacenDatos.getAppDataDir() + "/dependientes/")
    }
}