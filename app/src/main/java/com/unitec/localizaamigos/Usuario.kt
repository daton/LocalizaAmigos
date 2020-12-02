package com.unitec.localizaamigos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Usuario( var nickname:String,
                    var nombre:String,
                    var paterno:String,
                    var email:String,
                    var coordenadas:ArrayList<Coordenada>   ) {

}