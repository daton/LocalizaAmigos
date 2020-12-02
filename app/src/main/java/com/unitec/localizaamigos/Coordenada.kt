package com.unitec.localizaamigos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Coordenada(var lat:Double,
                      var lng:Double) {
}