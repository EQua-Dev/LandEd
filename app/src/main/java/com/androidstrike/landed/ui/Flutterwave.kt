package com.androidstrike.landed.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.androidstrike.landed.R
import com.androidstrike.landed.ui.landing.Profile
import com.androidstrike.landed.utils.Common
import com.androidstrike.landed.utils.toast
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class Flutterwave : AppCompatActivity() {

    /**
     * Test Mastercard details for mock
    Card number: 5531 8866 5214 2950
    cvv: 564
    Expiry: 09/32
    Pin: 3310
    OTP: 12345
     **/

    var landPrice by Delegates.notNull<Double>()
    var landPriceInt by Delegates.notNull<Int>()
    lateinit var landName: String
    lateinit var landLocation: String
    lateinit var documentId: String

    //    lateinit var ref: String
    val publicKey = "FLWPUBK-2d6ccddd437ac5c89668bb3116f33ed5-X"
    val encrypyKey = "884f53bff48591fe29facb22"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutterwave)


        landPrice = intent.getDoubleExtra("landPrice", 0.0)
        landName = intent.getStringExtra("landName")!!
        landLocation = intent.getStringExtra("landLocation")!!
        documentId = intent.getStringExtra("documentId")!!


        makePayment()

    }

    private fun makePayment() {
        RaveUiManager(this)
            .setAmount(landPrice!!)
            .setEmail(Common.userEmail)
            .setCountry("NG")
            .setCurrency("NGN").setfName(Common.userName).setlName(Common.userName)
            .setNarration("Payment For $landName, $landLocation")
            .setPublicKey(publicKey)
            .setEncryptionKey(encrypyKey)
            .setTxRef("${System.currentTimeMillis().toString()}$landName")
            .acceptAccountPayments(true)
            .acceptCardPayments(true)
            //.isPreAuth(true) this is used if you want to charge the customer but not withdraw it immediately, eg uber
            //.setSubAccounts() takes in list as parameter, it is used if you want to pay the charged fee into 2 accounts
            //.setPaymentPlan() used for subscription basis
            .onStagingEnv(true)
            .shouldDisplayFee(true)
            .showStagingLabel(true)
            .initialize()

    }

    var ref = RaveUiManager(this).txRef

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message: String = data.getStringExtra("response")
                .toString() //returns the entire raw data of the transaction
            Log.d("EQUA", "onActivityResult: $message")
            if (resultCode === RavePayActivity.RESULT_SUCCESS) {

                Log.d("EQUA", "onActivityResult: $ref")

                updatePurchase()

                val intent = Intent(this, Landing::class.java)
                startActivity(intent)



            } else if (resultCode === RavePayActivity.RESULT_ERROR) {
                toast("ERROR $message")

            } else if (resultCode === RavePayActivity.RESULT_CANCELLED) {
                toast("CANCELLED $message")

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun updatePurchase() = CoroutineScope(Dispatchers.IO).launch {

        val landQuery = Common.LandCollectionRef.whereEqualTo("name", landName).get().await()
        if (landQuery.documents.isNotEmpty()) {
            for (document in landQuery) {
                try {

                    Common.LandCollectionRef.document(documentId!!).update("status", "unavailable")
                    Common.LandCollectionRef.document(documentId!!)
                        .update("owner_name", "${Common.userName}")
                    Common.LandCollectionRef.document(documentId!!)
                        .update("owner_email", "${Common.userEmail}")
                    Common.LandCollectionRef.document(documentId!!)
                        .update("purchase_time", "${System.currentTimeMillis()}")
                    Common.LandCollectionRef.document(documentId!!)
                        .update("owner_uid", "${Common.userId}")

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        toast(e.message.toString())
                    }
                }
            }

        }
    }

}