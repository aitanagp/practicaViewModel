package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO

@Composable
fun Productos(
    viewModel: ProductosViewModel,
    onSelectItem: (ProductoDTO?) -> Unit
) {
    val items by viewModel.productos.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                viewModel.selectProducto(null)
                onSelectItem(null)
            }) {
                Icon(Icons.Default.Add, "Nuevo Producto")
                Text("Nuevo Producto")
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(columns = GridCells.Adaptive(180.dp)) {
            items(items.size) { index ->
                ProductoCard(
                    item = items[index],
                    onEdit = {
                        viewModel.selectProducto(items[index])
                        onSelectItem(items[index])
                    },
                    onDelete = { viewModel.borrar(items[index].id) },
                    onToggleActive = { viewModel.toggleActivo(items[index]) }
                )
            }
        }
    }
}