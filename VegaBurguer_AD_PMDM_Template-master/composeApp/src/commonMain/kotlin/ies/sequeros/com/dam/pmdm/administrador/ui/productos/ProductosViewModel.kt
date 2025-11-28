package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.ListarCategoriasUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.activar.*
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.actualizar.*
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.borrar.BorrarProductoUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.crear.*
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ListarProductosUseCase
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.administrador.modelo.ICategoriaRepositorio
import ies.sequeros.com.dam.pmdm.administrador.modelo.IProductoRepositorio
import ies.sequeros.com.dam.pmdm.commons.infraestructura.AlmacenDatos
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductosViewModel(
    productoRepo: IProductoRepositorio,
    categoriaRepo: ICategoriaRepositorio,
    almacenDatos: AlmacenDatos
) : ViewModel() {

    private val listarProductosUC = ListarProductosUseCase(productoRepo, almacenDatos)
    private val crearProductoUC = CrearProductoUseCase(productoRepo, almacenDatos)
    private val actualizarProductoUC = ActualizarProductoUseCase(productoRepo, almacenDatos)
    private val borrarProductoUC = BorrarProductoUseCase(productoRepo, almacenDatos)
    private val activarProductoUC = ActivarProductoUseCase(productoRepo, almacenDatos)
    // Necesitamos listar categorías para el desplegable del formulario
    private val listarCategoriasUC = ListarCategoriasUseCase(categoriaRepo, almacenDatos)

    private val _productos = MutableStateFlow<List<ProductoDTO>>(emptyList())
    val productos = _productos.asStateFlow()

    // Lista de categorías para el formulario
    private val _categorias = MutableStateFlow<List<CategoriaDTO>>(emptyList())
    val categorias = _categorias.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _selected = MutableStateFlow<ProductoDTO?>(null)
    val selected = _selected.asStateFlow()

    init {
        cargarDatos()
    }

   /* fun cargarDatos() {
        viewModelScope.launch {
            _productos.value = listarProductosUC.invoke()
            _categorias.value = listarCategoriasUC.invoke() // Cargamos categorías también
        }
    }*/

    fun cargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true

            // 1. Intentamos cargar PRODUCTOS
            try {
                _productos.value = listarProductosUC.invoke()
            } catch (e: Exception) {
                println("❌ Error cargando productos: ${e.message}")
                e.printStackTrace()
            }

            // 2. Intentamos cargar CATEGORÍAS (Independiente)
            try {
                val listaCategorias = listarCategoriasUC.invoke()
                _categorias.value = listaCategorias
                println("✅ Categorías cargadas en Productos: ${listaCategorias.size}")
            } catch (e: Exception) {
                println("❌ Error cargando categorías: ${e.message}")
                e.printStackTrace()
            }

            _isLoading.value = false
        }
    }

    fun selectProducto(p: ProductoDTO?) { _selected.value = p }

    fun crear(nombre: String, descripcion: String, precio: Double, imagen: String, catId: String, activo: Boolean) {
        val cmd = CrearProductoCommand(nombre, descripcion, precio, imagen, catId, activo)
        viewModelScope.launch {
            crearProductoUC.invoke(cmd)
            cargarDatos()
        }
    }

    fun actualizar(id: String, nombre: String, descripcion: String, precio: Double, imagen: String, catId: String, activo: Boolean) {
        val cmd = ActualizarProductoCommand(id, nombre, descripcion, precio, imagen, catId, activo)
        viewModelScope.launch {
            actualizarProductoUC.invoke(cmd)
            cargarDatos()
            if (_selected.value?.id == id) _selected.value = null
        }
    }

    fun borrar(id: String) {
        viewModelScope.launch {
            borrarProductoUC.invoke(id)
            cargarDatos()
        }
    }

    fun toggleActivo(p: ProductoDTO) {
        viewModelScope.launch {
            activarProductoUC.invoke(ActivarProductoCommand(p.id, !p.activo))
            cargarDatos()
        }
    }
}