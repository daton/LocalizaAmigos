package com.unitec.localizaamigos

data class Usuario( var nickname:String,
                    var nombre:String,
                    var paterno:String,
                    var email:String,
                    var coordenadas:ArrayList<Coordenada>   ) {

}