package com.example.opsc7312_regularbirds

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val firstFragment=HomeFragment()
        //val secondFragment=SecondFragment()
        //val thirdFragment=ThirdFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> setCurrentFragment(firstFragment)
                //R.id.person -> setCurrentFragment(secondFragment)
                //R.id.settings -> setCurrentFragment(thirdFragment)
            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}