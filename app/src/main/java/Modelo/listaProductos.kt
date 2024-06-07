package Modelo

import java.util.UUID

data class listaProductos(
    val uuid: String,
    var nombreProducto: String,
    var precio: Int,
    var cantidad: Int

)
