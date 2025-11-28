package ies.sequeros.com.dam.pmdm.administrador.ui.productos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.compose_multiplatform

@Composable
fun ProductoCard(
    item: ProductoDTO,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit
) {
    val alpha = if (item.activo) 1f else 0.5f
    val borderColor = if (item.activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    // Estado local para la imagen
    val imagePath = remember(item.imagePath) { mutableStateOf(item.imagePath) }

    Card(
        modifier = Modifier.padding(8.dp).fillMaxWidth().alpha(alpha),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(100.dp).clip(CircleShape).border(2.dp, borderColor, CircleShape).background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxSize())
            }

            Spacer(Modifier.height(8.dp))
            Text(item.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("${item.precio} €", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
            Text(item.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2)

            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(onClick = onToggleActive) {
                    Icon(if (item.activo) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, null)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}