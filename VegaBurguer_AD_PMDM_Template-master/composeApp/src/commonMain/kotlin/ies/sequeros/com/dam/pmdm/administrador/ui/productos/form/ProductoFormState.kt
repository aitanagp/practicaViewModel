package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

data class ProductoFormState(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "", // Usamos String para el input de texto
    val imagePath: String = "",
    val categoriaId: String = "", // para seleccionar la categoria
    val activo: Boolean = true,

    // Errores
    val nombreError: String? = null,
    val descripcionError: String? = null,
    val precioError: String? = null,
    val categoriaError: String? = null, // por si no se selecciona lacategoria salte un error
    val imagePathError: String? = null,

    val isValid: Boolean = false
)