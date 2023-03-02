package com.example.receipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    private lateinit var mEmail: EditText
    private lateinit var mConfirmPassword: EditText
    private lateinit var mPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mEmail = findViewById<EditText>(R.id.inputEmail)
        mPassword = findViewById<EditText>(R.id.inputPasswordText)
        mConfirmPassword = findViewById<EditText>(R.id.inputConfirmPassword)

        //On click of next button and proper user input, user will be autheticated.
        val nextButton = findViewById<Button>(R.id.NextButton)
        nextButton.setOnClickListener{
            //Email and Password validation
            val email = mEmail.text.toString().trim()
            val cpw = mConfirmPassword.text.toString().trim()
            val pw = mPassword.text.toString().trim()

            //Ensuring email is not left empty
            if (email.isEmpty()){
                mEmail.error = "Email ID is Required"
                return@setOnClickListener
            }
            //Ensuring password is not left empty
            if (pw.isEmpty()){
                mPassword.error = "Password is Required"
                return@setOnClickListener
            }
            //Ensuring password and confirm password match
            if (cpw.isEmpty() || !cpw.equals(pw,false)){
                mConfirmPassword.error = "Password does NOT Match"
                return@setOnClickListener
            }

            //Authenticate user - Create New User
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pw)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        //once successfully created user, carry out explicit intent to start Personal Details Activity
                        val intent = Intent(this, PersonalDetailsActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        //if registration fails, show error message
                        Toast.makeText(this@RegistrationActivity,it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }

        }//end of clickListener
    }
}