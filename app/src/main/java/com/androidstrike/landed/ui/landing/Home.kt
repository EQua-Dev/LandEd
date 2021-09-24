package com.androidstrike.landed.ui.landing

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.androidstrike.landed.R
import com.androidstrike.landed.adapters.HomePagerAdapter
import com.google.android.material.tabs.TabLayout

class Home : Fragment() {

    lateinit var fm: FragmentManager
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        tabLayout = view.findViewById(R.id.tab_title)!!
        viewPager = view.findViewById(R.id.view_pager)!!

        tabLayout.addTab(tabLayout.newTab().setText("Available"))
        tabLayout.addTab(tabLayout.newTab().setText("Unavailable"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = childFragmentManager?.let { HomePagerAdapter(activity, it,  tabLayout.tabCount) }
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                tabLayout.setSelectedTabIndicatorColor(Color.WHITE)
                tabLayout.setTabTextColors(Color.BLACK, Color.WHITE)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabLayout.setTabTextColors(Color.BLACK, Color.WHITE)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        return view
    }

}