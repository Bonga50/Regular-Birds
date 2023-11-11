package com.iie.opsc7312_regularbirds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mapbox.android.core.permissions.PermissionsManager
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var permissionManager: PermissionsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        var tempUserSettings :settingsModel? = settingsModel("","",10)
        lifecycleScope.launch {
            BirdObservationHandler.getUserObservationsFromFireStore()
            BirdObservationHandler.getImagesFromFireStore()
            tempUserSettings = UserHandler.getSettingsFromFirebse()
            if (tempUserSettings == null){
                UserHandler.addsettingstoFireBase( settingsModel(UserHandler.getVerifiedUser()!!,"Metric",10))
            }
            bottomNavigationView = findViewById(R.id.bottomNavigationView)

            val firstFragment = HomeFragment()
            val secondFragment = BirdListFragment()
            val thirdFragment = SettingsFragment()
            val forthFragment = AddNewObservationFragment()
            val fifthFragment = AreaDetailsFragment()
            //val thirdFragment=ThirdFragment()
            setCurrentFragment(firstFragment)

            //Home page
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_home -> setCurrentFragment(firstFragment)
                    R.id.nav_Birds -> setCurrentFragment(secondFragment)
                    R.id.nav_Settings -> setCurrentFragment(thirdFragment)
                    R.id.nav_addNew -> setCurrentFragment(forthFragment)
                    R.id.nav_NearMe -> setCurrentFragment(fifthFragment)
                }
                true
            }

            val modalBottomSheet = Popup_hotspotdetailsFragment()
            //modalBottomSheet.show(supportFragmentManager, Popup_hotspotdetailsFragment.TAG)
        }
    }
         fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    fun requestLocationPermissions(fragment: Fragment) {
        permissionManager?.requestLocationPermissions(fragment.requireActivity())
    }
}