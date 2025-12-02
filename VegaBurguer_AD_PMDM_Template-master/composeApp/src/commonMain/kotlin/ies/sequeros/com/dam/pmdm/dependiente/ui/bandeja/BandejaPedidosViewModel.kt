package ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.IPedidoRepositorio
import ies.sequeros.com.dam.pmdm.dependiente.aplicacion.pedidos.actualizar.MarcarPedidoEntregadoUseCase
import ies.sequeros.com.dam.pmdm.dependiente.aplicacion.pedidos.listar.ListarPedidosPendientesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BandejaPedidosViewModel(
    repositorio: IPedidoRepositorio
) : ViewModel() {

    private val listarPendientesUC = ListarPedidosPendientesUseCase(repositorio)
    private val marcarEntregadoUC = MarcarPedidoEntregadoUseCase(repositorio)

    // Lista de todos los pedidos
    private val _pedidos = MutableStateFlow<List<PedidoDTO>>(emptyList())
    val pedidos = _pedidos.asStateFlow()

    // --- AÑADIDO: El pedido seleccionado actualmente ---
    private val _selected = MutableStateFlow<PedidoDTO?>(null)
    val selected = _selected.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        cargarPedidos()
    }

    fun cargarPedidos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _pedidos.value = listarPendientesUC.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- AÑADIDO: Función para elegir un pedido por su ID ---
    // Llámala desde la pantalla anterior cuando hagas click en un pedido
    fun seleccionarPedido(id: String) {
        val pedidoEncontrado = _pedidos.value.find { it.id == id }
        _selected.value = pedidoEncontrado
    }

    // --- AÑADIDO: Limpiar selección al salir ---
    fun limpiarSeleccion() {
        _selected.value = null
    }

    fun entregarPedido(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                marcarEntregadoUC.invoke(id)

                // Limpiamos el seleccionado porque ya se entregó
                _selected.value = null

                cargarPedidos() // Recargar lista
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}