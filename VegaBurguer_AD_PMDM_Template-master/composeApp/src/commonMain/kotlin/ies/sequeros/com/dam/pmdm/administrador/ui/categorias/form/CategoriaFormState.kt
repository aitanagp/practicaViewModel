package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

data class CategoriaFormState(
    val nombre: String = "",
    val imagePath: String = "",
    val activa: Boolean = true,

    // Errores
    val nombreError: String? = null,
    val imagePathError : String? = null,

    // estado formulario
    val isValid: Boolean = false
)
