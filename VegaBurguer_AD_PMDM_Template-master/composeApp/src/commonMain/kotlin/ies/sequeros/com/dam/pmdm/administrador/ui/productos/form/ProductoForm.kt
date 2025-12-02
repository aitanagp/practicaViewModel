package ies.sequeros.com.dam.pmdm.administrador.ui.productos.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.productos.ProductosViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import ies.sequeros.com.dam.pmdm.commons.ui.SelectorImagenComposable
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoForm(
    productosViewModel: ProductosViewModel,
    onClose: () -> Unit,
    onConfirm: (ProductoFormState) -> Unit
) {
    val formViewModel: ProductoFormViewModel = viewModel {
        ProductoFormViewModel(productosViewModel.selected.value, onConfirm)
    }

    val state by formViewModel.uiState.collectAsState()
    val isValid by formViewModel.isFormValid.collectAsState()
    val categorias by productosViewModel.categorias.collectAsState()

    var expandedCat by remember { mutableStateOf(false) }
    val imagePath = remember { mutableStateOf(state.imagePath) }

    LaunchedEffect(state.imagePath) { imagePath.value = state.imagePath }

    Column(
        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Formulario Producto", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = state.nombre,
            onValueChange = { formViewModel.onNombreChange(it) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.descripcion,
            onValueChange = { formViewModel.onDescripcionChange(it) },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.precio,
            onValueChange = { formViewModel.onPrecioChange(it) },
            label = { Text("Precio (€)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de Categoría (desplegable)
        ExposedDropdownMenuBox(
            expanded = expandedCat,
            onExpandedChange = { expandedCat = !expandedCat }
        ) {
            val catSeleccionada = categorias.find { it.id == state.categoriaId }?.nombre ?: "Seleccione Categoría"
            OutlinedTextField(
                value = catSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                categorias.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat.nombre) },
                        onClick = {
                            formViewModel.onCategoriaChange(cat.id)
                            expandedCat = false
                        }
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = state.activo, onCheckedChange = { formViewModel.onActivoChange(it) })
            Text("Activo")
        }

        SelectorImagenComposable { path -> formViewModel.onImageChange(path) }

        Box(Modifier.height(150.dp).fillMaxWidth()) {
            ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxSize())
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { formViewModel.submit() }, enabled = isValid) {
                Icon(Icons.Default.Save, null)
                Text("Guardar")
            }
            OutlinedButton(onClick = onClose) {
                Text("Cancelar")
            }
        }
    }
}