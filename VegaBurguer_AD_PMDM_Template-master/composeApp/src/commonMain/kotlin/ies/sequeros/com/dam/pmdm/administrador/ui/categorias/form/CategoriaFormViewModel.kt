package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO

class CategoriaFormViewModel(
    private val item: CategoriaDTO?,
    private val onSuccess: (CategoriaFormState) -> Unit
) : ViewModel() {

    // Inicializamos el estado con los datos de la categoría si estamos editando
    private val _uiState = MutableStateFlow(
        CategoriaFormState(
            nombre = item?.nombre ?: "",
            imagePath = item?.imagePath ?: "",
            activa = item?.activa ?: true
        )
    )
    val uiState: StateFlow<CategoriaFormState> = _uiState.asStateFlow()

    // Validación reactiva del formulario
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        // Reglas de validación:
        // 1. Sin errores específicos
        state.nombreError == null &&
                state.imagePathError == null &&
                // 2. Campos obligatorios rellenos
                state.nombre.isNotBlank() &&
                state.imagePath.isNotBlank()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    // --- Manejadores de eventos de cambio ---

    fun onNombreChange(v: String) {
        _uiState.value = _uiState.value.copy(nombre = v, nombreError = validateNombre(v))
    }

    fun onImagePathChange(v: String) {
        _uiState.value = _uiState.value.copy(imagePath = v, imagePathError = validateImagePath(v))
    }

    fun onActivaChange(v: Boolean) {
        _uiState.value = _uiState.value.copy(activa = v)
    }

    fun clear() {
        _uiState.value = CategoriaFormState()
    }

    // --- Validaciones privadas ---

    private fun validateNombre(nombre: String): String? {
        if (nombre.isBlank()) return "El nombre es obligatorio"
        if (nombre.length < 2) return "El nombre es muy corto"
        return null
    }

    private fun validateImagePath(path: String): String? {
        if (path.isBlank()) return "La imagen es obligatoria"
        return null
    }

    // --- Validación final y envío ---

    fun validateAll(): Boolean {
        val s = _uiState.value
        val nombreErr = validateNombre(s.nombre)
        val imageErr = validateImagePath(s.imagePath)

        val newState = s.copy(
            nombreError = nombreErr,
            imagePathError = imageErr
        )
        _uiState.value = newState
        return listOf(nombreErr, imageErr).all { it == null }
    }

    fun submit(
        onSuccessSubmit: (CategoriaFormState) -> Unit,
        onFailure: ((CategoriaFormState) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val ok = validateAll()
            if (ok) {
                onSuccessSubmit(_uiState.value)
                // También llamamos al callback del constructor si es necesario
                // onSuccess(_uiState.value)
            } else {
                onFailure?.invoke(_uiState.value)
            }
        }
    }
}