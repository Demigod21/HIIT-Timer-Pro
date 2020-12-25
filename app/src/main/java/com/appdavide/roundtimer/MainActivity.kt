package com.appdavide.roundtimer

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.appdavide.roundtimer.ui.custom.CustomFragment
import com.appdavide.roundtimer.ui.saved.SavedFragment
import com.appdavide.roundtimer.ui.simple.SimpleFragment

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_saved -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, SavedFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_custom -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, CustomFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_simple -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,
                        SimpleFragment()
                    )
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.setSelectedItemId(R.id.navigation_simple);
    }
}
