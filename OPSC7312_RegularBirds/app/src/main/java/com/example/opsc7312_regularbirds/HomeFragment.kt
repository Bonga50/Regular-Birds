package com.example.opsc7312_regularbirds


import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.viewAnnotationOptions


class HomeFragment : Fragment(){

    val locations = listOf(
        Locations(-122.4194, 37.7749),  // San Francisco
        Locations(-122.4334, 37.7849),  // Western Addition
        Locations(-122.4033, 37.7949),  // South of Market
        Locations(-122.4234, 37.7649),  // Mission District
        Locations(-122.4134, 37.7549),  // Bernal Heights
        Locations(-122.4034, 37.7349),  // Excelsior
        Locations(-122.3934, 37.7249)   // Visitacion Valley
    )


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

        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
            // After the style is loaded, initialize the Location component.
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }
                    for (i in locations){
                        makeMapMarker(i)
                    }

                }
            }
        )
        // Add the listener to the map
        mapboxMap.addOnCameraChangeListener(listener)

        // Pass the user's location to camera
        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)

        return view
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

    fun makeMapMarker(locations: Locations){
        // Create an instance of the Annotation API and get the PointAnnotationManager.
        val annotationApi = mapView?.annotations
        val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView)
        // Convert the image resource to a Bitmap
        val icon = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
            // Define a geographic coordinate.
            .withPoint(Point.fromLngLat(locations.longitude, locations.latitude))
            // Specify the bitmap you assigned to the point annotation
            // The bitmap will be added to map style automatically.
            .withIconImage(icon)
        // Add the resulting pointAnnotation to the map.
        pointAnnotationManager?.create(pointAnnotationOptions)

        
    }



}



