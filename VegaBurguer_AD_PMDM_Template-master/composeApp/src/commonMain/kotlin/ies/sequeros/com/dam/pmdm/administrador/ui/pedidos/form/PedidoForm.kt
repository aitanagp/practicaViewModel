package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.form

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ies.sequeros.com.dam.pmdm.administrador.ui.pedidos.PedidosViewModel

@Composable
fun PedidoForm(
    pedidosViewModel: PedidosViewModel,
    onClose: () -> Unit,
    onConfirm: (PedidoFormState) -> Unit
) {
    // Inicializamos el VM del formulario con el pedido seleccionado
    val formViewModel: PedidoFormViewModel = viewModel {
        PedidoFormViewModel(
            pedidosViewModel.selected.value,
            onConfirm
        )
    }

    val state by formViewModel.uiState.collectAsState()
    val isValid by formViewModel.isFormValid.collectAsState()
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .defaultMinSize(minHeight = 200.dp),
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Receipt, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("Detalle del Pedido", style = MaterialTheme.typography.headlineSmall)
            }

            // Campo Cliente
            OutlinedTextField(
                value = state.cliente,
                onValueChange = { formViewModel.onClienteChange(it) },
                label = { Text("Nombre del Cliente") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = state.clienteError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (state.clienteError != null) {
                Text(state.clienteError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            // Información de solo lectura (Fecha y Total)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Fecha", style = MaterialTheme.typography.labelMedium)
                    Text(if (state.fecha.isEmpty()) "Hoy" else state.fecha, style = MaterialTheme.typography.bodyLarge)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total", style = MaterialTheme.typography.labelMedium)
                    Text("${state.total} €", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                }
            }

            HorizontalDivider()

            // Selector de Estado
            Text("Estado del Pedido:", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("PENDIENTE", "ENTREGADO", "CANCELADO").forEach { estadoOp ->
                    FilterChip(
                        selected = state.estado == estadoOp,
                        onClick = { formViewModel.onEstadoChange(estadoOp) },
                        label = { Text(estadoOp) },
                        leadingIcon = if (state.estado == estadoOp) {
                            { Icon(Icons.Default.Check, null) }
                        } else null,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when(estadoOp) {
                                "ENTREGADO" -> Color(0xFFE8F5E9) // Verde claro
                                "CANCELADO" -> Color(0xFFFFEBEE) // Rojo claro
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { formViewModel.submit() },
                    enabled = isValid
                ) {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(4.dp))
                    Text("Guardar Cambios")
                }

                OutlinedButton(onClick = onClose) {
                    Text("Cerrar")
                }
            }
        }
    }
}