package com.unitec.localizaamigos

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServicioUsuario {
    //VAMOS A SEGUIR LA RUTA DE SERVICIOS ESTILO REST

    //Primero , siguiendo el estilo REST  usamos el metodo POST
    //EL METODO POST "SIEMPRE" SE USA PARA "GUARDAR"
    //A la siguiente anotacion se le conoce como construiccion de una API REST
    @POST("api/usuario")
    fun guardar(@Body usuario:Usuario): Call<Estatus>

    //El siguiente método sirve para buscar todos los usuarios que ya guardste
    @GET("api/usuario")
    fun buscarTodos():List<Usuario>
}