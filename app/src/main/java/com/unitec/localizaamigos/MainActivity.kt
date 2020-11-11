package com.unitec.localizaamigos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.NotNull
import java.lang.Exception
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

   var  mapView: MapView?=null
    var mapBoxMap:MapboxMap?=null
    var permisosManager:PermissionsManager?=null
    val INTERAVALO_DEFECTO=1000L
    val TIEMPO_ESPERA=INTERAVALO_DEFECTO*5
    var localizacionEngine: LocationEngine?=null

    private var callback=LocalizacionActivity(this)

    //Implementamos esa clasesisita
    internal inner class LocalizacionActivity(activity:MainActivity):LocationEngineCallback<LocationEngineResult>{
        //En esta parte espere el objeto de tipo callback para acceder a la localizacion en el rango establecido
        private var activityWeak: WeakReference<MainActivity>?=null
        init {
            activityWeak= WeakReference(activity)
        }

        /******************************************************************************************************
         * Tercer bloque de código agregamos los siguiente métodos para
         */
        //Aqui en  el metodo  onSuccess , es donde constantemenete se esta checando la localizacion en el rango
        //que indicamos previamente en los atibutos.
        override fun onSuccess(result: LocationEngineResult?) {


            val activity: MainActivity? = activityWeak!!.get()
            if (activity != null) {
                val location = result?.lastLocation ?: return

                // Cada vez que se actualice con exito una nueva localización,  aparecera este Toast
                //Puedes eliminarlo si gustas para que no apareca cada rato y este mame y mame en la pantalla del usuario
                //
                Toast.makeText(
                    activity,
                    "Lat. actualizada:" + result.lastLocation!!.latitude.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                //La nueva posicion actualiada es la variable "pos"; los dobles signos de exclamacion en kotlin implican que este valor NO ES NULO
                // esta posicion es la que debe de irse agregando a un ArrayList en tu Perfil.
                //ES LA QUE NOS INTERESA PARA FINES DE MONITOREO O TRACKING DEL USUARIO!, HAY QUE PINCHE EMOCION!!
                var pos = LatLng()
                pos.latitude = result.lastLocation!!.latitude
                pos.longitude = result.lastLocation!!.longitude
// La variable posicion es la que va a  verse reflejada y atualizada en el mapa y lo trasladamos con el método:
                //animateCamera, pudes cambiar el zoom de la camara y el metodo "tilt", es para poner que tan inclinado se muestra tu mapita
                //para que simule que estas en 3D, es una mamada pero bueno!!, se ve chingon e impresiona a los no-ingenieros.
                val posicion = CameraPosition.Builder()
                    .target(pos)
                    .zoom(18.0)
                    .tilt(20.0)
                    .build()

                mapBoxMap?.animateCamera(CameraUpdateFactory.newCameraPosition(posicion), 500)

                // Pasamos la nueva localizacion actualizada  al mapbox cada que se cumpla el rango de tiempo
                if (activity.mapBoxMap != null && result.lastLocation != null) {
                    activity.mapBoxMap!!.getLocationComponent()
                        .forceLocationUpdate(result.lastLocation)
                }
            }
        }


    override fun onFailure(exception: Exception) {
        TODO("Not yet implemented")
    }
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //La instanccia de delara ANTES  DE TU setContentView
     // Mapbox.getInstance(this,"xxxxxx")
     Mapbox.getInstance(this, getString(R.string.miToken))


        mapView =findViewById(R.id.mapView) as MapView

        setContentView(R.layout.activity_main)
        //Paso 7 b.
        mapView?.onCreate(savedInstanceState)
        //Paso 7 c.
        mapView?.getMapAsync(this)

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

    override fun onMapReady(mapboxMap: MapboxMap) {
this.mapBoxMap=mapBoxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            Style.OnStyleLoaded {
                 @Override fun onStyleLoaded(@NotNull style:Style){
    habilitarLocalizacion(style)
                 }
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    fun habilitarLocalizacion(@NotNull styleCargado:Style){
     if(PermissionsManager.areLocationPermissionsGranted(this)){
        var localizacionComponente=mapBoxMap?.locationComponent
       var  locaActivacion=LocationComponentActivationOptions.builder(this,styleCargado)
           .useDefaultLocationEngine(false)
           .build()
         //
         localizacionComponente?.activateLocationComponent(locaActivacion)
         //
         localizacionComponente?.setLocationComponentEnabled(true)
         //
         localizacionComponente?.setCameraMode(CameraMode.TRACKING)
         //
         localizacionComponente?.setRenderMode(RenderMode.COMPASS)
         initLocalizacionEngine()

         }else{
         permisosManager= PermissionsManager(this)
         permisosManager?.requestLocationPermissions(this)
     }
    }
    @SuppressWarnings("MissingPermission")
    fun initLocalizacionEngine(){
      localizacionEngine=LocationEngineProvider.getBestLocationEngine(this)
     var  request=LocationEngineRequest.Builder(INTERAVALO_DEFECTO)
         .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
         .setMaxWaitTime(TIEMPO_ESPERA).build()
        localizacionEngine?.requestLocationUpdates(request, callback, mainLooper)
        localizacionEngine?.getLastLocation(callback)

    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(granted: Boolean) {
        TODO("Not yet implemented")
    }

}