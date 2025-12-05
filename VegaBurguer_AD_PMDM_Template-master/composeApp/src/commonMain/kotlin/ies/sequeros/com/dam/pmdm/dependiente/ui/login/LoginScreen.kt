package ies.sequeros.com.dam.pmdm.dependiente.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.dependientes.listar.DependienteDTO

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (DependienteDTO) -> Unit,
    //onNavigateToAdmin: () -> Unit,
    //onNavigateToDependiente: () -> Unit,
    onBack: () -> Unit
) {
    val usuario by viewModel.usuario.collectAsState()
    val clave by viewModel.clave.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val usuarioLogueado by viewModel.usuarioLogueado.collectAsState()

    LaunchedEffect(usuarioLogueado) {
        usuarioLogueado?.let { //user ->
            onLoginSuccess(it)
            /*usuarioLogueado?.let { user ->
                // 1. Guardar sesión global
                appViewModel.setUsuarioSesion(user)

                // 2. Decidir navegación según el rol
                if (user.isAdmin) {
                    onNavigateToAdmin()
                } else {
                    onNavigateToDependiente()
                }
            }*/
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(16.dp).width(400.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Acceso Dependientes", style = MaterialTheme.typography.headlineMedium)

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { viewModel.onUsuarioChange(it) },
                    label = { Text("Usuario") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = clave,
                    onValueChange = { viewModel.onClaveChange(it) },
                    label = { Text("Contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text("Entrar")
                    }
                }

                // Botón para volver atrás
                TextButton(onClick = onBack) {
                    Text("Volver")
                }
            }
        }
    }
}