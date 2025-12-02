package ies.sequeros.com.dam.pmdm.administrador.ui.pedidos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.pedidos.listar.PedidoDTO

@Composable
fun PedidoCard(
    pedido: PedidoDTO,
    onClick: () -> Unit
) {
    // Color del borde según estado (ejemplo simple)
    val borderColor = when (pedido.estado.uppercase()) {
        "ENTREGADO" -> Color.Green
        "CANCELADO" -> Color.Red
        else -> MaterialTheme.colorScheme.primary // PENDIENTE
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icono y Datos Principales
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "Pedido",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = pedido.cliente,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = pedido.fecha,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Estado: ${pedido.estado}",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (pedido.estado == "PENDIENTE") Color(0xFFFF8C00) else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Precio Total
            Text(
                text = "${pedido.total} €",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}