package ies.sequeros.com.dam.pmdm.cliente.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearLineaPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductosUseCase
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

// Clase auxiliar para el carrito
data class LineaCarrito(
    val producto: ProductoDTO,
    var cantidad: Int
)

class ClienteViewModel(
    categoriaRepo: ICategoriaRepositorio,
    productoRepo: IProductoRepositorio,
    pedidoRepo: IPedidoRepositorio,
    almacenDatos: AlmacenDatos
) : ViewModel() {

    // cassos de uso para obtener los datos
    private val listarCategoriasUC = ListarCategoriasUseCase(categoriaRepo, almacenDatos)
    private val listarProductosUC = ListarProductosUseCase(productoRepo, almacenDatos)
    private val crearPedidoUC = CrearPedidoUseCase(pedidoRepo)

    // Nombre del cliente
    private val _nombreCliente = MutableStateFlow("")
    val nombreCliente = _nombreCliente.asStateFlow()

    // ctagorias
    private val _categorias = MutableStateFlow<List<CategoriaDTO>>(emptyList())
    val categorias = _categorias.asStateFlow()

    private val _productos = MutableStateFlow<List<ProductoDTO>>(emptyList())
    val productos = _productos.asStateFlow() // Todos los productos

    // Categoría seleccionada
    private val _categoriaSeleccionada = MutableStateFlow<CategoriaDTO?>(null)
    val categoriaSeleccionada = _categoriaSeleccionada.asStateFlow()

    // CARRITO DE LA COMPRA
    private val _carrito = MutableStateFlow<List<LineaCarrito>>(emptyList())
    val carrito = _carrito.asStateFlow()

    // Totales calculados
    val totalPedido = _carrito.map { lineas ->
        lineas.sumOf { it.producto.precio * it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val numProductos = _carrito.map { lineas ->
        lineas.sumOf { it.cantidad }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0)

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            try {
                _categorias.value = listarCategoriasUC.invoke().filter { it.activa } // Solo activas
                _productos.value = listarProductosUC.invoke().filter { it.activo }   // Solo activos
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- ACCIONES ---

    fun setCliente(nombre: String) {
        _nombreCliente.value = nombre
    }

    fun seleccionarCategoria(cat: CategoriaDTO?) {
        _categoriaSeleccionada.value = cat
    }

    // Lógica del Carrito (+ y -)
    fun addProducto(producto: ProductoDTO) {
        // se copia la lista en una nueva lista mutable
        val listaActual = _carrito.value.toMutableList()

        val lineaExistente = listaActual.find { it.producto.id == producto.id }

        // si no se ha añadido ningún producto antes al carrito se debe de crear
        if (lineaExistente != null) {
            lineaExistente.cantidad++
        } else {
            listaActual.add(LineaCarrito(producto, 1))
        }

        // esto es para que se detecte el cambio
        _carrito.value = listaActual.toList() // Emitir cambio
    }

    fun removeProducto(producto: ProductoDTO) {
        val listaActual = _carrito.value.toMutableList()
        val lineaExistente = listaActual.find { it.producto.id == producto.id }

        if (lineaExistente != null) {
            if (lineaExistente.cantidad > 1) {
                lineaExistente.cantidad--
            } else {
                listaActual.remove(lineaExistente)
            }
            _carrito.value = listaActual.toList()
        }
    }

    fun eliminarLineaCompleta(producto: ProductoDTO) {
        val listaActual = _carrito.value.toMutableList()
        listaActual.removeAll { it.producto.id == producto.id }
        _carrito.value = listaActual
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
        _nombreCliente.value = ""
        _categoriaSeleccionada.value = null
    }

    // Confirmar Pedido en BBDD
    fun confirmarPedido(onSuccess: () -> Unit) {
        val lineasCommand = _carrito.value.map {
            CrearLineaPedidoCommand(it.producto.id, it.cantidad, it.producto.precio)
        }
        // 1. Obtener fecha actual y formatearla para MySQL (YYYY-MM-DD HH:MM:SS)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val fechaFormateada = sdf.format(Date())

        val command = CrearPedidoCommand(
            cliente = _nombreCliente.value,
            fecha = fechaFormateada,
            total = totalPedido.value,
            lineas = lineasCommand
        )
        viewModelScope.launch {
            try {
                crearPedidoUC.invoke(command)
                vaciarCarrito()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}