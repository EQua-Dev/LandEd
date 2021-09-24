package com.androidstrike.landed.ui.landing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidstrike.landed.R
import com.androidstrike.landed.adapters.LandOwnedAdapter
import com.androidstrike.landed.model.LandDetail
import com.androidstrike.landed.model.User
import com.androidstrike.landed.utils.Common
import com.androidstrike.landed.utils.getDate
import com.androidstrike.landed.utils.toast
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_available.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*

class Profile : Fragment() {

    lateinit var ownedLandAdapter: FirestoreRecyclerAdapter<LandDetail, LandOwnedAdapter>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retrieveUserProfile()
        retrieveUserLands()

        val layoutManager = LinearLayoutManager(requireContext())
        profile_recycler.layoutManager = layoutManager
        profile_recycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )
    }

    private fun retrieveUserLands() {

        val fireStore = FirebaseFirestore.getInstance()
        val availableLands = fireStore.collection("Land")
        val query = availableLands.whereEqualTo("owner_uid", Common.userId)

        val options = FirestoreRecyclerOptions.Builder<LandDetail>().setQuery(query, LandDetail::class.java).build()

        ownedLandAdapter = object: FirestoreRecyclerAdapter<LandDetail, LandOwnedAdapter>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandOwnedAdapter {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_profile_lands_owned, parent, false)
                return LandOwnedAdapter(itemView)
            }

            override fun onBindViewHolder(
                holder: LandOwnedAdapter,
                position: Int,
                model: LandDetail
            ) {
                val landName = model.name
                val landPrice = model.price
                val landLocation = model.location
                val landPurchaseDate = model.purchase_time

                val dateLong: Long? = landPurchaseDate?.toLong()
                val dateNew = getDate(dateLong, "dd/MM/yyyy").toString()

                val locale: Locale = Locale("en", "NG")
                val fmt: NumberFormat = NumberFormat.getCurrencyInstance(locale)


                holder.txtLandName.text = landName
                holder.txtLandPrice.text = StringBuilder("${fmt.format(landPrice)}m")
                holder.txtLandLocation.text = landLocation
                holder.txtLandPurchaseDate.text = dateNew
            }

        }

        profile_recycler.adapter = ownedLandAdapter

    }

    private fun retrieveUserProfile() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = Common.userCollectionRef.whereEqualTo("uid", Common.userId).get().await()

            for (doc in querySnapshot){
                val userProfile = doc.toObject(User::class.java)
                withContext(Dispatchers.Main){
                    profile_name.text = userProfile.name
                    profile_email.text = userProfile.email
                    profile_date_joined.text = userProfile.dateJoined
                    profile_occupation.text = userProfile.occupation


                }
            }

        }catch (e: Exception){
            withContext(Dispatchers.Main){
                activity?.toast(e.message.toString())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("EQuaLog", "onStart: ")
        ownedLandAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        Log.d("EQuaLog", "onResume: ")
        ownedLandAdapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        Log.d("EQuaLog", "onStop: ")
        if (ownedLandAdapter != null)
            ownedLandAdapter!!.stopListening()
    }

}