package ies.sequeros.com.dam.pmdm.cliente.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.categorias.listar.CategoriaDTO
import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO
import ies.sequeros.com.dam.pmdm.cliente.ui.ClienteViewModel
import ies.sequeros.com.dam.pmdm.commons.ui.ImagenDesdePath
import vegaburguer.composeapp.generated.resources.Res
import vegaburguer.composeapp.generated.resources.compose_multiplatform

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoCliente(
    viewModel: ClienteViewModel,
    onCancelar: () -> Unit,
    onConfirmar: () -> Unit
) {
    val categoriaSeleccionada by viewModel.categoriaSeleccionada.collectAsState()
    val carrito by viewModel.carrito.collectAsState()
    val total by viewModel.totalPedido.collectAsState()
    val numProductos by viewModel.numProductos.collectAsState()

    val categorias by viewModel.categorias.collectAsState()
    val productos by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        // texto direccion
                        Text("VegaBurguer - C/ IES Antonio Sequeros",
                            style = MaterialTheme.typography.titleMedium)
                        if (categoriaSeleccionada != null) {
                            Text("Categoría: ${categoriaSeleccionada!!.nombre}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                actions = {
                    // Icono del carrito con contador
                    // BadgeBox pone el número de productos
                    BadgedBox(badge = {
                        if (numProductos > 0) Badge { Text("$numProductos") }
                    }) {
                        // aqui se pone el carrito de compra
                        Icon(
                            Icons.Default.ShoppingCart,
                            "Carrito",
                            modifier = Modifier.size(32.dp).clickable { onConfirmar() } // Al pulsar carrito va al resumen
                        )
                    }
                    // se pone el precio total
                    Spacer(Modifier.width(16.dp))
                    Text("${total} €", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        // ÁREA INFERIOR
        bottomBar = {
            Row(
                Modifier.fillMaxWidth().padding(16.dp).height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.vaciarCarrito()
                        onCancelar()
                    },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("CANCELAR PEDIDO")
                }

                Button(
                    onClick = onConfirmar,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    enabled = numProductos > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Verde
                ) {
                    Text("CONFIRMAR PEDIDO")
                }
            }
        }
    ) { padding ->

        // --- ÁREA CENTRAL (Navegación) ---
        Box(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (categoriaSeleccionada == null) {
                // VISTA DE CATEGORÍAS
                ListaCategoriasCliente(categorias) { cat ->
                    viewModel.seleccionarCategoria(cat)
                }
            } else {
                // VISTA DE PRODUCTOS (De esa categoría)
                Column {
                    Button(onClick = { viewModel.seleccionarCategoria(null) }) {
                        Icon(Icons.Default.ArrowBack, null)
                        Text(" Volver a Categorías")
                    }
                    Spacer(Modifier.height(8.dp))

                    val productosFiltrados = productos.filter { it.categoriaId == categoriaSeleccionada!!.id }

                    ListaProductosCliente(
                        productos = productosFiltrados,
                        carrito = carrito,
                        onAdd = { viewModel.addProducto(it) },
                        onRemove = { viewModel.removeProducto(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun ListaCategoriasCliente(
    categorias: List<CategoriaDTO>,
    onSelect: (CategoriaDTO) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(180.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categorias.size) { i ->
            val cat = categorias[i]
            Card(
                onClick = { onSelect(cat) },
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    val imagePath = remember(cat.imagePath) { mutableStateOf(cat.imagePath) }
                    Box(Modifier.size(120.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                        ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(cat.nombre, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ListaProductosCliente(
    productos: List<ProductoDTO>,
    carrito: List<ies.sequeros.com.dam.pmdm.cliente.ui.LineaCarrito>,
    onAdd: (ProductoDTO) -> Unit,
    onRemove: (ProductoDTO) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp), // Más ancho para ver detalles
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(productos.size) { i ->
            val prod = productos[i]
            val cantidadEnCarrito = carrito.find { it.producto.id == prod.id }?.cantidad ?: 0

            Card(elevation = CardDefaults.cardElevation(2.dp)) {
                Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    val imagePath = remember(prod.imagePath) { mutableStateOf(prod.imagePath) }
                    Box(Modifier.size(80.dp).background(Color.LightGray, CircleShape)) {
                        ImagenDesdePath(imagePath, Res.drawable.compose_multiplatform, Modifier.fillMaxSize())
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(prod.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(prod.descripcion, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                        Text("${prod.precio} €", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    }

                    // Botones + y -
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        IconButton(onClick = { onAdd(prod) }) {
                            Icon(Icons.Default.AddCircle, "Añadir", tint = MaterialTheme.colorScheme.primary)
                        }

                        if (cantidadEnCarrito > 0) {
                            Text("$cantidadEnCarrito", style = MaterialTheme.typography.headlineSmall)
                            IconButton(onClick = { onRemove(prod) }) {
                                Icon(Icons.Default.RemoveCircle, "Quitar", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}