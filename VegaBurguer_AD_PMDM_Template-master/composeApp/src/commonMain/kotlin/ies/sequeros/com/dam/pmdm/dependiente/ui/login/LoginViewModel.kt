package ies.sequeros.com.dam.pmdm.dependiente.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.dependiente.aplicacion.login.LoginUseCase
import ies.sequeros.com.dam.pmdm.administrador.modelo.IDependienteRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    repositorio: IDependienteRepositorio,
    almacenDatos: AlmacenDatos
) : ViewModel() {

    private val loginUseCase = LoginUseCase(repositorio, almacenDatos)

    // Estado del formulario
    private val _usuario = MutableStateFlow("")
    val usuario = _usuario.asStateFlow()

    private val _clave = MutableStateFlow("")
    val clave = _clave.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    // Usuario logueado con éxito
    private val _usuarioLogueado = MutableStateFlow<DependienteDTO?>(null)
    val usuarioLogueado = _usuarioLogueado.asStateFlow()

    fun onUsuarioChange(v: String) { _usuario.value = v }
    fun onClaveChange(v: String) { _clave.value = v }

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val dependiente = loginUseCase.invoke(_usuario.value, _clave.value)
                _usuarioLogueado.value = dependiente
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}