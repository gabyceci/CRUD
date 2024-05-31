package RecycleViewHelpers

import Modelo.ClaseConexion
import Modelo.listaProductos
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gabriela.arevalo.crudgabriela1a.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Adaptador(private var Datos: List<listaProductos>): RecyclerView.Adapter<ViewHolder>() {


    fun actualizarRecyclerView(nuevaLista: List<listaProductos>){
        Datos = nuevaLista
        notifyDataSetChanged() //Notifica que hay datos nuevos
    }

    //1- Crear la funcion de eliminar
    fun eliminarRegistro(nombreProducto: String, position: Int){
        //Notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)

        //Quitar de la base de datos
        GlobalScope.launch(Dispatchers.IO){
            //Dos pasos para eliminar de la base de datos

            //1- Crear un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo una variale que contenga un PrepareStatement
            val deleteProducto = objConexion?.prepareStatement("delete from tbProductoss where nombreProducto = ?")!!
            deleteProducto.setString(1, nombreProducto)
            deleteProducto.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }

        //Notificamos el cambio para que refresque la lista
        Datos = listaDatos.toList()

        //Quito los datos de la lista
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_itam_card, parent, false)

        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = Datos[position]
        holder.textView.text = producto.nombreProducto








        //Darle click al icono de borrar
        holder.imgBorrar.setOnClickListener {


            //Crear una alerta de confirmacion para que se borre

            val context = holder.textView.context

            val builder = AlertDialog.Builder(context)

            builder.setTitle("Eliminar")
            builder.setMessage("¿Estas seguro que deseas elminar?")

            //Botones de mi alerta
            builder.setPositiveButton("Si"){
                dialog, wich ->
                eliminarRegistro(producto.nombreProducto, position)
            }

            builder.setNegativeButton("No"){
                dialog, wich ->
                //Si doy click en "No" se cierra la alerta
                dialog.dismiss()
            }

            //Para mostrar la alerta
            val dialog = builder.create()
            dialog.show()


        }
    }

}