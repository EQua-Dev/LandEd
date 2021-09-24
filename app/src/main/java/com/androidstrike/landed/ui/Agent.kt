package com.androidstrike.landed.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.androidstrike.landed.R
import com.androidstrike.landed.model.AgentModel
import com.androidstrike.landed.utils.toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_agent.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Agent : AppCompatActivity() {

    lateinit var agentName: String
    private val agentCollectionRef = Firebase.firestore.collection("Agents")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent)


        agentName = intent.getStringExtra("agentName")!!
        val actionBar = getSupportActionBar()

        if(actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);


        }

        actionBar?.title = "$agentName"


        retrieveAgentDetail()
    }

    private fun retrieveAgentDetail() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = agentCollectionRef
                .whereEqualTo("name", agentName).get().await()

            for (document in querySnapshot){
                val agentDetail = document.toObject(AgentModel::class.java)
                withContext(Dispatchers.Main){

                    val nameOfAgent = agentDetail.name
                    val emailOfAgent = agentDetail.email
                    val phoneOfAgent = agentDetail.phone


                    agent_name.text = agentDetail.name
                    agent_email.text = agentDetail.email
                    agent_phone.text = agentDetail.phone
                    agent_firm.text = agentDetail.firm
                    agent_xp.text = "${agentDetail.xp} XP"

                    setClicks(nameOfAgent,emailOfAgent,phoneOfAgent)
                }
            }

        }catch (e: Exception){
            withContext(Dispatchers.Main){
                toast(e.message.toString())
            }
        }
    }

    private fun setClicks(nameOfAgent: String, emailOfAgent: String, phoneOfAgent: String) {

        agent_phone.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.fromParts("tel",phoneOfAgent,null)
            startActivity(dialIntent)
        }

        agent_email.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND)

            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailOfAgent))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LAND MGT APP")
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, $nameOfAgent")

            emailIntent.type = "message/rfc822"

            startActivity(Intent.createChooser(emailIntent, "Send Email via: "))
        }

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}