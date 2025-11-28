package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductoFormViewModel(
    private val item: ProductoDTO?,
    private val onSuccess: (ProductoFormState) -> Unit
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductoFormState(
            nombre = item?.nombre ?: "",
            descripcion = item?.descripcion ?: "",
            precio = item?.precio?.toString() ?: "",
            imagePath = item?.imagePath ?: "",
            categoriaId = item?.categoriaId ?: "",
            activo = item?.activo ?: true
        )
    )
    val uiState = _uiState.asStateFlow()

    // Validación: Nombre, Precio válido y Categoría seleccionada
    val isFormValid: StateFlow<Boolean> = uiState.map { state ->
        state.nombre.isNotBlank() &&
                state.precio.toDoubleOrNull() != null &&
                state.categoriaId.isNotBlank() && // Obligatorio elegir categoría
                state.imagePath.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun onNombreChange(v: String) {
        _uiState.update { it.copy(nombre = v, nombreError = if (v.isBlank()) "Obligatorio" else null) }
    }

    fun onDescripcionChange(v: String) {
        _uiState.update { it.copy(descripcion = v) }
    }

    fun onPrecioChange(v: String) {
        val error = if (v.toDoubleOrNull() == null) "Precio inválido" else null
        _uiState.update { it.copy(precio = v, precioError = error) }
    }

    fun onCategoriaChange(v: String) {
        _uiState.update { it.copy(categoriaId = v, categoriaError = null) }
    }

    fun onImageChange(v: String) {
        _uiState.update { it.copy(imagePath = v, imagePathError = null) }
    }

    fun onActivoChange(v: Boolean) {
        _uiState.update { it.copy(activo = v) }
    }

    fun clear() { _uiState.value = ProductoFormState() }

    fun submit() {
        if (isFormValid.value) onSuccess(_uiState.value)
    }
}