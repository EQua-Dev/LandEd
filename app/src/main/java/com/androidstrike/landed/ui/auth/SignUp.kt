package com.androidstrike.landed.ui.auth

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
//import com.androidstrike.bias.model.User
import com.androidstrike.landed.utils.Common
import com.androidstrike.landed.utils.Common.userCollectionRef
import com.androidstrike.landed.utils.setOnSingleClickListener
import com.androidstrike.landed.utils.toast
import com.androidstrike.landed.R
import com.androidstrike.landed.model.User
import com.androidstrike.landed.utils.visible
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUp : Fragment(), AdapterView.OnItemSelectedListener  {

    private var emailAddress: String? = null
    lateinit var uName: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var occupation: String
    lateinit var etUsername: EditText
    lateinit var etUserEmail: EditText
    lateinit var etUserPassword: EditText
    lateinit var etUserConfirmPassword: EditText
    lateinit var pbLoading: ProgressBar

    var userOccupation: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.auto_complete_tv)
        val signUpTxtLogin = view.findViewById<TextView>(R.id.signup_txt_login)
        val signUpButtonRegister = view.findViewById<Button>(R.id.sign_up_btn_register)
        etUsername = view.findViewById(R.id.sign_up_user_name)
        etUserEmail = view.findViewById(R.id.sign_up_email)
        etUserPassword = view.findViewById(R.id.sign_up_password)
        etUserConfirmPassword = view.findViewById(R.id.sign_up_confirm_password)
        pbLoading = view.findViewById(R.id.pb_sign_up)


        val occupationArray = resources.getStringArray(R.array.occupation)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.drop_down_item,occupationArray)

        autoCompleteTextView.setAdapter(arrayAdapter)

        occupation = autoCompleteTextView.text.toString()

        signUpTxtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }

        signUpButtonRegister.setOnSingleClickListener {
            uName = etUsername.text.toString().trim()
            email = etUserEmail.text.toString().trim()
            password = etUserPassword.text.toString().trim()
            confirmPassword = etUserConfirmPassword.text.toString().trim()

            validateInput()

        }

        return view
    }

    private fun validateInput() {

        //if user name field is empty
        if (uName.isEmpty()) {
            text_input_layout_sign_up_user_name.error = "First Name Required"
            etUsername.requestFocus()
            return
        }
        // if the email and password fields are empty we display error messages
        if (email.isEmpty()) {
            text_input_layout_sign_up_email.error = "Email Required"
            etUserEmail.requestFocus()
            return
        }

        //if the email pattern/format does not does match that as defined, we display error messages
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            text_input_layout_sign_up_email.error = "Valid Email Required"
            etUserEmail.requestFocus()
            return
        }
        //if the password contains less than 6 characters we display error message
        if (password.isEmpty() || password.length < 6) {
            text_input_layout_sign_up_password.error = "6 char password required"
            etUserPassword.requestFocus()
            return
        }

        if (confirmPassword != password) {
            text_input_layout_sign_up_confirm_password.error = "Does Not Match Password"
            etUserConfirmPassword.requestFocus()
            return
        }
//        if (!Common.PASSWORD_PATTERN.matcher(password).matches()) {
//            sign_up_password.error =
//                "Password too weak. Must Contain at least one uppercase, one lowercase, one number and one character"
//            sign_up_password.requestFocus()
//            return
//        }
        else {
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        //  implement user sign up
        pbLoading.visible(true)
        Common.mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val newUserId = Common.mAuth.uid
                    val newUserOccupation = occupation
                    activity?.toast(newUserId.toString())
                    val date_joined = System.currentTimeMillis()
                    //saves user's details to the cloud db (firestore)
                    saveUser(email, newUserId, newUserOccupation, date_joined.toString())
//                    userId = Common.mAuth.currentUser?.uid
                    isFirstTime()
                    pbLoading.visible(false)
                    findNavController().navigate(R.id.action_signUp_to_signIn)
                } else {
                    it.exception?.message?.let {
                        pbLoading.visible(false)
                        activity?.toast(it)
                    }
                }
            }
    }

    private fun saveUser(
        email: String,
        newUserId: String?,
        newUserOccupation: String,
        dateJoined: String
    ) {
        val user = getUser(email, newUserId, newUserOccupation, dateJoined)
        Log.d("Equa", "saveUser: $newUserId")
        saveNewUser(user)
    }

    private fun saveNewUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            //create new user in firestore
            userCollectionRef.document(user.uid.toString()).set(user).await()
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                activity?.toast(e.message.toString())
            }
        }
    }

    private fun getUser(
        email: String,
        newUserId: String?,
        newUserOccupation: String,
        dateJoined: String
    ): User {
        val userName = etUsername.text.toString()
        val occupation = newUserOccupation
        val uid = newUserId.toString()
        val newEmail = email
        val dateJoined = dateJoined

        return User(userName,newEmail,occupation,uid,dateJoined)
    }

    //boolean shared pref to store whether user is using the app for the 1st time
    private fun isFirstTime() {
        val sharedPref =
            requireActivity().getSharedPreferences(Common.sharedPrefName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Common.firstTimeKey, false)
        editor.putString(Common.userNamekey, uName)
        editor.putString(Common.occupationKey, occupation)
        editor.apply()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}