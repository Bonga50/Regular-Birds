package com.example.opsc7312_regularbirds


import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(){

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = object : Runnable {
        override fun run() {
            mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
            handler.postDelayed(this, 30000) // Run every 30 seconds
        }
    }
    private lateinit var hotspotInterface: HotspotCollection
    private val apiKey = "6p4jn23qffqh"
    var annotationApi: AnnotationPlugin? =null
    private lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation"
    var markerList : ArrayList<PointAnnotationOptions> = ArrayList()
    var pointAnnotationManager: PointAnnotationManager?=null
    var location = mutableListOf<Locations>()
    private lateinit var mapView:MapView
    private lateinit var mapboxMap: MapboxMap
    private  val MY_PERMISSIONS_REQUEST_LOCATION = 99

    val listener = OnCameraChangeListener { cameraChangedEventData ->
        // Do something when the camera position changes
    }
    // Get the user's location as coordinates
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        Log.d("Location ", it.latitude().toString())
        hotspots(it.latitude(),it.longitude())
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Check for location permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request for permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
        //getting the map
        mapView = view.findViewById(R.id.mapView);
        mapboxMap = mapView.getMapboxMap()

        hotspotInterface = Hotspots.createEBirdService()

        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
            // After the style is loaded, initialize the Location component.
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }


                }
            }
        )
        // Add the listener to the map
        mapboxMap.addOnCameraChangeListener(listener)

        // Pass the user's location to camera
        handler.post(runnable)
        //mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        //mapView.location.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        return view
    }

    fun hotspots(latitude: Double, longitude: Double) {
        val radius = 25 // 10 kilometers

        hotspotInterface.getHotspots(apiKey,latitude, longitude, radius)
            .enqueue(object : Callback<List<Locations>> {
                override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>) {
                     if (response.isSuccessful) {
                        val hotspots = response.body()
                        for (i in response.body()!!) {
                            location.add(i)
                        }

                         createMarker()
                    }
                }

                override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                    // Handle API request failure
                }
            })
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


    override fun onDestroy() {
        super.onDestroy()
        mapboxMap.removeOnCameraChangeListener(listener)
    }

    override fun onResume() {
        super.onResume()
        handler.post(runnable) // Start updates when fragment is resumed
        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable) // Stop updates when fragment is paused
        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
    }
    fun makeMapMarker(locations: Locations){
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        val annotationApi = mapView?.annotations
        val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView)
        // Convert the image resource to a Bitmap
        val icon = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(locations.lng, locations.lat))
            // Specify the bitmap you assigned to the point annotation
            // The bitmap will be added to map style automatically.
            .withIconImage(icon)
        // Add the resulting pointAnnotation to the map.
        pointAnnotationManager?.create(pointAnnotationOptions)
    }
    fun clearAnotations(){
        markerList= ArrayList()
        pointAnnotationManager?.deleteAll()
    }

    private fun createMarker(){
        clearAnotations()
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
                annotation: PointAnnotation ->  onMarkerClick(annotation)
            true
        })
        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)

        for (i in location){
            var jsonObject = JSONObject();
            jsonObject.put("The clicked Location: ",i.lng.toString() + "," + i.lat.toString())
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(i.lng, i.lat))
                .withData(Gson().fromJson(jsonObject.toString(), JsonElement::class.java))
                .withIconImage(bitmapImage)

            markerList.add(pointAnnotationOptions)
        }
        pointAnnotationManager?.create(markerList);

    }
    fun onMarkerClick(marker: PointAnnotation) {
        var jsonelement:JsonElement? =marker.getData()
//        AlertDialog.Builder(requireContext())
//            .setTitle("Marker clicked")
//            .setMessage("Cliked"+jsonelement.toString())
//            .setPositiveButton("OK"){
//                dialog,whichButton->dialog.dismiss()
//            }.show()
        val bottomSheet = Popup_hotspotdetailsFragment()

        bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")

    }



}



