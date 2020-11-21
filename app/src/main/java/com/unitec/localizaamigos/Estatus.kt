package com.unitec.localizaamigos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Estatus {
    var mensaje :String?=null
    var success:Boolean?=null

}