package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.crear.CrearPedidoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.ListarPedidosUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PedidosViewModel(
    private val repositorio: IPedidoRepositorio,
    private val almacenDatos: AlmacenDatos
) : ViewModel() {

    // Casos de uso
    private val listarPedidosUseCase = ListarPedidosUseCase(repositorio)

    // Estado de la lista de pedidos
    private val _pedidos = MutableStateFlow<List<PedidoDTO>>(emptyList())
    val pedidos = _pedidos.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Pedido seleccionado
    private val _selected = MutableStateFlow<PedidoDTO?>(null)
    val selected = _selected.asStateFlow()

    // 1. Inicializar caso de uso de crear
    private val crearPedidoUseCase = CrearPedidoUseCase(repositorio)

    init {
        cargarPedidos()
    }

    fun cargarPedidos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val lista = listarPedidosUseCase.invoke()
                _pedidos.value = lista
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error al cargar pedidos: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    // 2. Función para guardar nuevo pedido
    fun crear(cliente: String, estado: String, total: Double, fecha: String) {
        val command = CrearPedidoCommand(
            cliente = cliente,
            fecha = fecha.ifEmpty { "2024-01-01" }, // Fecha por defecto si viene vacía
            estado = estado,
            total = total,
            lineas = emptyList() // Ojo: Se creará sin líneas si no las gestionas en el form
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                crearPedidoUseCase.invoke(command)
                cargarPedidos() // Recargar lista
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun selectPedido(pedido: PedidoDTO?) {
        _selected.value = pedido
    }
}