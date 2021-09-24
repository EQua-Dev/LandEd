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
import com.androidstrike.landed.adapters.URPPagerAdapter
import com.google.android.material.tabs.TabLayout

class URPlanning : Fragment() {

    lateinit var fm: FragmentManager
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_u_r_planning, container, false)

        tabLayout = view.findViewById(R.id.tab_urp_title)!!
        viewPager = view.findViewById(R.id.urp_view_pager)!!

        tabLayout.addTab(tabLayout.newTab().setText("Farming"))
        tabLayout.addTab(tabLayout.newTab().setText("Residential Building"))
        tabLayout.addTab(tabLayout.newTab().setText("Office Complex"))
        tabLayout.addTab(tabLayout.newTab().setText("Recreational Centre"))
        tabLayout.addTab(tabLayout.newTab().setText("Business Complex"))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = childFragmentManager?.let { URPPagerAdapter(activity, it,  tabLayout.tabCount) }
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