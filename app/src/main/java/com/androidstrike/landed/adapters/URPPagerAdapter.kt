package com.androidstrike.landed.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.androidstrike.landed.ui.landing.home.Available
import com.androidstrike.landed.ui.landing.home.Unavailable
import com.androidstrike.landed.ui.landing.urplanning.*

class URPPagerAdapter(
    var context: FragmentActivity?,
    fm: FragmentManager,
    var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Farming()
            }
            1 -> {
                Residential()
            }
            2 -> {
                Office()
            }
            3 -> {
                Recreational()
            }
            4 -> {
                Business()
            }
            else -> {
                getItem(position)
            }
        }
    }


}