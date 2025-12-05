package ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoScreen(
    viewModel: BandejaPedidosViewModel,
    onBack: () -> Unit
) {
    val pedido by viewModel.selected.collectAsState()

    if (pedido == null) {
        // Si no hay pedido seleccionado, volvemos
        LaunchedEffect(Unit) { onBack() }
        return
    }
    if (pedido != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Pedido de ${pedido!!.cliente}") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, "Volver")
                        }
                    }
                )
            },
            bottomBar = {
                Button(
                    onClick = { viewModel.entregarPedido(pedido!!.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
                ) {
                    Icon(Icons.Default.CheckCircle, null)
                    Spacer(Modifier.width(8.dp))
                    Text("MARCAR COMO ENTREGADO")
                }
            }
        ) { padding ->
            Column(Modifier.padding(padding).padding(16.dp)) {

                // Cabecera
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            "Total: ${pedido!!.total} €",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            "Fecha: ${pedido!!.fecha}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Productos:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

            // Lista de productos del pedido
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(pedido!!.lineas) { linea ->
                    ListItem(
                        headlineContent = { Text("Producto ID: ${linea.productoId}") },
                        trailingContent = {
                            Text("x${linea.cantidad}", fontWeight = FontWeight.Bold)
                        },
                        tonalElevation = 2.dp
                    )
                }
            }
                val lineas = pedido?.lineas ?: emptyList()

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(lineas) { linea ->
                        ListItem(
                            headlineContent = { Text("Producto ID: ${linea.productoId}") },
                            trailingContent = {
                                Text("x${linea.cantidad}", fontWeight = FontWeight.Bold)
                            },
                            tonalElevation = 2.dp
                        )
                    }
                }
            }
        }
    }
}