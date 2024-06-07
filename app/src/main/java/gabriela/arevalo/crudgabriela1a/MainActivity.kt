package gabriela.arevalo.crudgabriela1a

import Modelo.ClaseConexion
import Modelo.listaProductos
import RecycleViewHelpers.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //1- Mandar llamar todos los elementos de la vista
        val txtNombre = findViewById<EditText>(R.id.txtNombre)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecio)
        val txtCantidad = findViewById<EditText>(R.id.txtCantidad)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvDatos = findViewById<RecyclerView>(R.id.rcvDatos)


        //1- Ponerle un layout a mi recycleview
        rcvDatos.layoutManager = LinearLayoutManager(this)

        ////////Función para mostrar datos
        fun obtenerDatos(): List<listaProductos>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbProductoss")!!

            val listadoProductos = mutableListOf<listaProductos>()

            //Recorrer todos los datos que me trajo el select

            while (resultSet.next()){
                val uuid = resultSet.getString("uuid")
                val nombre = resultSet.getString("nombreProducto")
                val precio = resultSet.getInt("precio")
                val cantidad = resultSet.getInt("cantidad")
                val producto = listaProductos(uuid, nombre, precio, cantidad)
                listadoProductos.add(producto)
            }


            return listadoProductos

        }

        //Ejecutamos la funcion
        CoroutineScope(Dispatchers.IO).launch {
            val ejecutarFuncion = obtenerDatos()
            withContext(Dispatchers.Main){
                //Asigno el adaptador mi RecylerView
                //uNO MI ADAPTADOR CON EL RECYCLERVIEW
                val miAdaptador = Adaptador(ejecutarFuncion)
                rcvDatos.adapter = miAdaptador
            }
        }



        //2- Programar el boton de agregar
        btnAgregar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                //Guardar datos
                //1- Crear un objeto de la clase de conexion
                val objConexion = ClaseConexion().cadenaConexion()


                //2- Crear una variable que sea igual a un PrepareStatement
                val addProducto = objConexion?.prepareStatement("insert into tbProductoss(uuid, nombreProducto, precio, cantidad) values(?,?,?,?)")!!
                addProducto.setString(1, UUID.randomUUID().toString())
                addProducto.setString(2, txtNombre.text.toString())
                addProducto.setInt(3, txtPrecio.text.toString().toInt())
                addProducto.setInt(4, txtCantidad.text.toString().toInt())

                addProducto.executeUpdate()

                val nuevosProductos = obtenerDatos()

                //Creo una corrutina que actualice el listado
                withContext(Dispatchers.Main){
                    (rcvDatos.adapter as? Adaptador)?.actualizarRecyclerView(nuevosProductos)
                }
            }
        }


        //Mostrar datos


    }
}

