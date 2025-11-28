package ies.sequeros.com.dam.pmdm.administrador.ui.categorias.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import ies.sequeros.com.dam.pmdm.administrador.ui.dependientes.DependientesViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import ies.sequeros.com.dam.pmdm.commons.ui.SelectorImagenComposable

import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.hombre
import ies.sequeros.com.dam.pmdm.administrador.ui.categorias.CategoriasViewModel
import vegaburguer.composeapp.generated.resources.compose_multiplatform
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CategoriaForm(
    categoriasViewModel: CategoriasViewModel,
    onClose: () -> Unit,
    onConfirm: (datos: CategoriaFormState) -> Unit = {},
    categoriaFormViewModel: CategoriaFormViewModel = viewModel {
        CategoriaFormViewModel(
            categoriasViewModel.selected.value, onConfirm
        )
    }
) {
    val state by categoriaFormViewModel.uiState.collectAsState()
    val formValid by categoriaFormViewModel.isFormValid.collectAsState()
    val selected by categoriasViewModel.selected.collectAsState()

    val imagePath = remember { mutableStateOf(if (state.imagePath.isNotEmpty()) state.imagePath else "") }

    LaunchedEffect(state.imagePath) {
        if (state.imagePath.isNotEmpty()) {
            imagePath.value = state.imagePath
        }
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Título ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = if (selected == null) "Crear nueva categoría" else "Editar categoría",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo: Nombre ---
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { categoriaFormViewModel.onNombreChange(it) },
                label = { Text("Nombre de la categoría") },
                leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
                isError = state.nombreError != null,
                modifier = Modifier.fillMaxWidth()
            )

            state.nombreError?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
            }

            // --- Checkbox: Activa ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.activa,
                    onCheckedChange = { categoriaFormViewModel.onActivaChange(it) }
                )
                Text("Categoría Activa", style = MaterialTheme.typography.bodyMedium)
            }

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // --- Selector de Imagen ---
            Text("Selecciona una imagen:", style = MaterialTheme.typography.titleSmall)

            SelectorImagenComposable({ path: String ->
                categoriaFormViewModel.onImagePathChange(path)
                imagePath.value = path
            })

            Spacer(Modifier.height(8.dp))

            // Previsualización
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxSize())
            }

            state.imagePathError?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error)
            }

            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

            // --- Botones ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(onClick = { categoriaFormViewModel.clear() }) {
                    Icon(Icons.Filled.Autorenew, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }

                Button(
                    onClick = {
                        categoriaFormViewModel.submit(
                            onSuccessSubmit = { formState -> onConfirm(formState) }
                        )
                    },
                    enabled = formValid
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Guardar")
                }

                FilledTonalButton(onClick = { onClose() }) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cancelar")
                }
            }
        }
    }
}