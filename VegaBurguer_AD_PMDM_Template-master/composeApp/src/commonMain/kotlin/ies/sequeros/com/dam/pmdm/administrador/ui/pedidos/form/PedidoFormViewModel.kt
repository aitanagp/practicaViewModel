package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PedidoFormViewModel(
    private val item: PedidoDTO?,
    private val onSuccess: (PedidoFormState) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PedidoFormState(
            cliente = item?.cliente ?: "",
            estado = item?.estado ?: "PENDIENTE",
            total = item?.total?.toString() ?: "0.0",
            fecha = item?.fecha ?: ""
        )
    )
    val uiState = _uiState.asStateFlow()

    // Validación: Solo exigimos que haya un nombre de cliente
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        state.cliente.isNotBlank() &&
                state.clienteError == null
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onClienteChange(v: String) {
        _uiState.update { it.copy(cliente = v, clienteError = if (v.isBlank()) "El cliente es obligatorio" else null) }
    }

    fun onEstadoChange(v: String) {
        _uiState.update { it.copy(estado = v) }
    }

    fun submit() {
        if (isFormValid.value) {
            onSuccess(_uiState.value)
        }
    }
}