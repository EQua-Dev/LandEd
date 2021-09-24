package com.androidstrike.landed.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.androidstrike.landed.ui.landing.home.Available
import com.androidstrike.landed.ui.landing.home.Unavailable

class HomePagerAdapter(
    var context: FragmentActivity?,
    fm: FragmentManager,
    var totalTabs: Int): FragmentPagerAdapter(fm)
{
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{
                Available()
            }
            1 ->{
                Unavailable()
            }else -> getItem(position)
        }
    }


}