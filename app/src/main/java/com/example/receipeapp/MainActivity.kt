package com.example.receipeapp

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.receipeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var loginEmail:EditText
    private lateinit var loginPassword:EditText
    private lateinit var binding:ActivityMainBinding
    lateinit var receiver: AirplaneModeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //register broadcast receiver to detect receive notification from receiver
        receiver = AirplaneModeReceiver()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver,it)
        }

        //on Click of sign Up button will redirect to Registration Activity
        val signUpButton = findViewById<TextView>(R.id.signUp)
        signUpButton.setOnClickListener{
            //Explicit Intent to Registration Activity on click of Sign Up Button
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)   //to start new activity, in this case to start Registration Activity
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginEmail = findViewById<EditText>(R.id.inputEmailText)
        loginPassword = findViewById<EditText>(R.id.inputPassword)

        //on click of Login button and proper user input, user will be verified.
        //on successful authetication, user will be redirected to Meal Suggestion Activity
        loginButton.setOnClickListener{

            //New Additions
            val emailLogin = loginEmail.text.toString().trim()
            val password = loginPassword.text.toString().trim()

            if (emailLogin.isEmpty()){
                loginEmail.error = "Email ID is Required"
                return@setOnClickListener
            }
            if (password.isEmpty()){
                loginPassword.error = "Password is Required"
                return@setOnClickListener
            }


            //Authenticate user - Sign In for Existing user
            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin,password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        //on successful login, display toast to notify users that they have logged in.
                        Toast.makeText(this@MainActivity,"Logged In Successfully", Toast.LENGTH_SHORT).show()
                        //on successful login, explicit intent to meal suggestion activity
                        val intent = Intent(this,MealSuggestionActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        //if registration fails, show error msg
                        Toast.makeText(this@MainActivity,it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    //unregister broadcast receiver to stop receiving broadcasts and to avoid memory leaks
    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

}