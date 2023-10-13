package com.example.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HALF_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Popup_hotspotdetailsFragment: BottomSheetDialogFragment() {

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

        var btnGo = view.findViewById<Button>(R.id.btnGotoHotspot)

        btnGo.setOnClickListener {
            val intent = Intent(activity, LocationInput::class.java)
            startActivity(intent)
        }


        return view;
    }
    companion object {
        const val TAG = "ModalBottomSheet"
    }

}