package com.androidstrike.landed.ui.landing.home

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.landed.R
import com.androidstrike.landed.adapters.LandAvailableAdapter
import com.androidstrike.landed.model.LandDetail
import com.androidstrike.landed.ui.Agent
import com.androidstrike.landed.ui.Flutterwave
import com.androidstrike.landed.utils.IRecyclerItemClickListener
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_available.*
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Available : Fragment() {

    var availableLandAdapter: FirestoreRecyclerAdapter<LandDetail, LandAvailableAdapter>? = null
    lateinit var agentKeySplit: String
    var documentID = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

//        if (container?.isEmpty() == true)
        return inflater.inflate(R.layout.fragment_available, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("EQuaLog", "onBindViewHolder: here000")


        getRealTimeAvailableLands()


        val layoutManager = LinearLayoutManager(requireContext())
        rv_home_available.layoutManager = layoutManager
        rv_home_available.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
        Log.d("EQuaLog", "onBindViewHolder: here00")


    }

    private fun getRealTimeAvailableLands() {
        Log.d("EQuaLog", "onBindViewHolder: here001")

        val fireStore = FirebaseFirestore.getInstance()
        val availableLands = fireStore.collection("Land")
        val query = availableLands.whereEqualTo("status", "available")
        Log.d("EQuaLog", "onBindViewHolder: here001again")

        val options =
            FirestoreRecyclerOptions.Builder<LandDetail>().setQuery(query, LandDetail::class.java)
                .build()
        Log.d("EQuaLog", "onBindViewHolder: here001againoo")

        availableLandAdapter =
            object : FirestoreRecyclerAdapter<LandDetail, LandAvailableAdapter>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): LandAvailableAdapter {
                    Log.d("EQuaLog", "onBindViewHolder: here1")

                    val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.home_available_item, parent, false)
                    return LandAvailableAdapter(itemView)

                }

                override fun onBindViewHolder(
                    holder: LandAvailableAdapter,
                    position: Int,
                    model: LandDetail
                ) {

                    var documentId: String? = snapshots.getSnapshot(position).id

                    Log.d("EQuaLog", "onBindViewHolder: ${model.name}")
                    Log.d("EQuaLog", "onBindViewHolder: $documentId")

                    val landName = model.name
                    val landPrice = model.price
                    val landLocation = model.location
                    val landArea = model.area

                    val nameOfAgent = model.agent
                    val agentKey = nameOfAgent?.split(" ".toRegex())
                    agentKeySplit = agentKey!!.get(1)

                    val locale: Locale = Locale("en", "NG")
                    val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)



                    holder.txtLandName.text = landName
                    holder.txtLandArea.text = landArea
                    holder.txtLandLocation.text = landLocation
                    holder.txtLandPrice.text =
                        StringBuilder("${fmt.format(landPrice)}m")//"$fmt $landPrice m"//StringBuilder(landPrice)
                    holder.txtLandAgent.text = nameOfAgent



                    holder.setClick(object : IRecyclerItemClickListener {
                        override fun onItemClickListener(view: View, position: Int) {

                            showPurchaseDialog(
                                landName!!,
                                landArea!!, landLocation!!, landPrice!!, nameOfAgent, documentId
                            )
                        }
                    })

                }

            }

        rv_home_available.adapter = availableLandAdapter

    }


    private fun showPurchaseDialog(
        landName: String,
        landArea: String,
        landLocation: String,
        landPrice: Int,
        nameOfAgent: String,
        documentId: String?
    ) {


        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.drawable.custom_dialog_design)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_available_land_detail)

        val customLandName = dialog.findViewById(R.id.custom_title_land_name) as TextView
        val customLandPrice = dialog.findViewById(R.id.custom_land_price) as TextView
        val customAgentName = dialog.findViewById(R.id.custom_agent_name) as TextView
        val customLandLocation = dialog.findViewById(R.id.custom_location_name) as TextView

        val locale: Locale = Locale("en", "NG")
        val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)

        customLandName.text = landName
        customAgentName.text = nameOfAgent
        customLandLocation.text = landLocation
        customLandPrice.text =
            StringBuilder("${fmt.format(landPrice)}m")//"$fmt $landPrice m"//StringBuilder(landPrice)


        val makePaymentBtn = dialog.findViewById(R.id.btn_pre_pay_confirm) as Button
        val contactAgentBtn = dialog.findViewById(R.id.btn_contact_agent) as Button
        val cancelBtn = dialog.findViewById(R.id.btn_cancel) as Button
        val directionBtn = dialog.findViewById(R.id.btn_go_to_location) as ImageButton


        directionBtn.setOnClickListener {

            val builder = Uri.Builder()
            builder.scheme("geo")
                .path("0,0")
                .query(landLocation)

            val addressUri = builder.build()

            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$landLocation") )//addressUri)

            if (mapIntent.resolveActivity(activity?.packageManager!!) !=null){
                startActivity(mapIntent)
            }

        }
        makePaymentBtn.setOnClickListener {
            dialog.dismiss()

            val flutterIntent = Intent(requireContext(), Flutterwave::class.java)

            flutterIntent.putExtra("landPrice", landPrice)
            flutterIntent.putExtra("landName", landName)
            flutterIntent.putExtra("landLocation", landLocation)
            flutterIntent.putExtra("documentId", documentId)
            startActivity(flutterIntent)
        }

        contactAgentBtn.setOnClickListener {
            dialog.dismiss()

            val intent = Intent(requireContext(), Agent::class.java)
            intent.putExtra("agentName", nameOfAgent)
            startActivity(intent)

        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    //
    override fun onStart() {
        super.onStart()
        Log.d("EQuaLog", "onStart: ")
        availableLandAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        Log.d("EQuaLog", "onResume: ")
        availableLandAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        Log.d("EQuaLog", "onStop: ")
        if (availableLandAdapter != null)
            availableLandAdapter!!.stopListening()
    }

}