package ies.sequeros.com.dam.pmdm.dependiente.ui.bandeja

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandejaPedidosScreen(
    viewModel: BandejaPedidosViewModel,
    onPedidoClick: (String) -> Unit,
    onBack: () -> Unit
) {
    // Observamos el estado del ViewModel
    val pedidos by viewModel.pedidos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Recargar pedidos cada vez que se entra en esta pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarPedidos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos Pendientes") },
                actions = {
                    // Botón para recargar manualmente
                    IconButton(onClick = { viewModel.cargarPedidos() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar")
                    }
                    // Botón para salir/volver
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                pedidos.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No hay pedidos pendientes", style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { viewModel.cargarPedidos() }, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Actualizar")
                        }
                    }
                }
                else -> {
                    // Lista de pedidos
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pedidos) { pedido ->
                            PedidoItemCard(
                                pedido = pedido,
                                onClick = { onPedidoClick(pedido.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta individual para cada pedido
@Composable
fun PedidoItemCard(
    pedido: PedidoDTO,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${pedido.id}", // Si el ID es muy largo, puedes recortarlo
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${pedido.total} €",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = pedido.cliente, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = pedido.fecha, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}