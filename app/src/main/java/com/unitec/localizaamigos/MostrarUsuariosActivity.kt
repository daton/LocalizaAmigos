package com.unitec.localizaamigos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.activity_mostrar_usuarios.*

class MostrarUsuariosActivity : AppCompatActivity() {

    private val ICONITO = "iconito"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Mapbox.getInstance(this, getString(R.string.miToken))
        setContentView(R.layout.activity_mostrar_usuarios)


        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
//Cargar datos aqui.
it.addImage(ICONITO, BitmapUtils.getBitmapFromDrawable(getResources().getDrawable(R.drawable.mapbox_marker_icon_default))!!);
                val symbolManager = SymbolManager(mapView, mapboxMap, it)

                symbolManager.iconAllowOverlap = true
                symbolManager.iconIgnorePlacement = true

// Add symbol at specified lat/lon
                val symbol = symbolManager.create(
                    SymbolOptions()
                        .withLatLng(LatLng(19.43581, -99.07155))
                        .withIconImage(ICONITO)
                        .withIconSize(2.0f)
                )

            }
        }


    }
    public override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    public override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }
}