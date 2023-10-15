package com.example.opsc7312_regularbirds

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BirdListFragment : Fragment(), RVadapter_Observations.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView // Declare recyclerView as a class-level property
    private lateinit var recyclerViewAdapter: RVadapter_Observations
    private lateinit var BirdObservationList: List<BirdObservationModel> // Declare data as a class-level property


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_bird_list, container, false)
        BirdObservationList= BirdObservationHandler.observations
        recyclerView = view.findViewById(R.id.lvObservartions) // Initialize recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter = RVadapter_Observations(BirdObservationList)
        recyclerViewAdapter.itemClickListener = this

        recyclerView.adapter = recyclerViewAdapter

        //button to add new observation
        val btnCreateObservation = view.findViewById<Button>(R.id.btnCreateObservation)
        val activity = activity as HomeActivity
        btnCreateObservation.setOnClickListener {
            var forthFragment = AddNewObservationFragment()

            activity.setCurrentFragment(forthFragment)
        }
        return view
    }

    override fun OnItemClick(itemId: Int) {
        val bottomSheet = Popup_ObservationDetails()
        BirdObservationHandler.setSelectedObservation(BirdObservationHandler.getObservationById(itemId)!!)
        bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")
    }


}