package com.unitec.localizaamigos

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mapboxMap: MapboxMap

    private val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

    private var locationEngine: LocationEngine? = null



    private val callback: LocationChangeListeningActivityLocationCallback =
        LocationChangeListeningActivityLocationCallback(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Mapbox.getInstance(this, getString(R.string.miToken))


        setContentView(R.layout.activity_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap
        mapboxMap.setStyle(Style.OUTDOORS) {

            // Habilitamos la localizacion
            enableLocationComponent(it)
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Checams si los permisos de localizacion son otorgados
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Personalizacion de opciones de localizacion
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.colorAccent))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(
                this,
                loadedMapStyle
            )
                .locationComponentOptions(customLocationComponentOptions)
                .build()

            // Obtenemos una instancia del a componente de localizacion
            mapboxMap.locationComponent.apply {

                // Activamos la localizacin con opciones
                activateLocationComponent(locationComponentActivationOptions)

                // Habilitamos si la localizacionesta en true
                isLocationComponentEnabled = true

                // ponemos el tracking activado
                cameraMode = CameraMode.TRACKING

                // ajustamos la brujula como visible
                renderMode = RenderMode.COMPASS

                //El nuevo
                initLocationEngine();
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        val request = LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()
        locationEngine?.requestLocationUpdates(request, callback, mainLooper)
        locationEngine?.getLastLocation(callback)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, "Necesitas tu localizacion", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, "No conediste el permiso de localozacion", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    inner  class LocationChangeListeningActivityLocationCallback internal constructor(activity: MainActivity?):
        LocationEngineCallback<LocationEngineResult> {
        private val activityWeakReference: WeakReference<MainActivity>


        override fun onSuccess(result: LocationEngineResult){
            val activity:MainActivity=activityWeakReference.get()!!
            if(activity!=null){
                val location=result.lastLocation?:return


                Toast.makeText(
                    activity,
                    "${result.lastLocation!!.latitude.toString()},${result.lastLocation!!.longitude.toString()},${result.lastLocation!!.altitude.toString()}",
                    Toast.LENGTH_SHORT
                ).show()


                //En la siguiente clase vamos a poner en eta seccion una corutina con la cual
                //estaremos enviando cada segundo o cuando tenga conexion
                //la ultima localizacion actualziada.


//PassthenewlocationtotheMapsSDK'sLocationComponent
                if(activity.mapboxMap!=null&&result.lastLocation!=null){
                    activity.mapboxMap.getLocationComponent()
                        .forceLocationUpdate(result.lastLocation)
                }
            }
        }

        /**
         *TheLocationEngineCallbackinterface'smethodwhichfireswhenthedevice'slocationcan'tbecaptured
         *
         *@paramexceptiontheexceptionmessage
         */
        override fun onFailure(exception: Exception){
           var activity :MainActivity=activityWeakReference.get()!!
            if(activity!=null){
                Toast.makeText(
                    activity, exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        init{
            activityWeakReference=WeakReference(activity)
        }
    }


}

