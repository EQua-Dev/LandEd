package com.androidstrike.landed.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.androidstrike.landed.ui.auth.SignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

object Common {

    lateinit var currentUser: Any


    //local hashmap to store the course attendance value from the cloud db
    val attendanceMap: HashMap<String, Boolean?> = HashMap<String, Boolean?>()



    //values for shared preferences
    val sharedPrefName= "FirstUse"
    val firstTimeKey = "FirstTime"
    val userNamekey = "userName"
    val occupationKey = "userOccupation"

    var userOccupation: String? = ""


    var userName: String? = SignIn().userName
    var mAuth = FirebaseAuth.getInstance()
    var userId = mAuth.currentUser?.uid
    var userEmail = mAuth.currentUser?.email
    val userCollectionRef = Firebase.firestore.collection("Users")//.document("${Common.userId}")
    val LandCollectionRef = Firebase.firestore.collection("Land")//.document("${Common.userId}")




    //password regex validator
    val PASSWORD_PATTERN: Pattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +  //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +  //any letter
                "(?=.*[@#$%^&+!=])" +    //at least 1 special character
                "(?=\\S+$)" +  //no white spaces
                ".{4,}" +  //at least 4 characters
                "$"
    )

}