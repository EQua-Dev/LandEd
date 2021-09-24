package com.androidstrike.landed.ui.landing.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.landed.R
import com.androidstrike.landed.adapters.LandAvailableAdapter
import com.androidstrike.landed.adapters.LandUnavailableAdapter
import com.androidstrike.landed.model.LandDetail
import com.androidstrike.landed.utils.IRecyclerItemClickListener
import com.androidstrike.landed.utils.toast
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_available.*
import kotlinx.android.synthetic.main.fragment_unavailable.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Unavailable : Fragment() {

    lateinit var unavailableLandAdapter: FirestoreRecyclerAdapter<LandDetail, LandUnavailableAdapter>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unavailable, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("EQuaLog", "onBindViewHolder: here000")

        getRealTimeUnavailableLands()


        val layoutManager = LinearLayoutManager(requireContext())
        rv_home_unavailable.layoutManager = layoutManager
        rv_home_unavailable.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
        Log.d("EQuaLog", "onBindViewHolder: here00")


    }

    private fun getRealTimeUnavailableLands() {
        Log.d("EQuaLog", "onBindViewHolder: here001")

        val fireStore = FirebaseFirestore.getInstance()
        val unAvailableLands = fireStore.collection("Land")
        val query = unAvailableLands.whereEqualTo("status", "unavailable")
        Log.d("EQuaLog", "onBindViewHolder: here001again")

        val options =
            FirestoreRecyclerOptions.Builder<LandDetail>().setQuery(query, LandDetail::class.java)
                .build()
        Log.d("EQuaLog", "onBindViewHolder: here001againoo")

        unavailableLandAdapter =
            object : FirestoreRecyclerAdapter<LandDetail, LandUnavailableAdapter>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): LandUnavailableAdapter {
                    Log.d("EQuaLog", "onBindViewHolder: here1")

                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.home_unavailable_item, parent, false)
                    return LandUnavailableAdapter(itemView)

                }

                override fun onBindViewHolder(
                    holder: LandUnavailableAdapter,
                    position: Int,
                    model: LandDetail
                ) {

                    val documentId: String = snapshots.getSnapshot(position).id

                    Log.d("EQuaLog", "onBindViewHolder: ${model.name}")
                    Log.d("EQuaLog", "onBindViewHolder: $documentId")

                    val landName = model.name
                    val landPrice = model.price
                    val landLocation = model.location
                    val landArea = model.area

                    val nameOfOwner = model.owner_name

                    val emailOfOwner = model.owner_email





                    holder.txtLandName.append(landName)
                    holder.txtLandArea.append(landArea)
                    holder.txtLandLocation.append(landLocation)
                    holder.txtLandOwner.append(nameOfOwner)



                    holder.setClick(object : IRecyclerItemClickListener {
                        override fun onItemClickListener(view: View, position: Int) {

                            val emailIntent = Intent(Intent.ACTION_SEND)

                            emailIntent.data = Uri.parse("mailto:")
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailOfOwner))
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LAND MGT APP: $landName")
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, $nameOfOwner")

                            emailIntent.type = "message/rfc822"

                            startActivity(Intent.createChooser(emailIntent, "Send Email via: "))

                            activity?.toast(emailOfOwner.toString())

                        }
                    })

                }

            }

        rv_home_unavailable.adapter = unavailableLandAdapter

    }

    override fun onStart() {
        super.onStart()
        unavailableLandAdapter.startListening()
    }

    override fun onResume() {
        super.onResume()
        unavailableLandAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        unavailableLandAdapter.stopListening()
    }


}