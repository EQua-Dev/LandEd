package com.androidstrike.landed.adapters

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.landed.R

class LandOwnedAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var txtLandName: TextView
    var txtLandLocation: TextView
    var txtLandPrice: TextView
    var txtLandPurchaseDate: TextView

    init {
        txtLandName = itemView.findViewById(R.id.item_profile_land_name) as TextView
        txtLandLocation = itemView.findViewById(R.id.item_profile_land_location) as TextView
        txtLandPrice = itemView.findViewById(R.id.item_profile_land_price) as TextView
        txtLandPurchaseDate = itemView.findViewById(R.id.item_profile_land_purchase_date) as TextView
    }

}