package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.BorrarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.activar.ActivarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.actualizar.ActualizarCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaCommand
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.crear.CrearCategoriaUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriasViewModel(
    private val repositorio: ICategoriaRepositorio?,
    private val almacenDatos: AlmacenDatos
) : ViewModel() {

    // 1. Casos de Uso
    // Se inicializan dentro (igual que en DependientesViewModel)
    private val listarCategoriasUseCase = ListarCategoriasUseCase(repositorio, almacenDatos)
    private val crearCategoriaUseCase = CrearCategoriaUseCase(repositorio, almacenDatos)
    private val actualizarCategoriaUseCase = ActualizarCategoriaUseCase(repositorio, almacenDatos)
    private val borrarCategoriaUseCase = BorrarCategoriaUseCase(repositorio, almacenDatos)
    private val activarCategoriaUseCase = ActivarCategoriaUseCase(repositorio, almacenDatos)

    // 2. Estados de la UI
    private val _categorias = MutableStateFlow<List<CategoriaDTO>>(emptyList())
    val categorias: StateFlow<List<CategoriaDTO>> = _categorias.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para selección (útil para edición)
    private val _selected = MutableStateFlow<CategoriaDTO?>(null)
    val selected: StateFlow<CategoriaDTO?> = _selected.asStateFlow()

    init {
        cargarCategorias()
    }

    // --- MÉTODOS DE CARGA ---

    fun cargarCategorias() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val items = listarCategoriasUseCase.invoke()
                // Actualizamos la lista completa
                _categorias.value = items
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- MÉTODOS DE SELECCIÓN ---

    fun selectCategoria(categoria: CategoriaDTO?) {
        _selected.value = categoria
    }

    // --- MÉTODOS DE ACCIÓN (CRUD) ---

    fun crear(id: String, nombre: String, imgPath: String, activa: Boolean = true) {
        val command = CrearCategoriaCommand(id, nombre, imgPath, activa)
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val nuevaCategoria = crearCategoriaUseCase.invoke(command)
                // Añadimos a la lista localmente para reactividad inmediata
                _categorias.update { it + nuevaCategoria }
            } catch (e: Exception) {
                e.printStackTrace()
                // Aquí podrías manejar errores (mostrar Snackbar, etc.)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizar(id: String, nombre: String, imgPath: String, activa: Boolean) {
        val command = ActualizarCategoriaCommand(id, nombre, imgPath, activa)
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val categoriaActualizada = actualizarCategoriaUseCase.invoke(command)

                // Actualizamos el elemento específico en la lista local
                _categorias.update { lista ->
                    lista.map { if (it.id == id) categoriaActualizada else it }
                }

                // Si la categoría editada estaba seleccionada, limpiamos la selección
                if (_selected.value?.id == id) {
                    _selected.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun borrar(id: String) {
        viewModelScope.launch {
            try {
                borrarCategoriaUseCase.invoke(id)

                // Eliminamos de la lista local
                _categorias.update { lista ->
                    lista.filter { it.id != id }
                }

                if (_selected.value?.id == id) {
                    _selected.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cambiarEstado(categoria: CategoriaDTO) {
        // Invierte el estado actual (activar/desactivar)
        val command = ActivarCategoriaCommand(categoria.id, !categoria.activa)
        viewModelScope.launch {
            try {
                val categoriaActualizada = activarCategoriaUseCase.invoke(command)

                // Actualizamos en la lista local
                _categorias.update { lista ->
                    lista.map { if (it.id == categoria.id) categoriaActualizada else it }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}