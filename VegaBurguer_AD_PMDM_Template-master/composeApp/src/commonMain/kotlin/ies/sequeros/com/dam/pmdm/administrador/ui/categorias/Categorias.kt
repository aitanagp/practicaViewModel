package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO

import ies.sequeros.com.dam.pmdm.administrador.ui.AdminRoutes
import ies.sequeros.com.dam.pmdm.administrador.ui.MainAdministradorViewModel

@Composable
fun Categorias(
    categoriasViewModel: CategoriasViewModel,
    onSelectItem: (CategoriaDTO?) -> Unit
) {
    // Observamos el estado de la lista del ViewModel
    val items by categoriasViewModel.categorias.collectAsState()

    // Estado local para el buscador
    var searchText by remember { mutableStateOf("") }

    // Filtramos la lista en tiempo real según el texto de búsqueda
    val filteredItems = items.filter {
        if (searchText.isNotBlank()) {
            it.nombre.contains(searchText, ignoreCase = true)
        } else {
            true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // --- Barra Superior (Buscador + Botón Añadir) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                shape = RoundedCornerShape(16.dp),
                placeholder = { Text("Buscar categoría...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier.weight(1f).padding(8.dp)
            )

            Spacer(Modifier.width(8.dp))

            // Botón para crear nueva categoría
            OutlinedButton(
                onClick = {
                    categoriasViewModel.selectCategoria(null) // Limpiamos selección
                    onSelectItem(null) // Navegamos al formulario vacío
                },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
            }
        }

        // --- Rejilla de Categorías ---
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 250.dp), // Adaptable (más pequeño que dependientes)
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredItems.size) { index ->
                val item = filteredItems[index]

                CategoriaCard(
                    item = item,
                    onActivate = { categoriasViewModel.cambiarEstado(item) },
                    onDeactivate = { categoriasViewModel.cambiarEstado(item) },
                    onEdit = {
                        // Seleccionamos en el VM y notificamos navegación
                        categoriasViewModel.selectCategoria(item)
                        onSelectItem(item)
                    },
                    onDelete = {
                        // Borramos directamente
                        categoriasViewModel.borrar(item.id)
                    }
                )
            }
        }
    }
}