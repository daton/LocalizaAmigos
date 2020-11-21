package com.unitec.localizaamigos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_bienvenido.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class BienvenidoActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenido)

        //Clickeo del boton
        localizar.setOnClickListener {
            var i=Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }

        //Programamos el evento del boton cuyo id es guardar para dentro de él invocar
        //la tarea asincronica
        guardar.setOnClickListener {
            //Ahora aqui invocamos a la tarea asincronoca por medio de su cosntructorTarea
            TareaGuardarUsuario(applicationContext).execute()
        }



    }



}