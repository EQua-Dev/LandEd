package com.androidstrike.landed.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.landed.R
import com.androidstrike.landed.utils.IRecyclerItemClickListener

class LandAvailableAdapter(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    var txtLandName: TextView
    var txtLandLocation: TextView
    var txtLandArea: TextView
    var txtLandPrice: TextView
    var txtLandAgent: TextView

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener
    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtLandName = itemView.findViewById(R.id.tv_land_name) as TextView
        txtLandLocation = itemView.findViewById(R.id.tv_land_location) as TextView
        txtLandArea = itemView.findViewById(R.id.tv_land_area) as TextView
        txtLandPrice = itemView.findViewById(R.id.tv_land_price) as TextView
        txtLandAgent = itemView.findViewById(R.id.tv_land_agent) as TextView

        itemView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}