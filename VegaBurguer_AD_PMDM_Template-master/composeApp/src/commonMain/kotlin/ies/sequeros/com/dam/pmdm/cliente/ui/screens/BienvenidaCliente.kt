package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.cliente.ui.ClienteViewModel

@Composable
fun BienvenidaCliente(
    viewModel: ClienteViewModel,
    onComenzar: () -> Unit,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(Modifier.padding(16.dp).width(400.dp)) {
            Column(
                Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Bienvenido a VegaBurguer", style = MaterialTheme.typography.headlineLarge)
                Text("Por favor, introduce tu nombre para comenzar")

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Tu Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Button(
                    onClick = {
                        viewModel.setCliente(nombre)
                        onComenzar()
                    },
                    enabled = nombre.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("COMENZAR PEDIDO")
                }
               OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }
    }
}