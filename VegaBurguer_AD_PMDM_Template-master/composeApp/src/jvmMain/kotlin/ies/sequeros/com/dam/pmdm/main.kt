package ies.sequeros.com.dam.pmdm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDCategoriaRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDDependienteRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDPedidoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.BBDDProductoRepository
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.categorias.BBDDRepositorioCategoriasJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.dependientes.BBDDRepositorioDependientesJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.pedidos.BBDDRepositorioPedidosJava
import ies.sequeros.com.dam.pmdm.administrador.infraestructura.productos.BBDDRepositorioProductosJava
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import java.io.FileInputStream
import java.util.logging.LogManager

// ELIMINADO: object AppRoutes { ... } <- Esto ya está en commonMain
/*
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
    val categoriaRepositorio: ICategoriaRepositorio? = if (repoJava != null) {
        BBDDCategoriaRepository(repoJava)
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
            App(dependienteRepositorio, categoriaRepositorio, AlmacenDatos())
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
}*/
fun main() = application {

    // Variables para los repositorios finales
    var dependienteRepo: IDependienteRepositorio? = null
    var categoriaRepo: ICategoriaRepositorio? = null
    var productoRepo: IProductoRepositorio? = null
    var pedidoRepo: IPedidoRepositorio? = null

    // Variables para la infraestructura Java (para poder cerrar luego)
    var repoJavaDep: BBDDRepositorioDependientesJava? = null
    var repoJavaCat: BBDDRepositorioCategoriasJava? = null
    var repoJavaProd: BBDDRepositorioProductosJava? = null
    var repoJavaPed: BBDDRepositorioPedidosJava? = null

    try {
        // Inicializamos Dependientes
        repoJavaDep = BBDDRepositorioDependientesJava("app.properties")
        dependienteRepo = BBDDDependienteRepository(repoJavaDep)

        // Inicializamos Categorías
        repoJavaCat = BBDDRepositorioCategoriasJava("app.properties")
        categoriaRepo = BBDDCategoriaRepository(repoJavaCat)

        // Inicializamos Productos
        repoJavaProd = BBDDRepositorioProductosJava("app.properties")
        productoRepo = BBDDProductoRepository(repoJavaProd)

        // inicializamos pedidos
        repoJavaPed = BBDDRepositorioPedidosJava("app.properties")
        pedidoRepo = BBDDPedidoRepository(repoJavaPed)

    } catch (e: Exception) {
        e.printStackTrace()
        println("Error fatal iniciando la base de datos: ${e.message}")
    }

    configureExternalLogging("./logging.properties")

    Window(
        onCloseRequest = {
            repoJavaDep?.close()
            repoJavaCat?.close()
            repoJavaProd?.close()
            repoJavaPed?.close()
            exitApplication()
        },
        title = "VegaBurguer",
    ) {
        // Solo iniciamos si TODO ha cargado correctamente
        if (dependienteRepo != null && categoriaRepo != null && productoRepo != null && pedidoRepo != null) {
            App(
                dependienteRepositorio = dependienteRepo!!,
                categoriaRepositorio = categoriaRepo!!,
                productoRepositorio = productoRepo!!,
                pedidoRepositorio = pedidoRepo!!,
                almacenImagenes = AlmacenDatos()
            )
        } else {
            println("La aplicación no puede iniciar sin conexión a BBDD")
        }
    }
}

fun configureExternalLogging(path: String) {
    try {
        val file = java.io.File(path)
        if (file.exists()) {
            FileInputStream(file).use { fis ->
                LogManager.getLogManager().readConfiguration(fis)
                println("Logging configurado desde: $path")
            }
        }
    } catch (e: Exception) {
        println("Error configurando logs")
    }
}