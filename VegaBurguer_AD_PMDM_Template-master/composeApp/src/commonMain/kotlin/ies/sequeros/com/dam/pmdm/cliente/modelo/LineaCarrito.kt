package ies.sequeros.com.dam.pmdm.cliente.modelo

import ies.sequeros.com.dam.pmdm.administrador.aplicacion.productos.listar.ProductoDTO

data class LineaCarrito(
    val producto: ProductoDTO,
    var cantidad: Int
)