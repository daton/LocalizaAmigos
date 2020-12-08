package com.unitec.localizaamigos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coordenada(@JsonProperty("lat") var lat:Double?,
                      @JsonProperty("lng")    var lng:Double?) {

}