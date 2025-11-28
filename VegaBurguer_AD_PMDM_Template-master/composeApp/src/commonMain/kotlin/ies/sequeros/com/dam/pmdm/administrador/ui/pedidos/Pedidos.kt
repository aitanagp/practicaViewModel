package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO

@Composable
fun Pedidos(
    viewModel: PedidosViewModel,
    onPedidoClick: (PedidoDTO) -> Unit = {} // Por si quieres navegar al detalle
) {
    val pedidos by viewModel.pedidos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Recargar al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarPedidos()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Cabecera
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Listado de Pedidos",
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = { viewModel.cargarPedidos() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Recargar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (pedidos.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay pedidos registrados.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            // Lista de Pedidos (Usamos LazyColumn porque es una lista vertical simple)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pedidos) { pedido ->
                    PedidoCard(
                        pedido = pedido,
                        onClick = {
                            viewModel.selectPedido(pedido)
                            onPedidoClick(pedido)
                        }
                    )
                }
            }
        }
    }
}