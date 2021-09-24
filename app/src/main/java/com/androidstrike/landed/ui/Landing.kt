package com.androidstrike.landed.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.androidstrike.landed.R
import com.androidstrike.landed.ui.landing.Home
import com.androidstrike.landed.ui.landing.Profile
import com.androidstrike.landed.ui.landing.URPlanning
import kotlinx.android.synthetic.main.activity_landing.*

class Landing : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        setSupportActionBar(toolbar)

        replaceFragment(Home(), "Home")

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


//        val navController = Navigation.findNavController(this, R.id.fragment_container)
//
//        val appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
//        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration)
//        NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)
//        nav_view.setupWithNavController(navController)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        nav_view.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){
                R.id.nav_home -> replaceFragment(Home(), it.title.toString())
                R.id.nav_profile -> replaceFragment(Profile(), it.title.toString())
                R.id.nav_urp -> replaceFragment(URPlanning(), it.title.toString())
            }
            true
        }


    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return Navigation.findNavController(this, R.id.fragment_container)
//            .navigateUp() || super.onSupportNavigateUp()
//    }

//        setHasOptionsMenu(true)

    private fun replaceFragment(fragment: Fragment, title: String){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
        drawer_layout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}