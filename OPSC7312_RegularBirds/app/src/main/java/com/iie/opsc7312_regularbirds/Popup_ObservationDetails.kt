package com.iie.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class Popup_ObservationDetails: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //declarations
        var entryImages: entryImages?
        val view = inflater.inflate(R.layout.popup_observationdetails, container, false)
        var lblTitle = view.findViewById<TextView>(R.id.lblObservationTitle)
        var lblName = view.findViewById<TextView>(R.id.lblObservationBirdName)
        var lblDate = view.findViewById<TextView>(R.id.lblObservationDate)
        var lblLocationLat = view.findViewById<TextView>(R.id.lblObservationLatitude)
        var lblLocationLng = view.findViewById<TextView>(R.id.lblObservationLongitude)
        var imgViewBird = view.findViewById<ImageView>(R.id.birdImage)



        var btnGo = view.findViewById<Button>(R.id.btnGotoHotspot)

        btnGo.setOnClickListener {
            val intent = Intent(activity, BirdNavigationActivity::class.java)
            startActivity(intent)
        }

        var tempSelectedObservation = BirdObservationHandler.getSelectedObservation()


        if ( tempSelectedObservation!!.imageData!="" ){
            Log.d("Observation id ",tempSelectedObservation!!.toString())
            entryImages = BirdObservationHandler.getEntryImage(tempSelectedObservation!!.observationId)
            if(entryImages == null){entryImages = entryImages("","","")}
        } else{
            entryImages = entryImages("","","")
        }
        lblTitle.setText("Observation " + tempSelectedObservation!!.observationId.toString())
        lblName.setText("Bird name: " + tempSelectedObservation.observationName)
        lblDate.setText("Date: " + tempSelectedObservation.observationDate)
        lblLocationLat.setText("Latitude: " + tempSelectedObservation.userLocationLatitude.toString())
        lblLocationLng.setText("Longitude: " + tempSelectedObservation.userLocationLongitude.toString())


        if (! entryImages.ImageUrl.equals("")    ){
            val imageurl = tempSelectedObservation.imageData.toString()
            Picasso.get().load(entryImages.ImageUrl).into(imgViewBird)
        }else{
            imgViewBird.setImageResource(R.drawable.red_icon)
        }
        return view
    }
}
