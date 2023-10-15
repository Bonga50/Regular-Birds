package com.example.opsc7312_regularbirds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AreaDetailsFragment : Fragment(), RVadapter_NearByHotspots.OnItemClickListener{

    private lateinit var recyclerView: RecyclerView // Declare recyclerView as a class-level property
    private lateinit var recyclerViewAdapter: RVadapter_NearByHotspots
    private lateinit var BirdHotspotList: List<Locations> // Declare data as a class-level property

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_area_details, container, false)

        BirdHotspotList= BirdHotspots.locationsList
        recyclerView = view.findViewById(R.id.lvObservartions) // Initialize recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter = RVadapter_NearByHotspots(BirdHotspotList)
        recyclerViewAdapter.itemClickListener = this

        recyclerView.adapter = recyclerViewAdapter

        return view
    }

    override fun OnItemClick(itemId: Int) {
        val bottomSheet = Popup_hotspotdetailsFragment()
        BirdHotspots.setSelectedHotspot(itemId)
        bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")
        BirdHotspots.setUserOriginLocation(BirdHotspots.userLatitude,BirdHotspots.userLongitude)
    }


}