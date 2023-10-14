package com.example.opsc7312_regularbirds


import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.lang.UCharacter
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.QueriedFeature
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.generated.rasterDemSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.extension.style.style
import com.mapbox.maps.extension.style.terrain.generated.terrain
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
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import org.json.JSONObject
import java.util.concurrent.CopyOnWriteArrayList


class HomeFragment : Fragment() {

    val locations = listOf(
        Locations(-122.4194, 37.7749),  // San Francisco
        Locations(-122.4334, 37.7849),  // Western Addition
        Locations(-122.4033, 37.7949),  // South of Market
        Locations(-122.4234, 37.7649),  // Mission District
        Locations(-122.4134, 37.7549),  // Bernal Heights
        Locations(-122.4034, 37.7349),  // Excelsior
        Locations(-122.3934, 37.7249)   // Visitacion Valley
    )
    var annotationApi: AnnotationPlugin? =null
    private lateinit var annotationConfig: AnnotationConfig
    var layerIDD = "map_annotation"
    var markerList : ArrayList<PointAnnotationOptions> = ArrayList()
    var pointAnnotationManager:PointAnnotationManager?=null

    private val pointList = CopyOnWriteArrayList<Feature>()
    private lateinit var mapView:MapView
    private lateinit var mapboxMap: MapboxMap
    private  val MY_PERMISSIONS_REQUEST_LOCATION = 99
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    val listener = OnCameraChangeListener { cameraChangedEventData ->
        // Do something when the camera position changes
    }
    // Get the user's location as coordinates
    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
       Log.d("Location ", it.latitude().toString())
        mapView.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
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

    private val asyncInflater by lazy { AsyncLayoutInflater(requireContext()) }

    private var markerId = 0

    private var markerWidth = 0
    private var markerHeight = 0

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
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)
        markerWidth = bitmap.width
        markerHeight = bitmap.height
        //getting the map
        mapView = view.findViewById(R.id.mapView);
        mapboxMap = mapView.getMapboxMap()
        viewAnnotationManager = mapView.viewAnnotationManager

        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS,
            // After the style is loaded, initialize the Location component.
            object : Style.OnStyleLoaded {
                override fun onStyleLoaded(style: Style) {
                    prepareStyle(Style.MAPBOX_STREETS, bitmap)
                    mapView.gestures.addOnMoveListener(onMoveListener)
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
        createMarker()
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
        // Remove your listeners here
        mapView.gestures.removeOnMoveListener(onMoveListener)
        mapView.location.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }


    private fun onCameraTrackingDismissed() {

        mapView.location
            .removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView.location
            .removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView.gestures.removeOnMoveListener(onMoveListener)
    }


    private fun prepareStyle(styleUri: String, bitmap: Bitmap) = style(styleUri) {
        +image(BLUE_ICON_ID) {
            bitmap(bitmap)
        }
        +geoJsonSource(SOURCE_ID) {
            featureCollection(FeatureCollection.fromFeatures(pointList))
        }
        if (styleUri == Style.SATELLITE_STREETS) {
            +rasterDemSource(TERRAIN_SOURCE) {
                url(TERRAIN_URL_TILE_RESOURCE)
            }
            +terrain(TERRAIN_SOURCE)
        }
        +symbolLayer(LAYER_ID, SOURCE_ID) {
            iconImage(BLUE_ICON_ID)
            iconAnchor(IconAnchor.BOTTOM)
            iconAllowOverlap(false)
        }
    }

    fun clearAnotations(){
        markerList= ArrayList()
        pointAnnotationManager?.deleteAll()
    }

    private fun createMarker(){
        clearAnotations()
        pointAnnotationManager?.addClickListener(OnPointAnnotationClickListener {
            annotation:PointAnnotation ->  onMarkerClick(annotation)
            true
        })
        val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.mapbox_marker_icon_blue)

        for (i in locations){
            var jsonObject = JSONObject();
            jsonObject.put("The clicked Location: ",i.latitude.toString() + "," + i.latitude.toString())
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(i.longitude, i.latitude))
                .withData(Gson().fromJson(jsonObject.toString(),JsonElement::class.java))
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

    private fun addMarkerAndReturnId(point: Point): String {
        val currentId = "${MARKER_ID_PREFIX}${(markerId++)}"
        pointList.add(Feature.fromGeometry(point, null, currentId))
        val featureCollection = FeatureCollection.fromFeatures(pointList)
        mapboxMap.getStyle { style ->
            style.getSourceAs<GeoJsonSource>(SOURCE_ID)?.featureCollection(featureCollection)
        }
        return currentId
    }

    private fun Float.dpToPx() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        requireContext().resources.displayMetrics
    )

    private companion object {
        const val BLUE_ICON_ID = "blue"
        const val SOURCE_ID = "source_id"
        const val LAYER_ID = "layer_id"
        const val TERRAIN_SOURCE = "TERRAIN_SOURCE"
        const val TERRAIN_URL_TILE_RESOURCE = "mapbox://mapbox.mapbox-terrain-dem-v1"
        const val MARKER_ID_PREFIX = "view_annotation_"
        const val SELECTED_ADD_COEF_DP: Float = 8f
        const val STARTUP_TEXT = "Long click on a map to add a marker and click on a marker to pop-up annotation."
    }


}



