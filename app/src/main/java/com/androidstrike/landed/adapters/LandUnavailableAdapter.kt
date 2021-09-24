package com.androidstrike.landed.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.landed.R
import com.androidstrike.landed.utils.IRecyclerItemClickListener

class LandUnavailableAdapter(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    var txtLandName: TextView
    var txtLandLocation: TextView
    var txtLandArea: TextView
    var txtLandOwner: TextView

    lateinit var iRecyclerItemClickListener: IRecyclerItemClickListener
    fun setClick(iRecyclerItemClickListener: IRecyclerItemClickListener){
        this.iRecyclerItemClickListener = iRecyclerItemClickListener
    }

    init {
        txtLandName = itemView.findViewById(R.id.tv_land_unavailable_name) as TextView
        txtLandLocation = itemView.findViewById(R.id.tv_land_unavailable_location) as TextView
        txtLandArea = itemView.findViewById(R.id.tv_land_unavailable_area) as TextView
        txtLandOwner = itemView.findViewById(R.id.tv_land_unavailable_owner) as TextView

        itemView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        iRecyclerItemClickListener.onItemClickListener(v!!, adapterPosition)
    }
}