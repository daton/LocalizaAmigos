package com.unitec.localizaamigos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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


        //VAMOS A PROBAR QUÉ SUCEDE SI COLOCAMOS AQUI LA INVOCACION O LLAMADA DE LA CORUTINA
        lifecycleScope.launch {
            whenStarted {
                var respuesta = withContext(Dispatchers.IO) {
                    obtenerUsuarios()


                }


                //Vamos a veerificar que siga activo el id de seguridad interno
                var idMio = Settings.Secure.getString(
                    applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                Toast.makeText(applicationContext, "Vamos a ver ${idMio}", Toast.LENGTH_LONG)
                    .show()

            }

        }


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
            //Aqui nosotros simplemente vamos a invocar la activity de Busquedas
            var intent=Intent(applicationContext, BusquedasActivity::class.java)
            startActivity(intent)


        } //Esta llave de cierre es del boton buscar
    }// Esta llave de cierre es la del metodo onCreate()
/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

 */
        //Generamos el metodo que hara conexion al back end
        suspend fun enviarAbackend():Estatus{
            //Cuando pones un suspend este tendra como tipo de retorno intrinseco la corutina:
            return suspendCoroutine {
                //Aqui dentro va todo lo que tu ponias en el metodo donInBackground de tu tareaAsincronoca


                var coordenada=Coordenada(Globales.lat, Globales.lng)
                var coordenadas=ArrayList<Coordenada>()
                coordenadas.add(coordenada)

                var usuario = Usuario(
                    "campitos",
                 //   Settings.Secure.getString(applicationContext.contentResolver,Settings.Secure.ANDROID_ID),
                    "Campos",
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

    suspend fun obtenerUsuarios(){
        //Cuando pones un suspend este tendra como tipo de retorno intrinseco la corutina:
        return suspendCoroutine {
            //Aqui dentro va todo lo que tu ponias en el metodo donInBackground de tu tareaAsincronoca

            var retrofit = Retrofit.Builder()
                .baseUrl("https://jcunitec.herokuapp.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build()

            //PASO 2: GENERAR UN OBJETO PARAHABILITAR TU SERVICIO DE RETROFIT USANDO EL OBJETO
            //DEL PUNTO ANTERIOR
            var servicioUsuario = retrofit.create(ServicioUsuario::class.java)


            //PASO 2
            var enviarUsuarios = servicioUsuario.buscarTodos()


            var coordenadas=ArrayList<Coordenada>()

            //SE ENVIA  AL BACK- END Y  EN ESTE MOMENTO SE OBTIENE LA RESPUESTA
            usuarios = enviarUsuarios.execute().body()!!

            //Aqui viene la magia para compartir con el activity BusquedasActvity
            //nuestra variable usuarios de arriba
            Globales.usuarios=usuarios

            Log.i("TTT","Usuarios encontrados ${usuarios}")

        }

    }




    }





