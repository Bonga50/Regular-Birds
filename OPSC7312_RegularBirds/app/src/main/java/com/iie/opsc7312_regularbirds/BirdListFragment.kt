package com.iie.opsc7312_regularbirds

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class BirdListFragment : Fragment(), RVadapter_Observations.OnItemClickListener,RVadapter_Observations.OnItemLongClickListener {
    private lateinit var recyclerView: RecyclerView // Declare recyclerView as a class-level property
    private lateinit var recyclerViewAdapter: RVadapter_Observations
    private lateinit var BirdObservationList: List<BirdObservationModel> // Declare data as a class-level property


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_bird_list, container, false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout)

        BirdObservationList= BirdObservationHandler.userDataX

        recyclerView = view.findViewById(R.id.lvObservartions) // Initialize recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewAdapter = RVadapter_Observations(BirdObservationList)
        recyclerViewAdapter.itemClickListener = this
        recyclerViewAdapter.itemLongClickListener = this

        recyclerView.adapter = recyclerViewAdapter

        //button to add new observation
        val btnCreateObservation = view.findViewById<Button>(R.id.btnCreateObservation)
        val activity = activity as HomeActivity
        btnCreateObservation.setOnClickListener {
            var forthFragment = AddNewObservationFragment()

            activity.setCurrentFragment(forthFragment)
        }
        //refresh page
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
            swipeRefreshLayout.isRefreshing = false
        }
        return view
    }

    override fun OnItemClick(itemId: String) {
        val bottomSheet = Popup_ObservationDetails()
        BirdObservationHandler.setSelectedObservation(BirdObservationHandler.getObservationById(itemId)!!)
        bottomSheet.show(getChildFragmentManager(), "MyBottomSheet")
    }

    fun refresh() {
        // Clear your existing data
        //yourData.clear()

        // Load your data again
        loadData()
    }

    fun loadData() {

        lifecycleScope.launch {

            val job1 = async { BirdObservationHandler.getUserObservationsFromFireStore() }
            val job2 = async { BirdObservationHandler.getImagesFromFireStore() }

            try {
                // Await the completion of all jobs
                job1.await()
                job2.await()
            } catch (e: Exception) {
                // Handle the exception
            }
        }
        BirdObservationList= BirdObservationHandler.userDataX
        recyclerViewAdapter = RVadapter_Observations(BirdObservationList)
    }

    override fun OnItemLongClick(itemId: String) {
        //delete

        AlertDialog.Builder(requireContext())
            .setTitle("Delete Observation")
            .setMessage("Are you sure you want to delete this observation?")
            .setPositiveButton("Yes") { dialog, which ->
                lifecycleScope.launch {
                    // Call your delete function here
                    BirdObservationHandler.deleteUserObservationFromFireStore(itemId)
                }
            }
            .setNegativeButton("No", null)
            .show()

    }


}