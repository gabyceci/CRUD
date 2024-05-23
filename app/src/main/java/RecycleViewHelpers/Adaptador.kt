package RecycleViewHelpers

import Modelo.listaProductos
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gabriela.arevalo.crudgabriela1a.R

class Adaptador(private var Datos: List<listaProductos>): RecyclerView.Adapter<ViewHolder>() {


    fun actualizarRecyclerView(nuevaLista: List<listaProductos>){
        Datos = nuevaLista
        notifyDataSetChanged() //Notifica que hay datos nuevos
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_itam_card, parent, false)

        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto
    }

}