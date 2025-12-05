package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.ClienteViewModel

@Composable
fun PagoCliente(
    viewModel: ClienteViewModel,
    onFinalizar: () -> Unit,
    onBack: () -> Unit
) {
    val total by viewModel.totalPedido.collectAsState()
    val nombre by viewModel.nombreCliente.collectAsState()

    // Estado local para opción de pago
    val opcionesPago = listOf("Tarjeta de Crédito", "Efectivo")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(opcionesPago[0]) }

    var procesando by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Resumen de Pago", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(24.dp))

        Text("Cliente: $nombre", style = MaterialTheme.typography.titleMedium)
        Text("Total a Pagar: $total €", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        Spacer(Modifier.height(32.dp))

        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Seleccione método de pago:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                opcionesPago.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) }
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                procesando = true
                // Guardamos en BBDD y finalizamos
                viewModel.confirmarPedido {
                    procesando = false
                    onFinalizar()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !procesando,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            if (procesando) {
                CircularProgressIndicator(color = Color.White)
            } else {
                Icon(Icons.Default.CheckCircle, null)
                Spacer(Modifier.width(8.dp))
                Text("FINALIZAR Y PAGAR")
            }
        }
        // Botón para volver atrás
        TextButton(onClick = onBack) {
            Text("Volver")
        }
    }
}