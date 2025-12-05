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
import ies.sequeros.com.dam.pmdm.cliente.ui.ClientRoutes
import ies.sequeros.com.dam.pmdm.cliente.ui.ClienteViewModel
import ies.sequeros.com.dam.pmdm.cliente.ui.screens.BienvenidaCliente
import ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja.BandejaPedidosScreen
import ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja.BandejaPedidosViewModel
import ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja.DetallePedidoScreen
import ies.sequeros.com.dam.pmdm.dependiente.ui.login.LoginScreen
import ies.sequeros.com.dam.pmdm.dependiente.ui.login.LoginViewModel
import ies.sequeros.com.dam.pmdm.cliente.ui.screens.CarritoCliente
import ies.sequeros.com.dam.pmdm.cliente.ui.screens.CatalogoCliente
import ies.sequeros.com.dam.pmdm.cliente.ui.screens.PagoCliente

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

    val pedidosViewModel = viewModel {
        PedidosViewModel(pedidoRepositorio, almacenImagenes)
    }

    // ViewModel para el Login del Dependiente
    val loginViewModel = viewModel {
        LoginViewModel(dependienteRepositorio, almacenImagenes)
    }

    // ViewModel para la Bandeja de Pedidos del Dependiente ---
    val bandejaPedidosViewModel = viewModel {
        BandejaPedidosViewModel(pedidoRepositorio)
    }

    // viewmodel para clientes
    val clienteViewModel = viewModel {
        ClienteViewModel(
            categoriaRepo = categoriaRepositorio,
            productoRepo = productoRepositorio,
            pedidoRepo = pedidoRepositorio,
            almacenDatos = almacenImagenes
        )
    }

    appViewModel.setWindowsAdatativeInfo(currentWindowAdaptiveInfo())
    val navController = rememberNavController()

    val isDarkMode by appViewModel.darkMode.collectAsState()

    AppTheme(darkTheme = isDarkMode) {

        NavHost(
            navController = navController,
            startDestination = AppRoutes.Main
        ) {
            // Pantalla Principal (Selección de rol)
            composable(AppRoutes.Main) {
                Principal(
                    onAdministrador = { navController.navigate(AppRoutes.Administrador) },
                    onDependiente = { navController.navigate(AppRoutes.LoginDependiente) },
                    onTPV = {navController.navigate(ClientRoutes.Bienvenida)}
                )
            }

            // Pantalla Administrador
            composable(AppRoutes.Administrador) {
                MainAdministrador(
                    appViewModel = appViewModel,
                    mainViewModel = mainViewModel,
                    administradorViewModel = administradorViewModel,
                    dependientesViewModel = dependientesViewModel,
                    categoriasViewModel = categoriasViewModel,
                    productosViewModel = productosViewModel,
                    pedidosViewModel = pedidosViewModel,
                    onExit = { navController.popBackStack() }
                )
            }

            // Login Dependiente
            composable(AppRoutes.LoginDependiente) {
                LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { dependiente ->
                        // Navegar a la bandeja de pedidos al loguearse
                        navController.navigate(AppRoutes.MainDependiente) {
                            // Limpia la pila para que no puedan volver al login
                            popUpTo(AppRoutes.Main) { inclusive = false }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // Pantalla Principal del Dependiente (Lista de pedidos) ---
            composable(AppRoutes.MainDependiente) {
                BandejaPedidosScreen(
                    viewModel = bandejaPedidosViewModel,
                    onPedidoClick = { idPedido ->
                        // 1. Seleccionamos el pedido en el VM
                        bandejaPedidosViewModel.seleccionarPedido(idPedido)
                        // 2. Navegamos al detalle (asegúrate de tener esta ruta o string definido)
                        navController.navigate("detalle_pedido")
                    },
                    onBack = {
                        // Al salir, volvemos a la pantalla principal de la App y limpiamos sesión visualmente
                        navController.popBackStack(AppRoutes.Main, inclusive = false)
                    }
                )
            }

            // Pantalla Detalle de Pedido
            composable("detalle_pedido") {
                DetallePedidoScreen(
                    viewModel = bandejaPedidosViewModel,
                    onBack = {
                        // Volvemos a la lista
                        navController.popBackStack()
                    }
                )
            }
            // Pantalla Inicio CLiente
            composable(ClientRoutes.Bienvenida) {
                BienvenidaCliente(
                    viewModel = clienteViewModel,
                    onComenzar = {
                        navController.navigate(ClientRoutes.Catalogo)
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
            // 2. Catálogo (Principal)
            composable(ClientRoutes.Catalogo) {
                CatalogoCliente(
                    viewModel = clienteViewModel,
                    onCancelar = {
                        // Vuelve al inicio borrando historial
                        navController.navigate(AppRoutes.Main) { popUpTo(AppRoutes.Main) { inclusive = true } }
                    },
                    onConfirmar = {
                        // Va al carrito/resumen
                        navController.navigate(ClientRoutes.Carrito)
                    }
                )
            }

            // 3. Carrito (Detalle)
            composable(ClientRoutes.Carrito) {
                CarritoCliente(
                    viewModel = clienteViewModel,
                    onVolver = { navController.popBackStack() },
                    onConfirmar = {
                        navController.navigate(ClientRoutes.Pago)
                    },
                    onCancelarPedido = {
                        navController.navigate(AppRoutes.Main) { popUpTo(AppRoutes.Main) { inclusive = true } }
                    }
                )
            }

            // 4. Pago
            composable(ClientRoutes.Pago) {
                PagoCliente(
                    viewModel = clienteViewModel,
                    onFinalizar = {
                        // Al terminar, volvemos a la pantalla principal de la app (o a Bienvenida)
                        navController.navigate(AppRoutes.Main) {
                            popUpTo(AppRoutes.Main) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}