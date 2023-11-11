package com.iie.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Popup_hotspotdetailsFragment: BottomSheetDialogFragment() {
    var tempSelectedLocation: Locations? = null
    val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onStateChanged(bottomSheet: View, newState: Int) {

        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            // Do something for slide offset.
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //declarations
        val view =  inflater.inflate(R.layout.popup_hotspotdetails, container, false)

        var txtComName = view.findViewById<TextView>(R.id.txtComName)
        var txtSciname = view.findViewById<TextView>(R.id.txtSciName)
        var txtLocName = view.findViewById<TextView>(R.id.txtLocName)
        var txtLocation= view.findViewById<TextView>(R.id.txtLatLong)

        var btnGo = view.findViewById<Button>(R.id.btnGotoHotspot)

        btnGo.setOnClickListener {
            val intent = Intent(activity, BirdNavigationActivity::class.java)
            startActivity(intent)
        }
        tempSelectedLocation = BirdHotspots.obtainSelectedLocation()

        txtComName.setText(tempSelectedLocation?.comName ?: "Common Name")
        txtSciname.setText(tempSelectedLocation?.sciName ?: "sci name")
        txtLocName.setText(tempSelectedLocation?.locName ?: "loc name")
        txtLocation.setText(tempSelectedLocation?.lng.toString() +", "+tempSelectedLocation?.lat.toString() ?: "loc name")


        return view;
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }

}