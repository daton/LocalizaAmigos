package com.unitec.localizaamigos

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.activity_busquedas.*
import kotlinx.android.synthetic.main.activity_main.*

class BusquedasActivity : AppCompatActivity() {

    //aqui declaramos un string donde vamos a usar un nombre para designar
    //un iconnito para nuestras ubicaciones, a estos se les llama marcadores
     val MARCADOR="iconito"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Invocamos el token
        Mapbox.getInstance(this,getString(R.string.miToken))

        setContentView(R.layout.activity_busquedas)
        //Aqui invocamos el mapita de la vista
        mapView2?.onCreate(savedInstanceState)
        mapView2?.getMapAsync {mapboxMap->
        mapboxMap .setStyle(Style.MAPBOX_STREETS){
             it.addImage(MARCADOR, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.mapbox_marker_icon_default))!!)
         //Creamos este simbolo y lo debemos de agregar usando la clase de mapbox Symbolmanager
             val symbolManager= SymbolManager(mapView2,mapboxMap, it)
            symbolManager.iconAllowOverlap=true
            symbolManager.iconIgnorePlacement=true
          //El ultimo paso e localizar este icono en un lugar del mapa es decir en su lat/Lng
            val simbolo=symbolManager.create(
                SymbolOptions()
                    .withLatLng(LatLng(19.43581,-99.07155))
                    .withIconImage(MARCADOR)
                    .withIconSize(2.0f)
            )


         }

        }

    }
    //Implementamos todos losmetodos de manejo de memoria, que ya habiamos puesto
    //en la MainActivity donde se muestra el mapa de localizacion
    override fun onStart() {
        super.onStart()
        mapView2.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView2.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView2.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView2.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView2.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView2.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView2.onLowMemory()
    }
}