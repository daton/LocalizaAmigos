package com.unitec.localizaamigos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Usuario(@JsonProperty("nickname") var nickname:String?,
                   @JsonProperty("nombre") var nombre:String?,
                   @JsonProperty("idAndroid") var idAndroid:String?,
                   @JsonProperty("paterno")var paterno:String?,
                   @JsonProperty("email")  var email:String?,
                   @JsonProperty("coordenadas")  var coordenadas:ArrayList<Coordenada>?   ) {

}