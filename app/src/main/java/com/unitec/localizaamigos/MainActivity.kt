package com.unitec.localizaamigos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //La instanccia de delara ANTES  DE TU setContentView
     // Mapbox.getInstance(this,"xxxxxx")
     Mapbox.getInstance(this, getString(R.string.miToken))

        setContentView(R.layout.activity_main)
        //Paso 7 b.
        mapView?.onCreate(savedInstanceState)
        //Paso 7 c.
        mapView?.getMapAsync { mapboxMap->
            mapboxMap.setStyle(Style.MAPBOX_STREETS){
                //Aqui puedes estilizar aun mas tu mapita
            }
        }
    }
//Paso 7 d.
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        //Invocamos si es que se guardo, una instancia del ultimo mapa
        mapView?.onSaveInstanceState(outState)

    }

    override fun onDestroy() {
        super.onDestroy()
        //Destruimos el mapa cuando ya no se este en la app
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //para baja memoria
        mapView?.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        //Para pausar la activad del mapa en lo que haces otra cosa, por ejemplo contestar una llamada
        mapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

}