package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.ClienteViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoCliente(
    viewModel: ClienteViewModel,
    onVolver: () -> Unit,
    onConfirmar: () -> Unit, // Ir a pago
    onCancelarPedido: () -> Unit // Volver al inicio y borrar
) {
    val carrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalPedido.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Pedido") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp).height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.vaciarCarrito()
                        onCancelarPedido()
                    },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("CANCELAR TODO")
                }

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    enabled = carrito.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("CONFIRMAR Y PAGAR (${total}€)")
                }
            }
        }
    ) { padding ->
        if (carrito.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("El carrito está vacío", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onVolver) { Text("Volver al catálogo") }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(carrito) { linea ->
                    Card(elevation = CardDefaults.cardElevation(2.dp)) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(linea.producto.nombre, fontWeight = FontWeight.Bold)
                                Text("${linea.producto.precio} €/ud", style = MaterialTheme.typography.bodySmall)
                                Text("Subtotal: ${total} €", color = MaterialTheme.colorScheme.primary)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.removeProducto(linea.producto) }) {
                                    Icon(Icons.Default.RemoveCircle, "Menos")
                                }
                                Text("${linea.cantidad}", style = MaterialTheme.typography.titleLarge)
                                IconButton(onClick = { viewModel.addProducto(linea.producto) }) {
                                    Icon(Icons.Default.AddCircle, "Más")
                                }
                                IconButton(onClick = { viewModel.eliminarLineaCompleta(linea.producto) }) {
                                    Icon(Icons.Default.Delete, "Borrar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}