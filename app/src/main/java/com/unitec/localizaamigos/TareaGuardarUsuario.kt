package com.unitec.localizaamigos

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class TareaGuardarUsuario(var contexto: Context?): AsyncTask<Void, Void, Void>() {

    //Declaramos como atributos
    var coordenada1=Coordenada(Globales.lat!!,Globales.lng!!)
   // var coordenada2=Coordenada(19.51,99.63)

    var coordenadas= arrayListOf<Coordenada>(coordenada1)

    var usuario=Usuario("campitos",
        "Juan Carlos",
        "Campos",
        "rapidclimate@gmail.com",
       coordenadas)

    //Finalmete ponemos el objeto de respuesta (Estatus)
    var estatus=Estatus()

    override fun onPreExecute() {
        super.onPreExecute()
        //Este metodo sirve para pedir informacion a la interfaz de usuario
        //Para hacerlo rapido crearemos un objeto fake

    }

    //Este metodo sirve para enviar al back la informacion en este caso, la info fake
    //declarada arriba
    override fun doInBackground(vararg p0: Void?): Void? {
      //Aqui vamos a prparar nuestro usuario para enviarlo hacia el background con retrofit
      //Paso 1: Crear un objeto de tipo retrofit
        var retrofit =Retrofit.Builder()
            .baseUrl("https://jcunitec.herokuapp.com/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        //PASO 2: GENERAR UN OBJETO PARAHABILITAR TU SERVICIO DE RETROFIT USANDO EL OBJETO
        //DEL PUNTO ANTERIOR
        var servicioUsuario=retrofit.create(ServicioUsuario::class.java)
        //PASO 3: ENVIAR AL BACK -END
        var envio=servicioUsuario.guardar(usuario)
        //SE ENVIA  AL BACK- END Y  EN ESTE MOMENTO SE OBTIENE LA RESPUESTA
        estatus=envio.execute().body()!!


        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        //FINALMENTE VAMOS A MOSTRAR EN PANTALLA (INTERFAZ DE USUARIO) EL ESTATUS DE NUESTRO
        //BACK
        Toast.makeText(contexto,estatus.mensaje, Toast.LENGTH_LONG ).show()
    }
}