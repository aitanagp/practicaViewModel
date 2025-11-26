package ies.sequeros.com.dam.pmdm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.BBDDRepositorioDependientesJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import java.io.FileInputStream
import java.util.logging.LogManager

// ELIMINADO: object AppRoutes { ... } <- Esto ya está en commonMain

fun main() = application {

    // 1. Inicializamos el repositorio de Java (Infraestructura Desktop)
    // Usamos try-catch para evitar que la app explote si falla la BBDD
    var repoJava: BBDDRepositorioDependientesJava? = null
    try {
        repoJava = BBDDRepositorioDependientesJava("app.properties")
    } catch (e: Exception) {
        e.printStackTrace()
        println("⚠️ Error conectando a la base de datos: ${e.message}")
    }

    // 2. Creamos el wrapper de Kotlin si la conexión fue exitosa
    val dependienteRepositorio: IDependienteRepositorio? = if (repoJava != null) {
        BBDDDependienteRepository(repoJava)
    } else {
        null
    }

    // Configuración de logs
    configureExternalLogging("./logging.properties")

    Window(
        onCloseRequest = {
            repoJava?.close()
            exitApplication()
        },
        title = "VegaBurguer",
    ) {
        // 3. Lanzamos la App común
        // Solo si tenemos repositorio válido. Si no, podríamos mostrar un error.
        if (dependienteRepositorio != null) {
            App(dependienteRepositorio, AlmacenDatos())
        } else {
            println("No se puede iniciar la App porque falló la conexión a BBDD")
        }
    }
}

fun configureExternalLogging(path: String) {
    try {
        // Verificamos si existe antes de intentar leerlo para evitar errores en consola
        val file = java.io.File(path)
        if (file.exists()) {
            FileInputStream(file).use { fis ->
                LogManager.getLogManager().readConfiguration(fis)
                println("Logging configurado desde: $path")
            }
        }
    } catch (e: Exception) {
        println("No se pudo cargar logging.properties externo: $path")
    }
}