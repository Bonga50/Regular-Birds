package com.example.opsc7312_regularbirds


import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.opsc7312_regularbirds.BirdObservationHandler.observations
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
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
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class HomeFragment : Fragment(){


    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var hotspotInterface: HotspotCollection
    private val apiKey = "6p4jn23qffqh"
    var annotationApi: AnnotationPlugin? =null
    private lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation"
    var markerList : ArrayList<PointAnnotationOptions> = ArrayList()
    var userMarkerList : ArrayList<PointAnnotationOptions> = ArrayList()
    var pointAnnotationManager: PointAnnotationManager?=null
    var userPointAnnotationManager: PointAnnotationManager?=null
    var location = mutableListOf<Locations>()
    var hotspotsList = mutableListOf<BirdObservationModel>()
    var latitude:Double=0.0;
    var longitude:Double=0.0;
    var bearing:Double=-1.0;
    private lateinit var mapView:MapView
    private lateinit var mapboxMap: MapboxMap
    private  val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private val decimalFormat = DecimalFormat("0.###", DecimalFormatSymbols.getInstance(Locale.US))


    val listener = OnCameraChangeListener { cameraChangedEventData ->
        // Do something when the camera position changes
    }
    // Get the user's location as coordinates
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        Log.d("bearing ", it.toString())
        if (latitude!=0.0&&longitude!=0.0){
            bearing=it;
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(Point.fromLngLat(longitude,latitude)).build())
        }
           // mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())

    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        Log.d("Location ", it.latitude().toString())
        if (  latitude!= decimalFormat.format(it.latitude()).toDouble() &&
            longitude!= decimalFormat.format(it.longitude()).toDouble()){

            latitude = decimalFormat.format(it.latitude()).toDouble()
            longitude = decimalFormat.format(it.longitude()).toDouble()


            hotspots(latitude,longitude)


            BirdHotspots.setUserOriginLocation(latitude,longitude)
        }

        mapView.gestures.focalPoint = mapView.getMapboxMap().pixelForCoordinate(it)

    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }
        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    //private val asyncInflater by lazy { AsyncLayoutInflater(requireContext()) }
    private var markerId = 0

    private var markerWidth = 0
    private var markerHeight = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        var btnRecenter = view.findViewById<ImageView>(R.id.btnRecenter)
        latitude=0.0;
        longitude=0.0;

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
        viewAnnotationManager = mapView.viewAnnotationManager
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
        annotationApi=mapView?.annotations
        annotationConfig = AnnotationConfig(
            layerId =layerIDD
        )
        pointAnnotationManager = annotationApi?.createPointAnnotationManager(annotationConfig)
        userPointAnnotationManager = annotationApi?.createPointAnnotationManager(annotationConfig)
        // Add the listener to the map
        //mapboxMap.addOnCameraChangeListener(listener)
        userHotspots()
        // Pass the user's location to camera

        mapView.location.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)

        btnRecenter.setOnClickListener{
            mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(Point.fromLngLat(longitude,latitude)).build())
        }

        return view
    }


    private fun onCameraTrackingDismissed() {

        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }

    fun hotspots(latitude: Double, longitude: Double) {
        BirdHotspots.clearLocations()
        val radius = BirdHotspots.getMaxDistance()
        hotspotsList = BirdObservationHandler.observations // Assuming this is a valid list of Locations

        BirdHotspots.resetIdCount()
        hotspotInterface.getHotspots(apiKey,latitude, longitude, radius)
            .enqueue(object : Callback<List<Locations>> {
                override fun onResponse(call: Call<List<Locations>>, response: Response<List<Locations>>) {
                     if (response.isSuccessful) {
                        val hotspots = response.body()
                        for (i in response.body()!!) {
                            i.obsvId = BirdHotspots.generateId()
                            BirdHotspots.addLocation(i)
                        }
                         location = BirdHotspots.locationsList
                         if (isAdded) {
                             createMarker()
                         }
                    }
                }

                override fun onFailure(call: Call<List<Locations>>, t: Throwable) {
                    // Handle API request failure
                }
            })
    }

    fun userHotspots() {
        //hotspotsList.clear() // Clear the existing hotspots
        hotspotsList = BirdObservationHandler.observations // Assuming this is a valid list of Locations

        if (isAdded) {
            //createMarkerUserObser()
        }
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
        // Remove your listeners here
        mapView.gestures.removeOnMoveListener(onMoveListener)
        mapView.location.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    fun clearAnotations(){
        markerList= ArrayList()
        userMarkerList= ArrayList()

        pointAnnotationManager?.deleteAll()
        userPointAnnotationManager?.deleteAll()

    }

    fun clearUserAnotations(){
        userMarkerList= ArrayList()

        userPointAnnotationManager?.deleteAll()
    }

    private fun createMarker(){
        //clearAnotations()
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
                annotation: PointAnnotation ->  onMarkerClick(annotation)
            true
        })

        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)
        val bitmapImage2 = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_red)

        for (i in location){
            var jsonObject = JSONObject();
            jsonObject.put("ID",i.obsvId.toString())
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(i.lng, i.lat))
                .withData(Gson().fromJson(jsonObject.toString(), JsonElement::class.java))
                .withIconImage(bitmapImage)

            markerList.add(pointAnnotationOptions)
        }
        for (i in hotspotsList){
            var jsonObject = JSONObject();
            jsonObject.put("ID",i.observationId.toString())
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(i.userLocationLongitude, i.userLocationLatitude))
                .withData(Gson().fromJson(jsonObject.toString(), JsonElement::class.java))
                .withIconImage(bitmapImage2)
            markerList.add(pointAnnotationOptions)
        }


        pointAnnotationManager?.create(markerList);

    }

    private fun createMarkerUserObser(){
        clearUserAnotations()
        /*userPointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
                //annotation: PointAnnotation ->  onMarkerClick(annotation)
            true
        })*/
        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_red)

        for (i in hotspotsList){
            var jsonObject = JSONObject();
            jsonObject.put("ID",i.observationId.toString())
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(i.userLocationLongitude, i.userLocationLatitude))
                .withData(Gson().fromJson(jsonObject.toString(), JsonElement::class.java))
                .withIconImage(bitmapImage)
            userMarkerList.add(pointAnnotationOptions)
        }
        userPointAnnotationManager?.create(userMarkerList);
    }

    fun onMarkerClick(marker: PointAnnotation) {
        var jsonelement:JsonElement? =marker.getData()
        val jsonObj = JSONObject(jsonelement.toString())
        val value: String = jsonObj.getString("ID")
//        AlertDialog.Builder(requireContext())
//            .setTitle("Marker clicked")
//            .setMessage("Cliked"+value)
//            .setPositiveButton("OK"){
//                dialog,whichButton->dialog.dismiss()
//            }.show()
        if(value.contains("Obsv")){
            val x = BirdObservationHandler.getObservationById(value)
            BirdObservationHandler.setSelectedObservation(x!!)
            BirdHotspots.setDistentOriginLocation(x!!.userLocationLatitude,x!!.userLocationLongitude)
            val bottomSheet = Popup_ObservationDetails()
            bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")
        }else {
            val bottomSheet = Popup_hotspotdetailsFragment()
            val y = BirdHotspots.getLocationByObsvId(value.toInt())
            BirdHotspots.setDistentOriginLocation(y!!.lat,y!!.lng)

            bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")
        }
        BirdHotspots.setUserOriginLocation(latitude,longitude)
    }



}