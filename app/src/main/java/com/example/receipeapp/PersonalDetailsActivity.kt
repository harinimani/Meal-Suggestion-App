package com.example.receipeapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var userName: TextInputEditText
    var genderString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details)

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //Invoke calendar view when user clicks on date of birth text to select their DOB.
        val dobButton = findViewById<TextView>(R.id.editDOB)
        dobButton.setOnClickListener{
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDay ->
                    dobButton.setText(""+mDay+"/"+(mMonth+1)+"/"+mYear)
                }, year, month, day)
            //show dialog
            dpd.show()
        }

        userName = findViewById(R.id.inputName)
        val male = findViewById<com.google.android.material.checkbox.MaterialCheckBox>(R.id.maleCheckBox)
        val female = findViewById<com.google.android.material.checkbox.MaterialCheckBox>(R.id.femaleCheckBox)

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener{

            //getting current user ID
            val currentUser = FirebaseAuth.getInstance().currentUser
            //current user ID stored into variable uid
            val uid = currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            val name = userName.text.toString().trim()

            val user:MutableMap<String, Any> = HashMap()
            user["name"] = name

            //only male checked
            if (male.isChecked && !female.isChecked){
                user["gender"] = "male"
                genderString = "male"
            }
            //only female checked
            else if (female.isChecked && !male.isChecked){
                user["gender"] = "female"
                genderString = "female"
            }
            //if none or both male and female are checked
            else{
                male.error = "Select a Gender (M/F)"
                female.error = "Select a Gender (M/F)"
                Toast.makeText(this, "Select Gender (M/F)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //store user information to the users collection in firebase.
            //sets the docID to the user ID.
            db.collection("users").document(uid.toString())
                .set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Loaded to Database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }

            //Explicit Intent to DietScreen Activity.
            val intent = Intent(this,DietScreen::class.java)
            //Sending additional information, user's gender data, to the next activity.
            intent.putExtra("gender",genderString)
            startActivity(intent)

        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }

    }
}