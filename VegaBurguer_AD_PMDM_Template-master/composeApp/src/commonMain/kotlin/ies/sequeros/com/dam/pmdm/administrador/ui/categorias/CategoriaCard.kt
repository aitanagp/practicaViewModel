package ies.sequeros.com.dam.pmdm.administrador.ui.categorias

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath


import vegaburguer.composeapp.generated.resources.Res
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import vegaburguer.composeapp.generated.resources.compose_multiplatform

@Suppress("UnrememberedMutableState")
@Composable
fun CategoriaCard(
    item: CategoriaDTO,
    onActivate: (CategoriaDTO) -> Unit,
    onDeactivate: (CategoriaDTO) -> Unit,
    onEdit: (CategoriaDTO) -> Unit,
    onDelete: (CategoriaDTO) -> Unit
) {
    // Animación de opacidad según si está activa o no
    val cardAlpha by animateFloatAsState(if (item.activa) 1f else 0.5f)

    // Estado para la imagen (si viene vacía se podría manejar aquí)
    val imagePath = mutableStateOf(if (item.imagePath.isNotEmpty()) item.imagePath else "")

    // Color del borde según estado
    val borderColor = if (item.activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .alpha(cardAlpha),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen circular
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(3.dp, borderColor, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                // Usamos ImagenDesdePath igual que en Dependientes
                // Asegúrate de tener un drawable por defecto (ej. 'compose_multiplatform' o uno de comida)
                ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxWidth())
            }

            // Nombre de la categoría
            Text(
                text = item.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Chip de Estado
            AssistChip(
                onClick = {},
                label = { Text(if (item.activa) "Activa" else "Inactiva") },
                leadingIcon = {
                    Icon(
                        if (item.activa) Icons.Default.CheckCircle else Icons.Default.Block,
                        contentDescription = null,
                        tint = if (item.activa) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            HorizontalDivider(Modifier.fillMaxWidth(0.8f))

            // Botones de Acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Activar / Desactivar
                OutlinedIconButton(
                    onClick = {
                        if (item.activa) onDeactivate(item) else onActivate(item)
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (item.activa) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Icon(
                        if (item.activa) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (item.activa) "Desactivar" else "Activar"
                    )
                }

                // Editar
                OutlinedIconButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }

                // Eliminar
                OutlinedIconButton(
                    onClick = { onDelete(item) },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}