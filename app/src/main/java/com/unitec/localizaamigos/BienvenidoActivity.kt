package com.unitec.localizaamigos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted

import kotlinx.android.synthetic.main.activity_bienvenido.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import kotlin.coroutines.suspendCoroutine


class BienvenidoActivity : AppCompatActivity() {

//Declaramos nuestro atributo de tipo Estatus


    var estatus=Estatus()
    var usuarios=ArrayList<Usuario>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        //INicializamos por primera vez la localizacion
        Globales.lat=19.66
        Globales.lng=66.66


        //Clickeo del boton
        localizar.setOnClickListener {
            var i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }

        //Programamos el evento del boton cuyo id es guardar para dentro de él invocar
        //la tarea asincronica
        guardar.setOnClickListener {

            //Ahora aqui invocamos a la tarea asincronoca por medio de su cosntructorTarea
            // TareaGuardarUsuario(applicationContext).execute()
            lifecycleScope.launch {
                whenStarted {
                    var respuesta = withContext(Dispatchers.IO) {
                        enviarAbackend()

                    }



                }
            }//Termina launch


        }

        buscar.setOnClickListener {


        }
    }

        //Generamos el metodo que hara conexion al back end
        suspend fun enviarAbackend():Estatus{
            //Cuando pones un suspend este tendra como tipo de retorno intrinseco la corutina:
            return suspendCoroutine {
                //Aqui dentro va todo lo que tu ponias en el metodo donInBackground de tu tareaAsincronoca
                var coordenada1 = Coordenada(Globales.lat!!, Globales.lng!!)

                var coordenadas = arrayListOf<Coordenada>(coordenada1)

                var usuario = Usuario(
                    "campitos",
                    "Juan Carlos",
                    "Campos",
                    "rapidclimate@gmail.com",
                    coordenadas
                )
                var retrofit = Retrofit.Builder()
                    .baseUrl("https://jcunitec.herokuapp.com/")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build()

                //PASO 2: GENERAR UN OBJETO PARAHABILITAR TU SERVICIO DE RETROFIT USANDO EL OBJETO
                //DEL PUNTO ANTERIOR
                var servicioUsuario = retrofit.create(ServicioUsuario::class.java)


                //PASO 3: ENVIAR AL BACK -END
                var envio = servicioUsuario.guardar(usuario)
                //SE ENVIA  AL BACK- END Y  EN ESTE MOMENTO SE OBTIENE LA RESPUESTA
                estatus = envio.execute().body()!!
                Log.i("TTT", estatus.mensaje!!)

            }
return estatus
        }




    }





