package ies.sequeros.com.dam.pmdm

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ies.sequeros.com.dam.pmdm.administrador.AdministradorViewModel
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio

import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministrador
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel

/*
@Suppress("ViewModelConstructorInComposable")
@Composable

fun App(
    dependienteRepositorio: IDependienteRepositorio,
    categoriaRepositorio: ICategoriaRepositorio?,
    almacenImagenes: AlmacenDatos
) {

    //view model
    val appViewModel= viewModel {  AppViewModel() }
    val mainViewModel= remember { MainAdministradorViewModel() }
    val administradorViewModel= viewModel { AdministradorViewModel() }

    val dependientesViewModel = viewModel{ DependientesViewModel(
        dependienteRepositorio, almacenImagenes
    )}
    // Crear VIEWMODEL CATEGORIAS
    val categoriasViewModel = viewModel{
        CategoriasViewModel(categoriaRepositorio, almacenImagenes)
    }


    appViewModel.setWindowsAdatativeInfo( currentWindowAdaptiveInfo())
    val navController= rememberNavController()

    // corregir darktema
    val isDarkMode by appViewModel.darkMode.collectAsState()
    // para cambiar el modo
    AppTheme(darkTheme = isDarkMode) {

        NavHost(
            navController,
            startDestination = AppRoutes.Main
        ) {
            composable(AppRoutes.Main) {
                Principal({
                    navController.navigate(AppRoutes.Administrador)
                },{},{})
            }
            composable(AppRoutes.Administrador) {
                // PASAR EL NUEVO PARÁMETRO A MAIN ADMINISTRADOR
                MainAdministrador(
                    appViewModel = appViewModel,
                    mainViewModel = mainViewModel,
                    administradorViewModel = administradorViewModel,
                    dependientesViewModel = dependientesViewModel,
                    categoriasViewModel = categoriasViewModel,
                    onExit = {
                        navController.popBackStack()
                    }
                )
            }

        }
    }

}*/

@Suppress("ViewModelConstructorInComposable")
@Composable
fun App(
    dependienteRepositorio: IDependienteRepositorio,
    categoriaRepositorio: ICategoriaRepositorio,
    productoRepositorio: IProductoRepositorio,
    pedidoRepositorio: IPedidoRepositorio,
    almacenImagenes: AlmacenDatos
) {

    val appViewModel = viewModel { AppViewModel() }
    val mainViewModel = viewModel { MainAdministradorViewModel() }
    val administradorViewModel = viewModel { AdministradorViewModel() }

    val dependientesViewModel = viewModel {
        DependientesViewModel(dependienteRepositorio, almacenImagenes)
    }

    val categoriasViewModel = viewModel {
        CategoriasViewModel(categoriaRepositorio, almacenImagenes)
    }

    val productosViewModel = viewModel {
        ProductosViewModel(productoRepositorio, categoriaRepositorio, almacenImagenes)
    }

    // CORRECCIÓN 1: Pasar también 'almacenImagenes'
    val pedidosViewModel = viewModel {
        PedidosViewModel(pedidoRepositorio, almacenImagenes)
    }

    appViewModel.setWindowsAdatativeInfo(currentWindowAdaptiveInfo())
    val navController = rememberNavController()

    val isDarkMode by appViewModel.darkMode.collectAsState()

    AppTheme(darkTheme = isDarkMode) {

        NavHost(
            navController = navController,
            startDestination = AppRoutes.Main
        ) {
            composable(AppRoutes.Main) {
                Principal(
                    onAdministrador = { navController.navigate(AppRoutes.Administrador) },
                    onDependiente = {},
                    onTPV = {}
                )
            }

            composable(AppRoutes.Administrador) {
                MainAdministrador(
                    appViewModel = appViewModel,
                    mainViewModel = mainViewModel,
                    administradorViewModel = administradorViewModel,
                    dependientesViewModel = dependientesViewModel,
                    categoriasViewModel = categoriasViewModel,
                    productosViewModel = productosViewModel,
                    pedidosViewModel = pedidosViewModel, // CORRECCIÓN 2: Pasarlo (ahora dará error en MainAdmin hasta que lo arregles abajo)
                    onExit = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}