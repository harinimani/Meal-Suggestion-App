package com.example.receipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DietScreen : AppCompatActivity() {

    private lateinit var userHeight : TextInputEditText
    private lateinit var userWeight : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diet_screen)

        userHeight = findViewById(R.id.inputHeight)
        userWeight = findViewById(R.id.inputWeight)

        // allow positive and negative decimal or integer numbers only
        userHeight.inputNumbersOnly()
        userWeight.inputNumbersOnly()


        val gender = getIntent().getStringExtra("gender")


        val intoleranceSpinner: AutoCompleteTextView = findViewById(R.id.foodIntolerance)
        val allergiesSpinner: AutoCompleteTextView = findViewById(R.id.foodAllergy)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.foodIntolerance, android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            intoleranceSpinner.setAdapter(adapter)
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.foodAllergies, android.R.layout.simple_spinner_item).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            allergiesSpinner.setAdapter(adapter)
        }

        val nextButton = findViewById<Button>(R.id.doneButton)


        nextButton.setOnClickListener{

            //validation for Weight and Height input
            val weightinput = findViewById<EditText>(R.id.inputWeight)
            val heightinput = findViewById<EditText>(R.id.inputHeight)

            val mWeight = weightinput.text.toString().trim()
            val mHeight = heightinput.text.toString().trim()

            //Ensuring Weight is not left empty
            if (mWeight.isEmpty()){
                weightinput.error = "Weight is Required"
                return@setOnClickListener
            }
            //Ensuring Height is not left empty
            if (mHeight.isEmpty()){
                heightinput.error = "Height is Required"
                return@setOnClickListener
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid
            val db = FirebaseFirestore.getInstance()
            val height = userHeight.text.toString().trim()
            val weight = userWeight.text.toString().trim()
            val bmi =  weight.toFloat() / (height.toFloat() * height.toFloat())

            var bmiCategory = ""

            var calorieLimit = 1200

            //calculation for user's calorie limit
            if (gender.equals("Male",true)){
                calorieLimit = 2000
            }
            else if (gender.equals("Female",true)){
                calorieLimit = 1800
            }

            //calculation for categorizing the weight category user falls in
            if (bmi < 18.5){
                bmiCategory = "Underweight"
                calorieLimit = calorieLimit + 500
            }
            else if (bmi >= 18.5 && bmi<25){
                bmiCategory = "Normal"
            }
            else if (bmi >= 25 && bmi < 30){
                bmiCategory = "Overweight"
                calorieLimit = calorieLimit - 500
            }
            else{
                bmiCategory = "Obese"
                calorieLimit = calorieLimit - 500
            }

            val bmiStr =  bmi.toString()

            //user information stored in HashMap
            //create HashMap() of type "<String, any>"
            val user:MutableMap<String, Any> = HashMap()
            //adding elements to the HashMap
            user["weight"] = weight
            user["height"] = height
            user["bmi"] = bmiStr
            user["bmiCategory"] = bmiCategory
            user["calorieLimit"] = calorieLimit.toString()

            //storing user information into Firestore, into 'users' collection
            //set document ID to user ID
            db.collection("users").document(uid.toString())
                .set(user, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Loaded to Database", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }

            //explicit intent to Meal Suggestion Activity
            val intent = Intent(this,MealSuggestionActivity::class.java)
            startActivity(intent)
        }//end of function

    }

    // extension function to set edit text multiple input types
    // allow to input positive and negative decimal and integer numbers
    fun TextInputEditText.inputNumbersOnly(){
        inputType =
            InputType.TYPE_CLASS_NUMBER or // allow numbers
                    InputType.TYPE_NUMBER_FLAG_DECIMAL // allow decimal numbers

    }

}

